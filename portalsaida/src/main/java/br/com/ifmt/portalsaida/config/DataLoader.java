package br.com.ifmt.portalsaida.config;

import br.com.ifmt.portalsaida.model.*;
import br.com.ifmt.portalsaida.repository.*;
import br.com.ifmt.portalsaida.security.SenhaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalTime;

/**
 * Carga inicial de DADOS DE EXEMPLO (só roda se o banco estiver vazio).
 * Substitua pelos departamentos, cursos, turmas e horários reais do campus.
 */
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired private DepartamentoRepository departamentoRepository;
    @Autowired private CursoRepository cursoRepository;
    @Autowired private TurmaRepository turmaRepository;
    @Autowired private AlunoRepository alunoRepository;
    @Autowired private HorarioAulaRepository horarioRepository;
    @Autowired private UsuarioRepository usuarioRepository;

    private static final LocalTime[][] AULAS = {
        {LocalTime.of(7, 30), LocalTime.of(8, 20)}, {LocalTime.of(8, 20), LocalTime.of(9, 10)},
        {LocalTime.of(9, 30), LocalTime.of(10, 20)}, {LocalTime.of(10, 20), LocalTime.of(11, 10)},
        {LocalTime.of(11, 10), LocalTime.of(12, 0)}
    };
    private static final String[] DISCIPLINAS = {"Matemática", "Português", "Física", "História", "Biologia"};

    @Override
    public void run(String... args) {
        if (departamentoRepository.count() > 0) return;

        Departamento info = dep("Departamento de Informática", "DEINF");
        Departamento edif = dep("Departamento de Edificações", "DEEDF");
        Curso cInfo = curso("Técnico em Informática Integrado ao Ensino Médio", info);
        Curso cEdif = curso("Técnico em Edificações Integrado ao Ensino Médio", edif);
        Turma i1 = turma("INFO 1A", 1, cInfo);
        Turma i2 = turma("INFO 2A", 2, cInfo);
        Turma e1 = turma("EDIF 1A", 1, cEdif);

        String[] nomes = {"Ana Souza", "Bruno Lima", "Carla Dias"};
        int n = 1;
        for (Turma t : new Turma[]{i1, i2, e1})
            for (String nome : nomes) aluno(nome + " (" + t.getNome() + ")", "2026" + String.format("%04d", n++), t);

        for (Turma t : new Turma[]{i1, i2, e1}) horariosPadrao(t);

        usuario("Administrador", "admin", "admin123", Perfil.ADMIN, null);
        usuario("Coordenador Informática", "coord.info", "123456", Perfil.COORDENADOR, info);
        usuario("Secretário Informática", "sec.info", "123456", Perfil.SECRETARIO, info);
        usuario("Secretário Edificações", "sec.edif", "123456", Perfil.SECRETARIO, edif);
    }

    private Departamento dep(String nome, String sigla) {
        Departamento d = new Departamento(); d.setNome(nome); d.setSigla(sigla); return departamentoRepository.save(d);
    }
    private Curso curso(String nome, Departamento d) {
        Curso c = new Curso(); c.setNome(nome); c.setDepartamento(d); return cursoRepository.save(c);
    }
    private Turma turma(String nome, int serie, Curso c) {
        Turma t = new Turma(); t.setNome(nome); t.setSerie(serie); t.setCurso(c); return turmaRepository.save(t);
    }
    private void aluno(String nome, String matricula, Turma t) {
        Aluno a = new Aluno(); a.setNome(nome); a.setMatricula(matricula); a.setTurma(t); alunoRepository.save(a);
    }
    private void horariosPadrao(Turma t) {
        DiaSemana[] dias = {DiaSemana.SEGUNDA, DiaSemana.TERCA, DiaSemana.QUARTA, DiaSemana.QUINTA, DiaSemana.SEXTA};
        for (int d = 0; d < dias.length; d++)
            for (int s = 0; s < AULAS.length; s++) {
                HorarioAula h = new HorarioAula();
                h.setTurma(t); h.setDiaSemana(dias[d]);
                h.setHoraInicio(AULAS[s][0]); h.setHoraFim(AULAS[s][1]);
                h.setDisciplina(DISCIPLINAS[(d + s) % DISCIPLINAS.length]);
                h.setProfessor("Professor(a) de " + h.getDisciplina());
                horarioRepository.save(h);
            }
    }
    private void usuario(String nome, String login, String senha, Perfil p, Departamento d) {
        Usuario u = new Usuario(); u.setNome(nome); u.setLogin(login); u.setSenhaHash(SenhaUtil.hash(senha));
        u.setPerfil(p); u.setDepartamento(d); u.setAtivo(true); usuarioRepository.save(u);
    }
}
