package taxify;

import java.util.ArrayList;
import java.util.List;

public interface IVehicle {

    public int getId();
    public ILocation getLocation();
    public ILocation getDestination();
    public ArrayList<IService> getServices();
    public IStatistics getStatistics();
    public VehicleStatus getStatus();
    public List<ILocation> getRoute();
    public void setStatus(VehicleStatus status);
    public void setCompany(ITaxiCompany company);
    public void setRoute(List<ILocation> route);
    public void setLocation(ILocation location);
    public void pickService(IService service);
    public void startService();
    public void endService();
    public void notifyArrivalAtPickupLocation();
    public void notifyArrivalAtDropoffLocation();
    public boolean isFree();
    public boolean isInService();
    public void move();
    public double calculateCost(IService service);
    public String showDrivingRoute();
    public String toString();
    public List<ILocation> setDrivingRouteToDestination(ILocation location, ILocation destination);

}