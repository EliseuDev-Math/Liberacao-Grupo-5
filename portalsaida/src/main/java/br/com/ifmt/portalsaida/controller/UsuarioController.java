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
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired private UsuarioService usuarioService;
    @Autowired private DepartamentoService departamentoService;

    private void carregarCombos(Model model, UsuarioSessao u) {
        model.addAttribute("perfis", Perfil.values());
        model.addAttribute("departamentos", departamentoService.listarTodos());
    }

    @GetMapping
    public String listar(@SessionAttribute("usuario") UsuarioSessao u, Model model) {
        model.addAttribute("usuarios", usuarioService.listar());
        return "usuario/lista";
    }

    @GetMapping("/novo")
    public String novo(@SessionAttribute("usuario") UsuarioSessao u, Model model) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setAtivo(true);
        model.addAttribute("usuario", dto);
        carregarCombos(model, u);
        return "usuario/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, @SessionAttribute("usuario") UsuarioSessao u, Model model) {
        model.addAttribute("usuario", usuarioService.buscarDTO(id));
        carregarCombos(model, u);
        return "usuario/form";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("usuario") UsuarioDTO dto, BindingResult result,
                         @SessionAttribute("usuario") UsuarioSessao u, Model model) {
        if (result.hasErrors()) {
        carregarCombos(model, u);
            return "usuario/form";
        }
        usuarioService.salvar(dto);
        return "redirect:/usuarios";
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, @SessionAttribute("usuario") UsuarioSessao u) {
        usuarioService.excluir(id, u);
        return "redirect:/usuarios";
    }
}
