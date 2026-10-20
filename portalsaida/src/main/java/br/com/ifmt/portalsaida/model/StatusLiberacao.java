package br.com.ifmt.portalsaida.model;

public enum StatusLiberacao {
    AUTORIZADA("Autorizada"),
    CANCELADA("Cancelada");

    private final String descricao;
    StatusLiberacao(String descricao) { this.descricao = descricao; }
    public String getDescricao() { return descricao; }
}
