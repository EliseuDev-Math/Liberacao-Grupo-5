package br.com.ifmt.portalsaida.service;

import br.com.ifmt.portalsaida.DTO.LiberacaoDTO;
import br.com.ifmt.portalsaida.exception.NegocioException;
import br.com.ifmt.portalsaida.model.*;
import br.com.ifmt.portalsaida.repository.*;
import br.com.ifmt.portalsaida.security.UsuarioSessao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/** Testes unitários das regras de negócio da liberação (sem banco, usando Mockito). */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class LiberacaoServiceTest {

    @Mock LiberacaoRepository liberacaoRepository;
    @Mock TurmaRepository turmaRepository;
    @Mock AlunoRepository alunoRepository;
    @Mock HorarioAulaRepository horarioRepository;
    @Mock UsuarioRepository usuarioRepository;
    @InjectMocks LiberacaoService service;

    Turma turma; Aluno aluno; Usuario autor;
    UsuarioSessao secretarioInfo;
    LocalDate segunda;

    @BeforeEach
    void preparar() {
        Departamento dep = new Departamento(); dep.setId(10L);
        Curso curso = new Curso(); curso.setId(1L); curso.setDepartamento(dep);
        turma = new Turma(); turma.setId(5L); turma.setNome("INFO 1A"); turma.setCurso(curso);
        aluno = new Aluno(); aluno.setId(7L); aluno.setTurma(turma);
        autor = new Usuario(); autor.setId(1L);
        secretarioInfo = new UsuarioSessao(1L, "Sec", Perfil.SECRETARIO, 10L, "Informática");
        segunda = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));

        HorarioAula aula = new HorarioAula();
        aula.setHoraInicio(LocalTime.of(7, 30)); aula.setHoraFim(LocalTime.of(12, 0));

        when(turmaRepository.findById(5L)).thenReturn(Optional.of(turma));
        when(alunoRepository.findById(7L)).thenReturn(Optional.of(aluno));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(autor));
        when(horarioRepository.findByTurmaIdAndDiaSemanaOrderByHoraInicioAsc(5L, DiaSemana.SEGUNDA)).thenReturn(List.of(aula));
        when(liberacaoRepository.save(any(Liberacao.class))).thenAnswer(i -> i.getArgument(0));
    }

    private LiberacaoDTO dto(TipoLiberacao tipo, LocalTime hora) {
        LiberacaoDTO d = new LiberacaoDTO();
        d.setTipo(tipo); d.setTurmaId(5L); d.setMotivo(MotivoLiberacao.AUSENCIA_PROFESSOR);
        d.setJustificativa("Professor ausente por doença"); d.setComprovante("Ofício 12/2026");
        d.setDataHoraSaida(segunda.atTime(hora));
        if (tipo == TipoLiberacao.ALUNO) d.setAlunoId(7L);
        return d;
    }

    @Test
    void autorizaSaidaDaTurmaDentroDoHorario() {
        Liberacao l = service.autorizar(dto(TipoLiberacao.TURMA, LocalTime.of(10, 0)), secretarioInfo);
        assertEquals(StatusLiberacao.AUTORIZADA, l.getStatus());
        assertEquals(turma, l.getTurma());
        verify(liberacaoRepository).save(any(Liberacao.class));
    }

    @Test
    void autorizaSaidaIndividualDeAluno() {
        Liberacao l = service.autorizar(dto(TipoLiberacao.ALUNO, LocalTime.of(10, 0)), secretarioInfo);
        assertEquals(aluno, l.getAluno());
    }

    @Test
    void bloqueiaTurmaDeOutroDepartamento() {
        UsuarioSessao outro = new UsuarioSessao(2L, "Sec Edif", Perfil.SECRETARIO, 99L, "Edificações");
        assertThrows(NegocioException.class, () -> service.autorizar(dto(TipoLiberacao.TURMA, LocalTime.of(10, 0)), outro));
        verify(liberacaoRepository, never()).save(any());
    }

    @Test
    void bloqueiaSaidaForaDoPeriodoDeAulas() {
        assertThrows(NegocioException.class, () -> service.autorizar(dto(TipoLiberacao.TURMA, LocalTime.of(13, 0)), secretarioInfo));
    }

    @Test
    void bloqueiaAlunoDeOutraTurma() {
        Turma outraTurma = new Turma(); outraTurma.setId(6L);
        aluno.setTurma(outraTurma);
        assertThrows(NegocioException.class, () -> service.autorizar(dto(TipoLiberacao.ALUNO, LocalTime.of(10, 0)), secretarioInfo));
    }

    @Test
    void bloqueiaQuandoTurmaJaFoiLiberadaNoDia() {
        when(liberacaoRepository.existsByTurmaIdAndTipoAndStatusAndDataHoraSaidaBetween(
                anyLong(), any(), any(), any(), any())).thenReturn(true);
        assertThrows(NegocioException.class, () -> service.autorizar(dto(TipoLiberacao.ALUNO, LocalTime.of(10, 0)), secretarioInfo));
    }

    @Test
    void coordenadorNaoPodeAutorizar() {
        UsuarioSessao coord = new UsuarioSessao(3L, "Coord", Perfil.COORDENADOR, 10L, "Informática");
        assertThrows(NegocioException.class, () -> service.autorizar(dto(TipoLiberacao.TURMA, LocalTime.of(10, 0)), coord));
    }
}
