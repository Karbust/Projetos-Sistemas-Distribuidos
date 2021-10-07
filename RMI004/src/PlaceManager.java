import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class PlaceManager extends UnicastRemoteObject implements PlacesListInterface {
  private static final long serialVersionUID = 1L;
  private static ArrayList<Place> places = new ArrayList<Place>();
  private final Integer port;

  public PlaceManager(Integer port) throws RemoteException {
    super(0);
    this.port = port;
    cloneFromReplica();
  }

  @Override
  public void addPlace(Place p) throws RemoteException {
    places.add(p);
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

  private void cloneFromReplica() {
    ReplicasManagementInterface r = null;
    String url = null;
    PlacesListInterface p = null;

    try {
      r = (ReplicasManagementInterface) Naming.lookup("rmi://localhost:2024/replicamanager");
      url = r.addReplica("rmi://localhost:" + port.toString() + "/placelist");
      System.out.println(url);

      if (url != null) {
        p = (PlacesListInterface) Naming.lookup(url);
        places = p.allPlaces();
      }
    } catch (MalformedURLException | RemoteException | NotBoundException e) {
      e.printStackTrace();
    }
  }
}
