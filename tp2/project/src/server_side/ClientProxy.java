/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_side;

import commonInfo.Message;
import commonInfo.MessageException;

import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This data type defines the service thread for a server-client architecture.
 * The communication is based on messages over TCP sockets.
 */

/**
 *
 * @author pedro
 * @author franciscoteixeira.
 */
public class ClientProxy extends Thread {
    /**
     * Counter for launched proxy threads.
     * @serialField numProxy.
     */
    private static int numProxy;
    
    /**
     * Server Communication channel.
     * @serialField server channel.
     */
    
    /**
     * Communication channel.
     *
     * @serialField server channel
     */
    private final Server_Channel schannel;
    
    /**
     * Server interface.
     *
     * @serialField server Interface
     */
    private final Server_Interface sInterface;
    
    /**
     * Server communication.
     * 
     * @serialField server com
     */
    private final Server_Channel scomm;
    
    /**
     * Instantiation identifier generator.
     * @return instantiation identifier.
     */
    private static int getProxyId() {
        // representação do tipo de dados ClientProxy na máquina virtual de java
        Class<ClientProxy> cl = null;
        int proxyId;                              // identificador da instanciação

        try {
            cl = (Class<ClientProxy>) Class.forName("serverSide.ClientProxy");
        } catch (ClassNotFoundException e) {
            System.out.println("O tipo de dados ClientProxy não foi encontrado!");
            System.exit(1);
        }

        synchronized (cl) {
            proxyId = numProxy;
            numProxy += 1;
        }

        return proxyId;
    }
    
    /**
     * Server interface instantiations.
     *
     * @param scomm server communication
     * @param schannel communication channel
     * @param sInterface server interface
     */
    public ClientProxy(Server_Channel scomm, Server_Channel schannel, Server_Interface sInterface) {
        //super("Proxy_" + getProxyId());
        super("Proxy_" + 1);
        this.schannel = schannel;
        this.sInterface = sInterface;
        this.scomm = scomm;
    }
    
    /**
     * Agent's life cycle.
     */
    @Override
    public void run() {
        Message inMessage = null; // mensagem de entrada
        Message outMessage = null;                      // mensagem de saída

        inMessage = (Message) schannel.read();                     // ler pedido do cliente
        try 
        {
            outMessage = sInterface.processAndReply(inMessage, scomm);         // processá-lo
        } catch (MessageException e) 
        {
            System.out.println("Thread " + getName() + ": " + e.getMessage() + "!");
            System.out.println(e.getMessageValue().toString());
            System.exit(1);
        } catch (SocketException ex) {
            Logger.getLogger(ClientProxy.class.getName()).log(Level.SEVERE, null, ex);
        }
        schannel.write(outMessage);                                // enviar resposta ao cliente
        schannel.close();                                                // fechar canal de comunicação
        
        if(sInterface.serviceEnded())
        {
            System.out.println("Closing service ... Done!");
            System.exit(0);
        }
    }
}
