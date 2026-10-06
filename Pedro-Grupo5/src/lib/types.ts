export type Turno = 'Matutino' | 'Vespertino' | 'Noturno' | 'Integral';

export type StatusRequisicao = 'pendente' | 'aprovado' | 'recusado';

export interface Curso {
  nome: string;
  modalidade: string;
}

export interface Departamento {
  sigla: string;
  nome: string;
  cursos: Curso[];
  coordenador: string;
  tituloCoordenador: string;
  professorResponsavel: string;
  tituloProfessor: string;
}

export interface Aluno {
  matricula: string;
  nome: string;
  cpf: string;
  dataNascimento: string;
  departamento: string;
  curso: string;
  turma: string;
  turno: Turno;
  responsavel: string;
  telefoneResponsavel: string;
  telefoneAluno: string;
  atualizadoEm: string;
}

export interface Requisicao {
  id: string;
  protocolo: string;
  matriculaAluno: string;
  nomeAluno: string;
  cpfAluno: string;
  departamento: string;
  curso: string;
  turma: string;
  turno: Turno;
  data: string;
  horarioNormal: string;
  horarioSaida: string;
  motivo: string;
  observacoes: string;
  responsavel: string;
  telefoneResponsavel: string;
  status: StatusRequisicao;
  criadoEm: string;
  decididoPor?: string;
  decididoEm?: string;
  justificativaDecisao?: string;
}
