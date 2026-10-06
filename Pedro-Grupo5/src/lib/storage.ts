import type { Aluno, Requisicao, StatusRequisicao } from './types';

const ALUNO_KEY = 'ifmt.saida.aluno';
const REQ_KEY = 'ifmt.saida.requisicoes';

export function getAluno(): Aluno | null {
  try {
    const raw = localStorage.getItem(ALUNO_KEY);
    return raw ? (JSON.parse(raw) as Aluno) : null;
  } catch {
    return null;
  }
}

export function saveAluno(aluno: Aluno) {
  localStorage.setItem(ALUNO_KEY, JSON.stringify(aluno));
}

export function clearAluno() {
  localStorage.removeItem(ALUNO_KEY);
}

export function getRequisicoes(): Requisicao[] {
  try {
    const raw = localStorage.getItem(REQ_KEY);
    return raw ? (JSON.parse(raw) as Requisicao[]) : [];
  } catch {
    return [];
  }
}

export function getRequisicaoById(id: string): Requisicao | undefined {
  return getRequisicoes().find((r) => r.id === id);
}

export function saveRequisicao(req: Requisicao) {
  const all = getRequisicoes();
  all.unshift(req);
  localStorage.setItem(REQ_KEY, JSON.stringify(all));
}

export function updateRequisicao(id: string, patch: Partial<Requisicao>) {
  const all = getRequisicoes().map((r) => (r.id === id ? { ...r, ...patch } : r));
  localStorage.setItem(REQ_KEY, JSON.stringify(all));
}

export function decidirRequisicao(id: string, status: StatusRequisicao, decididoPor: string, justificativa: string) {
  updateRequisicao(id, {
    status,
    decididoPor,
    decididoEm: new Date().toISOString(),
    justificativaDecisao: justificativa,
  });
}

export function gerarProtocolo(): string {
  const ano = new Date().getFullYear();
  const seq = getRequisicoes().length + 1;
  const rand = Math.floor(Math.random() * 900 + 100);
  return `SA-${ano}-${String(seq).padStart(4, '0')}${rand}`;
}

export function gerarId(): string {
  return `${Date.now().toString(36)}${Math.random().toString(36).slice(2, 8)}`;
}
