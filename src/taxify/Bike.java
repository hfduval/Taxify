package taxify;

public class Bike extends Vehicle {

    private ILocation userLocation;

    public Bike(int id, ILocation location) {
        super(id, location);
    }

    @Override
    public void pickService(IService service) {
        // pick a service, set destination to the service pickup location

        this.getServices().add(service);
        this.userLocation = service.getPickupLocation();
        this.setRoute(this.setDrivingRouteToDestination(userLocation, this.getLocation()));
    }

    @Override
    public void move() {
        // get the next location from the driving route

        if (this.getRoute().isEmpty()) {
            return;
        }

        if (this.getStatus() == VehicleStatus.BOOKED) {
            this.userLocation = this.getRoute().get(0);
        } else {
            this.setLocation(this.getRoute().get(0));
        }
        this.getRoute().remove(0);

        if (this.getRoute().isEmpty()) {

            if (this.getLocation().getX() == this.userLocation.getX() && this.getLocation().getY() == this.userLocation.getY()) {
                notifyArrivalAtPickupLocation();
            } else {
                notifyArrivalAtDropoffLocation();
            }

        }
    }

    @Override
    public double calculateCost(IService service) {
        return super.calculateCost(service) * 0.5;
    }

    @Override
    public String toString() {
        if (this.getStatus() == VehicleStatus.FREE) {
            return "Bike    " + this.getId() + " at " + this.getLocation() + " is free and ready to be booked";
        } else if (this.getStatus() == VehicleStatus.BOOKED) {
            return "Bike    " + this.getId() + " at " + this.getLocation() + " is being picked up by user " +
                    this.getServices().get(0).getUser().getId() + " who is at location " + this.userLocation;
        } else {
            return "Bike    " + super.toString();
        }
    }

}
