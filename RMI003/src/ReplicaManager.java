import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;

class ReplicaManager extends UnicastRemoteObject implements PlacesListManagerInterface {
    // Attributes
    private static final long serialVersionUID = 1L;
    private static final ArrayList<String> placesList = new ArrayList<String>();

    // Constructor
    public ReplicaManager() throws RemoteException {
        super(0);
        placesList.add("rmi://localhost:2025/placelist");
        placesList.add("rmi://localhost:2026/placelist");
        placesList.add("rmi://localhost:2027/placelist");
        /*RMIServer.main(new String[]{"2025"});
        RMIServer.main(new String[]{"2026"});
        RMIServer.main(new String[]{"2027"});*/
    }

    @Override
    public void addPlace(Place p) throws RemoteException {
        for (String url : placesList) {
            invokeObjectRegistry(p);
            addObjectReplica(p,url);
        }
    }

    @Override
    public String getPlaceListAddress(String objectID) throws RemoteException {
        if (!placesList.isEmpty()) {
            return placesList.get((int) (Math.random() * placesList.size()));
        }
        return null;
    }

    private void invokeObjectRegistry(Place p) {
        ObjectRegistryInterface o = null;
        try {
            o = (ObjectRegistryInterface) Naming.lookup("rmi://localhost:2023/registry");
            o.addRManager(p.getPostalCode(), "rmi://localhost:2024/replicamanager");
        } catch (MalformedURLException | RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    private void addObjectReplica(Place place, String url) {
        PlacesListInterface p = null;
        try {
            p = (PlacesListInterface) Naming.lookup(url);
            p.addPlace(place);
        } catch (MalformedURLException | RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    public String addReplica(String replicaAddress) throws RemoteException {
        String replica = getPlaceListAddress("");
        placesList.add(replicaAddress);
        return replica;
    }

    public void removeReplica(String replicaAddress) throws RemoteException {
        placesList.remove(replicaAddress);
    }
}