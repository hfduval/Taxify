package taxify;

public class Taxi extends Vehicle {

    public Taxi(int id, ILocation location) {
        super(id, location);
    }

    @Override
    public int calculateCost(IService service) {
        return super.calculateCost(service) * 2;
    }

    @Override
    public String toString() {
        return "Taxi    " + super.toString();
    }
}
