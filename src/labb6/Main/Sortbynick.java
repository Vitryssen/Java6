/*
 * André Nordlund
 * 2021-02-23
 * Java 2
 * Lab 6
 */
package labb6.Main;

import labb6.DataStructures.Friend;
import java.util.Comparator;

/**
 *
 * @author André
 */
public class Sortbynick implements Comparator<Friend>{
    @Override
    public int compare(Friend a, Friend b){
        String alower = a.getNick().toLowerCase();
        String blower = b.getNick().toLowerCase();
        return alower.compareTo(blower); //compare a.getNick() if you wish to sort
    }                                    //uppercase before lowercase
}
