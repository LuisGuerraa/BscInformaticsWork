# Estrutura do Software

## Fase 1

* Inicialmente foram definidos os `path templates` necessários à conceção da aplicação na classe Configuration(método build). Para guardar os templates dos comandos foi necessário criar a classe `Router`. Nesta classe foi definida uma estrutura em `ArrayList` com o tamanho dos métodos possiveis para esta aplicação, que neste caso serão `GET` e `POST`. Dentro dessa estrutura está definido um `TreeMap` que irá conter como chave uma String referente à primeira palavra do `path template` inserido e como valor um outro `TreeMap` que por sua vez terá como chave a String correspondente ao `path template` e como valor um CommandHandler. Exemplo: O path template `/courses/{acr}/classes`, juntamente com o CommandHandler associado, estará presente numa Tree onde a chave é `/courses`.

* CommandHandler é uma classe Abstrata. Esta classe tem como método abstrato `execute()`. Cada path template vai ter associado um comando, e este será uma classe que irá estender de CommandHandler tendo assim a obrigação de dar uma implementação ao método `execute()`.

* Para verificar se os parâmetros inseridos pelo utilizador são compatíveis com os templates criou-se um método `match()` na classe `Router`. Este irá comparar o path recebido com os templates existentes, retornando NULL caso não encontre uma correspondência entre path e template.

* Decidiu-se criar um package `Model` com todos os tipos de Entidades, sendo desta maneira possível retornar um tipo específico de entidade quando fosse executado o método `execute()` para que a informação pretendida pelo utilizador fosse visualizada na linha de comandos.

* Na estrutura temos um package `Commands`, que armazena as classes para cada comando possível (e.g `GetClassesByCourseOfSemesterAndNum`). Essas classes têm um método `execute()` que retorna uma lista `List<Entity>` e que realiza as instruções necessárias para a comunicação com a base de dados, onde o gerenciamento dos recursos de JDBC (connections) é feito. A conexão é aberta e fechada garantindo assim a libertação dos recursos após a realização de um comando.

* Relativamente ao gerenciamento de erros e a sua comunicação ao utilizador por parte da aplicação `App`, se o utilizador não inserir dois ou três argumentos apresenta um erro de modo a informar que este inseriu argumentos inválidos. No que concerne a outro tipo de erros como a inexistência de conexão com a base de dados e erros semânticos na escrita do path, etc... são realizados no método executeCommand()`.

* Foram também adicionadas duas classes de testes para que se verifique se a informação nos comandos inseridos corresponde ao pretendido. Para isso, em cada uma dessas classes, existe um método que testa o comando correspondente, que contém uma lista `List<Entity>` indicando a informação que se pretende nessa lista e comparando com a lista proveniente do comando correspondente.

* De referir que foi criado um ficheiro `Auhentication.txt` que contém os dados pessoais de acesso à base de dados apoiada pela classe `Authentication`. No momento de criação de uma conexão(`Configuration.build()`) essa classe irá ler o ficheiro para assim poder realizar o acesso, ficando com uma conexão por aplicação onde cada comando utiliza essa mesma conexão para abri-la e fechá-la quando se pretenda realizar um novo comando.


## Fase 2

* Para esta segunda fase do projeto começámos por implementar a classe `Header`, responsável por guardar o `PrintStream` e o tipo de visualização desejada(html ou plain).
Caso no comando o utilizador não introduza opções para Headers, ou caso os mesmo sejam inválidos, é criado à mesma um Header cujo PrintStream é o `System.out` e a sua visualização é em texto(plain).`
Assim criou-se a classe `Request` que para além de armazenar o Header vai também guardar todo o tipo de informação que é introduzida pelo utilizador, `Method`, `Path`, `Parameter`).

* Também no início desta segunda fase foi implementado o método handle na classe abstracta `CommandHandler` que seria responsável por abrir e fechar a ligação à base de dados e chamar o respetivo comando `execute`.

* O método `execute` é abstrato em `CommandHandler` e por sua vez cada comando criado como estende da classe abstrata vai ter de dar uma implementação ao mesmo método.
Para além deste método, cada comando tem a obrigação também de dar uma implementação aos métodos `getArguments` e `getDescription`, o primeiro retorna um array de Strings correspondente aos argumentos necessários para a execução do comando, o segundo retorna uma String correspondente à descrição do comando(o qual é chamado pelo comando OPTION  /`).

* Como já foi mencionado, cada comando dá uma implementação ao método `execute`, e este por sua vez retorna uma instância de `Result`.
`Result` é uma classe abstracta, e a mesma tem como filhos `ComposedResult`, `SingleResult` e `NoResult`.
Cada vez que o utilizador inserir um comando, e o mesmo for válido, uma destas 3 implementações de `Result` vai ser criada(consoante o tipo de comando: get,post,..).

* Após a criação de um Result a App faz a verificação do Header recebido, e consoante o tipo de visualização pretendida é chamado o `printPlain`(método que dá print do resultado em texto) ou o `printHtml`(método que dá print do resultado em html).
Estes 2 métodos provêm de `IPrintData`, interface que a classe `Result` implementa.

* De forma a criar uma visualização em formato html foi criada a classe `Node`(abstracta) que tem como filhos `Element` e `Text`.
Um `Element` representa um elemento em formato html(ex: head, body, table, title,..), já `Text` representa o valor de um `Element`. Element pode ter Elements e Texts como seus filhos o mesmo não aconteçe com Text.
Assim, cada vez que é necessário visualizar um Result em html, é criada uma árvore de `Node`s, em que cada Result impõe a sua forma de a popular, e de seguida o método `printTo(PrintStream)` mostra no PrintStream recebido como parâmetro o resultado em html da árvore criada.

### Novos comandos 

* Foram definidas as classes `GetTime`, `GetCommands` e `Put` da respetica entidade que indicam os novos métodos possíveis nesta fase: mostrar o tempo atual, mostrar as opções com uma lista de comandos possíveis e atualizar uma entidade existente.

* Para serem definidos os novos comandos, foi criada a entidade `Group` no modelo para ser possível obter ou criar informações acerca dos grupos que uma turma possa ter e os respetivos elementos.

* A organização dos comandos foi melhorada com a criação de novos packages que contenham comandos respetivos a uma entidade.

* Nesta fase foi criada a possibilidade de um parâmetro poder ter vários valores, para isso foi alterada a lista de parâmetros da sua respetiva classe, onde esta continha um valor para uma chave e passou a ter um conjunto de valores para uma chave, ou seja, foi criada uma lista como valor de uma chave da estrutura `HashMap`. Desta forma, os comandos que tenham a possibilidade de ter vários valores, a sua lista será percorrida e tratada da forma que seja necessária para mostrar dados.