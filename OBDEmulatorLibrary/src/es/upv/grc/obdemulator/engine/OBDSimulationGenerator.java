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
import java.util.HashMap;
import java.util.Iterator;
import osal.GeoPoint;
import osal.GeoUtils;

/**
 * @author Oscar Alvear
 *
 */
public final class OBDSimulationGenerator extends OBDGenerator {

    public static final int MAX_RPM = 2000;
    public static final int MAX_SPEED = 255;
    public static final int MAX_GEAR = 6;
    public static final int MIN_RPM = 500;

    OBDClientHandler handler;
    OBDVehicle vehicle;
    Date time, lastTime = new Date();

    public OBDSimulationGenerator(OBDClientHandler handler, ECUVehicle pData) {
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
                simulate(handler.getAcceleration(), handler.getRotation());
                updateData();
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
    }

    private void simulatev000(int acc, int rotation) {
        double chRotation = 0.01;
        int chRPM = 100;
        int chSpeedAc = 5;
        int chSpeedDc = 10;
        double speedForGear = 40;

        int dSpeed = -1;

        int rndAcc = (int) (Math.random() * 10 - 5);

        vehicle.setPosition(new GeoPoint(handler.getLatitude(), handler.getLongitude()));

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
                vehicle.setSpeed(Math.min(255, (vehicle.getSpeed() + chSpeedAc * acc / 100)));
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

        vehicle.setSpeed(Math.max(0, vehicle.getSpeed() + dSpeed));

        vehicle.setDireccion(vehicle.getDireccion() + (chRotation * rotation));

        vehicle.setRpm(vehicle.getRpm() + rndAcc);
        vehicle.setLoad(vehicle.getRpm() * 100 / MAX_RPM);

        lastTime = time;
    }

    private void simulate(double acceleration, int rotation) {

        double incRotation = Math.abs(rotation) / 25;

        double chRotation = 0.01 * incRotation;
        double chRPM = 100;
        double chSpeedAc = 10;
        double chSpeedDc = 5;
        double dSpeed = -0.05;

        double dDistance = 0;
        
        int[] vMaxSpeedGear = {0, 30, 50, 80, 110, 180, MAX_SPEED};
        int[] vMinRpmGear = {0, 500, 800, 1200, 1500, 1800, 2000, MAX_RPM};

        int rndAcc = (int) (Math.random() * 100 - 50);
        
        double interval = time.getTime() - lastTime.getTime();

        vehicle.setPosition(new GeoPoint(handler.getLatitude(), handler.getLongitude()));

        vehicle.setTime(vehicle.getTime() + interval / 1000);

        vehicle.setpThrottle(Math.max(0, (int) acceleration));

        if (acceleration >= 0) {
            double incSpeed = acceleration * chSpeedAc * (double) (255 - vehicle.getSpeed()) / (interval * 3600);
            dDistance = (vehicle.getSpeed() - (incSpeed / 2)) * interval / 3600;

            vehicle.setSpeed(Math.min(255f, (vehicle.getSpeedD() + incSpeed)));

            if (vehicle.getSpeed() > vMaxSpeedGear[vehicle.getGear()]) {
                vehicle.setGear(vehicle.getGear() + 1);
                vehicle.setRpm(vMinRpmGear[vehicle.getGear()]);
            } else {
                vehicle.setRpm(vMinRpmGear[vehicle.getGear()] + (int) chRPM * (int) acceleration / 100);
            }
        } else if (acceleration < 0) {
            vehicle.setSpeed(Math.max(0, vehicle.getSpeedD() + chSpeedDc * acceleration / 100));
            dDistance = vehicle.getSpeed() * interval / 3600;

            if (vehicle.getSpeed() < vMaxSpeedGear[vehicle.getGear() - 1]) {
                vehicle.setGear(Math.max(1, vehicle.getGear() - 1));
            }
            vehicle.setRpm(vMinRpmGear[vehicle.getGear()]);
        }
        GeoPoint geoPosition = GeoUtils.getPosition(vehicle.getLatitude(), vehicle.getLongitude(), -vehicle.getDireccion(), dDistance);
        vehicle.setPosition(geoPosition);

        vehicle.setSpeed(Math.max(0, vehicle.getSpeedD() + dSpeed));
        
        vehicle.setDireccion(vehicle.getDireccion() + (chRotation * rotation));
        
        vehicle.setRpm(vehicle.getRpm() + rndAcc);
        vehicle.setLoad(vehicle.getRpm() * 100 / vMinRpmGear[vMinRpmGear.length - 1]);

        lastTime = time;

    }

    private static double simulateReal(double acceleration, int rotation, double V0) {
        double cAcc = acceleration / 100;
        
        double anc = Math.toRadians(0);
        double m = 1550;        //Masa (g)
        double g = 9.8;         //Gravedad(m/s2)
        double u0 = 0.015;
        double u1 = 7e-6;
        double Pm = 200000;     //Potencia Maxima;
        double RPMm = 5000;
        double nt = 0.95;
        double nc = 0.95;
        double S = 2;           //Area frontal
        double Cx = 0.25;       //Coeficiente aerodinamico
        double p = 0.95;        
        
        
        double ur = u0 + u1 * Math.pow(V0,2);
        double Fr = ur * m * g * Math.cos(anc);
        double Fa = (0.5) * S * Cx * p * Math.pow(V0,2);
        double Fc = m * g * Math.sin(anc);
        double Fs = Math.sqrt(Math.pow(Fr, 2) + Math.pow(Fc, 2));
        
//        double resistence = -(Fs + Fa) / m;        
//        System.out.println("Fa: " + Fa);   
//        System.out.println("Fr: " + Fr);   
//        System.out.println("Fc: " + Fc);   
//        System.out.println("Fs: " + Fs);   
//        System.out.println("Rs: " + resistence);   
        
        double Wm = RPMm  * 2 * Math.PI / 60; 
        double We = Wm * cAcc;
        
        double P1 = Pm / Wm; 
        double P2 = Pm / Math.pow(Wm,2);
        double P3 = Pm / Math.pow(Wm, 3);
        double Pe = P1 * We + P2 * Math.pow(We, 2) - P3 * Math.pow(We, 3);
        
        double Feff = Pe / (V0==0?0.1:V0);
        
        double Ftot = Feff - Fa - Fs;
        
        double n = nc * nt;
        double acc = n * Ftot / m;
        
//        System.out.println("Pe: " + Pe);
//        System.out.println("Peff: " + Pe);
        return acc;
    }

    static void simulatePhisic(){
        double m;       //m:    masa
        double a;       //a:    aceleracion
        double Ca;      //Coeficiente aerodinamico
        double F;       //Fuerza
        double S;       //Area del vehiculo
        double p;       //ro:
        double Ra;      //Reistencia aerodinamica
        
    }
    
    
    private void updateData() {
        getData().get(EngineRPM.PID).setValue(vehicle.getRpm());
        getData().get(VehicleSpeed.PID).setValue(vehicle.getSpeed());
        getData().get(EngineLoad.PID).setValue(vehicle.getLoad());
        getData().get(ThrottlePosition.PID).setValue(vehicle.getpThrottle());
        getData().setLatitude(vehicle.getLatitude());
        getData().setLongitude(vehicle.getLongitude());
        getData().setAltitude(vehicle.getAltitude());
        getData().setBearing(vehicle.getBearing());

        int nd = data.size();
        final String[] pids = new String[nd];
        int ii = 0;
        Iterator<String> iter = data.keySet().iterator();
        HashMap<String, Double> values = new HashMap<String, Double>();
        while (iter.hasNext()) {
            String code = iter.next();
            pids[ii] = code;
            values.put("<" + code + ">", (double)data.get(code).getIntValue());
            ii++;
        }
        for (String pid : pids) {
            HashMap<String, Double> vls = values;
            vls.put("<value>", (double)data.get(pid).getIntValue());
            if(data.get(pid).isGeneric()){
                try {
                    if(!((GenericOBDCode)data.get(pid)).getValueFormula().equals("<value>")) {
                        double value = es.upv.grc.obdemulator.utils.Math.calculate(((GenericOBDCode)data.get(pid)).getValueFormula(), vls);
                        data.get(pid).setValue((int)value);
                    }
                } catch (Exception ex) {}
            }
        }

        handler.updateData(vehicle);

    }

    @Override
    public void initData() {

        getData().setCode(PIDSupported.PID, new PIDSupported());
        getData().get(PIDSupported.PID).setValue(0xFFFFFFFF);
        getData().setCode("0110", new GenericOBDCode("4110", 200, "grams/sec", "MAF air flow rate", 2, "100 * <value>", 0, 655));

        getData().setCode("0105", new GenericOBDCode("4105", 80, "C", "Engine coolant temperature", 1, "<value> + 40", -40, 215));

        getData().setCode("010A", new GenericOBDCode("410A", 300, "kPa (gauge)", "Fuel pressure", 1, "<value> / 3", 0, 765));
        getData().setCode("010B", new GenericOBDCode("410B", 100, "kPa (absolute)", "Intake manifold absolute pressure", 1, "<value>", 0, 255));

        getData().setCode("0146", new GenericOBDCode("4146", 25, "C", "Ambient air temperature", 1, "<value> + 40", -40, 215));

        getData().setCode("011B", new GenericOBDCode("411B", 0xC4D5, "C", "Bank 2, Sensor 4: Oxygen sensor voltage, Short term fuel trim", 2, "<value>", 0, 100000));
        getData().setCode("011A", new GenericOBDCode("411A", 0xC4D5, "C", "Bank 2, Sensor 3: Oxygen sensor voltage, Short term fuel trim", 2, "<value>", 0, 100000));
        getData().setCode("0119", new GenericOBDCode("4119", 0xC4D5, "C", "Bank 2, Sensor 2: Oxygen sensor voltage, Short term fuel trim", 2, "<value>", 0, 100000));
        getData().setCode("0118", new GenericOBDCode("4118", 0xC4D5, "C", "Bank 2, Sensor 1: Oxygen sensor voltage, Short term fuel trim", 2, "<value>", 0, 100000));
        getData().setCode("0117", new GenericOBDCode("4117", 0xFFFF, "C", "Bank 1, Sensor 4: Oxygen sensor voltage, Short term fuel trim", 2, "<value>", 0, 100000));
        getData().setCode("0116", new GenericOBDCode("4116", 0xFFFF, "C", "Bank 1, Sensor 3: Oxygen sensor voltage, Short term fuel trim", 2, "<value>", 0, 100000));
        getData().setCode("0115", new GenericOBDCode("4115", 0xFFFF, "C", "Bank 1, Sensor 2: Oxygen sensor voltage, Short term fuel trim", 2, "<value>", 0, 100000));
        getData().setCode("0114", new GenericOBDCode("4114", 0xFFFF, "C", "Bank 1, Sensor 1: Oxygen sensor voltage, Short term fuel trim", 2, "<value>", 0, 100000));

        getData().setCode("0142", new GenericOBDCode("4142", 30, "V", "Control module voltage", 2, "<value> * 1000", 0, 65));
        getData().setCode("010E", new GenericOBDCode("410E", 2, "° relative to #1 cylinder", "Timing advance", 1, "(<value> + 64) * 2", -64, 64));
        getData().setCode("010F", new GenericOBDCode("410F", 65, "C", "Intake air temperature", 1, "<value> + 40", -40, 215));

        getData().setCode("0133", new GenericOBDCode("4133", 100, "C", "Barometric pressure", 1, "<value>", 0, 255));

        getData().setCode("012F", new GenericOBDCode("412F", 50, "%", "Fuel Level Input", 1, "255 / 100 * <value>", 0, 100));
        updateData();
    }
    
    public static void main(String args[]) {
        double Vx = 5;
        double V0 = 1;
        double a = 1;
        Date t1 = new Date();
        double tt = 0;
        int i = 0;
        //while(tt < 100*1000) {
        int tp = 25;
        while((int)(a * 100) > 0) {
            tp = Math.max(0, Math.min(tp - 4 + ((int)(Math.random() * 9)),100));
            Date t2 = new Date();
            double t = (t2.getTime() - t1.getTime());
            t1 = t2;
            a = OBDSimulationGenerator.simulateReal(100, 0, V0);
            Vx = V0 + a * t / 1000;
            V0 = Vx;
            tt += t;
            System.out.println("" + tt / 1000 + "\t" + (int)(Vx * 3.6) + "\t" + a + "\t");
            i++;
//            try {
//                Thread.sleep(50);
//            } catch (InterruptedException ex) {
//                Logger.getLogger(OBDSimulationGenerator.class.getName()).log(Level.SEVERE, null, ex);
//            }
        }
    }
    
}
