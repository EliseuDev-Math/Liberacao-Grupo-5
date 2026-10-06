import { Link } from 'react-router-dom';
import { ArrowRight, ClipboardList, FileCheck2, ShieldCheck, UserPlus, Clock, Building2 } from 'lucide-react';
import { CAMPUS, DEPARTAMENTOS } from '../lib/data';

const steps = [
  {
    icon: <UserPlus className="text-emerald-700" size={22} />,
    title: '1. Cadastre-se',
    text: 'O(a) aluno(a) preenche seus dados acadêmicos uma única vez: matrícula, curso, turma e turno.',
    to: '/usuario',
  },
  {
    icon: <ClipboardList className="text-emerald-700" size={22} />,
    title: '2. Solicite a saída',
    text: 'Registre data, horário pretendido e o motivo da saída antecipada, gerando um protocolo.',
    to: '/requisicao',
  },
  {
    icon: <FileCheck2 className="text-emerald-700" size={22} />,
    title: '3. Acompanhe a autorização',
    text: 'Visualize o documento oficial com as assinaturas da coordenação, professor(a) e direção.',
    to: '/autorizacoes',
  },
];

export default function Home() {
  return (
    <div>
      <section className="relative overflow-hidden bg-emerald-950 text-white">
        <img src="/hero.jpg" alt="" className="absolute inset-0 h-full w-full object-cover opacity-25" />
        <div className="absolute inset-0 bg-gradient-to-r from-emerald-950 via-emerald-950/95 to-emerald-900/70" />
        <div className="relative mx-auto max-w-6xl px-4 py-20 md:px-6 md:py-28">
          <p className="mb-3 flex items-center gap-2 text-xs font-semibold uppercase tracking-[0.25em] text-amber-300">
            <Building2 size={16} /> {CAMPUS.sigla} — {CAMPUS.campus}
          </p>
          <h1 className="max-w-3xl font-serif text-4xl font-bold leading-tight md:text-5xl">
            Sistema de Autorização de Saída Antecipada de Alunos
          </h1>
          <p className="mt-5 max-w-2xl text-base text-emerald-100 md:text-lg">
            Solicite, registre e acompanhe autorizações para que o(a) estudante deixe o campus antes do
            horário normal de encerramento das aulas, com o devido registro de responsabilidade da
            instituição, do responsável e do corpo docente.
          </p>
          <div className="mt-8 flex flex-wrap gap-3">
            <Link
              to="/requisicao"
              className="inline-flex items-center gap-2 rounded-full bg-amber-400 px-6 py-3 text-sm font-bold text-emerald-950 shadow-lg shadow-amber-900/30 transition hover:bg-amber-300"
            >
              Solicitar saída antecipada <ArrowRight size={16} />
            </Link>
            <Link
              to="/administracao"
              className="inline-flex items-center gap-2 rounded-full border border-white/30 px-6 py-3 text-sm font-semibold text-white transition hover:bg-white/10"
            >
              Ver telas administrativas
            </Link>
          </div>
        </div>
      </section>

      <section className="mx-auto max-w-6xl px-4 py-16 md:px-6">
        <div className="mb-10 text-center">
          <p className="text-xs font-semibold uppercase tracking-[0.2em] text-emerald-700">Como funciona</p>
          <h2 className="mt-2 font-serif text-2xl font-bold text-emerald-950 md:text-3xl">
            Três passos para uma saída antecipada segura
          </h2>
        </div>
        <div className="grid gap-6 md:grid-cols-3">
          {steps.map((s) => (
            <Link
              key={s.title}
              to={s.to}
              className="group rounded-2xl border border-emerald-900/10 bg-white p-6 shadow-sm transition hover:-translate-y-1 hover:shadow-lg"
            >
              <div className="mb-4 flex h-12 w-12 items-center justify-center rounded-xl bg-emerald-50">{s.icon}</div>
              <h3 className="font-serif text-lg font-bold text-emerald-950">{s.title}</h3>
              <p className="mt-2 text-sm text-slate-600">{s.text}</p>
              <span className="mt-4 inline-flex items-center gap-1 text-sm font-semibold text-emerald-700 group-hover:gap-2">
                Acessar <ArrowRight size={14} />
              </span>
            </Link>
          ))}
        </div>
      </section>

      <section className="bg-slate-50 py-16">
        <div className="mx-auto max-w-6xl px-4 md:px-6">
          <div className="mb-10 flex flex-col items-start justify-between gap-4 md:flex-row md:items-end">
            <div>
              <p className="text-xs font-semibold uppercase tracking-[0.2em] text-emerald-700">Cursos do campus</p>
              <h2 className="mt-2 font-serif text-2xl font-bold text-emerald-950 md:text-3xl">
                Departamentos e cursos técnicos
              </h2>
            </div>
            <p className="max-w-md text-sm text-slate-500">
              A autorização é sempre encaminhada à coordenação e ao(à) professor(a) vinculados ao
              departamento responsável pelo curso do(a) estudante.
            </p>
          </div>
          <div className="grid gap-6 md:grid-cols-2">
            {DEPARTAMENTOS.map((d) => (
              <div key={d.sigla} className="rounded-2xl border border-emerald-900/10 bg-white p-6 shadow-sm">
                <div className="mb-4 flex items-center justify-between">
                  <h3 className="font-serif text-lg font-bold text-emerald-950">{d.nome}</h3>
                  <span className="rounded-full bg-emerald-800 px-3 py-1 text-xs font-bold text-white">{d.sigla}</span>
                </div>
                <ul className="space-y-2">
                  {d.cursos.map((c) => (
                    <li key={c.nome} className="flex items-start gap-2 text-sm text-slate-600">
                      <span className="mt-1.5 h-1.5 w-1.5 shrink-0 rounded-full bg-amber-500" />
                      <span>
                        <strong className="text-slate-800">{c.nome}</strong> ({c.modalidade})
                      </span>
                    </li>
                  ))}
                </ul>
              </div>
            ))}
          </div>
        </div>
      </section>

      <section className="mx-auto max-w-6xl px-4 py-16 md:px-6">
        <div className="grid gap-6 rounded-2xl border border-emerald-900/10 bg-emerald-900 p-8 text-white md:grid-cols-3 md:p-10">
          <div className="flex items-start gap-3">
            <ShieldCheck className="shrink-0 text-amber-300" />
            <div>
              <p className="font-serif font-bold">Registro oficial</p>
              <p className="mt-1 text-sm text-emerald-100">Cada solicitação gera um protocolo único e rastreável.</p>
            </div>
          </div>
          <div className="flex items-start gap-3">
            <Clock className="shrink-0 text-amber-300" />
            <div>
              <p className="font-serif font-bold">Comparação de horários</p>
              <p className="mt-1 text-sm text-emerald-100">O sistema evidencia o horário normal e o horário de saída pretendido.</p>
            </div>
          </div>
          <div className="flex items-start gap-3">
            <FileCheck2 className="shrink-0 text-amber-300" />
            <div>
              <p className="font-serif font-bold">Assinaturas registradas</p>
              <p className="mt-1 text-sm text-emerald-100">Documento final pronto para impressão com todas as assinaturas.</p>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
}
