package taxify;

public interface ITaxiCompany {

    public String getName();
    public int getTotalServices();
    public boolean provideService(int user);
    public int findRideType(int userIndex, int freeVehicleIndex, int rideShareVehicleIndex, ILocation[] endPoints);
    public void arrivedAtPickupLocation(IVehicle vehicle);

    public void arrivedAtDropoffLocation(IVehicle vehicle);
}
