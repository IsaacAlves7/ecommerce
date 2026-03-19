const express = require('express');
const MercadoPago = require('mercadopago');
const app = express();

MercadoPago.configure({
    sandbox: true,
    access_token: "test_1234567890abcdefg" // Substitua pelo seu token de acesso
});

app.get('/', (req, res) => {
  res.send("ID do produto: " + Date.now() + "<br>Para pagar, acesse <a href='/pay'>/pay</a>");
}); 

app.get('/pay', (req, res) => {
  var desc;
  
  // Pode fazer uma busca no banco de dados para pegar a descrição do produto
  var dados = {
    items: [
        item = {
            // UUID && DATA (h/m/s)
            id: "" + Date.now(), // String: ID do produto (diferente para cada venda do produto)
            title: "Teste de produto",
            description: desc, // Descrição do produto
            picture_url: "https://picsum.photos/200/300",
            unit_price: parseFloat(100),
            currency_id: "BRL",
            quantity: 1
        }
    ],
    payer: {
        name: "Nome do comprador",
        surname: "Sobrenome do comprador",
        email: ""
    }
  };    
});  

app.listen(8080, () => {
  console.log('Server is running on http://localhost:3000');
});