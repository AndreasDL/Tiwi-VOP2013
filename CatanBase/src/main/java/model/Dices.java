/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.Random;
import observer.IObservable;
import observer.IObserver;

/**
 *
 * @author Jens
 */
public class Dices implements IObservable{
    private int dice1;
    private int dice2;
    private ArrayList<IObserver> observers= new ArrayList<IObserver>();
    
    public Dices() {
        this.dice1 = 0;
        this.dice2 = 0;
    }   
    
    
//     public void generate(){
//         dice1 = rnd.nextInt(6)+1;
//         dice2 = rnd.nextInt(6)+1;
//         notifyObservers();
//     }

    public int getDice1() {
        return dice1;
    }

    public int getDice2() {
        return dice2;
    }

    public void setDices(int dice1,int dice2) {
        this.dice1 = dice1;
        this.dice2 = dice2;
        System.out.println("setDices");
        notifyObservers();
    }
    
    public void addObserver(IObserver obs) {
        observers.add(obs);
    }

    public void notifyObservers(String arg) {
        for( IObserver obs : observers){
            obs.update(this, arg);
        }
    }

    public void notifyObservers() {
        for( IObserver obs : observers){
            obs.update(this, null);
        }
    }
     
     
}
