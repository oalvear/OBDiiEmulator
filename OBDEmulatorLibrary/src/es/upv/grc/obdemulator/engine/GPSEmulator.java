/**
 *
 */
package es.upv.grc.obdemulator.engine;

import es.upv.grc.obdemulator.elm.ECUVehicle;
import es.upv.grc.obdemulator.elm.codes.EngineLoad;
import es.upv.grc.obdemulator.elm.codes.EngineRPM;
import es.upv.grc.obdemulator.elm.codes.GenericOBDCode;
import es.upv.grc.obdemulator.elm.codes.PIDSupported;
import es.upv.grc.obdemulator.elm.codes.ThrottlePosition;
import es.upv.grc.obdemulator.elm.codes.VehicleSpeed;
import java.util.Date;
import osal.GeoPoint;

/**
 * @author Oscar Alvear
 *
 */
public final class GPSEmulator extends OBDGenerator {

    public static final int MAX_RPM = 2000;
    public static final int MAX_GEAR = 5;
    public static final int MIN_RPM = 500;
    OBDClientHandler handler;
    OBDVehicle vehicle;
    Date time, lastTime = new Date();

    public GPSEmulator(OBDClientHandler handler, ECUVehicle pData) {
        this.handler = handler;
        vehicle = new OBDVehicle();
        setData(pData);
        initData();
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            try {
                time = new Date();
                simulate(handler.getAcceleration(),handler.getRotation());
                updateData();
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
    }

    private void simulate(int acc, int rot) {
        double rotation = 0.01;
        int chRPM = 100;
        int chSpeedAc = 5;
        int chSpeedDc = 10;
        int speedForGear = 40;
        int rndAcc = (int) (Math.random() * 10 - 5);

        vehicle.setPosition(new GeoPoint(handler.getLatitude(),handler.getLongitude()));
//        vehicle.setLatitude(handler.getLatitude());
//        vehicle.setLongitude(handler.getLongitude());
        
        double interval = time.getTime() - lastTime.getTime();
        vehicle.setTime(vehicle.getTime() + interval / 1000);
        if (acc > 5) {
            vehicle.setpThrottle(acc);
            if (vehicle.getRpm() >= MAX_RPM && vehicle.getGear() < MAX_GEAR) {
                vehicle.setGear(vehicle.getGear() + 1);
                vehicle.setRpm(MIN_RPM);
            }
            if (vehicle.getRpm() < MAX_RPM) {
                vehicle.setRpm(vehicle.getRpm() + chRPM * acc / 100);
                vehicle.setSpeed(vehicle.getSpeed() + chSpeedAc * acc / 100);
            }
        } else if (acc < -5) {
            if (vehicle.getSpeed() <= speedForGear * vehicle.getGear() && vehicle.getGear() > 1) {
                vehicle.setGear(vehicle.getGear() - 1);
                vehicle.setRpm(1000);
            }
            vehicle.setSpeed(Math.max(0, vehicle.getSpeed() + chSpeedDc * acc / 100));
            vehicle.setRpm(MIN_RPM);
            vehicle.setpThrottle(0);
        }
        //if(vehicle.getRotation()>-50 && vehicle.getRotation()<50)
            //vehicle.setRotation(vehicle.getRotation()+(rotation * rot));
        vehicle.setDireccion(vehicle.getDireccion()+(rotation * rot));
        
        vehicle.setRpm(vehicle.getRpm() + rndAcc);
        vehicle.setLoad(vehicle.getRpm() * 100 / MAX_RPM);

        lastTime = time;
    }

    private void updateData() {
        getData().get(EngineRPM.PID).setValue(vehicle.getRpm());
        getData().get(VehicleSpeed.PID).setValue(vehicle.getSpeed());
        getData().get(EngineLoad.PID).setValue(vehicle.getLoad());
        getData().get(ThrottlePosition.PID).setValue(vehicle.getpThrottle());
        getData().get("9901").setValue((int)(vehicle.getLatitude() * 1000));
        getData().get("9902").setValue((int)(vehicle.getLongitude() * 1000));
        //getData().get("0110").setValue(200);

        handler.updateData(vehicle);

    }

    @Override
    public void initData() {
        //getData().setCode(PIDSupported.PID, new PIDSupported());
        getData().get(PIDSupported.PID).setValue(0xFFFFFFFF);
        //getData().setCode("0110", new GenericOBDCode("4110",200,"grams/sec","MAF air flow rate",2,"100 * <value>",0,655));
        getData().setCode("9901", new GenericOBDCode("9901",0,"grados","latitude",2,"<value>",-360000,360000));
        getData().setCode("9902", new GenericOBDCode("9902",0,"grados","longitude",2,"<value>",-360000,360000));

        
        getData().setCode("0105", new GenericOBDCode("4105",80,"C","Engine coolant temperature",1,"<value> + 40",-40,215));

        getData().setCode("010A", new GenericOBDCode("410A",300,"kPa (gauge)","Fuel pressure",1,"<value> / 3",0,765));
        getData().setCode("010B", new GenericOBDCode("410B",100,"kPa (absolute)","Intake manifold absolute pressure",1,"<value>",0,255));
        
        
        getData().setCode("0146", new GenericOBDCode("4146",25,"C","Ambient air temperature",1,"<value> + 40",-40,215));

        
        
        getData().setCode("011B", new GenericOBDCode("411B",0xC4D5,"C","Bank 2, Sensor 4: Oxygen sensor voltage, Short term fuel trim",2,"<value>",0,100000));
        getData().setCode("011A", new GenericOBDCode("411A",0xC4D5,"C","Bank 2, Sensor 3: Oxygen sensor voltage, Short term fuel trim",2,"<value>",0,100000));
        getData().setCode("0119", new GenericOBDCode("4119",0xC4D5,"C","Bank 2, Sensor 2: Oxygen sensor voltage, Short term fuel trim",2,"<value>",0,100000));
        getData().setCode("0118", new GenericOBDCode("4118",0xC4D5,"C","Bank 2, Sensor 1: Oxygen sensor voltage, Short term fuel trim",2,"<value>",0,100000));
        getData().setCode("0117", new GenericOBDCode("4117",0xFFFF,"C","Bank 1, Sensor 4: Oxygen sensor voltage, Short term fuel trim",2,"<value>",0,100000));
        getData().setCode("0116", new GenericOBDCode("4116",0xFFFF,"C","Bank 1, Sensor 3: Oxygen sensor voltage, Short term fuel trim",2,"<value>",0,100000));
        getData().setCode("0115", new GenericOBDCode("4115",0xFFFF,"C","Bank 1, Sensor 2: Oxygen sensor voltage, Short term fuel trim",2,"<value>",0,100000));
        getData().setCode("0114", new GenericOBDCode("4114",0xFFFF,"C","Bank 1, Sensor 1: Oxygen sensor voltage, Short term fuel trim",2,"<value>",0,100000));
        
        getData().setCode("0142", new GenericOBDCode("4142",30,"V","Control module voltage",2,"<value> * 1000",0,65));
        getData().setCode("010E", new GenericOBDCode("410E",2,"° relative to #1 cylinder","Timing advance",1,"(<value> + 64) * 2",-64,64));
        getData().setCode("010F", new GenericOBDCode("410F",65,"C","Intake air temperature",1,"<value> + 40",-40,215));
        
        getData().setCode("0133", new GenericOBDCode("4133",100,"C","Barometric pressure",1,"<value>",0,255));

        
        getData().setCode("012F", new GenericOBDCode("412F",50,"%","Fuel Level Input",1,"255 / 100 * <value>",0,100));
        updateData();
    }

    public static void main(String[] args) {
        String resp = "410100";
        String pid = "0" + resp.substring(1,4);
        System.out.println(pid);
    }
}