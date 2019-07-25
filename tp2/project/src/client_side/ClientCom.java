/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client_side;

import genclass.GenericIO;
import java.io.*;
import java.net.*;

/**
 *   Este tipo de dados implementa o canal de comunicação, lado do cliente, para uma comunicação baseada em passagem de
 *   mensagens sobre sockets usando o protocolo TCP.
 *   A transferência de dados é baseada em objecto que é efectuada num objecto de cada vez.
 */

/**
 *
 * @author pedro
 */
public class ClientCom {
    /**
     * Communication socket
     * @serialField commSocket
     */
    private Socket commSocket = null;
    
    /**
     * Host computer system where the server is.
     * @serialField serverHostName
     */
    private String serverHostName = null;
    
    /**
     * Server listening port.
     * @serialField serverPortNum
     */
    private int serverPortNum;
    
    /**
     * Input stream of communication channel.
     * @serialField in
     */
    private ObjectInputStream in = null;
    
    /**
     * Output stream of communication channel.
     * @serialField out
     */
    private ObjectOutputStream out = null;
    
    /**
     * Instantiate client communication channel.
     * @param hostName server host name.
     * @param portNum port number.
     */
    public ClientCom (String hostName, int portNum)
    {
        serverHostName = hostName;
        serverPortNum = portNum;
    }
    
    /**
     * Open communication channel.
     * Instantiate a communication socket with connection to server address.
     * Open input and output streams of communication socket.
     * @return true if communication channel is open return false if not.
     */
    @SuppressWarnings("CallToPrintStackTrace")
    public boolean open ()
    {
        boolean success = true;
        SocketAddress serverAddress = new InetSocketAddress (serverHostName, serverPortNum);

        try
        { 
            commSocket = new Socket();
            commSocket.connect (serverAddress); //https://docs.oracle.com/javase/7/docs/api/java/net/Socket.html#connect(java.net.SocketAddress,%20int)
        }
        catch (UnknownHostException e)
        { 
            GenericIO.writelnString (Thread.currentThread ().getName () + " - o nome do sistema computacional onde reside o servidor é desconhecido: " + serverHostName + "!");
            e.printStackTrace ();
            System.exit (1);
        }
        catch (NoRouteToHostException e)
        { 
            GenericIO.writelnString (Thread.currentThread ().getName () + " - o nome do sistema computacional onde reside o servidor é inatingível: " + serverHostName + "!");
            e.printStackTrace ();
            System.exit (1);
        }
        catch (ConnectException e)
        { 
            GenericIO.writelnString (Thread.currentThread ().getName () + " - o servidor não responde em: " + serverHostName + "." + serverPortNum + "!");
            if (e.getMessage ().equals ("Connection refused")){
                success = false;
            }
            else {
                GenericIO.writelnString (e.getMessage () + "!");
                e.printStackTrace ();
                System.exit (1);
            }
        }
        catch (SocketTimeoutException e)
        { 
            GenericIO.writelnString (Thread.currentThread ().getName () + " - ocorreu um time out no estabelecimento da ligação a: " + serverHostName + "." + serverPortNum + "!");
            success = false;
        }
        catch (IOException e)   //erro fatal - outras causas
        { 
            GenericIO.writelnString (Thread.currentThread ().getName () + " - ocorreu um erro indeterminado no estabelecimento da ligação a: " + serverHostName + "." + serverPortNum + "!");
            e.printStackTrace ();
            System.exit (1);
        }

        if (!success) return (success);

        try
        { 
            out = new ObjectOutputStream (commSocket.getOutputStream ());
        }
        catch (IOException e)
        { 
            GenericIO.writelnString (Thread.currentThread ().getName () + " - não foi possível abrir o canal de saída do socket!");
            e.printStackTrace ();
            System.exit (1);
        }

        try
        { 
            in = new ObjectInputStream (commSocket.getInputStream ());
        }
        catch (IOException e)
        { 
            GenericIO.writelnString (Thread.currentThread ().getName () + " - não foi possível abrir o canal de entrada do socket!");
            e.printStackTrace ();
            System.exit (1);
        }

        return (success);
    }
    
    /**
     * Close communication channel.
     * Close input and output streams of communication socket.
     * Close communication socket.
     */
    @SuppressWarnings("CallToPrintStackTrace")
    public void close ()
    {
        try
        { 
            in.close();
        }
        catch (IOException e)
        { 
            GenericIO.writelnString (Thread.currentThread ().getName () + " - não foi possível fechar o canal de entrada do socket!");
            e.printStackTrace ();
            System.exit (1);
        }

        try
        { 
            out.close();
        }
        catch (IOException e)
        { 
            GenericIO.writelnString (Thread.currentThread ().getName () + " - não foi possível fechar o canal de saída do socket!");
            e.printStackTrace ();
            System.exit (1);
        }

        try
        { 
            commSocket.close();
        }
        catch (IOException e)
        { 
            GenericIO.writelnString (Thread.currentThread ().getName () + " - não foi possível fechar o socket de comunicação!");
            e.printStackTrace ();
            System.exit (1);
        }
    }
    
    /**
     * Read object from communication channel.
     * @return read object.
     */
    @SuppressWarnings("CallToPrintStackTrace")
    public Object read ()
    {
        Object obj = null;

        try
        { 
            obj = in.readObject ();
        }
        catch (InvalidClassException e)
        { 
            GenericIO.writelnString (Thread.currentThread ().getName () + " - o objecto lido não é passível de desserialização!");
            e.printStackTrace ();
            System.exit (1);
        }
        catch (IOException e)
        { 
            GenericIO.writelnString (Thread.currentThread ().getName () + " - erro na leitura de um objecto do canal de entrada do socket de comunicação!");
            e.printStackTrace ();
            System.exit (1);
        }
        catch (ClassNotFoundException e)
        { 
            GenericIO.writelnString (Thread.currentThread ().getName () + " - o objecto lido corresponde a um tipo de dados desconhecido!");
            e.printStackTrace ();
            System.exit (1);
        }
        return obj;
    }
    
    /**
     * Write object in the communication channel.
     * @param obj object to be written.
     */
    @SuppressWarnings("CallToPrintStackTrace")
    public void write(Object obj)
    {
        try
        { 
            out.writeObject (obj);
        }
        catch (InvalidClassException e)
        { 
            GenericIO.writelnString (Thread.currentThread ().getName () + " - o objecto a ser escrito não é passível de serialização!");
            e.printStackTrace ();
            System.exit (1);
        }
        catch (NotSerializableException e)
        { 
            GenericIO.writelnString (Thread.currentThread ().getName () + " - o objecto a ser escrito pertence a um tipo de dados não serializável!");
            e.printStackTrace ();
            System.exit (1);
        }
        catch (IOException e)
        { 
            GenericIO.writelnString (Thread.currentThread ().getName () + " - erro na escrita de um objecto do canal de saída do socket de comunicação!");
            e.printStackTrace ();
            System.exit (1);
        }
    }
}
