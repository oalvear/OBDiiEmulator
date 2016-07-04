package es.upv.grc.obdemulator.elm.codes;

import es.upv.grc.obdemulator.elm.OBDCode;

public class ThrottlePosition extends OBDCode {

    public static final String PID = "0111";

    public ThrottlePosition() {
        super();
        setLenght(1);
        setDescription("Throttle Position");
        setUnits("%");
        setValue("00");
        setId("4111");
    }

    @Override
    public void setValue(int value) {
        if (value > 0 && value <= 100) {
            numericValue[0] = value;            
            String val = Integer.toHexString(value * 255 / 100);
            for (int i = val.length(); i < getLenght() * 2; i++) {
                val = "0" + val;
            }
            setValue(val);
        }
    }

}
