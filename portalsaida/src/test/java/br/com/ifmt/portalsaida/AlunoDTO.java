package br.com.ifmt.portalsaida.DTO;

import jakarta.validation.constraints.*;

public class AlunoDTO {

    private Long id;

    @NotBlank(message = "Informe o nome do aluno")
    private String nome;

    @NotBlank(message = "Informe a matrícula")
    private String matricula;

    @NotNull(message = "Selecione a turma")
    private Long turmaId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    public Long getTurmaId() { return turmaId; }
    public void setTurmaId(Long turmaId) { this.turmaId = turmaId; }

}
