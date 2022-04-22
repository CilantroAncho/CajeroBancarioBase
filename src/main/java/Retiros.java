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
  Efectivo obj_efectivo = new Efectivo();
  
  Scanner L = new Scanner(System.in);

  public void presentar_menu() throws IOException{

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
      
      //String fecha_;
      double monto_retirar;
      
      long posicion_cajeros;
      long posicion_clientes;
      long posicion_efectivo;
      
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
                  posicion_efectivo = obj_efectivo.buscar_codigo(cod_cajero, archivo_efectivo);
                  
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
                            obj_clientes.leer_registro(posicion_clientes, archivo_cliente);

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
                      fecha = L.nextLine();
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
                                
                                obj_efectivo.leer(posicion_efectivo, archivo_efectivo);
                            
                                if(obj_efectivo.cantidad_den_mayor > cantidad_de_mayor){
                                
                                    if(obj_efectivo.cantidad_den_menor > cantidad_de_menor){
                                    
                                        correcto = true;
                                        cantidad_de_billetes_menor = cantidad_de_menor;
                                        cantidad_de_billetes_mayor = cantidad_de_mayor;
                                        
                                    }else{
                                    
                                        System.out.println("No hay suficientes billetes de cantidad menor para esta transaccion.");
                                        
                                    }
                                    
                                }else{
                                
                                    System.out.println("No hay suficientes billetes de cantidad mayor para esta transaccion.");
                                    
                                }
                                
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
                      
                      retiro = (obj_cajeros.denominacion_mayor * cantidad_de_billetes_mayor) + (obj_cajeros.denominacion_menor * cantidad_de_billetes_menor);
                      
                      obj_clientes.balance -= (obj_cajeros.denominacion_mayor * cantidad_de_billetes_mayor) + (obj_cajeros.denominacion_menor * cantidad_de_billetes_menor);
                      obj_clientes.actualizar_registro(posicion_clientes, archivo_cliente);
                      //Reducimos balance de cliente                                         
                      
                      //Reducimos billetes en efectivo.
                      
                      obj_efectivo.cantidad_den_menor -= cantidad_de_billetes_menor;
                      obj_efectivo.cantidad_den_mayor -= cantidad_de_billetes_mayor;
                      obj_efectivo.actualizar_registro(posicion_efectivo, archivo_efectivo);
                      
                      //Reducimos billetes en efectivo
                      
                      actualizar_registro(archivo_retiros.length(), archivo_retiros);
                          
                      System.out.println("Transaccion Exitosa.");
                      
                  }else{
                  
                      System.out.println("Cajero no existe. Intentelo otra vez.");
                      
                  }
                  
                  
              } catch (Exception e) {
                  
                  e.printStackTrace();
              }
              
          }
          
      } catch (Exception e) {
          
          e.printStackTrace();
          
      }
      
  }
  
  public void consultar() throws FileNotFoundException, IOException{
  
        RandomAccessFile archivo_retiros = new RandomAccessFile("Retiros.dat", "rw");
        RandomAccessFile archivo_cliente = new RandomAccessFile("Clientes.dat", "rw");
        
        int num_cuenta;
        long posicion_clientes;
        boolean correcto = false;
      
        try {
            
            do{
                      
                System.out.println("Numero de cuenta: ");
                num_cuenta = L.nextInt();
                
                if(num_cuenta == 0){
                
                    break;
                    
                }
                      
                posicion_clientes = obj_clientes.buscar_codigo(num_cuenta, 2, archivo_cliente);
                      
                if(posicion_clientes != -1){

                    correcto = true;
                    leer_registros_cliente(posicion_clientes, archivo_cliente);
                    obj_clientes.leer_registro(posicion_clientes, archivo_cliente);
                    
                    System.out.println("<<" + nombre_de_cliente + ">>");
                    System.out.println("<<" + balance_de_cliente + ">>");
                    
                    System.out.println("Cod. Cajero\tFecha\tMonto");
                    
                    long posicion = buscar_codigo(num_cuenta, archivo_retiros);
                    
                    if(posicion != -1){
                        
                        try {
                            
                            while(true){
                        
                            leer(posicion, archivo_retiros);
                            posicion = archivo_retiros.getFilePointer();
              
                            System.out.println(codigo_cajero + "\t" + fecha + "\t" + retiro);
                            
                            }
                            
                        } catch (Exception e) {
                            
                            System.out.println("Fin de archivo.");
                            
                        }finally{
                        
                            archivo_retiros.close();
                            archivo_cliente.close();
          
                            
                        }                  
                        
                    }else{
                    
                        System.out.println("No tiene retiros.");
                        
                    }

                }else{

                    System.out.println("Numero de cuenta no existe. Intentalo otra vez.");
                    correcto = false;

                }
                          
            }while(!correcto);
            
          
          
      } catch (Exception e) {
          
          e.printStackTrace();
          
      }
  
  }
  
    public long buscar_codigo(int cod_, RandomAccessFile archivo){
  
      long pos_ = 0;
      
      try {
          
          archivo.seek(0);
          
          while(true){
          
              codigo_cajero = archivo.readInt();
              numero_de_cuenta_cliente = archivo.readInt();
              fecha = archivo.readLine();
              retiro = archivo.readDouble();            
              
              if(numero_de_cuenta_cliente == cod_){
              
                  break;
                  
              }
              
              pos_ = archivo.getFilePointer();
              
          }
          
      } catch (IOException e) {
          
          pos_ = -1;
          
      }      
      
      return pos_;
      
  }

    public void leer(long posicion, RandomAccessFile archivo) throws IOException{
        
        archivo.seek(posicion);
        codigo_cajero = archivo.readInt();
        numero_de_cuenta_cliente = archivo.readInt();
        fecha = archivo.readLine();
        balance_de_cliente = archivo.readDouble();
        
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
      archivo.writeDouble(retiro);
      
      
  }
  
    String TamanoStr(String H, int Long){
        
      String tH = H.trim();
      
      while (tH.length() < Long){
      
        tH += " ";
          
      }
      
      return tH;
  
  }
  
}