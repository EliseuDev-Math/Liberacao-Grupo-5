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
public class AuthService {
    @Autowired private UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public UsuarioSessao autenticar(String login, String senha) {
        Usuario u = usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new NegocioException("Login ou senha inválidos."));
        if (!u.isAtivo() || !SenhaUtil.hash(senha).equals(u.getSenhaHash()))
            throw new NegocioException("Login ou senha inválidos.");
        Departamento d = u.getDepartamento();
        return new UsuarioSessao(u.getId(), u.getNome(), u.getPerfil(),
                d == null ? null : d.getId(), d == null ? null : d.getNome());
    }
}
