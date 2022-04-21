import java.io.*;
import java.util.Scanner;

public class Clientes{

  int codigo;
  String nombre;
  int numero_de_cuenta;
  double balance;
  Scanner L = new Scanner(System.in);

  public void presentar_menu() throws IOException{

    int opcion = -1;

    System.out.println("===Menu Clientes===");
    System.out.println("1) Actualizar");
    System.out.println("2) Consultar");
    System.out.println("3) Finalizar");
    System.out.println("==================");

    do{

      System.out.println("Leer opcion: ");
      
      try{

        opcion = L.nextInt();

        if(opcion > 3 || opcion < 1){

          opcion = -1;
          System.out.println("Opcion invalida.");
          
        }
      
      }catch(Exception e){

        e.printStackTrace();
      
      }
      
    }while(opcion == -1);

    switch(opcion){

      case 1:
        actualizar();
        break;
      case 2:
        consultar();
        break;
      default:
        break;
        
    }
    
  }

  public void actualizar(){
  
      int codigo_cliente;
      String nombre_cliente;
      int numero_de_cuenta_cliente;
      double balance_cliente;
      long posicion;
      boolean correcto = false;
      
      try {
          
          RandomAccessFile archivo = new RandomAccessFile("Clientes.dat", "rw");
          
          while(true){
          
              System.out.println("Codigo: ");
              codigo_cliente = L.nextInt();
              
              if(codigo_cliente == 0){
              
                  break;
                  
              }
              
              try {
                  
                  posicion = buscar_codigo(codigo_cliente, 1, archivo);
                  
                  if(posicion != -1){//Modificamos el que existe
                  
                      leer_registro(posicion, archivo);
                      
                      L.nextLine();
                      System.out.println("Nuevo Nombre (" + nombre + "): ");
                      nombre_cliente = L.nextLine();
                      
                      do {

                          System.out.println("Nuevo numero de cuenta (" + numero_de_cuenta + "): ");
                          numero_de_cuenta_cliente = L.nextInt();
                          
                          if(numero_de_cuenta_cliente > 0 && (buscar_codigo(numero_de_cuenta_cliente, 2, archivo) == -1) ){
                          
                              correcto = true;
                              
                          }else{
                          
                              System.out.println("Error: El numero de cuenta es igual o inferior a 0 o ya existe ese numero de cuenta.");
                              correcto = false;
                              
                          }
                          
                      } while (!correcto);                
                      
                      System.out.println("Nuevo balance (" + balance + ")");
                      balance_cliente = L.nextDouble();
                      
                      nombre = nombre_cliente;
                      numero_de_cuenta = numero_de_cuenta_cliente;
                      balance = balance_cliente;
                      
                      actualizar_registro(posicion, archivo);
                      
                  }else{// Creamos uno nuevo
                  
                      codigo = codigo_cliente;
                      
                      L.nextLine();
                      System.out.println("Nombre: ");
                      nombre = L.nextLine();
                      
                      do {

                          System.out.println("Numero de cuenta: ");
                          numero_de_cuenta_cliente = L.nextInt();
                          
                          if(numero_de_cuenta_cliente > 0 && (buscar_codigo(numero_de_cuenta_cliente, 2, archivo) == -1) ){
                          
                              correcto = true;
                              
                          }else{
                          
                              System.out.println("Error: El numero de cuenta es igual o inferior a 0 o ya existe ese numero de cuenta.");
                              correcto = false;
                              
                          }
                          
                      } while (!correcto);
                      
                      numero_de_cuenta = numero_de_cuenta_cliente;
                      
                      System.out.println("Balance: ");
                      balance = L.nextDouble();
                      
                      actualizar_registro(archivo.length(), archivo);
                      
                  }
                  
              } catch (Exception e) {
                  
                  e.printStackTrace();
                  
              }
              
          }
                  
      } catch (Exception e) {
      }
      
      
  }
  
  public void consultar() throws FileNotFoundException, IOException{
  
      RandomAccessFile archivo = new RandomAccessFile("Clientes.dat", "rw");
  
      try {
          
          System.out.println("Codigo\t Nombre\t\t\t Cuenta\t Balance");
          
          long posicion = 0;        
          
          while(true){
          
              leer_registro(posicion, archivo);
              posicion = archivo.getFilePointer();
              
              System.out.println(codigo + "\t" + nombre + " " + numero_de_cuenta + " " + balance);
              
          }
          
      } catch (Exception e) {
          
          System.out.println("Fin de archivo.");
          
      }finally{
      
          archivo.close();
          
      }
      
  }
  
  public long buscar_codigo(int cod_, int cual_campo, RandomAccessFile archivo){
  
      long pos_ = 0;
      int cod;
      String nom;
      int num_cuenta;
      double balc;
      
      try {
          
          archivo.seek(0);
          
          while(true){
          
              cod = archivo.readInt();
              nom = archivo.readLine();
              num_cuenta = archivo.readInt();
              balc = archivo.readDouble();
              
              if(cual_campo == 1){//Si cual campo es 1 se busca el ID
              
                if(cod_ == cod){
              
                  break;
                  
                }
                  
              }else{//Si cual campo es 2 se busca el numero de cuenta
              
                  if(cod_ == num_cuenta){
                  
                      break;
                      
                  }
                  
              }
              
              pos_ = archivo.getFilePointer();
              
          }
          
      } catch (IOException e) {
          
          pos_ = -1;
          
      }
      
      return pos_;
      
  }
  
  public void leer_registro(long pos_, RandomAccessFile archivo) throws IOException{
  
      archivo.seek(pos_);
      codigo = archivo.readInt();
      nombre = archivo.readLine();
      numero_de_cuenta = archivo.readInt();
      balance = archivo.readDouble();
      
  }
  
  public void actualizar_registro(long pos_, RandomAccessFile archivo) throws IOException{
  
      archivo.seek(pos_);
      archivo.writeInt(codigo);
      archivo.writeBytes( TamanoStr(nombre, 25) + "\r\n" );
      archivo.writeInt(numero_de_cuenta);
      archivo.writeDouble(balance);
      
      
  }
  
  String TamanoStr(String H, int Long){
        
      String tH = H.trim();
      
      while (tH.length() < Long){
      
        tH += " ";
          
      }
      
      return tH;
  
  }
  
}
