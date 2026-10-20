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
public class DepartamentoService {
    @Autowired private DepartamentoRepository departamentoRepository;
    @Autowired private CursoRepository cursoRepository;
    @Autowired private UsuarioRepository usuarioRepository;

    public List<Departamento> listarTodos() { return departamentoRepository.findAll(Sort.by("nome")); }

    /** Admin vê todos; os demais perfis veem só o seu departamento. */
    public List<Departamento> listarPermitidos(UsuarioSessao u) {
        if (u.isAdmin()) return listarTodos();
        if (u.getDepartamentoId() == null) return List.of();
        return departamentoRepository.findById(u.getDepartamentoId()).map(List::of).orElse(List.of());
    }

    public Departamento buscar(Long id) {
        return departamentoRepository.findById(id).orElseThrow(() -> new NegocioException("Departamento não encontrado."));
    }

    public DepartamentoDTO buscarDTO(Long id) {
        Departamento d = buscar(id);
        DepartamentoDTO dto = new DepartamentoDTO();
        dto.setId(d.getId()); dto.setNome(d.getNome()); dto.setSigla(d.getSigla());
        return dto;
    }

    @Transactional
    public void salvar(DepartamentoDTO dto) {
        departamentoRepository.findByNomeIgnoreCase(dto.getNome().trim())
                .filter(x -> !x.getId().equals(dto.getId()))
                .ifPresent(x -> { throw new NegocioException("Já existe um departamento com esse nome."); });
        Departamento d = dto.getId() == null ? new Departamento() : buscar(dto.getId());
        d.setNome(dto.getNome().trim());
        d.setSigla(dto.getSigla().trim().toUpperCase());
        departamentoRepository.save(d);
    }

    @Transactional
    public void excluir(Long id) {
        if (cursoRepository.existsByDepartamentoId(id))
            throw new NegocioException("Não é possível excluir: o departamento possui cursos.");
        if (usuarioRepository.existsByDepartamentoId(id))
            throw new NegocioException("Não é possível excluir: o departamento possui usuários.");
        departamentoRepository.delete(buscar(id));
    }
}
