import { useEffect, useMemo, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { ClipboardList, Send, AlertTriangle, UserRound } from 'lucide-react';
import PageHeader from '../components/PageHeader';
import { HORARIOS_PADRAO, MOTIVOS_PADRAO } from '../lib/data';
import { getAluno, saveRequisicao, gerarId, gerarProtocolo } from '../lib/storage';
import type { Requisicao as RequisicaoType } from '../lib/types';

function todayISO() {
  return new Date().toISOString().slice(0, 10);
}

export default function Requisicao() {
  const navigate = useNavigate();
  const aluno = getAluno();

  const horarioPadrao = useMemo(() => (aluno ? HORARIOS_PADRAO[aluno.turno] : undefined), [aluno]);

  const [data, setData] = useState(todayISO());
  const [horarioSaida, setHorarioSaida] = useState('');
  const [motivo, setMotivo] = useState(MOTIVOS_PADRAO[0]);
  const [observacoes, setObservacoes] = useState('');
  const [responsavel, setResponsavel] = useState(aluno?.responsavel ?? '');
  const [telefoneResponsavel, setTelefoneResponsavel] = useState(aluno?.telefoneResponsavel ?? '');
  const [erro, setErro] = useState('');

  useEffect(() => {
    if (horarioPadrao && !horarioSaida) {
      // sugere meia hora antes do horário normal de saída
      const [h, m] = horarioPadrao.saida.split(':').map(Number);
      const total = h * 60 + m - 30;
      const hh = String(Math.floor(total / 60)).padStart(2, '0');
      const mm = String(total % 60).padStart(2, '0');
      setHorarioSaida(`${hh}:${mm}`);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [horarioPadrao]);

  if (!aluno) {
    return (
      <div>
        <PageHeader
          icon={<ClipboardList size={14} />}
          eyebrow="Passo 2 de 3"
          title="Página da requisição"
          description="Solicite a saída antecipada informando data, horário e motivo."
        />
        <div className="mx-auto max-w-2xl px-4 py-16 text-center md:px-6">
          <AlertTriangle className="mx-auto mb-4 text-amber-500" size={40} />
          <h2 className="font-serif text-xl font-bold text-emerald-950">Cadastro necessário</h2>
          <p className="mt-2 text-sm text-slate-600">
            Antes de abrir uma requisição, cadastre os dados do(a) estudante na página do usuário.
          </p>
          <Link to="/usuario" className="mt-6 inline-flex items-center gap-2 rounded-full bg-emerald-800 px-6 py-3 text-sm font-bold text-white hover:bg-emerald-700">
            <UserRound size={16} /> Ir para cadastro do usuário
          </Link>
        </div>
      </div>
    );
  }

  function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setErro('');
    if (!data) return setErro('Informe a data da saída.');
    if (!horarioSaida) return setErro('Informe o horário de saída pretendido.');
    if (horarioPadrao && horarioSaida >= horarioPadrao.saida) {
      return setErro(`O horário de saída deve ser anterior ao horário normal (${horarioPadrao.saida}).`);
    }
    if (motivo.startsWith('Outro') && !observacoes.trim()) {
      return setErro('Descreva o motivo no campo de observações.');
    }

    const req: RequisicaoType = {
      id: gerarId(),
      protocolo: gerarProtocolo(),
      matriculaAluno: aluno!.matricula,
      nomeAluno: aluno!.nome,
      cpfAluno: aluno!.cpf,
      departamento: aluno!.departamento,
      curso: aluno!.curso,
      turma: aluno!.turma,
      turno: aluno!.turno,
      data,
      horarioNormal: horarioPadrao?.saida ?? '',
      horarioSaida,
      motivo,
      observacoes,
      responsavel,
      telefoneResponsavel,
      status: 'pendente',
      criadoEm: new Date().toISOString(),
    };
    saveRequisicao(req);
    navigate(`/autorizacao/${req.id}`);
  }

  return (
    <div>
      <PageHeader
        icon={<ClipboardList size={14} />}
        eyebrow="Passo 2 de 3"
        title="Página da requisição"
        description="Preencha os dados da saída antecipada. Ao enviar, um protocolo será gerado e a autorização ficará pendente de aprovação da coordenação/professor(a)."
      />

      <div className="mx-auto max-w-3xl px-4 py-10 md:px-6">
        <div className="mb-6 rounded-2xl border border-emerald-900/10 bg-white p-5 shadow-sm">
          <p className="text-xs font-semibold uppercase tracking-wide text-emerald-700">Estudante identificado</p>
          <div className="mt-2 grid gap-x-6 gap-y-1 text-sm text-slate-700 sm:grid-cols-2">
            <p><strong>{aluno.nome}</strong></p>
            <p>Matrícula: {aluno.matricula}</p>
            <p>{aluno.curso}</p>
            <p>Turma {aluno.turma} · {aluno.turno}</p>
          </div>
          <Link to="/usuario" className="mt-3 inline-block text-xs font-semibold text-emerald-700 underline">Editar dados cadastrais</Link>
        </div>

        <form onSubmit={handleSubmit} className="space-y-6 rounded-2xl border border-emerald-900/10 bg-white p-6 shadow-sm md:p-8">
          <div className="grid gap-5 sm:grid-cols-3">
            <Field label="Data da saída">
              <input type="date" className="input" min={todayISO()} value={data} onChange={(e) => setData(e.target.value)} />
            </Field>
            <Field label={`Horário normal (${aluno.turno})`}>
              <input className="input bg-slate-100" value={horarioPadrao?.saida ?? ''} readOnly />
            </Field>
            <Field label="Horário de saída pretendido">
              <input type="time" className="input" value={horarioSaida} onChange={(e) => setHorarioSaida(e.target.value)} />
            </Field>
          </div>

          <Field label="Motivo da saída antecipada">
            <select className="input" value={motivo} onChange={(e) => setMotivo(e.target.value)}>
              {MOTIVOS_PADRAO.map((m) => (
                <option key={m} value={m}>{m}</option>
              ))}
            </select>
          </Field>

          <Field label="Observações (opcional, obrigatório se motivo = Outro)">
            <textarea className="input min-h-24" value={observacoes} onChange={(e) => setObservacoes(e.target.value)} placeholder="Detalhe informações adicionais relevantes..." />
          </Field>

          <div className="grid gap-5 sm:grid-cols-2">
            <Field label="Responsável pela retirada (se aplicável)">
              <input className="input" value={responsavel} onChange={(e) => setResponsavel(e.target.value)} placeholder="Nome do responsável" />
            </Field>
            <Field label="Telefone do responsável">
              <input className="input" value={telefoneResponsavel} onChange={(e) => setTelefoneResponsavel(e.target.value)} placeholder="(65) 90000-0000" />
            </Field>
          </div>

          {erro && (
            <p className="flex items-center gap-2 rounded-lg bg-red-50 px-4 py-3 text-sm font-medium text-red-700">
              <AlertTriangle size={16} /> {erro}
            </p>
          )}

          <div className="flex justify-end border-t border-slate-100 pt-6">
            <button type="submit" className="inline-flex items-center gap-2 rounded-full bg-amber-400 px-6 py-3 text-sm font-bold text-emerald-950 shadow transition hover:bg-amber-300">
              <Send size={16} /> Enviar requisição
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

function Field({ label, children }: { label: string; children: React.ReactNode }) {
  return (
    <label className="block">
      <span className="mb-1.5 block text-sm font-semibold text-slate-700">{label}</span>
      {children}
    </label>
  );
}
