/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.Serializable;
import utility.TileType;

/**
 * Wordt gebruikt om door te geven welk type tegel er gebruikt wordt bij Drag & Drop
 * @author tom
 */
public class TileTransfer implements Transferable, Serializable{
    public static DataFlavor TILE_FLAVOR = new DataFlavor(TileTransfer.class, "TileType");
    private DataFlavor[] flavors = {TILE_FLAVOR};
    private TileType type;
    
    /**
     * Constructor
     * @param type TileType die verplaatst wordt
     */
    public TileTransfer(TileType type) {
        this.type = type;
        
    }

    public TileType getType() {
        return type;
    }

    public void setType(TileType type) {
        this.type = type;
    }

    public DataFlavor[] getTransferDataFlavors() {
        return flavors;
    }

    public boolean isDataFlavorSupported(DataFlavor arg0) {
        return TILE_FLAVOR.match(arg0);
    }

    public Object getTransferData(DataFlavor arg0) throws UnsupportedFlavorException, IOException {
        if(isDataFlavorSupported(arg0)){
            return type;
        }
        else{
            return null;
        }
    }
   
    
}
