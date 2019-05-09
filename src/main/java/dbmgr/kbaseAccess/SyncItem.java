package dbmgr.kbaseAccess;

public class SyncItem {

    private String area;
    private String lng;
    private String lat;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return "SyncItem{" +
                "area='" + area + '\'' +
                ", lng='" + lng + '\'' +
                ", lat='" + lat + '\'' +
                '}';
    }
}
