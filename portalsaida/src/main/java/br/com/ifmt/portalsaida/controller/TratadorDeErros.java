package br.com.ifmt.portalsaida.controller;

import br.com.ifmt.portalsaida.exception.NegocioException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/** Mostra qualquer NegocioException numa página amigável (templates/erro.html). */
@ControllerAdvice
public class TratadorDeErros {
    @ExceptionHandler(NegocioException.class)
    public ModelAndView negocio(NegocioException e) {
        ModelAndView mv = new ModelAndView("erro");
        mv.addObject("mensagem", e.getMessage());
        return mv;
    }
}
