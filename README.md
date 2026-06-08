# 📱 Totens - Self-Service Kiosk
<img src="https://github.com/user-attachments/assets/d2e0b306-8960-4990-b4ea-0d5468021be0" align="right" height="177">

Outro ponto interessante sobre PDVs, além da conexão com pinpads, é sobre os **totens de autoatendimento** (self-service point of sale (POS) system), como os do McDonald’s, Burger King ou redes de varejo como Lojas Americanas, que já são um outro nível de sistema, bem diferente da relação simples `PDV ↔ pinpad`. Ali não estamos falando só de um “canal de comunicação”, mas de um sistema embarcado completo com software de alto nível, mesmo que visualmente pareça “só uma TV Android com touch”.

O totem é um sistema embarcado de aplicação, rodando sobre um SO completo, usando protocolos web modernos para backend e, quando necessário, protocolos TEF proprietários para pagamento. Ele substitui o vendedor humano porque implementa exatamente a lógica que antes estava distribuída entre PDV, operador e gerente, só que de forma automatizada, controlada e escalável.

Na prática, o totem é um **terminal de autoatendimento (self-service kiosk)**. Ele é um computador embarcado — normalmente um SoC ARM ou x86 — rodando um *sistema operacional completo*, quase sempre Android (AOSP customizado), Linux embarcado ou Windows IoT. Diferente do pinpad, esse sistema *executa lógica de negócio*, toma decisões, controla fluxo de compra, integra meios de pagamento, aplica regras fiscais, conversa com backend, gerencia estado de sessão e lida com exceções. Isso, por definição, já caracteriza um sistema embarcado moderno, mesmo usando um SO “grande”.

<img src="https://github.com/user-attachments/assets/8390b82a-7e79-42ba-9029-35c74096ca82" align="right" height="277">

Do ponto de vista de software, a aplicação que você vê na tela é geralmente um **aplicativo kiosk-mode**, escrito em Android (Java/Kotlin), Web (HTML/JS rodando num WebView), ou até engines multiplataforma. Ele roda em modo bloqueado, sem acesso ao sistema, sem barra de navegação, sem multitarefa visível, exatamente para garantir segurança e previsibilidade. É comum o Android ser altamente customizado, sem Play Services, sem launcher padrão, com permissões fixas e watchdog reiniciando a aplicação se algo falhar.

Quando falamos de protocolo, aí entra um ponto crucial: o totem não fala “direto” com o adquirente financeiro como um pinpad clássico. Ele atua como um *cliente de múltiplos serviços backend*. A seleção de produtos, preços, promoções, combos e impostos vem de APIs REST ou GraphQL internas da rede, normalmente sobre HTTPS. A finalização da compra gera uma ordem que é enviada para um backend central, que por sua vez conversa com o ERP, o sistema de cozinha (KDS), o estoque e o fiscal. Tudo isso é feito com protocolos de aplicação padrão da web, quase sempre REST/JSON, às vezes gRPC, e filas para eventos assíncronos.

O pagamento, por sua vez, costuma seguir dois caminhos. Em muitos casos, o totem **controla um pinpad externo**, usando exatamente aqueles protocolos proprietários de TEF que você já conhece. Nesse cenário, o totem faz o papel do PDV: inicia transação, envia valor, aguarda resposta e conclui a venda. Em outros modelos mais modernos, especialmente com pagamento por QR Code, NFC ou carteiras digitais, o próprio totem se integra a um **SDK de pagamento** fornecido pelo adquirente ou gateway, usando APIs de alto nível, e o pinpad vira apenas um periférico de captura segura (ou nem existe).

Portanto, diferente do pinpad isolado, o totem é um **orquestrador**. Ele coordena interface, regras de negócio, pagamentos, integrações e experiência do usuário. O protocolo dominante ali não é mais serial ou USB com mensagens binárias, mas sim protocolos de aplicação de alto nível, principalmente HTTP/HTTPS com JSON, autenticação, versionamento e observabilidade. A comunicação com hardware específico — leitor NFC, impressora térmica, pinpad — continua existindo, mas fica encapsulada em drivers, SDKs ou serviços locais.


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
