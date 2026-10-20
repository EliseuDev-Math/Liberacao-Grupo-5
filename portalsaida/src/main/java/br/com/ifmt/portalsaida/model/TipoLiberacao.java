package br.com.ifmt.portalsaida.model;

public enum TipoLiberacao {
    TURMA("Turma inteira"),
    ALUNO("Aluno individual");

    private final String descricao;
    TipoLiberacao(String descricao) { this.descricao = descricao; }
    public String getDescricao() { return descricao; }
}
