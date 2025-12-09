package Students;
import Exceptions.LodgingIsFullException;
import Services.*;
import dataStructures.Iterator;

public class ThriftyImpl extends StudentAbs implements Thrifty {

    private Eating cheapestEating;
    private Lodging cheapestLodging;

    public ThriftyImpl(String name, String country, Lodging home) throws LodgingIsFullException {
        super(name, country, home, StudentType.THRIFTY);
        this.cheapestLodging = home;
        this.cheapestEating = null;
    }

    @Override
    protected void registerVisit(Service service) {
    }

    @Override
    public void visitEating(Eating eating) {
        if (cheapestEating == null || eating.getPrice() < cheapestEating.getPrice()) {
            cheapestEating = eating;
        }
    }

    @Override
    public void updateCheapestLodging(Lodging lodging) {
        cheapestLodging = lodging;
    }

    @Override
    public boolean canMoveTo(Lodging newHome) {
        return newHome.getPrice() < cheapestLodging.getPrice();
    }

    @Override
    public boolean isDistracted(Eating eating) {
        return cheapestEating != null && eating.getPrice() > cheapestEating.getPrice();
    }

    @Override
    public Service findMostRelevant(Iterator<Service> services) {
        Service cheapestService = null;

        while (services.hasNext()) {
            Service current = services.next();

            if (cheapestService == null) {
                cheapestService = current;
            } else if (current.getPrice() < cheapestService.getPrice()) {
                // Found a cheaper service
                cheapestService = current;
            }
        }
        return cheapestService;
    }

}