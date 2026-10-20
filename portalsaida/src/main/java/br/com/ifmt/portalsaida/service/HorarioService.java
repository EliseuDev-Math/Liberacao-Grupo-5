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
public class HorarioService {
    @Autowired private HorarioAulaRepository horarioRepository;
    @Autowired private TurmaService turmaService;

    public List<HorarioAula> listar(UsuarioSessao u, Long turmaId) {
        Sort ordem = Sort.by("turma.nome", "diaSemana", "horaInicio");
        List<HorarioAula> lista = u.isAdmin() ? horarioRepository.findAll(ordem)
                : horarioRepository.findByTurmaCursoDepartamentoId(u.getDepartamentoId(), ordem);
        if (turmaId != null) lista = lista.stream().filter(h -> h.getTurma().getId().equals(turmaId)).toList();
        return lista;
    }

    public HorarioAula buscar(Long id, UsuarioSessao u) {
        HorarioAula h = horarioRepository.findById(id).orElseThrow(() -> new NegocioException("Horário não encontrado."));
        u.exigirDepartamento(h.getTurma().getCurso().getDepartamento().getId());
        return h;
    }

    public HorarioAulaDTO buscarDTO(Long id, UsuarioSessao u) {
        HorarioAula h = buscar(id, u);
        HorarioAulaDTO dto = new HorarioAulaDTO();
        dto.setId(h.getId()); dto.setTurmaId(h.getTurma().getId()); dto.setDiaSemana(h.getDiaSemana());
        dto.setHoraInicio(h.getHoraInicio()); dto.setHoraFim(h.getHoraFim());
        dto.setDisciplina(h.getDisciplina()); dto.setProfessor(h.getProfessor());
        return dto;
    }

    @Transactional
    public void salvar(HorarioAulaDTO dto, UsuarioSessao u) {
        Turma turma = turmaService.buscar(dto.getTurmaId(), u);
        if (!dto.getHoraFim().isAfter(dto.getHoraInicio()))
            throw new NegocioException("A hora final deve ser depois da hora inicial.");
        for (HorarioAula outro : horarioRepository.findByTurmaIdAndDiaSemanaOrderByHoraInicioAsc(turma.getId(), dto.getDiaSemana())) {
            if (outro.getId().equals(dto.getId())) continue;
            if (dto.getHoraInicio().isBefore(outro.getHoraFim()) && dto.getHoraFim().isAfter(outro.getHoraInicio()))
                throw new NegocioException("Conflito: já existe a aula \"" + outro.getDisciplina() + "\" (" + outro.getHoraInicio() + "–" + outro.getHoraFim() + ") nesse dia.");
        }
        HorarioAula h = dto.getId() == null ? new HorarioAula() : buscar(dto.getId(), u);
        h.setTurma(turma);
        h.setDiaSemana(dto.getDiaSemana());
        h.setHoraInicio(dto.getHoraInicio());
        h.setHoraFim(dto.getHoraFim());
        h.setDisciplina(dto.getDisciplina().trim());
        h.setProfessor(dto.getProfessor() == null ? null : dto.getProfessor().trim());
        horarioRepository.save(h);
    }

    @Transactional
    public void excluir(Long id, UsuarioSessao u) {
        horarioRepository.delete(buscar(id, u));
    }
}
