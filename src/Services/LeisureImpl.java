package Services;

import Students.Student;

import java.io.Serializable;

/**
 * Implementation of the {@link Leisure} service.
 * Represents a leisure or cultural service (e.g., theater, cinema) that offers
 * a student discount. This class is serializable.
 */
public class LeisureImpl extends ServiceAbs implements Leisure {
    /**
     * The student discount percentage (e.g., 20 for 20%).
     */
    int discount;

    /**
     * Constructs a new Leisure service.
     * The price stored in the base class is the *already discounted* price.
     *
     * @param name     The name of the leisure service.
     * @param lat      The latitude coordinate.
     * @param lon      The longitude coordinate.
     * @param price    The full (non-discounted) ticket price.
     * @param discount The student discount percentage (0-100).
     */
    public LeisureImpl(String name, long lat, long lon, int price, int discount) {
        super(name, lat, lon, price - (price * discount / 100), Services.ServiceType.LEISURE, discount);
        this.discount = discount;
    }

}