package Services;

public interface ServiceReadOnly {

    String getName();
    long getLat();
    long getLon();
    long getLatitude();
    long getLongitude();
    int getPrice();
    int getAvgStar();
    ServiceType getType();
    boolean hasEvaluationWithTag(String tag);

}
