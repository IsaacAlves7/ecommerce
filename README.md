# 🛒 Ecommerce
<img src="https://img.shields.io/badge/Mercado_Pago-NPM-00ACCC?style=flat&logo=Mercado-Pago&logoColor=white"> <img src="https://img.shields.io/badge/Nubank-NPM-512BD4?style=flat&logo=Nubank&logoColor=white"> <img src="https://img.shields.io/badge/Bitcoin-NPM-gold?style=flat&logo=Bitcoin&logoColor=white"> <img src="https://img.shields.io/badge/Solidity-NPM-00ADD8?style=flat&logo=Solidity&logoColor=white"> <img src="https://img.shields.io/badge/PicPay-NPM-512BD4?style=flat&logo=PicPay&logoColor=white"> <img src="https://img.shields.io/badge/Mastercard-NPM-orange?style=flat&logo=Mastercard&logoColor=white"> <img src="https://img.shields.io/badge/PIX-NPM-cyan?style=flat&logo=Pix&logoColor=white">

<a href="https://github.com/IsaacAlves7/ecommerce"><img src="https://em-content.zobj.net/source/microsoft-teams/400/shopping-cart_1f6d2.png" align="right" height="77"></a>

Esse repositório é focado em soluções de projetos financeiros, de integrações com plataformas de pagamento e emissão de notas, e transações bancárias via APIs ou SDKs, para comércios eletrônicos (Ecommerces e Ebusiness) ou payments através de POS - Ponto de Venda.

O projeto de integração do CRM e ERP, como por exemplo, o Pipefy e Oracle NetSuite POS com o Gateway de Pagamentos ou plataforma de pagamentos como a Stone para realizar a emição de notas dos comprovantes da compra de produtos para os clientes da loja. 

Portanto, o nosso objetivo é evitar toda a parte manual entre vendedor e cliente, e automatizar o máximo possível as transações de vendas com as soluções mais modernas utilizadas no mercado na nossa aplicação em produção. O diagrama acima, é usado para a melhor compreensão do projeto.

Existem diferentes formas, métodos, técnicas e estratégias para construir aplicações financeiras, você pode ou não incluir banco de dados, sendo que para uma aplicação desse porte é necessário armazenar a maior quantidade de dados possível. Além de se atentar em requisitos técnicos como qualidade, persistência, escalabilidade, disponibilidade e capacidade.

System Design: Construindo um Sistema de Processamento de Pagamentos

Pagamentos online em sites de comércio eletrônico agora são onipresentes. Um site apresenta uma página de pagamento, seu cartão é cobrado e a transação é concluída, aparentemente como mágica. Mas você já se perguntou como seu pagamento realmente passa por vários intermediários, do processador à rede de cartões e ao seu banco antes de ser concluído? Ou já ficou frustrado por ser cobrado duas vezes por uma única compra? Ou se perguntou como o sistema garante que seu pagamento não seja perdido durante o trânsito? 

Vamos projetar um processador de pagamentos com idempotência embutida para evitar cobranças duplicadas e fortes garantias de consistência para garantir que cada transação seja registrada de forma confiável — mesmo quando algo der errado.

Antes de explicar como funciona nossa aplicação, vamos explicar alguns termos técnicos que são teoricamente importantes nesse projeto:

<div align="center"><img src="https://github.com/user-attachments/assets/e685cff7-a967-44a8-8e7b-8dcabeaf401e"></div>

> [!Important]
> É importantíssimo que nesse cenário de System Design (Infraestrutura) seja aplicado com boas técnicas de Design Patterns, Arquiteturas de Software, DDD + TDD + BDD, assim como padrões de microsserviços.

> [!Tip]
> Uma dica que vale a pena ser citada, é que cada negócio funciona de um forma específica no seu sistema de vendas e comércio, portanto, preste muita atenção na forma de trabalhar!

# 💰 POS - Point of Sale
<a href=""><img src="https://github.com/user-attachments/assets/4614eae8-d3bb-495f-8642-ef90f9dece36" align="right" height="177"></a>

Um ponto crucial nessa integração, é o **POS - Point of Sale (PDV - Ponto de Venda)** é um sistema utilizado por empresas para processar vendas e transações com clientes. Ele combina hardware e software para ajudar a gerenciar diversas operações no ponto de venda, como registrar compras, processar pagamentos (cartão de crédito, débito, dinheiro, e pagamentos móveis), emitir recibos e monitorar o estoque. 

Além de transações financeiras, um sistema POS pode integrar funções adicionais, como gerenciamento de inventário, relatórios de vendas e programas de fidelidade de clientes. Então, o hardware de um POS geralmente inclui dispositivos como um terminal de vendas (caixa registradora), **leitores de cartões (PIN pads - Terminais POS - Maquininhas)**, scanners de código de barras, e impressoras de recibos. O software do POS é o que gerencia todo o processo de venda e as operações relacionadas. 

![modelos-maquininhas-stone-compativeis-1](https://github.com/user-attachments/assets/0f22fecb-7eb9-4636-88aa-9aa9e4d7f4e3)

Hoje em dia, muitos POS funcionam também em tablets ou smartphones, conectando-se a **gateways de pagamento** (Stone) para processar as transações com segurança. Um gateway de pagamento é uma tecnologia que permite a comunicação entre o sistema de pagamento de um comerciante (como uma loja online, uma aplicação de vendas ou um terminal de ponto de venda) e as redes financeiras que processam os pagamentos (como bancos, operadoras de cartões de crédito ou outras instituições financeiras). Essencialmente, o gateway de pagamento é responsável por autenticar, autorizar e processar transações de pagamento, garantindo que o dinheiro seja transferido corretamente entre o cliente e o comerciante.

A **bandeira** (brand) identifica a rede à qual o cartão está associado, como **VISA**, **Mastercard**, **American Express**, entre outras. As bandeiras representam as empresas responsáveis pelo processamento das transações, pela definição das taxas, e pela padronização de aceitação dos cartões nos estabelecimentos comerciais. Portanto, o campo `"brand": "VISA"` refere-se à bandeira do cartão de crédito. 

A **impressão de Nota Fiscal Eletrônica (NFe)** é o processo de gerar uma representação física de uma NFe, também conhecida como **DANFE** (Documento Auxiliar da Nota Fiscal Eletrônica). A NFe é um documento digital que registra a transação comercial, mas, para facilitar o transporte e conferência da mercadoria, a legislação exige o DANFE, que é uma versão simplificada e impressa da NFe.

Aqui estão os principais aspectos da impressão de NFe:

1. **DANFE (Documento Auxiliar da NFe)**: O DANFE é um documento que acompanha a mercadoria durante o transporte, permitindo que a fiscalização consulte a NFe registrada eletronicamente. Inclui informações essenciais sobre a transação, como número da NFe, chave de acesso (um código único para consulta online), descrição dos produtos, valores e impostos. A impressão é geralmente em papel A4 ou em formatos menores, e é obrigatória para o transporte de mercadorias.

1. **Chave de Acesso e Código de Barras**: O DANFE inclui a **chave de acesso**, que é um código numérico que permite consultar a NFe na Secretaria da Fazenda (SEFAZ) pela internet.
Também possui um **código de barras**, facilitando a leitura e conferência das informações por meio de leitores ópticos.

1. **Finalidade da Impressão de NFe**: A impressão permite a conferência visual das informações da NFe, o que é útil em casos de fiscalização ou auditoria. Serve como comprovante para o consumidor e como registro da operação, embora o documento digital seja o que possui validade fiscal.

1. **Diferença entre DANFE e NFe**: O DANFE não possui validade fiscal, apenas a NFe eletrônica registrada nos sistemas da SEFAZ tem. O DANFE é um resumo e representação visual da NFe para fins de transporte e conferência, enquanto a NFe digital contém todos os dados fiscais.

5. **Softwares para Impressão de NFe**: Muitas empresas utilizam sistemas integrados de ERP ou software específico para emissão de NFe que também geram o DANFE automaticamente, facilitando a impressão e o armazenamento.

A impressão de NFe em forma de DANFE é, portanto, uma forma prática e legalmente exigida de registrar as transações e garantir a segurança no transporte de mercadorias, mantendo a validade fiscal da NFe digital armazenada.

> [!Warning]
> A nota/comprovante impresso pela maquininha de cartão não é nota fiscal e não substitui uma nota fiscal em nenhum cenário legal ou contábil no Brasil. Ela é apenas um comprovante de pagamento.

Em resumo, o POS é essencial para a operação de qualquer comércio que precise registrar e gerenciar suas vendas e pagamentos, sendo fundamental para o controle do fluxo financeiro e a organização interna da empresa. Existem diferentes tipos de sistemas de POS (Point of Sale), que variam conforme o porte do negócio, o setor e as necessidades específicas de cada operação. Aqui estão os principais tipos de POS:

1. **POS Tradicional (Desktop POS)**: Consiste em um sistema completo de ponto de venda fixo, geralmente com hardware dedicado, como um computador, monitor, gaveta de dinheiro, leitor de código de barras e pin pad. Comum em lojas de varejo de médio e grande porte, restaurantes e supermercados. O sistema oferece integração com inventário, controle de vendas, emissão de recibos e relatórios detalhados.

<img src="https://github.com/user-attachments/assets/fc586928-112a-43f0-b63f-3f2cebaa44e9" height="37" align="right">

2. **POS Móvel (Mobile POS)**: Funciona em dispositivos móveis, como smartphones ou tablets, usando um aplicativo de POS que pode se conectar a leitores de cartão portáteis via Bluetooth ou entrada de áudio. Ideal para pequenos negócios e vendas externas, como food trucks, feiras e vendedores autônomos. Leve e fácil de transportar, permite que vendedores processem pagamentos em movimento, mantendo a flexibilidade. **InfinitePay** é uma plataforma brasileira de serviços financeiros e soluções de pagamento voltada para micro, pequenos e médios empreendedores. Ela oferece um ecossistema completo para vender e receber, que inclui maquininhas de cartão, aceitar pagamentos no celular (Tap to Pay/InﬁniteTap), conta digital integrada, Pix sem taxas, links de pagamento, gestão de cobranças, loja online e outros serviços financeiros para comerciantes e empresas no Brasil. Em outras palavras, InfinitePay não é só uma maquininha, mas uma solução completa de recebimento e gestão financeira, incluindo ferramenta para aceitar cartões físicos, pagamentos por aproximação com smartphone, gestão de saldo e movimentação do dinheiro do negócio.

3. **POS de Maquininha (Standalone POS)**: São as "maquininhas de cartão" que funcionam de forma autônoma, sem a necessidade de um sistema central de ponto de venda. Elas podem se conectar à internet via Wi-Fi, chip 3G/4G e podem realizar operações de pagamento com cartão e QR code. Usadas principalmente por pequenos negócios, autônomos e profissionais liberais, essas maquininhas são simples e práticas.

4. **POS Autoatendimento (Self-service POS)**: Usado em totens de autoatendimento, comuns em fast-foods, cinemas, hotéis e aeroportos, permitindo que o cliente faça pedidos e pague sem a necessidade de um operador. Oferece conveniência e pode reduzir filas e custos de mão-de-obra, otimizando a experiência do cliente. Em geral, inclui tela de toque e leitor de cartão para pagamento direto.

5. **POS Web ou Cloud POS**: Um sistema de POS baseado em nuvem que funciona em navegadores web, eliminando a necessidade de hardware dedicado. Acessível em dispositivos conectados à internet, permite que dados de vendas e inventário sejam acessados de qualquer lugar, sendo ideal para negócios com várias unidades ou que necessitam de flexibilidade. Facilita a integração com outros sistemas e possui backup automático dos dados, garantindo segurança e fácil recuperação de informações.

6. **POS All-in-One**: Um sistema completo que integra hardware e software em um único dispositivo compacto, muitas vezes com tela de toque e leitor de cartão embutido. Projetado para simplificar a operação e reduzir espaço físico, é ideal para lojas que precisam de um sistema eficiente e esteticamente agradável. Os modelos mais modernos incluem suporte para NFC, QR code, e são altamente personalizáveis para diferentes setores.

7. **POS Multicanal (Omnichannel POS)**: Esse tipo de POS conecta as vendas em loja física, online e outros canais de vendas, permitindo um gerenciamento centralizado de inventário, pedidos e clientes. Ideal para empresas que operam com lojas físicas e e-commerce, oferecendo uma visão integrada de todas as transações e melhorando a experiência de compra do cliente. Esse sistema facilita o controle de inventário e permite que o cliente realize compras online e retire na loja física, por exemplo.

Esses tipos de POS ajudam negócios de diferentes tamanhos e setores a gerenciar operações de pagamento, vendas e inventário de acordo com suas necessidades e o nível de mobilidade ou integração exigido. A escolha depende da estrutura, volume de transações e preferências de cada estabelecimento.

<img src="https://static.wixstatic.com/media/521dfd_2fb7cf0f02e142fdbeb2473a0c4717a1~mv2.png/v1/fill/w_980,h_232,al_c,q_85,usm_0.66_1.00_0.01,enc_auto/NetSuite-logo-half-light.png" align="right" height="77">

Partindo pro nosso PDV, o **Oracle NetSuite** é uma plataforma de software empresarial baseada em nuvem que oferece uma gama de aplicações para gestão financeira, ERP (Planejamento de Recursos Empresariais), CRM (Gestão de Relacionamento com o Cliente), e e-commerce. É amplamente utilizado por empresas de diferentes tamanhos para gerenciar operações de negócios de forma integrada. Podemos relacionar o Oracle NetSuite como um sistema de PDV - Ponto de Venda ou ponto de serviço (POS - Point of Sale ou Point of Service) ao utilizar máquinas PIN Pads para realizar vendas. O NetSuite oferece funcionalidades de gerenciamento de vendas, incluindo a integração com dispositivos de pagamento, que podem incluir terminais de cartão, como PIN Pads.

Dentro da plataforma, iremos utilizar o **Oracle NetSuite Pay** que é uma solução integrada de processamento de pagamentos projetada especificamente para empresas que usam o NetSuite. O NetSuite Pay permite que você aceite pagamentos com cartão de crédito on-line, por telefone, no ponto de venda (POS) ou por outro método de pagamento digital, tudo com segurança de dados compatível com PCI. O NetSuite mantém um registro completo do ciclo de vida completo da transação, dando a você uma visão de 360 ​​graus do histórico de transações do seu cliente e rastreamento detalhado de auditoria, mantendo a segurança dos dados do cartão de pagamento. 

Para instalar o NetSuite Pay:

1. Acesse SuiteApps.
2. No campo Pesquisar , digite "NetSuite Pay".
3. Clique no bloco SuiteApp.
4. Na página de detalhes do SuiteApp, clique em Instalar.

O NetSuite Pay é um SuiteApp gerenciado. Quando melhorias ou novos recursos são adicionados ao SuiteApp, sua conta é atualizada automaticamente. Para obter mais informações sobre como instalar, atualizar e excluir o SuiteApps do SuiteApp Marketplace, consulte Instalação do SuiteApp. O NetSuite Pay oferece preços simples e transparentes usando um aplicativo simplificado e um processo de integração.

Os clientes do NetSuite Pay podem:

- Conclua um processo de inscrição e integração incorporado ao NetSuite (a subscrição e o serviço comercial são fornecidos pela <a href="https://developers.versapay.com/">Versapay</a>).

- Aceite pagamentos com cartão de crédito e ACH em canais de comércio eletrônico, pontos de venda e contas a receber diretamente no NetSuite.

- Acelere os tempos de cobrança com faturas automatizadas do tipo clique para pagar como parte dos seus fluxos de trabalho nativos para maximizar a eficiência e eliminar horas gastas na reconciliação de pagamentos com o NetSuite.

- Processe pagamentos com segurança e proteja você e seus clientes tokenizando e armazenando com segurança dados confidenciais de cartão de crédito em conformidade com o **PCI DSS**.

O NetSuite Pay funciona em conjunto com o Payment Link para:

- Envie faturas eletrônicas com um link de pagamento ou código QR para uma página de pagamento segura.

- Configure o link de pagamento para selecionar determinados métodos de pagamento, permitir pagamentos parciais e incluir um logotipo e informações da empresa.

- Envie notificações de pagamento usando modelos de e-mail personalizados para informar os clientes quando o pagamento for aceito ou rejeitado.

- Permita que os clientes visualizem e paguem suas contas com um clique, usando cartão de crédito ou débito.

O NetSuite Pay também funciona com o NetSuite Customer Center, o SuiteCommerce MyAccount e o SuiteCommerce Advanced My Account para que compradores empresariais visualizem faturas, adicionem e gerenciem métodos de pagamento com segurança e paguem com mais rapidez e eficiência.

Principais recursos e benefícios:

- Pagamentos online – Simplifica o processo de recebimento do seu dinheiro e ajuda a reduzir o número de processos manuais.

- Gerenciamento de métodos de pagamento – permite que você escolha os métodos de pagamento que aceita, incluindo tipos de cartões de crédito e ACH.

- Tempo de processamento aprimorado – Os pagamentos são processados ​​automaticamente com pouca ou nenhuma necessidade de interação humana.

- Pagamentos por e-mail – Use o Link de pagamento para permitir que seus clientes enviem o pagamento clicando no link de pagamento em um e-mail.

- Integração aprimorada – O NetSuite Pay integra-se com o Customer Center, SuiteCommerce MyAccount, SuiteCommerce Advanced MyAccount e SuiteCommerce Advanced Webstore.

Para integrar com sucesso um PinPad ao NetSuite, você precisará garantir a compatibilidade entre o seu PinPad, gateway de pagamento e o sistema de pagamento do NetSuite. Se não existir um SuiteApp pré-construído, pode ser necessário usar as APIs do SuiteScript e do NetSuite para criar soluções personalizadas. Certifique-se sempre de que sua integração seja segura e compatível com os padrões de processamento de pagamentos.

Temos muitas soluções de pagamentos automatizados para a emição de comprovantes. Basta optar a que melhor se adequa no seu projeto.

<img width="1100" height="620" alt="pos-(1)" src="https://github.com/user-attachments/assets/b456734c-2f0a-4a57-9bf6-e1644c76d4c2" />

# 🏦 Banco
<img src="https://em-content.zobj.net/source/microsoft-teams/400/bank_1f3e6.png" align="right" height="77">

No ecossistema de pagamentos com cartão, o **banco emissor** e o **banco adquirente** são dois pilares fundamentais que atuam em lados opostos da transação, cada um com um papel distinto e essencial para que uma compra seja autorizada e concluída com sucesso. Sua relação é uma dança coreografada que acontece em segundos.

O **banco emissor** é o banco do **consumidor**, o portador do cartão. É a instituição financeira que emitiu o cartão de crédito ou débito que o cliente utiliza na hora da compra. Suas responsabilidades primárias são em relação ao consumidor final: ele aprova o crédito do cliente, envia o cartão para ele, gerencia a sua conta (o limite de crédito no caso de cartão de crédito e o saldo da conta no caso de débito) e, o mais crucial no momento da transação, é o **guardião dos fundos**. 

Quando o cartão é passado na maquininha, é o banco emissor quem autoriza ou nega a transação, verificando se há limite ou saldo disponível e se a compra não é fraudulenta. No final do processo, é ele quem efetivamente paga o valor da compra ao banco adquirente, para depois cobrar do seu cliente (via fatura no crédito ou débito em conta no débito).

O **banco adquirente** (ou "adquirente"), por sua vez, é o banco do **estabelecimento comercial**, ou seja, da loja, restaurante ou empresa que está vendendo o produto ou serviço. Sua função é viabilizar comercialmente que aquele estabelecimento possa aceitar pagamentos com cartão. Para isso, o adquirente fornece a maquininha (POS - Point of Sale) ou a solução de e-commerce, processa as transações e, o mais importante, é o responsável por **liquidadar os valores para a conta do lojista**. Ele atua como um intermediário que recebe o valor da venda de todos os bancos emissores (através das bandeiras como Visa e Mastercard) e, após descontar suas taxas, deposita o valor líquido na conta corrente do comerciante.

Para ilustrar o fluxo de uma transação, imagine uma compra de R$ 100 em uma padaria:

1.  **Cliente** passa o cartão (emitido pelo **Banco A**) na maquininha da **Padaria**.
2.  A maquininha é fornecida e conectada ao **Banco B** (o adquirente).
3.  O **Banco B** envia a solicitação de autorização para a bandeira do cartão, que a repassa para o **Banco A**.
4.  O **Banco A** (emissor) verifica se o cliente tem limite/saldo e se a compra é segura. Se tudo estiver ok, ele **autoriza a transação** e "reserva" os R$ 100 na conta do cliente.
5.  A autorização retorna o caminho inverso e a venda é aprovada na maquininha.
6.  Ao final do dia, a **Padaria** envia o lote de vendas para o **Banco B** (adquirente).
7.  O **Banco B** processa esses lotes e, através das bandeiras, recebe de todos os bancos emissores (incluindo o Banco A) o valor das vendas brutas.
8.  O **Banco B** então desconta suas tarifas (ex: R$ 2) e deposita o valor líquido (R$ 98) na conta corrente da Padaria.

Em resumo, a diferença central é:

*   **Banco Emissor:** É o "banco de quem paga". Sua relação é com o portador do cartão. Ele cuida do crédito/saldo do consumidor e **paga** as compras.
*   **Banco Adquirente:** É o "banco de quem recebe". Sua relação é com o lojista. Ele processa os pagamentos e **repassa** o valor das vendas para o comerciante.

Essa divisão de funções cria um sistema seguro, eficiente e universal, onde o comerciante não precisa ter relacionamento com milhares de bancos diferentes, e o consumidor pode usar seu cartão em milhões de estabelecimentos ao redor do mundo.

## Serasa Experian
<img src="https://github.com/user-attachments/assets/bc068b24-dc5e-47d1-864c-ff2c14a62e7b" align="right" height="77">

A **Serasa Experian** é uma empresa de análise de dados e informações de crédito, amplamente conhecida no Brasil por fornecer soluções para avaliação de risco de crédito, prevenção de fraudes e proteção ao consumidor e empresas. Fundada em 1968, a Serasa é uma das principais referências em informações de crédito no Brasil e, em 2007, foi adquirida pela multinacional **Experian**, uma empresa global de análise de informações e dados de crédito com sede no Reino Unido. Com essa união, a Serasa Experian passou a integrar soluções globais de análise de crédito com foco na realidade brasileira.

A Serasa Experian oferece uma gama de serviços voltados tanto para empresas quanto para consumidores:

1. **Consultas de Crédito e Análise de Risco**: Empresas utilizam os serviços da Serasa para avaliar a capacidade financeira e o risco de inadimplência de consumidores e outras empresas antes de conceder crédito. A consulta pode incluir histórico de dívidas, score de crédito, registros de protestos, cheques sem fundo e pendências financeiras.

2. **Score de Crédito**: A Serasa Experian desenvolve o **Serasa Score**, uma pontuação de crédito usada para indicar o risco de inadimplência de uma pessoa. Esse score é calculado com base no histórico de crédito, comportamento financeiro e outros fatores, sendo utilizado por bancos, financeiras e outras empresas na análise de concessão de crédito.

3. **Proteção Contra Fraudes**: A Serasa oferece soluções de segurança que ajudam empresas e consumidores a se protegerem contra fraudes, especialmente em transações digitais. Isso inclui monitoramento de CPF, alertas de tentativas de uso indevido de informações pessoais e verificação de identidade.
     
4. **Consultas para Consumidores**: Consumidores podem acessar seu próprio histórico de crédito, visualizar pendências e o score Serasa, além de acompanhar e entender seu perfil financeiro. Oferece também monitoramento de CPF e serviço de notificação em caso de movimentações suspeitas.

5. **Serviços de Cobrança e Recuperação de Crédito**: Para empresas, a Serasa Experian possui soluções de cobrança, ajudando a recuperar dívidas de forma eficiente e com estratégias personalizadas. Essas ferramentas auxiliam na comunicação com inadimplentes, facilitando negociações e aumentando a taxa de recuperação de valores devidos.

6. **Marketing e Segmentação de Dados**: Oferece ferramentas para segmentação e análise de clientes potenciais, ajudando empresas a identificar públicos-alvo e tomar decisões estratégicas de marketing. Os dados permitem que as empresas criem campanhas mais assertivas com base no perfil de crédito e comportamento dos consumidores.

7. **Educação Financeira e Ferramentas de Planejamento**: A Serasa também promove programas de educação financeira para consumidores, ajudando-os a entender melhor como funciona o crédito e como melhorar seu perfil financeiro. Inclui orientação para renegociação de dívidas e dicas para aumentar o score de crédito.

A Serasa Experian desempenha um papel crucial no sistema financeiro brasileiro, ao:

- Facilitar o acesso ao crédito para consumidores e empresas, criando uma base mais sólida para decisões financeiras.
- Ajudar empresas a minimizar o risco de inadimplência e fraudes.
- Fornecer aos consumidores maior controle sobre suas finanças pessoais e informações de crédito.

A Serasa Experian é, portanto, uma das principais empresas que conecta instituições financeiras e empresas com dados de crédito, promovendo segurança nas transações e ajudando consumidores a melhorar sua saúde financeira.

Existem diversas APIs que permitem integração com a **Serasa Experian** para acessar informações de crédito, validação de dados, prevenção de fraudes, entre outros. Essas APIs são especialmente úteis para empresas que precisam consultar dados financeiros e de crédito para tomar decisões de risco mais informadas. Algumas das principais APIs oferecidas pela Serasa Experian incluem:

1. **API de Score de Crédito**
   - Permite consultar o **Serasa Score** de consumidores, que é uma pontuação baseada no histórico de pagamento, comportamento de crédito e outros dados financeiros.
   - Ajuda empresas a avaliar o risco de inadimplência e a tomar decisões de crédito de forma rápida e automatizada.

2. **API de Consulta de CPF e CNPJ**
   - Com essa API, é possível consultar o histórico de crédito e verificar pendências financeiras de indivíduos (CPF) e empresas (CNPJ).
   - Inclui informações como dívidas ativas, protestos e histórico de cheques sem fundo, sendo útil para empresas que desejam avaliar o perfil financeiro de clientes e parceiros.

3. **API de Validação de Dados e Identidade**
   - Oferece verificação de dados de identidade para prevenir fraudes e garantir que as informações fornecidas por clientes estejam corretas.
   - Pode validar dados como nome, CPF, CNPJ, endereço e outras informações pessoais.

4. **API de Prevenção de Fraude**
   - Esta API oferece verificações adicionais para detectar comportamentos suspeitos e prevenir fraudes em transações financeiras.
   - Utiliza técnicas avançadas de machine learning e análises de padrões para identificar possíveis fraudes antes que elas ocorram, sendo muito utilizada em e-commerce e serviços de pagamento online.

5. **API de Enriquecimento de Dados**
   - Permite enriquecer dados cadastrais de clientes com informações adicionais, como histórico de endereços e dados de contatos.
   - Ajuda empresas a obter uma visão mais completa do perfil de clientes e parceiros, facilitando a segmentação e personalização de serviços.

6. **API de Monitoramento de Crédito**
   - Com essa API, empresas podem monitorar continuamente o perfil de crédito de clientes e parceiros.
   - Envia alertas em caso de mudanças significativas, como novas dívidas, protestos ou alterações no score, permitindo que a empresa ajuste seu nível de risco em tempo real.

7. **API de Cobrança e Recuperação de Crédito**
   - Facilita o processo de cobrança de dívidas, integrando informações para notificação de clientes inadimplentes.
   - Empresas podem automatizar a comunicação com clientes em débito e melhorar as taxas de recuperação de crédito.

8. **API de Consultas para Locação e Imobiliário**
   - Voltada para o setor imobiliário, essa API permite a consulta de dados de crédito específicos para contratos de locação.
   - Facilita a avaliação de inquilinos e garante segurança nas negociações de aluguel de imóveis.

9. **API de Relacionamento com o Consumidor**
   - Essa API permite que empresas interajam com clientes para melhorar a relação de confiança e auxiliar na educação financeira.
   - Facilita o envio de informações sobre o score de crédito e status financeiro para os próprios clientes, incentivando boas práticas de pagamento e uso do crédito.

Essas APIs são utilizadas por uma ampla gama de setores, incluindo:

- **Instituições financeiras e bancos**: Para concessão de crédito e análise de risco.
- **E-commerce e varejo**: Para avaliação de risco em transações online e offline.
- **Empresas de telecomunicações**: Para validação de dados e análise de risco de inadimplência em serviços pós-pagos.
- **Imobiliárias**: Para análise de crédito e validação de locatários.
- **Empresas de cobrança**: Para automatizar processos de cobrança e recuperação de crédito.

Essas integrações ajudam as empresas a automatizar processos críticos, reduzir riscos financeiros e melhorar a experiência do cliente.

# 💵 BNPL (Buy Now, Pay Later)
O **BNPL - Buy Now, Pay Later** (Compre Agora, Pague Depois), é um modelo de crédito instantâneo que tem ganhado enorme relevância no ecossistema financeiro digital por permitir que o consumidor realize uma compra e pague em parcelas ou em uma data futura, sem necessariamente utilizar um cartão de crédito tradicional. Trata-se de uma evolução dos sistemas de parcelamento, mas com a diferença de estar totalmente integrada à experiência de compra, tanto online quanto física. Ou seja, o cliente escolhe o produto, finaliza a compra e, no momento do checkout, recebe a opção de pagar depois — muitas vezes sem juros ou com taxas menores que as de cartões.

A operação do BNPL ocorre da seguinte forma: uma fintech ou instituição financeira atua como intermediária entre o consumidor e o lojista. Assim que o cliente opta por pagar depois, a empresa de BNPL paga o valor integral ao comerciante, garantindo que ele receba à vista, e o consumidor passa a dever à fintech, que define o cronograma de pagamentos. Isso traz benefícios para ambos os lados — o comerciante não assume risco de inadimplência e ainda aumenta a taxa de conversão de vendas, e o consumidor ganha flexibilidade de pagamento, podendo adquirir bens de maior valor sem precisar de um limite alto de crédito.

O modelo BNPL se consolidou como parte do movimento das chamadas *embedded finances* — ou “finanças embutidas” —, que têm como objetivo integrar serviços financeiros diretamente ao ponto de consumo. Plataformas como Klarna, Afterpay, Affirm e Mercado Crédito são exemplos populares, oferecendo esse tipo de pagamento em marketplaces, e-commerces e até lojas físicas, geralmente dentro de sistemas POS (Point of Sale). Na prática, o BNPL combina aspectos de crédito, tecnologia e experiência do usuário em um formato de microfinanciamento instantâneo, sem as burocracias tradicionais de aprovação de crédito bancário.

Contudo, o crescimento do BNPL também traz desafios e discussões regulatórias, especialmente quanto à responsabilidade do consumidor e ao controle de endividamento. Muitos usuários acabam contratando várias compras parceladas em diferentes plataformas, sem uma visão consolidada da dívida total. Por isso, órgãos reguladores e bancos centrais de vários países estão avaliando políticas para equilibrar a inovação com a proteção financeira do consumidor.

Em síntese, o BNPL é mais do que uma forma de pagamento — é uma transformação na forma como o crédito é oferecido e consumido. Ele reflete a tendência global de tornar o ato de pagar algo quase invisível, integrado ao fluxo da experiência de compra, e adaptado ao comportamento digital e instantâneo do consumidor moderno.

O crescimento do BNPL tem sido dramático nos últimos anos. O provedor BNPL representa a interface principal entre os comerciantes e os clientes para comércio eletrônico e POS (ponto de venda).

O diagrama abaixo mostra como o processo funciona:

<img width="901" height="984" alt="image" src="https://github.com/user-attachments/assets/7073d0e9-2c95-4643-b783-91aa0a9157a5" />

**POS (Point of Sale)** significa **Ponto de Venda** — é o sistema (hardware + software) utilizado para **processar transações comerciais** em tempo real, geralmente no varejo físico ou digital. Ele engloba os terminais de pagamento, maquininhas de cartão, caixas registradoras digitais e softwares que registram as vendas, emitem recibos e integram estoque e faturamento. Quando você paga com cartão numa loja, o POS é o sistema que processa o pagamento, conecta-se à adquirente, valida o cartão, aprova a transação e emite o comprovante. Ou seja, o POS é a **infraestrutura de pagamento no ponto de compra**.

Já o **BNPL (Buy Now, Pay Later)** significa **Compre Agora, Pague Depois** — é uma **modalidade de crédito instantâneo**, normalmente integrada ao checkout online (mas cada vez mais disponível também em POS físicos), que permite ao consumidor dividir ou postergar o pagamento de uma compra sem necessariamente usar um cartão de crédito. Na prática, o BNPL é um **modelo de financiamento embutido no momento da compra**: a fintech ou o banco paga o lojista à vista, e o consumidor paga depois em parcelas, com ou sem juros, dependendo do acordo. Exemplos disso são empresas como **Klarna, Afterpay, Affirm e Mercado Crédito**, além de soluções oferecidas por bancos digitais.

Em resumo:

* **POS** → é a **plataforma de transação** (onde o pagamento acontece).
* **BNPL** → é a **solução de crédito embutida** no pagamento (como o pagamento será feito).

Os dois se conectam: o BNPL muitas vezes é integrado **ao POS**, tanto físico quanto digital, permitindo que o consumidor escolha “pagar depois” diretamente na maquininha ou no checkout online.

Então, se quisermos sintetizar de forma prática:

* O **POS** é o **meio** de pagamento.
* O **BNPL** é o **modelo financeiro** de pagamento.

# 💳 Rede de cartões
<a href="https://youtu.be/EyMRZpgJUuc"><img src="https://img.shields.io/badge/VISA-23-00599C?style=flat&logo=VISA&logoColor=white"></a> <a href="https://youtu.be/EyMRZpgJUuc"><img src="https://img.shields.io/badge/VISA-23-00599C?style=flat&logo=VISA&logoColor=white"></a> <a href="https://youtu.be/EyMRZpgJUuc"><img src="https://img.shields.io/badge/VISA-23-00599C?style=flat&logo=VISA&logoColor=white"></a> <a href="https://youtu.be/EyMRZpgJUuc"><img src="https://img.shields.io/badge/VISA-23-00599C?style=flat&logo=VISA&logoColor=white"></a> <a href="https://youtu.be/EyMRZpgJUuc"><img src="https://img.shields.io/badge/VISA-23-00599C?style=flat&logo=VISA&logoColor=white"></a>

Uma **rede de cartões** é uma infraestrutura que conecta emissores de cartões (bancos e instituições financeiras) aos adquirentes (empresas que processam pagamentos para comerciantes). Ela facilita transações com cartões de crédito e débito, garantindo que os pagamentos sejam autorizados e processados corretamente.

As principais redes de cartões incluem Visa, Mastercard, American Express e Discover, que operam globalmente. No Brasil, redes como Elo e Hipercard também são bastante utilizadas. Essas redes desempenham um papel essencial no sistema de pagamentos, definindo regras para transações, taxas e segurança. 

As “bandeiras de cartão” e as “redes de pagamento” muitas vezes se referem às mesmas entidades, mas tecnicamente têm funções específicas dentro do ecossistema de pagamentos. Quando falamos de bandeiras como Visa, Mastercard, American Express, Discover, Elo, Hipercard e Diners Club, estamos nos referindo aos sistemas que licenciam o uso da sua marca em cartões emitidos por bancos e que também gerenciam as regras e a comunicação das transações entre o emissor (banco do cliente) e o adquirente (banco do lojista).

No modelo mais comum — o chamado quatro partes — temos: cliente, lojista, emissor (banco do cliente), adquirente (banco do lojista), e a bandeira como intermediária entre emissor e adquirente. American Express e Diners, em alguns contextos, atuam também como emissores diretos, ou seja, integram todas essas partes (modelo de três partes). As bandeiras definem padrões técnicos, regras de segurança (como o uso do EMV, tokenização, 3DS), taxas (interchange, MDR), além de arbitrar disputas e cuidar da liquidação das transações.

No Brasil, além de Visa e Mastercard, Elo (criada por BB, Bradesco e Caixa), Hipercard (do Itaú) e Cabal também compõem o ecossistema, cada uma com aceitação e políticas próprias. A distinção correta ajuda principalmente quem trabalha com meios de pagamento, fintechs ou integração com TEF e POS, já que entender a cadeia (bandeira, adquirente, subadquirente, gateway) é essencial para oferecer soluções robustas e compatíveis com o mercado nacional e internacional.

Como funcionam as disputas e o estorno da Visa? Uma disputa acontece quando o titular do cartão discorda da cobrança de um comerciante. Um estorno é um processo de reversão da cobrança. Às vezes, os dois termos são usados de forma intercambiável.

Uma disputa é cara: para cada dólar em transações contestadas, um adicional de US$ 1,50 é gasto em taxas e despesas.

<img width="982" height="1111" alt="image" src="https://github.com/user-attachments/assets/60dea8fc-418c-47c9-8e9d-c7dfc89a47b9" />

Passos 1-3: O titular do cartão, Bob, levanta uma disputa com o emissor do cartão. O banco emissor analisa os detalhes. Em casos de disputas legítimas, o banco emissor envia uma solicitação de estorno à rede de cartões.

Etapas 4-6: A rede de cartões envia a disputa ao banco adquirente. Depois de revisar os detalhes, o banco adquirente pode pedir ao comerciante para resolver o problema.

Passos 7,8: O comerciante tem duas opções:

1. Os comerciantes podem aceitar estornos se parecerem legítimos.
algarismo. O comerciante pode representar ao emissor o documento que suporta a transação.

Etapas 9-11: O banco adquirente analisa as evidências e representa a transação para a rede de cartões, que a encaminha ao emissor.

Etapas 12-14: O emissor analisa a representação. Existem duas opções:
1. O emissor cobra a transação de volta ao titular do cartão;
2. O emissor submete a disputa à rede de cartões para arbitragem.

Passo 15: A rede de cartões decide com base nas evidências e atribui a responsabilidade final ao titular do cartão ou ao comerciante.

👉 Para você: a disputa é cara. Como podemos reduzi-lo e tornar o processo mais simplificado?

## [Card] Como a AMEX processa milhões de transações diárias com latência de milissegundos
Veremos as decisões de arquitetura e design que os engenheiros da AMEX tomaram ao construir o novo sistema de pagamento.

O desenvolvimento do Global Transaction Router na American Express mostra como um sistema de pagamento bem arquitetado pode alcançar alta escalabilidade, baixa latência e tolerância a falhas.

Ao tomar decisões técnicas estratégicas, como usar Go para simultaneidade, gRPC para comunicação eficiente e registro assíncrono para reduzir gargalos, a equipe de engenharia garantiu que o GTR pudesse lidar com milhões de transações por segundo sem degradação do desempenho.

Estratégias de otimização, como criação de perfil e benchmarking, mutexes de leitor-gravador e gravações diretas de soquete, melhoraram ainda mais a velocidade de processamento de transações. Isso permitiu que a American Express construísse um sistema de transações financeiras de ponta capaz de atender às demandas cada vez maiores da economia digital.

> [!Warning]
> Isenção de responsabilidade: Os detalhes desta postagem foram derivados da apresentação da equipe de engenharia da American Express no Monster Scale Summit da ScyllaDB. Todo o crédito pelos detalhes técnicos vai para a equipe de engenharia da American Express. Agradecimentos especiais a ScyllaDB por organizar o evento. Os links para os artigos originais estão presentes na seção de referências no final do post. Tentamos analisar os detalhes e fornecer nossa opinião sobre eles. Se você encontrar alguma imprecisão ou omissão, deixe um comentário e faremos o possível para corrigi-la.

A American Express processa trilhões de dólares em transações todos os anos, o que significa milhões de transações diárias em todo o mundo.

Considerando o quão impacientes os usuários podem se tornar se uma transação demorar muito, essas transações devem dar suporte a respostas de baixa latência em milissegundos. Para atender a esse requisito, a American Express reconstruiu seu sistema de pagamento em 2018 para criar uma infraestrutura mais moderna, flexível e confiável.

Mas qual era o problema com o sistema antigo?

O sistema mais antigo era uma rede de pagamento local tradicional que dependia de infraestrutura legada. Tinha várias limitações que o tornavam menos adequado para o futuro. Além disso, era difícil mudar e escalar com base na demanda. A AMEX queria garantir que sua rede de pagamento pudesse lidar com volumes crescentes de transações, mantendo alta velocidade e segurança.

As principais razões para a mudança são as seguintes:

- Prontidão para a nuvem: o sistema anterior não foi totalmente projetado para ambientes de nuvem. A American Express pretendia construir um sistema que pudesse aproveitar a computação em nuvem para melhor escalabilidade e resiliência.

- Flexibilidade e adaptabilidade: O setor financeiro evolui rapidamente, com novas tecnologias e regulamentações de pagamento surgindo com frequência. Um sistema redesenhado permitiria atualizações e integrações mais rápidas com parceiros.

- Segurança e confiabilidade: Como um grande provedor de pagamentos, a American Express precisava de uma rede que pudesse processar milhões de transações com segurança e sem falhas. O sistema atualizado foi projetado para ser mais resiliente contra interrupções.

- Processamento de baixa latência: os pagamentos devem ser processados em milissegundos. O sistema mais antigo enfrentava desafios para atender às expectativas de desempenho modernas. Ao atualizar a rede, a American Express garantiu aprovações de transações mais rápidas e experiências mais suaves para os clientes.

- Lidando com mais transações – O volume de pagamentos digitais continua a crescer. O novo sistema foi construído para lidar com um número significativamente maior de transações por segundo, reduzindo o risco de lentidão ou falhas.

**Roteador de transações globais**: A peça central do novo sistema de pagamento é o Global Transaction Router (GTR). O sistema de pagamento AMEX tem que interagir com diferentes entidades envolvidas no tratamento de transações de pagamento.

- Adquirentes (Merchant Banks): São instituições financeiras ou bancos que processam transações com cartão em nome de um comerciante. Quando um cliente usa seu cartão AMEX em uma loja ou online, o adquirente é responsável por enviar a solicitação de transação para uma rede de pagamento como a AMEX.

- Processadores (Provedores de Serviços de Pagamento): São empresas que gerenciam a infraestrutura técnica necessária para lidar com transações de pagamento entre comerciantes, bancos adquirentes e redes de cartões. Os processadores garantem que os dados de pagamento sejam transmitidos com segurança e formatados corretamente para autorização.

- Emissores (bancos emissores de cartões): Um emissor é um banco ou instituição financeira que fornece o cartão de pagamento ao cliente. O emissor é responsável por verificar se o titular do cartão possui fundos de crédito suficientes disponíveis para aprovar a transação.

<img width="1456" height="866" alt="image" src="https://github.com/user-attachments/assets/694d7e38-e30e-4a89-92d7-727f79771979" />

O GTR fica na borda da rede AMEX, o que significa que é o primeiro sistema que interage com essas diferentes entidades. Veja como as várias entidades trabalham com o GTR:

- Um comerciante (por meio de um adquirente) envia uma solicitação de pagamento quando um cliente usa seu cartão AMEX.

- A solicitação chega ao GTR, que determina a melhor rota para processar a transação.

- O GTR encaminha a solicitação ao processador, que verifica os detalhes da transação e a encaminha ao emissor.

- O emissor verifica se o titular do cartão tem fundos ou crédito suficientes e envia uma resposta de aprovação/recusa por meio do GTR.

- O comerciante recebe a aprovação final e o pagamento é concluído.

- A liquidação ocorre mais tarde, quando o emissor transfere fundos para o adquirente, que então paga ao comerciante.

**Desafios únicos para o GTR**: Houve vários desafios únicos quando se tratou de construir o GTR:

- Gerenciando sessões TCP de longa duração: Ao contrário dos serviços da Web modernos que usam APIs baseadas em HTTP de curta duração, os sistemas de pagamento geralmente contam com o ISO 8583, um padrão de mensagens financeiras mais antigo que opera em conexões TCP de longa duração. Isso significa que as conexões persistentes devem ser mantidas por longos períodos.

- Manipulação de mensagens ISO 8583: ISO 8583 é um protocolo de mensagens amplamente utilizado, mas desatualizado em transações financeiras.

- Picos de tráfego e picos de carga: As transações financeiras não ocorrem a uma taxa constante. O sistema teve que lidar com picos repentinos, como durante a Black Friday e as temporadas de compras de fim de ano.

- Garantindo latência ultrabaixa: Os sistemas de pagamento operam em uma escala de milissegundos. Mesmo um pequeno atraso no processamento de uma transação pode resultar em pagamentos recusados devido a tempos limite e má experiência do cliente se uma transação demorar muito para ser concluída.

**Decisões de projeto do GTR**: A equipe de engenharia da AMEX tomou várias decisões técnicas ao projetar o GTR para garantir que ele superasse os desafios. Algumas das principais decisões são as seguintes:

**1 - Escolha do idioma: Go (Golang)** - A equipe selecionou Go (Golang) como a linguagem principal para o desenvolvimento do GTR.

Mas por que ir?

Um dos principais motivos foi o modelo de simultaneidade do Go. As transações de pagamento envolvem milhares de conexões simultâneas, cada uma exigindo processamento em tempo real. Go fornece goroutines que podem lidar com milhares de conexões abertas de forma eficiente sem sobrecarga excessiva de CPU ou memória.

Em segundo lugar, o Go compila antecipadamente (AOT), o que significa que os aplicativos são iniciados instantaneamente sem a necessidade de otimizações de tempo de execução. Isso é importante para o processamento de transações de baixa latência.

O coletor de lixo do Go também é otimizado para aplicativos de baixa latência, garantindo que a limpeza de memória não interrompa o processamento transacional. Em comparação com linguagens como Java, onde a coleta de lixo pode causar pausas aleatórias (picos de GC), o GC do Go foi projetado para minimizar atrasos nas transações.

**2 - gRPC sobre HTTP/2** - A AMEX optou pelo gRPC em vez do HTTP/2 para melhorar a comunicação interna dentro do sistema de pagamento.

O gRPC usa buffers de protocolo (protobuf) em vez de JSON, o que resulta em tamanhos de mensagem menores e serialização e desserialização mais rápidas. Ao rotear pagamentos, o GTR converte mensagens ISO 8583 em mensagens gRPC usando protobuf, tornando o processamento interno mais rápido.

<img width="1456" height="935" alt="image" src="https://github.com/user-attachments/assets/9d065837-62e8-42e9-ada6-d4d343f2e424" />

Além disso, o gRPC sobre HTTP/2 permite multiplexação, o que significa que várias solicitações podem ser enviadas e processadas simultaneamente em uma única conexão TCP. Isso é fundamental para lidar com milhares de transações em tempo real sem atrasos.

Veja o diagrama abaixo que mostra a multiplexação HTTP/2 em ação.

<img width="1456" height="834" alt="image" src="https://github.com/user-attachments/assets/3066f3a9-d751-4cc1-b0c8-6ee9c2180c43" />

**3 - Log assíncrono para otimização de desempenho** - Em sistemas tradicionais, o registro é síncrono, o que significa que toda vez que o aplicativo grava uma mensagem de log, ele pausa a execução até que o log seja gravado em um arquivo ou banco de dados. Isso retarda o processamento de transações, especialmente ao lidar com milhões de entradas de log por segundo.

O GTR usa log assíncrono. As mensagens de log são gravadas em um buffer na memória em vez de serem processadas imediatamente. Uma goroutine separada lê do buffer e grava logs em lotes, reduzindo a contenção. Dessa forma, as transações continuam sendo processadas enquanto os logs são gravados em segundo plano.

Veja o diagrama abaixo:

<img width="1456" height="875" alt="image" src="https://github.com/user-attachments/assets/d9ce1c22-37b4-49d7-884b-c3e912dab26f" />

Além disso, a AMEX criou manipuladores de registro personalizados usando a biblioteca "slog" padrão do Go. A biblioteca ajuda a armazenar logs em buffer na memória e estruturá-los para facilitar a depuração. A AMEX também otimizou o registro usando a amostragem de registros, onde apenas um subconjunto de registros é registrado em condições de carga pesada.

**Estratégias de otimização**: Para garantir que o Global Transaction Router pudesse lidar com milhões de transações por segundo, mantendo baixa latência e alta confiabilidade, a equipe de engenharia da American Express (AMEX) implementou várias estratégias importantes de otimização.

Vejamos as três principais áreas de otimização.

**1 - Criação de perfil e benchmarking**: A criação de perfil é o processo de analisar um programa enquanto ele está em execução para identificar gargalos de desempenho (por exemplo, funções lentas, alto uso da CPU ou ineficiências de memória).

Por outro lado, o benchmarking envolve a execução de um conjunto de testes para medir o desempenho do sistema em diferentes condições, como altos volumes de transações.

A equipe usou as ferramentas de criação de perfil integradas do Go (pprof) para analisar o desempenho do sistema. Eles mediram o uso da CPU, o consumo de memória e o tempo de execução de diferentes funções na base de código. Isso os ajudou a identificar quais funções estavam demorando muito para serem executadas e precisavam de otimização.

Em seguida, a equipe executou testes de benchmark simulando cenários de alto tráfego para testar o desempenho do GTR sob estresse. Eles executaram testes para medir parâmetros como latência, taxa de transferência e recuperação de falhas.

**2 - Usando mutexes de leitor-gravador**: Um mutex (bloqueio de exclusão mútua) é um mecanismo usado para controlar o acesso a recursos compartilhados em um sistema simultâneo. Se várias rotinas Go (threads) tentarem ler e gravar os mesmos dados ao mesmo tempo, poderá ocorrer corrupção de dados. Um mutex impede isso permitindo que apenas um thread acesse o recurso por vez.

Em vez de usar um mutex padrão, a equipe da AMEX implementou um Mutex Reader-Writer. Isso ocorre porque um mutex padrão bloqueia um recurso completamente, o que significa que nenhum outro thread pode acessá-lo até que o bloqueio seja liberado. Essa abordagem é ineficiente quando vários threads só precisam ler os dados e cria atrasos desnecessários bloqueando todo o acesso.

O Reader-Writer Mutex funciona de forma diferente. Ele permite que várias operações de leitura aconteçam ao mesmo tempo. As operações de gravação ainda exigem acesso exclusivo.

Consulte o diagrama abaixo que mostra a diferença entre um mutex padrão e um mutex de leitor-gravador.

<img width="1456" height="1491" alt="image" src="https://github.com/user-attachments/assets/fef87553-7b7e-4754-9575-65f5167339c6" />

**3 - Evitando o uso excessivo de canais Go**: Os canais Go são uma maneira de as rotinas Go se comunicarem entre si, passando dados entre elas. Eles são frequentemente usados para coordenar tarefas assíncronas.

Inicialmente, a AMEX usava canais Go para processar dados de transações, mas descobriu que essa abordagem era mais lenta do que a comunicação direta de soquete.

Os canais introduzem sobrecarga extra porque:

- As mensagens precisam ser enfileiradas e sincronizadas antes de serem processadas.

- Cada transação tinha que esperar que um canal fosse lido antes de avançar.

- Isso adicionou complexidade desnecessária em transações financeiras de alta velocidade.

Em vez de ler de um soquete TCP, enviar dados por meio de um canal Go e, em seguida, gravar em um soquete gRPC, a equipe removeu a etapa intermediária do canal. As transações foram processadas diretamente do TCP para o gRPC, eliminando atrasos desnecessários.

Veja o diagrama abaixo:

<img width="1456" height="1490" alt="image" src="https://github.com/user-attachments/assets/e3e435cf-9903-4600-9e65-3647589ae52a" />

Melhores práticas operacionais
Por fim, a equipe de engenharia da AMEX também segue algumas práticas recomendadas operacionais para garantir que o Global Transaction Router funcione com eficiência em todas as condições.

1 - Teste de desempenho contínuo para cada versão: Mesmo uma pequena alteração na base de código pode afetar o desempenho, a escalabilidade ou a velocidade da transação. Uma pequena atualização que parece inofensiva pode introduzir gargalos inesperados em cenários de alto tráfego.

Portanto, cada versão passa por testes de desempenho, independentemente de quão pequena seja a atualização. Os testes de benchmark são executados para medir a velocidade de processamento da transação, o tempo de resposta e o uso de recursos do sistema. As ferramentas de teste de carga simulam volumes de transações do mundo real para garantir que o sistema possa lidar com cargas de pico.

2 - Teste de Caos: As redes de pagamento devem permanecer operacionais 24 horas por dia, 7 dias por semana, mesmo durante picos repentinos de tráfego, interrupções de rede ou falhas no sistema.

Para garantir isso, a AMEX realiza testes regulares de caos.

O teste de caos envolve a introdução intencional de falhas ou condições extremas para observar como o sistema responde. Isso ajuda a garantir que o GTR possa se recuperar rapidamente de problemas inesperados sem interrupção do serviço.

3 - Processo de Desenvolvimento Iterativo: Em vez de fazer atualizações grandes e pouco frequentes, a equipe da AMEX melhora continuamente o GTR por meio de pequenos aprimoramentos incrementais.

Os desenvolvedores analisam os dados de criação de perfil para encontrar áreas em que o desempenho pode ser melhorado. Em vez de implantar alterações grandes e arriscadas, pequenas atualizações são feitas com frequência.

Essa abordagem garante que o desempenho, a segurança e a escalabilidade estejam sempre melhorando.

## [Card] PCI DSS (Payment Card Industry Data Security Standard)
O **PCI DSS** (Payment Card Industry Data Security Standard), ou Padrão de Segurança de Dados para a Indústria de Cartões de Pagamento, é um conjunto de normas criado para garantir a proteção dos dados de cartão de crédito e débito em qualquer ambiente que processa, armazena ou transmite essas informações. Essas normas foram desenvolvidas pelo **PCI Security Standards Council**, formado por grandes empresas de cartões de pagamento, como **Visa, Mastercard, American Express, Discover e JCB**.

O objetivo do PCI DSS é reduzir fraudes e vazamentos de dados, estabelecendo medidas de segurança robustas para proteger as informações dos titulares de cartões durante o processamento e armazenamento. É uma forma de padronizar a segurança na indústria de pagamentos, ajudando empresas e consumidores a evitar incidentes como roubo de informações financeiras.

O PCI DSS é composto por **12 requisitos** principais, divididos em seis categorias. Cada requisito visa abordar um aspecto importante da segurança de dados:

1. **Construir e manter uma rede segura e protegida**:
   - Instalar e manter uma configuração de firewall para proteger os dados do titular do cartão.
   - Não usar senhas padrão fornecidas pelos fornecedores e outras configurações de segurança padrão.

2. **Proteger os dados do titular do cartão**:
   - Proteger os dados armazenados.
   - Criptografar a transmissão de dados de titulares de cartão por meio de redes públicas abertas.

3. **Manter um programa de gestão de vulnerabilidades**:
   - Usar e atualizar regularmente software antivírus.
   - Desenvolver e manter sistemas e aplicativos seguros.

4. **Implementar medidas rigorosas de controle de acesso**:
   - Restringir o acesso aos dados dos titulares de cartão por negócios que precisem saber.
   - Identificar e autenticar o acesso aos componentes do sistema.
   - Restringir o acesso físico aos dados do titular do cartão.

5. **Monitorar e testar redes regularmente**:
   - Acompanhar e monitorar todo o acesso a recursos de rede e dados do titular do cartão.
   - Testar regularmente os sistemas e processos de segurança.

6. **Manter uma política de segurança da informação**: Manter uma política que enderece a segurança das informações para todos os colaboradores e fornecedores.

As empresas que processam transações com cartão de pagamento são classificadas em **quatro níveis de conformidade**, com base no volume de transações anuais que processam:

- **Nível 1**: Mais de 6 milhões de transações por ano.
- **Nível 2**: De 1 a 6 milhões de transações por ano.
- **Nível 3**: De 20.000 a 1 milhão de transações por ano.
- **Nível 4**: Menos de 20.000 transações por ano.

Empresas de maior volume (Nível 1) estão sujeitas a auditorias anuais rigorosas por uma entidade certificada, enquanto empresas menores podem realizar autoavaliações, embora ainda devam cumprir as normas.

O cumprimento do PCI DSS é essencial para qualquer empresa que lide com pagamentos com cartão, pois:

- **Reduz o risco de fraudes** e vazamentos de dados.
- **Aumenta a confiança dos clientes** ao demonstrar o compromisso com a segurança dos dados financeiros.
- **Evita penalidades financeiras** e perda de reputação associadas a vazamentos de dados.

Para estar em conformidade com o PCI DSS, uma empresa pode:
- Implementar **tokenização e criptografia** de dados de pagamento.
- **Segregar a rede de pagamento** da rede corporativa.
- Aplicar **controles de acesso rigorosos** e limitar o número de funcionários que têm acesso a dados sensíveis.
  
Em resumo, o PCI DSS é um padrão essencial para a segurança no setor de pagamentos, ajudando a proteger dados financeiros e minimizar riscos de fraude para comerciantes e consumidores.

A integração de um PinPad (terminal de pagamento) com NetSuite pode ser feita através de uma combinação de integração de hardware e configuração de software. Isso geralmente envolve trabalhar com um gateway de pagamento que suporte transações PinPad e integrá-lo ao ambiente NetSuite. Aqui estão as etapas gerais para fazer isso:

1. Escolha um gateway de pagamento que suporte integração com PinPad. Exemplos: PayPal, Stripe, Square, Adyen ou Cybersource. Certifique-se de que o gateway suporta processamento de cartão com chip EMV (Europay, MasterCard, Visa) se desejar cumprir os padrões de segurança. Certifique-se de que o gateway forneça um conector NetSuite ou suporte APIs que possam ser usadas para integração.

2. Instale ou configure a integração do gateway de pagamento no NetSuiteAcesse SuiteApp Marketplace no NetSuite e pesquise o SuiteApp de integração do gateway de pagamento. Se disponível, instale o SuiteApp seguindo a documentação.Você também pode usar scripts personalizados se nenhum SuiteApp direto estiver disponível. Navegue até `Configuração > Contabilidade > Perfis de processamento de pagamento e configure seu provedor de pagamento para lidar com transações`.

3. Conecte o PinPad ao seu sistemaConexão PinPad: A maioria dos PinPads modernos são conectados via USB, Ethernet ou Bluetooth. O PinPad deve ser capaz de se comunicar com o seu gateway de pagamento através do seu POS ou diretamente através de umAPI baseada em nuvem.Alguns gateways oferecem middleware que se comunica entre o NetSuite, o PinPad e o processador de pagamento.

4. Use SuiteScript para integrar PinPad com NetSuite (se necessário)Se não existir integração direta, você poderá criar uma integração personalizada usando SuiteScript para se comunicar com a API PinPad. As etapas incluem:

Exemplo: Resposta básica de como é feita o processo em JavaScript (SuiteScript).

```javascript
define(['N/https', 'N/record'], function(https, record) {
    function processPayment(context) {
        var paymentDetails = {
            amount: 0.00, // 100.00
            cardDetails: {
                cardNumber: 'XXXX-XXXX-XXXX-1234',
                expiryDate: '12/24',
                cvv: '123'
            }
        };

        var response = https.post({
            url: 'https://api.paymentgateway.com/process',
            body: JSON.stringify(paymentDetails),
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer []'
            }
        });

        if (response.code == 200) {
            var paymentRecord = record.create({type: 'customerpayment'});
            paymentRecord.setValue({fieldId: 'amount', value: 0.00}); // 100.00
            paymentRecord.save();
        }
    }

    return {
        execute: processPayment
    };
});
```

5. Testando a integração do PinPad Transações de teste: execute algumas transações de teste em seu ambiente sandbox ou Postman. Certifique-se de que o PinPad se comunique corretamente com o gateway de pagamento e que os resultados da transação sejam refletidos no NetSuite. Verifique se os pagamentos dos clientes são registrados corretamente e se os relatórios financeiros estão atualizados.

6. Conformidade e Segurança: Certifique-se de que a integração siga as diretrizes PCI-DSS (Payment Card Industry Data Security Standard) para lidar com as informações do titular do cartão com segurança.Os PinPads geralmente lidam com dados confidenciais do cartão e os tokenizam antes de enviá-los ao gateway, reduzindo os riscos de segurança.

7. Habilite o PinPad na configuração do seu ponto de venda (POS) (se aplicável). Se você estiver usando o NetSuite como sistema de ponto de venda (POS), certifique-se de que o PinPad esteja integrado ao seu POS SuiteApp. Habilite opções para transações com cartão de crédito ou cartão com chip e defina as configurações para funcionar com o gateway de pagamento e o PinPad.

8. Suporte ao cliente e solução de problemas, mantenha contato com seu provedor de gateway de pagamento para solução de problemas ou atualizações sobre alterações de API. Verifique regularmente se há atualizações no firmware do PinPad ou no SuiteApp de integração de pagamento do NetSuite. Conclusão para integrar com sucesso um PinPad ao NetSuite, você precisará garantir a compatibilidade entre o seu PinPad, gateway de pagamento e o sistema de pagamento do NetSuite. Se não existir um SuiteApp pré-construído, pode ser necessário usar as APIs do SuiteScript e do NetSuite para criar soluções personalizadas. Certifique-se sempre de que sua integração seja segura e compatível com os padrões de processamento de pagamentos.

# 🤝 Gateway de pagamento
<img src="https://img.shields.io/badge/Stripe-NPM-512BD4?style=flat&logo=Stripe&logoColor=white"> <a href="https://medium.com/@prashant558908/paypal-low-level-design-interview-questions-from-recent-interviews-0e1170f2690c"><img src="https://img.shields.io/badge/PayPal-NPM-blue?style=flat&logo=PayPal&logoColor=white"></a>

<img src="https://em-content.zobj.net/source/microsoft-teams/400/handshake_1f91d.png" align="right" height="77">

Um **gateway de pagamento** é uma tecnologia que permite que empresas aceitem pagamentos online por meio de cartões de crédito, débito e outras formas de pagamento eletrônico. Ele atua como uma ponte entre o site ou aplicativo de uma empresa e as instituições financeiras que processam os pagamentos. Em resumo, um gateway de pagamento é uma tecnologia que permite que empresas aceitem pagamentos online, enquanto plataformas de pagamento eletrônico são empresas que oferecem soluções de pagamento completo. 

Algumas empresas podem oferecer ambas as soluções, atuando como gateway de pagamento e plataforma de pagamento eletrônico, proporcionando uma solução completa para negócios online. Veja abaixo:

<img height="578" align="right" src="https://github.com/user-attachments/assets/021c250e-58d2-49c8-bba3-44a22228f6cf" />

- <a href="">PagSeguro</a>

- <a href="">Mercado Pago</a>

- <a href="">Pagar.me</a>

- <a href="">Stripe</a>

- <a href="">PayPal</a>

- <a href="">Adyen</a>

- <a href="">Cielo</a>

- <a href="">Getnet</a>

- <a href="">Zoop</a>

- <a href="">Braspag</a>

- <a href="">Coda Payments</a>

- <a href="">Amazon payments</a>

- <a href="">PayU</a>

- <a href="">Rede</a>

Um gateway de pagamento oferece várias funcionalidades, incluindo:

1. Processamento de transações: O gateway de pagamento processa as transações de pagamento em tempo real.
2. Segurança: O gateway de pagamento oferece recursos de segurança avançados para proteger as informações de pagamento dos clientes.
3. Integração com plataformas: O gateway de pagamento pode ser integrado a plataformas de e-commerce e outros sistemas.

![gateway-de-pagamento-task-internet-1-768x384](https://github.com/user-attachments/assets/a5b28b6a-5392-4a59-8c62-c69280d0b4ff)

A principal diferença entre um gateway de pagamento e plataformas de pagamento eletrônico como Stripe, Stone, PayPal e Mercado Pago é que:

1. Gateway de pagamento: É uma tecnologia que permite que empresas aceitem pagamentos online, mas não necessariamente processa os pagamentos em si. Alguns exemplos de gateways de pagamento incluem: PayU, Cielo, Rede e etc.
2. Plataformas de pagamento eletrônico: São empresas que oferecem soluções de pagamento completo, incluindo processamento de transações, gestão de contas e segurança. Já as plataformas de pagamento eletrônico incluem: Stripe, Stone, PayPal, Mercado Pago, etc.

Pense no gateway como a "maquininha virtual" que processa pagamentos, enquanto a plataforma é um sistema mais completo que ajuda a gerenciar as transações. Algumas empresas oferecem apenas gateways, enquanto outras têm plataformas que incluem gateways como parte de um serviço mais amplo.

<img width="1024" height="387" alt="infografico-como-gateway-de-pagamento-funciona" src="https://github.com/user-attachments/assets/3ba64e6e-eb07-4305-98fa-87aaacc24258" />

> [!Important]
> A escolha entre um gateway de pagamento e uma plataforma de pagamento online (ou intermediadora de pagamentos) depende do modelo de negócio, volume de transações e do nível de controle que você deseja. Mas se a pergunta é qual oferece mais lucro, a resposta é: Para negócios com grande volume e estrutura própria, o gateway de pagamento costuma gerar mais lucro líquido a longo prazo, porque as taxas são menores e você tem mais controle sobre a operação.

## [Gateway] Plataformas de pagamento eletrônico
<img src="https://img.shields.io/badge/Stripe-NPM-512BD4?style=flat&logo=Stripe&logoColor=white"> <a href="https://medium.com/@prashant558908/paypal-low-level-design-interview-questions-from-recent-interviews-0e1170f2690c"><img src="https://img.shields.io/badge/PayPal-NPM-blue?style=flat&logo=PayPal&logoColor=white"></a>

<img src="https://github.com/user-attachments/assets/8a5ea030-ea28-4629-8f68-5aaf0a939c50" align="right" height="77">

O **Mercado Pago** é uma plataforma de pagamento online criada pelo Mercado Livre, uma das maiores plataformas de e-commerce da América Latina. O Mercado Pago permite que usuários façam pagamentos online, transfiram dinheiro e gerenciem suas finanças de forma segura e conveniente. O Mercado Pago é uma plataforma de pagamento online líder na América Latina que oferece soluções seguras e convenientes para usuários que desejam fazer pagamentos online e gerenciar suas finanças.

O Mercado Pago oferece várias funcionalidades, incluindo:

1. Pagamentos online: O Mercado Pago permite que usuários façam pagamentos online em sites e lojas que aceitam a plataforma.
2. Transferências de dinheiro: O Mercado Pago permite que usuários transfiram dinheiro entre si.
3. Cartão pré-pago: O Mercado Pago oferece um cartão pré-pago que pode ser usado para fazer compras online e offline.

O Mercado Pago é popular devido às suas vantagens, incluindo:

1. Segurança: O Mercado Pago oferece recursos de segurança avançados para proteger transações online.
2. Conveniência: O Mercado Pago permite que usuários façam pagamentos online e transfiram dinheiro de forma rápida e fácil.
3. Integração com o Mercado Livre: O Mercado Pago é integrado ao Mercado Livre, o que facilita a compra e venda de produtos na plataforma.

<img src="https://upload.wikimedia.org/wikipedia/commons/b/b5/PayPal.svg" align="right" height="77">

O **PayPal** é uma plataforma de pagamento online que permite que indivíduos e empresas enviem e recebam pagamentos pela internet. O PayPal é uma plataforma de pagamento online líder que oferece soluções seguras e convenientes para indivíduos e empresas que desejam fazer pagamentos online.

O PayPal oferece várias funcionalidades, incluindo:

1. Pagamentos online: O PayPal permite que usuários façam pagamentos online sem precisar compartilhar informações de cartão de crédito.
2. Transferências de dinheiro: O PayPal permite que usuários transfiram dinheiro entre si.
3. Proteção ao comprador: O PayPal oferece proteção ao comprador em caso de problemas com compras online.

O PayPal é popular devido às suas vantagens, incluindo:

1. Segurança: O PayPal oferece recursos de segurança avançados para proteger transações online.
2. Conveniência: O PayPal permite que usuários façam pagamentos online sem precisar compartilhar informações de cartão de crédito.
3. Ampla aceitação: O PayPal é aceito em milhões de sites e lojas online em todo o mundo.

<img src="https://upload.wikimedia.org/wikipedia/commons/b/ba/Stripe_Logo%2C_revised_2016.svg" align="right" height="77">

O **Stripe** é uma plataforma de pagamento online que permite que empresas aceitem pagamentos por meio de cartões de crédito, débito e outras formas de pagamento digital. O Stripe é uma plataforma de pagamento online líder que oferece soluções seguras e flexíveis para empresas que desejam aceitar pagamentos online.

O <a href="https://cachecowboy.medium.com/how-stripes-backend-handles-billions-in-payments-every-day-3c1c2003a6a8">Stripe</a> oferece várias funcionalidades, incluindo:

1. Processamento de pagamentos: O Stripe permite que empresas processem pagamentos online de forma segura e eficiente.
2. Integração com plataformas: O Stripe pode ser integrado a plataformas de e-commerce, como Shopify, WooCommerce e Magento.
3. Gestão de assinaturas: O Stripe permite que empresas gerenciem assinaturas e pagamentos recorrentes.

O Stripe é popular entre empresas devido às suas vantagens, incluindo:

1. Segurança: O Stripe oferece recursos de segurança avançados para proteger transações online.
2. Flexibilidade: O Stripe pode ser personalizado para atender às necessidades específicas de cada empresa.
3. Integração fácil: O Stripe oferece APIs e ferramentas para facilitar a integração com plataformas e sistemas existentes.

<img src="https://github.com/user-attachments/assets/f7bce4a2-bf5d-42ea-ad0b-0b367de29f07" align="right" height="77">

A **Stone** é uma empresa brasileira de tecnologia e serviços financeiros que atua principalmente no setor de pagamentos eletrônicos. Fundada em 2012, a Stone oferece soluções para empresas processarem pagamentos por meio de cartões de crédito e débito, além de uma série de serviços financeiros voltados para o crescimento de negócios, especialmente no mercado de pequenos e médios empresários. A Stone é reconhecida por suas maquininhas de cartão e por seu foco em atendimento ao cliente e inovação tecnológica.

- https://docs.openbank.stone.com.br/sandbox/docs/guias/integracao-para-fornecedores/notification/
- https://docs.openbank.stone.com.br/sandbox/
- https://online.stone.com.br/docs/o-que-e-o-stone-online
- Contato da Stone: 3004-9680

## [Gateway] Gateway-Independent Payment Provider
Como projetar uma **Camada de Provedor de Pagamento (Payment Provider Layer)** usando princípios LLD e Padrões de Design para manter a aplicação flexível, extensível e sustentável.

![1_aD2hPfOPysL9W-sP3RUQbw](https://github.com/user-attachments/assets/6f3887fa-cbf8-493d-83c5-3240a5b9199f)

Ao construir uma plataforma de comércio eletrônico ou produto SaaS, integrar um gateway de pagamento é inevitável. Mas associar sua aplicação de forma rígida a um gateway específico se torna uma dívida técnica que vem depois:

- ✔ E se o preço mudar?
- ✔ E se o gateway falhar durante os eventos de pico?
- ✔ E se o negócio expandir para vários países, exigindo múltiplos gateways?

Objetivos do Sistema:
- Seu app não deveria se importar com qual gateway de pagamento é usado.
- Você deve conseguir trocar o `Razorpay → o Stripe → PayPal` sem mudar o código da empresa.
- Adicionar um novo gateway deveria ser apenas adicionar uma nova classe adaptadora, não reescrever tudo.

Então você cria:

- Uma interface comum e ciclo de vida → `PaymentBase`
- Um ponto de entrada para pagamentos → `PaymentProvider`
- Um Factory para criar um adaptador adequado → `GatewayFactory`
- Uma classe por gateway → , , `RazorpayAdapterStripeAdapterPayPalAdapter`
- Um formato de resposta unificado → `PaymentOrderResponse`

2. Ciclo de Vida do Pagamento — Análise Profunda: Você apoia explicitamente estas etapas:

1. `CreateOrder`
2. Verificar o Pagamento
3. Webhook
4. Reembolso (opcional, mas parte da base)

1. `CreateOrder` (Criar Ordem) é quando seu backend informa ao gateway:

> "Quero coletar ₹X desse usuário, crie um pedido para ele."

No Gateway de Pagamento, você chama `.instance.orders.create({ ... })`

O que seu código faz:

- As chamadas de controlador/serviço:
- `paymentProvider.createOrder(options)`
- `PaymentProvider` encaminha isso para o adaptador selecionado:
- `return this.#Adapter.createOrder(options);`
- `RazorpayAdapter.createOrder()` Então:
- Chamadas de Razorpay SDK
- Recebe a resposta crua do Razorpay
- Converte para o formato padrão `PaymentOrderResponse`

Então, não importa qual gateway, seu app sempre mostra:

```json
{
  orderId,
  amount,
  currency,
  status,
  receipt,
  attempts,
  created_at,
  meta
}
```

Isso é extremamente importante: o código de negócios nunca toca nas respostas brutas do SDK.

2. Verificar a assinatura do pagamento - Certifique-se de que o pagamento não foi adulterado. Cada provedor implementa sua própria lógica:

Exemplo: Razorpay → verificação de hash HMAC SHA-256.

Então: `paymentProvider.verifyPaymentSignature({ orderId, paymentId, signature })`

retorna ou: True → Marca sucesso do pagamento Pedido de → Falsa Rejeitar (tentativa de fraude) ➡ Garantia de segurança com diferenças mínimas de chamadas

3. Manuseio de Webhook - Webhooks lidam com eventos como:

- Pagamento autorizado
- Pagamento capturado
- Processamento do reembolso
- Pagamento falhou
- Assinatura cobrada

Esses eventos:

- São enviados pelo Razorpay → seu backend.
- Não dependa da interface (é bom para confiabilidade).
- Deve ser verificado criptograficamente

Pontos profundos:

- Você verifica o webhook antes de analisar o JSON (correto para Razorpay).
- Você usa para evitar o tempo dos ataques.crypto.timingSafeEqual
- Você normaliza o webhook do gateway em seu próprio formato unificado de evento via .webhookSuccess

Então a camada de controlador/caso de uso não se importa se o Razorpay ou o Stripe dispararam o webhook: Ele sempre obtém: `.{ success, event, provider, orderId, paymentId, amount, ... }`

4. Pagamento do Reembolso

Em:`PaymentBase`

```js
async refundPayment(refundDetails) {
  throw new CustomError('refundPayment() must be implemented');
}
```

Em:`PaymentProvider`

```js
async refundPayment(details) {
  if (this.#Adapter.refundPayment) {
    return this.#Adapter.refundPayment(details);
  } else {
    throw new Error('Refund not supported by this payment gateway');
  }
}
```

Então:

- A lógica de negócios simplesmente chama `paymentProvider.refundPayment({ ... })`
- O adaptador traduz isso em uma API específica para gateway.

Novamente, comportamento unificado, implementações subjacentes diferentes.

3. Componentes Centrais (LLD): Estrutura de pastas

<pre>
paymentModule/
├── PaymentProvider.js
├── PaymentGateway/
│   ├── base.js
│   ├── RazorpayAdapter.js
│   ├── StripeAdapter.js
│   ├── PaypalAdapter.js
│   └── GatewayFactory.js
└── utils/
    ├── FormatOrderResponses.js
</pre>

Padrão de Design:

<img src="https://github.com/user-attachments/assets/8d8af17c-aa19-4ce5-a154-e61f92151c7a" align="right">

```md
| Pattern  | Implemented Where    | What It Fixes                      |
| -------- | -------------------- | ---------------------------------- |
| Template | PaymentBase          | Prevents duplicate lifecycle logic |
| Adapter  | RazorpayAdapter etc. | SDK differences                    |
| Factory  | PaymentFactory       | Dependency creation mess           |
| Strategy | PaymentProvider      | Switching providers at runtime     |
```

Template Design Pattern: Definido em → (classe abstrata); Subclasses sobrepõem apenas o que varia → fluxo comum permanece intacto `PaymentBase`. Por que esse padrão? Todo gateway de pagamento segue o mesmo ciclo de vida:

```md
config → createOrder → verifyPayment → webhook → refund
```

Mas cada gateway faz de forma diferente, então você fornece uma estrutura padrão e força funções essenciais a serem implementadas.

Como funciona no seu código:

```js
class PaymentBase {
  config() { throw new Error("must implement"); }
  async createOrder() { throw new Error("must implement"); }
  async verifyPaymentSignature() { throw new Error("must implement"); }
  async webhookHandler() { throw new Error("must implement"); }
  async refundPayment() { throw new Error("must implement"); }
}
```

Cada subclasse deve sobrescrever esses métodos ou falhar → impor correção.

- ✔ Ciclo
- ✔ de vida de pagamento reutilizável Contrato forte entre sistema e gateways
- ✔ Evita lógica de negócio duplicada em cada adaptador

```js
/**
 * PaymentGateway/base.js
 * Template Pattern design for payment gateways
 * Base class for payment gateways
 * config → createOrder → verify → webhook
 */
import { CustomError } from 'zoopse-crm-shared';
import { PaymentOrderResponse } from './formateClass.js';

class PaymentBase {
  #processedEventIds = new Set();
  constructor(config) {
    this.configData = config || {};
    this.config();
  }

  /** Initialize SDK or API client (must override) */
  config() {
    throw new CustomError('config() must be implemented by subclass');
  }
  /** Create payment order (must override) */
  async createOrder(orderDetails) {
    throw new CustomError('createOrder() must be implemented');
  }

  /** Verify payment (must override) */
  async verifyPaymentSignature(details) {
    throw new CustomError('verifyPaymentSignature() must be implemented');
  }

  /** Gateway webhook handler */
  async webhookHandler(req) {
    throw new CustomError('webhookHandler() must be implemented');
  }

  /** Refund payment (must override) */
  async refundPayment(refundDetails) {
    throw new CustomError('refundPayment() must be implemented');
  }

  /** Convert amount → smallest unit */
  toSmallestCurrencyUnit(amount) {
    return Math.round(amount * 100); // ₹ → paise, $ → cents
  }

  /** Common options used in all payment gateways */

  buildOrderResponse(raw, meta = {}) {
    if (raw.error) {
      return {
        success: false,
        error: raw.error
      };
    }
    return new PaymentOrderResponse({
      orderId: raw.orderId,
      amount: raw.amount,
      currency: raw.currency,
      status: raw.status,
      receipt: raw.receipt,
      attempts: raw.attempts,
      created_at: raw.created_at,
      meta
    });
  }
  checkDuplicateEvent = (eventId) => {
    if (this.#processedEventIds.has(eventId)) {
      return true; // Duplicate event
    }
    this.#processedEventIds.add(eventId);
    return false; // New event
  };
   /** Handle successful payment (common flow) */
  async webhookSuccess(webhookData) {
    if (!webhookData || !webhookData.orderId || !webhookData.paymentId) {
      throw new CustomError('Invalid webhook data');
    }
    const results = {
      success: true,
      event: webhookData.event || "payment_success",
      provider: webhookData.provider,
      orderId: webhookData.orderId,
      paymentId: webhookData.paymentId,
      amount: webhookData.amount,
      currency: webhookData.currency,
      status: webhookData.status,
      meta: webhookData.meta || {},
      timestamp: Date.now()
    };
    console.log("Unified Webhook:", results);
    return results;
  }
}

export default PaymentBase;
```

Factory Design Pattern: Aplicado em → RazorpayAdapter, StripeAdapter, PayPalAdapter

SDKs de terceiros possuem formatos diferentes de API:

- Razorpay → `orders.create()`
- Listra → `paymentIntents.create()`
- PayPal → `orders.capture()`

Suas classes Adapter traduzem esses nomes nos MESMOS métodos definidos no `PaymentBase`.

Por quê? Evitar a criação de objetos dispersos e manter a responsabilidade única.

## [Gateway] API Connect Stone 2.0
<img src="https://github.com/user-attachments/assets/96aeb644-10e9-484b-80ba-6dc99b8b1748" align="right" height="77">

O **Connect Stone** é uma camada de integração entre o sistema do Cliente / Parceiro, podendo ser uma Plataforma de Ecommerce ou um PDV, com o POS. Tem como objetivo realizar a automação entre os sistemas - Gerenciamento e Pagamento - e a integração entre canais - Físico e Digital. Essa é uma solução de conexão de sistemas de uma forma simples, rápida e completa às maquininhas Stone. 

O Connect 2.0 é uma camada de integração entre o sistema do Cliente / Parceiro, Software de Gestão, com o POS Stone. Tem como objetivo realizar a automação entre os sistemas - Gerenciamento e Pagamento. 

Com esta solução, é possível criar pedidos via <a href="https://docs.pagar.me/reference/introdu%C3%A7%C3%A3o-1">API Pagar.me</a>, pagá-los presencialmente pelo **terminal POS** (a maquininha) e receber a resposta das transações em tempo real.

> A realidade do cliente que usa um sistema/ERP sem integração com as transações, e como conseguimos somar forças para melhorar a produtividade do nosso cliente! Para que seu estabelecimento possa ter esta funcionalidade, o primeiro passo é entrar em contato com o seu software de gestão (PDV) e entender com os mesmos se eles já estão integrados no produto. Caso não, o seu sistema de gestão precisa entrar em contato com a Stone por meio do nosso Programa de Parcerias. A ativação e próximos passos serão executados com o Software de Gestão e a equipe de integração da Stone. 

Com esta solução, é possível criar pedidos via API Pagar.me (integrada em seu PDV ou app inStore), pagá-los presencialmente pelo terminal POS (a maquininha) e receber a resposta das transações em tempo real via nossos webhooks.

Abaixo, detalhamos um fluxo simplificado em que um PDV interage com a API do Pagar.me para que um pedido criado seja pago presencialmente através da maquininha. Fluxo da Solução:

<div align="center">
  <img src="https://github.com/user-attachments/assets/532e9ecd-043e-47e8-86fd-387e58ce5ef3">
</div>

Participantes do fluxo do Connect 2.0:

<img src="https://github.com/user-attachments/assets/b9aa1e7a-1e3b-48fc-a0f7-02eaae820923" align="right" height="777">

1. PDV Parceiro (Oracle NetSuite): Mais conhecido também como Software/Sistema de Gestão, este é o participante responsável pela ponte entre a Stone e o Estabelecimento. O software de gestão tem como responsabilidade todo o desenvolvimento da integração com a Stone para implementação do produto. Cabe a ele também a realização de todo o processo de ativação e implantação do produto no estabelecimento comercial. 

2. Pagar.me: Esta é nossa API responsável pela comunicação entre o Software de Gestão e a maquininha Stone. Por meio desta integração que há a criação dos pedidos de vendas, fechamento da transação e impressão de NFe. O software de gestão quando realiza a integração se comunica com a API Pagar.me. 

3. POS Stone: A maquininha Stone é a responsável pelo recebimento do pedido gerado pela API Pagar.me. No POS Stone é onde há o pagamento da transação, cancelamento, consulta de relatório de vendas e impressão de NFe. 

Com uma simples integração via API, também é possível realizar impressões de notas fiscais e cupons após o pagamento de um pedido via POS integrado. Você pode saber mais em Impressão de nota fiscal. Além disso, nossa solução oferece dois fluxos transacionais e a possibilidade de impressão de nota fiscal ou cupom, aumentando ainda mais as possibilidades de integração e os casos de uso. Saiba mais em Fluxos transacionais e Impressão de nota fiscal.

Por meio da integração com Connect 2.0, conseguimos:

- Tornar o fluxo de venda muito mais ajustado e dinâmico;
- Ter maior controle das informações de cada venda;
- Transações vinculadas e conciliadas diretamente no seu sistema de gestão;
- Aumento de produtividade do estabelecimento;
- Diminuição de erros humanos no processo de conciliação de vendas;
- Mobilidade no processo de fechamento de vendas, já que a máquina não precisa estar cabeada com o sistema de gestão para receber o pedido gerado. 

Hoje, o Connect Stone suporta os seguintes modelos de terminais:

<div align="center">
<img src="https://github.com/user-attachments/assets/aaf78ab8-96b6-4bcf-8a4d-ff023a919280" height="377">

<!-- <sub><b>A8, P2 e L3</b> (Recomendado por melhor performance com o produto integrado)</sub> -->
</div>

A operação é feita através da:

1. Ativação: Para que você possa ter acesso a esta integração, é necessário que o seu Software de Gestão ou PDV realize uma integração com a API Connect 2.0, para isso é necessário que o mesmo esteja cadastrado no nosso Programa de Parcerias.
2. Tipo de Integração: Existem duas configurações que podem ter para o pedido para aparecer no POS: Pedido Direto e Pedido Listado. Esta definição acontece no momento do credenciamento/ativação do produto pelo Parceiro de Software.

Pedido Direto: Nesta configuração, a partir do momento que acontece a requisição de criação do pedido, o mesmo já consta no POS com as informações de pagamento, tipo de transação (crédito, débito, voucher), parcelamento e valor. O consumidor final irá apenas digitar a senha para finalizar o pagamento.
      
![image](https://github.com/user-attachments/assets/62204edd-fe29-4350-9768-61a02c7d664a)

Pedido Listado: Por meio desta configuração é permitido que o cliente crie uma lista de pedidos a serem pagas no POS. Nesse fluxo, o POS integrado irá listar todos os pedidos em aberto para esse terminal nessa conta no Pagar.me, e o cliente seleciona qual será pago. Este modelo funciona bem, por exemplo, para operações de delivery. 

![image](https://github.com/user-attachments/assets/77f2a4bb-936f-4679-9cee-715267dcd630)

Este modelo funciona bem, por exemplo, para operações de PDV. 

Nesta configuração, a partir do momento que acontece a requisição de criação do pedido, o mesmo já consta no POS com as informações de pagamento, tipo de transação (crédito, débito, voucher), parcelamento e valor. O consumidor final irá apenas digitar a senha para finalizar o pagamento. 

Este modelo funciona bem, por exemplo, para operações de PDV. 

Versionamento do App de pagamento: Com o terminal em mãos, o próximo passo é verificar se o App de Pagamento Stone está preparado para receber as requisições de transações. Para que os pedidos gerados constem no POS, você precisa clicar no App de Pagamento e na tela deve constar a mensagem "Aguardando Pagamentos", como na imagem abaixo:

A integração com o Connect Stone pode ser aplicada em alguns cenários e modelos de negócio. Sendo feita de modo bem simplista, atendendo as necessidades básicas.

**Automação de Pagamentos**: Quando analisamos o fluxo de pagamento hoje de um lojista, notamos que existem duas principais ferramentas que são usadas: o software de gestão/PDV e a maquininha de cartão.

Sabemos que a maioria dos estabelecimentos operam de forma desintegrada, ou seja, não existe uma automação dos pagamentos que são transacionadas, gerando os seguintes problemas:

- Operação manual de gerenciamento de vendas
- Passividade de erros humanos na conciliação;
- Perca de tempo do lojista na conferência das vendas.

**Fluxos transacionais**: Para utilizar esta solução você deve ativar a integração e escolher o fluxo transacional utilizado pelo POS. Existem dois tipos de fluxos transacionais pré-definidos para realizar pagamento via POS:

1. Listagem de pedidos: Essa integração permite que o parceiro crie uma lista de pedidos a serem pagas no POS. Nesse fluxo, o POS integrado irá listar todos os pedidos em aberto para esse terminal nessa conta no Pagar.me. Dessa forma, o operador do POS poderá selecionar um pedido para realizar seu pagamento.

![image](https://github.com/user-attachments/assets/1137726d-41e2-497d-a612-d1b93e2b523d)

2. Pagamento direto de pedidos: Essa integração permite que o parceiro crie um pedido a ser pago diretamente para um POS integrado. Nesse fluxo, o POS fica em espera, aguardando a criação de uma cobrança direcionada a ele. Ao receber um pedido, o POS entra automaticamente na tela de pagamento para realizar a transação. O fluxo ao realizar pagamento ou cancelamento de transações é idêntica à _1. Listagem de pedidos_:

![image](https://github.com/user-attachments/assets/b4240d1c-2d2d-42ae-91d5-22bbc4c5af33)

3. Impressão de NFe: Por meio do produto do Connect 2.0 é possível a impressão de NFe diretamente da maquininha (a geração/emissão da mesma é de responsabilidade do seu sistema de gestão). Para que esta funcionalidade seja habilitada na máquina, entre em contato com seu software de gestão e demonstre o interesse. 

**FLUXO ONDE NÃO EXISTE TRANSAÇÃO INTEGRADA**:

<img src="">

Pensando, nestes pontos de gargalo que uma operação desintegrada pode gerar, foi desenvolvido o Connect Stone. Através desta integração, conseguimos manter todos os vínculos entre uma venda criada no sistema e a transação feita na maquininha.
Por meio desta integração, conseguiremos ajudar nosso cliente a ter um fluxo de venda muito mais ajustado e dinâmico, e também a ter um maior controle das informações de cada transação, melhorando a experiência e aumentando a produtividade do estabelecimento.

**FLUXO COM TRANSAÇÃO INTEGRADA**:

<img src="">

**Autenticação**: Antes de começar, você precisa obter suas chaves de API. Para isso, siga os seguintes passos:

1. Acesse este link e faça login com seu usuário,
2. Após acessar o Dash, navegue até a área de Desenvolvimento e em seguida clique em Chaves.

Tipos de Chave:

> 🚧 **Ambiente de Sandbox**: O ambiente de testes é aplicável somente no ambiente de criação de pedidos na API do Pagar.me. Dado que não possuímos ambiente de testes na Stone, os pedidos criados no Pagar.me em ambiente de Sandbox não serão refletidos no POS. Com isso, não possuímos um ambiente de Sandbox para realizar testes de ponta a ponta no Connect Stone. e os pedidos devem ser criados diretamente no ambiente de produção.

# 🤑 TEF - Transferência Eletrônica de Fundos
Inicialmente, utilizariamos a **Transferência Eletrônica de Fundos (TEF)** que é um sistema de pagamentos que faz a comunicação das transações de forma automática e entre diversas adquirentes (as empresas de cartões). O sistema envia os pagamentos de qualquer tipo de cartão para a liquidação financeira de pagamentos pelas operadoras. A TEF permite a integração das vendas em cartão e o sistema de vendas da empresa, sem a necessidade de realizar a conciliação de forma manual ou por meio de um software. Como tudo é feito de forma integrada, a chance de erros é muito menor. 

Fluxo da solução:

<a href="https://blog.vindi.com.br/tef/"><img src="https://github.com/user-attachments/assets/066b3faf-6655-4e91-bfef-b1f4c4157d46"></a><br>

Além da facilidade de <a href="https://youtu.be/n3I6lXQYufo">integração</a> das informações e da possibilidade de aceitar diversos cartões com um só equipamento, o processamento dos pagamentos por meio de TEF costuma ser mais barato. Trabalhar com TEF costuma ser a melhor opção para empresas que realizam as vendas dentro da própria loja, com processamento em caixa, e que têm uma alta taxa de recebimento em cartões. 

A TEF é geralmente utilizada em redes de lojas como supermercados ou estabelecimentos com muitos caixas onde a rotatividade de consumidores é grande. Há uma confusão muito comum no mercado, pois a TEF não é um equipamento (hardware), sendo somente a transação, ou seja, é o sistema de pagamentos (software) que faz a comunicação de forma automática entre diversas adquirentes. O que ficou popularmente conhecido como TEF – na realidade – é um **PIN Pad** (hardware) que está conectado a um computador através de um fio (cabo) e o sistema TEF está instalado nesse computador.

TEF é a sigla para Transferência Eletrônica de Fundos. Ele é um sistema que possibilita que vendas de crédito, débito ou voucher sejam realizadas de maneira mais eficiente. É importante saber que TEF é um serviço destinado para clientes pessoa jurídica (CNPJ) com vendas acima de R$ 40.000,00 por mês. Este valor pode ser de uma unidade ou a soma de algumas unidades do estabelecimento comercial (matriz + filiais). Se o seu negócio não possui CNPJ, portanto classificado como pessoa física (CPF) procure sobre os produtos POS ou TON.

Principais vantagens da solução TEF:

1. Por ser um produto multiadquirente, permite que um mesmo ponto de venda possa ser configurado para uso de várias adquirentes (Stone, Cielo, GetNet, etc), preservando as particularidades do serviço de cada uma delas.
 
2. Emissão de nota fiscal juntamente com o pagamento da compra, tornando-o mais seguro contra fraudes internas.

3. Centralização e organização das transações da loja principal e suas filiais.

4. Diminuição de fraude tributária por meio de emissão de cupom fiscal.

5. Realiza o processo de pagamento incluindo corretamente a venda aos itens vendidos.

6. Conciliação com os recebimentos no Banco de forma automatizada.

7. Velocidade no atendimento dado aos clientes.

8. Relatórios detalhados de suas vendas em tempo real, possibilitando um controle de estoque mais eficiente.

9. Autorização de venda em segundos (a caixa registradora envia o valor total da compra diretamente para o pinpad).

10. Segurança para seus clientes.

11. Diminuição da perda de vendas por conexão.

12. Compatibilidade com muitas **bandeiras** (brands).

Principais desvantagens da solução TEF:

1. Período extenso de desenvolvimento para algo pequeno.

2. Maior custo em equipamentos de hardware.

<a href="https://tefdoc.stone.com.br/docs/getting-started"><img src="https://github.com/user-attachments/assets/9197b041-baeb-43d0-a213-1b60548b01ec" align="right" height="177"></a>

O **AutoTEF** é um SDK .Net que permite realizar pagamentos (Débito, Crédito, Voucher) do tipo TEF no Autorizador Stone, por se tratar de um produto backend é preciso que o Parceiro desenvolva um Frontend para se integrar ao SDK. Por ser construído em .Net é nativo em Windows. Através do AutoTef, você pode fazer todas as operações de pagamento:

- Compra
- Cancelamento
- Impressão de comprovante
- Controle do Terminal PINPad

Para se integrar com a SDK da Stone, você precisa passar por três etapas:

1. Integração: Nessa fase ainda não é necessário ser cliente cadastrado para enviar requisições. Possuímos um ambiente de sandbox (testes), onde é possível testar todas as situações que ocorrem em produção, como transações negadas e timeout. Para receber uma credencial de integração é necessário se cadastrar no nosso programa de parcerias. Cadastre-se aqui.

2. Homologação: Após finalizar o desenvolvimento do seu aplicativo, você receberá um roteiro de testes que deve ser preenchido e enviado para que possamos validar sua integração. Esse é um processo de teste, que tem como objetivo validar se todos os casos transacionais estão sendo executados com sucesso para que você não tenha problemas ao colocar sua solução em produção. Nós da Stone Partner Hub acompanharemos todo o processo com você. Procure seu Gerente Comercial (O comercial responsável pela sua conta na Stone) para iniciar o processo de homologação.

3. Produção: Ao final da homologação, você receberá o nosso OK para ir para produção. Neste momento o Gerente Comercial Stone responsável por sua parceria conosco irá disponibilizar o manual do parceiro contendo todas as informações necessárias para que sua operação cresça com a gente. Caso já tenha se cadastrado no nosso programa de parcerias, entre em contato com seu Gerente Comercial na Stone ou através do email integracoes.parcerias@stone.com.br. Caso ainda não tenha se cadastrado, você consegue fazê-lo aqui.

Para entender melhor os termos utilizados em nossa documentação, aqui estão a definicação de alguns conceitos da Stone. Veja alguns dados importantes para inicializar o SDK da Stone:

```txt
Nome Fantasia: 
Documento: 
Data de criação: 
Stonecode: 
SAK: 
```

<table><thead><tr><th>Termo</th><th>Definição</th></tr></thead><tbody><tr><td>StoneCode</td><td>É o código de identificação do seu cadastro aqui na Stone. Ele também é conhecido como código de afiliação. É o código do estabelecimento dentro da Stone. Esse valor é único e cada estabelecimento possui apenas um Stone Code. É o código do estabelecimento dentro da Stone. Esse valor é único e cada estabelecimento possui apenas um Stone Code.</td></tr><tr><td>SAK</td><td>Sales Affiliation Key. É uma chave gerada no seu cadastro que possibilita a configuração do TEF junto à Stone. Por ser uma informação sensível, essa chave é confidencial. Dessa forma, evitamos problemas de segurança e mantemos a integridade do seu negócio. Portanto, é de suma importância ela estar habilitada. É uma chave privada, enviada na mensageria da transação a qual identifica o estabelecimento comercial que fez a requisição do pagamento. Para cada Stone Code é gerada uma SAK.</td></tr><tr><td>ATK</td><td>É o código de autorização gerado pela Stone, tanto para transações aprovadas, como para transações negadas. Este é o valor que será referência para identificar uma transação na Stone. Também é conhecido como StoneID ou NSU Host</td></tr><tr><td>ITK</td><td>É o código de identificação da transação gerado pela aplicação. Este valor não pode se repetir para uma SAK por 5 anos. Pode ser formatado seguindo a seguinte sugestão de regra: nnnnnn+DATA. Sendo que nnnnnn = número sequencial gerado pelo sistema (hash) que se comunica com o host Stone, e a data no formato ddmmyyyyHHMMSS..</td></tr><tr><td>Autorização e Captura</td><td>Ao iniciar uma compra, o primeiro passo é realizar uma autorização da transação, ou seja, para garantir que o portador do cartão tenha limite para comprar o produto/serviço. Após ter a autorização aprovada, é realizada uma captura da transação, ou seja, a confirmação do valor autorizador anteriormente..</td></tr><tr><td>Cancelamento</td><td>É uma transação que ocorre quando o cancelamento é realizado no mesmo dia ou em um dia diferente da compra. No primeiro caso o valor é recebido pelo portador do cartão em até 5 dias, variando de emissor para emissor. No segundo caso o valor é recebido pelo portador do cartão na próxima fatura.</td></tr><tr><td>Reversão (Fallback)</td><td>É uma operação que ocorre quando há algum problema de comunicação entre SDK e o autorizador da Stone. Ela faz com que não hajam cobranças incorretas na fatura do portador do cartão.</td></tr><tr><td>AUT</td><td>É o código de autorização do emissor.</td></tr></tbody></table>

Para implantar a Stone em seu sistema de TEF, atente-se às etapas abaixo, pois são bem importantes:

1.  Dentro do seu módulo/sistema de TEF, as URLs de comunicação com a Stone são:

    - URL (Transações): https://tef.stone.com.br
    - URL (Terminal/Carga): https://tms.stone.com.br

2. Caso possua Proxy/Firewall em sua rede, libere a conexão para os endereços acima.

3. Garanta que não há nenhuma regra de Firewall no Windows ou em qualquer dispositivo de rede. Caso exista alguma regra de firewall em seu sistema, os IP’s Stone precisam ser liberados. Os ranges de IP que devem estar liberados são:

   - `192.86.4.0/24`
   - `206.51.241.0/24`
   - `199.250.254.0/24`
   - `209.10.137.0/24`
   - `209.10.240.0/24`

4. Mantenha o seu sistema de TEF com o módulo Stone e os caixas (pontos de venda) sempre atualizados com as versões mais recentes, evitando problemas transacionais.

> Lembre-se: Caso precise de ajuda para solicitar as versões mais novas, entre em contato com a empresa que presta suporte nas configurações do seu TEF.

5. Caso você use algum serviço de conciliação, atualize a versão da sua ferramenta.

> Lembre-se: O arquivo de conciliação deve ser baixado a partir das 5 horas da manhã, sendo referente ao dia anterior.

6. Verifique com sua empresa de automação comercial a necessidade de alguma alteração no sistema/software da loja e/ou caixa.

7. Caso tenha adquirido pinpad Stone, efetue a instalação dos drivers conforme orientação abaixo. 

8. Altere para a Stone o roteamento das **bandeiras** (brands) negociadas com nosso Comercial. 

9. Aplique a carga de tabelas e realize uma transação teste no débito e no crédito para avaliar se está tudo correto. 

Qualquer dúvida relacionada ao SDK ou a PIN Pad, deve ser reportada ao time de suporte de desenvolvimento da Stone, aqui está alguns canais de suporte da provedora:

- Stone: 3004-9681
- Email: parceiro@stone.com.br
- Site: https://partnerhub.stone.com.br/#/home
- Docs: https://online.stone.com.br/docs/o-que-e-o-stone-online

Outro ponto bastante importante é a **TEF House** (ou House de TEF) que é uma empresa especializada em fornecer soluções de *Transferência Eletrônica de Fundos* (TEF) no contexto de pagamentos eletrônicos. Essas empresas desenvolvem e oferecem sistemas que integram terminais de pagamento, como <a href="https://acessocredenciamento.blogspot.com/p/bandeiras_5667.html">máquinas de cartão</a>, aos sistemas de automação comercial de estabelecimentos, facilitando a comunicação entre bancos, operadoras de cartão e o sistema de vendas. A TEF House oferece diversos serviços, como autorização de pagamentos, segurança nas transações, e integração com múltiplas operadoras de cartão, além de garantir o compliance com as normas de segurança, como o PCI-DSS.

<a href="https://www.linx.com.br/fisico/"><img src="https://github.com/user-attachments/assets/d610ea07-3a66-44ff-a9d3-c31efdd8f99b" align="right" height="77"></a>

A **Linx TEF House** é uma solução de Transferência Eletrônica de Fundos (TEF) que oferece a possibilidade de gerenciar transações de pagamento de maneira centralizada. Desenvolvida pela Linx, uma empresa especializada em soluções de tecnologia para o varejo, essa plataforma permite processar e autorizar transações com cartões de crédito, débito, vouchers e outros métodos de pagamento em ambientes de varejo e comércio, seja físico ou digital.

0. **Contato da Linx**: +55 51 9470-1205

1. **Integração Centralizada de Pagamentos**: A Linx TEF House permite que o varejista centralize o processamento de transações de diferentes bandeiras e tipos de pagamento, o que facilita a gestão dos pagamentos. A solução se conecta a várias adquirentes (como Cielo, Rede, Getnet, etc.) e a gateways de pagamento, unificando o processo de autorização em uma única interface.

2. **Segurança e Conformidade (PCI DSS)**: A solução está de acordo com o PCI DSS (Padrão de Segurança de Dados da Indústria de Cartões de Pagamento), garantindo que as informações sensíveis dos cartões sejam processadas e armazenadas com segurança. Oferece mecanismos de proteção, como criptografia, para garantir a segurança dos dados durante o processamento e transmissão de informações de pagamento.

3. **TEF Discado, Dedicado e IP**:
   - **TEF Discado**: Usa uma linha telefônica discada para se conectar ao servidor central de autorização de pagamentos. É uma opção mais lenta, mas pode ser útil em áreas com conectividade limitada.
   - **TEF Dedicado**: Utiliza uma linha dedicada para processar as transações de pagamento, sendo mais rápida e confiável do que a discada.
   - **TEF IP**: Funciona por meio da internet, permitindo processamento em tempo real. Essa modalidade é a mais utilizada devido à sua rapidez e eficiência, especialmente em ambientes com conexão de alta velocidade.

4. **Gestão de Transações e Relatórios**:
   - Linx TEF House oferece funcionalidades de monitoramento e geração de relatórios detalhados sobre as transações processadas. Isso permite que os varejistas acompanhem o desempenho de vendas, taxas de aprovação e falhas de transações em tempo real.
   - Inclui funcionalidades para consulta, cancelamento e reimpressão de comprovantes, facilitando a gestão de transações diretamente pelo sistema.

5. **Flexibilidade de Integração com Sistemas de PDV**: A TEF House pode ser integrada a diferentes sistemas de ponto de venda (PDV), seja por APIs, SDKs ou módulos específicos de integração, facilitando a adoção em ambientes variados de varejo.

6. **Redução de Custos Operacionais**: Como a Linx TEF House centraliza todas as operações de pagamento, ela pode reduzir a necessidade de múltiplos dispositivos e provedores de serviço, diminuindo os custos operacionais e melhorando a experiência de pagamento.

Para implementar a Linx TEF House, o varejista conecta o sistema de PDV à plataforma Linx TEF, que passa a gerenciar as comunicações entre o PDV e os adquirentes de pagamento. Com isso, o processo é algo como:

1. **Início da Transação**: O sistema de PDV envia uma solicitação de pagamento para a Linx TEF House.
2. **Autorização**: A Linx TEF House roteia a transação para o adquirente apropriado.
3. **Resposta**: O adquirente envia a resposta de aprovação ou rejeição da transação para a Linx TEF House, que encaminha ao PDV.
4. **Finalização**: Se aprovada, a transação é concluída no PDV; caso contrário, é possível tentar novamente ou processar uma forma alternativa de pagamento.

Alguns dados importantes para inicializar o Linx TEF House:

```txt
IP 
Cód. Empresa: 
pdv 
```

Vantagens da Linx TEF House:

- **Rapidez e eficiência** na comunicação com adquirentes.
- **Segurança e conformidade** com PCI DSS.
- **Escalabilidade**, atendendo desde pequenas lojas até grandes redes varejistas.
- **Controle centralizado** e relatórios para otimizar a gestão de transações.

A Linx TEF House é, portanto, uma solução robusta para varejistas que buscam centralizar e otimizar o gerenciamento de pagamentos eletrônicos, com flexibilidade e segurança.

O funcionamento do sistema é determinado pelo seguinte fluxo de integração de um app back-end escrito na linguagem de programação C# (C-Sharp) com a sua biblioteca .NET que faz a integração do Pipefy com uma TEF (Transferência Eletrônica de Fundos) utilizando o Oracle NetSuite PDV e a provedora de soluções de pagamentos Stone.

1. Arquitetura Geral: A aplicação back-end em .NET será responsável por orquestrar a comunicação entre os três sistemas:

   - <a href="">Pipefy</a>: Gestão de processos e automações (para capturar ou enviar dados dos clientes e processos).
  
   - <a href="">Oracle NetSuite PDV</a>: Sistema de gestão financeira, com integração ao PDV para controle de vendas e recebimentos.
  
   - <a href="">Stone/Linx (TEF)</a>: Integração com a Stone para processar pagamentos eletrônicos de forma segura. O PIN Pad (``) é a “máquina” em que o pagador fará o uso do cartão para executar o pagamento através de uma Transferência Eletrônica de Fundos (TEF). Ele é um equipamento de leitura de cartões de crédito e débito que aceita diversas bandeiras e vários bancos e precisa estar conectado a um computador ligado ao sistema de TEF. Visualmente falando, o aparelho é muito semelhante às outras máquinas de cartão, mas o PIN Pad é apenas um leitor de cartões e não funciona sozinho. Ele precisa de conexão com os demais elementos do sistema para viabilizar e concluir o pagamento da operação.

> Lembre-se: Não há necessidade de um banco de dados, os dados já estão armazenados dentro do Pipefy e do Oracle NetSuite.

2. Componentes Principais:

2.1. Pipefy Integration

- Objetivo: Capturar dados dos processos Pipefy (dados dos clientes, pedidos) e enviar os resultados do pagamento para registrar nos cards/processos.
- Ferramentas: GraphQL API do Pipefy.

Tarefas: Consulta de cards relacionados a vendas ou pagamentos pendentes. Atualização dos cards com status de pagamento e informações da transação via webhook.

2.2. Oracle NetSuite PDV Integration

- Objetivo: Registrar os pagamentos e transações no sistema financeiro de vendas (PDV).
- Ferramentas: API REST do Oracle NetSuite.
- Tarefas: Envio dos dados de transações de vendas e pagamentos e atualização de estoques e emissão de notas fiscais após finalização do pagamento.

2.3. TEF Stone Integration

- Objetivo: Processar pagamentos via Stone TEF.
- Ferramentas: SDK ou API da Stone/Linx. Aqui entra o papel da Linx em prover e dar suporte a esse SDK da TEF.
- Tarefas: Processamento de pagamentos no terminal de venda (TEF). Retorno do status da transação (aprovado/rejeitado) para atualizar no Oracle NetSuite PDV e Pipefy.

3. Fluxo do Processo de Integração:

3.1. Etapa de Início: Recebendo Pedido do Pipefy

1. O sistema de Pipefy notifica via Webhook que um novo processo de venda foi criado ou que um pagamento pendente precisa ser processado.
2. Capturar o ID do processo no Pipefy.
3. Obter dados da venda (ex: valor, detalhes do cliente).

3.2. Processamento do Pagamento (TEF):

1. A aplicação .NET realiza uma chamada à API da Stone para iniciar o processo de pagamento.
2. Dados do pedido e do cliente são enviados (ex: valor total, método de pagamento) = nosso comprovante.
3. A aplicação aguarda a resposta da transação (aprovada/rejeitada).

4. (`if`) Caso o pagamento seja aprovado: A aplicação recebe os detalhes da transação (ID da transação, status). O pagamento é registrado no Oracle NetSuite PDV.

5. (`else`) Caso o pagamento seja rejeitado: O status é atualizado no Pipefy e uma notificação pode ser enviada para reprocessar ou solicitar novo pagamento.

3.3. Atualização no Oracle NetSuite PDV
Com o pagamento aprovado, o sistema atualiza o Oracle NetSuite PDV:
- Registrando a transação financeira no módulo de vendas.
- Atualizando o estoque (caso necessário).
- Emitindo a nota fiscal eletrônica (caso configurado).

3.4. Finalização no Pipefy
A aplicação .NET faz uma chamada à API do Pipefy para atualizar o status do card de venda:

- Status atualizado para "Pago".
- Detalhes da transação adicionados no card (ex: ID da transação, valor, data).

3.5. Manutenção e Monitoramento
- Implementar um serviço de BackgroundService para monitorar transações pendentes ou falhas:
- Verificar o status de transações não concluídas.
- Reprocessar automaticamente em casos de falha de comunicação.

4. Tecnologias e Ferramentas Envolvidas
- .NET Core (C#): Para implementação do back-end e integração entre os serviços.
- Pipefy API (GraphQL): Para capturar e atualizar informações de processos.
- Oracle NetSuite API: Para sincronização de dados de vendas e estoque no PDV.
- Stone SDK/API: Para processar pagamentos via TEF.
- RabbitMQ: (opcional) Para gerenciamento de mensagens assíncronas (pagamentos pendentes, retries).
- Entity Framework: Para persistência dos dados locais e log de transações.
- Docker: Para containerização da aplicação.

5. Considerações
- Segurança: Certifique-se de usar TLS/SSL nas integrações e armazenar tokens de autenticação de forma segura.
- Logs e Auditoria: Implementar logging detalhado para auditoria de transações financeiras.
- Tratamento de Erros: Implementar estratégias de retry em caso de falhas de comunicação, principalmente nas integrações com o TEF e Pipefy.
- Escalabilidade: Usar filas de mensagens (ex: RabbitMQ) para processar grandes volumes de pagamentos ou integrações com o Oracle NetSuite.

Conclusão: Este fluxo cobre o ciclo completo de integração entre Pipefy, Stone/Linx (TEF) e Oracle NetSuite PDV, garantindo a sincronização de dados e processamento seguro de pagamentos.

<img src="https://www.svgrepo.com/show/354186/pipefy.svg" align="right" height="77">

O **Pipefy** é uma plataforma CRM de gestão de processos que permite às empresas automatizar fluxos de trabalho, melhorar a colaboração e aumentar a eficiência operacional. Através de uma interface intuitiva, os usuários podem criar e gerenciar processos personalizados sem a necessidade de programação, facilitando a integração com outras ferramentas e sistemas. É ideal para equipes que desejam otimizar processos, como vendas, atendimento ao cliente e gerenciamento de projetos.

O processo de integração do Pipefy com o TEF é por meio da API do Pipefy sendo manipulada pelo GraphQL e conectada com a API do Oracle NetSuite onde contém o nosso PDV que funciona em REST API para realizar as transações e para controlar essas requisições foi desenvolvido uma aplicação escrita em C#/.NET. Através do NetSuite, você pode gerenciar o fluxo de vendas, controlar estoque e emitir recibos, enquanto as transações de pagamento são feitas de forma segura via TEF.

Para fazermos as requisições via API GraphQL, vamos precisar ter em mãos os seguintes dados:

- **Pipe**: `Confirmação de entradas - 2`
- **Cards**: `Conf. entrada + aceite Netsuite`

Então, foram levantadas as seguintes etapas para a iniciação do projeto:

1. Token de autenticação do Pipefy: https://app.pipefy.com/tokens

2. Roles de permissão do usuário Pipefy: Admin

3. Credenciais de parceiros com a provedora do TEF ou contratos com aplicações de terceiros. A NetSuite tem a melhor proposta que atende aos requisitos da empresa.

4. API do Pipefy: https://api.pipefy.com/graphql

Para consultar o card de vendas do Pipefy:

```gpl
query {
  card(id: [card]) {
    title
    done
    id
    fields {
      date_value
      datetime_value
      filled_at
      float_value
      indexName
      name
      native_value
      report_value
      updated_at
      field {
       label
       id
      }
      value
    }
    updated_at
  }
}
```

Exemplo de integração entre o Pipefy e o Oracle NetSuite:

```javascript
const axios = require('axios');

// Credenciais e URLs
const netsuiteAuth = {
  username: '',
  password: '',
};

const pipefyAuth = {
  token: '',
};

const netsuiteUrl = 'https://api.netsuite.com/rest/record/v1/customer';
const pipefyUrl = 'https://api.pipefy.com/graphql';

// Função para obter dados do NetSuite
async function getNetsuiteData() {
  try {
    const response = await axios.get(netsuiteUrl, {
      auth: {
        username: netsuiteAuth.username,
        password: netsuiteAuth.password,
      },
    });

    return response.data;
  } catch (error) {
    console.error('Erro ao obter dados do NetSuite:', error);
    throw error;
  }
}

// Função para enviar dados para o Pipefy
async function sendToPipefy(data) {
  const query = `
    mutation {
      createCard(pipe_id: SEU_PIPE_ID, title: "Novo Cliente", fields_attributes: [{field_id: SEU_CAMPO_ID, field_value: "${data.valor}"}]) {
          card {
              id
          }
      }
    }
  `;

  try {
    const response = await axios.post(pipefyUrl, { query }, {
      headers: {
        Authorization: `Bearer ${pipefyAuth.token}`, // Inclir o Token
      },
    });

    return response.data;
  } catch (error) {
    console.error('Erro ao enviar dados para o Pipefy:', error);
    throw error;
  }
}

// Executa a integração
async function runIntegration() {
  try {
    // Obtém dados do NetSuite
    const netsuiteData = await getNetsuiteData();

    // Envia dados para o Pipefy
    const pipefyResponse = await sendToPipefy(netsuiteData);

    console.log('Integração concluída com sucesso:', pipefyResponse);
  } catch (error) {
    console.error('Erro na integração:', error);
  }
}

// Executar a integração quando o script é executado
runIntegration();
```

# 📋 CMS - Content Management System
Um **sistema de gerenciamento de conteúdo (CMS)** ajuda as empresas a gerenciarem os conteúdos digitais. Equipes inteiras podem utilizar esses sistemas para criarem, editarem, organizarem e publicarem conteúdos. Ele atua como um ponto focal para armazenar conteúdo e fornece processos automatizados para gerenciamento e criação colaborativa de conteúdo digital usando fluxos de trabalho internos (ou projetados). Privilégios diversos e responsabilidades são fornecidos aos usuários, com base seus cargos e funções. 

CMS é a sigla para Content Management System, ou Sistema de Gestão de Conteúdo, em tradução livre. Trata-se de uma plataforma que facilita a criação, edição e publicação de conteúdos. Além disso, integra ferramentas analíticas e automação de fluxos de trabalho, o que é ideal para diversos tipos de sites. 

Trata-se de uma alternativa para facilitar o processo de criação de blogs profissionais ou pessoais, que dispensa a necessidade de entender códigos de programação como HTML e CSS. Com um conhecimento básico de computação, é possível desenvolver seu próprio conteúdo sem dificuldade, com ferramentas totalmente intuitivas, claras e didáticas. Esse é o caso do WordPress, uma das plataformas mais populares para a criação e gestão de websites.

Um CMS é um sistema de gerenciamento de conteúdo que abstrai operações de CRUD, governança e publicação, oferecendo interfaces administrativas e mecanismos de entrega para disponibilização de conteúdo em aplicações web, mobile ou APIs, podendo ou não ser oferecido como SaaS.

Por exemplo, os autores podem publicar e salvar os seus conteúdos. Porém, os editores podem publicar e alterar esses conteúdos. Os administradores podem fazer tudo isso, bem como conceder permissões aos outros integrantes da empresa para que possam atualizar ou revisar o conteúdo.

Um CMS ajuda a criar e gerenciar sites e conteúdo de sites usando o mínimo de sobrecarga técnica, para que você possa fazer melhor conteúdo em vez de atuar como gerente de projeto ou tráfego. Ao fornecer uma solução fácil e econômica para gerenciamento de conteúdo, um CMS permite que as empresas gerenciem e distribuam seu conteúdo sem investir em uma equipe de desenvolvimento de conteúdo em tempo integral.

Um CMS (Content Management System) é um software projetado para criar, gerenciar, editar, versionar e publicar conteúdo, normalmente por meio de operações de CRUD, oferecendo interfaces distintas por perfil de usuário, como Admin, Editor e Usuário final. Ele abstrai a complexidade técnica para que o conteúdo possa ser mantido sem necessidade de intervenção direta no código.

Ele pode se comportar como um SaaS, mas não é inerentemente um SaaS. Aqui está a distinção correta:

- CMS é a categoria / tipo de sistema
- SaaS é o modelo de entrega

Ou seja:

- WordPress → CMS (pode ser self-hosted ou SaaS via WordPress.com)

- Shopify → CMS + e-commerce entregue como SaaS

- <a href="https://strapi.io/">Strapi</a>, Ghost, Drupal → CMS (normalmente self-hosted)

- Contentful, Sanity → Headless CMS entregues como SaaS

O **Strapi** está ganhando destaque como uma solução headless CMS open-source que permite aos desenvolvedores criar APIs personalizadas de forma rápida e eficiente. Com um back-end altamente flexível, o Strapi facilita a criação de APIs para diversos tipos de aplicações, desde sites a aplicativos móveis.

O Strapi é um CMS headless de código aberto, construído em Node.js, que permite aos desenvolvedores criar, gerenciar e distribuir conteúdo via APIs RESTful ou GraphQL. A principal vantagem do Strapi é sua interface amigável e a capacidade de personalizar o back-end sem a necessidade de codificação complexa. Com ele, você pode estruturar e gerenciar dados facilmente, criar permissões e autenticações, e conectar o front-end de sua escolha para consumir esses dados.

O Strapi é uma excelente opção para desenvolvedores que buscam flexibilidade e poder em um CMS headless open-source. Com ele, você pode criar APIs personalizadas para atender exatamente às necessidades do seu projeto, integrando facilmente com qualquer front-end. Se você precisa de uma solução para gerenciar conteúdo e APIs de forma eficiente, o Strapi é definitivamente uma opção a ser considerada.

- Modelo de Dados Flexível: O Strapi permite criar modelos de dados personalizados com facilidade, utilizando uma interface visual para definir campos e relações entre diferentes tipos de conteúdo. Isso facilita a criação de APIs que atendam exatamente às necessidades do seu projeto.
- APIs RESTful e GraphQL: O Strapi suporta tanto APIs RESTful quanto GraphQL, oferecendo flexibilidade na maneira como os dados são consultados e manipulados. Isso é ideal para desenvolvedores que desejam otimizar a forma de consumir dados em diferentes tipos de aplicações.
- Autenticação e Permissões: Com o Strapi, você pode configurar facilmente regras de autenticação e permissões para diferentes tipos de usuários. Isso é especialmente útil para projetos que exigem controle de acesso robusto, como plataformas de aprendizado e e-commerce.
- Integração com Bancos de Dados: O Strapi suporta uma ampla variedade de bancos de dados, como MongoDB, MySQL, PostgreSQL e SQLite, permitindo que você escolha a melhor solução de armazenamento para o seu projeto.
- Personalização de API: Uma das principais vantagens do Strapi é a capacidade de personalizar suas APIs sem esforço, usando extensões e plugins. Isso permite adicionar funcionalidades específicas ao seu CMS, como lógica de negócios personalizada e integração com serviços de terceiros.

Sim, no núcleo técnico, um CMS gira em torno de:

- Create (criar conteúdo)
- Read (exibir conteúdo)
- Update (editar conteúdo)
- Delete (remover conteúdo)

Mas o diferencial de um CMS não é só o CRUD. É o que vem em cima dele:

- versionamento de conteúdo
- permissões e papéis
- workflows (rascunho, revisão, publicação)
- mídia (upload, compressão, CDN)
- SEO, cache, preview
- internacionalização
- templates ou APIs de entrega

Blogs e e-commerce - Você acertou no exemplo, com uma nuance:

- Blogs → CMS clássico (posts, páginas, categorias)
- E-commerce → CMS + domínio de negócio (produtos, pedidos, pagamento)

Um CMS pode ter DDD, independentemente de ser SaaS ou self-hosted. O fato de ele ser entregue como SaaS não impede em nada o uso de DDD. Pelo contrário: multi-tenant, permissões, workflows e regras de publicação são terrenos férteis para DDD. E não só pode como, em CMS mais sérios, é até recomendável.

DDD não entra para “fazer CRUD bonito”, entra quando há regras, estados e linguagem de negócio. Em um CMS real, isso existe de sobra:

- Conteúdo não é só um registro: ele tem estado (rascunho, revisado, publicado, arquivado)
- Usuários não são iguais: papéis, permissões, escopos
- Publicação tem regra: data, aprovação, visibilidade
- Multitenancy: organização, projeto, espaço, site
- Versionamento e histórico
- SEO, internacionalização, preview, cache

Tudo isso é domínio, não infra. Por isso plataformas como Magento, WooCommerce e Shopify são consideradas CMS especializados, não só lojas.

Interfaces (Admin x Usuário):

- Admin / Editor → interface de gestão (backoffice)
- Usuário final → interface de consumo (site, app, frontend)

Nos Headless CMS, essa separação é ainda mais clara:

- Admin → painel
- Usuário → consome via API (REST/GraphQL)

Tipos de sistemas de gerenciamento de conteúdo (CMS): Praticamente qualquer CMS possuem duas partes, e front e o back-end. O front-end é a interface com a qual o usuário interage. É a a estrutura e estilo visual do site. O front-end reúne HTML, CSS e JavaScript para entregar conteúdo rico e interativo, que possuam o estilo do branding da sua marca.

O back-end de um CMS é a aplicação utilizada para publicar novos conteúdos em um website. O processo começa com o acesso à interface web para adicionar, criar e públicar conteúdo no front-end do seu CMS. Em vez de programar em HTML, CSS e JavaScript, você cria o seu conteúdo com uma interface similar a do Microsoft Word. Em seguida, o back-end armazena o conteúdo em um banco de dados e o publica no front-end do website.

Juntos, esses dois sistemas compõem o CMS. Eles permitem que você publique conteúdo sem a necessidade de conhecer as tecnologias web por trás ou desenvolver uma aplicação web do zero.

Veja a seguir alguns tipos de sistemas de gerenciamento de conteúdo disponíveis atualmente:

1. **CMS Acoplado**: Um CMS acoplado geralmente remete a um CMS tradicional. Esse sistema oferece um back-end totalmente acessível que se conecta e modifica o banco de dados do site, além de publicar os conteúdos em um front-end com os estilos definidos. Embora um CMS acoplado seja uma solução completa, a principal distinção entre ele e um CMS de software como serviço (SaaS) é que um CMS acoplado requer hospedagem na web dedicada para execução. Embora a hospedagem web seja relativamente barata, é importante lembrar que um CMS requer a instalação e manutenção de tecnologias específicas para tornar o software funcional. De forma complementar, é provável que um CMS acoplado exija que um administrador o configure e ajuste as configurações de instalação para uso contínuo. O WordPress é um exemplo de um CMS acoplado, pois ele oferece um pacote completo para que os usuários instalem, publiquem um site e publiquem o conteúdo avançando.

2. **CMS SaaS**: Um CMS SaaS também é uma solução completa, de ponta a ponta. Porém, o CMS SaaS é armazenado na nuvem, em comparação com um CMS acoplado. Isso significa que esse CMS não requer instalação, configuração com hospedagem web pré-configurada. Um CMS baseado no SaaS é uma excelente solução para empresas que precisam de uma presença web direta, pois oferece todos os recursos sem nenhum servidor ou sobrecarga de hospedagem web. Essa plataforma permite que todos os usuários acessem rapidamente os websites, gerenciem o conteúdo e distribuam os conteúdos entre os canais digitais.

3. **CMS Desacoplado**: Em um CMS desacoplado, a apresentação do website é "desacoplada" do back-end. O sistema de entrega se encaixa entre a apresentação do website e acessa o back-end por meio de uma interface de programação de aplicações (API). Um CMS desacoplado é uma solução avançada que oferece mais flexibilidade para interagir com o conteúdo criado no back-end. Por exemplo, imagine que a sua empresa quer usar a biblioteca de conteúdo para outro fim, por exemplo, com aplicações móveis. Nesse caso, um CMS desacoplado é uma solução atraente porque ele suporta vários aplicativos adaptáveis no front-end, mantendo ao mesmo tempo seu conteúdo e informações consistentes no back-end.

4. **CMS headless**: Um headless CMS só possui um sistema back-end que acessa o banco de dados e armazena o conteúdo com uma aplicação front-end integrada personalizada. Esse sistema oferece mais flexibilidade do que um CMS desacoplado, porém o nível de trabalho é maior do que com qual''quer outro tipo. Um headless CMS também exigem que o desenvolvedor defina, crie e conecte uma aplicação front-end. Um headless CMS é uma boa solução para organizações que precisem de maior controle e flexibilidade sobre o acesso aos conteúdos. Ele fornece armazenamento de conteúdo e recursos organizacionais ao mesmo tempo que permite um aplicativo personalizado no front-end, seja um site, um aplicativo móvel ou algum outro front-end.

Recursos principais que um sistema de gerenciamento de conteúdo deve ter
Descobrir quais recursos de um CMS são essenciais para o seu negócio pode começar com os recursos que sejam essenciais para um sistema de gerenciamento de conteúdo. Em seu processo de tomada de decisão, pode ser útil fazer comparações lado a lado de soluções e produtos de gerenciamento de conteúdos. Porém, a pergunta que ainda fica é: "Quais são os recursos principais de qualquer solução?"

Funções de usuário e gerenciamento de conteúdo baseado em funções
Existem diversas funções dentro de um Sistema de Gerenciamento de Conteúdo. Compreender como eles funcionam juntos é importante para dar acesso aos seus usuários e eles possam realizar suas tarefas e acessar o conteúdo digital apropriado para a função de cada um. Essas funções variam desde funções típicas da organização até funções de gerenciamento de aplicativos, comparação de tarefas e recursos por funções de aplicativo, até funções de tipo de recurso (permissões), decidindo o que os usuários podem ver e fazer com o conteúdo, incluindo documentos, sites ou modelos.

Gerenciamento de ativos digitais
Sistemas de Gerenciamento de Conteúdo como o da Oracle oferecem recursos avançados para gerenciar todos os seus ativos digitais para uso em diferentes canais de marketing, incluindo sites, materiais de marketing, campanhas por e-mail, lojas on-line, pesquisa paga e blogs. Ele fornece um hub de conteúdo centralizado para todos os seus ativos, onde você pode organizá-los em repositórios e coleções e criar regras e fluxos de trabalho para definir como o conteúdo pode ser usado e onde.

Criar tipos de ativos para definir quais informações você precisa coletar quando os usuários criam ativos que sejam essenciais para qualquer CMS. Os tipos de ativos digitais definem os atributos personalizados necessários para os seus ativos digitais (arquivos, imagens e vídeos).

Gerenciamento de conteúdo na nuvem
Como acontece conosco, é fundamental migrar o seu gerenciamento de conteúdo para a nuvem, centralizando seu conteúdo em um só lugar e tornando-o acessível de qualquer lugar.

Você pode agrupar seus arquivos em pastas para executar operações diárias de gerenciamento de arquivos, incluindo copiar, mover, excluir, da mesma forma que em seu sistema local e arquitetura. Como todos os arquivos estão armazenados na nuvem, os usuários conseguem acessá-los de qualquer lugar, incluindo dispositivos móveis.

Conteúdo colaborativo
Com todo o conteúdo de suas organizações na nuvem, será fácil compartilhar ativos ou pastas para colaborar com outras pessoas dentro e fora de sua organização. Todos com os quais você compartilha conteúdo terão acesso às informações mais recentes, onde quer que estejam, sempre que precisarem. Compartilhar e registrar a coloração do conteúdo permitirá que o usuário monitore como e quando cada item compartilhado foi acessado.

Crie websites
Com as soluções de gerenciamento de conteúdo, você pode criar e publicar rapidamente sites de marketing, ajuda e comunidade - do conceito ao lançamento - com experiências on-line envolventes. O processo de criação de sites é totalmente integrado entre conteúdo, colaboração e design por meio de um único ambiente de criação e publicação.

Soluções baseadas em nuvem facilitam o início rápido, usando modelos prontos, componentes de arrastar e soltar (WYSIWYG), layouts de página de amostra e temas de site para montar um site a partir de blocos de construção predefinidos. Ou os seus desenvolvedores podem criar designs personalizados, incluindo modelos, temas ou componentes para criar experiências online únicas.

O que levar em consideração antes de avaliar se um CMS é o ideal para você
Um CMS deixa o conteúdo mais fácil de ser encontrado, trabalhando com seis áreas do gerenciamento de conteúdo.

Governança de conteúdo
Existem políticas e procedimentos implementados para criar e gerenciar conteúdo?

Arquitetura da informação para utilização de tags
O conteúdo atual e novo é categorizado e marcado de forma que todos entendam?

Processos comerciais para o gerenciamento de conteúdo
Será que os fluxos de trabalho estão padronizados e automatizados?

Experiência do usuário aplicado a conteúdo
Como as suas principais partes interessadas, de colaboradores à clientes, conseguem encontrar todas as informações de que eles precisam?

Tecnologia e aplicações
Será que a tecnologia atual está sendo utilizada da forma adequada? Quais melhorias ou novos sistemas otimizariam o fluxo de informações com a segurança ideal? Você tem um sistema de gerenciamento de conteúdo baseado na nuvem?

Valor comercial de um CMS
Será que os seus dados e conteúdos podem ser gerenciados de forma segura e eficiente? Você está cumprindo todas as diretrizes regulatórias aplicáveis?

Como utilizar um sistema de gerenciamento de conteúdo para o seu website
Um sistema de gerenciamento de conteúdo ajuda na criação, gerenciamento e publicação de conteúdo na internet. Esse sistema também ajuda a manter os conteúdos organizados e acessíveis para que possam ser reutilizados e ressignificados de forme efetiva. Existem vários tipos de sistemas de gerenciamento de conteúdo disponíveis, desde baseados em nuvem até um CMS sem interface do usuário, para atender a todas as necessidades do público.

Ele não apenas oferece uma maneira de armazenar e gerenciar todas as suas informações em um único banco de dados facilmente acessível, como também faz o seguinte:

Apoia a colaboração interna e entre equipes
Oferece uma forma fácil e acessível para atualização do conteúdo
Aumenta a visibilidade do conteúdo
Aumenta a produtividade
Reduz os custos
Permite que você mantenha a consistência do conteúdo
Permite escalonar os serviços, à medida que você cresce

Como um CMS pode ajudar a desenvolver um website?
Em suma, uma plataforma CMS cuida de todos os detalhes técnicos do desenvolvimento e gerenciamento de website. Por exemplo, para publicar conteúdo na internet, como um post de blog, o primeiro passo é criar um arquivo HTML. O HTML permite estruturar o conteúdo escrito para que o navegador consiga interpretar o conteúdo. Esse arquivo permite a inserção de imagens e vídeos no conteúdo, além de criar links para outros documentos HTML.

Assim que o conteúdo é criado e estruturado, você consegue alterar o visual por meio de folhas de estilo (CSS). O CSS ajuda a mudar a fonte, cor e tamanho de qualquer elemento na página, para que fiquem no estilo que você desejar. Depois de concluído, você faz upload dos arquivos HTML e CSS - juntamente com quaisquer arquivos de imagem e vídeo - para um servidor Web onde seu novo site está acessível a qualquer pessoa.

Embora esse processo possa parecer até que simples, não é um método muito eficiente para criar documentos e compartilhá-los online. E muitas empresas não possuem os recursos de TI para se dedicar a essa tarefa.

Tecnologia CMS avançada
HTML e CSS são viáveis para criar documentos simples e legíveis, mas são limitados ao criar sites que oferecem amplas capacidades e funcionalidades - o tipo de site moderno que a maioria das empresas precisa para ter sucesso.

Por exemplo, para adicionar recursos interativos e funcionalidades que saltem aos olhos em seu site, você precisa usar o JavaScript - a linguagem de programação da web. HTML e CSS são linguagens de programação relativamente simples, utilizadas para criar documentos. O JavaScript é uma linguagem de programação dedicada, não muito diferente das linguagens utilizadas para desenvolver aplicações móveis ou para computadores.

Suponha que você planeja migrar seu site de blog para um site de mídia que serve milhares de visitantes por mês e publica vários conteúdos por dia. Neste caso, você precisa começar a incorporar tecnologias de banco de dados para armazenar e acessar todos os seus conteúdos. Você precisa de um banco de dados com sua própria linguagem de programação e outra linguagem de programação para passar informações entre o banco de dados e a parte do site que os usuários acessam e navegam. Assim, o cenário se complica rapidamente. E é nesse ponto que o CMS entra em ação.

Benefícios de um sistema de gerenciamento de conteúdo (CMS)
Explore o Oracle Content Management
Explore o Oracle Content Management
A demanda por experiências de marketing digital está crescendo e não mostra sinais de desgaste. Os consumidores atuais estão exigindo soluções omnichannel e experiências sem atrito. A tecnologia digital oferece infinitas maneiras de as empresas se envolverem com seus clientes e se manterem conectadas. Então não é de se admirar que o mercado CMS cresça de US$36 bilhões em 2018 para US$123,5 bilhões até 2026.

As empresas reconhecem como um sistema de gerenciamento de conteúdo pode ajudá-las a aproveitar todo o valor de seu conteúdo, simplificando o processo de criação e distribuição de conteúdo. As equipes de marketing e vendas que investem em um CMS podem esperar obter quatro benefícios principais à medida que trabalham para levar sua mensagem ao mercado e melhorar as experiências do cliente.

1. Melhor colaboração e organização: Com um CMS, vários membros da equipe de marketing de conteúdo podem contribuir e ajudar a publicar conteúdo. Com seus recursos de gerenciamento de fluxo de trabalho, armazenamento de conteúdo e programação, o sistema ajuda a manter tudo organizado. Os CMSs baseados em navegador podem ser acessados de qualquer lugar, e os colegas de equipe de diferentes locais podem colaborar em projetos de conteúdo no mesmo sistema e na versão mais atual. Os membros da equipe podem acessar o mesmo conteúdo, por estar armazenado no mesmo local. Não há necessidade de enviar vários arquivos para várias pessoas - e nenhuma dor de cabeça de tentar controlar várias versões.

2. Amigável e intuitivo: Com o CMS, os usuários não precisam saber programar em HTML e CSS. Não importa o nível de habilidade, os usuários por toda empresa podem acessar e publicar os conteúdos. Um sistema de gerenciamento de conteúdo facilita o upload de conteúdo nas páginas web e atualizá-los por meio do editor de conteúdo.

3. SEO e plug-ins e ferramentas de otimização de conteúdo: Você quer melhorar o seu SEO (Otimização de Mecanismo de Busca) e aumentar o tráfego do seu site? Com o CMS, pode pode usar plug-ins e ferramentas para aumentar a sua classificação nas buscas. Essas ferramentas podem incluir recursos para a interface front-end para incluir títulos, tags "meta" e tags "alt" às páginas web.

4. Mais tempo para focar nos conteúdos: Com um sistema CMS direto, melhor organização e ferramentas e plug-ins para ajudar a SEO, os criadores de conteúdo podem se concentrar na produção de conteúdo de qualidade. As equipes de marketing não precisam trabalhar com diversos arquivos e códigos. É possível compartilhar as criações com os demais colaboradores que estejam há quilômetros de distância. Eles têm tempo para refinar a cópia, ajustar imagens, produzir mais conteúdo de vídeo ou conduzir testes A/B em diferentes linhas de assunto, ofertas, CTAs e formatos.
