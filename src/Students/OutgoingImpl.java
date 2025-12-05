package Students;

import Exceptions.LodgingIsFullException;
import Services.Lodging;
import Services.Service;
public class OutgoingImpl extends StudentAbs implements Outgoing {
    public OutgoingImpl(String name, String country, Lodging home) throws LodgingIsFullException {
        super(name, country, home, StudentType.OUTGOING);
        this.visitedServices.addLast(home);
    }

    @Override
    public void registerVisit(Service service) {
        if (visitedServicesSet.get(service) == null) {
            visitedServices.addLast(service);
            visitedServicesSet.put(service, true);
        }
    }
}