/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.grc.obdemulator.engine;

import osal.GeoPoint;

/**
 *
 * @author Usuario
 */
public class OBDVehicle {

    private double speed = 1;
    private int rpm = 0;
    private int acceleration = 0;
    private int gear = 1;
    private int load = 0;
    private int pThrottle = 0;
    private double time = 0;
    private double rotation = 0;
    private double direccion = 0;
    GeoPoint position = new GeoPoint(0, 0);
    

    /**
     * @return the speed
     */
    public int getSpeed() {
        return (int)speed;
    }
    
    /**
     * @return the speed
     */
    public double getSpeedD() {
        return speed;
    }

    /**
     * @param speed the speed to set
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * @return the rpm
     */
    public int getRpm() {
        return rpm;
    }

    /**
     * @param rpm the rpm to set
     */
    public void setRpm(int rpm) {
        this.rpm = rpm;
    }

    /**
     * @return the acceleration
     */
    public int getAcceleration() {
        return acceleration;
    }

    /**
     * @param acceleration the acceleration to set
     */
    public void setAcceleration(int acceleration) {
        this.acceleration = acceleration;
    }

    /**
     * @return the gear
     */
    public int getGear() {
        return gear;
    }

    /**
     * @param gear the gear to set
     */
    public void setGear(int gear) {
        this.gear = gear;
    }

    /**
     * @return the load
     */
    public int getLoad() {
        return load;
    }

    /**
     * @param load the load to set
     */
    public void setLoad(int load) {
        this.load = load;
    }

    /**
     * @return the pThrottle
     */
    public int getpThrottle() {
        return pThrottle;
    }

    /**
     * @param pThrottle the pThrottle to set
     */
    public void setpThrottle(int pThrottle) {
        this.pThrottle = pThrottle;
    }

    /**
     * @return the time
     */
    public double getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(double time) {
        this.time = time;
    }

    /**
     * @return the rotation
     */
    public double getRotation() {
        return rotation;
    }

    /**
     * @param rotation the rotation to set
     */
    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    /**
     * @return the direccion
     */
    public double getDireccion() {
        return direccion;
    }

    /**
     * @param direccion the direccion to set
     */
    public void setDireccion(double direccion) {
        this.direccion = direccion;
    }

    public void setPosition(GeoPoint position) {
        this.position = position;
    }

    public double getLatitude() {
        return position.getLatitude();
    }

    public double getLongitude() {
        return position.getLongitude();
    }
    
    public double getBearing() {
        return position.getBearing();
    }
    
    public double getAltitude() {
        return position.getAltitude();
    }

}
