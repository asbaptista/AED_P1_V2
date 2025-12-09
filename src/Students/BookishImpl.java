package Students;

import Exceptions.LodgingIsFullException;
import Services.Leisure;
import Services.Lodging;
import Services.Service;
public class BookishImpl extends StudentAbs implements Bookish {
    public BookishImpl(String name, String country, Lodging home) throws LodgingIsFullException {
        super(name, country, home, StudentType.BOOKISH);
    }

    @Override
    public void registerVisit(Service service) {
        if (service instanceof Leisure) {
            if (visitedServicesSet.get(service) == null) {
                visitedServices.addLast(service);
                visitedServicesSet. put(service, true);
            }
        }
    }

}