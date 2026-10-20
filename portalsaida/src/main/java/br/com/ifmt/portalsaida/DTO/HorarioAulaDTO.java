package br.com.ifmt.portalsaida.DTO;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalTime;
import java.time.LocalDateTime;
import br.com.ifmt.portalsaida.model.*;

public class HorarioAulaDTO {

    private Long id;

    @NotNull(message = "Selecione a turma")
    private Long turmaId;

    @NotNull(message = "Selecione o dia da semana")
    private DiaSemana diaSemana;

    @NotNull(message = "Informe a hora inicial")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime horaInicio;

    @NotNull(message = "Informe a hora final")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime horaFim;

    @NotBlank(message = "Informe a disciplina")
    private String disciplina;

    private String professor;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getTurmaId() { return turmaId; }
    public void setTurmaId(Long turmaId) { this.turmaId = turmaId; }

    public DiaSemana getDiaSemana() { return diaSemana; }
    public void setDiaSemana(DiaSemana diaSemana) { this.diaSemana = diaSemana; }

    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }

    public LocalTime getHoraFim() { return horaFim; }
    public void setHoraFim(LocalTime horaFim) { this.horaFim = horaFim; }

    public String getDisciplina() { return disciplina; }
    public void setDisciplina(String disciplina) { this.disciplina = disciplina; }

    public String getProfessor() { return professor; }
    public void setProfessor(String professor) { this.professor = professor; }

}
