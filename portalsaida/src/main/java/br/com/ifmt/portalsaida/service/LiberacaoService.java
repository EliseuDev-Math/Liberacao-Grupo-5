package br.com.ifmt.portalsaida.service;

import br.com.ifmt.portalsaida.DTO.*;
import br.com.ifmt.portalsaida.exception.NegocioException;
import br.com.ifmt.portalsaida.model.*;
import br.com.ifmt.portalsaida.repository.*;
import br.com.ifmt.portalsaida.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.*;
import java.util.*;

@Service
public class LiberacaoService {
    @Autowired private LiberacaoRepository liberacaoRepository;
    @Autowired private TurmaRepository turmaRepository;
    @Autowired private AlunoRepository alunoRepository;
    @Autowired private HorarioAulaRepository horarioRepository;
    @Autowired private UsuarioRepository usuarioRepository;

    public List<Liberacao> listar(UsuarioSessao u, LocalDate data) {
        List<Liberacao> lista = u.isAdmin() ? liberacaoRepository.findAllByOrderByDataHoraSaidaDesc()
                : liberacaoRepository.findByTurmaCursoDepartamentoIdOrderByDataHoraSaidaDesc(u.getDepartamentoId());
        if (data != null) lista = lista.stream().filter(l -> l.getDataHoraSaida().toLocalDate().equals(data)).toList();
        return lista;
    }

    /** Saídas autorizadas de hoje, em ordem de horário (útil para a portaria). */
    public List<Liberacao> listarHoje(UsuarioSessao u) {
        return listar(u, LocalDate.now()).stream()
                .filter(l -> l.getStatus() == StatusLiberacao.AUTORIZADA)
                .sorted(Comparator.comparing(Liberacao::getDataHoraSaida))
                .toList();
    }

    public Liberacao buscar(Long id, UsuarioSessao u) {
        Liberacao l = liberacaoRepository.findById(id).orElseThrow(() -> new NegocioException("Liberação não encontrada."));
        u.exigirDepartamento(l.getTurma().getCurso().getDepartamento().getId());
        return l;
    }

    @Transactional
    public Liberacao autorizar(LiberacaoDTO dto, UsuarioSessao u) {
        if (!u.isPodeAutorizar()) throw new NegocioException("Seu perfil não pode autorizar saídas.");

        Turma turma = turmaRepository.findById(dto.getTurmaId()).orElseThrow(() -> new NegocioException("Turma não encontrada."));
        u.exigirDepartamento(turma.getCurso().getDepartamento().getId());

        Aluno aluno = null;
        if (dto.getTipo() == TipoLiberacao.ALUNO) {
            if (dto.getAlunoId() == null) throw new NegocioException("Selecione o aluno.");
            aluno = alunoRepository.findById(dto.getAlunoId()).orElseThrow(() -> new NegocioException("Aluno não encontrado."));
            if (!aluno.getTurma().getId().equals(turma.getId()))
                throw new NegocioException("O aluno não pertence à turma selecionada.");
        }

        LocalDateTime saida = dto.getDataHoraSaida();
        if (saida.toLocalDate().isBefore(LocalDate.now()))
            throw new NegocioException("A data da saída não pode estar no passado.");
        validarHorario(turma, saida);
        validarDuplicidade(turma, aluno, dto.getTipo(), saida.toLocalDate());

        Usuario autor = usuarioRepository.findById(u.getId()).orElseThrow(() -> new NegocioException("Usuário não encontrado."));
        Liberacao l = new Liberacao();
        l.setTipo(dto.getTipo());
        l.setTurma(turma);
        l.setAluno(aluno);
        l.setMotivo(dto.getMotivo());
        l.setJustificativa(dto.getJustificativa().trim());
        l.setComprovante(dto.getComprovante().trim());
        l.setDataHoraSaida(saida);
        l.setDataHoraRegistro(LocalDateTime.now());
        l.setStatus(StatusLiberacao.AUTORIZADA);
        l.setAutorizadoPor(autor);
        return liberacaoRepository.save(l);
    }

    @Transactional
    public void cancelar(Long id, UsuarioSessao u) {
        if (!u.isPodeAutorizar()) throw new NegocioException("Seu perfil não pode cancelar liberações.");
        Liberacao l = buscar(id, u);
        if (l.getStatus() != StatusLiberacao.AUTORIZADA) throw new NegocioException("Essa liberação já foi cancelada.");
        l.setStatus(StatusLiberacao.CANCELADA);
        liberacaoRepository.save(l);
    }

    /** A saída precisa cair dentro do período de aulas da turma naquele dia. */
    private void validarHorario(Turma turma, LocalDateTime saida) {
        DiaSemana dia = DiaSemana.de(saida.toLocalDate());
        List<HorarioAula> aulas = dia == null ? List.of()
                : horarioRepository.findByTurmaIdAndDiaSemanaOrderByHoraInicioAsc(turma.getId(), dia);
        if (aulas.isEmpty()) throw new NegocioException("A turma não possui aulas cadastradas nesse dia da semana.");
        LocalTime inicio = aulas.get(0).getHoraInicio();
        LocalTime fim = aulas.get(aulas.size() - 1).getHoraFim();
        LocalTime hora = saida.toLocalTime();
        if (hora.isBefore(inicio) || !hora.isBefore(fim))
            throw new NegocioException("Horário fora do período de aulas da turma (" + inicio + " às " + fim + ").");
    }

    private void validarDuplicidade(Turma turma, Aluno aluno, TipoLiberacao tipo, LocalDate data) {
        LocalDateTime ini = data.atStartOfDay();
        LocalDateTime fim = data.atTime(LocalTime.MAX);
        if (liberacaoRepository.existsByTurmaIdAndTipoAndStatusAndDataHoraSaidaBetween(
                turma.getId(), TipoLiberacao.TURMA, StatusLiberacao.AUTORIZADA, ini, fim))
            throw new NegocioException("Essa turma já possui uma saída coletiva autorizada nesta data.");
        if (tipo == TipoLiberacao.ALUNO && liberacaoRepository.existsByAlunoIdAndStatusAndDataHoraSaidaBetween(
                aluno.getId(), StatusLiberacao.AUTORIZADA, ini, fim))
            throw new NegocioException("Esse aluno já possui uma saída autorizada nesta data.");
    }
}
