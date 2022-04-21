import java.io.*;
import java.util.Scanner;

public class Retiros{
  //Cajero
  int codigo_cajero;
  String ubicacion_cajero;
  String ciudad_cajero;
  int deno_menor;
  int deno_mayor;
  int cantidad_de_billetes_menor;
  int cantidad_de_billetes_mayor;
  //Cajero
  //Cliente
  String nombre_de_cliente;
  int numero_de_cuenta_cliente;
  double balance_de_cliente;
  //Cliente
  String fecha;
  double retiro;
  
  Cajeros obj_cajeros = new Cajeros();
  Clientes obj_clientes = new Clientes();
  
  Scanner L = new Scanner(System.in);

  public void presentar_menu(){

    int opcion = -1;

    System.out.println("===Menu Retiros===");
    System.out.println("1) Retirar");
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
      //Datos de cajero
      int cod_cajero;
      String ubi;
      String ciud;
      int den_menor;
      int den_mayor;
      //Datos de cajero
      //Datos de Cliente
      int num_cuenta;
      String nombre;
      double balance;
      
      String fecha_;
      double monto_retirar;
      
      long posicion_retiros;
      long posicion_cajeros;
      long posicion_clientes;
      
      boolean correcto = false;
      
      try {
          
          RandomAccessFile archivo_retiros = new RandomAccessFile("Retiros.dat", "rw");
          RandomAccessFile archivo_cajero = new RandomAccessFile("Cajeros.dat", "rw");
          RandomAccessFile archivo_cliente = new RandomAccessFile("Clientes.dat", "rw");
          RandomAccessFile archivo_efectivo = new RandomAccessFile("Efectivo.dat", "rw");
          
          while(true){
          
              System.out.println("Codigo cajero: ");
              cod_cajero = L.nextInt();
              
              if(cod_cajero == 0){
              
                  break;
                  
              }
              
              try {
                  
                  posicion_cajeros = obj_cajeros.buscar_codigo(cod_cajero, archivo_cajero);
                  
                  if(posicion_cajeros != 1){
                  
                      leer_registros_cajero(posicion_cajeros, archivo_cajero);
                      
                      System.out.println("<<" + ubicacion_cajero + ">> <<" + ciudad_cajero + ">>");
                      
                      do{
                      
                        System.out.println("Numero de cuenta: ");
                        num_cuenta = L.nextInt();
                      
                        posicion_clientes = obj_clientes.buscar_codigo(num_cuenta, 2, archivo_cliente);
                      
                        if(posicion_clientes != -1){

                            correcto = true;
                            leer_registros_cliente(posicion_clientes, archivo_cliente);

                        }else{

                            System.out.println("Numero de cuenta no existe. Intentalo otra vez.");
                            correcto = false;

                        }
                          
                      }while(!correcto);
                      
                      correcto = false;
                      
                      System.out.println("<<" + nombre_de_cliente + ">>");
                      L.nextLine();
                      //Gerson trabaja esto
                      System.out.println("Fecha: ");
                      fecha_ = L.nextLine();
                      //Gerson trabaja esto
                      
                      do{
                      
                        System.out.println("Monto a retirar: ");
                        monto_retirar = L.nextDouble(); 
                        
                        if(monto_retirar < balance_de_cliente){
                        
                            int cantidad_de_menor = 0;
                            int cantidad_de_mayor = 0;
                            
                            cantidad_de_mayor = (int) monto_retirar/deno_mayor;
                            monto_retirar = monto_retirar - (cantidad_de_mayor * deno_mayor); 
                            
                            cantidad_de_menor = (int) monto_retirar/deno_menor;
                            monto_retirar = monto_retirar - (cantidad_de_menor * deno_menor);
                            
                            if(monto_retirar == 0.0){
                            
                                correcto = true;
                                cantidad_de_billetes_menor = cantidad_de_menor;
                                cantidad_de_billetes_mayor = cantidad_de_mayor;
                                
                            }else{
                            
                                correcto = false;
                                System.out.println("Balance no se puede retribuir con las denominaciones del cajero. Intente otra vez");
                                
                            }                          
                            
                        }else{
                        
                            correcto = true;
                            System.out.println("No tiene fondos suficientes para hcer este retiro.");
                            
                        }
                                                  
                      }while(!correcto);                                            
                      //Reducimos balance de cliente
                      retiro = monto_retirar;           
                      balance_de_cliente -= retiro;                 
                      
                      obj_clientes.balance = balance_de_cliente;
                      obj_clientes.actualizar_registro(posicion_clientes, archivo_cliente);
                      //Reducimos balance de cliente
                      
                      
                      
                      posicion_retiros = buscar_codigo(cod_cajero, archivo_retiros);
                      
                      if(posicion_retiros != 1){
                      
                          actualizar_registro(posicion_retiros, archivo_retiros);
                          
                      }else{
                      
                          actualizar_registro(archivo_retiros.length(), archivo_retiros);
                          
                      }
                      
                      System.out.println("Transaccion Exitosa.");
                      
                  }else{
                  
                      System.out.println("Cajero no existe. Intentelo otra vez.");
                      
                  }
                  
                  
              } catch (Exception e) {
              }
              
          }
          
      } catch (Exception e) {
          
          e.printStackTrace();
          
      }
      
  }
  
  public void consultar(){}
  
    public long buscar_codigo(int cod_, RandomAccessFile archivo){
  
      long pos_ = 0;
      int cod;
      
      try {
          
          archivo.seek(0);
          
          while(true){
          
              cod = archivo.readInt();
              numero_de_cuenta_cliente = archivo.readInt();
              fecha = archivo.readLine();
              balance_de_cliente = archivo.readDouble();
              
              if(cod == cod_){
              
                  break;
                  
              }
              
              pos_ = archivo.getFilePointer();
              
          }
          
      } catch (IOException e) {
          
          pos_ = -1;
          
      }      
      
      return pos_;
      
  }    
  
    public void leer_registros_cajero(long posicion, RandomAccessFile arch) throws IOException{
  
      obj_cajeros.leer_registro(posicion, arch);
      codigo_cajero = obj_cajeros.codigo;
      ubicacion_cajero = obj_cajeros.ubicacion;
      ciudad_cajero = obj_cajeros.ciudad;
      deno_menor = obj_cajeros.denominacion_menor;
      deno_mayor = obj_cajeros.denominacion_mayor;
      
  }
 
    public void leer_registros_cliente(long posicion, RandomAccessFile arch) throws IOException{
  
      obj_clientes.leer_registro(posicion, arch);
      nombre_de_cliente = obj_clientes.nombre;
      numero_de_cuenta_cliente = obj_clientes.numero_de_cuenta;
      balance_de_cliente = obj_clientes.balance;
      
  }
    
  public void actualizar_registro(long pos_, RandomAccessFile archivo) throws IOException{
  
      archivo.seek(pos_);
      archivo.writeInt(codigo_cajero);
      archivo.writeInt(numero_de_cuenta_cliente);
      archivo.writeBytes( TamanoStr(fecha, 10) + "\r\n" );      
      archivo.writeDouble(balance_de_cliente);
      
      
  }
  
    String TamanoStr(String H, int Long){
        
      String tH = H.trim();
      
      while (tH.length() < Long){
      
        tH += " ";
          
      }
      
      return tH;
  
  }
  
}