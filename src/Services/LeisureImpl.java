package Services;

import Exceptions.InvalidDiscountPriceException;
import Exceptions.InvalidTicketPriceException;

public class LeisureImpl extends ServiceAbs implements Leisure {
    int discount;

    public LeisureImpl(String name, long lat, long lon, int price, int discount)throws InvalidTicketPriceException, InvalidDiscountPriceException {
        super(name, lat, lon, price - (price * discount / 100), Services.ServiceType.LEISURE, discount);
        if (price <= 0) {
            throw new InvalidTicketPriceException();
        }
        if (discount < 0 || discount > 100) {
            throw new InvalidDiscountPriceException();
        }
        this.discount = discount;
    }

}