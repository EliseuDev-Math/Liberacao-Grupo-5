package br.com.ifmt.portalsaida.model;

import java.time.DayOfWeek;
import java.time.LocalDate;

public enum DiaSemana {
    SEGUNDA("Segunda-feira", DayOfWeek.MONDAY),
    TERCA("Terça-feira", DayOfWeek.TUESDAY),
    QUARTA("Quarta-feira", DayOfWeek.WEDNESDAY),
    QUINTA("Quinta-feira", DayOfWeek.THURSDAY),
    SEXTA("Sexta-feira", DayOfWeek.FRIDAY),
    SABADO("Sábado", DayOfWeek.SATURDAY);

    private final String descricao;
    private final DayOfWeek dayOfWeek;

    DiaSemana(String descricao, DayOfWeek dayOfWeek) { this.descricao = descricao; this.dayOfWeek = dayOfWeek; }
    public String getDescricao() { return descricao; }

    /** Converte uma data no dia da semana do sistema. Retorna null para domingo. */
    public static DiaSemana de(LocalDate data) {
        for (DiaSemana d : values()) if (d.dayOfWeek == data.getDayOfWeek()) return d;
        return null;
    }
}
