package seminars.domain.satellite;

public class SatelliteState {

    private boolean active;
    private String statusMessage = "Не активирован";

    public void activate() {
        active = true;
        statusMessage = "Активен";
    }

    public void deactivate(String reason) {
        active = false;
        statusMessage = reason;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public String toString() {
        return "SatelliteState{" +
                "isActive=" + active +
                ", statusMessage='" + statusMessage + '\'' +
                '}';
    }
}
