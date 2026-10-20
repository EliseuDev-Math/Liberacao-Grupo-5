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
public class CursoService {
    @Autowired private CursoRepository cursoRepository;
    @Autowired private TurmaRepository turmaRepository;
    @Autowired private DepartamentoService departamentoService;

    public List<Curso> listar(UsuarioSessao u) {
        Sort ordem = Sort.by("nome");
        return u.isAdmin() ? cursoRepository.findAll(ordem)
                : cursoRepository.findByDepartamentoId(u.getDepartamentoId(), ordem);
    }

    public Curso buscar(Long id, UsuarioSessao u) {
        Curso c = cursoRepository.findById(id).orElseThrow(() -> new NegocioException("Curso não encontrado."));
        u.exigirDepartamento(c.getDepartamento().getId());
        return c;
    }

    public CursoDTO buscarDTO(Long id, UsuarioSessao u) {
        Curso c = buscar(id, u);
        CursoDTO dto = new CursoDTO();
        dto.setId(c.getId()); dto.setNome(c.getNome()); dto.setDepartamentoId(c.getDepartamento().getId());
        return dto;
    }

    @Transactional
    public void salvar(CursoDTO dto, UsuarioSessao u) {
        Long depId = u.isAdmin() ? dto.getDepartamentoId() : u.getDepartamentoId();
        u.exigirDepartamento(depId);
        Curso c = dto.getId() == null ? new Curso() : buscar(dto.getId(), u);
        c.setNome(dto.getNome().trim());
        c.setDepartamento(departamentoService.buscar(depId));
        cursoRepository.save(c);
    }

    @Transactional
    public void excluir(Long id, UsuarioSessao u) {
        Curso c = buscar(id, u);
        if (turmaRepository.existsByCursoId(id))
            throw new NegocioException("Não é possível excluir: o curso possui turmas.");
        cursoRepository.delete(c);
    }
}
