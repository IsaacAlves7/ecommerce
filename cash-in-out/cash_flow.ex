defmodule CashFlow do
  @moduledoc """
  API pública do sistema de Cash-in e Cash-out.

  ## Exemplo de uso

      # Criar uma conta
      {:ok, "conta_001"} = CashFlow.create_account("conta_001", "João Silva", 1000.0)

      # Depositar (Cash-in)
      {:ok, tx} = CashFlow.cash_in("conta_001", 500.0, "Salário")

      # Sacar (Cash-out)
      {:ok, tx} = CashFlow.cash_out("conta_001", 200.0, "Conta de luz")

      # Verificar saldo
      {:ok, balance} = CashFlow.balance("conta_001")

      # Ver extrato
      {:ok, statement} = CashFlow.statement("conta_001")
  """

  alias CashFlow.Account

  defdelegate create_account(id, owner, initial_balance \\ 0.0), to: Account, as: :create
  defdelegate cash_in(account_id, amount, description \\ "Depósito"), to: Account
  defdelegate cash_out(account_id, amount, description \\ "Saque"), to: Account
  defdelegate balance(account_id), to: Account
  defdelegate statement(account_id), to: Account
  defdelegate recent_transactions(account_id, limit \\ 10), to: Account
  defdelegate close_account(account_id), to: Account, as: :close

  @doc """
  Imprime um extrato formatado no terminal.
  """
  @spec print_statement(String.t()) :: :ok | {:error, term()}
  def print_statement(account_id) do
    with {:ok, stmt} <- statement(account_id) do
      IO.puts(format_statement(stmt))
    end
  end

  @doc """
  Realiza uma transferência entre duas contas.
  """
  @spec transfer(String.t(), String.t(), float(), String.t()) ::
          {:ok, map()} | {:error, term()}
  def transfer(from_id, to_id, amount, description \\ "Transferência") do
    with {:ok, out_tx} <- cash_out(from_id, amount, "#{description} → #{to_id}"),
         {:ok, in_tx} <- cash_in(to_id, amount, "#{description} ← #{from_id}") do
      {:ok, %{cash_out: out_tx, cash_in: in_tx}}
    end
  end

  # ── Formatting ─────────────────────────────────────────────────────────────

  defp format_statement(stmt) do
    separator = String.duplicate("─", 60)
    thin_sep = String.duplicate("·", 60)

    header = """

    ╔══════════════════════════════════════════════════════════╗
    ║              EXTRATO DE CONTA – CashFlow                 ║
    ╚══════════════════════════════════════════════════════════╝
    """

    info = """
    #{separator}
     Conta   : #{stmt.account_id}
     Titular : #{stmt.owner}
     Saldo   : #{format_currency(stmt.balance)}
     Criada  : #{format_datetime(stmt.created_at)}
     Total   : #{stmt.total_transactions} transação(ões)
    #{separator}
    """

    transactions_header = "  Data/Hora            Tipo           Valor        Saldo\n#{thin_sep}"

    transactions =
      stmt.transactions
      |> Enum.map(&format_transaction/1)
      |> Enum.join("\n")

    footer = """
    #{separator}
     Saldo atual: #{format_currency(stmt.balance)}
    #{separator}
    """

    header <> info <> transactions_header <> "\n" <> transactions <> "\n" <> footer
  end

  defp format_transaction(tx) do
    alias CashFlow.Transaction

    type_str = Transaction.type_label(tx.type)
    amount_str = format_currency(tx.amount)
    balance_str = format_currency(tx.balance_after)
    date_str = format_datetime(tx.inserted_at)

    color =
      case tx.type do
        :cash_in -> "\e[32m"
        :cash_out -> "\e[31m"
      end

    reset = "\e[0m"

    "  #{date_str}  #{color}#{String.pad_trailing(type_str, 13)}#{reset}  " <>
      "#{String.pad_leading(amount_str, 12)}  #{String.pad_leading(balance_str, 12)}" <>
      "\n    └─ #{tx.description}  [#{tx.id}]"
  end

  defp format_currency(amount) when is_float(amount) do
    "R$ #{:erlang.float_to_binary(amount, decimals: 2)}"
  end

  defp format_currency(amount) when is_integer(amount) do
    "R$ #{amount}.00"
  end

  defp format_datetime(%DateTime{} = dt) do
    "#{zero_pad(dt.day)}/#{zero_pad(dt.month)}/#{dt.year} " <>
      "#{zero_pad(dt.hour)}:#{zero_pad(dt.minute)}:#{zero_pad(dt.second)}"
  end

  defp zero_pad(n), do: String.pad_leading("#{n}", 2, "0")
end
