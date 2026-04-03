defmodule CashFlow.MixProject do
  use Mix.Project

  def project do
    [
      app: :cash_flow,
      version: "1.0.0",
      elixir: "~> 1.15",
      start_permanent: Mix.env() == :prod,
      deps: deps(),
      description: "Sistema de Cash-in e Cash-out em Elixir"
    ]
  end

  def application do
    [
      extra_applications: [:logger],
      mod: {CashFlow.Application, []}
    ]
  end

  defp deps do
    [
      {:uuid, "~> 1.1"}
    ]
  end
end
