import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { UserRound, Save, ArrowRight, CheckCircle2 } from 'lucide-react';
import PageHeader from '../components/PageHeader';
import { DEPARTAMENTOS } from '../lib/data';
import type { Aluno, Turno } from '../lib/types';
import { getAluno, saveAluno } from '../lib/storage';

const EMPTY: Aluno = {
  matricula: '',
  nome: '',
  cpf: '',
  dataNascimento: '',
  departamento: DEPARTAMENTOS[0].sigla,
  curso: DEPARTAMENTOS[0].cursos[0].nome,
  turma: '',
  turno: 'Matutino',
  responsavel: '',
  telefoneResponsavel: '',
  telefoneAluno: '',
  atualizadoEm: '',
};

export default function Usuario() {
  const [form, setForm] = useState<Aluno>(EMPTY);
  const [saved, setSaved] = useState(false);
  const [errors, setErrors] = useState<Record<string, string>>({});
  const navigate = useNavigate();

  useEffect(() => {
    const existing = getAluno();
    if (existing) setForm(existing);
  }, []);

  const departamentoAtual = DEPARTAMENTOS.find((d) => d.sigla === form.departamento) ?? DEPARTAMENTOS[0];

  function update<K extends keyof Aluno>(key: K, value: Aluno[K]) {
    setForm((f) => ({ ...f, [key]: value }));
    setSaved(false);
  }

  function validate(): boolean {
    const e: Record<string, string> = {};
    if (!form.nome.trim()) e.nome = 'Informe o nome completo.';
    if (!/^\d{6,12}$/.test(form.matricula.trim())) e.matricula = 'Matrícula deve conter apenas números (6 a 12 dígitos).';
    if (!/^\d{11}$/.test(form.cpf.replace(/\D/g, ''))) e.cpf = 'Informe um CPF válido (11 dígitos).';
    if (!form.dataNascimento) e.dataNascimento = 'Informe a data de nascimento.';
    if (!form.turma.trim()) e.turma = 'Informe a turma (ex: 1º INFO A).';
    if (!form.telefoneAluno.trim()) e.telefoneAluno = 'Informe um telefone de contato.';
    setErrors(e);
    return Object.keys(e).length === 0;
  }

  function handleSubmit(ev: React.FormEvent) {
    ev.preventDefault();
    if (!validate()) return;
    saveAluno({ ...form, atualizadoEm: new Date().toISOString() });
    setSaved(true);
  }

  return (
    <div>
      <PageHeader
        icon={<UserRound size={14} />}
        eyebrow="Passo 1 de 3"
        title="Página do usuário"
        description="Cadastre os dados acadêmicos do(a) estudante. Essas informações serão reaproveitadas automaticamente ao abrir uma requisição de saída antecipada."
      />

      <div className="mx-auto max-w-3xl px-4 py-10 md:px-6">
        <form onSubmit={handleSubmit} className="space-y-6 rounded-2xl border border-emerald-900/10 bg-white p-6 shadow-sm md:p-8">
          <div className="grid gap-5 sm:grid-cols-2">
            <Field label="Nome completo" error={errors.nome}>
              <input className="input" value={form.nome} onChange={(e) => update('nome', e.target.value)} placeholder="Ex: Maria Eduarda Souza Lima" />
            </Field>
            <Field label="Matrícula" error={errors.matricula}>
              <input className="input" value={form.matricula} onChange={(e) => update('matricula', e.target.value.replace(/\D/g, ''))} placeholder="Ex: 2026104001" />
            </Field>
            <Field label="CPF" error={errors.cpf}>
              <input className="input" value={form.cpf} onChange={(e) => update('cpf', e.target.value)} placeholder="000.000.000-00" />
            </Field>
            <Field label="Data de nascimento" error={errors.dataNascimento}>
              <input type="date" className="input" value={form.dataNascimento} onChange={(e) => update('dataNascimento', e.target.value)} />
            </Field>
          </div>

          <div className="grid gap-5 sm:grid-cols-2">
            <Field label="Departamento">
              <select
                className="input"
                value={form.departamento}
                onChange={(e) => {
                  const dep = DEPARTAMENTOS.find((d) => d.sigla === e.target.value)!;
                  update('departamento', dep.sigla);
                  update('curso', dep.cursos[0].nome);
                }}
              >
                {DEPARTAMENTOS.map((d) => (
                  <option key={d.sigla} value={d.sigla}>{d.nome}</option>
                ))}
              </select>
            </Field>
            <Field label="Curso">
              <select className="input" value={form.curso} onChange={(e) => update('curso', e.target.value)}>
                {departamentoAtual.cursos.map((c) => (
                  <option key={c.nome} value={c.nome}>{c.nome} ({c.modalidade})</option>
                ))}
              </select>
            </Field>
            <Field label="Turma" error={errors.turma}>
              <input className="input" value={form.turma} onChange={(e) => update('turma', e.target.value)} placeholder="Ex: 2º INFO B" />
            </Field>
            <Field label="Turno">
              <select className="input" value={form.turno} onChange={(e) => update('turno', e.target.value as Turno)}>
                {(['Matutino', 'Vespertino', 'Noturno', 'Integral'] as Turno[]).map((t) => (
                  <option key={t} value={t}>{t}</option>
                ))}
              </select>
            </Field>
          </div>

          <div className="grid gap-5 sm:grid-cols-2">
            <Field label="Telefone do(a) aluno(a)" error={errors.telefoneAluno}>
              <input className="input" value={form.telefoneAluno} onChange={(e) => update('telefoneAluno', e.target.value)} placeholder="(65) 90000-0000" />
            </Field>
            <Field label="Nome do responsável (se menor de idade)">
              <input className="input" value={form.responsavel} onChange={(e) => update('responsavel', e.target.value)} placeholder="Opcional para maiores de idade" />
            </Field>
            <Field label="Telefone do responsável">
              <input className="input" value={form.telefoneResponsavel} onChange={(e) => update('telefoneResponsavel', e.target.value)} placeholder="(65) 90000-0000" />
            </Field>
          </div>

          <div className="flex flex-col-reverse items-stretch gap-3 border-t border-slate-100 pt-6 sm:flex-row sm:items-center sm:justify-between">
            <div className="min-h-[24px] flex items-center gap-2 text-sm">
              {saved && (
                <span className="flex items-center gap-1.5 font-medium text-emerald-700">
                  <CheckCircle2 size={16} /> Dados salvos com sucesso.
                </span>
              )}
            </div>
            <div className="flex gap-3">
              <button type="submit" className="inline-flex items-center justify-center gap-2 rounded-full bg-emerald-800 px-6 py-3 text-sm font-bold text-white transition hover:bg-emerald-700">
                <Save size={16} /> Salvar cadastro
              </button>
              <button
                type="button"
                onClick={() => {
                  if (validate()) {
                    saveAluno({ ...form, atualizadoEm: new Date().toISOString() });
                    navigate('/requisicao');
                  }
                }}
                className="inline-flex items-center justify-center gap-2 rounded-full border border-emerald-800 px-6 py-3 text-sm font-bold text-emerald-800 transition hover:bg-emerald-50"
              >
                Ir para requisição <ArrowRight size={16} />
              </button>
            </div>
          </div>
        </form>
      </div>
    </div>
  );
}

function Field({ label, error, children }: { label: string; error?: string; children: React.ReactNode }) {
  return (
    <label className="block">
      <span className="mb-1.5 block text-sm font-semibold text-slate-700">{label}</span>
      {children}
      {error && <span className="mt-1 block text-xs font-medium text-red-600">{error}</span>}
    </label>
  );
}
