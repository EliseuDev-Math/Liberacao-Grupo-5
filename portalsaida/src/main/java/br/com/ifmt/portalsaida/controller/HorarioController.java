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
@RequestMapping("/horarios")
public class HorarioController {

    @Autowired private HorarioService horarioService;
    @Autowired private TurmaService turmaService;

    private void carregarCombos(Model model, UsuarioSessao u) {
        model.addAttribute("turmas", turmaService.listar(u));
        model.addAttribute("dias", DiaSemana.values());
    }

    @GetMapping
    public String listar(@SessionAttribute("usuario") UsuarioSessao u, Model model, @RequestParam(required = false) Long turmaId) {
        model.addAttribute("horarios", horarioService.listar(u, turmaId));
        model.addAttribute("turmas", turmaService.listar(u));
        model.addAttribute("turmaId", turmaId);
        return "horario/lista";
    }

    @GetMapping("/novo")
    public String novo(@SessionAttribute("usuario") UsuarioSessao u, Model model) {
        HorarioAulaDTO dto = new HorarioAulaDTO();
        model.addAttribute("horario", dto);
        carregarCombos(model, u);
        return "horario/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, @SessionAttribute("usuario") UsuarioSessao u, Model model) {
        model.addAttribute("horario", horarioService.buscarDTO(id, u));
        carregarCombos(model, u);
        return "horario/form";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("horario") HorarioAulaDTO dto, BindingResult result,
                         @SessionAttribute("usuario") UsuarioSessao u, Model model) {
        if (result.hasErrors()) {
        carregarCombos(model, u);
            return "horario/form";
        }
        horarioService.salvar(dto, u);
        return "redirect:/horarios";
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, @SessionAttribute("usuario") UsuarioSessao u) {
        horarioService.excluir(id, u);
        return "redirect:/horarios";
    }
}
