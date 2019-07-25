/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import interfaces.broker.Repository_Broker;
import interfaces.spectator.Repository_Spec;
import interfaces.horses.Repository_Horses;

/**
 *
 * @author pedro
 * @author franciscoteixeira
 */
public interface Repository_Itf extends Repository_Broker, Repository_Spec, Repository_Horses{
    
}
