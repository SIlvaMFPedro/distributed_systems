/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_side;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import genclass.GenericIO;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.net.BindException;
import java.net.SocketException;

/**
 * @author pedro
 * @author franciscoteixeira
 */
public class Server_Channel {
    /**
     * Listening Socket.
     * @serialField listeningSocket.
     */
    private ServerSocket listeningSocket;
    
    /**
     * Communication Socket.
     * @serialField commSocket.
     */
    private Socket commSocket;
    
    /**
     * Input stream of communication socket.
     * @serialField in;
     */
    private ObjectInputStream in = null;
    
    /**
     * Output stream of communication socket.
     * @serialField out;
     */
    private ObjectOutputStream out = null;
    
    /**
     * Listening server port.
     * @serialField serverPortNum;
     */
    private int serverPortNum;
    
    
    /**
     * Instantiate server channel.
     * @param portNum port number
     */
    public Server_Channel (int portNum)
    {
        this.serverPortNum = portNum;
        this.listeningSocket = null;
        this.commSocket=null;
    }
    
    /**
     * Instantiate server channel.
     * @param portNum port number
     * @param listeningSocket listening socket
     */
    public Server_Channel (int portNum, ServerSocket listeningSocket)
    {
        this.serverPortNum = portNum;
        this.listeningSocket = listeningSocket;
        this.commSocket=null;
    }
    
    /**
     * Instantiate server channel.
     * @param portNum port number
     * @param listeningSocket listening socket
     * @param comSocket communication socket
     */
    public Server_Channel (int portNum, ServerSocket listeningSocket, Socket comSocket)
    {
        this.serverPortNum = portNum;
        this.listeningSocket = listeningSocket;
        this.commSocket = comSocket;
    }
    
    /**
     * Establish service channel.
     * Instantiate listening socket with local machine address and public listening ports.
     */
    public void start(){
        try {
            // Server instantiation
            listeningSocket = new ServerSocket (serverPortNum, 1); //fila de ligações em espera(comprimento 1);
        } catch(BindException e){ //ERRO - o port já está a ser usado noutra ligação.
            GenericIO.writelnString(Thread.currentThread().getName() + "ERROR - não foi possivel estabelecer a ligação com a socket de escuta ao port: " + serverPortNum + "!");
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) { //ERRO - outras causas;
            //Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, e);
            GenericIO.writelnString(Thread.currentThread().getName() + "UNEXPECTED ERROR - não foi possivel estabelecer a ligação com a socket de escuta ao port: " + serverPortNum + "!");
            e.printStackTrace();
            System.exit(1);
            
        }
    }
    
    /**
     * Listening process.
     * Create a communication channel for pending request.
     * Instantiate communication socket and associate with client address.
     * Open input and output streams of communication socket.
     * @return communication channel.
     */
    public Server_Channel commChannel() {
        Server_Channel repch;
        repch = new Server_Channel(serverPortNum, listeningSocket);
        try {            
            repch.commSocket = listeningSocket.accept(); 
        } catch (SocketException e){
            //Logger.getLogger(Server_Channel.class.getName()).log(Level.SEVERE, null, e);
            GenericIO.writelnString (Thread.currentThread().getName() + " - socket de escuta ao port : " + serverPortNum + " foi fechada durante o processo de escuta!");
            e.printStackTrace ();
            System.exit (1);
        } catch (IOException e) {
            //Logger.getLogger(Server_Channel.class.getName()).log(Level.SEVERE, null, e);
            GenericIO.writelnString (Thread.currentThread().getName() +  " - não foi possível abrir um canal de comunicação para o pedido pendente!");
            e.printStackTrace ();
            System.exit (1);
        }
        
        try{
            repch.in = new ObjectInputStream (repch.commSocket.getInputStream());
        } catch (IOException e){
            //Logger.getLogger(Server_Channel.class.getName()).log(Level.SEVERE, null, e);
            GenericIO.writelnString (Thread.currentThread().getName() + " - não foi possível estabelecer comunicação com o canal de entrada da socket!");
            e.printStackTrace ();
            System.exit (1);
        }
        
        try{
            repch.out = new ObjectOutputStream (repch.commSocket.getOutputStream());
        } catch (IOException e){
            //Logger.getLogger(Server_Channel.class.getName()).log(Level.SEVERE, null, e);
            GenericIO.writelnString (Thread.currentThread().getName() + " - não foi possível estabelecer comunicação com o canal de saida da socket!");
            e.printStackTrace ();
            System.exit (1);
        }
        
        return repch;
    }
    
    /**
     * Close communication channel.
     * Close input and output streams of communication socket.
     * Close communication socket.
     */
    @SuppressWarnings("CallToPrintStackTrace")
    public void close(){
        try {
            in.close();
        } catch (IOException e){
            //Logger.getLogger(Server_Channel.class.getName()).log(Level.SEVERE, null, e);
            GenericIO.writelnString (Thread.currentThread().getName() + " - não foi possível fechar o canal de entrada da socket!");
            e.printStackTrace ();
            System.exit (1);
        }
        
        try {
            out.close();
        } catch (IOException e){
            //Logger.getLogger(Server_Channel.class.getName()).log(Level.SEVERE, null, e);
            GenericIO.writelnString (Thread.currentThread().getName() + " - não foi possível fechar o canal de saída da socket!");
            e.printStackTrace ();
            System.exit (1);
        }
        
        try {
            commSocket.close();
        } catch (IOException e){
            //Logger.getLogger(Server_Channel.class.getName()).log(Level.SEVERE, null, e);
            GenericIO.writelnString (Thread.currentThread().getName() + " - não foi possível fechar socket de comunicação!");
            e.printStackTrace ();
            System.exit (1);
        }
    }
    
    
    /**
     * Read object from communication channel.
     * @return read object.
     */
    @SuppressWarnings("CallToPrintStackTrace")
    public Object read(){
        Object obj = null;
        try {
            obj = in.readObject ();
        } 
        catch (InvalidClassException e)
        { 
            //Logger.getLogger(Server_Channel.class.getName()).log(Level.SEVERE, null, e);
            GenericIO.writelnString (Thread.currentThread().getName() + " - o objecto lido não é passível de desserialização!");
            e.printStackTrace ();
            System.exit (1);
        }
        catch (IOException e)
        { 
            //Logger.getLogger(Server_Channel.class.getName()).log(Level.SEVERE, null, e);
            GenericIO.writelnString (Thread.currentThread().getName() + " - erro na leitura de um objecto do canal de entrada do socket de comunicação!");
            e.printStackTrace ();
            System.exit (1);
        }
        catch (ClassNotFoundException e)
        {   //Logger.getLogger(Server_Channel.class.getName()).log(Level.SEVERE, null, e);
            GenericIO.writelnString (Thread.currentThread().getName() + " - o objecto lido corresponde a um tipo de dados desconhecido!");
            e.printStackTrace ();
            System.exit (1);
        }
        return obj;        
    }
    
    /**
     * Write object in communication channel.
     * @param obj object to be written.
     */
    @SuppressWarnings("CallToPrintStackTrace")
    public void write(Object obj){
        try {
            out.writeObject(obj);
        }
        catch (InvalidClassException e)
        { 
            //Logger.getLogger(Server_Channel.class.getName()).log(Level.SEVERE, null, e);
            GenericIO.writelnString (Thread.currentThread().getName() + " - o objecto a ser escrito não é passível de serialização!");
            e.printStackTrace ();
            System.exit (1);
        }
        catch (NotSerializableException e)
        { 
            //Logger.getLogger(Server_Channel.class.getName()).log(Level.SEVERE, null, e);
            GenericIO.writelnString (Thread.currentThread().getName() + " - o objecto a ser escrito pertence a um tipo de dados não serializável!");
            e.printStackTrace ();
            System.exit (1);
        }
        catch (IOException e)
        {   
            //Logger.getLogger(Server_Channel.class.getName()).log(Level.SEVERE, null, e);
            GenericIO.writelnString (Thread.currentThread().getName() + " - erro na escrita de um objecto do canal de saída do socket de comunicação!");
            e.printStackTrace ();
            System.exit (1);
        }
    }
}
