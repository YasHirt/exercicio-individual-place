# Revisão do Exercício Individual

Revisor: Gabriel
Data: 24/06/2026

## Visão Geral

O projeto implementa uma API REST para gerenciamento de filmes com as entidades `Filme`, `Genero` e `Ator`, usando Spring Boot com PostgreSQL e Flyway. A estrutura de pacotes foi organizada corretamente em camadas (`controller`, `service`, `repository`, `model`, `dto`, `exceptions`), o que demonstra boa compreensão do padrão de arquitetura esperado. As migrations com Flyway e o uso de Lombok também foram incorporados de forma adequada ao projeto.

## O que foi bem feito

A camada de `FilmeService` é a parte mais desenvolvida do projeto e demonstra vários comportamentos corretos: uso de injeção de dependência via construtor (sem `@Autowired`), separação clara de responsabilidades entre service e controller, lançamento de exceções específicas (`FilmeNotFoundException`, `GeneroNotFoundException`), e uso de `@Transactional` no método de criação. O `GlobalExceptionHandler` com `@RestControllerAdvice` foi criado e trata corretamente as exceções de negócio, retornando respostas estruturadas com o DTO `ApiErrorDto`. O relacionamento `@ManyToOne` na entidade `Filme` foi configurado sem fetch type explícito (o que é aceitável aqui), e o `@OneToMany` em `Genero` foi configurado com `fetch = FetchType.LAZY` conforme a dica do exercício. O relacionamento `@ManyToMany` entre `Filme` e `Ator` foi modelado corretamente com `@JoinTable`. As migrations SQL cobrem todas as tabelas necessárias e usam boas práticas como `BIGSERIAL`, chaves primárias compostas na tabela de associação e `ON DELETE CASCADE`.

## Problemas Técnicos Críticos

O método `atualizarFilme` em `FilmeService` não compila por dois motivos simultâneos: existe uma referência à variável inexistente `a` na expressão `if (a)`, e o método declara retorno `FilmeDto` mas não possui nenhum `return`. O projeto inteiro não sobe enquanto esses erros existirem.

`AtorNotFoundException` foi criada como uma classe Java comum, sem estender `RuntimeException`. Como consequência, ela não pode ser usada como exceção: qualquer tentativa de `throw new AtorNotFoundException()` resulta em erro de compilação, e ela não seria capturada pelo `GlobalExceptionHandler` mesmo se fosse lançada.

As classes `AtorService`, `GeneroService`, `AtorController` e `GeneroController` estão completamente vazias, sem qualquer implementação. O exercício exige que as entidades de apoio (`Ator` e `Genero`) tenham pelo menos os endpoints de criação (POST) e listagem (GET). Sem isso, não é possível nem cadastrar atores e gêneros para testar o CRUD principal de filmes. Além disso, `AtorService` e `GeneroService` não possuem a anotação `@Service`, então o Spring não registra esses beans e a injeção de dependência falha quando os controllers forem implementados.

`AtorDto` foi declarado como `interface` em vez de `record` ou `class`. Um DTO como interface não tem campos, construtores nem métodos concretos, portanto não consegue transportar nenhum dado entre camadas. Já `GeneroDto` é um record válido, mas está completamente vazio: sem campos, é impossível passar os dados de um gênero pelo DTO.

## Problemas Funcionais e de Design

O exercício especifica `PATCH /filmes/{id}` para atualizações, mas o controller implementou `@PutMapping()` sem id na URL. Os verbos HTTP têm semântica diferente: PUT substitui o recurso completo e deve receber o identificador na URL; PATCH aplica atualização parcial. O endpoint correto seria `@PatchMapping("/{id}")` recebendo o id como `@PathVariable`. Além disso, o método `atualizarFilme` do service (que trata o PATCH com `FilmePatchDTO`) nunca é chamado por nenhum endpoint do controller.

Em `FilmePatchDTO`, o campo `id` está marcado com `@NotNull` e pertence ao corpo da requisição. Em REST, o id do recurso a ser modificado deve vir na URL (`/filmes/{id}`), não no corpo. O corpo do PATCH deve conter apenas os campos que se deseja atualizar. Manter o id no body cria redundância e obriga o cliente a enviar um dado que já está na URL.

O exercício instrui explicitamente que a validação do campo de texto em branco deve ficar no Service, nunca no Controller. O `@NotBlank` colocado no campo `nome` de `FilmeDto` delega essa responsabilidade ao framework (Bean Validation), que executa a verificação na camada de controller antes de o service ser chamado. A validação deve ser feita programaticamente dentro do método `incluirFilme`.

O método `alterarFilme` (atualização completa) está sem `@Transactional`. Sem isso, se ocorrer uma falha após modificar o objeto mas antes de concluir o `save`, o banco pode ficar em estado inconsistente. O método de exclusão `deletarFilme` também pode se beneficiar de `@Transactional`.

Em `pesquisarFilme` e `listarFilmes`, o acesso `filme.getGenero().getId()` pode lançar `NullPointerException` se o campo `genero_id` no banco for nulo, pois a coluna não tem constraint `NOT NULL` na migration de `filme`. A correção pode ser feita na migration (adicionando `NOT NULL`) ou com verificação de nulo no código antes de acessar o id do gênero.

O método `findAllById` do Spring Data JPA retorna silenciosamente uma lista parcial quando algum ID não existe. Se for enviada a lista `[1, 2, 999]` e o ator `999` não existir, o método retorna apenas `[ator1, ator2]` sem qualquer aviso ou exceção. A forma correta de tratar isso é comparar o tamanho da lista retornada com o número de IDs enviados e lançar uma exceção quando houver divergência.

Em `Genero.java`, o campo `filmes` recebeu `@ToString.Exclude` para evitar loop infinito no `toString`. No entanto, o `@Data` do Lombok também gera `equals()` e `hashCode()` incluindo esse campo. Acessar a lista de filmes dentro dessas operações pode disparar uma consulta ao banco ou causar `LazyInitializationException` fora de um contexto transacional. O campo também deve receber `@EqualsAndHashCode.Exclude`.

A primeira migration de `ator` cria a coluna `id` como `BIGINT PRIMARY KEY` sem geração automática. Isso exigiria que o cliente enviasse o id manualmente ao criar um ator. Uma segunda migration foi necessária para corrigir isso com `ADD GENERATED BY DEFAULT AS IDENTITY`. Idealmente a coluna já seria criada como `BIGSERIAL` ou com `GENERATED BY DEFAULT AS IDENTITY` desde o início, como foi feito em `genero` e `filme`.

Por fim, o import do `@Transactional` usado é `jakarta.transaction.Transactional` em vez de `org.springframework.transaction.annotation.Transactional`. Ambos funcionam em projetos Spring Boot modernos, mas a versão do Spring é a convencional na comunidade e oferece mais opções de configuração como `propagation`, `isolation` e `readOnly`.

## Avaliação Final

O exercício demonstra compreensão dos conceitos fundamentais: a estrutura em camadas está correta, o tratamento de exceções tem uma base boa, os relacionamentos JPA foram modelados adequadamente e a decisão de usar Flyway com migrations versionadas é uma boa prática. Porém, o projeto está incompleto e não compila em seu estado atual. Três das seis classes de service/controller das entidades de apoio estão vazias, o método principal de PATCH tem um erro de sintaxe que impede a compilação, e vários problemas funcionais impediriam o correto funcionamento da API mesmo que o projeto subisse. O próximo passo é corrigir os erros de compilação antes de tudo, depois implementar as entidades de apoio e por último revisar os pontos de design indicados nos comentários.
