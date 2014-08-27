/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package states;

/**
 *
 * @author Samuel
 */
public class OtherFirstBuildingState extends StandardState{

    private PlaceStructureState state;

    public OtherFirstBuildingState(PlaceStructureState state) {
        this.state=state;
        setController(state.getController());
        getController().setMessages("otherPlayer", "");
        state.getBoard().getCurrentPlayer().setOtherPlayerState(true);
        state.getController().getOwnName();
        
    }

 
    @Override
    public void goNextState() {
        state.getController().setMessages("placeVillage", "");
        state.getBoard().getCurrentPlayer().setOtherPlayerState(false);
        getController().setState(state);
    }

    @Override
    public String toString() {
        return "OtherPlayerFirstBuilding";
    }
    
    
    
    
}
