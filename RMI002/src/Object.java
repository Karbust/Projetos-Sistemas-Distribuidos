import java.io.Serializable;

public class Object implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer objectID;
    private String serverAddress;

    public Object(Integer objectID, String serverAddress) {
        this.objectID = objectID;
        this.serverAddress = serverAddress;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }
}
