export default function SignatureBlock({
  name,
  role,
  filled = true,
}: {
  name: string;
  role: string;
  filled?: boolean;
}) {
  return (
    <div className="flex flex-col items-center text-center">
      <div className="flex h-14 items-end justify-center">
        {filled ? (
          <span className="font-signature text-3xl leading-none text-emerald-900">{name}</span>
        ) : (
          <span className="text-sm text-slate-300">(aguardando assinatura)</span>
        )}
      </div>
      <div className="mt-1 w-56 border-t border-slate-400" />
      <p className="mt-2 text-sm font-semibold text-slate-800">{name}</p>
      <p className="text-xs text-slate-500">{role}</p>
    </div>
  );
}
