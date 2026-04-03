defmodule CashFlow.Account do
  @moduledoc """
  GenServer que gerencia o estado de uma conta financeira.
  Controla saldo, transações de Cash-in e Cash-out e histórico.
  """

  use GenServer, restart: :transient

  alias CashFlow.Transaction

  @type state :: %{
          id: String.t(),
          owner: String.t(),
          balance: float(),
          transactions: [Transaction.t()],
          created_at: DateTime.t()
        }

  # ── Client API ────────────────────────────────────────────────────────────

  @doc "Inicia um processo de conta vinculado ao Registry."
  @spec start_link(keyword()) :: GenServer.on_start()
  def start_link(opts) do
    account_id = Keyword.fetch!(opts, :id)
    GenServer.start_link(__MODULE__, opts, name: via(account_id))
  end

  @doc "Cria uma nova conta no sistema."
  @spec create(String.t(), String.t(), float()) :: {:ok, String.t()} | {:error, term()}
  def create(account_id, owner, initial_balance \\ 0.0)
      when is_binary(account_id) and is_binary(owner) and initial_balance >= 0 do
    opts = [id: account_id, owner: owner, initial_balance: initial_balance]

    case DynamicSupervisor.start_child(CashFlow.AccountSupervisor, {__MODULE__, opts}) do
      {:ok, _pid} -> {:ok, account_id}
      {:error, {:already_started, _}} -> {:error, :account_already_exists}
      error -> error
    end
  end

  @doc "Realiza um Cash-in (depósito) na conta."
  @spec cash_in(String.t(), float(), String.t()) ::
          {:ok, Transaction.t()} | {:error, term()}
  def cash_in(account_id, amount, description \\ "Depósito")
      when is_binary(account_id) and amount > 0 do
    call(account_id, {:cash_in, amount, description})
  end

  @doc "Realiza um Cash-out (saque/débito) na conta."
  @spec cash_out(String.t(), float(), String.t()) ::
          {:ok, Transaction.t()} | {:error, term()}
  def cash_out(account_id, amount, description \\ "Saque")
      when is_binary(account_id) and amount > 0 do
    call(account_id, {:cash_out, amount, description})
  end

  @doc "Consulta o saldo atual da conta."
  @spec balance(String.t()) :: {:ok, float()} | {:error, term()}
  def balance(account_id), do: call(account_id, :balance)

  @doc "Retorna o extrato completo da conta."
  @spec statement(String.t()) :: {:ok, map()} | {:error, term()}
  def statement(account_id), do: call(account_id, :statement)

  @doc "Retorna as últimas N transações da conta."
  @spec recent_transactions(String.t(), pos_integer()) ::
          {:ok, [Transaction.t()]} | {:error, term()}
  def recent_transactions(account_id, limit \\ 10),
    do: call(account_id, {:recent_transactions, limit})

  @doc "Encerra a conta e remove do sistema."
  @spec close(String.t()) :: :ok | {:error, term()}
  def close(account_id) do
    case Registry.lookup(CashFlow.Registry, account_id) do
      [{pid, _}] -> DynamicSupervisor.terminate_child(CashFlow.AccountSupervisor, pid)
      [] -> {:error, :account_not_found}
    end
  end

  # ── GenServer Callbacks ───────────────────────────────────────────────────

  @impl true
  def init(opts) do
    initial_balance = Keyword.get(opts, :initial_balance, 0.0)

    state = %{
      id: Keyword.fetch!(opts, :id),
      owner: Keyword.fetch!(opts, :owner),
      balance: initial_balance,
      transactions: [],
      created_at: DateTime.utc_now()
    }

    {:ok, state}
  end

  @impl true
  def handle_call(:balance, _from, state) do
    {:reply, {:ok, state.balance}, state}
  end

  def handle_call({:cash_in, amount, description}, _from, state) do
    new_balance = Float.round(state.balance + amount, 2)

    transaction = Transaction.new(:cash_in, amount, description, new_balance)

    new_state = %{
      state
      | balance: new_balance,
        transactions: [transaction | state.transactions]
    }

    {:reply, {:ok, transaction}, new_state}
  end

  def handle_call({:cash_out, amount, description}, _from, state) do
    if state.balance >= amount do
      new_balance = Float.round(state.balance - amount, 2)
      transaction = Transaction.new(:cash_out, amount, description, new_balance)

      new_state = %{
        state
        | balance: new_balance,
          transactions: [transaction | state.transactions]
      }

      {:reply, {:ok, transaction}, new_state}
    else
      {:reply, {:error, :insufficient_funds}, state}
    end
  end

  def handle_call(:statement, _from, state) do
    result = %{
      account_id: state.id,
      owner: state.owner,
      balance: state.balance,
      total_transactions: length(state.transactions),
      created_at: state.created_at,
      transactions: Enum.reverse(state.transactions)
    }

    {:reply, {:ok, result}, state}
  end

  def handle_call({:recent_transactions, limit}, _from, state) do
    recent =
      state.transactions
      |> Enum.take(limit)
      |> Enum.reverse()

    {:reply, {:ok, recent}, state}
  end

  # ── Helpers ───────────────────────────────────────────────────────────────

  defp via(account_id) do
    {:via, Registry, {CashFlow.Registry, account_id}}
  end

  defp call(account_id, message) do
    case Registry.lookup(CashFlow.Registry, account_id) do
      [{_pid, _}] ->
        GenServer.call(via(account_id), message)

      [] ->
        {:error, :account_not_found}
    end
  end
end
