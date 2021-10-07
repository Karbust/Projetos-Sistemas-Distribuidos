import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {
  public static void main(String[] args) {
    Registry r = null;
    try {
      r = LocateRegistry.createRegistry(Integer.parseInt(args[0]));
    } catch (RemoteException a) {
      a.printStackTrace();
    }

    try {
      PlacesListInterface placeList = new PlaceManager(Integer.parseInt(args[0]));
      assert r != null;
      r.rebind("placelist", placeList);

      System.out.println("Place server ready");
    } catch (Exception e) {
      System.out.println("Place server main " + e.getMessage());
    }
  }
}