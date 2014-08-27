/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package observer;

/**
 *
 * @author Jens
 */
public interface IObservable {
    
    public void addObserver(IObserver obs);
    
    public void notifyObservers(String arg);
    
    public void notifyObservers();
}
