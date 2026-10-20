package br.com.ifmt.portalsaida.controller;

import br.com.ifmt.portalsaida.DTO.*;
import br.com.ifmt.portalsaida.model.*;
import br.com.ifmt.portalsaida.security.UsuarioSessao;
import br.com.ifmt.portalsaida.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/turmas")
public class TurmaController {

    @Autowired private TurmaService turmaService;
    @Autowired private CursoService cursoService;

    private void carregarCombos(Model model, UsuarioSessao u) {
        model.addAttribute("cursos", cursoService.listar(u));
    }

    @GetMapping
    public String listar(@SessionAttribute("usuario") UsuarioSessao u, Model model) {
        model.addAttribute("turmas", turmaService.listar(u));
        return "turma/lista";
    }

    @GetMapping("/novo")
    public String novo(@SessionAttribute("usuario") UsuarioSessao u, Model model) {
        TurmaDTO dto = new TurmaDTO();
        model.addAttribute("turma", dto);
        carregarCombos(model, u);
        return "turma/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, @SessionAttribute("usuario") UsuarioSessao u, Model model) {
        model.addAttribute("turma", turmaService.buscarDTO(id, u));
        carregarCombos(model, u);
        return "turma/form";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("turma") TurmaDTO dto, BindingResult result,
                         @SessionAttribute("usuario") UsuarioSessao u, Model model) {
        if (result.hasErrors()) {
        carregarCombos(model, u);
            return "turma/form";
        }
        turmaService.salvar(dto, u);
        return "redirect:/turmas";
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, @SessionAttribute("usuario") UsuarioSessao u) {
        turmaService.excluir(id, u);
        return "redirect:/turmas";
    }
}
