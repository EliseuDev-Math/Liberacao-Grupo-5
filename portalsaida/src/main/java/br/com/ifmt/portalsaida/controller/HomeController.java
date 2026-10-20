package br.com.ifmt.portalsaida.controller;

import br.com.ifmt.portalsaida.security.UsuarioSessao;
import br.com.ifmt.portalsaida.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
public class HomeController {

    @Autowired private LiberacaoService liberacaoService;
    @Autowired private TurmaService turmaService;
    @Autowired private AlunoService alunoService;

    @GetMapping("/")
    public String index(@SessionAttribute("usuario") UsuarioSessao u, Model model) {
        model.addAttribute("liberacoesHoje", liberacaoService.listarHoje(u));
        model.addAttribute("totalTurmas", turmaService.listar(u).size());
        model.addAttribute("totalAlunos", alunoService.listar(u, null).size());
        return "index";
    }
}
