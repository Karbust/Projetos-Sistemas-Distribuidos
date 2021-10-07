import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class PlaceManager extends UnicastRemoteObject implements PlacesListInterface {
  private static final long serialVersionUID = 1L;
  private static final ArrayList<Place> places = new ArrayList<Place>();

  public PlaceManager() throws RemoteException {
    super(0);
  }

  @Override
  public void addPlace(Place p) throws RemoteException {
    places.add(p);
    ObjectRegistryInterface o = null;
    try {
      o = (ObjectRegistryInterface) Naming.lookup("rmi://localhost:2023/registry");
      o.addObject(p.getPostalCode(), "rmi://localhost:2022/placelist");
    } catch (RemoteException | NotBoundException | MalformedURLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public ArrayList<Place> allPlaces() throws RemoteException {
    return places;
  }

  @Override
  public Place getPlace(String objectID) throws RemoteException {
    for (Place p : places) {
      if (p.getPostalCode().equals(objectID)) {
        return p;
      }
    }
    return null;
  }
}
