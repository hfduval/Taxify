package taxify;

public interface ITaxiCompany {

    public String getName();
    public int getTotalServices();
    public boolean requestService(int user);

    public void arrivedAtPickupLocation(IVehicle vehicle);

    public void arrivedAtDropoffLocation(IVehicle vehicle);
}
