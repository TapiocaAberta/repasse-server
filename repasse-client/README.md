Client for Repasse project
--

Cliente para o projeto Repasse.

Utiliza `gulp`, `bower` e outras coisas.

### Construir

- Instale nodejs: `apt install nodejs`
- No diretorio `repasse-client` rode: `npm install` (como root)
- Instale o cliente gulp: `npm install --global gulp-cli`
- Instale bower: `sudo npm install --global bower`
- Instale as dependencias do bower: `bower install`
- Configure `gulfile.js` e modifique a variavel `DEPLOY_DIR` para apontar para o `welcome-content` do seu Wildfly. Outra possibilidade é construir, copiar o conteúdo do diretório `dist` para dentro do `repasse-server/src/main/webcontent`
- Rode `gulp`
