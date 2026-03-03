/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.bankjava.account.domain;

/**
 *
 * @author pedro
 */
public class Account {

    // Atributos, todos os atributos são privados e para serem acessados usam métodos especificos
    private long id;
    private int accountNumber;
    private int accountAgency;
    private String accountHolder;
    private double accountBalance;
    private double accountEspecialLimit;
    private double accountEspecialLimitDefault;


    // O construtor é privado para proteger a criação de objetos, para serem gerados sempre objetos validos
    private Account(int accountNumber, int accountAgency, String accountHolder, double accountBalance, double accountEspecialLimitDefault){
        this.accountNumber = accountNumber;
        this.accountAgency = accountAgency;
        this.accountHolder = accountHolder;
        this.accountBalance = accountBalance;
        this.accountEspecialLimitDefault = accountEspecialLimitDefault;
        this.accountEspecialLimit = accountEspecialLimitDefault;
    }

    // Factory Method, traz um metodo de criação seguro, onde o objeto criado sempre sera valido
    // O Factory Method é um design pattern que tem como objetivo criar objetos de forma segura, onde o objeto criado sempre sera valido, evitando a criação de objetos invalidos
    // Um metodo estático, ou seja, pode ser chamado sem a necessidade de criar um objeto da classe, e retorna um objeto da classe
    public static Account newAccount(
            int accountNumber,
            int accountAgency,
            String accountHolder,
            double accountBalance,
            double accountEspecialLimitDefault)
    {
        return new Account(accountNumber,accountAgency,accountHolder, accountBalance, accountEspecialLimitDefault);
    }

    //Metodo para saque
    public void withdraw(double value){
      if (value < 0.0) throw new IllegalArgumentException("Value is cant be negative");
      if (value > (accountBalance + accountEspecialLimit)){
          throw new IllegalArgumentException("Value is greater than the account balances");
      }
      if(value > accountBalance){
          accountBalance = 0.0;
          double valueWithdrawLimit = value - accountBalance;
          accountEspecialLimit = accountEspecialLimit - valueWithdrawLimit;
      }
    }

    //Metodo para deposito
    public void deposit(double value){
         if(value < 0.0) throw new IllegalArgumentException("Value is cant be negative");
         if(accountEspecialLimit != accountEspecialLimitDefault){
             double diferenceEspacialLimitUse = accountEspecialLimitDefault - accountEspecialLimit;
             if((diferenceEspacialLimitUse + value) > accountEspecialLimitDefault){
                 double diferenceValueOfDiferenceEspecialLimit = value - accountEspecialLimitDefault;
                 accountEspecialLimit = accountEspecialLimitDefault;
                 accountBalance += diferenceValueOfDiferenceEspecialLimit;
             } else {
                 accountEspecialLimit = accountEspecialLimit + value;
             }

         } else {
             accountBalance += value;
         }
    }

    //extrato bancario
    public void seeBankStatement(){
        System.out.println("Agencia: "+accountNumber+ " Conta: "+accountNumber+" Saldo: "+accountBalance + " Limite Usado: "+accountEspecialLimit);
    }

    //getters
    public long getId() { return id; }
    public int getAccountNumber() { return accountNumber; }
    public int getAccountAgency() { return accountAgency; }
    public String getAccountHolder() { return accountHolder; }
    public double getAccountBalance() { return accountBalance; }
    public double getAccountEspecialLimit() { return accountEspecialLimit; }
    public double getAccountEspecialLimitDefault() { return accountEspecialLimitDefault; }

    //setters (deixei somente os necessarios para o exemplo)
    public void setId(long id) {
        this.id = id;
    }

    public void setAccountEspecialLimit(double Double) {
        this.accountEspecialLimit = Double;
    }

    //Metodo sobrescrito da classe, facilita o print
    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", accountNumber=" + accountNumber +
                ", accountAgency=" + accountAgency +
                ", accountHolder='" + accountHolder + '\'' +
                ", accountBalance=" + accountBalance +
                ", accountEspecialLimit=" + accountEspecialLimit +
                ", accountEspecialLimitDefault=" + accountEspecialLimitDefault +
                '}';
    }
}


