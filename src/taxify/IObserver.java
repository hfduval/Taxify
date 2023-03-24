package taxify;

/**
 * This interface defines the Observer class. This class imitates the observer design pattern.
 */

public interface IObserver {

    /**
     * This method is used by Subject classes to update observers when their state has changed.
     * @param message sent by the Subject
     */
    public void updateObserver(String message);

}