import java.rmi.*;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.rmi.Naming;

public class RMIClient {
    static public void main (String[] Args) {
        PlacesListInterface l1 = null;
        try{
            System.out.println("Localizar servidor de Objetos...");
            l1  = (PlacesListInterface) Naming.lookup("rmi://localhost:2022/placelist");
            Place p = new Place("3510", "Viseu");
            System.out.println("Invocar addPlace() ...");
            l1.addPlace(p);

            System.out.println(l1.getPlace("3510").getPostalCode());

            System.out.println("Obter o endereço do servidor no Registry() ...");
            ObjectRegistryInterface l2;
            PlacesListInterface l3;
            try {
                l2 = (ObjectRegistryInterface) Naming.lookup("rmi://localhost:2023/registry");
                String addr = l2.resolve("3510");

                System.out.println(addr);

                System.out.println("Invocar getPlace() no servidor de objetos...");
                l3  = (PlacesListInterface) Naming.lookup(addr);
                System.out.println(l3.getPlace("3510").getLocality());
            } catch (NotBoundException e) {
                e.getCause();
            }

        } catch(Exception e) { System.out.println("Problemas de Comunicação\n" + e.getMessage()); }
    }
}
