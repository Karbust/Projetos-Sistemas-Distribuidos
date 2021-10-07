import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class ObjectRegistry extends UnicastRemoteObject implements ObjectRegistryInterface {
    private static final long serialVersionUID = 1L;
    private static final HashMap<String, String> servers = new HashMap<String, String>();

    public ObjectRegistry() throws RemoteException {
        super(0);
    }

    @Override
    public void addObject(String objectID, String serverAddress) throws RemoteException {
        servers.put(objectID, serverAddress);
    }

    @Override
    public String resolve(String objectID) throws RemoteException {
        return servers.get(objectID);
    }
}