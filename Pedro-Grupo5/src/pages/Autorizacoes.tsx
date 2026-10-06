import { useMemo, useState } from 'react';
import { Link } from 'react-router-dom';
import { FolderOpen, Search, Eye } from 'lucide-react';
import PageHeader from '../components/PageHeader';
import StatusBadge from '../components/StatusBadge';
import { getRequisicoes } from '../lib/storage';
import type { StatusRequisicao } from '../lib/types';

function formatDate(iso: string) {
  if (!iso) return '-';
  const [y, m, d] = iso.split('-');
  return `${d}/${m}/${y}`;
}

export default function Autorizacoes() {
  const [busca, setBusca] = useState('');
  const [status, setStatus] = useState<StatusRequisicao | 'todos'>('todos');
  const requisicoes = useMemo(() => getRequisicoes(), []);

  const filtradas = requisicoes.filter((r) => {
    const matchStatus = status === 'todos' || r.status === status;
    const q = busca.trim().toLowerCase();
    const matchBusca =
      !q || r.nomeAluno.toLowerCase().includes(q) || r.matriculaAluno.includes(q) || r.protocolo.toLowerCase().includes(q);
    return matchStatus && matchBusca;
  });

  return (
    <div>
      <PageHeader
        icon={<FolderOpen size={14} />}
        eyebrow="Passo 3 de 3"
        title="Página da autorização"
        description="Consulte o histórico de requisições registradas neste dispositivo e acesse o documento oficial de cada uma."
      />

      <div className="mx-auto max-w-6xl px-4 py-10 md:px-6">
        <div className="mb-6 flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
          <div className="relative w-full sm:max-w-xs">
            <Search size={16} className="pointer-events-none absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
            <input
              className="input pl-9"
              placeholder="Buscar por nome, matrícula ou protocolo"
              value={busca}
              onChange={(e) => setBusca(e.target.value)}
            />
          </div>
          <select className="input sm:w-52" value={status} onChange={(e) => setStatus(e.target.value as StatusRequisicao | 'todos')}>
            <option value="todos">Todos os status</option>
            <option value="pendente">Pendente de aprovação</option>
            <option value="aprovado">Autorizado</option>
            <option value="recusado">Indeferido</option>
          </select>
        </div>

        {filtradas.length === 0 ? (
          <div className="rounded-2xl border border-dashed border-slate-300 bg-white p-12 text-center">
            <FolderOpen className="mx-auto mb-3 text-slate-300" size={36} />
            <p className="font-semibold text-slate-600">Nenhuma requisição encontrada</p>
            <p className="mt-1 text-sm text-slate-400">Registre uma nova solicitação na página de requisição.</p>
            <Link to="/requisicao" className="mt-5 inline-flex items-center gap-2 rounded-full bg-emerald-800 px-5 py-2.5 text-sm font-bold text-white hover:bg-emerald-700">
              Nova requisição
            </Link>
          </div>
        ) : (
          <div className="overflow-hidden rounded-2xl border border-emerald-900/10 bg-white shadow-sm">
            <table className="w-full min-w-[720px] text-left text-sm">
              <thead className="bg-emerald-50 text-xs uppercase tracking-wide text-emerald-800">
                <tr>
                  <th className="px-4 py-3">Protocolo</th>
                  <th className="px-4 py-3">Aluno(a)</th>
                  <th className="px-4 py-3">Curso</th>
                  <th className="px-4 py-3">Data</th>
                  <th className="px-4 py-3">Status</th>
                  <th className="px-4 py-3"></th>
                </tr>
              </thead>
              <tbody>
                {filtradas.map((r) => (
                  <tr key={r.id} className="border-t border-slate-100 hover:bg-slate-50">
                    <td className="px-4 py-3 font-mono text-xs text-slate-700">{r.protocolo}</td>
                    <td className="px-4 py-3">
                      <p className="font-medium text-slate-800">{r.nomeAluno}</p>
                      <p className="text-xs text-slate-400">Mat. {r.matriculaAluno}</p>
                    </td>
                    <td className="px-4 py-3 text-slate-600">{r.curso}</td>
                    <td className="px-4 py-3 text-slate-600">{formatDate(r.data)}</td>
                    <td className="px-4 py-3"><StatusBadge status={r.status} /></td>
                    <td className="px-4 py-3 text-right">
                      <Link to={`/autorizacao/${r.id}`} className="inline-flex items-center gap-1.5 text-xs font-semibold text-emerald-700 hover:underline">
                        <Eye size={14} /> Ver documento
                      </Link>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
}
