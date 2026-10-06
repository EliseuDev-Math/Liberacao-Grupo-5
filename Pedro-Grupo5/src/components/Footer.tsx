import { MapPin, Mail, Phone } from 'lucide-react';
import { CAMPUS } from '../lib/data';

export default function Footer() {
  return (
    <footer className="border-t border-emerald-900/10 bg-emerald-950 text-emerald-100 print:hidden">
      <div className="mx-auto grid max-w-6xl gap-8 px-4 py-10 md:grid-cols-3 md:px-6">
        <div>
          <div className="flex items-center gap-3">
            <img src="/logo.png" alt="Brasão" className="h-10 w-10 object-contain" />
            <div>
              <p className="font-serif text-sm font-bold uppercase tracking-wide text-white">{CAMPUS.sigla}</p>
              <p className="text-xs text-emerald-300">{CAMPUS.campus}</p>
            </div>
          </div>
          <p className="mt-4 text-sm leading-relaxed text-emerald-200">
            Sistema institucional de autorização de saída antecipada de alunos, com registro de
            solicitações e assinaturas do corpo docente e da coordenação de curso.
          </p>
        </div>

        <div className="text-sm text-emerald-200">
          <p className="mb-3 font-semibold text-white">Contato</p>
          <p className="mb-2 flex items-start gap-2"><MapPin size={16} className="mt-0.5 shrink-0" /> {CAMPUS.endereco}</p>
          <p className="mb-2 flex items-center gap-2"><Phone size={16} /> {CAMPUS.telefone}</p>
          <p className="flex items-center gap-2"><Mail size={16} /> {CAMPUS.email}</p>
        </div>

        <div className="text-sm text-emerald-200">
          <p className="mb-3 font-semibold text-white">Aviso</p>
          <p className="leading-relaxed">
            Este sistema foi desenvolvido como demonstração. Nomes de coordenadores, professores e
            assinaturas exibidos são ilustrativos e não representam necessariamente os servidores
            atuais do campus.
          </p>
        </div>
      </div>
      <div className="border-t border-white/10 py-4 text-center text-xs text-emerald-400">
        © {new Date().getFullYear()} {CAMPUS.instituicao} — {CAMPUS.campus}
      </div>
    </footer>
  );
}
