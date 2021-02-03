public class messageOverhead {

    private Nachricht message;
    private Connection con;
    public messageOverhead(Connection con, Nachricht nachricht){
        this.message = nachricht;
        this.con = con;
    }

    public Nachricht getMessage() {
        return message;
    }

    public void setMessage(Nachricht message) {
        this.message = message;
    }

    public Connection getCon() {
        return con;
    }

    public void setCon(Connection con) {
        this.con = con;
    }
}
