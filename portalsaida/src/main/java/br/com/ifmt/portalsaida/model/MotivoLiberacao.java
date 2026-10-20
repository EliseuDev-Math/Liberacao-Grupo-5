package br.com.ifmt.portalsaida.model;

public enum MotivoLiberacao {
    AUSENCIA_PROFESSOR("Ausência de professor"),
    CONSULTA_MEDICA("Consulta/atestado médico"),
    EMERGENCIA_FAMILIAR("Emergência familiar"),
    PROBLEMA_ESTRUTURAL("Problema estrutural/falta de energia ou água"),
    EVENTO_INSTITUCIONAL("Evento institucional"),
    OUTRO("Outro");

    private final String descricao;
    MotivoLiberacao(String descricao) { this.descricao = descricao; }
    public String getDescricao() { return descricao; }
}
