Repasse Server
--

O projeto Repasse lado servidor (core)


Requerimentos:

* Java 8.x
* Maven 3.x
* Wildfly 8.2


## Configurando

A única configuração do projeto local é com relação ao banco de dados. Instruções:

* Você vai encontrar dois arquivo de "datasource" na pasta `transfgov-server/src/main/webapp/WEB-INF`: `transf-gov-ds.mysql` e `transf-gov-ds.h2`;
* Renomeie o arquivo .h2 para usar o banco de dados de testes locais ou o mysql. Se optar pelo MySQL, você terá que instalar um módulo do MySql no Wildfly. Para testes recomendo renomerar o .h2 para .xml, por exemplo:  
~~~
$ mv transf-gov-ds.h2 transf-gov-ds.xml
~~~

## Executando localmente

Agora você já pode executar localmente. Tenha em mente o uso de Maven e Wildfly (um guia para começar pode ser encontrado [aqui](http://aprendendo-javaee.blogspot.com.br/2014/01/ola-mundo-java-web-com-maven-e-wildfly.html). Após instalado Java 8, Maven e Wildfly 8, vamos rodar o servidor:

~~~
$ cd ${WILDFLY_HOME}
$ ./bin/standalone.sh
~~~
*(para apertar aperte ctrl+c no console)*

Para executar o projeto acesse o diretório repasse-server e execute:

~~~
$ mvn clean install wildfly:deploy
~~~

Você poderá acompanhar nos logs do Widfly a instalação da aplicação.  A aplicação deverá estar disponível em `http://localhos:8080/`. Acesse essa URL usando um navegador. Aproveite para conhecer mais a aplicação no lado servidor (TODO: DEVERÁ EXISTIR UMA PÁGINA EXPLICANDO A API REST E ATÉ UM API EXPLORER)

## Como importar no eclipse?

Você deve importar o projeto utilizando `Existing Maven projects`. Não comitamos o `.project` para evitar problemas de variáveis locais.

## Preenchendo o projeto com dados

O projeto é capaz de pegar os dados no portal da transparência automaticamente fazendo o download, descompactando e preenchendo o banco de dados. Para isso acesse  `http://localhost:8080/rest/carga/{ANO}/{MES}` usando POST com um ano e mês válido, por exemplo:

~~~
$ curl -X POST -u '{usuario}:{senha}' http://localhost:8080/rest/carga/transferencia/2015/1/
~~~
* veja abaixo sobre como criar o usuário e senha*  

A carga deve demorar aproximadamente 40 minutos! Notem que para cada mês há aproximadamente 150 mil registros! No entanto, no meio da carga você pode acessar os dados já disponíveis, por exemplo, a URL para acessar os estados disponíveis seria:

~~~
$ curl http://localhost:8080/rest/estado
~~~

Você pode também acessar a página de administração em `http://localhost:8080/admin.html`.

## Usuário e senha

O sistema exige um usuário para realizar a carga e acessar a página de administraço (admin.html). Para criar esse usuário utilize o script `{WILDFLY_HOME}/bin/add-user.sh` para o Linux. Para windows utilize o .bat. Adicione o usuário utilizando a opção *Application User* e não se esqueça de dar o role *admin* para ele. PAra mais informaçes sobre o usuário veja a [documentação do Wildfly](https://docs.jboss.org/author/display/WFLY8/add-user+utility).
