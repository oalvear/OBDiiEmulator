package es.upv.grc.obdemulator.elm.codes;

import es.upv.grc.obdemulator.elm.OBDCode;

public class MonitorStatus extends OBDCode {

    public static final String PID = "0101";

    public MonitorStatus() {
        super();
        setLenght(4);
        setDescription("Monitor Status");
        setUnits("");
        setValue("FFFFFFFE");
        setId("4101");
    }

    @Override
    public void setValue(int value) {
        String val = Integer.toHexString(value);
        for (int i = val.length(); i < getLenght() * 2; i++) {
            val = "0" + val;
        }
        setValue(val);
    }

}
