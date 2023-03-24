package taxify;

/**
 * The interface ISubject declares methods to add, remove and notify observers
 */

public interface ISubject {

    /**
     * Adds an observer to the list of observers watching this subject.
     * @param observer is the observer object being added
     */
    public void addObserver(IObserver observer);
    /**
     * Sends a message to all observers using the notifyObserver method
     * @param message sent to the observers
     */
    public void notifyObserver(String message);

}