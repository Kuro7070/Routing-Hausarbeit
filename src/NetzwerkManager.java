import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.RandomGenerator;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NetzwerkManager {


    int currentClick;

    List<RoutingPoint> rps;
    List<Connection> connections;
    private int events;
    private int averageDegree;
    Graph graph;

    private int nameMultiplicator = 1;
    private int nameCounter = 65;
    private int edgeIDCounter = 1;

    private int messageCounterPerRound = 0;
    private int messageGesamtCounter = 0;
    private int messageIndex = 1;
    private int maxMessagesPerClick;

    private int maxClicks;

    public NetzwerkManager(int maxClicks, int events, int averageDegree, int maxMessagesPerClick) {
        this.maxClicks = maxClicks;
        currentClick = 1;
        this.averageDegree = averageDegree;
        this.events = events;
        rps = new ArrayList<>();
        connections = new ArrayList<>();
        this.maxMessagesPerClick = maxMessagesPerClick;
    }


    public void start() {

        graph = randomGraph("test1", 5,7);
        String styleSheet =   "node {" +
                "	fill-color: red;" +
                "text-size: 20;"+
                "size: 15;"+
                "}"+
                "edge {" +
                "size: 2;"+
                "text-size: 15;"+
                "text-color: blue;" +
                "}";
        graph.setAttribute("ui.stylesheet", styleSheet);

        //graphVollstaendig();

       /* Generator gen = new RandomGenerator(averageDegree);
        gen.addSink(graph);
        gen.begin();
        for(int i=0; i<events; i++)
            gen.nextEvents();
        gen.end();*/

        graph.display();
        createNetzwerk();
        update();

    }

    public Graph randomGraph(String name, int nodes, int edges) {

        Graph graph = new SingleGraph(name);
        List<Node> addedNodes = new ArrayList<>();
        Random random = new Random();
        int alreadyAddedEdges = 0;


        if(nodes < 2 || edges < nodes-1){
            System.out.println("Knoten oder Kanten nicht genug oder zu viel");
            //throw new Exception();
        }



        //erzeugt alle nodes
        for (int i = 1; i < nodes; i++) {
           Node currentNode  = graph.addNode(createName());
            currentNode.setAttribute("ui.label",currentNode.getId());
        }

        List<Node> nodeList = new ArrayList<>(graph.getNodeSet());


        Node urNode = nodeList.get(0);
        addedNodes.add(urNode);

        //verküpft alle Nodes
        for (int i = 1; i < nodeList.size(); i++) {
            Node currentNode = nodeList.get(i);

            Node randomNode = addedNodes.get(random.nextInt(addedNodes.size()));
            addedNodes.add(currentNode);


            Edge currentEdge = graph.addEdge(Integer.toString(edgeIDCounter++), currentNode.getId(), randomNode.getId());
            currentEdge.setAttribute("ui.label", edgeIDCounter-1);
            alreadyAddedEdges++;

        }

        //restliche Edges hinzufügen
        for (int i = 0; i < alreadyAddedEdges; i++) {
            Node node1 = nodeList.get(random.nextInt(nodeList.size()));
            Node node2 = nodeList.get(random.nextInt(nodeList.size()));

            while(node1.getEdgeBetween(node2.getId()) != null || node1 == node2){
                 node1 = nodeList.get(random.nextInt(nodeList.size()));
                 node2 = nodeList.get(random.nextInt(nodeList.size()));
            }

            Edge currentEdge = graph.addEdge(Integer.toString(edgeIDCounter++), node1, node2);
            currentEdge.setAttribute("ui.label", edgeIDCounter-1);
        }

        return graph;
    }



    public void createNetzwerk() {

        List<Node> nodes = new ArrayList<Node>(graph.getNodeSet());
        List<Edge> edges = new ArrayList<Edge>(graph.getEdgeSet());

        for (Node node : nodes) {

            RoutingPoint point = new RoutingPoint(node.getId(), new ArrayList<>(), maxClicks,(maxMessagesPerClick*maxClicks));
            rps.add(point);

        }

        for (Edge edge : edges) {
            String node1Name = edge.getNode0().getId();
            String node2Name = edge.getNode1().getId();

            RoutingPoint point1  = rps.stream().filter( e -> e.getRoutingPointName().equals(node1Name)).findAny().orElse(null);
            RoutingPoint point2  = rps.stream().filter( e -> e.getRoutingPointName().equals(node2Name)).findAny().orElse(null);


            Random random = new Random();
            int randomKapazitaet = (random.nextInt(3-1)+1);


            Connection connection = new Connection(Integer.parseInt(edge.getId()), point1, point2, randomKapazitaet , 0);

            //System.out.println("conenction: " + connection.getConnectionID() + "point1: " + point1.getRoutingPointName() + " = " + node1Name + "point2: " + point2.getRoutingPointName() + " = " + node2Name);

            connections.add(connection);

            point1.addConnection(connection);
            point2.addConnection(connection);
        }

    }

    public void sendMessages(){

        Random random = new Random();

        for (int i = 0; i < maxMessagesPerClick; i++) {



            RoutingPoint start = rps.get(random.nextInt(rps.size()));


            RoutingPoint target = rps.get(random.nextInt(rps.size()));
            while(target.getRoutingPointName().equals(start.getRoutingPointName())) {
                target = rps.get(random.nextInt(rps.size()));

            }

            Nachricht message = new Nachricht(Integer.toString(messageIndex), target.getRoutingPointName(), start.getRoutingPointName(), start.getRoutingPointName());
            //( nachricht,  targetName,  startName,  LastRoutingPointName)

            start.addOutGoingMessage(new messageOverhead(null, message));
            messageCounterPerRound++;
            messageGesamtCounter++;
            messageIndex++;
        }
    }

    public String createName() {
        String name = "";
        char nameChar = (char) nameCounter;
        for (int i = 0; i < nameMultiplicator; i++) {
            name = name.concat(Character.toString(nameChar));
        }
        nameCounter++;
        if (nameCounter > 90) {
            nameCounter = 65;
            nameMultiplicator++;
        }

        return name;
    }


    public void update() {


        while (currentClick <= maxClicks) {
            sendMessages();


            for (RoutingPoint rp : rps) {
                rp.update();
            }

            for (Connection con : connections) {
                con.update();
            }

            messageCounterPerRound = 0;
            currentClick++;
            //System.out.println("------------------------------------------neuer click------------------------------------------");

            double durchschnittAuslastung = 0.0;

            for(RoutingPoint rp : rps){
                durchschnittAuslastung += rp.getCurrentAuslastung();
            }

            durchschnittAuslastung = (durchschnittAuslastung / rps.size());

            System.out.println(durchschnittAuslastung);

        }

        for (RoutingPoint rp : rps){
            System.out.println("RoutingName: " + rp.getRoutingPointName() + " - AverageAuslastung: " + customRound(rp.getAverageAuslastungRoutingPoint(),2));
            System.out.println(rp.getRoutingTabelle());
        }


        System.out.println();
        System.out.println(graph.getEdgeSet());



    }

    private double customRound(double value, int decimalPoints) {
        double d = Math.pow(10, decimalPoints);
        return Math.rint(value * d) / d;
    }

}
