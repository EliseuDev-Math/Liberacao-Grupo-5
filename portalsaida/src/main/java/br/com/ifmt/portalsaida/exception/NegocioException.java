package br.com.ifmt.portalsaida.exception;

/** Erro de regra de negócio: a mensagem é mostrada ao usuário. */
public class NegocioException extends RuntimeException {
    public NegocioException(String mensagem) { super(mensagem); }
}
