package br.com.ifmt.portalsaida.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

/** Exige login e aplica as permissões por perfil, de acordo com a URL. */
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        String ctx = req.getContextPath();
        String path = req.getRequestURI().substring(ctx.length());
        HttpSession session = req.getSession(false);
        UsuarioSessao u = session == null ? null : (UsuarioSessao) session.getAttribute("usuario");

        if (u == null) { res.sendRedirect(ctx + "/login"); return false; }

        // "leitura" = GET que não abre formulário de criação/edição
        boolean leitura = "GET".equals(req.getMethod())
                && !path.endsWith("/novo") && !path.endsWith("/nova") && !path.contains("/editar/");

        boolean permitido = true;
        if (path.startsWith("/departamentos") || path.startsWith("/usuarios")) {
            permitido = u.isAdmin();
        } else if (path.startsWith("/cursos") || path.startsWith("/turmas")
                || path.startsWith("/alunos") || path.startsWith("/horarios")) {
            permitido = leitura || u.isPodeGerenciarCadastros();
        } else if (path.startsWith("/liberacoes")) {
            permitido = leitura || u.isPodeAutorizar();
        }
        if (!permitido) { res.sendRedirect(ctx + "/acesso-negado"); return false; }
        return true;
    }
}
