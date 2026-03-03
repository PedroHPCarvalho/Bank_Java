/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import com.bankjava.account.Infrastructure.AccountRepository;
import com.bankjava.account.Infrastructure.ConnectionFactory;
import com.bankjava.account.Infrastructure.DatabaseInitializer;
import com.bankjava.account.domain.Account;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author pedro
 */
public class Application {
    public static void main(String[] args) throws IOException {
      Account account= Account.newAccount(4112,12,"Pedro Carvalho", 132.0,500);
      Account account2= Account.newAccount(4112,12,"Pedro Herique", 132.0,500);

      DatabaseInitializer.createInitilizer(ConnectionFactory.getInstance().createConnection()).initilizerH2();

      Account accountReturned = AccountRepository.save(account);
      Account accountReturned2 = AccountRepository.save(account2);

      account.deposit(20000);

      Account accountReturnedUpdated = AccountRepository.update(1,account);

      List<Account> returnedList = AccountRepository.findAll();

      for (Account returnedAccount : returnedList){
          System.out.println(returnedAccount.toString());
      }

      try {
          Thread.sleep(13000);
      } catch (InterruptedException e) {
          throw new RuntimeException(e);
      }


      AccountRepository.deleteById(2);


        try{
        org.h2.tools.Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
      } catch (SQLException e) {
          throw new RuntimeException(e);
      }
      System.in.read();
    }
}
