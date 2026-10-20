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
public class TurmaService {
    @Autowired private TurmaRepository turmaRepository;
    @Autowired private AlunoRepository alunoRepository;
    @Autowired private HorarioAulaRepository horarioRepository;
    @Autowired private LiberacaoRepository liberacaoRepository;
    @Autowired private CursoService cursoService;

    public List<Turma> listar(UsuarioSessao u) {
        Sort ordem = Sort.by("nome");
        return u.isAdmin() ? turmaRepository.findAll(ordem)
                : turmaRepository.findByCursoDepartamentoId(u.getDepartamentoId(), ordem);
    }

    public Turma buscar(Long id, UsuarioSessao u) {
        Turma t = turmaRepository.findById(id).orElseThrow(() -> new NegocioException("Turma não encontrada."));
        u.exigirDepartamento(t.getCurso().getDepartamento().getId());
        return t;
    }

    public TurmaDTO buscarDTO(Long id, UsuarioSessao u) {
        Turma t = buscar(id, u);
        TurmaDTO dto = new TurmaDTO();
        dto.setId(t.getId()); dto.setNome(t.getNome()); dto.setSerie(t.getSerie()); dto.setCursoId(t.getCurso().getId());
        return dto;
    }

    @Transactional
    public void salvar(TurmaDTO dto, UsuarioSessao u) {
        Curso curso = cursoService.buscar(dto.getCursoId(), u);
        Turma t = dto.getId() == null ? new Turma() : buscar(dto.getId(), u);
        t.setNome(dto.getNome().trim());
        t.setSerie(dto.getSerie());
        t.setCurso(curso);
        turmaRepository.save(t);
    }

    @Transactional
    public void excluir(Long id, UsuarioSessao u) {
        Turma t = buscar(id, u);
        if (alunoRepository.existsByTurmaId(id)) throw new NegocioException("Não é possível excluir: a turma possui alunos.");
        if (horarioRepository.existsByTurmaId(id)) throw new NegocioException("Não é possível excluir: a turma possui horários cadastrados.");
        if (liberacaoRepository.existsByTurmaId(id)) throw new NegocioException("Não é possível excluir: a turma possui liberações registradas.");
        turmaRepository.delete(t);
    }
}
