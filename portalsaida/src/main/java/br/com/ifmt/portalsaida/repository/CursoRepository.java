package br.com.ifmt.portalsaida.repository;

import br.com.ifmt.portalsaida.model.*;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
    List<Curso> findByDepartamentoId(Long departamentoId, Sort sort);
    boolean existsByDepartamentoId(Long departamentoId);
}
