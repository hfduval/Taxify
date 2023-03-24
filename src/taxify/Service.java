package taxify;

public class Service implements IService {
    private IUser user;
    private ILocation pickup;
    private ILocation dropoff;
    private int stars;

    public Service(IUser user, ILocation pickup, ILocation dropoff) {
        this.user = user;
        this.pickup = pickup;
        this.dropoff = dropoff;
        this.stars = 0;
    }

    @Override
    public IUser getUser() {
        return this.user;
    }

    @Override
    public ILocation getPickupLocation() {
        return this.pickup;
    }

    @Override
    public ILocation getDropoffLocation() {
        return this.dropoff;
    }

    @Override
    public int getStars() {
        return this.stars;
    }

    @Override
    public void setStars(int stars) {
        this.stars = stars;
    }

    @Override
    public int calculateDistance() {
        return Math.abs(this.pickup.getX() - this.dropoff.getX()) + Math.abs(this.pickup.getY() - this.dropoff.getY());
    }

    @Override
    public String toString() {
        return this.getPickupLocation().toString() + " to " + this.getDropoffLocation().toString();
    }
}
