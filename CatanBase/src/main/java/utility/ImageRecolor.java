/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.awt.Color;
import java.awt.image.RGBImageFilter;
import model.Road;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jens
 */
public class ImageRecolor extends RGBImageFilter {
    private static final Logger log = LoggerFactory.getLogger(RGBImageFilter.class);
    private Color color;

    public ImageRecolor(Color color) {
        //log.debug("Entering ImageRecolor constructor ( color= {} )", color.toString());
        this.color = color;
        canFilterIndexColorModel = true;
        log.debug("Leaving ImageRecolor constructor");
    } 
    
    @Override
    public int filterRGB(int x, int y, int rgb) {
        log.debug("Entering filterRGB ( x= {} y= {} rgb= {}",x,y,rgb);
        //System.out.println(rgb);
        if(rgb==-1) {
            return color.getRGB();
        }
        else {
            return rgb;
        }
     /*   if( rgb > 0xff0000){
            log.debug("returning from filterRGB= {}", color.getRGB());
            return color.getRGB();
        }else{
            log.debug("returing from filterRGB= {}",rgb);
            return rgb;
        }*/
    }
    
}
