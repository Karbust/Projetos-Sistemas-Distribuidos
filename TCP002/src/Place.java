import java.io.Serializable;

public class Place implements Serializable {
    private String postalCode;
    private String locality;

    public Place(String postalCode, String locality) {
        this.postalCode = postalCode;
        this.locality = locality;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getLocality() {
        return locality;
    }
}
