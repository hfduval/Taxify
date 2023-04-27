package taxify;

import java.util.List;
import java.util.*;

public class ApplicationSimulator implements IApplicationSimulator, IObserver {
    private ITaxiCompany company;
    private List<IUser> users;
    private List<IVehicle> vehicles;

    public ApplicationSimulator(ITaxiCompany company, List<IUser> users, List<IVehicle> vehicles) {
        this.company = company;
        this.users = users;
        this.vehicles = vehicles;
    }

    @Override
    public void show() {
        // shows the status of the vehicles

        System.out.println("\n" + this.company.getName() + " status \n");

        for (int i=0; i<this.vehicles.size(); i++) {
            System.out.println(this.vehicles.get(i).toString());
        }
    }

    @Override
    public void showStatistics() {
        // shows the statistics of the company
        System.out.println("\n" + this.company.getName() + " statistics \n");

        for (int i=0; i<this.vehicles.size(); i++) {
            IStatistics curr = this.vehicles.get(i).getStatistics();
            System.out.println(
                    this.vehicles.get(i).getClass().getSimpleName() + " " + this.vehicles.get(i).getId() + " " +
                            curr.getServices() + " services " + curr.getDistance() + " km. " + curr.getBilling() + " "
                            + curr.getBilling() + " eur. " + curr.getReviews() + " reviews " + curr.getStars() + " stars"
                    );
        }
    }

    @Override
    public void update() {
        // moves the vehicles to their next location

        for (int i=0; i<this.vehicles.size(); i++) {
            if (!vehicles.get(i).isFree()) {
                this.vehicles.get(i).move();
            }
        }
    }

    @Override
    public void requestService() {
        // finds a "free" user and requests a service to the Taxi Company

        for (int i = 0; i < this.users.size(); ++i) {
            if (!this.users.get(i).getService()) {
                this.users.get(i).requestService();
                break;
            }
        }
    }

    @Override
    public int getTotalServices() {
        return this.company.getTotalServices();
    }

    @Override
    public void updateObserver(String message) {
        System.out.println(message);
    }
}