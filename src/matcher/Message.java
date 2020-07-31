package matcher;

public class Message {
    private boolean gamiyole;
    private String email;
    private String startTime;
    private String endTime;
    private String time;
    private String destination;
    private String lat, lng;
    private int seats;
    private String content;

    public boolean isFromUni() {
        return fromUni;
    }

    public void setFromUni(boolean fromUni) {
        this.fromUni = fromUni;
    }

    private boolean fromUni;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isGamiyole() {
        return gamiyole;
    }

    public void setGamiyole(boolean gamiyole) {
        this.gamiyole = gamiyole;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "gamiyole: " + gamiyole + ", startTime: " + startTime + ", endTime: " + endTime
                + ", time: " + time + ", location: " + destination + ", seats: " + seats
                + ", lat: " + lat + ", lng: " + lng;
    }
}
