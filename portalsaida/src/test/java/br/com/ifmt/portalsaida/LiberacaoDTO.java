package br.com.ifmt.portalsaida.DTO;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalTime;
import java.time.LocalDateTime;
import br.com.ifmt.portalsaida.model.*;

public class LiberacaoDTO {

    private Long id;

    @NotNull(message = "Selecione o tipo de liberação")
    private TipoLiberacao tipo;

    @NotNull(message = "Selecione a turma")
    private Long turmaId;

    private Long alunoId;

    @NotNull(message = "Selecione o motivo")
    private MotivoLiberacao motivo;

    @NotBlank(message = "Informe a justificativa")
    @Size(min = 10, max = 500, message = "A justificativa deve ter entre 10 e 500 caracteres")
    private String justificativa;

    @NotBlank(message = "Informe a comprovação apresentada")
    @Size(max = 500, message = "A comprovação deve ter no máximo 500 caracteres")
    private String comprovante;

    @NotNull(message = "Informe a data e hora da saída")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dataHoraSaida;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public TipoLiberacao getTipo() { return tipo; }
    public void setTipo(TipoLiberacao tipo) { this.tipo = tipo; }

    public Long getTurmaId() { return turmaId; }
    public void setTurmaId(Long turmaId) { this.turmaId = turmaId; }

    public Long getAlunoId() { return alunoId; }
    public void setAlunoId(Long alunoId) { this.alunoId = alunoId; }

    public MotivoLiberacao getMotivo() { return motivo; }
    public void setMotivo(MotivoLiberacao motivo) { this.motivo = motivo; }

    public String getJustificativa() { return justificativa; }
    public void setJustificativa(String justificativa) { this.justificativa = justificativa; }

    public String getComprovante() { return comprovante; }
    public void setComprovante(String comprovante) { this.comprovante = comprovante; }

    public LocalDateTime getDataHoraSaida() { return dataHoraSaida; }
    public void setDataHoraSaida(LocalDateTime dataHoraSaida) { this.dataHoraSaida = dataHoraSaida; }

}
