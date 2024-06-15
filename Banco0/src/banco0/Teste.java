/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package banco0;

/**
 *
 * @author pedro
 */
public class Teste {
    public static void main(String[] args) {
      Conta c1 = new Conta("123456","Zé","0221");

      
      c1.depositar(500.0);
      c1.verExtrato();
      
      c1.sacar(10.0);
      c1.verExtrato();
      
      c1.sacar(1000.0);
      c1.verExtrato();
      
      c1.depositar(-40.0);
      c1.verExtrato();
    }
    
}
