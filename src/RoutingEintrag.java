import scala.Int;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RoutingEintrag {

    //{socket, Liste
    //socket, Liste
    //socket, Liste}

    HashMap<Integer, List<Double>> eintragsVerzeichnis;


    public RoutingEintrag(int socket, List<Double> probability) {
        eintragsVerzeichnis = new HashMap<Integer, List<Double>>();
        eintragsVerzeichnis.put(socket, probability);
    }

    public void putEintrag(int socket, double probability) {
        if (eintragsVerzeichnis.containsKey(socket)) {
            eintragsVerzeichnis.get(socket).add(probability);
        } else {
            List<Double> tempList = new ArrayList<>();
            tempList.add(probability);
            eintragsVerzeichnis.put(socket, tempList);
        }
    }

    public HashMap<Integer, List<Double>> getMap() {
        return eintragsVerzeichnis;
    }


    public boolean containsKey(int key) {
        return eintragsVerzeichnis.containsKey(key);
    }

    @Override
    public String toString() {

        HashMap<Integer, Double> tempMapAusgabe = new HashMap<>();

        for (Integer key : eintragsVerzeichnis.keySet()) {
            double value = average(eintragsVerzeichnis.get(key));
            tempMapAusgabe.put(key, value);
        }

        return tempMapAusgabe.toString();


    }


    public List<Double> getValue(int key) {
        return eintragsVerzeichnis.get(key);
    }

    public int getMaxConnection() {

        double[][] max = new double[1][2];
        max[0][0] = 0;
        max[0][1] = 0;

        for (Integer key : eintragsVerzeichnis.keySet()) {
            double value = average(eintragsVerzeichnis.get(key));

            if(max[0][1] > value){
                max[0][0] = key;
                max[0][1] = value;
            }

        }


        return (int)max[0][0];
    }

    public double average(List<Double> list) {
        double summe = 0.0;
        for (Double elem : list) {
            summe += elem;
        }
        return customRound((summe / list.size()), 2);
    }

    private double customRound(double value, int decimalPoints) {
        double d = Math.pow(10, decimalPoints);
        return Math.rint(value * d) / d;
    }

}
