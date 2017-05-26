
package testng;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "suiteFile"
})
@XmlRootElement(name = "suite-files")
public class SuiteFiles {

    @XmlElement(name = "suite-file")
    protected List<SuiteFile> suiteFile;

    /**
     * Gets the value of the suiteFile property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the suiteFile property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSuiteFile().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SuiteFile }
     * 
     * 
     */
    public List<SuiteFile> getSuiteFile() {
        if (suiteFile == null) {
            suiteFile = new ArrayList<SuiteFile>();
        }
        return this.suiteFile;
    }

}
