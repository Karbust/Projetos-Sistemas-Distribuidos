import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {
  public static void main(String[] args) {
    Registry r = null;
    try {
      r = LocateRegistry.createRegistry(2022);
    } catch (RemoteException a) {
      a.printStackTrace();
      try {
        r = LocateRegistry.getRegistry(2022);
      } catch (NumberFormatException | RemoteException e1) {
        e1.printStackTrace();
      }
    }

    try {
      PlacesListInterface placeList = new PlaceManager(2022);
      assert r != null;
      r.rebind("placelist", placeList);

      System.out.println("Place server ready");
    } catch (Exception e) {
      System.out.println("Place server main " + e.getMessage());
    }
  }
}
