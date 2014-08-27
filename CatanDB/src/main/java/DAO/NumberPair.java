/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

/**
 *
 * @author Andreas De Lille
 */
public class NumberPair {
    private String date;
    private int count;

    public NumberPair(String date, int count) {
        this.date = date;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public String getDate() {
        return date;
    }
    
    
}
