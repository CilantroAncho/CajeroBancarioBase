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

    public void presentar_menu(){

        Scanner sc = new Scanner(System.in);

        int opcion = -1;

        System.out.println("===Menu Cajeros===");
        System.out.println("1) Actualizar");
        System.out.println("2) Consultar");
        System.out.println("3) Finalizar");
        System.out.println("==================");

        do{

          System.out.println("Leer opcion: ");

          try{

            opcion = sc.nextInt();

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

  public void actualizar(){}
  public void consultar(){}

}
