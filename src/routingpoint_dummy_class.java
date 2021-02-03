import org.graphstream.ui.j2dviewer.renderer.shape.swing.CircleOnEdge;
import scala.Int;

import java.util.*;

public class routingpoint_dummy_class {
}/*
   import org.graphstream.ui.j2dviewer.renderer.shape.swing.CircleOnEdge;
import scala.Int;

import java.util.*;

public class RoutingPoint {

    private Queue<messageOverhead> incommingMessages;
    private Queue<messageOverhead> outgoingMessages;
    private Queue<messageOverhead> nochzusenden;

    private List<Connection> connections;

    private String routingPointName;
    private HashMap<String, RoutingEintrag> routingTabelle;
    //Hasmap{RoutingName -> RoutringEintrag}

    private List<Nachricht> alleErhaltenenNachrichten = new ArrayList<>();

    private double currentAuslastung;

    private List<Double> averageAuslastung;
    private int maxClicks;
    private int allMessages;


    public RoutingPoint(String name, List<Connection> connections, int maxClicks, int allMessages) {
        this.routingPointName = name;
        this.connections = connections;
        incommingMessages = new LinkedList<>();
        outgoingMessages = new LinkedList<>();
        nochzusenden = new LinkedList<>();

        routingTabelle = new HashMap<>();
        averageAuslastung = new ArrayList<>();
        this.maxClicks = maxClicks;
        this.allMessages = allMessages;
    }

    public void update() {

        currentAuslastung = ((double) (100.0 / allMessages) * outgoingMessages.size());

        averageAuslastung.add(currentAuslastung);


        //bearbeite alle Nachricht aus incommingMesages
        //füge diese outgoingMessages hinzu
        editInCommingMessages();

        //sende alle möglichen Nachricht aus outgoingMessages raus
        sendOutgoingMessages();

    }

    public void addConnection(Connection connection) {
        connections.add(connection);
    }

    public double getAverageAuslastungRoutingPoint() {
        return average(averageAuslastung);
    }

    public HashMap<String, RoutingEintrag> getRoutingTabelle() {
        return routingTabelle;
    }

    public void sendOutgoingMessages() {

        if(nochzusenden.size() > 0 ){
            System.out.println("Wird hinzugefügt");
            for (Nachricht n : nochzusenden){
                outgoingMessages.add(n);

            }
            nochzusenden = new LinkedList<>();
        }


        while (outgoingMessages.size() > 0 ) {


            Nachricht message = outgoingMessages.poll();

            message.setNachrichtenDauer(message.getNachrichtenDauer() + 1);

            if (routingTabelle.containsKey(message.getTargetName())) {
                //Ziel bekannt, bestimmter Socket raussenden

                int outConnectionID = getMaxConnection(routingTabelle.get(message.getTargetName()));


                Connection outConenction = connections.stream().filter(e -> e.getConnectionID() == outConnectionID).findAny().orElse(null);

                if (outConenction != null) {
                    // prüfen, ob genug Platz auf der Connection, falls nicht Message zurück in die OutgoingList
                    if (outConenction.getCurrentKapazitaet() > 0) {
                        outConenction.addNachricht(message);

                    } else {
                        //outgoingMessages.add(message);
                        nochzusenden.add(message);
                        System.out.println("Kapazität ist gerade ausgelastet");
                    }
                } else {
                    System.out.println("Connection nicht gefunden Zeile: 70");
                }
                message.setLastRoutingPointName(getRoutingPointName());
            } else {
                //Nachricht an alle Nachbarn raussenden



                if (message.getLastRoutingName().equals(message.getStartName())) {
                    for (Connection con : connections) {

                        if (con.getCurrentKapazitaet() > 0) {
                            con.addNachricht(message);


                        } else {
                            //System.out.println("kapazität nicht ausreichend");
                        }
                    }
                } else {
                    for (Connection con : connections) {


                        if (con.getCurrentKapazitaet() > 0 &&
                                !(con.getPoint1().getRoutingPointName().equals(message.getLastRoutingName())
                                        || con.getPoint2().getRoutingPointName().equals(message.getLastRoutingName()))) {
                            con.addNachricht(message);


                        } else {
                            System.out.println("kapazität nicht ausreichend oder rückfälliger weg");
                        }
                    }
                }
                message.setLastRoutingPointName(getRoutingPointName());

            }


        }
    }

    public int getMaxConnection(RoutingEintrag eintrag) {

        return eintrag.getMaxConnection();
    }

    public void addOutGoingMessage(Nachricht message) {
        outgoingMessages.add(message);
    }

    public double average(List<Double> list) {
        double summe = 0.0;
        for (Double elem : list) {
            summe += elem;
        }
        return (summe / list.size());
    }

    public void editInCommingMessages() {

        System.out.println(incommingMessages.size());
        if (incommingMessages.size() > 0) {


            Nachricht message = incommingMessages.poll();


            editRoutingTable(message);

            if (message.getTargetName().equals(routingPointName)) {

                alleErhaltenenNachrichten.add(message);

            } else {
                //Nachricht weiterleiten

                //letzterRoutingPoint auf aktuellen wechseln


                outgoingMessages.add(message);
            }
        }
    }


    public void editRoutingTable(Nachricht message) {


        Connection connectionMessageComesIn = message.getCurrentConnection();

       /* for (Connection con : connections) {
            if (con.getPoint1().getRoutingPointName().equals(message.getLastRoutingName()) || con.getPoint2().getRoutingPointName().equals(message.getLastRoutingName())) {
                connectionMessageComesIn = con;
            }
        }*/

//neue Daten die durch Message erhalten worden sind, in die Routingtabelle eintragen
   /*     if (connectionMessageComesIn != null) {

                if (!message.getStartName().equals(routingPointName)) {
                if (routingTabelle.containsKey(message.getStartName())) {
                //enthält key bereits

                routingTabelle.get(message.getStartName()).putEintrag(connectionMessageComesIn.getConnectionID(), ((double) (100.0 / maxClicks) * message.getNachrichtenDauer()));
                } else {
                //enthält Key noch nicht


                List<Double> tempList = new ArrayList<>();
        tempList.add(((double) (100.0 / maxClicks) * message.getNachrichtenDauer()));


        RoutingEintrag eintrag = new RoutingEintrag(connectionMessageComesIn.getConnectionID(), tempList);
        routingTabelle.put(message.getStartName(), eintrag);

        }
        }
        //System.out.println(routingTabelle);

        } else {
        System.out.println("Connection nicht gefunden");
        }

        }

// neue nachricht incomming message hinzufügen
public void addIncommingMessage(Nachricht nachricht) {
        incommingMessages.add(nachricht);

        }

public String getRoutingPointName() {
        return routingPointName;
        }
        }

        */