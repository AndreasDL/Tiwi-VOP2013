/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Catan.database;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.security.SecureRandom;
import java.math.BigInteger;

/**
 *
 * @author Andreas De Lille
 */
public class Hasher {
    private static SecureRandom random = new SecureRandom();
//    public static void main(String[] args) {
//        Hasher hash = new Hasher();
//    }
    
    public Hasher(){
        //random = new SecureRandom();
        //String salt = "hoi";//makeSalt();
        //String pass = "team02";
        //System.out.println(salt);
        //System.out.println(Hasher.hash(pass,salt));
    }
    public static String hash(String pass,String salt){
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-512");

            md.update((pass+salt).getBytes());

            byte byteData[] = md.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            
            //System.out.println(sb.toString());
            
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Hasher.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
  
    public static String makeSalt()
    {
        return new BigInteger(130, random).toString(32);
    }

}
