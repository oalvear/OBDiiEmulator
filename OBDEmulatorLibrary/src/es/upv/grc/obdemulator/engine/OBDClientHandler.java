/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.grc.obdemulator.engine;

/**
 *
 * @author Usuario
 */
public interface OBDClientHandler {
    public void updateData(OBDVehicle vehicle);
    public int getAcceleration();
    public int getRotation();
    public double getLatitude();
    public double getLongitude();
}
