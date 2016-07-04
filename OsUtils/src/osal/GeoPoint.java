/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package osal;

/**
 *
 * @author Usuario
 */
public class GeoPoint {

    double latitude = 0;
    double longitude = 0;
    double bearing = 0;
    double altitude = 0;

    public GeoPoint(double latitude, double longitude) {
        this(latitude,longitude,0);
        this.altitude = 0;
    }

    public GeoPoint(double latitude, double longitude, double bearing) {
        this.altitude = 0;
        this.bearing = bearing;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getBearing() {
        return bearing;
    }
    
    public double getAltitude() {
        return altitude;
    }
    
    public void setBearing(double bearing) {
        this.bearing = bearing;
    }
    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }
}
