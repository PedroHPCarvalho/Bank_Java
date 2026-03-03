package com.bankjava.account.Infrastructure;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
     // Atributos para conexão com o banco de dados H2, são padrão para o H2, mas podem ser alterados caso seja necessário
     private String user = "sa";
     private String password = "";
     private String URL_BD = "jdbc:h2:mem:bankjava;DB_CLOSE_DELAY=-1";
     // Atributo quer armazena a instancia da classe, para implementação do Singleton, onde uma classe tem apenas uma instancia que é reutilizada,
     // Evitando a criação de varias instancias da classe, o que pode ser custoso em termos de recursos, e garantindo que a conexão com o banco de dados seja utilzada sempre
     // a mesma instancia, evitando problemas de concorrência e garantindo a integridade dos dados.
     // é um metodo estatico, ou seja, pode ser chamado sem a necessidade de criar um objeto da classe, e retorna um objeto da classe, para disponibiliza a instancia
     private static ConnectionFactory instance;

        // O construtor é privado para proteger a criação de objetos, para serem gerados sempre objetos validos, e
        // garantir que a classe seja utilizada apenas através do metodo getInstance, garantindo a implementação do Singleton
     private ConnectionFactory(){}

    // Metodo para disponibilizar a instancia da classe, onde é verificado se a instancia já existe,
    // caso exista ela é retornada, caso não exista ela é criada e retornada, garantindo a implementação do Singleton
     public static ConnectionFactory getInstance(){
         if (instance == null){
             instance = new ConnectionFactory();
         }
         return instance;
     }

        // Metodo para criar a conexão com o banco de dados, onde é utilizado
        // o DriverManager para criar a conexão, e caso ocorra algum erro na criação da conexão, uma RuntimeException é lançada
     public Connection createConnection(){
         try{
             return DriverManager.getConnection(URL_BD,user,password);
         } catch (SQLException e) {
             throw new RuntimeException(e);
         }
     }
}
