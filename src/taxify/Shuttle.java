package taxify;

public class Shuttle extends Vehicle {

    public Shuttle(int id, ILocation location) {
        super(id, location);
    }

    @Override
    public int calculateCost() {
        return (int) (super.calculateCost() * 1.5);
    }

    @Override
    public String toString() {
        return "Shuttle " + super.toString();
    }
}