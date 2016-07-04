package es.upv.grc.obdemulator.elm.codes;

import es.upv.grc.obdemulator.elm.OBDCode;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class GenericOBDCode extends OBDCode {

    public static final String PID = "";

    private String formula;
    private String valueFormula;
    

    public GenericOBDCode(String id, int defaultValue, String units, String description, int length, String formula, int minValue, int maxValue) {
        super();
        isGeneric = true;
        this.formula = formula;
        setMinValue(minValue);
        setMaxValue(maxValue);
        setLenght(length);
        setDescription(description);
        setUnits(units);
        setId(id);
        setValue(defaultValue);
        setValueFormula("<value>");
    }

    @Override
    public void setValue(int value) {
        if (value >= getMinValue() && value <= getMaxValue()) {
            numericValue[0] = value;
            try {
                HashMap<String,Double> values = new HashMap<String,Double>();
                values.put("<value>", Double.valueOf(value));
                double result = es.upv.grc.obdemulator.utils.Math.calculate(formula, values);
                String val = Integer.toHexString((int)result);
                for (int i = val.length(); i < getLenght() * 2; i++) {
                    val = "0" + val;
                }
                setValue(val);
            } catch (Exception ex) {
                Logger.getLogger(GenericOBDCode.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * @return the formula
     */
    public String getFormula() {
        return formula;
    }

    /**
     * @param formula the formula to set
     */
    public void setFormula(String formula) {
        this.formula = formula;
    }    /**
     * @return the formula
     */
    public String getValueFormula() {
        return valueFormula;
    }

    /**
     * @param formula the formula to set
     */
    public void setValueFormula(String formula) {
        this.valueFormula = formula;
    }
    
}
