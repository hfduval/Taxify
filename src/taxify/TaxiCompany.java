package taxify;

import java.util.ArrayList;
import java.util.List;

public class TaxiCompany implements ITaxiCompany, ISubject {
    private String name;
    private List<IUser> users;
    private List<IVehicle> vehicles;
    private int totalServices;
    private IObserver observer;

    public TaxiCompany(String name, List<IUser> users, List<IVehicle> vehicles) {
        this.name = name;
        this.users = users;
        this.vehicles = vehicles;
        this.totalServices = 0;

        // set the taxi company for users and vehicles

        for (int i=0; i<this.users.size(); i++)
            this.users.get(i).setCompany(this);

        for (int i=0; i<this.vehicles.size(); i++)
            this.vehicles.get(i).setCompany(this);
    }

    @Override
    public String getName() {
        // returns the name of the company
        return name;
    }

    @Override
    public int getTotalServices() {
        // returns the total services
        return totalServices;
    }

    @Override
    public boolean provideService(int user) {
        int userIndex = indexOfUserId(user);
        int freeVehicleIndex = findFreeVehicle();
        int rideShareVehicleIndex = findRideShareVehicle();
        ILocation[] endPoints = new ILocation[2];

        int vehicleIndex = findRideType(userIndex, freeVehicleIndex, rideShareVehicleIndex, endPoints);

        if (vehicleIndex != -1) {

                // update the user status

                this.users.get(userIndex).setService(true);

                // create a service with the user, the pickup and the drop-off location

                IService service;
                service = new Service(this.users.get(userIndex), endPoints[0], endPoints[1]);

                // assign the new service to the vehicle

                this.vehicles.get(vehicleIndex).pickService(service);
                notifyObserver("User " + this.users.get(userIndex).getId() + " requests a service from " + service.toString() + ", the ride is assigned to " +
                        this.vehicles.get(vehicleIndex).getClass().getSimpleName() + " " + this.vehicles.get(vehicleIndex).getId() + " at location " +
                        this.vehicles.get(vehicleIndex).getLocation().toString());

                // update the counter of services

                this.totalServices++;

                return true;

            }

            return false;
    }

    public int findRideType(int userIndex, int freeVehicleIndex, int rideShareVehicleIndex, ILocation[] endPoints) {
        boolean rideShare = isRideShare(rideShareVehicleIndex, endPoints);

        // if there is no free vehicle and ride-sharing is unavailable
        if (!rideShare && freeVehicleIndex == -1) {
            return -1;
        }

        // if there is a free vehicle, assign a random pickup and drop-off location to the new service
        // the distance between the pickup and the drop-off location should be at least 3 blocks
        if (freeVehicleIndex != -1 && !rideShare) {
            do {

                endPoints[0] = ApplicationLibrary.randomLocation();
                endPoints[1] = ApplicationLibrary.randomLocation(endPoints[0]);

            } while (ApplicationLibrary.distance(endPoints[0], this.vehicles.get(freeVehicleIndex).getLocation()) < 3);
            if (this.vehicles.get(freeVehicleIndex).getClass() == Bike.class || this.vehicles.get(freeVehicleIndex).getClass() == Scooter.class) {
                this.vehicles.get(freeVehicleIndex).setStatus(VehicleStatus.BOOKED);
            } else {
                this.vehicles.get(freeVehicleIndex).setStatus(VehicleStatus.PICKUP);
            }
            return freeVehicleIndex;
        }
        return rideShareVehicleIndex;
    }

    @Override
    public void arrivedAtPickupLocation(IVehicle vehicle) {
        // a vehicle arrives at the pickup location

        notifyObserver(String.format("%-8s",vehicle.getClass().getSimpleName()) + vehicle.getId() + " loads user " +
                ((vehicle.getStatus() == VehicleStatus.PICKUP_RIDE_SHARE) ?
                        vehicle.getServices().get(1).getUser().getId() : vehicle.getServices().get(0).getUser().getId()));
    }

    @Override
    public void arrivedAtDropoffLocation(IVehicle vehicle) {
        // a vehicle arrives at the drop-off location

        IService service;

        if (vehicle.getStatus() == VehicleStatus.SERVICE) {
           service = vehicle.getServices().get(0);
        } else {
            service = vehicle.getServices().get(1);
        }

        int user = service.getUser().getId();
        int userIndex = indexOfUserId(user);

        // the taxi company requests the user to rate the service, and updates its status

        this.users.get(userIndex).rateService(service);
        this.users.get(userIndex).setService(false);

        // update the counter of services

        this.totalServices--;

        notifyObserver(String.format("%-8s",vehicle.getClass().getSimpleName()) + vehicle.getId() + " drops off user " + user);
    }

    @Override
    public void addObserver(IObserver observer) {
        this.observer = observer;
    }

    @Override
    public void notifyObserver(String message) {
        this.observer.updateObserver(message);
    }

    private int findFreeVehicle() {
        // finds a free vehicle and returns the index of the vehicle in the list, otherwise it returns -1
        for (IVehicle i: vehicles) {
            if (i.isFree()) {
                return vehicles.indexOf(i);
            }
        }
        return -1;
    }

    private int findRideShareVehicle() {
        for (IVehicle i: vehicles) {
            if (i.isInService()) {
                if (i.getClass() == Taxi.class || i.getClass() == Shuttle.class) {
                    return vehicles.indexOf(i);
                }
            }
        }
        return -1;
    }

    private int indexOfUserId(int id) {
        // finds the index of a user with the input id in the list, otherwise it returns -1
        for (IUser i: users) {
            if (i.getId() == id) {
                return users.indexOf(i);
            }
        }
        return -1;
    }

    private boolean isRideShare(int rideShareVehicleIndex, ILocation[] endPoints) {

        // If there is a vehicle available for ride-share check if both users are ok with is by using a randomizer
        if (rideShareVehicleIndex != -1) {

            if (this.vehicles.get(rideShareVehicleIndex).getClass() == Bike.class
                    || this.vehicles.get(rideShareVehicleIndex).getClass() == Scooter.class) {
                return false;
            }

            endPoints[0] = ApplicationLibrary.randomLocation(); //updates origin but not destination --> Sprint 4 V1
            endPoints[1] = ApplicationLibrary.randomLocation(endPoints[0]);

            // If vehicle is within 3 distance then ride-sharing is available
            if (ApplicationLibrary.distance(endPoints[0], this.vehicles.get(rideShareVehicleIndex).getLocation()) < 3 &&
                    ApplicationLibrary.distance(endPoints[0], this.vehicles.get(rideShareVehicleIndex).getDestination()) > 3) {

                // If users "accept" return true. Users "accept" if the random value is even
                if (ApplicationLibrary.rand(2) % 2 == 0) {
                    this.vehicles.get(rideShareVehicleIndex).setStatus(VehicleStatus.PICKUP_RIDE_SHARE);
                    return true;
                }
            }
        }
        return false;
    }
}