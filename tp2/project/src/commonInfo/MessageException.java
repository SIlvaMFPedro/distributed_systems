/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commonInfo;

/**
 * Este tipo de dados define uma excepção que é lançada se a mensagem for inválida.
 */

/**
 *
 * @author pedro
 */
public class MessageException extends Exception{
    /**
     * Message that created the exception.
     */
     private Message msg;
     
     /**
      * Message Exception Instantiation.
      * @param error_message message with error condition.
      * @param msg message that created the exception.
      */
     public MessageException(String error_message, Message msg){
         super(error_message);
         this.msg = msg;
     }
     
     /**
      * Return message that created the exception.
      * @return message
      */
     public Message getMessageValue(){
         return msg;
     }
    
}
