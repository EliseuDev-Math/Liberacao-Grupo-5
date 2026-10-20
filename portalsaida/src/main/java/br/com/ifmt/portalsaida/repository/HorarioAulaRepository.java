package br.com.ifmt.portalsaida.repository;

import br.com.ifmt.portalsaida.model.*;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface HorarioAulaRepository extends JpaRepository<HorarioAula, Long> {
    List<HorarioAula> findByTurmaCursoDepartamentoId(Long departamentoId, Sort sort);
    List<HorarioAula> findByTurmaIdAndDiaSemanaOrderByHoraInicioAsc(Long turmaId, DiaSemana dia);
    boolean existsByTurmaId(Long turmaId);
}
