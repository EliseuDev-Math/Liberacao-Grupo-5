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
@RequestMapping("/cursos")
public class CursoController {

    @Autowired private CursoService cursoService;
    @Autowired private DepartamentoService departamentoService;

    private void carregarCombos(Model model, UsuarioSessao u) {
        model.addAttribute("departamentos", departamentoService.listarPermitidos(u));
    }

    @GetMapping
    public String listar(@SessionAttribute("usuario") UsuarioSessao u, Model model) {
        model.addAttribute("cursos", cursoService.listar(u));
        return "curso/lista";
    }

    @GetMapping("/novo")
    public String novo(@SessionAttribute("usuario") UsuarioSessao u, Model model) {
        CursoDTO dto = new CursoDTO();
        if (!u.isAdmin()) dto.setDepartamentoId(u.getDepartamentoId());
        model.addAttribute("curso", dto);
        carregarCombos(model, u);
        return "curso/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, @SessionAttribute("usuario") UsuarioSessao u, Model model) {
        model.addAttribute("curso", cursoService.buscarDTO(id, u));
        carregarCombos(model, u);
        return "curso/form";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("curso") CursoDTO dto, BindingResult result,
                         @SessionAttribute("usuario") UsuarioSessao u, Model model) {
        if (result.hasErrors()) {
        carregarCombos(model, u);
            return "curso/form";
        }
        cursoService.salvar(dto, u);
        return "redirect:/cursos";
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, @SessionAttribute("usuario") UsuarioSessao u) {
        cursoService.excluir(id, u);
        return "redirect:/cursos";
    }
}
