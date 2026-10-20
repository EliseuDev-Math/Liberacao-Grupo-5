package br.com.ifmt.portalsaida.repository;

import br.com.ifmt.portalsaida.model.*;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LiberacaoRepository extends JpaRepository<Liberacao, Long> {
    List<Liberacao> findAllByOrderByDataHoraSaidaDesc();
    List<Liberacao> findByTurmaCursoDepartamentoIdOrderByDataHoraSaidaDesc(Long departamentoId);
    boolean existsByTurmaIdAndTipoAndStatusAndDataHoraSaidaBetween(Long turmaId, TipoLiberacao tipo, StatusLiberacao status, LocalDateTime inicio, LocalDateTime fim);
    boolean existsByAlunoIdAndStatusAndDataHoraSaidaBetween(Long alunoId, StatusLiberacao status, LocalDateTime inicio, LocalDateTime fim);
    boolean existsByTurmaId(Long turmaId);
    boolean existsByAlunoId(Long alunoId);
    boolean existsByAutorizadoPorId(Long usuarioId);
}
