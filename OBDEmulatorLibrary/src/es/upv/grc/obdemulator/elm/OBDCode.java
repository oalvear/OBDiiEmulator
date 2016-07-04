/**
 *
 */
package es.upv.grc.obdemulator.elm;

/**
 * @author Oscar Alvear
 *
 */
public abstract class OBDCode {

    private String id;
    private int lenght;
    private String description;
    private String units;
    protected String hexValue;
    private long minValue;
    private long maxValue;

    private boolean isMultiline = false;

    protected double[] numericValue = {0};

    protected boolean isGeneric = false;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    public int getLenght() {
        return lenght;
    }

    public void setLenght(int lenght) {
        this.lenght = lenght;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getStringValue() {
        return null;
    }

    protected void setValue(String value) {
        hexValue = /*id + " " +*/ value.toUpperCase();
    }

    public String getValue() {
        //return id + " " + hexValue;
        return id + hexValue;
    }

    public abstract void setValue(int value);

    /**
     * @return the minValue
     */
    public long getMinValue() {
        return minValue;
    }

    /**
     * @param minValue the minValue to set
     */
    public void setMinValue(long minValue) {
        this.minValue = minValue;
    }

    /**
     * @return the maxValue
     */
    public long getMaxValue() {
        return maxValue;
    }

    /**
     * @param maxValue the maxValue to set
     */
    public void setMaxValue(long maxValue) {
        this.maxValue = maxValue;
    }

    public boolean isGeneric() {
        return isGeneric;
    }

    public int getIntValue() {
        return (int) numericValue[0];
    }

    /**
     * @return the isMultiline
     */
    public boolean isIsMultiline() {
        return isMultiline;
    }

    /**
     * @param isMultiline the isMultiline to set
     */
    public void setIsMultiline(boolean isMultiline) {
        this.isMultiline = isMultiline;
    }
}
