package es.upv.grc.obdemulator.elm;

import es.upv.grc.obdemulator.elm.codes.EngineLoad;
import es.upv.grc.obdemulator.elm.codes.EngineRPM;
import es.upv.grc.obdemulator.elm.codes.MonitorStatus;
import es.upv.grc.obdemulator.elm.codes.PIDSupported;
import es.upv.grc.obdemulator.elm.codes.ThrottlePosition;
import es.upv.grc.obdemulator.elm.codes.VehicleSpeed;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Oscar Alvear
 */
public final class ECUVehicle extends HashMap<String, OBDCode> {

    private final Map<String, OBDATParam> atParams;
    private int baud = 250;
    private boolean echo = true, memory = false, linefeed = true, 
            header = false, space = true, DLC = false;
    private String did = "";
    private final int[] bauds = {500, 41, 10, 10, 10, 10, 500, 500, 250, 250, 250, 125, 50};
    private final String[] dps = {"Auto", 
        "SAE J1850 PWM", "SAE J1850 VWM", 
        "ISO 9141-2", 
        "ISO 14230-4 KWP", "ISO 14230-4 KWP", 
        "ISO 14230-4 CAN", "ISO 15765-4 CAN", "ISO 15765-4 CAN", "ISO 15765-4 CAN", 
        "SAE J1939 CAN", 
        "USER1 CAN", "USER2CAN"};
    private String DP = "Auto";
    private int DPN = 0;

    private double latitude = 0.0f, longitude = 0.0f, altitude = 0.0f, bearing = 0.0f;

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ECUVehicle() {
        super();
        atParams = new HashMap<String, OBDATParam>();
        setAtCommand("ATZ", "ELM327 v2.0");
        setAtCommand("ATD", "OK");
        setAtCommand("ATI", "ELM327 v2.0");
        setAtCommand("ATDPN", "2.0");
        setAtCommand("AT@1", "BOSS: Bluetooth OBDii Simple Simulator");
        
        setAtCommand("ATAT0", "OK");
        setAtCommand("ATAT1", "OK");
        setAtCommand("ATAT2", "OK");
        
        setAtCommand("ATE0", "OK");
        setAtCommand("ATE1", "OK");
        setAtCommand("ATM0", "OK");
        setAtCommand("ATM1", "OK");
        setAtCommand("ATL0", "OK");
        setAtCommand("ATL1", "OK");
        setAtCommand("ATS0", "OK");
        setAtCommand("ATS1", "OK");
        setAtCommand("ATH0", "OK");
        setAtCommand("ATH1", "OK");
        setAtCommand("ATD0", "OK");
        setAtCommand("ATD1", "OK");

        setCode(PIDSupported.PID, new PIDSupported());
        setCode(MonitorStatus.PID, new MonitorStatus());
        setCode(EngineRPM.PID, new EngineRPM());
        setCode(VehicleSpeed.PID, new VehicleSpeed());
        setCode(EngineLoad.PID, new EngineLoad());
        setCode(ThrottlePosition.PID, new ThrottlePosition());

    }

    public void setCode(String id, OBDCode value) {
        super.put(id, value);
    }

    public void setCode(String id, String value) {
        this.get(id).setValue(value);
    }

    public void setAtCommand(String param, String value) {
        atParams.put(param, new OBDATParam(param, value, ""));
    }

    public void setAtCommand(String param, OBDATParam atParam) {
        atParams.put(param, atParam);
    }

    public String getAtParam(String param) {
        try {
            param = param.replace(" ", "");
            if (param.startsWith("ATZ") || param.startsWith("ATD") ) {
                reset();
            } else if (param.startsWith("ATE0")) {
                echo = false;
            } else if (param.startsWith("ATE1")) {
                echo = true;
            } else if (param.startsWith("ATM0")) {
                memory = false;
            } else if (param.startsWith("ATM1")) {
                memory = true;
            } else if (param.startsWith("ATL0")) {
                linefeed = false;
            } else if (param.startsWith("ATL1")) {
                linefeed = true;
            } else if (param.startsWith("ATH0")) {
                header = false;
            } else if (param.startsWith("ATH1")) {
                header = true;
            } else if (param.startsWith("ATS0")) {
                space = false;
            } else if (param.startsWith("ATS1")) {
                space = true;
            } else if (param.startsWith("ATD0")) {
                DLC = false;
            } else if (param.startsWith("ATD1")) {
                DLC = true;
            } else if (param.startsWith("ATST")) {
                setAtCommand(param, "OK");
            } else if (param.startsWith("ATSP0")) {
                DPN = 0;
                setAtCommand(param, "OK");
            } else if (param.startsWith("ATSP1")) {
                DPN = 1;
                setAtCommand(param, "OK");
            } else if (param.startsWith("ATSP2")) {
                DPN = 2;
                setAtCommand(param, "OK");
            } else if (param.startsWith("ATSP3")) {
                DPN = 3;
                setAtCommand(param, "OK");
            } else if (param.startsWith("ATSP4")) {
                DPN = 4;
                setAtCommand(param, "OK");
            } else if (param.startsWith("ATSP5")) {
                DPN = 5;
                setAtCommand(param, "OK");
            } else if (param.startsWith("ATSP6")) {
                DPN = 6;
                setAtCommand(param, "OK");
            } else if (param.startsWith("ATSP7")) {
                DPN = 7;
                setAtCommand(param, "OK");
            } else if (param.startsWith("ATSP8")) {
                DPN = 8;
                setAtCommand(param, "OK");
            } else if (param.startsWith("ATSP9")) {
                DPN = 9;
                setAtCommand(param, "OK");
            } else if (param.startsWith("ATSPA")) {
                DPN = 10;
                setAtCommand(param, "OK");
            } else if (param.startsWith("ATSPB")) {
                DPN = 11;
                setAtCommand(param, "OK");
            } else if (param.startsWith("ATSPC")) {
                DPN = 12;
                setAtCommand(param, "OK");
            }
            setAtCommand("ATDPN", String.valueOf((float)DPN));
            setAtCommand("ATDP", dps[DPN]);
            baud = bauds[DPN];

            String resp = atParams.get(param).getValue();
            return (resp != null ? resp : "");
        } catch (Exception e) {
            return "?";
        }
    }
    
    private String CR() {
        return "\r" + (linefeed?"\n":"") + ">";
    }

    private String setSpace(String resp) {
        if(!space)
            return resp;
        String resp2 = "";
        int nd = resp.length() - 1;
        for (int i = 0; i < nd; i = i + 2) {
            try {
                resp2 += resp.substring(i,i + 2) + " ";
            }catch(Exception e){
                
            }
        }
        return resp2;
    }
    private void reset() {
        echo = false;
        memory = false;
        linefeed = true;
        header = false;
        space = true;
        DLC = false;
        baud = bauds[0];
    }

    public String getCode(String id) throws NullPointerException {
        id = id.toUpperCase();
        id = id.replace(" ", "");

        String resp;
        if (id.startsWith("AT")) {
            resp = getAtParam(id);
        } else if (id.startsWith("GPS")) {
            return getCoodinate();
        } else {

            if (get(id) != null) {
                resp = (echo ? setSpace(get(id).getId()): " ") + setSpace(get(id).getValue());
            } else {
                resp = id + " NODATA";
            }
            try {
                Thread.sleep((resp.length()) * 1000 / baud);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return resp + CR();
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public double getBearing() {
        return bearing;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public void setBearing(double bearing) {
        this.bearing = bearing;
    }

    public String getCoodinate() {
        return latitude + ":" + longitude;
    }

}
