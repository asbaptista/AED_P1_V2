import Services.ServiceReadOnly;
import Students.StudentReadOnly;
import dataStructures.Iterator;
import Services.ServiceType;

public interface AreaReadOnly {

    String getName();
    long getTopLat();
    long getLeftLong();
    long getBottomLat();
    long getRightLong();
    int getNumberOfServices();
    boolean isWithinBounds(long lat, long lon);
    boolean containsService(String name);
    ServiceReadOnly getService(String name);
    Iterator<? extends ServiceReadOnly> getServices();
    Iterator<? extends ServiceReadOnly> getRankedServices();
    Iterator<? extends  ServiceReadOnly> getServicesByTypeAndStars(ServiceType type, int stars);
    Iterator<? extends ServiceReadOnly> getServicesByTypeOrderedByStars(ServiceType type);
    boolean hasServicesOfType(ServiceType type);
    StudentReadOnly getStudent(String name);
    Iterator<? extends StudentReadOnly> listAllStudents();
    Iterator<? extends StudentReadOnly> listStudentsByCountry(String country);
}
