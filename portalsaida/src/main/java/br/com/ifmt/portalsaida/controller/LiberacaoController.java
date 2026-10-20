package br.com.ifmt.portalsaida.controller;

import br.com.ifmt.portalsaida.DTO.LiberacaoDTO;
import br.com.ifmt.portalsaida.exception.NegocioException;
import br.com.ifmt.portalsaida.model.*;
import br.com.ifmt.portalsaida.security.UsuarioSessao;
import br.com.ifmt.portalsaida.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/liberacoes")
public class LiberacaoController {

    @Autowired private LiberacaoService liberacaoService;
    @Autowired private TurmaService turmaService;
    @Autowired private AlunoService alunoService;

    private void carregarCombos(Model model, UsuarioSessao u) {
        model.addAttribute("tipos", TipoLiberacao.values());
        model.addAttribute("motivos", MotivoLiberacao.values());
        model.addAttribute("turmas", turmaService.listar(u));
        model.addAttribute("alunos", alunoService.listar(u, null));
    }

    @GetMapping
    public String listar(@SessionAttribute("usuario") UsuarioSessao u, Model model,
                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        model.addAttribute("liberacoes", liberacaoService.listar(u, data));
        model.addAttribute("data", data);
        return "liberacao/lista";
    }

    @GetMapping("/nova")
    public String nova(@SessionAttribute("usuario") UsuarioSessao u, Model model) {
        LiberacaoDTO dto = new LiberacaoDTO();
        dto.setTipo(TipoLiberacao.TURMA);
        dto.setDataHoraSaida(LocalDateTime.now().withSecond(0).withNano(0));
        model.addAttribute("liberacao", dto);
        carregarCombos(model, u);
        return "liberacao/form";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("liberacao") LiberacaoDTO dto, BindingResult result,
                         @SessionAttribute("usuario") UsuarioSessao u, Model model) {
        if (!result.hasErrors()) {
            try {
                Liberacao l = liberacaoService.autorizar(dto, u);
                return "redirect:/liberacoes/" + l.getId();
            } catch (NegocioException e) {
                result.reject("negocio", e.getMessage()); // volta ao formulário sem perder o que foi digitado
            }
        }
        carregarCombos(model, u);
        return "liberacao/form";
    }

    @GetMapping("/{id}")
    public String detalhe(@PathVariable Long id, @SessionAttribute("usuario") UsuarioSessao u, Model model) {
        model.addAttribute("liberacao", liberacaoService.buscar(id, u));
        return "liberacao/detalhe";
    }

    @PostMapping("/cancelar/{id}")
    public String cancelar(@PathVariable Long id, @SessionAttribute("usuario") UsuarioSessao u) {
        liberacaoService.cancelar(id, u);
        return "redirect:/liberacoes/" + id;
    }
}
