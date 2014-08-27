/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package observer;

/**
 *
 * @author Jens
 */
public interface IObserver {   
    
    public void update(IObservable o, String arg);
}
