#!/usr/bin/env elixir
# Demo script — executa com: elixir demo.exs
# (após rodar: mix deps.get && mix compile)

# Inicializa a aplicação manualmente para o script
Application.ensure_all_started(:cash_flow)

alias CashFlow

IO.puts("\n\e[1;36m══════════════════════════════════════════════\e[0m")
IO.puts("\e[1;36m     CashFlow – Demo de Cash-in e Cash-out    \e[0m")
IO.puts("\e[1;36m══════════════════════════════════════════════\e[0m\n")

# ── 1. Criar contas ──────────────────────────────────────────────────────────

IO.puts("📂 Criando contas...")
{:ok, "joao"} = CashFlow.create_account("joao", "João Silva", 2000.0)
{:ok, "maria"} = CashFlow.create_account("maria", "Maria Oliveira", 500.0)

IO.puts("   ✅ João Silva  – saldo inicial: R$ 2000.00")
IO.puts("   ✅ Maria Oliveira – saldo inicial: R$ 500.00\n")

# ── 2. Cash-in (depósitos) ───────────────────────────────────────────────────

IO.puts("💰 Realizando Cash-ins...")
{:ok, tx1} = CashFlow.cash_in("joao", 1500.0, "Salário mensal")
IO.puts("   ✅ João recebeu R$ 1500.00 — novo saldo: R$ #{tx1.balance_after}")

{:ok, tx2} = CashFlow.cash_in("maria", 300.0, "Freelance")
IO.puts("   ✅ Maria recebeu R$ 300.00 — novo saldo: R$ #{tx2.balance_after}\n")

# ── 3. Cash-out (saques/pagamentos) ─────────────────────────────────────────

IO.puts("💸 Realizando Cash-outs...")
{:ok, tx3} = CashFlow.cash_out("joao", 800.0, "Aluguel")
IO.puts("   ✅ João pagou aluguel R$ 800.00 — novo saldo: R$ #{tx3.balance_after}")

{:ok, tx4} = CashFlow.cash_out("maria", 150.0, "Internet + streaming")
IO.puts("   ✅ Maria pagou contas R$ 150.00 — novo saldo: R$ #{tx4.balance_after}\n")

# ── 4. Tentativa com saldo insuficiente ─────────────────────────────────────

IO.puts("⚠️  Tentando saque maior que o saldo de Maria...")
case CashFlow.cash_out("maria", 9999.0, "Compra indevida") do
  {:error, :insufficient_funds} ->
    IO.puts("   ❌ Recusado: saldo insuficiente (saldo seguro!)\n")
  _ ->
    IO.puts("   Resultado inesperado\n")
end

# ── 5. Transferência entre contas ────────────────────────────────────────────

IO.puts("🔁 Transferindo R$ 500.00 de João para Maria...")
{:ok, %{cash_out: co, cash_in: ci}} =
  CashFlow.transfer("joao", "maria", 500.0, "Divisão de gastos")

IO.puts("   ✅ João enviou R$ 500.00 — saldo: R$ #{co.balance_after}")
IO.puts("   ✅ Maria recebeu R$ 500.00 — saldo: R$ #{ci.balance_after}\n")

# ── 6. Mais transações para João ─────────────────────────────────────────────

CashFlow.cash_in("joao", 200.0, "Reembolso despesas")
CashFlow.cash_out("joao", 95.0, "Supermercado")
CashFlow.cash_out("joao", 45.50, "Farmácia")
CashFlow.cash_in("joao", 750.0, "Bônus trimestral")

# ── 7. Extrato completo ──────────────────────────────────────────────────────

IO.puts("\n")
CashFlow.print_statement("joao")

IO.puts("\n")
CashFlow.print_statement("maria")

# ── 8. Consulta de saldo final ────────────────────────────────────────────────

{:ok, joao_balance} = CashFlow.balance("joao")
{:ok, maria_balance} = CashFlow.balance("maria")

IO.puts("📊 Resumo final:")
IO.puts("   João  → \e[32mR$ #{Float.round(joao_balance, 2)}\e[0m")
IO.puts("   Maria → \e[32mR$ #{Float.round(maria_balance, 2)}\e[0m")

# ── 9. Encerrar contas ────────────────────────────────────────────────────────

:ok = CashFlow.close_account("joao")
:ok = CashFlow.close_account("maria")

IO.puts("\n✅ Contas encerradas com sucesso.")
IO.puts("\e[1;36m══════════════════════════════════════════════\e[0m\n")
