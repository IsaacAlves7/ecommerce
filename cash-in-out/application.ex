defmodule CashFlow.Application do
  @moduledoc """
  Supervisor principal da aplicação CashFlow.
  Gerencia o Registry de contas e o DynamicSupervisor de processos de conta.
  """

  use Application

  @impl true
  def start(_type, _args) do
    children = [
      # Registry para localizar contas pelo ID
      {Registry, keys: :unique, name: CashFlow.Registry},

      # Supervisor dinâmico para processos de conta
      {DynamicSupervisor, name: CashFlow.AccountSupervisor, strategy: :one_for_one}
    ]

    opts = [strategy: :one_for_one, name: CashFlow.Supervisor]
    Supervisor.start_link(children, opts)
  end
end
