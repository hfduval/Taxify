package taxify;

import java.util.ArrayList;
import java.util.List;

public abstract class Vehicle implements IVehicle {
    private int id;
    private ITaxiCompany company;
    private ArrayList<IService> services;
    private VehicleStatus status;
    private ILocation location;
    private ILocation destination;
    private IStatistics statistics;
    private List<ILocation> route;

    public Vehicle(int id, ILocation location) {
        this.id = id;
        this.services = new ArrayList<>();
        this.status = VehicleStatus.FREE;
        this.location = location;
        this.destination = ApplicationLibrary.randomLocation(this.location);
        this.statistics = new Statistics();
        this.route = setDrivingRouteToDestination(this.location, this.destination);
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public ILocation getLocation() {
        return location;
    }

    @Override
    public ILocation getDestination() {
        return destination;
    }

    @Override
    public ArrayList<IService> getServices() {
        return services;
    }

    @Override
    public IStatistics getStatistics() {
        return statistics;
    }

    @Override
    public VehicleStatus getStatus() {
        return status;
    }

    @Override
    public List<ILocation> getRoute() {
        return route;
    }

    @Override
    public void setStatus(VehicleStatus status) {
        this.status = status;
    }

    @Override
    public void setCompany(ITaxiCompany company) {
        this.company = company;
    }

    @Override
    public void setRoute(List<ILocation> route) {
        this.route = route;
    }

    @Override
    public void setLocation(ILocation location) {
        this.location = location;
    }

    @Override
    public void pickService(IService service) {
        // pick a service, set destination to the service pickup location

        this.services.add(service);
        this.destination = service.getPickupLocation();
        this.route = setDrivingRouteToDestination(this.location, this.destination);
    }

    @Override
    public void startService() {

        if (status == VehicleStatus.PICKUP_RIDE_SHARE) {
            this.status = VehicleStatus.SERVICE_RIDE_SHARE;
        } else {
            this.status = VehicleStatus.SERVICE;
        }

        int prevDist = ApplicationLibrary.distance(this.location, services.get(0).getDropoffLocation());
        for (IService i : services) {
            int currDist = ApplicationLibrary.distance(this.location, i.getDropoffLocation());
            if (currDist <= prevDist) {
                this.destination = i.getDropoffLocation();
                prevDist = currDist;
            }
        }

        this.route = setDrivingRouteToDestination(this.location, this.destination);
        // set destination to the service drop-off location, update the driving route, set status to "service"
    }

    @Override
    public void endService() {
        IService service = this.services.get(0);
        VehicleStatus prevStatus = status;
        // update vehicle statistics
        if (prevStatus == VehicleStatus.SERVICE_RIDE_SHARE) {
            if (this.location == this.services.get(0).getDropoffLocation()) {
                this.services.set(0, this.services.get(1));
            } else {
                service = this.services.get(1);
            }
            this.destination = services.get(0).getDropoffLocation();
            this.route = setDrivingRouteToDestination(this.location, this.destination);
            this.services.remove(this.services.get(1));
            setStatus(VehicleStatus.SERVICE);
        }

        this.statistics.updateBilling(this.calculateCost(service));
        this.statistics.updateDistance(service.calculateDistance());
        this.statistics.updateServices();

        // if the service is rated by the user, update statistics

        if (this.services.get(0).getStars() != 0) {
            this.statistics.updateStars(service.getStars());
            this.statistics.updateReviews();
        }

        if (prevStatus == VehicleStatus.SERVICE) {
            // set service to null, and status to "free"

            this.services.remove(0);
            this.destination = ApplicationLibrary.randomLocation(this.location);
            this.route = setDrivingRouteToDestination(this.location, this.destination);
            this.status = VehicleStatus.FREE;
        }
    }

    @Override
    public void notifyArrivalAtPickupLocation() {
        this.company.arrivedAtPickupLocation(this);
        this.startService();
        // notify the company that the vehicle is at the pickup location and start the service
    }

    @Override
    public void notifyArrivalAtDropoffLocation() {
        this.company.arrivedAtDropoffLocation(this);
        this.endService();
    }

    @Override
    public boolean isFree() {
        return this.status == VehicleStatus.FREE;
        // returns true if the status of the vehicle is "free" and false otherwise
    }

    @Override
    public boolean isInService() {
        return this.status == VehicleStatus.SERVICE;
        // returns true if the status of the vehicle is "SERVICE" and false otherwise
    }

    @Override
    public void move() {
        // get the next location from the driving route

        if (this.route.isEmpty()) {
            this.destination = ApplicationLibrary.randomLocation(this.location);
            this.route = setDrivingRouteToDestination(this.location, this.destination);
        }

        this.location = this.route.get(0);
        this.route.remove(0);

        if (this.route.isEmpty()) {
            if (this.services.isEmpty()) {
                // the vehicle continues its random route

                this.destination = ApplicationLibrary.randomLocation(this.location);
                this.route = setDrivingRouteToDestination(this.location, this.destination);
            }
            else {
                // checks if the vehicle has arrived to a pickup or drop off location

                ILocation firstPickup = this.services.get(0).getPickupLocation();
                ILocation firstDestination = this.services.get(0).getDropoffLocation();

                if (this.location.getX() == firstPickup.getX() && this.location.getY() == firstPickup.getY()) {

                    notifyArrivalAtPickupLocation();

                } else if (this.location.getX() == firstDestination.getX() && this.location.getY() == firstDestination.getY()) {

                    notifyArrivalAtDropoffLocation();

                } else if (this.status == VehicleStatus.PICKUP_RIDE_SHARE || this.status == VehicleStatus.SERVICE_RIDE_SHARE) {
                    ILocation secondPickup = this.services.get(1).getPickupLocation();
                    ILocation secondDestination = this.services.get(1).getDropoffLocation();
                    if (this.location.getX() == secondPickup.getX() && this.location.getY() == secondPickup.getY()) {

                        notifyArrivalAtPickupLocation();

                    } else if (this.location.getX() == secondDestination.getX() && this.location.getY() == secondDestination.getY()) {

                        notifyArrivalAtDropoffLocation();

                    }
                }
            }
        }
    }

    @Override
    public double calculateCost(IService service) {
        return service.calculateDistance();
        // returns the cost of the service as the distance
    }

    @Override
    public String showDrivingRoute() {
        String s = "";

        for (ILocation l : this.route)
            s = s + l.toString() + " ";

        return s;
    }

    @Override
    public String toString() {
        return this.id + " at " + this.location + " driving to " + this.destination +
                ((this.status == VehicleStatus.FREE) ? " is free with path " + showDrivingRoute(): ((this.status == VehicleStatus.PICKUP) ? " to pickup user " +
                        this.services.get(0).getUser().getId() : (this.status == VehicleStatus.PICKUP_RIDE_SHARE) ? " to pickup ride sharing user " +
                        this.services.get(1).getUser().getId() : (this.status == VehicleStatus.SERVICE_RIDE_SHARE ? " is ride sharing" : " isn't ride sharing")));
    }

    @Override
    public List<ILocation> setDrivingRouteToDestination(ILocation location, ILocation destination) {
        List<ILocation> route = new ArrayList<ILocation>();

        int x1 = location.getX();
        int y1 = location.getY();

        int x2 = destination.getX();
        int y2 = destination.getY();

        int dx = Math.abs(x1 - x2);
        int dy = Math.abs(y1 - y2);

        for (int i=1; i<=dx; i++) {
            x1 = (x1 < x2) ? x1 + 1 : x1 - 1;

            route.add(new Location(x1, y1));
        }

        for (int i=1; i<=dy; i++) {
            y1 = (y1 < y2) ? y1 + 1 : y1 - 1;

            route.add(new Location(x1, y1));
        }

        return route;
    }
}