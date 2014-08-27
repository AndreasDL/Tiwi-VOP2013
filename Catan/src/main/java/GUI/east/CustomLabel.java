/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.east;

import GUI.east.ImageTooltip;
import javax.swing.JLabel;
import javax.swing.JToolTip;
import utility.Layout;

/**
 *
 * @author Jens
 */
public class CustomLabel extends JLabel{
    
    private ImageTooltip itt;
    private int i;
    
    public CustomLabel(int value,int i) {
        this.i=i;
        setText(Integer.toString(value));
        setFont(Layout.FONT);
       // setForeground(Color.white);
        setOpaque(true);
        setBackground(Layout.ITEM_COLOR);
        setHorizontalAlignment(JLabel.CENTER);
    }

    @Override
    public JToolTip createToolTip() {
        if(itt == null){
            itt= new ImageTooltip(i);
            itt.setComponent(this);
        }
        return itt;
    }
/*
    @Override
    public Point getToolTipLocation(MouseEvent event) {
        return  event.getPoint();
    }
 */   
    
    
    
    
    
    
    
}
