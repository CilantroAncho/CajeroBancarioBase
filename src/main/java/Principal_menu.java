import java.io.*;
import java.util.Scanner;

//Proyecto FInal: Simulacion de Cajero Bancario
//INF-167-006
//1-21-0012 Raynick Rosario
//1-21-0395 Gerson Santos

public class Principal_menu {
    
    public static void main(String[] args) throws IOException {
        
        Scanner sc = new Scanner(System.in);

        boolean correr = true;

        do{

          int opcion = -1;

          System.out.println("===Menu General===");
          System.out.println("1) Cajeros");
          System.out.println("2) Clientes");
          System.out.println("3) Efectivo");
          System.out.println("4) Retiros");
          System.out.println("5) Finalizar");
          System.out.println("==================");

          do{

            System.out.println("Leer opcion: ");

            try{

              opcion = sc.nextInt();

              if(opcion > 5 || opcion < 1){

                opcion = -1;
                System.out.println("Opcion invalida.");

              }

            }catch(Exception e){

              e.printStackTrace();

            }

              }while(opcion == -1);

          switch(opcion){

            case 1:
              Cajeros obj = new Cajeros();
              obj.presentar_menu();
              break;
            case 2:
              Clientes obj2 = new Clientes();
              obj2.presentar_menu();
              break;
            case 3:
              Efectivo obj3 = new Efectivo();
              obj3.presentar_menu();
              break;
            case 4:
              Retiros obj4 = new Retiros();
              obj4.presentar_menu();
              break;
            default:
              System.out.println("Adios!");
              correr = false;
              System.exit(0);
              break;

          }

        }while(correr);

        sc.close(); 
        
    }
    
}
