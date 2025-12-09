package Services;

import dataStructures.*;
import java.io.*;

public abstract class ServiceAbs implements Service, Serializable {

    private final String name;

    private final long lat;

    private final long lon;

    private final int price;

    private double avgStar;

    protected final int value;


    private final Services.ServiceType type;

    private final TwoWayList<Evaluation> evaluations;

    public ServiceAbs(String name, long lat, long lon, int price, Services.ServiceType type, int value) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.price = price;
        this.type = type;
        this.avgStar = 0.0;
        this.value = value;
        this.evaluations = new DoublyLinkedList<>();
        addReview(4, "");
    }
    @Override
    public String getName() {
        return name;
    }


    @Override
    public long getLatitude() {
        return lat;
    }

    @Override
    public long getLongitude() {
        return lon;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public int getAvgStar() {
        return (int) Math.round(avgStar);
    }

    @Override
    public Services.ServiceType getType() {
        return type;
    }


    @Override
    public void addReview(int rating, String comment) {
        Evaluation evaluation = new EvaluationImpl(rating, comment.toLowerCase());
        evaluations.addLast(evaluation);
        updateStars(rating);
    }

    @Override
    public void updateStars(int stars) {
        int totalEvaluations = evaluations.size();
        avgStar = ((avgStar * (totalEvaluations - 1)) + stars) / totalEvaluations;
    }

    @Override
    public boolean hasEvaluationWithTag(String tag) {
        Iterator<Evaluation> it = evaluations.iterator();
        while (it.hasNext()) {
            if (it.next().containsTag(tag)) {
                return true;
            }
        }
        return false;
    }


}