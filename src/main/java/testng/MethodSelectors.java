
package testng;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "methodSelector"
})
@XmlRootElement(name = "method-selectors")
public class MethodSelectors {

    @XmlElement(name = "method-selector")
    protected List<MethodSelector> methodSelector;

    /**
     * Gets the value of the methodSelector property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the methodSelector property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMethodSelector().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MethodSelector }
     * 
     * 
     */
    public List<MethodSelector> getMethodSelector() {
        if (methodSelector == null) {
            methodSelector = new ArrayList<MethodSelector>();
        }
        return this.methodSelector;
    }

}
