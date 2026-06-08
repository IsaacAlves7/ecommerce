Não existe um único sistema operacional usado por todos os totens do McDonald's no mundo.

Historicamente, muitos quiosques de autoatendimento do McDonald's utilizaram versões da família Windows Embedded, como o Windows Embedded POSReady 7 e posteriormente versões do Windows 10 IoT Enterprise. Essas versões do Windows foram criadas especificamente para caixas, terminais de pagamento, quiosques de autoatendimento e equipamentos de varejo. ([Avnet][1])

Há diversos relatos e registros de telas de erro, atualizações e até marcas d'água de ativação do Windows aparecendo em quiosques do McDonald's, o que reforça o uso de variantes do Windows em muitas instalações.

Ao mesmo tempo, a infraestrutura moderna do McDonald's é bastante heterogênea. Dependendo do país, fornecedor do equipamento e geração do quiosque, também existem implementações baseadas em Linux para alguns sistemas de autoatendimento, painéis digitais e sistemas de cozinha. ([Flavor365][2])

Do ponto de vista de arquitetura, um totem moderno costuma ser algo próximo de:

```text
+--------------------------+
| Interface do McDonald's  |
+--------------------------+
            |
            v
+--------------------------+
| Aplicação do Quiosque    |
+--------------------------+
            |
            v
+--------------------------+
| Windows IoT ou Linux     |
+--------------------------+
            |
            v
+--------------------------+
| Hardware Industrial      |
| Touchscreen              |
| Impressora               |
| NFC                      |
| Leitor de Cartão         |
+--------------------------+
```

Na prática, quando você toca na tela e monta um pedido, você não está interagindo diretamente com o Windows ou Linux. Existe uma aplicação própria do McDonald's rodando em modo quiosque ("kiosk mode"), bloqueando o acesso ao sistema operacional e exibindo apenas a interface de pedidos. ([TouchWo][3])

Se você vier da área de desenvolvimento, a analogia é semelhante a um navegador rodando em tela cheia, mas muito mais travado:

```text
Windows IoT
     |
     +-- Software do McDonald's
             |
             +-- Pedido
             +-- Pagamento
             +-- Impressão
             +-- Integração com cozinha
```

Hoje, se eu tivesse que apostar qual é o sistema operacional mais comum nos totens mais recentes do McDonald's, diria que provavelmente é alguma variante do **Windows IoT/Embedded**, porque ele oferece excelente suporte para periféricos industriais, impressoras térmicas, leitores de cartão, NFC, Active Directory e gerenciamento corporativo em larga escala. Porém, dependendo da região e do fornecedor, Linux também aparece em parte da infraestrutura. ([TouchWo][3])

O curioso é que, por trás daquela tela simples onde você escolhe um Big Mac, geralmente existe um computador x86 relativamente comum, muitas vezes com processador Intel, SSD, vários dispositivos USB internos e um sistema operacional corporativo bastante semelhante ao que você encontraria em caixas de supermercado, aeroportos e terminais de autoatendimento bancário. ([ghuntley.com][4])

[1]: https://www.avnet.com/msembedded/embedded-software/WEPOS7/windows-embedded-posready-7/?utm_source=chatgpt.com "Windows Embedded POSReady 7 | Avnet MS Embedded Solutions"
[2]: https://eathealthy365.com/the-operating-system-that-powered-billions-of-burgers/?utm_source=chatgpt.com "What OS Did McDonald's Use? The Full History (2026)"
[3]: https://touchwo.com/fil/who-makes-mcdonalds-self-ordering-kiosks/?utm_source=chatgpt.com "Who Makes McDonald's Self-Ordering Kiosks?"
[4]: https://ghuntley.com/mcdonalds/?utm_source=chatgpt.com "Why are McDonald’s Self Service Kiosks so hackable?"
