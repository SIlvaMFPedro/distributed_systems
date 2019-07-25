/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_side;

import commonInfo.Message;
import commonInfo.MessageException;

import java.net.SocketException;
/**
 *
 * @author pedro
 * @author franciscoteixeira
 */
public interface Server_Interface {
    /**
     * Processes the received messages and replies to the entity that sent it.
     * @param msg_in message in
     * @param sch server channel
     * @return message out
     * @throws MessageException message exception
     * @throws SocketException  socket exception
     */
    public Message processAndReply(Message msg_in, Server_Channel sch) throws MessageException, SocketException;
    
    /**
     * Tell the service if it is allowed to end or not.
     * @return true if the system can terminate, false otherwise.
     */
    public boolean serviceEnded();
    /**
     * Tell the monitors if it is allowed to end or not.
     * @return true if the monitors can terminate, false otherwise.
     */
    public boolean endOfMonitors();
}
