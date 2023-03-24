package taxify;

public class Statistics implements IStatistics {
    private int services;
    private int reviews;
    private int stars;
    private int billing;
    private int distance;

    public Statistics() {
        services = 0;
        reviews = 0;
        stars = 0;
        billing = 0;
        distance = 0;
    }

    public int getServices() {
        return services;
    }

    public int getReviews() {
        return reviews;
    }

    public double getStars() {
        return ((float)stars / reviews);
    }

    public int getDistance() {
        return distance;
    }

    public int getBilling() {
        return billing;
    }

    public void updateServices() {
        ++services;
    }

    public void updateReviews() {
        ++reviews;
    }

    public void updateStars(int stars) {
        this.stars += stars;
    }

    public void updateDistance(int distance) {
        this.distance += distance;
    }

    public void updateBilling(int billing) {
        this.billing += billing;
    }
}
