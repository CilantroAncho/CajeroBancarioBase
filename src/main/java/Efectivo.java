import java.io.*;
import java.util.Scanner;

public class Efectivo{

  int codigo_cajero;
  int cantidad_den_menor;
  int cantidad_den_mayor;
  
  Cajeros obj_de_cajeros = new Cajeros();
  
  Scanner L = new Scanner(System.in);

  public void presentar_menu() throws FileNotFoundException, IOException{

    int opcion = -1;

    System.out.println("===Menu Efectivo===");
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
      int den_menor;
      int den_mayor;
      
      long posicion_efectivo;
      long posicion_cajero;
      boolean correcto = false;
      
      try {
          
          RandomAccessFile archivo_efectivo = new RandomAccessFile("Efectivo.dat", "rw");
          RandomAccessFile archivo_cajero = new RandomAccessFile("Cajeros.dat", "rw");
          
          while(true){
          
              System.out.println("Codigo cajero: ");
              codigo_de_cajero = L.nextInt();
              
              if(codigo_de_cajero == 0){
              
                  break;
                  
              }
              
              try {
                  
                  posicion_efectivo = buscar_codigo(codigo_de_cajero, archivo_efectivo);
                  posicion_cajero = obj_de_cajeros.buscar_codigo(codigo_de_cajero, archivo_cajero);
                  
                  if(posicion_cajero != -1){
                  
                      obj_de_cajeros.leer_registro(posicion_cajero, archivo_cajero);
                      
                      if(posicion_efectivo != -1){
                          
                        leer(posicion_efectivo, archivo_efectivo);
                      
                        do{

                            System.out.println("Nueva cantidad de denominacion menor ("+ obj_de_cajeros.denominacion_menor +"): ");
                            den_menor = L.nextInt();

                            if(den_menor >= 0){

                                correcto = true;

                            }else{

                                correcto = false;
                                System.out.println("Error. Intentar otra vez.");

                            }

                        }while(!correcto);

                        correcto = false;

                        do{

                            System.out.println("Nueva cantidad de denominacion mayor ("+ obj_de_cajeros.denominacion_mayor +"): ");
                            den_mayor = L.nextInt();

                            if(den_mayor >= 0){

                                 correcto = true;

                             }else{

                                 correcto = false;
                                 System.out.println("Error. Intentar otra vez.");

                             }

                         }while(!correcto);  

                        cantidad_den_menor = den_menor;
                        cantidad_den_mayor = den_mayor;

                        actualizar_registro(posicion_efectivo, archivo_efectivo);                        
                          
                      }else{
                          
                          codigo_cajero = codigo_de_cajero;
                          
                          System.out.println("Cantidad de denominacion menor("+obj_de_cajeros.denominacion_menor+"): ");
                          cantidad_den_menor = L.nextInt();
                          
                          System.out.println("Cantidad de denominacion mayor(" + obj_de_cajeros.denominacion_mayor + "): ");
                          cantidad_den_mayor = L.nextInt();
                          
                          actualizar_registro(archivo_efectivo.length(), archivo_efectivo);
                          
                      }
                      
                  }else{
                  
                      System.out.println("Cajero no existe. Intentar otra vez.");
                      
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
  
        RandomAccessFile archivo = new RandomAccessFile("Efectivo.dat", "rw");
      
        try {
          
          System.out.println("Codigo de Cajero\t\t Cantidad Denominacion Menor\t Cantidad Denominacion Mayor");
          
          long posicion = 0;        
          
          while(true){
          
              leer(posicion, archivo);
              posicion = archivo.getFilePointer();
              
              System.out.println(codigo_cajero + "\t" + cantidad_den_menor + " " + cantidad_den_mayor);
              
          }
          
      } catch (Exception e) {
          
          System.out.println("Fin de archivo.");
          
      }finally{
      
          archivo.close();
          
      }
      
  }
  
  public void actualizar_registro(long pos_, RandomAccessFile archivo) throws IOException{
  
      archivo.seek(pos_);
      archivo.writeInt(codigo_cajero);
      archivo.writeInt(cantidad_den_menor);
      archivo.writeInt(cantidad_den_mayor);
      
  }
  
  public long buscar_codigo(int cod_, RandomAccessFile archivo){
  
      long pos_ = 0;
      int cod;
      int deno_menor;
      int deno_mayor;
      
      try {
          
          archivo.seek(0);
          
          while(true){
          
              cod = archivo.readInt();
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
  
  public void leer(long posicion, RandomAccessFile arch) throws IOException{
  
      arch.seek(posicion);
      codigo_cajero = arch.readInt();
      cantidad_den_menor = arch.readInt();
      cantidad_den_mayor = arch.readInt();
      
  }
  
}