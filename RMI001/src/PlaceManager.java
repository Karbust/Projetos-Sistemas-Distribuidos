import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class PlaceManager extends UnicastRemoteObject implements PlacesListInterface {
  private static final long serialVersionUID = 1L;

  // Attributes
  private static final ArrayList<Place> places = new ArrayList<Place>();

  // Constructor
  public PlaceManager() throws RemoteException {
    super(0);
  }
  
  public PlaceManager(Integer port) throws RemoteException {
    super(0);
  }

  public void addPlace(Place p) throws RemoteException {
    places.add(p);
  }

  public ArrayList<Place> allPlaces() throws RemoteException {
    return places;
  }
}
