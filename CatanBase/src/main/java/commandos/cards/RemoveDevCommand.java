package commandos.cards;


import commandos.ICommand;
import commandos.build.MakeStartVillageCommand;
import model.Board;
import model.interfaces.IController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import states.Build2RoadsState;
import states.MonopolyState;
import states.OtherPlayerState;
import utility.DevCardType;

public class RemoveDevCommand implements ICommand{
    private static final Logger log = LoggerFactory.getLogger(MakeStartVillageCommand.class);
    int player;
    DevCardType card;
    public RemoveDevCommand(int player,DevCardType card) {
        log.debug("Entering BuyDevCommand ( player= {} )",player);
        this.player=player;
        this.card=card;
    }

    
    public void executeClient(IController controller , Board board) {
       board.getCurrentPlayer().removeCard(card.getValue());   
       board.getCurrentPlayer().notifyYourObservers();
    }
    
    public boolean executeServer(Board board,int id) {
        if(id == board.getCurrentPlayerId() && id == player && board.removeDevCard(player,card)){    
            //board.setPlayCard(board.getPlayerPlace(player));
            //board.setPlayCard2Stuff(board.getPlayerPlace(player));
            board.setPlayCard2Stuff(board.getPlayerPlace(player),card.getValue());
            return true;
        }
        return false;
    }

    public void restoreState(IController controller, int prevCommand, boolean myTurn) {
        if(myTurn){
            if(card== DevCardType.ROAD){
                controller.setState(new Build2RoadsState(controller.getState()));
            }
            else if(card == DevCardType.MONOPOLY){
                controller.setState(new MonopolyState(controller.getState(), true));
            }
            else if(card==DevCardType.RESOURCES){
                controller.setState(new MonopolyState(controller.getState(), false));
            }            
        }
        else{
            controller.setState(new OtherPlayerState(controller, controller.getBoard())); 
            controller.startTimer();
        }
        controller.getBoard().setLastRolledNumber();
    }

    public String uitleg(Board b) {
        return "speler: " + b.getPlayer(player).getName() + " heeft een " + card.toString() + " gespeeld";
    }
}
