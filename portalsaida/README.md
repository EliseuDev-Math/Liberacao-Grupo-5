# Portal Saída
Sistema de liberação de saída antecipada de turmas e alunos do IFMT – Campus Octayde Jorge da Silva.
Spring Boot 3.5 · Java 21 · Maven · Thymeleaf · JPA/Hibernate · H2 (dev) / MySQL (produção).

## Como rodar
1. Abra a pasta no IntelliJ (File > Open > `pom.xml` > "Open as Project") e aguarde o Maven baixar as dependências.
2. Execute `PortalsaidaApplication` (ou `mvn spring-boot:run`).
3. Acesse http://localhost:8080 — console do banco: http://localhost:8080/h2-console (JDBC URL `jdbc:h2:mem:portalsaidabd`, usuário `sa`, senha vazia).
4. Testes: `mvn test`.

## Usuários de exemplo (criados pelo `DataLoader`)
| Login | Senha | Perfil | Departamento |
|---|---|---|---|
| admin | admin123 | Administrador | todos |
| coord.info | 123456 | Coordenador | Informática |
| sec.info | 123456 | Secretário | Informática |
| sec.edif | 123456 | Secretário | Edificações |

## Permissões
- **Administrador**: tudo, em todos os departamentos (inclusive departamentos e usuários).
- **Coordenador**: cadastra cursos, turmas, alunos e horários do **seu** departamento; só consulta liberações.
- **Secretário**: autoriza/cancela liberações do **seu** departamento; consulta cadastros e horários.

## Estrutura (mesma do livro, Cap. 3–5)
`model` (entidades) · `repository` (JpaRepository) · `DTO` · `service` (regras de negócio) · `controller` (@Controller) · `security` (login e perfis) · `config` · `templates/` (Thymeleaf) · `static/css`.

## Regras de negócio implementadas (LiberacaoService)
Perfil autorizado; turma do departamento do usuário; aluno pertence à turma; data não pode ser passada;
saída dentro do período de aulas da turma no dia; sem duplicidade (turma/aluno no mesmo dia);
justificativa, motivo e comprovação obrigatórios; cancelamento só de liberação ativa.

## Trocar para MySQL
Edite `application-mysql.properties` e rode com `-Dspring-boot.run.profiles=mysql`.
