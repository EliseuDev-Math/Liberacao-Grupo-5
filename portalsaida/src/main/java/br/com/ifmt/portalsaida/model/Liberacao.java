package br.com.ifmt.portalsaida.model;

import jakarta.persistence.*;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
public class Liberacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoLiberacao tipo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "turma_id")
    private Turma turma;

    @ManyToOne
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;

    @ManyToOne(optional = false)
    @JoinColumn(name = "autorizado_por_id")
    private Usuario autorizadoPor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MotivoLiberacao motivo;

    @Column(nullable = false, length = 500)
    private String justificativa;

    @Column(nullable = false, length = 500)
    private String comprovante;

    @Column(nullable = false)
    private LocalDateTime dataHoraSaida;

    @Column(nullable = false)
    private LocalDateTime dataHoraRegistro;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusLiberacao status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public TipoLiberacao getTipo() { return tipo; }
    public void setTipo(TipoLiberacao tipo) { this.tipo = tipo; }

    public Turma getTurma() { return turma; }
    public void setTurma(Turma turma) { this.turma = turma; }

    public Aluno getAluno() { return aluno; }
    public void setAluno(Aluno aluno) { this.aluno = aluno; }

    public Usuario getAutorizadoPor() { return autorizadoPor; }
    public void setAutorizadoPor(Usuario autorizadoPor) { this.autorizadoPor = autorizadoPor; }

    public MotivoLiberacao getMotivo() { return motivo; }
    public void setMotivo(MotivoLiberacao motivo) { this.motivo = motivo; }

    public String getJustificativa() { return justificativa; }
    public void setJustificativa(String justificativa) { this.justificativa = justificativa; }

    public String getComprovante() { return comprovante; }
    public void setComprovante(String comprovante) { this.comprovante = comprovante; }

    public LocalDateTime getDataHoraSaida() { return dataHoraSaida; }
    public void setDataHoraSaida(LocalDateTime dataHoraSaida) { this.dataHoraSaida = dataHoraSaida; }

    public LocalDateTime getDataHoraRegistro() { return dataHoraRegistro; }
    public void setDataHoraRegistro(LocalDateTime dataHoraRegistro) { this.dataHoraRegistro = dataHoraRegistro; }

    public StatusLiberacao getStatus() { return status; }
    public void setStatus(StatusLiberacao status) { this.status = status; }

}
