package br.com.ifmt.portalsaida.service;

import br.com.ifmt.portalsaida.DTO.*;
import br.com.ifmt.portalsaida.exception.NegocioException;
import br.com.ifmt.portalsaida.model.*;
import br.com.ifmt.portalsaida.repository.*;
import br.com.ifmt.portalsaida.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.*;
import java.util.*;

@Service
public class UsuarioService {
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private LiberacaoRepository liberacaoRepository;
    @Autowired private DepartamentoService departamentoService;

    public List<Usuario> listar() { return usuarioRepository.findAll(Sort.by("nome")); }

    public Usuario buscar(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new NegocioException("Usuário não encontrado."));
    }

    public UsuarioDTO buscarDTO(Long id) {
        Usuario x = buscar(id);
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(x.getId()); dto.setNome(x.getNome()); dto.setLogin(x.getLogin()); dto.setPerfil(x.getPerfil());
        dto.setDepartamentoId(x.getDepartamento() == null ? null : x.getDepartamento().getId());
        dto.setAtivo(x.isAtivo());
        return dto; // a senha nunca volta para a tela
    }

    @Transactional
    public void salvar(UsuarioDTO dto) {
        if (dto.getPerfil() != Perfil.ADMIN && dto.getDepartamentoId() == null)
            throw new NegocioException("Coordenador e secretário precisam de um departamento.");
        usuarioRepository.findByLogin(dto.getLogin().trim())
                .filter(x -> !x.getId().equals(dto.getId()))
                .ifPresent(x -> { throw new NegocioException("Esse login já está em uso."); });

        Usuario x = dto.getId() == null ? new Usuario() : buscar(dto.getId());
        boolean senhaInformada = dto.getSenha() != null && !dto.getSenha().isBlank();
        if (dto.getId() == null && !senhaInformada) throw new NegocioException("Informe a senha do novo usuário.");
        if (senhaInformada) {
            if (dto.getSenha().length() < 6) throw new NegocioException("A senha deve ter ao menos 6 caracteres.");
            x.setSenhaHash(SenhaUtil.hash(dto.getSenha()));
        }
        x.setNome(dto.getNome().trim());
        x.setLogin(dto.getLogin().trim());
        x.setPerfil(dto.getPerfil());
        x.setAtivo(dto.isAtivo());
        x.setDepartamento(dto.getPerfil() == Perfil.ADMIN ? null : departamentoService.buscar(dto.getDepartamentoId()));
        usuarioRepository.save(x);
    }

    @Transactional
    public void excluir(Long id, UsuarioSessao logado) {
        if (id.equals(logado.getId())) throw new NegocioException("Você não pode excluir o próprio usuário.");
        if (liberacaoRepository.existsByAutorizadoPorId(id))
            throw new NegocioException("Esse usuário possui liberações registradas. Desmarque \"Ativo\" em vez de excluir.");
        usuarioRepository.delete(buscar(id));
    }
}
