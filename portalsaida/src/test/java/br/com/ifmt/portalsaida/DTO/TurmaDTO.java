package br.com.ifmt.portalsaida.DTO;

import jakarta.validation.constraints.*;

public class TurmaDTO {

    private Long id;

    @NotBlank(message = "Informe o nome da turma")
    private String nome;

    @NotNull(message = "Informe a série")
    @Min(value = 1, message = "Série mínima: 1")
    @Max(value = 4, message = "Série máxima: 4")
    private Integer serie;

    @NotNull(message = "Selecione o curso")
    private Long cursoId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Integer getSerie() { return serie; }
    public void setSerie(Integer serie) { this.serie = serie; }

    public Long getCursoId() { return cursoId; }
    public void setCursoId(Long cursoId) { this.cursoId = cursoId; }

}
