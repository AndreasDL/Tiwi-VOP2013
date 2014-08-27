/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package commandos;

import model.Board;
import model.interfaces.IController;

/**
 *  wordt gebruikt om iets uit te voeren dat door andere speler kan worden opgevraagd
 *  altijd als spelers update aan server vragen geeft deze de commandos die nog niet bij die speler zijn uitgevoerd
 * @author Samuel
 */
public interface ICommand {
    /**
     * voert een commando op de client uit
     * @param board : commando wordt op het bord uitgevoerd
     */
    public void executeClient(IController controller , Board board); 
    /**
     * voert een commando op de client uit
     * @param board : commando wordt op het bord uitgevoerd
     * @param id  : id van de speler die het commando uitvoerd
     */
    public boolean executeServer(Board board,int id); 
    
    public void restoreState(IController controller,int prevCommand,boolean myTurn);
    
    @Override
    public String toString();
    
    //output die in historiek moet komen.
    public String uitleg(Board b);
    
}
