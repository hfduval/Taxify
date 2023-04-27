package taxify;

public class Shuttle extends Vehicle {

    public Shuttle(int id, ILocation location) {
        super(id, location);
    }

    @Override
    public double calculateCost(IService service) {
        return (int) (super.calculateCost(service) * 1.5);
    }

    @Override
    public String toString() {
        return "Shuttle " + super.toString();
    }
}