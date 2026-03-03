# Bank Java — CRUD com JDBC Puro

Projeto de estudo com objetivo de revisar conceitos fundamentais de Java, conexão com banco de dados via **JDBC puro** e aplicação de **Design Patterns** clássicos — sem frameworks como Spring Boot.

---

## Objetivo

Construir um CRUD completo de contas bancárias conectando diretamente ao banco de dados H2 em memória, sem abstrações de ORM ou injeção de dependência automática. Tudo na mão.

---

## Tecnologias

- Java 17+
- Maven
- JDBC (java.sql)
- H2 Database (em memória)

---

## Estrutura do Projeto

```
src/main/java/com/bankjava/account/
├── domain/
│   └── Account.java              # Entidade de negócio
├── Infrastructure/
│   ├── ConnectionFactory.java    # Gerencia a conexão com o banco
│   ├── DatabaseInitializer.java  # Cria a tabela ao iniciar
│   └── AccountRepository.java   # Operações CRUD
└── Application.java              # Ponto de entrada

src/main/resources/
└── schema.sql                    # DDL da tabela
```

---

## Design Patterns Aplicados

### Singleton — `ConnectionFactory`

Garante que apenas uma instância de `ConnectionFactory` exista durante toda a execução da aplicação. O construtor é privado e o acesso é feito exclusivamente pelo método `getInstance()`.

```java
public class ConnectionFactory {
    private static ConnectionFactory instance;

    private ConnectionFactory() {}

    public static ConnectionFactory getInstance() {
        if (instance == null) {
            instance = new ConnectionFactory();
        }
        return instance;
    }

    public Connection createConnection() {
        try {
            return DriverManager.getConnection(URL_BD, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
```

**Por que Singleton aqui?**  
Em projetos reais, instanciar múltiplos gerenciadores de conexão pode ser custoso e gerar inconsistências. O Singleton centraliza esse controle.

> Em ambientes multithread, o padrão correto seria usar **double-checked locking** com `volatile`. Deixado como evolução futura.

---

### Factory Method — `Account`

O construtor de `Account` é privado. A criação do objeto é feita exclusivamente pelo método estático `newAccount()`, garantindo que apenas objetos válidos sejam criados — sem ID (que será gerado pelo banco).

```java
public class Account {
    private long id;
    private int accountNumber;
    // ...

    private Account(int accountNumber, int accountAgency, String accountHolder,
                    double accountBalance, double accountEspecialLimitDefault) {
        // inicialização
    }

    public static Account newAccount(int accountNumber, int accountAgency,
                                     String accountHolder, double accountBalance,
                                     double accountEspecialLimitDefault) {
        return new Account(accountNumber, accountAgency, accountHolder,
                           accountBalance, accountEspecialLimitDefault);
    }
}
```

**Por que Factory Method aqui?**  
Impede que o ID seja setado antes da persistência no banco. O objeto nasce sem ID — o banco gera e devolve após o INSERT.

---

## JDBC — Conceitos Praticados

### Abrindo Conexão

```java
Connection connection = DriverManager.getConnection(url, user, password);
```

O `DriverManager` é responsável por abrir o canal com o banco. A `Connection` representa esse canal ativo.

---

### Try-with-resources

Qualquer recurso que implemente `AutoCloseable` pode ser declarado no `try` e será fechado automaticamente ao sair do bloco — mesmo em caso de exceção.

```java
try (Connection connection = ConnectionFactory.getInstance().createConnection();
     PreparedStatement ps = connection.prepareStatement(sql)) {

    // executa operações

} catch (SQLException e) {
    throw new RuntimeException(e);
}
```

**Por que isso importa?**  
Conexões com banco são recursos caros. Não fechar gera memory leak e pode travar a aplicação. O try-with-resources elimina esse risco.

---

### PreparedStatement

Diferente do `Statement` simples, o `PreparedStatement` separa o SQL dos dados via `?`. Isso evita **SQL Injection** e permite que o banco compile o SQL uma vez e reutilize.

```java
String sql = "INSERT INTO account (account_number, account_holder) VALUES (?, ?)";
PreparedStatement ps = connection.prepareStatement(sql);
ps.setInt(1, account.getAccountNumber());
ps.setString(2, account.getAccountHolder());
ps.executeUpdate();
```

---

### Recuperando ID Gerado pelo Banco

Após um INSERT, o banco gera o ID automaticamente via `AUTO_INCREMENT`. Para recuperá-lo:

```java
PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
ps.executeUpdate();

ResultSet rs = ps.getGeneratedKeys();
if (rs.next()) {
    account.setId(rs.getLong(1));
}
```

---

### ResultSet

O `ResultSet` funciona como um cursor que aponta para as linhas retornadas por um SELECT. Começa antes da primeira linha e avança com `next()`.

```java
// Uma linha esperada
if (resultSet.next()) {
    String holder = resultSet.getString("account_holder");
}

// Múltiplas linhas
while (resultSet.next()) {
    list.add(/* monta objeto */);
}
```

---

## CRUD Implementado

| Método | SQL | Descrição |
|---|---|---|
| `save(Account)` | INSERT | Persiste e retorna com ID gerado |
| `findById(long)` | SELECT WHERE id = ? | Retorna uma conta ou null |
| `findAll()` | SELECT * | Retorna lista de todas as contas |
| `update(long, Account)` | UPDATE WHERE id = ? | Atualiza registro existente |
| `deleteById(long)` | DELETE WHERE id = ? | Remove registro pelo ID |

---

## H2 — Banco em Memória

O H2 não requer instalação. É configurado diretamente na URL de conexão:

```
jdbc:h2:mem:bankjava;DB_CLOSE_DELAY=-1
```

- `mem:bankjava` — banco em memória com nome `bankjava`
- `DB_CLOSE_DELAY=-1` — mantém o banco ativo enquanto a JVM estiver rodando

**Console Web do H2**  
Para visualizar os dados durante a execução:

```java
org.h2.tools.Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
```

Acesse `http://localhost:8082` com as credenciais:
- JDBC URL: `jdbc:h2:mem:bankjava;DB_CLOSE_DELAY=-1`
- User: `sa`
- Password: *(vazio)*


