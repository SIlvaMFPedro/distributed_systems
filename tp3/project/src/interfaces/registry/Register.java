/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces.registry;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;


/**
 *
 * @author pedro
 * @author franciscoteixeira
 */

/**
 *  This data type defines the operational interface of a remote object of type RegisterRemoteObject.
 *  It provides the functionality to register remote objects in the local registry service.
 */
public interface Register extends Remote{
    
    /**
     * Register bind method that binds a remote reference to the specified name in the registry.
     * @param name name to associate with the remote reference.
     * @param reference reference to remote object.
     * @throws RemoteException If either the invocation of the remote method, or the communication with the registry
     * @throws AlreadyBoundException If the name we wish to associate is already in use
     */
    public void bind(String name, Remote reference) throws RemoteException, AlreadyBoundException;
    
    /**
     * Register unbind method that removes the binding for the specified name in the registry;
     * @param name associated reference name
     * @throws RemoteException If either the invocation of the remote method, or the communication with service registry has failed.
     * @throws NotBoundException If the associated reference name is not in use.
     */
    public void unbind(String name) throws RemoteException, NotBoundException;
    
    /**
     * Register re-bind method that replaces the binding for the specified name in this registry with the supplied remote reference.
     * If a previous binding for the specified name exists, it is discarded.
     * @param name name we wish to associate with the remote reference.
     * @param reference reference to remote object
     * @throws RemoteException If either the invocation of the remote method, or the communication with service registry has failed.
     */
    public void rebind(String name, Remote reference) throws RemoteException;
}
