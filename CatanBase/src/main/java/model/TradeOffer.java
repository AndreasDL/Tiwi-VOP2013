/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import utility.TileType;

/**
 * Deze klasse houdt een type grondstof bij waarvaan een aantal ofwel gegeven wordt aan de speler/bank 
 * ofwel gevraagd wordt aan de andere speler.
 * toGive == true => weggeven
 * toGive == false => krijgen
 * @author Andreas De Lille
 */
public class TradeOffer {
    private int[] offer= new int[TileType.NUMBER_OF_RESOURCES];
    private int[] request = new int[TileType.NUMBER_OF_RESOURCES];

    public TradeOffer(int[] offer, int[] request) {
        this.offer=offer;
        this.request=request;       
    }
    public TradeOffer(){
        for(int i=0; i < TileType.NUMBER_OF_RESOURCES;i++){
            offer[i]=0;
            request[i]=0;
        }
    }
    public void addOfferResource(int i, int amount){
        offer[i]+=amount;
    }
    public void addRequestResource(int i, int amount){
        request[i]+=amount;
    }

    public int[] getOffer() {
        return offer;
    }

    public int[] getRequest() {
        return request;
    }
    
    
    
    public String getOfferString(){
        String txt="";
        TileType[] types = TileType.values();
        for(int i=0; i < TileType.NUMBER_OF_RESOURCES; i++){
            if(offer[i]!=0){
                txt+="aantal: " +offer[i] + " van: "+types[i].getTranslation()+"\n";
            }            
        }
        return txt;
    }
    
    public String getRequestString(){
        String txt="";
        TileType[] types = TileType.values();
        for(int i=0; i < TileType.NUMBER_OF_RESOURCES; i++){
            if(request[i]!=0){
                txt+="aantal: " +request[i] + " van: "+types[i].getTranslation()+"\n";
            }            
        }
        return txt;
    }
}
