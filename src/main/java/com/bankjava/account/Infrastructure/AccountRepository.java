package com.bankjava.account.Infrastructure;

import com.bankjava.account.domain.Account;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountRepository {

    //CREATE DO CRUD, ELE CRIA UM REGISTRO NA TABELA E RETORNA O OBJETO CRIADO COM O ID GERADO PELO BANCO DE DADOS
    public static Account save(Account account) {
        String sql = "INSERT INTO account " +
                "(account_number," +
                "account_agency," +
                "account_holder," +
                "account_balance," +
                "account_especial_limit," +
                "account_especial_limit_default) " +
                "VALUES (?,?,?,?,?,?)";
        // O try-with-resources é utilizado para garantir que os recursos de conexão e declaração sejam fechados automaticamente após o uso, evitando vazamentos de recursos
        try (Connection connection = ConnectionFactory.getInstance().createConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setInt(1,account.getAccountNumber());
            preparedStatement.setInt(2,account.getAccountAgency());
            preparedStatement.setString(3,account.getAccountHolder());
            preparedStatement.setDouble(4,account.getAccountBalance());
            preparedStatement.setDouble(5,account.getAccountEspecialLimit());
            preparedStatement.setDouble(6,account.getAccountEspecialLimitDefault());
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                account.setId(resultSet.getLong(1));
            }

            return account;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //READ POR ID DO CRUD, ELE BUSCA UM REGISTRO NA TABELA PELO ID E RETORNA O OBJETO CRIADO COM OS DADOS DO REGISTRO, SE O REGISTRO NÃO EXISTIR ELE RETORNA NULL
    public static Account findById(long idSearch){
        String sql = "SELECT * FROM account WHERE id = ?";
        try(Connection connection = ConnectionFactory.getInstance().createConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setLong(1,idSearch);
            ResultSet resultSet = preparedStatement.executeQuery();

            Account accountRecreated;
            if(resultSet.next()){
                accountRecreated = Account.newAccount(
                        resultSet.getInt("account_number"),
                        resultSet.getInt("account_agency"),
                        resultSet.getString("account_holder"),
                        resultSet.getDouble("account_balance"),
                        resultSet.getDouble("account_especial_limit_default")
                );
                accountRecreated.setId(resultSet.getLong("id"));
                accountRecreated.setAccountEspecialLimit(resultSet.getDouble("account_especial_limit"));
                return accountRecreated;
            }

            return null;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //READ ALL DO CRUD, BUSCA TODOS OS REGISTROS DA TABELA E RETORNA UMA LISTA DE OBJETOS
    public static List<Account> findAll(){
        String sql = "SELECT * FROM account";

        try(Connection connection = ConnectionFactory.getInstance().createConnection(); PreparedStatement preparedStatement  = connection.prepareStatement(sql)){
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Account> returnedArrayList = new ArrayList<>();

            while(resultSet.next()){
                Account accountToArrayReturn =  Account.newAccount(
                    resultSet.getInt("account_number"),
                    resultSet.getInt("account_agency"),
                    resultSet.getString("account_holder"),
                    resultSet.getDouble("account_balance"),
                    resultSet.getDouble("account_especial_limit_default")
                );

                accountToArrayReturn.setId(resultSet.getLong("id"));
                accountToArrayReturn.setAccountEspecialLimit(resultSet.getDouble("account_especial_limit"));

                returnedArrayList.add(accountToArrayReturn);
            }

            return returnedArrayList;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //UPDATE DO CRUD, ELE ATUALIZA UM REGISTRO NA TABELA PELO ID E RETORNA O OBJETO ATUALIZADO, UTILIZA UM NOVO OBJETO COM OS DADOS ATUALIZADOS PARA REALIZAR A ATUALIZAÇÃO,
    // SE O REGISTRO NÃO EXISTIR ELE RETORNA NULL
    public static Account update(long id,Account updatedAccount){
        String sql = "UPDATE account SET " +
                "account_number = ?," +
                "account_agency = ?," +
                "account_holder = ?," +
                "account_balance = ?," +
                "account_especial_limit = ?," +
                "account_especial_limit_default = ? " +
                "WHERE id = ?";
        try(Connection connection = ConnectionFactory.getInstance().createConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1,updatedAccount.getAccountNumber());
            preparedStatement.setInt(2,updatedAccount.getAccountAgency());
            preparedStatement.setString(3,updatedAccount.getAccountHolder());
            preparedStatement.setDouble(4,updatedAccount.getAccountBalance());
            preparedStatement.setDouble(5,updatedAccount.getAccountEspecialLimit());
            preparedStatement.setDouble(6,updatedAccount.getAccountEspecialLimitDefault());
            preparedStatement.setLong(7,id);
            preparedStatement.executeUpdate();

            return updatedAccount;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //DELETE DO CRUD, ELE DELETA UM REGISTRO NA TABELA PELO ID, SE O REGISTRO NÃO EXISTIR ELE NÃO FAZ NADA
    public static void deleteById(long id){
        String sql = "DELETE FROM account WHERE id = ?";
        try(Connection connection = ConnectionFactory.getInstance().createConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

            System.out.println("Id: "+ id +" is deleted");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

