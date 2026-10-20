package br.com.ifmt.portalsaida.controller;

import br.com.ifmt.portalsaida.exception.NegocioException;
import br.com.ifmt.portalsaida.security.UsuarioSessao;
import br.com.ifmt.portalsaida.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    @Autowired private AuthService authService;

    @GetMapping("/login")
    public String formLogin(HttpSession session) {
        return session.getAttribute("usuario") != null ? "redirect:/" : "login";
    }

    @PostMapping("/login")
    public String entrar(@RequestParam String login, @RequestParam String senha, HttpSession session, Model model) {
        try {
            UsuarioSessao u = authService.autenticar(login.trim(), senha);
            session.setAttribute("usuario", u);
            return "redirect:/";
        } catch (NegocioException e) {
            model.addAttribute("erro", e.getMessage());
            return "login";
        }
    }

    @GetMapping("/logout")
    public String sair(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/acesso-negado")
    public String acessoNegado() { return "acesso-negado"; }
}
