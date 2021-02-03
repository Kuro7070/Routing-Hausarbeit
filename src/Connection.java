import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Connection {

    private LimitedQueue<Nachricht> nachrichtenSpeicher;

    private RoutingPoint point1;
    private RoutingPoint point2;


    private int kapazitaet;

    private int connectionID;

    public Connection(int id, RoutingPoint point1, RoutingPoint point2, int kapazitaet, int fehlerrate) {
        nachrichtenSpeicher = new LimitedQueue(kapazitaet);
        this.connectionID = id;
        this.point1 = point1;
        this.point2 = point2;
        this.kapazitaet = kapazitaet;
    }

    public void update() {

        while (nachrichtenSpeicher.size() > 0) {
            Nachricht currentMessage = nachrichtenSpeicher.poll();
            RoutingPoint target = null;
            if (currentMessage.getLastRoutingName().equals(point1.getRoutingPointName())) {

                target = point2;
            } else{
                target = point1;


            }
//String test = null;
            //if(currentMessage.getCurrentConnection() != null)test= Integer.toString(currentMessage.getCurrentConnection().getConnectionID());
            //System.out.println("lastroutingpoint: " + currentMessage.getLastRoutingName() + " targe: " + target.getRoutingPointName() + " conn: " + test + " point1: " + point1.getRoutingPointName() + " point2: " + point2.getRoutingPointName());

            //System.out.println("last routing: " + currentMessage.getLastRoutingName() + " currentconenciton: " + getConnectionID());
            //currentMessage.setCurrentConnection(this);
            target.addIncommingMessage(this, currentMessage);

        }


    }

    public RoutingPoint getPoint1() {
        return point1;
    }

    public RoutingPoint getPoint2() {
        return point2;
    }

    public int getConnectionID() {
        return connectionID;
    }

    public int getCurrentKapazitaet() {
        return (kapazitaet - nachrichtenSpeicher.size());
    }

    public boolean addNachricht(Nachricht nachricht) {
        return nachrichtenSpeicher.add(nachricht);

    }
}
