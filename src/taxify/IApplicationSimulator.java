package taxify;

/**
 * This interface defines the app simulator class. These methods will be used to simulate the taxify application.
 */
public interface IApplicationSimulator {

    /**
     * This method shows the status of the vehicles.
     */
    public void show();

    /**
     * This method shows the statistics of the company.
     */
    public void showStatistics();

    /**
     * This method moves vehicles to their next location.
     */
    public void update();

    /**
     * This method finds a "free" user and requests a service to the Taxi Company
     */
    public void requestService();

    /**
     * This method gets the total number of services.
     */
    public int getTotalServices();

}
