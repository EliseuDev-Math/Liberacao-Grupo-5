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
@RequestMapping("/alunos")
public class AlunoController {

    @Autowired private AlunoService alunoService;
    @Autowired private TurmaService turmaService;

    private void carregarCombos(Model model, UsuarioSessao u) {
        model.addAttribute("turmas", turmaService.listar(u));
    }

    @GetMapping
    public String listar(@SessionAttribute("usuario") UsuarioSessao u, Model model, @RequestParam(required = false) Long turmaId) {
        model.addAttribute("alunos", alunoService.listar(u, turmaId));
        model.addAttribute("turmas", turmaService.listar(u));
        model.addAttribute("turmaId", turmaId);
        return "aluno/lista";
    }

    @GetMapping("/novo")
    public String novo(@SessionAttribute("usuario") UsuarioSessao u, Model model) {
        AlunoDTO dto = new AlunoDTO();
        model.addAttribute("aluno", dto);
        carregarCombos(model, u);
        return "aluno/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, @SessionAttribute("usuario") UsuarioSessao u, Model model) {
        model.addAttribute("aluno", alunoService.buscarDTO(id, u));
        carregarCombos(model, u);
        return "aluno/form";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("aluno") AlunoDTO dto, BindingResult result,
                         @SessionAttribute("usuario") UsuarioSessao u, Model model) {
        if (result.hasErrors()) {
        carregarCombos(model, u);
            return "aluno/form";
        }
        alunoService.salvar(dto, u);
        return "redirect:/alunos";
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, @SessionAttribute("usuario") UsuarioSessao u) {
        alunoService.excluir(id, u);
        return "redirect:/alunos";
    }
}
