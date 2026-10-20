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
@RequestMapping("/departamentos")
public class DepartamentoController {

    @Autowired private DepartamentoService departamentoService;

    @GetMapping
    public String listar(@SessionAttribute("usuario") UsuarioSessao u, Model model) {
        model.addAttribute("departamentos", departamentoService.listarTodos());
        return "departamento/lista";
    }

    @GetMapping("/novo")
    public String novo(@SessionAttribute("usuario") UsuarioSessao u, Model model) {
        DepartamentoDTO dto = new DepartamentoDTO();
        model.addAttribute("departamento", dto);
        return "departamento/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, @SessionAttribute("usuario") UsuarioSessao u, Model model) {
        model.addAttribute("departamento", departamentoService.buscarDTO(id));
        return "departamento/form";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("departamento") DepartamentoDTO dto, BindingResult result,
                         @SessionAttribute("usuario") UsuarioSessao u, Model model) {
        if (result.hasErrors()) {
            return "departamento/form";
        }
        departamentoService.salvar(dto);
        return "redirect:/departamentos";
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, @SessionAttribute("usuario") UsuarioSessao u) {
        departamentoService.excluir(id);
        return "redirect:/departamentos";
    }
}
