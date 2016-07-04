package es.upv.grc.obdemulator.elm.codes;

import es.upv.grc.obdemulator.elm.OBDCode;

/**
 *
 * @author Usuario
 */
public class PIDSupported extends OBDCode {

    public static final String PID = "0100";

    public PIDSupported() {
        super();
        setLenght(4);
        setDescription("PIDs supported [01 - 20]");
        setUnits("pids");
        setValue("FFFFFFFF");
        setId("4100");
    }

    @Override
    public void setValue(int value) {
        String val = Integer.toHexString(value);
        for (int i = val.length(); i < 8; i++) {
            val = "0" + val;
        }
        setValue(val);
    }

}
