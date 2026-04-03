defmodule CashFlow.Transaction do
  @moduledoc """
  Representa uma transação financeira no sistema.
  """

  @type type :: :cash_in | :cash_out

  @type t :: %__MODULE__{
          id: String.t(),
          type: type(),
          amount: Decimal.t(),
          description: String.t(),
          balance_after: Decimal.t(),
          inserted_at: DateTime.t()
        }

  defstruct [:id, :type, :amount, :description, :balance_after, :inserted_at]

  @doc """
  Cria uma nova transação.
  """
  @spec new(type(), float() | integer(), String.t(), float() | integer()) :: t()
  def new(type, amount, description, balance_after)
      when type in [:cash_in, :cash_out] do
    %__MODULE__{
      id: generate_id(),
      type: type,
      amount: to_decimal(amount),
      description: description,
      balance_after: to_decimal(balance_after),
      inserted_at: DateTime.utc_now()
    }
  end

  defp generate_id do
    :crypto.strong_rand_bytes(8)
    |> Base.encode16(case: :lower)
  end

  defp to_decimal(value) when is_float(value), do: Float.round(value, 2)
  defp to_decimal(value) when is_integer(value), do: value * 1.0
  defp to_decimal(value), do: value

  @doc "Retorna o tipo da transação como string legível."
  def type_label(:cash_in), do: "Cash-in  ↑"
  def type_label(:cash_out), do: "Cash-out ↓"
end
