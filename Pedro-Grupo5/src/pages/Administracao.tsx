import { Landmark, Info, Clock3 } from 'lucide-react';
import PageHeader from '../components/PageHeader';
import SignatureBlock from '../components/SignatureBlock';
import { CAMPUS, DEPARTAMENTOS, HORARIOS_PADRAO } from '../lib/data';

export default function Administracao() {
  return (
    <div>
      <PageHeader
        icon={<Landmark size={14} />}
        eyebrow="Painel administrativo"
        title="Telas iniciais administrativas"
        description="Cadastro institucional, fixo, dos responsáveis autorizados a assinar documentos de saída antecipada: direção do campus, coordenação de cada curso e o(a) professor(a) orientador(a) de turma."
      />

      <div className="mx-auto max-w-6xl px-4 py-10 md:px-6">
        <div className="mb-8 flex items-start gap-3 rounded-xl border border-amber-300 bg-amber-50 p-4 text-sm text-amber-900">
          <Info size={18} className="mt-0.5 shrink-0" />
          <p>
            Estas telas são <strong>estáticas</strong>: os nomes, cargos e assinaturas abaixo representam o
            cadastro administrativo de referência utilizado para compor automaticamente o documento de
            autorização. Dados ilustrativos para fins de demonstração do sistema.
          </p>
        </div>

        <div className="mb-10 rounded-2xl border border-emerald-900/10 bg-white p-8 shadow-sm">
          <div className="mb-6 flex items-center justify-between">
            <div>
              <p className="text-xs font-semibold uppercase tracking-[0.2em] text-emerald-700">Direção do campus</p>
              <h2 className="mt-1 font-serif text-xl font-bold text-emerald-950">{CAMPUS.campus}</h2>
            </div>
            <img src="/logo.png" className="h-14 w-14 object-contain opacity-90" alt="" />
          </div>
          <div className="grid gap-8 sm:grid-cols-2">
            <SignatureBlock name={CAMPUS.diretorGeral} role="Diretor Geral do Campus" />
            <SignatureBlock name={CAMPUS.diretorEnsino} role="Diretora de Ensino" />
          </div>
        </div>

        <h2 className="mb-4 font-serif text-xl font-bold text-emerald-950">Assinaturas por departamento</h2>
        <div className="grid gap-6 lg:grid-cols-2">
          {DEPARTAMENTOS.map((d) => (
            <div key={d.sigla} className="rounded-2xl border border-emerald-900/10 bg-white p-6 shadow-sm">
              <div className="mb-5 flex items-center justify-between">
                <h3 className="font-serif text-lg font-bold text-emerald-950">{d.nome}</h3>
                <span className="rounded-full bg-emerald-800 px-3 py-1 text-xs font-bold text-white">{d.sigla}</span>
              </div>
              <ul className="mb-6 space-y-1.5">
                {d.cursos.map((c) => (
                  <li key={c.nome} className="text-sm text-slate-600">
                    • {c.nome} <span className="text-slate-400">({c.modalidade})</span>
                  </li>
                ))}
              </ul>
              <div className="grid gap-6 border-t border-dashed border-slate-200 pt-6 sm:grid-cols-2">
                <SignatureBlock name={d.coordenador} role={d.tituloCoordenador} />
                <SignatureBlock name={d.professorResponsavel} role={d.tituloProfessor} />
              </div>
            </div>
          ))}
        </div>

        <div className="mt-10 rounded-2xl border border-emerald-900/10 bg-white p-6 shadow-sm">
          <div className="mb-4 flex items-center gap-2">
            <Clock3 className="text-emerald-700" size={20} />
            <h2 className="font-serif text-lg font-bold text-emerald-950">Quadro de horários padrão por turno</h2>
          </div>
          <div className="overflow-x-auto">
            <table className="w-full min-w-[420px] text-left text-sm">
              <thead>
                <tr className="border-b border-slate-200 text-xs uppercase tracking-wide text-slate-500">
                  <th className="py-2 pr-4">Turno</th>
                  <th className="py-2 pr-4">Entrada</th>
                  <th className="py-2 pr-4">Saída normal</th>
                </tr>
              </thead>
              <tbody>
                {Object.entries(HORARIOS_PADRAO).map(([turno, h]) => (
                  <tr key={turno} className="border-b border-slate-100 last:border-0">
                    <td className="py-2.5 pr-4 font-medium text-slate-800">{turno}</td>
                    <td className="py-2.5 pr-4 text-slate-600">{h.entrada}</td>
                    <td className="py-2.5 pr-4 text-slate-600">{h.saida}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
          <p className="mt-3 text-xs text-slate-400">
            Utilizado para calcular automaticamente a comparação entre o horário normal e o horário de
            saída solicitado.
          </p>
        </div>
      </div>
    </div>
  );
}
