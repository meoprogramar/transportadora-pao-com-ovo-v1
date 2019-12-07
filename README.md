# transportadora-pao-com-ovo-v1

### Descrição

Projeto da disciplina de Programação WEB, professor Thiago Santana Batista.

> A Transportadora Pão com Ovo é uma transportadora. Atualmente o sistema deles é todo
manual, com vários documentos em papel, problemas de comunicação, de controle, e de
rastreamento. A Empresa consiste de uma matriz, localizada em Campina Grande, e várias franquias
espalhadas pelo Brasil. Cada franquia pode enviar ou receber encomendas...
[descrição completa do problema](https://drive.google.com/open?id=1C1_RQ51ShHxFOBzmYPI9y6zrxi2ckwfx).

Tecnologias utilizadas:
- Spring Boot 2
- Thymeleaf
- JPA
- Hibernate
- MySQL

Ferramentas utilizadas:
- Eclipse Java EE IDE for Web Developers
- MySQL Workbench
- Visual Studio Code

Para o gerênciamento usou-se metodologia de desenvolvimento ágil, scrum com 4 sprints ao longo do período.

### Documentação passo-a-passo

1. Clone o projeto na sua máquina.

2. No Eclipse vá em "File -> Import -> Maven -> Existing maven projects", aperte em "Next" e selecione onde está a pasta que foi extraida no passo anterior.

3. Na parte de "Project" selecione o arquivo "pom.xml...:jar" aperte em "Finish" e aguarde a IDE importar as dependências.

4. Com o projeto importado vá em "src/main/resources -> application.properties", altere seu nome de usuário e senha para que o projeto possa instanciar o banco de dados.
```
spring.datasource.username=<usuário do mysql>
spring.datasource.password=<senha do mysql>
```
5. Salve as mudanças, em seguida vá em "src/main/java -> uepb.transportadora -> TransportadoraApplication.java" clique com botão direito e siga "Run As" -> "Java Application", agora aguarde todo o processo de build do sistema.

6. Após todo log do sistema ser mostrado, abre seu navegador em [http://localhost:8080/](http://localhost:8080/).
```
usuário: admin
senha: admin
```

7. (Opcional) O projeto utiliza APIs do google para algumas funções, caso tenha uma API_KEY siga os próximos passos.

> Observação 1: Caso não utilize a API Key as funções de mostrar o mapa e plotar pontos podem não funcionar. 
Observação 2: Caso sua conta do google não tenha cadastrado informações de pagamento a função de getLocation() pode não funcionar.

8. (Opcional) Com a API Key no ctrl+c altere o link de requisição nos arquivos 'franquiasListar.html' e 'FranquiaService.java' no método getLocation(), com isso o projeto estará pronto.
```
<script async defer
  src="https://maps.googleapis.com/maps/api/js?key=SUA_API_KEY_AQUI&callback=initMap">
</script>
```

### Demostração

![Captura de tela de 2019-12-06 23-32-37](https://user-images.githubusercontent.com/34866806/70367790-56d77f80-1882-11ea-9774-19685a6b030f.png)
![Captura de tela de 2019-12-06 23-39-23](https://user-images.githubusercontent.com/34866806/70367791-56d77f80-1882-11ea-8b36-6294dfd78bab.png)
![Captura de tela de 2019-12-06 23-40-30](https://user-images.githubusercontent.com/34866806/70367792-56d77f80-1882-11ea-98b4-6df6ee70ee58.png)

### [Versão 2 utilizando Spring Boot como API + Node.js + Vue.js](https://github.com/meoprogramar/transportadora-pao-com-ovo-v2)
