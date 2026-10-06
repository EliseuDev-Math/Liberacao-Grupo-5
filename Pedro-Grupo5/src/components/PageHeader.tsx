import type { ReactNode } from 'react';

export default function PageHeader({
  eyebrow,
  title,
  description,
  icon,
}: {
  eyebrow: string;
  title: string;
  description?: string;
  icon?: ReactNode;
}) {
  return (
    <div className="border-b border-emerald-900/10 bg-gradient-to-br from-emerald-900 via-emerald-800 to-emerald-950 text-white print:hidden">
      <div className="mx-auto max-w-6xl px-4 py-12 md:px-6">
        <p className="mb-2 flex items-center gap-2 text-xs font-semibold uppercase tracking-[0.2em] text-amber-300">
          {icon}
          {eyebrow}
        </p>
        <h1 className="font-serif text-3xl font-bold md:text-4xl">{title}</h1>
        {description && <p className="mt-3 max-w-2xl text-sm text-emerald-100 md:text-base">{description}</p>}
      </div>
    </div>
  );
}
