const express = require('express');
const shell = require('shelljs');
const util = require('util');
const bodyParser = require('body-parser');

const app = express();

app.use(bodyParser.text());
app.get('/', (req, res) => {
  console.log('Received a request.');

  const target = process.env.TARGET || 'World';
  res.send(`Hello ${target}!`);
});

app.post('/exec', (req, res) => {
    const shellResp = shell.exec(req.body, {
        "timeout": 10*1000
    });
    res.send(shellResp.stdout);
});

const port = process.env.PORT || 8080;
app.listen(port, () => {
  console.log(`Listening on port ${port}`);
});