import java.util.Scanner;

//Proyecto FInal: Simulacion de Cajero Bancario
//INF-167-006
//1-21-0012 Raynick Rosario
//X-XX-XXXX Gerson Santos

public class Principal_menu {
    
    public static void main(String[] args) {
        
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

              break;
            case 3:
              Efectivo obj3 = new Efectivo();

              break;
            case 4:
              Retiros obj4 = new Retiros();

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
