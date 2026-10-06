import type { Departamento } from './types';

export const CAMPUS = {
  instituicao: 'Instituto Federal de Educação, Ciência e Tecnologia de Mato Grosso',
  sigla: 'IFMT',
  campus: 'Campus Cuiabá – Cel. Octayde Jorge da Silva',
  endereco: 'Rua Zulmira Canavarros, nº 95, Centro Norte, Cuiabá – MT, CEP 78005-390',
  telefone: '(65) 3616-4300',
  email: 'secretaria.cba@ifmt.edu.br',
  diretorGeral: 'Prof. Dr. Marcelo Augusto Vieira',
  diretorEnsino: 'Profa. Ma. Sandra Regina Pacheco',
};

export const DEPARTAMENTOS: Departamento[] = [
  {
    sigla: 'DCOM',
    nome: 'Departamento de Computação',
    cursos: [
      { nome: 'Técnico em Informática', modalidade: 'Integrado ao Ensino Médio' },
      { nome: 'Técnico em Informática para Internet', modalidade: 'Integrado' },
    ],
    coordenador: 'Prof. Me. Rodrigo Almeida Castro',
    tituloCoordenador: 'Coordenador do Curso — DCOM',
    professorResponsavel: 'Profa. Esp. Camila Rezende Souza',
    tituloProfessor: 'Professora Orientadora de Turma',
  },
  {
    sigla: 'DINFRA',
    nome: 'Departamento de Infraestrutura',
    cursos: [
      { nome: 'Técnico em Edificações', modalidade: 'Integrado e Subsequente' },
      { nome: 'Técnico em Agrimensura', modalidade: 'Integrado e Subsequente' },
    ],
    coordenador: 'Profa. Dra. Beatriz Nunes Carvalho',
    tituloCoordenador: 'Coordenadora do Curso — DINFRA',
    professorResponsavel: 'Prof. Esp. Fábio Henrique Lopes',
    tituloProfessor: 'Professor Orientador de Turma',
  },
  {
    sigla: 'DGH',
    nome: 'Departamento de Gestão e Hospitalidade',
    cursos: [
      { nome: 'Técnico em Eventos', modalidade: 'Integrado' },
      { nome: 'Técnico em Secretariado', modalidade: 'Integrado' },
      { nome: 'Técnico em Hospedagem', modalidade: 'Subsequente' },
    ],
    coordenador: 'Prof. Me. André Luiz Nascimento',
    tituloCoordenador: 'Coordenador do Curso — DGH',
    professorResponsavel: 'Profa. Esp. Juliana Ferreira Lima',
    tituloProfessor: 'Professora Orientadora de Turma',
  },
  {
    sigla: 'DEEA',
    nome: 'Departamento de Engenharia Elétrica e Automação',
    cursos: [
      { nome: 'Técnico em Eletrotécnica', modalidade: 'Integrado' },
      { nome: 'Técnico em Eletrônica', modalidade: 'Integrado' },
      { nome: 'Técnico em Automação Industrial', modalidade: 'Subsequente' },
      { nome: 'Técnico em Mecatrônica', modalidade: 'Integrado' },
    ],
    coordenador: 'Prof. Dr. Eduardo Henrique Martins',
    tituloCoordenador: 'Coordenador do Curso — DEEA',
    professorResponsavel: 'Profa. Esp. Patrícia Gomes Ribeiro',
    tituloProfessor: 'Professora Orientadora de Turma',
  },
];

export const HORARIOS_PADRAO: Record<string, { entrada: string; saida: string }> = {
  Matutino: { entrada: '07:00', saida: '12:20' },
  Vespertino: { entrada: '13:00', saida: '18:20' },
  Noturno: { entrada: '19:00', saida: '22:40' },
  Integral: { entrada: '07:00', saida: '17:30' },
};

export const MOTIVOS_PADRAO = [
  'Consulta médica / odontológica',
  'Mal-estar / atendimento de saúde',
  'Compromisso familiar inadiável',
  'Exame, prova ou processo seletivo externo',
  'Estágio, trabalho ou entrevista de emprego',
  'Falecimento / luto familiar',
  'Deslocamento para outra cidade',
  'Outro (especificar em observações)',
];

export function getDepartamentoPorCurso(curso: string): Departamento | undefined {
  return DEPARTAMENTOS.find((d) => d.cursos.some((c) => c.nome === curso));
}
