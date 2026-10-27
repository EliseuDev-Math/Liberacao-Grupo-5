package br.com.ifmt.portalsaida.DTO;

import jakarta.validation.constraints.*;

public class CursoDTO {

    private Long id;

    @NotBlank(message = "Informe o nome do curso")
    private String nome;

    @NotNull(message = "Selecione o departamento")
    private Long departamentoId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Long getDepartamentoId() { return departamentoId; }
    public void setDepartamentoId(Long departamentoId) { this.departamentoId = departamentoId; }

}
