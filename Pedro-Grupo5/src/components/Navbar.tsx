import { useState } from 'react';
import { Link, NavLink } from 'react-router-dom';
import { Menu, X, GraduationCap } from 'lucide-react';
import { CAMPUS } from '../lib/data';

const links = [
  { to: '/', label: 'Início' },
  { to: '/administracao', label: 'Administração' },
  { to: '/usuario', label: 'Usuário' },
  { to: '/requisicao', label: 'Requisição' },
  { to: '/autorizacoes', label: 'Autorizações' },
];

export default function Navbar() {
  const [open, setOpen] = useState(false);

  return (
    <header className="sticky top-0 z-40 border-b border-emerald-900/10 bg-white/95 backdrop-blur print:hidden">
      <div className="mx-auto flex max-w-6xl items-center justify-between px-4 py-3 md:px-6">
        <Link to="/" className="flex items-center gap-3">
          <img src="/logo.png" alt="Brasão institucional" className="h-11 w-11 object-contain" />
          <div className="leading-tight">
            <p className="font-serif text-sm font-bold uppercase tracking-wide text-emerald-900 md:text-base">
              {CAMPUS.sigla} · Saída Antecipada
            </p>
            <p className="text-[11px] text-slate-500 md:text-xs">{CAMPUS.campus}</p>
          </div>
        </Link>

        <nav className="hidden items-center gap-1 md:flex">
          {links.map((l) => (
            <NavLink
              key={l.to}
              to={l.to}
              end={l.to === '/'}
              className={({ isActive }) =>
                `rounded-full px-4 py-2 text-sm font-medium transition-colors ${
                  isActive
                    ? 'bg-emerald-800 text-white shadow-sm'
                    : 'text-slate-600 hover:bg-emerald-50 hover:text-emerald-800'
                }`
              }
            >
              {l.label}
            </NavLink>
          ))}
        </nav>

        <button
          className="rounded-lg border border-slate-200 p-2 text-emerald-900 md:hidden"
          onClick={() => setOpen((v) => !v)}
          aria-label="Abrir menu"
        >
          {open ? <X size={20} /> : <Menu size={20} />}
        </button>
      </div>

      {open && (
        <nav className="flex flex-col gap-1 border-t border-slate-100 bg-white px-4 py-3 md:hidden">
          {links.map((l) => (
            <NavLink
              key={l.to}
              to={l.to}
              end={l.to === '/'}
              onClick={() => setOpen(false)}
              className={({ isActive }) =>
                `flex items-center gap-2 rounded-lg px-3 py-2 text-sm font-medium ${
                  isActive ? 'bg-emerald-800 text-white' : 'text-slate-600 hover:bg-emerald-50'
                }`
              }
            >
              <GraduationCap size={16} />
              {l.label}
            </NavLink>
          ))}
        </nav>
      )}
    </header>
  );
}
