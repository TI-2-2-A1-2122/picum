package nl.ags.picum.dataStorage.dataUtil;

public class Point {
    private float longitude;
    private float latitude;
    private String id;


    public Point(float longitude, float latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.id = "no-id";
    }

    public Point(float longitude, float latitude, String id) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.id = id;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Point{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                ", id='" + id + '\'' +
                '}';
    }
}
