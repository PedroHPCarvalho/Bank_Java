package com.bankjava.account.Infrastructure;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    private Connection connection;

    // Construtor privado para evitar a criação direta de instâncias da classe
    private DatabaseInitializer(Connection connection){
        this.connection = connection;
    }

    // Factory Method para criar uma instância do DatabaseInitializer, garantindo que a conexão seja fornecida e evitando a criação de objetos inválidos
    public static DatabaseInitializer createInitilizer(Connection connection){
        return new DatabaseInitializer(connection);
    }

    // Metodo para inicializar o banco de dados H2, onde é lido o arquivo schema.sql e executado as
    // instruções SQL contidas nele para criar as tabelas e estruturas necessárias no banco de dados
    public void initilizerH2(){
        InputStream inputStreamSqlSchema = getClass().getClassLoader().getResourceAsStream("schema.sql");
        String sqlInstruction;
        try {
            sqlInstruction = new String(inputStreamSqlSchema.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            Statement statement = connection.createStatement();
            statement.execute(sqlInstruction);
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
