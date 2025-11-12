package Services;

/**
 * Interface defining the specific contract for Leisure services.
 * It extends the base {@link Service} interface, adding functionalities
 * related to student discounts on ticket prices.
 */
public interface Leisure extends Service {

    /**
     * Calculates and returns the final price for a student after
     * applying the student discount.
     *
     * @return The discounted ticket price.
     */
    int checkDiscountedPrice();
}