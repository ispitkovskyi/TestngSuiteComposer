
package testng;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

import static common.Constants.METHODS;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "includeOrExcludeOrParameter"
})
@XmlRootElement(name = "methods")
public class Methods  implements INamedSuiteMember{

    @XmlElements({
        @XmlElement(name = "include", type = Include.class),
        @XmlElement(name = "exclude", type = Exclude.class),
        @XmlElement(name = "parameter", type = Parameter.class)
    })
    protected List<Object> includeOrExcludeOrParameter;

    /**
     * Gets the value of the name property.
     * Stub for this no-named node
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getName() {
        return METHODS;
    }

    /**
     * Gets the value of the includeOrExcludeOrParameter property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the includeOrExcludeOrParameter property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIncludeOrExcludeOrParameter().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Include }
     * {@link Exclude }
     * {@link Parameter }
     * 
     * 
     */
    public List<Object> getIncludeOrExcludeOrParameter() {
        if (includeOrExcludeOrParameter == null) {
            includeOrExcludeOrParameter = new ArrayList<Object>();
        }
        return this.includeOrExcludeOrParameter;
    }

}
