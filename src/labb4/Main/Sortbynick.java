/*
 * André Nordlund
 * 2021-02-19
 * Java 2
 * Lab 4
 */
package labb4.Main;

import labb4.DataStructures.Friend;
import java.util.Comparator;

/**
 *
 * @author André
 */
public class Sortbynick implements Comparator<Friend>{
    public int compare(Friend a, Friend b){
        String alower = a.getNick().toLowerCase();
        String blower = b.getNick().toLowerCase();
        return alower.compareTo(blower); //compare a.getNick() if you wish to sort
    }                                    //uppercase before lowercase
}
