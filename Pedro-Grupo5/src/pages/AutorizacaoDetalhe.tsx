import { useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Printer, ArrowLeft, CheckCircle2, XCircle, ShieldAlert, Clock3, FileWarning } from 'lucide-react';
import PageHeader from '../components/PageHeader';
import StatusBadge from '../components/StatusBadge';
import SignatureBlock from '../components/SignatureBlock';
import CodeStamp from '../components/CodeStamp';
import { CAMPUS, getDepartamentoPorCurso } from '../lib/data';
import { decidirRequisicao, getRequisicaoById } from '../lib/storage';
import type { Requisicao } from '../lib/types';

function formatDate(iso: string) {
  if (!iso) return '-';
  const [y, m, d] = iso.split('-');
  return `${d}/${m}/${y}`;
}

function formatDateTime(iso?: string) {
  if (!iso) return '-';
  const d = new Date(iso);
  return d.toLocaleString('pt-BR');
}

export default function AutorizacaoDetalhe() {
  const { id } = useParams();
  const [, force] = useState(0);
  const req = id ? getRequisicaoById(id) : undefined;
  const [justificativa, setJustificativa] = useState('');

  if (!req) {
    return (
      <div>
        <PageHeader icon={<FileWarning size={14} />} eyebrow="Autorização" title="Documento não encontrado" />
        <div className="mx-auto max-w-2xl px-4 py-16 text-center md:px-6">
          <p className="text-slate-600">Nenhuma requisição foi encontrada com este identificador.</p>
          <Link to="/autorizacoes" className="mt-6 inline-flex items-center gap-2 rounded-full bg-emerald-800 px-6 py-3 text-sm font-bold text-white hover:bg-emerald-700">
            <ArrowLeft size={16} /> Voltar para autorizações
          </Link>
        </div>
      </div>
    );
  }

  return <DocumentoView req={req} justificativa={justificativa} setJustificativa={setJustificativa} onDecidir={() => force((n) => n + 1)} />;
}

function DocumentoView({
  req,
  justificativa,
  setJustificativa,
  onDecidir,
}: {
  req: Requisicao;
  justificativa: string;
  setJustificativa: (v: string) => void;
  onDecidir: () => void;
}) {
  const departamento = getDepartamentoPorCurso(req.curso);

  function decidir(status: 'aprovado' | 'recusado') {
    decidirRequisicao(req.id, status, departamento?.coordenador ?? CAMPUS.diretorGeral, justificativa);
    onDecidir();
  }

  return (
    <div>
      <PageHeader
        icon={<ShieldAlert size={14} />}
        eyebrow="Documento oficial"
        title="Página da autorização"
        description="Documento gerado automaticamente a partir da requisição, pronto para conferência e impressão."
      />

      <div className="mx-auto max-w-4xl px-4 py-10 md:px-6">
        <div className="mb-5 flex flex-wrap items-center justify-between gap-3 print:hidden">
          <Link to="/autorizacoes" className="inline-flex items-center gap-1.5 text-sm font-semibold text-emerald-700 hover:underline">
            <ArrowLeft size={15} /> Voltar à lista
          </Link>
          <button
            onClick={() => window.print()}
            className="inline-flex items-center gap-2 rounded-full bg-emerald-800 px-5 py-2.5 text-sm font-bold text-white hover:bg-emerald-700"
          >
            <Printer size={16} /> Imprimir / salvar PDF
          </button>
        </div>

        <article className="rounded-2xl border border-emerald-900/10 bg-white p-8 shadow-sm print:rounded-none print:border-0 print:shadow-none md:p-10">
          <header className="flex flex-col items-center gap-4 border-b-2 border-emerald-900 pb-6 text-center">
            <img src="/logo.png" alt="Brasão" className="h-16 w-16 object-contain" />
            <div>
              <p className="text-xs font-semibold uppercase tracking-widest text-slate-500">{CAMPUS.instituicao}</p>
              <p className="text-xs font-semibold uppercase tracking-widest text-slate-500">{CAMPUS.campus}</p>
              <h1 className="mt-2 font-serif text-2xl font-bold text-emerald-950 md:text-3xl">
                Autorização de Saída Antecipada
              </h1>
            </div>
          </header>

          <div className="mt-6 flex flex-wrap items-center justify-between gap-4">
            <div className="text-sm text-slate-600">
              <p><strong>Protocolo:</strong> <span className="font-mono">{req.protocolo}</span></p>
              <p><strong>Emitido em:</strong> {formatDateTime(req.criadoEm)}</p>
            </div>
            <StatusBadge status={req.status} />
          </div>

          <section className="mt-8 grid gap-6 sm:grid-cols-2">
            <div>
              <h2 className="mb-2 text-xs font-bold uppercase tracking-wide text-emerald-700">Dados do(a) estudante</h2>
              <dl className="space-y-1 text-sm text-slate-700">
                <Row label="Nome" value={req.nomeAluno} />
                <Row label="Matrícula" value={req.matriculaAluno} />
                <Row label="CPF" value={req.cpfAluno} />
                <Row label="Curso" value={req.curso} />
                <Row label="Turma / turno" value={`${req.turma} · ${req.turno}`} />
                <Row label="Departamento" value={departamento ? `${departamento.nome} (${departamento.sigla})` : req.departamento} />
              </dl>
            </div>
            <div>
              <h2 className="mb-2 text-xs font-bold uppercase tracking-wide text-emerald-700">Dados da saída solicitada</h2>
              <dl className="space-y-1 text-sm text-slate-700">
                <Row label="Data" value={formatDate(req.data)} />
                <Row label="Horário normal de saída" value={req.horarioNormal || '-'} />
                <Row label="Horário de saída pretendido" value={req.horarioSaida} highlight />
                <Row label="Responsável pela retirada" value={req.responsavel || 'Não se aplica'} />
                <Row label="Telefone do responsável" value={req.telefoneResponsavel || '-'} />
              </dl>
            </div>
          </section>

          <section className="mt-6 rounded-xl bg-amber-50 p-4">
            <h2 className="mb-1 text-xs font-bold uppercase tracking-wide text-amber-800">Motivo declarado</h2>
            <p className="text-sm text-amber-950">{req.motivo}</p>
            {req.observacoes && <p className="mt-2 text-sm text-amber-900"><strong>Observações:</strong> {req.observacoes}</p>}
          </section>

          {req.status !== 'pendente' && (
            <section className="mt-6 rounded-xl border border-slate-200 p-4 text-sm text-slate-600">
              <p><strong>Decisão registrada por:</strong> {req.decididoPor}</p>
              <p><strong>Em:</strong> {formatDateTime(req.decididoEm)}</p>
              {req.justificativaDecisao && <p className="mt-1"><strong>Justificativa:</strong> {req.justificativaDecisao}</p>}
            </section>
          )}

          <section className="mt-10">
            <h2 className="mb-6 text-center text-xs font-bold uppercase tracking-wide text-emerald-700">Assinaturas</h2>
            <div className="grid gap-8 sm:grid-cols-2">
              <SignatureBlock name={req.nomeAluno} role="Assinatura do(a) estudante" />
              <SignatureBlock
                name={req.responsavel || '—'}
                role="Assinatura do(a) responsável"
                filled={Boolean(req.responsavel)}
              />
              <SignatureBlock
                name={departamento?.professorResponsavel ?? '—'}
                role={departamento?.tituloProfessor ?? 'Professor(a) responsável'}
                filled={req.status === 'aprovado'}
              />
              <SignatureBlock
                name={departamento?.coordenador ?? CAMPUS.diretorGeral}
                role={departamento?.tituloCoordenador ?? 'Coordenação'}
                filled={req.status === 'aprovado'}
              />
            </div>
          </section>

          <footer className="mt-10 flex flex-wrap items-end justify-between gap-6 border-t border-dashed border-slate-300 pt-6">
            <p className="max-w-sm text-xs text-slate-400">
              Documento gerado eletronicamente pelo sistema de Autorização de Saída Antecipada do {CAMPUS.sigla} – {CAMPUS.campus}.
              A validade está condicionada à assinatura física ou eletrônica dos responsáveis indicados.
            </p>
            <div className="flex flex-col items-center gap-1">
              <CodeStamp value={req.protocolo} />
              <span className="text-[10px] font-mono text-slate-400">{req.protocolo}</span>
            </div>
          </footer>
        </article>

        {req.status === 'pendente' && (
          <div className="mt-6 rounded-2xl border border-emerald-900/10 bg-white p-6 shadow-sm print:hidden">
            <div className="mb-4 flex items-center gap-2">
              <Clock3 className="text-amber-500" size={18} />
              <h2 className="font-serif text-lg font-bold text-emerald-950">Ação da coordenação / professor(a)</h2>
            </div>
            <p className="mb-3 text-sm text-slate-500">
              Área reservada para o(a) responsável do departamento registrar a decisão sobre esta solicitação.
            </p>
            <textarea
              className="input mb-4 min-h-20"
              placeholder="Justificativa da decisão (opcional)"
              value={justificativa}
              onChange={(e) => setJustificativa(e.target.value)}
            />
            <div className="flex flex-wrap gap-3">
              <button onClick={() => decidir('aprovado')} className="inline-flex items-center gap-2 rounded-full bg-emerald-700 px-5 py-2.5 text-sm font-bold text-white hover:bg-emerald-600">
                <CheckCircle2 size={16} /> Aprovar saída
              </button>
              <button onClick={() => decidir('recusado')} className="inline-flex items-center gap-2 rounded-full border border-red-300 bg-red-50 px-5 py-2.5 text-sm font-bold text-red-700 hover:bg-red-100">
                <XCircle size={16} /> Indeferir
              </button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}

function Row({ label, value, highlight }: { label: string; value: string; highlight?: boolean }) {
  return (
    <div className="flex justify-between gap-3 border-b border-dashed border-slate-100 py-1">
      <dt className="text-slate-400">{label}</dt>
      <dd className={highlight ? 'font-bold text-emerald-800' : 'font-medium text-slate-800'}>{value}</dd>
    </div>
  );
}
