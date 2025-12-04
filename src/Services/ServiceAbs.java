package Services;

import dataStructures.*;

import java.io.*;

/**
 * Abstract base class implementing the {@link Service} interface.
 * Provides core functionality for all services, including name, location, price,
 * and management of user evaluations (reviews and star ratings).
 * This class is serializable.
 */
public class ServiceAbs implements Service, Serializable {

    /**
     * The official name of the service.
     */
    String name;

    /**
     * The geographic latitude of the service, stored as a long.
     */
    long lat;

    /**
     * The geographic longitude of the service, stored as a long.
     */
    long lon;

    /**
     * The base price of the service (e.g., menu price, room price, ticket price).
     */
    int price;

    /**
     * The internal average star rating, stored as a double for precision.
     */
    double avgStar;

    /**
     * A service-specific value.
     * Represents capacity for Eating/Lodging services and discount percentage for Leisure services.
     */
    int value;

    /**
     * The total number of evaluations this service has received.
     */
    int nEval;

    /**
     * The type of the service (e.g., EATING, LODGING, LEISURE).
     */
    Services.ServiceType type;

    /**
     * A list of all {@link Evaluation} objects submitted for this service.
     */
    private final TwoWayList<Evaluation> evaluations;

    /**
     * Initializes a new service.
     * Sets all service attributes and automatically adds an initial 4-star review
     * as required by the system specification.
     *
     * @param name  The name of the service.
     * @param lat   The latitude coordinate.
     * @param lon   The longitude coordinate.
     * @param price The base price of the service.
     * @param type  The {@link ServiceType} (EATING, LODGING, or LEISURE).
     * @param value The service-specific value (capacity or discount percentage).
     */
    public ServiceAbs(String name, long lat, long lon, int price, Services.ServiceType type, int value) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.price = price;
        this.type = type;
        this.value = value;
        this.avgStar = 0.0;
        this.nEval = 0;
        this.evaluations = new DoublyLinkedList<>();
        addReview(4, "");
    }
    /**
     * Gets the name of the service.
     *
     * @return The service's name.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Gets the latitude coordinate of the service.
     *
     * @return The service's latitude.
     */
    @Override
    public long getLat() {
        return lat;
    }

    /**
     * Gets the longitude coordinate of the service.
     *
     * @return The service's longitude.
     */
    @Override
    public long getLon() {
        return lon;
    }

    /**
     * Gets the latitude coordinate of the service.
     * (Alias for {@link #getLat()}).
     *
     * @return The service's latitude.
     */
    @Override
    public long getLatitude() {
        return lat;
    }

    /**
     * Gets the longitude coordinate of the service.
     * (Alias for {@link #getLon()}).
     *
     * @return The service's longitude.
     */
    @Override
    public long getLongitude() {
        return lon;
    }

    /**
     * Gets the base price of the service.
     *
     * @return The service's base price.
     */
    @Override
    public int getPrice() {
        return price;
    }

    /**
     * Gets the average star rating, rounded to the nearest integer.
     *
     * @return The rounded integer average star rating.
     */
    @Override
    public int getAvgStar() {
        return (int) Math.round(avgStar);
    }

    /**
     * Gets the type of the service.
     *
     * @return The {@link ServiceType} of the service.
     */
    @Override
    public Services.ServiceType getType() {
        return type;
    }


    /**
     * Adds a new user evaluation (review) to this service.
     * Creates a new {@link Evaluation} object, adds it to the list,
     * and triggers an update of the average star rating.
     *
     * @param rating  The star rating (1-5).
     * @param comment The text comment for the review.
     */
    @Override
    public void addReview(int rating, String comment) {
        Evaluation evaluation = new EvaluationImpl(rating, comment.toLowerCase());
        evaluations.addLast(evaluation);
        nEval++;
        updateStars(rating);
    }

    /**
     * Updates the internal average star rating with a new rating.
     * Recalculates the weighted average based on the new total number of evaluations.
     *
     * @param stars The new star rating to include in the average.
     */
    @Override
    public void updateStars(int stars) {
        avgStar = ((avgStar * (nEval - 1)) + stars) / nEval;
    }

    /**
     * Checks if any evaluation for this service contains a specific tag (word).
     * The search is case-insensitive and iterates through all review descriptions.
     *
     * @param tag The tag (word) to search for in the evaluation descriptions.
     * @return true if at least one evaluation contains the tag, false otherwise.
     */
    @Override
    public boolean hasEvaluationWithTag(String tag) {
        Iterator<Evaluation> it = evaluations.iterator();
        while (it.hasNext()) {
            Evaluation eval = it.next();
            if (eval.containsTag(tag)) {
                return true;
            }
        }
        return false;
    }


}