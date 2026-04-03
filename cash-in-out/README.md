# 🛒 CashFlow — Sistema de Cash-in e Cash-out em Elixir
<img src="https://img.shields.io/badge/Nubank-NPM-512BD4?style=flat&logo=Nubank&logoColor=white"> <img src="https://img.shields.io/badge/Bitcoin-NPM-gold?style=flat&logo=Bitcoin&logoColor=white"> <img src="https://img.shields.io/badge/Solidity-NPM-00ADD8?style=flat&logo=Solidity&logoColor=white"> <img src="https://img.shields.io/badge/PicPay-NPM-512BD4?style=flat&logo=PicPay&logoColor=white"> <img src="https://img.shields.io/badge/Mastercard-NPM-orange?style=flat&logo=Mastercard&logoColor=white"> <img src="https://img.shields.io/badge/PIX-NPM-cyan?style=flat&logo=Pix&logoColor=white">

<a href="https://github.com/IsaacAlves7/ecommerce"><img src="https://em-content.zobj.net/source/microsoft-teams/400/shopping-cart_1f6d2.png" align="right" height="77"></a>

Um sistema de Cash-in e Cash-out robusto em Elixir, com GenServer, supervisão de processos e histórico de transações. Sistema financeiro de Cash-in e Cash-out desenvolvido em Elixir, utilizando **GenServer**, **DynamicSupervisor**, **Registry** e o padrão OTP para gerenciamento de contas concorrentes e tolerantes a falhas.

## Arquitetura

```
CashFlow.Application (Supervisor)
├── CashFlow.Registry        (Registry – localiza contas por ID)
└── CashFlow.AccountSupervisor (DynamicSupervisor)
    ├── CashFlow.Account ["conta_001"]  (GenServer)
    ├── CashFlow.Account ["conta_002"]  (GenServer)
    └── ...
```

Cada conta é um **processo independente** identificado por um ID único no
`Registry`. O `DynamicSupervisor` gerencia o ciclo de vida dos processos,
garantindo isolamento de falhas entre contas.

## Estrutura do Projeto

```
cash_flow/
├── lib/
│   ├── cash_flow.ex                  # API pública + formatação de extrato
│   ├── cash_flow/
│   │   ├── account.ex                # GenServer da conta
│   │   ├── transaction.ex            # Struct de transação
│   │   └── application.ex            # Supervisor principal
├── test/
│   └── cash_flow_test.exs            # Testes ExUnit
├── demo.exs                          # Script de demonstração
└── mix.exs
```

## Instalação

```bash
cd cash_flow
mix deps.get
mix compile
```

## Uso Básico

```elixir
# Iniciar a aplicação
iex -S mix

# Criar uma conta com saldo inicial
{:ok, "conta_001"} = CashFlow.create_account("conta_001", "João Silva", 1000.0)

# Cash-in: depositar dinheiro
{:ok, tx} = CashFlow.cash_in("conta_001", 500.0, "Salário")

# Cash-out: sacar/pagar
{:ok, tx} = CashFlow.cash_out("conta_001", 200.0, "Aluguel")

# Verificar saldo
{:ok, balance} = CashFlow.balance("conta_001")
# => {:ok, 1300.0}

# Transferência entre contas
{:ok, %{cash_out: out, cash_in: in}} =
  CashFlow.transfer("conta_001", "conta_002", 300.0, "Divisão")

# Extrato formatado no terminal
CashFlow.print_statement("conta_001")

# Últimas 5 transações
{:ok, txs} = CashFlow.recent_transactions("conta_001", 5)

# Encerrar conta
:ok = CashFlow.close_account("conta_001")
```


## API Completa

| Função                                     | Descrição                            |
|--------------------------------------------|--------------------------------------|
| `create_account(id, owner, balance \\ 0.0)`| Cria nova conta                      |
| `cash_in(id, amount, description)`         | Deposita valor na conta              |
| `cash_out(id, amount, description)`        | Retira valor da conta                |
| `balance(id)`                              | Consulta saldo atual                 |
| `statement(id)`                            | Extrato completo                     |
| `recent_transactions(id, limit \\ 10)`     | Últimas N transações                 |
| `transfer(from, to, amount, description)`  | Transferência entre contas           |
| `print_statement(id)`                      | Imprime extrato formatado no terminal|
| `close_account(id)`                        | Encerra e remove a conta             |

## Respostas de Erro

| Erro                    | Causa                                    |
|-------------------------|------------------------------------------|
| `{:error, :account_not_found}`     | Conta não existe no sistema   |
| `{:error, :account_already_exists}`| ID de conta já em uso         |
| `{:error, :insufficient_funds}`    | Saldo insuficiente para saque |

## Testes

```bash
mix test
```

## Demo

```bash
mix run demo.exs
```

## Decisões de Design

- **GenServer por conta**: cada conta é um processo OTP independente — falhas
  em uma conta não afetam as demais.
- **Registry com nomes via**: localização de processos por ID de string sem
  acoplamento de PID.
- **DynamicSupervisor**: criação e remoção de contas em tempo de execução.
- **Imutabilidade**: o estado da conta é imutável e atualizado atomicamente
  pelo GenServer, sem condições de corrida.
- **Transações como lista invertida**: as transações são armazenadas em ordem
  reversa (mais recente primeiro) para eficiência de `prepend`.
