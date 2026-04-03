defmodule CashFlowTest do
  use ExUnit.Case, async: false

  alias CashFlow

  setup do
    account_id = "test_#{:erlang.unique_integer([:positive])}"
    {:ok, ^account_id} = CashFlow.create_account(account_id, "Test User", 1000.0)

    on_exit(fn ->
      CashFlow.close_account(account_id)
    end)

    {:ok, account_id: account_id}
  end

  # ── create_account ─────────────────────────────────────────────────────────

  describe "create_account/3" do
    test "cria conta com saldo inicial" do
      id = "nova_#{:erlang.unique_integer([:positive])}"
      assert {:ok, ^id} = CashFlow.create_account(id, "Maria", 500.0)
      assert {:ok, 500.0} = CashFlow.balance(id)
      CashFlow.close_account(id)
    end

    test "cria conta sem saldo inicial (padrão 0.0)" do
      id = "vazia_#{:erlang.unique_integer([:positive])}"
      assert {:ok, ^id} = CashFlow.create_account(id, "Pedro")
      assert {:ok, 0.0} = CashFlow.balance(id)
      CashFlow.close_account(id)
    end

    test "falha ao criar conta duplicada", %{account_id: id} do
      assert {:error, :account_already_exists} = CashFlow.create_account(id, "Outro")
    end
  end

  # ── cash_in ────────────────────────────────────────────────────────────────

  describe "cash_in/3" do
    test "aumenta o saldo corretamente", %{account_id: id} do
      assert {:ok, tx} = CashFlow.cash_in(id, 300.0, "Depósito PIX")

      assert tx.type == :cash_in
      assert tx.amount == 300.0
      assert tx.balance_after == 1300.0
      assert {:ok, 1300.0} = CashFlow.balance(id)
    end

    test "múltiplos cash-ins acumulam corretamente", %{account_id: id} do
      {:ok, _} = CashFlow.cash_in(id, 100.0)
      {:ok, _} = CashFlow.cash_in(id, 200.0)
      {:ok, _} = CashFlow.cash_in(id, 50.0)

      assert {:ok, 1350.0} = CashFlow.balance(id)
    end

    test "retorna erro para conta inexistente" do
      assert {:error, :account_not_found} = CashFlow.cash_in("nao_existe", 100.0)
    end
  end

  # ── cash_out ───────────────────────────────────────────────────────────────

  describe "cash_out/3" do
    test "diminui o saldo corretamente", %{account_id: id} do
      assert {:ok, tx} = CashFlow.cash_out(id, 400.0, "Aluguel")

      assert tx.type == :cash_out
      assert tx.amount == 400.0
      assert tx.balance_after == 600.0
      assert {:ok, 600.0} = CashFlow.balance(id)
    end

    test "falha com saldo insuficiente", %{account_id: id} do
      assert {:error, :insufficient_funds} = CashFlow.cash_out(id, 9999.0)
      # Saldo não deve ter mudado
      assert {:ok, 1000.0} = CashFlow.balance(id)
    end

    test "permite sacar exatamente o saldo total", %{account_id: id} do
      assert {:ok, tx} = CashFlow.cash_out(id, 1000.0, "Saque total")
      assert tx.balance_after == 0.0
      assert {:ok, 0.0} = CashFlow.balance(id)
    end
  end

  # ── statement ──────────────────────────────────────────────────────────────

  describe "statement/1" do
    test "retorna extrato completo com transações", %{account_id: id} do
      {:ok, _} = CashFlow.cash_in(id, 500.0, "Salário")
      {:ok, _} = CashFlow.cash_out(id, 100.0, "Mercado")

      assert {:ok, stmt} = CashFlow.statement(id)

      assert stmt.account_id == id
      assert stmt.balance == 1400.0
      assert stmt.total_transactions == 2
      assert length(stmt.transactions) == 2
    end

    test "transações aparecem em ordem cronológica", %{account_id: id} do
      {:ok, _} = CashFlow.cash_in(id, 100.0, "Primeiro")
      {:ok, _} = CashFlow.cash_out(id, 50.0, "Segundo")

      {:ok, stmt} = CashFlow.statement(id)
      [first, second] = stmt.transactions

      assert first.description == "Primeiro"
      assert second.description == "Segundo"
    end
  end

  # ── transfer ───────────────────────────────────────────────────────────────

  describe "transfer/4" do
    test "transfere entre duas contas", %{account_id: from_id} do
      to_id = "destino_#{:erlang.unique_integer([:positive])}"
      {:ok, ^to_id} = CashFlow.create_account(to_id, "Destino", 0.0)

      assert {:ok, %{cash_out: out_tx, cash_in: in_tx}} =
               CashFlow.transfer(from_id, to_id, 250.0, "Pagamento")

      assert out_tx.type == :cash_out
      assert in_tx.type == :cash_in
      assert {:ok, 750.0} = CashFlow.balance(from_id)
      assert {:ok, 250.0} = CashFlow.balance(to_id)

      CashFlow.close_account(to_id)
    end

    test "falha na transferência por saldo insuficiente", %{account_id: from_id} do
      to_id = "destino2_#{:erlang.unique_integer([:positive])}"
      {:ok, ^to_id} = CashFlow.create_account(to_id, "Destino", 0.0)

      assert {:error, :insufficient_funds} =
               CashFlow.transfer(from_id, to_id, 9999.0)

      # Saldos não mudam
      assert {:ok, 1000.0} = CashFlow.balance(from_id)
      assert {:ok, 0.0} = CashFlow.balance(to_id)

      CashFlow.close_account(to_id)
    end
  end

  # ── recent_transactions ────────────────────────────────────────────────────

  describe "recent_transactions/2" do
    test "limita o número de transações retornadas", %{account_id: id} do
      for i <- 1..10 do
        CashFlow.cash_in(id, i * 10.0, "Tx #{i}")
      end

      assert {:ok, txs} = CashFlow.recent_transactions(id, 3)
      assert length(txs) == 3
    end
  end
end
