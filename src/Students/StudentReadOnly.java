package Students;

import Services.LodgingReadOnly;
import Services.ServiceReadOnly;
import dataStructures.Iterator;

public interface StudentReadOnly {

    String getName();
    String getCountry();
    LodgingReadOnly getHome();
    ServiceReadOnly getCurrent();
    StudentType getType();
    Iterator<?  extends ServiceReadOnly> getVisitedIterator();

}
