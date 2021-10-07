import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

class ReplicaManager extends UnicastRemoteObject implements PlacesListManagerInterface, ReplicasManagementInterface {
    // Attributes
    private static final long serialVersionUID = 1L;
    private static final ArrayList<String> placesList = new ArrayList<String>();

    // Constructor
    public ReplicaManager() throws RemoteException {
        super(0);
    }

    public ReplicaManager(String[] arrayURL) throws RemoteException {
        super(0);
        Collections.addAll(placesList, arrayURL);
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

    @Override
    public String addReplica(String replicaAddress) throws RemoteException {
        String replica = getPlaceListAddress("");
        placesList.add(replicaAddress);
        return replica;
    }

    @Override
    public void removeReplica(String replicaAddress) throws RemoteException {
        placesList.remove(replicaAddress);
    }
}