
package testng;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "selectorClassOrScript"
})
@XmlRootElement(name = "method-selector")
public class MethodSelector {

    @XmlElements({
        @XmlElement(name = "selector-class", type = SelectorClass.class),
        @XmlElement(name = "script", type = Script.class)
    })
    protected List<Object> selectorClassOrScript;

    /**
     * Gets the value of the selectorClassOrScript property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the selectorClassOrScript property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSelectorClassOrScript().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SelectorClass }
     * {@link Script }
     * 
     * 
     */
    public List<Object> getSelectorClassOrScript() {
        if (selectorClassOrScript == null) {
            selectorClassOrScript = new ArrayList<Object>();
        }
        return this.selectorClassOrScript;
    }

}
