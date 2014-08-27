/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.interfaces;

/**
 * Deze interface wordt gebruikt om een settlement voor te stellen.
 * @author Andreas De Lille
 */
public interface ISettlement {
    public IPlayer getPlayer();
    public int settlementSize();
}
