package br.com.ifmt.portalsaida.model;

public enum Perfil {
    ADMIN("Administrador"),
    COORDENADOR("Coordenador"),
    SECRETARIO("Secretário");

    private final String descricao;
    Perfil(String descricao) { this.descricao = descricao; }
    public String getDescricao() { return descricao; }
}
