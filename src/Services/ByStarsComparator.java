package Services;

import dataStructures.Comparator;
import java.io.Serializable;

/**
 * A comparator for {@link Service} objects that sorts them based on their
 * average star rating in descending order (highest stars first).
 * This class is serializable.
 */
public class ByStarsComparator implements Comparator<Service>, Serializable {

    /**
     * Standard serial version UID for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Compares two {@link Service} objects for order.
     * <p>
     * The comparison `Integer.compare(service2.getAvgStar(), service1.getAvgStar())`
     * is used, which places the service with the higher star rating first.
     *
     * @param service1 The first service to be compared.
     * @param service2 The second service to be compared.
     * @return A negative integer, zero, or a positive integer.
     * By comparing service2 to service1, this method sorts
     * the services in **descending order** of their average star rating.
     */
    @Override
    public int compare(Service service1, Service service2) {
        // Compares service2 to service1 to achieve descending order (highest stars first)
        return Integer.compare(service2.getAvgStar(), service1.getAvgStar());
    }
}