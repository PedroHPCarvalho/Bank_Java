/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package banco0;

/**
 *
 * @author pedro
 */
public class Conta {
   private String num;
   // encapsulamento somente os metodos que a classe acessa
   private String tit;
   private String age;

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
   private double sld;
   private double lim = 500;
   private double cta;
   
   Conta(String num,String tit, String age){
       this.num = num;
       this.tit = tit;
       this.age = age;
       
   }

 
    
    
    public void sacar(double valor){
      if (valor <0.0)
            System.out.println("Valores negativos não são aceitos");
      else
          sld = sld - valor;
      
      if((lim + sld) < 0.0);
        System.out.println("Limite exedido");
    }
      
      
    public void depositar(double valor){
     if(valor<0.0)
            System.out.println("Valores negativos não são aceitos");
     else
         sld = sld + valor;
        
    }
    
    public void verExtrato(){
        System.out.println("Agencia: "+age+ " Conta: "+cta+" Saldo: "+sld + " Limite: "+lim);      
    }

    void verExtrato(String string, String zé, String string0) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
    

