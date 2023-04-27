package taxify;

import java.util.ArrayList;
import java.util.List;

public class TestProgram {

    public static void main(String[] args) {

        List<IUser> users = new ArrayList<IUser>(15);
        for (int i = 1; i <= 15; ++i) {
            User tmp = new User(i, "fname" + i, "lname" + i);
            users.add(tmp);
        }

        List<IVehicle> vehicles = new ArrayList<IVehicle>(10);
        for (int i = 1; i <= 12; ++i) {
            IVehicle curr;
            ILocation pt = ApplicationLibrary.randomLocation();
            if (i % 4 == 0) {
                curr = new Taxi(i, pt);
            } else if (i % 4 == 1) {
                curr = new Shuttle(i, pt);
            } else if (i % 4 == 2) {
                curr = new Bike(i, pt);
            } else {
                curr = new Scooter(i, pt);
            }
            vehicles.add(curr);
        }

        TaxiCompany taxify = new TaxiCompany("Taxify", users, vehicles);
        ApplicationSimulator application = new ApplicationSimulator(taxify, users, vehicles);

        taxify.addObserver(application);

        application.show();

        for (int i = 0; i < 5; ++i) {
            application.requestService();
        }

        int j;
        do {
            j = 0;
            for (int i = 0; i < vehicles.size(); ++i) {
                if (vehicles.get(i).isFree()) j += 1;
            }
            if (ApplicationLibrary.rand(10) % 5 == 0) application.requestService();
            application.show();
            application.update();
        } while (j < vehicles.size());

        application.showStatistics();

    }

}