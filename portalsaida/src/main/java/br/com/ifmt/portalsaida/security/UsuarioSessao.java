package br.com.ifmt.portalsaida.security;

import br.com.ifmt.portalsaida.exception.NegocioException;
import br.com.ifmt.portalsaida.model.Perfil;
import java.io.Serializable;

/** Dados do usuário logado, guardados na HttpSession. */
public class UsuarioSessao implements Serializable {
    private final Long id;
    private final String nome;
    private final Perfil perfil;
    private final Long departamentoId;
    private final String departamentoNome;

    public UsuarioSessao(Long id, String nome, Perfil perfil, Long departamentoId, String departamentoNome) {
        this.id = id; this.nome = nome; this.perfil = perfil;
        this.departamentoId = departamentoId; this.departamentoNome = departamentoNome;
    }
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public Perfil getPerfil() { return perfil; }
    public Long getDepartamentoId() { return departamentoId; }
    public String getDepartamentoNome() { return departamentoNome; }

    public boolean isAdmin() { return perfil == Perfil.ADMIN; }
    public boolean isCoordenador() { return perfil == Perfil.COORDENADOR; }
    public boolean isSecretario() { return perfil == Perfil.SECRETARIO; }
    /** Cadastros (cursos, turmas, alunos, horários). */
    public boolean isPodeGerenciarCadastros() { return isAdmin() || isCoordenador(); }
    /** Autorizar e cancelar liberações. */
    public boolean isPodeAutorizar() { return isAdmin() || isSecretario(); }

    public boolean temAcessoAoDepartamento(Long idDepartamento) {
        return isAdmin() || (departamentoId != null && departamentoId.equals(idDepartamento));
    }
    public void exigirDepartamento(Long idDepartamento) {
        if (!temAcessoAoDepartamento(idDepartamento))
            throw new NegocioException("Você não tem permissão para operar em outro departamento.");
    }
}
