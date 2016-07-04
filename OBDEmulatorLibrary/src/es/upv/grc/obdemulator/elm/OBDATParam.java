/**
 *
 */
package es.upv.grc.obdemulator.elm;

/**
 * @author Oscar Alvear
 *
 */
public class OBDATParam {

    private String description;
    private String id;
    private String value;

    /**
     *
     * @param id
     * @param value
     * @param description
     */
    public OBDATParam(String id, String value, String description) {
        this.id = id;
        this.value = value;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    protected void setValue(String value) {
        this.value = value;
    }

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

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

}
