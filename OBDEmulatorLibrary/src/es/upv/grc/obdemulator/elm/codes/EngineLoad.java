package es.upv.grc.obdemulator.elm.codes;

import es.upv.grc.obdemulator.elm.OBDCode;

public class EngineLoad extends OBDCode {

    public static final String PID = "0104";

    public EngineLoad() {
        super();
        setLenght(1);
        setDescription("Load Engine");
        setUnits("%");
        setValue("00");
        setId("4104");
    }

    @Override
    public void setValue(int value) {
        String val = Integer.toHexString(value * 255 / 100);
        numericValue[0] = value;
        for (int i = val.length(); i < getLenght() * 2; i++) {
            val = "0" + val;
        }
        setValue(val);
    }
}
