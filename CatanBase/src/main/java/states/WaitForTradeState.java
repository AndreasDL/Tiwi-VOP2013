/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package states;	

import commandos.ICommand;	
import java.awt.event.ActionEvent;	
import java.awt.event.ActionListener;	
import java.util.ArrayList;	
import javax.swing.Timer;	
import model.interfaces.IState;

/**	
 * @author Andreas De Lille	
 */
	
public class WaitForTradeState extends StandardState implements ActionListener{
		
    private Timer tmr = new Timer(1000, this);	
    private IState prevState;
    private boolean isMulti;

	
    public WaitForTradeState(IState state) {	
       setController(state.getController());	
       setBoard(state.getBoard());	

       this.prevState=state;

       tmr.start();	

    }
	
	
    public void actionPerformed(ActionEvent e) {	
        
        ArrayList<ICommand> list= getController().getUpdates();
        for( ICommand cmd : list){	
            cmd.executeClient(getController(),getBoard());	
            getBoard().addCommand(cmd);
//<<<<<<< HEAD
//        }	
//        //if(dorepaint)	
//        if( ( !isMulti && !list.isEmpty() ) || ( isMulti && getController().isTradeOver() ) ) {	
//            getController().syncResources();
//            tmr.stop();	
//            goNextState();	
//        }        
//=======
        }

       

    }
    @Override	
    public void goNextState() {
        
        tmr.stop();
        getController().notifyObservers("btn");
        getController().setState(prevState);

    }
}
