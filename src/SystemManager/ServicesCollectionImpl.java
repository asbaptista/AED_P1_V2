package SystemManager;

import Exceptions.ServiceAlreadyExistsException;
import Services.Service;
import Services.ServiceType;
import dataStructures.*;

import java.io.*;

public class ServicesCollectionImpl implements ServiceCollection, Serializable {

    private final List<Service> servicesByInsertion;
    private final Map<String, Service> servicesByName;
    private final List<Service>[] rankingByStars;
    private final Map<ServiceType, List<Service>[]> servicesByTypeAndStars;

    public ServicesCollectionImpl() {
        this.servicesByInsertion = new DoublyLinkedList<>();
        this.servicesByName = new ClosedHashTable<>();
        this.rankingByStars = createStarsArray();
        this.servicesByTypeAndStars = new SepChainHashTable<>();
    }

    private List<Service>[] createStarsArray() {
        @SuppressWarnings("unchecked")
        List<Service>[] array = (List<Service>[]) new List[5];
        for (int i = 0; i < 5; i++) {
            array[i] = new DoublyLinkedList<>();
        }
        return array;
    }

    @Override
    public void add(Service service)throws ServiceAlreadyExistsException {
        if (contains(service.getName())) {
            throw new ServiceAlreadyExistsException();
        }
        servicesByInsertion.addLast(service);
        servicesByName.put(service.getName().toLowerCase(),service);
        addServiceToRankingByStars(service);
        addServiceToTypeStarsMap(service);
    }

    @Override
    public void updateRankingByStars(Service service, int oldStars) {
        int newStars = service.getAvgStar();

        if (newStars == oldStars) {
            return;
        }

        removeServiceFromList(rankingByStars[oldStars - 1], service);
        addServiceToRankingByStars(service);

        ServiceType type = service.getType();
        List<Service>[] starsArray = servicesByTypeAndStars.get(type);

        if (starsArray != null) {
            removeServiceFromList(starsArray[oldStars - 1], service);
        }
        addServiceToTypeStarsMap(service);
    }

    private void removeServiceFromList(List<Service> list, Service service) {
        if (list != null) {
            int index = list.indexOf(service);
            if (index != -1) {
                list.remove(index);
            }
        }
    }

    private void addServiceToRankingByStars(Service service) {
        int stars = service.getAvgStar();
        rankingByStars[stars - 1].addLast(service);

    }

    private void addServiceToTypeStarsMap(Service service) {
        ServiceType type = service.getType();
        int stars = service.getAvgStar();

        List<Service>[] starsArray = servicesByTypeAndStars.get(type);
        if (starsArray == null) {
            starsArray = createStarsArray();
            servicesByTypeAndStars.put(type, starsArray);
        }

        starsArray[stars - 1].addLast(service);
    }

    @Override
    public Service findByName(String name) {
        return servicesByName.get(name.toLowerCase());
    }

    @Override
    public boolean contains(String name) {
        return findByName(name) != null;
    }

    @Override
    public int size() {
        return servicesByInsertion.size();
    }

    @Override
    public Iterator<Service> listServices() {
        return servicesByInsertion.iterator();
    }

    @Override
    public Iterator<Service> getServicesByStars() {
        return new ListsIterator<>(rankingByStars);
    }

    @Override
    public Iterator<Service> getServicesByTypeOrderedByStars(ServiceType type) {
        List<Service>[] starsArray = servicesByTypeAndStars.get(type);

        return new ListsIterator<>(starsArray);
    }

    @Override
    public boolean hasServicesOfType(ServiceType type) {
        List<Service>[] starsArray = servicesByTypeAndStars.get(type);
        if (starsArray == null) {
            return false;
        }
        for (List<Service> list : starsArray) {
            if (list != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<Service> getServicesByTypeAndStars(ServiceType type, int stars) {
        List<Service>[] starsArray = servicesByTypeAndStars.get(type);

        if (starsArray != null && starsArray[stars - 1] != null) {
            return starsArray[stars - 1].iterator();
        }

        return new DoublyLinkedList<Service>().iterator();
    }

    @Override
    public Iterator<Service> getServicesByTag(String tag) {
        return new FilterIterator<>(servicesByInsertion.iterator(),
            service -> service.hasEvaluationWithTag(tag));
    }


}

