package es.upv.grc.obdemulator.elm.codes;

import es.upv.grc.obdemulator.elm.OBDCode;

public class VehicleSpeed extends OBDCode {

    public static final String PID = "010D";

    public VehicleSpeed() {
        super();
        setLenght(1);
        setDescription("Vehicle speed");
        setUnits("km/h");
        setValue("00");
        setId("410D");
    }

    @Override
    public void setValue(int value) {
        if (value > 0 && value <= 255) {
            numericValue[0] = value;
            String val = Integer.toHexString(value);
            for (int i = val.length(); i < getLenght() * 2; i++) {
                val = "0" + val;
            }
            setValue(val);
        }
    }

}
