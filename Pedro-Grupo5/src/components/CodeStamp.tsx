export default function CodeStamp({ value, size = 7 }: { value: string; size?: number }) {
  const cells: boolean[] = [];
  for (let i = 0; i < size * size; i++) {
    const charCode = value.charCodeAt(i % value.length) || 0;
    cells.push(((charCode + i * 31) % 5) < 2);
  }
  // keep corners solid, like a finder pattern, for visual authenticity
  const isFinder = (i: number) => {
    const r = Math.floor(i / size);
    const c = i % size;
    const inCorner = (rr: number, cc: number) => rr < 2 && cc < 2;
    return (
      inCorner(r, c) ||
      inCorner(r, size - 1 - c) ||
      inCorner(size - 1 - r, c)
    );
  };

  return (
    <div
      className="grid w-20 gap-[2px] rounded bg-emerald-950 p-1.5"
      style={{ gridTemplateColumns: `repeat(${size}, minmax(0, 1fr))` }}
      aria-hidden
    >
      {cells.map((filled, i) => (
        <div key={i} className={`aspect-square rounded-[1px] ${filled || isFinder(i) ? 'bg-white' : 'bg-transparent'}`} />
      ))}
    </div>
  );
}
