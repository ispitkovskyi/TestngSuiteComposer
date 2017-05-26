
package testng;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "groups",
    "listenersOrPackagesOrTestOrParameterOrMethodSelectorsOrSuiteFiles"
})
@XmlRootElement(name = "suite")
public class Suite {

    @XmlAttribute(name = "name", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String name;
    @XmlAttribute(name = "junit")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String junit;
    @XmlAttribute(name = "verbose")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String verbose;
    @XmlAttribute(name = "parallel")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String parallel;
    @XmlAttribute(name = "parent-module")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String parentModule;
    @XmlAttribute(name = "guice-stage")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String guiceStage;
    @XmlAttribute(name = "configfailurepolicy")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String configfailurepolicy;
    @XmlAttribute(name = "thread-count")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String threadCount;
    @XmlAttribute(name = "annotations")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String annotations;
    @XmlAttribute(name = "time-out")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String timeOut;
    @XmlAttribute(name = "skipfailedinvocationcounts")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String skipfailedinvocationcounts;
    @XmlAttribute(name = "data-provider-thread-count")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String dataProviderThreadCount;
    @XmlAttribute(name = "object-factory")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String objectFactory;
    @XmlAttribute(name = "group-by-instances")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String groupByInstances;
    @XmlAttribute(name = "preserve-order")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String preserveOrder;
    @XmlAttribute(name = "allow-return-values")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String allowReturnValues;
    protected Groups groups;
    @XmlElements({
        @XmlElement(name = "listeners", type = Listeners.class),
        @XmlElement(name = "packages", type = Packages.class),
        @XmlElement(name = "test", type = Test.class),
        @XmlElement(name = "parameter", type = Parameter.class),
        @XmlElement(name = "method-selectors", type = MethodSelectors.class),
        @XmlElement(name = "suite-files", type = SuiteFiles.class)
    })
    protected List<Object> listenersOrPackagesOrTestOrParameterOrMethodSelectorsOrSuiteFiles;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the junit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJunit() {
        if (junit == null) {
            return "false";
        } else {
            return junit;
        }
    }

    /**
     * Sets the value of the junit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJunit(String value) {
        this.junit = value;
    }

    /**
     * Gets the value of the verbose property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVerbose() {
        return verbose;
    }

    /**
     * Sets the value of the verbose property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVerbose(String value) {
        this.verbose = value;
    }

    /**
     * Gets the value of the parallel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParallel() {
        if (parallel == null) {
            return "false";
        } else {
            return parallel;
        }
    }

    /**
     * Sets the value of the parallel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParallel(String value) {
        this.parallel = value;
    }

    /**
     * Gets the value of the parentModule property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParentModule() {
        return parentModule;
    }

    /**
     * Sets the value of the parentModule property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParentModule(String value) {
        this.parentModule = value;
    }

    /**
     * Gets the value of the guiceStage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGuiceStage() {
        if (guiceStage == null) {
            return "DEVELOPMENT";
        } else {
            return guiceStage;
        }
    }

    /**
     * Sets the value of the guiceStage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGuiceStage(String value) {
        this.guiceStage = value;
    }

    /**
     * Gets the value of the configfailurepolicy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConfigfailurepolicy() {
        if (configfailurepolicy == null) {
            return "skip";
        } else {
            return configfailurepolicy;
        }
    }

    /**
     * Sets the value of the configfailurepolicy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConfigfailurepolicy(String value) {
        this.configfailurepolicy = value;
    }

    /**
     * Gets the value of the threadCount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getThreadCount() {
        if (threadCount == null) {
            return "5";
        } else {
            return threadCount;
        }
    }

    /**
     * Sets the value of the threadCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setThreadCount(String value) {
        this.threadCount = value;
    }

    /**
     * Gets the value of the annotations property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnnotations() {
        return annotations;
    }

    /**
     * Sets the value of the annotations property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnnotations(String value) {
        this.annotations = value;
    }

    /**
     * Gets the value of the timeOut property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimeOut() {
        return timeOut;
    }

    /**
     * Sets the value of the timeOut property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimeOut(String value) {
        this.timeOut = value;
    }

    /**
     * Gets the value of the skipfailedinvocationcounts property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSkipfailedinvocationcounts() {
        if (skipfailedinvocationcounts == null) {
            return "false";
        } else {
            return skipfailedinvocationcounts;
        }
    }

    /**
     * Sets the value of the skipfailedinvocationcounts property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSkipfailedinvocationcounts(String value) {
        this.skipfailedinvocationcounts = value;
    }

    /**
     * Gets the value of the dataProviderThreadCount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataProviderThreadCount() {
        if (dataProviderThreadCount == null) {
            return "10";
        } else {
            return dataProviderThreadCount;
        }
    }

    /**
     * Sets the value of the dataProviderThreadCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataProviderThreadCount(String value) {
        this.dataProviderThreadCount = value;
    }

    /**
     * Gets the value of the objectFactory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObjectFactory() {
        return objectFactory;
    }

    /**
     * Sets the value of the objectFactory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObjectFactory(String value) {
        this.objectFactory = value;
    }

    /**
     * Gets the value of the groupByInstances property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGroupByInstances() {
        if (groupByInstances == null) {
            return "false";
        } else {
            return groupByInstances;
        }
    }

    /**
     * Sets the value of the groupByInstances property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGroupByInstances(String value) {
        this.groupByInstances = value;
    }

    /**
     * Gets the value of the preserveOrder property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPreserveOrder() {
        if (preserveOrder == null) {
            return "true";
        } else {
            return preserveOrder;
        }
    }

    /**
     * Sets the value of the preserveOrder property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPreserveOrder(String value) {
        this.preserveOrder = value;
    }

    /**
     * Gets the value of the allowReturnValues property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAllowReturnValues() {
        if (allowReturnValues == null) {
            return "false";
        } else {
            return allowReturnValues;
        }
    }

    /**
     * Sets the value of the allowReturnValues property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAllowReturnValues(String value) {
        this.allowReturnValues = value;
    }

    /**
     * Gets the value of the groups property.
     * 
     * @return
     *     possible object is
     *     {@link Groups }
     *     
     */
    public Groups getGroups() {
        return groups;
    }

    /**
     * Sets the value of the groups property.
     * 
     * @param value
     *     allowed object is
     *     {@link Groups }
     *     
     */
    public void setGroups(Groups value) {
        this.groups = value;
    }

    /**
     * Gets the value of the listenersOrPackagesOrTestOrParameterOrMethodSelectorsOrSuiteFiles property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listenersOrPackagesOrTestOrParameterOrMethodSelectorsOrSuiteFiles property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListenersOrPackagesOrTestOrParameterOrMethodSelectorsOrSuiteFiles().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Listeners }
     * {@link Packages }
     * {@link Test }
     * {@link Parameter }
     * {@link MethodSelectors }
     * {@link SuiteFiles }
     * 
     * 
     */
    public List<Object> getListenersOrPackagesOrTestOrParameterOrMethodSelectorsOrSuiteFiles() {
        if (listenersOrPackagesOrTestOrParameterOrMethodSelectorsOrSuiteFiles == null) {
            listenersOrPackagesOrTestOrParameterOrMethodSelectorsOrSuiteFiles = new ArrayList<Object>();
        }
        return this.listenersOrPackagesOrTestOrParameterOrMethodSelectorsOrSuiteFiles;
    }

}