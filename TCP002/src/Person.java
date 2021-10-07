import java.io.Serializable;

public class Person implements Serializable {
    private String name;
    private int year;
    private Place place;

    public Person(String name, Place place, int year) {
        this.name = name;
        this.year = year;
        this.place = place;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }
}