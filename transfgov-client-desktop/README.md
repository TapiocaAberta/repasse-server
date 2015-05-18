Cliente TransfGov Desktop
--

Uma aplicação cliente desktop para o projeto TransfGov.


Requerimentos:

* Java 8.x
* Maven 3.x
* Wildfly (se for rodar o servidor localmente)

Para executar o projeto vá em transfgov-server e execute:

~~~
$ mvn clean install
~~~

Adicionalmente na aplicação servidor, execute o Wildfly:

~~~
$ cd ${WILDFLY_HOME}
$ ./bin/standalone.sh
~~~

E faça o deploy da aplicação no Wildfly (se atente para a configuração de banco de dados):

~~~
$ mvn widlfly:deploy
~~~

A aplicação deverá estar disponível em `http://localhos:8080/transfgov`. Acesse essa URL usando um navegador.

Por fim execute o cliente usando:

~~~
$ mvn exec:java -Dexec.mainClass="org.jugvale.transfgov.cliente.Principal"
~~~


Caso não vá rodar o servidor localmente, use nosso servidor no openshift:

TODO
