/*
 * André Nordlund
 * 2021-02-23
 * Java 2
 * Lab 6
 */
package labb6.Socket;

import labb6.Exceptions.CustomException;

/**
 *
 * @author André
 */
public interface HostListener {
    void messageRecieved(String message) throws CustomException;
}
