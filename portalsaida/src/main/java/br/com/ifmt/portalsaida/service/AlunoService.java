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
public class AlunoService {
    @Autowired private AlunoRepository alunoRepository;
    @Autowired private LiberacaoRepository liberacaoRepository;
    @Autowired private TurmaService turmaService;

    public List<Aluno> listar(UsuarioSessao u, Long turmaId) {
        Sort ordem = Sort.by("nome");
        List<Aluno> lista = u.isAdmin() ? alunoRepository.findAll(ordem)
                : alunoRepository.findByTurmaCursoDepartamentoId(u.getDepartamentoId(), ordem);
        if (turmaId != null) lista = lista.stream().filter(a -> a.getTurma().getId().equals(turmaId)).toList();
        return lista;
    }

    public Aluno buscar(Long id, UsuarioSessao u) {
        Aluno a = alunoRepository.findById(id).orElseThrow(() -> new NegocioException("Aluno não encontrado."));
        u.exigirDepartamento(a.getTurma().getCurso().getDepartamento().getId());
        return a;
    }

    public AlunoDTO buscarDTO(Long id, UsuarioSessao u) {
        Aluno a = buscar(id, u);
        AlunoDTO dto = new AlunoDTO();
        dto.setId(a.getId()); dto.setNome(a.getNome()); dto.setMatricula(a.getMatricula()); dto.setTurmaId(a.getTurma().getId());
        return dto;
    }

    @Transactional
    public void salvar(AlunoDTO dto, UsuarioSessao u) {
        Turma turma = turmaService.buscar(dto.getTurmaId(), u);
        alunoRepository.findByMatricula(dto.getMatricula().trim())
                .filter(x -> !x.getId().equals(dto.getId()))
                .ifPresent(x -> { throw new NegocioException("Já existe um aluno com essa matrícula."); });
        Aluno a = dto.getId() == null ? new Aluno() : buscar(dto.getId(), u);
        a.setNome(dto.getNome().trim());
        a.setMatricula(dto.getMatricula().trim());
        a.setTurma(turma);
        alunoRepository.save(a);
    }

    @Transactional
    public void excluir(Long id, UsuarioSessao u) {
        Aluno a = buscar(id, u);
        if (liberacaoRepository.existsByAlunoId(id))
            throw new NegocioException("Não é possível excluir: o aluno possui liberações registradas.");
        alunoRepository.delete(a);
    }
}
