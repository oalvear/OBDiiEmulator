/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.grc.obdemulator.connection;

/**
 *
 * @author Usuario
 */
public interface BluetoothConnectionHandler {
    
    public void updateRequest(String msg);
    public void updateResponse(String msg);
    public void updateLog(String msg);
    
}
