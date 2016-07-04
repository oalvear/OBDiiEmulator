package es.upv.grc.obdemulator.elm.codes;

import es.upv.grc.obdemulator.elm.OBDCode;

public class EngineRPM extends OBDCode {

    public static final String PID = "010C";

    public EngineRPM() {
        super();
        setLenght(2);
        setDescription("Engine RPM");
        setUnits("rpm");
        setValue("0000");
        setId("410C");
    }

    @Override
    public void setValue(int value) {
        numericValue[0] = value;        
        String val = Integer.toHexString(value * 4);
        for (int i = val.length(); i < getLenght() * 2; i++) {
            val = "0" + val;
        }
        setValue(val);
    }
}
