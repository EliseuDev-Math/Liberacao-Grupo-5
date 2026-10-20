package br.com.ifmt.portalsaida.DTO;

import jakarta.validation.constraints.*;

public class DepartamentoDTO {

    private Long id;

    @NotBlank(message = "Informe o nome do departamento")
    private String nome;

    @NotBlank(message = "Informe a sigla")
    @Size(max = 10, message = "A sigla deve ter no máximo 10 caracteres")
    private String sigla;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getSigla() { return sigla; }
    public void setSigla(String sigla) { this.sigla = sigla; }

}
