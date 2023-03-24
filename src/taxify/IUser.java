package taxify;

public interface IUser {

    public int getId();
    public String getFirstName();
    public String getLastName();
    public boolean getService();
    public void setService(boolean service);
    public void setCompany(ITaxiCompany company);
    public void requestService();
    public void rateService(IService service);
    public String toString();

}
