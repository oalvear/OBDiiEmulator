/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package osal;

import it.units.GoogleCommon.GeocodeException;
import it.units.GoogleCommon.Location;
import it.units.GoogleElevation.ElevationRequestor;
import it.units.GoogleElevation.ElevationResponse;

/**
 *
 * @author Usuario
 */
public class GeoUtils {

    private static final double EARTH_RADIUS = 6378137; // equatorial earth radius for EPSG:3857 (Mercator) 

    public static GeoPoint getPosition(double lat, double lng, double bearing, double distance) {
        double dist = distance / EARTH_RADIUS;  // convert dist to angular distance in radians
        double lon1 = Math.toRadians(lng);
        double lat1 = Math.toRadians(lat);
        double brng = Math.toRadians(bearing);

        double lat2 = Math.asin(Math.sin(lat1) * Math.cos(dist)
                + Math.cos(lat1) * Math.sin(dist) * Math.cos(brng));
        double lon2 = lon1 + Math.atan2(Math.sin(brng) * Math.sin(dist) * Math.cos(lat1),
                Math.cos(dist) - Math.sin(lat1) * Math.sin(lat2));
        lon2 = (lon2 + 3 * Math.PI) % (2 * Math.PI) - Math.PI;

        GeoPoint resp = new GeoPoint(Math.toDegrees(lat2), Math.toDegrees(lon2));
        resp.setBearing(((bearing + 180) % 360));
        return resp;
    }

    public static GeoPoint getPosition(GeoPoint position, double distance) {
        return getPosition(position.getLatitude(), position.getLongitude(), position.getBearing(), distance);
    }

    public static double calculateDistance(double userLat, double userLng, double venueLat, double venueLng) {

        double latDistance = Math.toRadians(userLat - venueLat);
        double lngDistance = Math.toRadians(userLng - venueLng);

        double a = (Math.sin(latDistance / 2) * Math.sin(latDistance / 2))
                + (Math.cos(Math.toRadians(userLat)))
                * (Math.cos(Math.toRadians(venueLat)))
                * (Math.sin(lngDistance / 2))
                * (Math.sin(lngDistance / 2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;

    }

    public static GeoPoint setAltitude(GeoPoint position) {
        Location location = new Location();
        location.setLat((float)position.getLatitude());
        location.setLng((float)position.getLongitude());

        ElevationRequestor requestor = new ElevationRequestor();
        ElevationResponse elevationResponse;
        try {
            elevationResponse = requestor.getElevation(location);
            position.setAltitude(elevationResponse.getResults().get(0).getElevation());
            System.out.println(elevationResponse.getResults().get(0));
        } catch (GeocodeException ex) {
            ex.printStackTrace();
        }
        return position;
    }

    public static void main(String args[]) {
        GeoPoint p = getPosition(40.066176, -0.125007, 0, 0);
        setAltitude(p);
        System.out.println(p.getAltitude());
    }

}
