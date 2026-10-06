import { CheckCircle2, Clock3, XCircle } from 'lucide-react';
import type { StatusRequisicao } from '../lib/types';

const CONFIG: Record<StatusRequisicao, { label: string; classes: string; icon: React.ReactNode }> = {
  pendente: {
    label: 'Pendente de aprovação',
    classes: 'bg-amber-100 text-amber-800 border-amber-300',
    icon: <Clock3 size={14} />,
  },
  aprovado: {
    label: 'Autorizado',
    classes: 'bg-emerald-100 text-emerald-800 border-emerald-300',
    icon: <CheckCircle2 size={14} />,
  },
  recusado: {
    label: 'Indeferido',
    classes: 'bg-red-100 text-red-800 border-red-300',
    icon: <XCircle size={14} />,
  },
};

export default function StatusBadge({ status }: { status: StatusRequisicao }) {
  const c = CONFIG[status];
  return (
    <span className={`inline-flex items-center gap-1.5 rounded-full border px-3 py-1 text-xs font-semibold ${c.classes}`}>
      {c.icon}
      {c.label}
    </span>
  );
}
