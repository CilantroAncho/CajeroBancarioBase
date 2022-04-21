import java.io.*;
import java.util.Scanner;

/**
 *
 * @author raynickrosario
 */
public class Cajeros {
  
    int codigo;
    String ubicacion;
    String ciudad;
    int denominacion_menor;
    int denominacion_mayor;
    
    Scanner L = new Scanner(System.in);

    public void presentar_menu() throws IOException{

        int opcion = -1;

        System.out.println("===Menu Cajeros===");
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
      
      int codigo_de_cajero;
      String ubicacion_de_cajero;
      String ciudad_de_cajero;
      int denominacion_menor_de_cajero = 0;
      int denominacion_mayor_de_cajero = 0;
      long posicion;
      boolean correcto = false;
      
      try{
      
        RandomAccessFile archivo = new RandomAccessFile("Cajeros.dat", "rw");
        
        while(true){
        
            System.out.println("Codigo Cajero: ");
            codigo_de_cajero = L.nextInt();
            
            if(codigo_de_cajero == 0){// Se acaba el programa si el usuario pone 0.
            
                break;
                
            }
            
            try {
                
                posicion = buscar_codigo(codigo_de_cajero, archivo);
                
                if(posicion != -1){// El cajero existe. Modificamos
                
                    leer_registro(posicion, archivo);
                    
                    L.nextLine();
                    System.out.println("Nueva Ubicacion ("+ ubicacion + "): ");
                    ubicacion_de_cajero = L.nextLine();
                    
                    System.out.println("Nueva Ciudad ("+ ciudad +"): ");
                    ciudad_de_cajero = L.nextLine();
                    
                    do{
                        
                        System.out.println("Nueva denominacion menor ("+ denominacion_menor +"): ");
                        denominacion_menor_de_cajero = L.nextInt();
                    
                        if(denominacion_menor_de_cajero == 100 || denominacion_menor_de_cajero == 200 || denominacion_menor_de_cajero == 500 || denominacion_menor_de_cajero == 1000){
                        
                            correcto = true;
                            
                        }else{
                        
                            correcto = false;
                            System.out.println("Error. Intentar otra vez.");
                            
                        }
                        
                    }while(!correcto);
                    
                    correcto = false;
                    
                    do{
                        
                        System.out.println("Nueva denominacion mayor ("+ denominacion_mayor +"): ");
                        denominacion_mayor_de_cajero = L.nextInt();
                    
                        if(denominacion_mayor_de_cajero == 100 || denominacion_mayor_de_cajero == 200 || denominacion_mayor_de_cajero == 500 || denominacion_mayor_de_cajero == 1000){
                        
                            correcto = true;
                            
                        }else{
                        
                            correcto = false;
                            System.out.println("Error. Intentar otra vez.");
                            
                        }
                        
                    }while(!correcto);
                    
                    ubicacion = ubicacion_de_cajero;
                    ciudad = ciudad_de_cajero;
                    denominacion_menor = denominacion_menor_de_cajero;
                    denominacion_mayor = denominacion_mayor_de_cajero;
                    
                    actualizar_registro(posicion, archivo);
                    
                }else{// El cajero no existe. Creamos                    
                
                    codigo = codigo_de_cajero;
                    
                    L.nextLine();
                    System.out.println("Ubicacion: ");
                    ubicacion = L.nextLine();
                    
                    System.out.println("Ciudad: ");
                    ciudad = L.nextLine();
                    
                    do{
                        
                        System.out.println("Denominacion menor: ");
                        denominacion_menor_de_cajero = L.nextInt();
                    
                        if(denominacion_menor_de_cajero == 100 || denominacion_menor_de_cajero == 200 || denominacion_menor_de_cajero == 500 || denominacion_menor_de_cajero == 1000){
                        
                            correcto = true;
                            
                        }else{
                        
                            correcto = false;
                            System.out.println("Error. Intentar otra vez.");
                            
                        }
                        
                    }while(!correcto);
                    
                    correcto = false;
                    
                    do{
                        
                        System.out.println("Denominacion mayor: ");
                        denominacion_mayor_de_cajero = L.nextInt();
                    
                        if(denominacion_mayor_de_cajero == 100 || denominacion_mayor_de_cajero == 200 || denominacion_mayor_de_cajero == 500 || denominacion_mayor_de_cajero == 1000){
                        
                            correcto = true;
                            
                        }else{
                        
                            correcto = false;
                            System.out.println("Error. Intentar otra vez.");
                            
                        }
                        
                    }while(!correcto);
                    
                    denominacion_menor = denominacion_menor_de_cajero;
                    denominacion_mayor = denominacion_mayor_de_cajero;
                    
                    actualizar_registro(archivo.length(), archivo);
                    
                }
                
            } catch (Exception e) {
                
                e.printStackTrace();
                
            }
            
        }
          
      }catch(Exception e){}
      
      
  }
  
  public void consultar() throws FileNotFoundException, IOException{
      
      RandomAccessFile archivo = new RandomAccessFile("Cajeros.dat", "rw");
  
      try {
          
          System.out.println("Codigo\t Ubicacion\t\t Ciudad\t\t Denominacion Menor\t Denominacion Mayor");
          
          long posicion = 0;        
          
          while(true){
          
              leer_registro(posicion, archivo);
              posicion = archivo.getFilePointer();
              
              System.out.println(codigo + "\t" + ubicacion + " " + ciudad + " " + denominacion_menor + "\t\t\t" + denominacion_mayor);
              
          }
          
      } catch (Exception e) {
          
          System.out.println("Fin de archivo.");
          
      }finally{
      
          archivo.close();
          
      }
      
  }
  
  public long buscar_codigo(int cod_, RandomAccessFile archivo){
  
    long pos_ = 0;
    int cod;
    String ubi;
    String ciu;
    int deno_menor;
    int deno_mayor;
    
      try {
          
          archivo.seek(0);
          
          while(true){
          
              cod = archivo.readInt();
              ubi = archivo.readLine();
              ciu = archivo.readLine();
              deno_menor = archivo.readInt();
              deno_mayor = archivo.readInt();
              
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

  public void leer_registro(long pos_, RandomAccessFile archivo) throws IOException{
  
      archivo.seek(pos_);
      codigo = archivo.readInt();
      ubicacion = archivo.readLine();
      ciudad = archivo.readLine();
      denominacion_menor = archivo.readInt();
      denominacion_mayor = archivo.readInt();
      
  }

  public void actualizar_registro(long pos_, RandomAccessFile archivo) throws IOException{
  
      archivo.seek(pos_);
      archivo.writeInt(codigo);
      archivo.writeBytes( TamanoStr(ubicacion, 20) + "\r\n" );
      archivo.writeBytes( TamanoStr(ciudad, 20) + "\r\n" );
      archivo.writeInt(denominacion_menor);
      archivo.writeInt(denominacion_mayor);
      
      
  }
  
  String TamanoStr(String H, int Long){
        
      String tH = H.trim();
      
      while (tH.length() < Long){
      
        tH += " ";
          
      }
      
      return tH;
  
  }
  
}
