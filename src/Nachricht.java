public class Nachricht {


    private String nachricht;
    private String targetName;
    private String startName;
    private String LastRoutingPointName;
    private int nachrichtenDauer;

    private Connection currentConnection = null;

    public Nachricht(String nachricht, String targetName, String startName, String LastRoutingPointName) {
        this.LastRoutingPointName = LastRoutingPointName;
        this.nachricht = nachricht;
        this.startName = startName;
        this.targetName = targetName;
        nachrichtenDauer = 0;
    }

    public Connection getCurrentConnection() {
        return currentConnection;
    }

    public void setCurrentConnection(Connection currentConnection) {
        this.currentConnection = currentConnection;
    }

    public int getNachrichtenDauer() {
        return nachrichtenDauer;
    }

    public void setNachrichtenDauer(int nachrichtenDauer) {
        this.nachrichtenDauer = nachrichtenDauer;
    }

    public String getNachricht() {
        return nachricht;
    }

    public void setNachricht(String nachricht) {
        this.nachricht = nachricht;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getStartName() {
        return startName;
    }

    public void setStartName(String startName) {
        this.startName = startName;
    }

    public String getLastRoutingName() {
        return LastRoutingPointName;
    }

    public void setLastRoutingPointName(String LastRoutingPointName) {
        this.LastRoutingPointName = LastRoutingPointName;
    }
}
