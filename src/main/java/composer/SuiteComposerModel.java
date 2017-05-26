package composer;

import common.Constants;
import common.TestLogger;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import testng.Class;
import testng.*;

import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;
import java.io.File;
import java.io.FileReader;
import java.util.*;

import static common.Constants.*;

/**
 * Created by ispitkovskyi on 1/3/2017.
 */
public class SuiteComposerModel implements IModel {
    ObjectFactory suiteObjectFactory;

    public SuiteComposerModel(){
        suiteObjectFactory = new ObjectFactory();
    }

    @Override
    public Suite treeToSuite(TreeModel actionsTreeModel){
        CustomMutableTreeNode rootNode = (CustomMutableTreeNode)actionsTreeModel.getRoot();
        String suiteName = rootNode.getUserObject().toString();
        //Enumeration<Object> actions = rootNode.children();


        Suite suite = suiteObjectFactory.createSuite();
        suite.setVerbose("10");
        suite.setAnnotations("JDK");
        suite.setName(suiteName);
        suite.setParallel("false");
        List<Object> suiteBody = suite.getListenersOrPackagesOrTestOrParameterOrMethodSelectorsOrSuiteFiles();

        //Add global suite parameters
        if(rootNode.getParameters().getDataVector().size()>0)
            addParameters(rootNode.getParameters(), suiteBody);

        //addActionsToSuite2(actions, suiteBody);
        addActionsToSuite(rootNode, suiteBody);

        return suite;
    }

    private void addActionsToSuite(CustomMutableTreeNode parent, List suiteElementsContainer){
        Enumeration<Object> subNodes = parent.children();

        while(subNodes.hasMoreElements()){
            CustomMutableTreeNode node = (CustomMutableTreeNode) subNodes.nextElement();
            int nodeType = node.getTestngType();
            //todo: Move common code from "case" blocks to separate function
            switch(nodeType){
                case TYPE_TEST_ACTION:
                    Test test = suiteObjectFactory.createTest();
                    test.setName(node.getUserObject().toString());
                    suiteElementsContainer.add(test);

                    if(node.getParameters().getDataVector().size() > 0)
                        addParameters(node.getParameters(), test.getParameter());

                    //There can be only 1 "CLASSES" element inside test!
                    ArrayList<Test> classesList = new ArrayList<>();
                    classesList.add(test);
                    addActionsToSuite(node, classesList);
                    break;
                case TYPE_CLASSES:
                    Classes classes = suiteObjectFactory.createClasses();
                    //There can be only 1 "CLASSES" element inside test!
                    Test classesContainer = (Test) suiteElementsContainer.get(0);
                    classesContainer.setClasses(classes);

                    if(node.getParameters().getDataVector().size() > 0)
                        addParameters(node.getParameters(), classes.getParameter());

                    addActionsToSuite(node, classes.getClazz());
                    break;
                case TYPE_CLASS:
                    Class clazz = suiteObjectFactory.createClass();
                    clazz.setName(node.getUserObject().toString());
                    suiteElementsContainer.add(clazz);
                    //Parameters are not allowed inside class by current testng.xml schema
                    addActionsToSuite(node, clazz.getMethods());
                    break;
                case TYPE_METHODS:
                    Methods methods = suiteObjectFactory.createMethods();
                    suiteElementsContainer.add(methods);

                    if(node.getParameters().getDataVector().size()>0){
                        addParameters(node.getParameters(), methods.getIncludeOrExcludeOrParameter());
                    }

                    addActionsToSuite(node, methods.getIncludeOrExcludeOrParameter());
                    break;
                case TYPE_METHOD:
                    Include includeMethod = new Include();
                    includeMethod.setName(node.getUserObject().toString());
                    suiteElementsContainer.add(includeMethod);
                    //Parameters are not allowed inside include by current testng.xml schema
                    break;
                case TYPE_METHOD_EXCLUDED:
                    Exclude excludeMethod = new Exclude();
                    excludeMethod.setName(node.getUserObject().toString());
                    suiteElementsContainer.add(excludeMethod);
                    //Parameters are not allowed inside include by current testng.xml schema
                    break;
                default:
                    //Parameters are added from tables stored inside parent nodes, so skip handing TYPE_PARAMETER
                    TestLogger.logDebug(String.format("%s element %s skipped", node.getTestngType(), node.getUserObject().toString()));
                    break;
            }
        }
    }

    private void addParameters(DefaultTableModel parameters, List parametersContainer){
        Vector<Object> paramsVect = parameters.getDataVector();

        Enumeration<Object> paramsVectEnum = paramsVect.elements();
        while (paramsVectEnum.hasMoreElements()) {
            Vector record = (Vector) paramsVectEnum.nextElement();
            String param = record.elementAt(0).toString();
            String value = record.elementAt(1).toString();
            if(value != null && !value.isEmpty()) {
                Parameter parameter = suiteObjectFactory.createParameter();
                parameter.setName(param);
                parameter.setValue(value);
                parametersContainer.add(parameter);
            }
        }
    }

    @Override
    public void saveSuite(Suite suiteObj, String suiteXmlPath){
        try {
            File suiteFile = new File(suiteXmlPath);
            JAXBContext jaxbContext = JAXBContext.newInstance(Suite.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty formatted
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(suiteObj, suiteFile);
            jaxbMarshaller.marshal(suiteObj, System.out);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Suite openSuite(String suiteXmlPath){
        Suite suite = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Suite.class);
            SAXParserFactory spf = SAXParserFactory.newInstance();

            try {
                spf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
                spf.setFeature("http://xml.org/sax/features/validation", false);
                spf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
                XMLReader xmlReader = spf.newSAXParser().getXMLReader();
                InputSource inputSource = new InputSource(new FileReader(suiteXmlPath));

                SAXSource source = new SAXSource(xmlReader, inputSource);

                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                suite = (Suite)jaxbUnmarshaller.unmarshal(source);
            }catch(Exception e){
                TestLogger.logError("Exception on preparing XML reader: " + e.getCause());
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return suite;
    }

    /*
    * This fails due to Exception happening because of HTTP link to DTD schema of testng.xml, if such is present in suite xml file
    @Override
    public Suite openSuite(String suiteXmlPath){
        Suite suite = null;
        try {
            File suiteFile = new File(suiteXmlPath);
            JAXBContext jaxbContext = JAXBContext.newInstance(Suite.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            suite = (Suite)jaxbUnmarshaller.unmarshal(suiteFile);

        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return suite;
    }
    */

    @Override
    public DefaultMutableTreeNode suiteToTree(Suite suiteObj){
        //todo: Implement global suite parameters
        String suiteName = suiteObj.getName();
        DefaultMutableTreeNode root = new CustomMutableTreeNode(suiteName, TYPE_ROOT);

        List<Object> suiteRootElements = suiteObj.getListenersOrPackagesOrTestOrParameterOrMethodSelectorsOrSuiteFiles();

        addTreeElements(root, suiteRootElements);

        return root;
    }


    private void addTreeElements(DefaultMutableTreeNode parent, List children){
        Iterator itr = children.iterator();

        while(itr.hasNext()){
            Object child = itr.next();
            String childType = child.getClass().getName();
            INamedSuiteMember memberChild = (INamedSuiteMember) child;

            DefaultMutableTreeNode childNode = new DefaultMutableTreeNode("stub");

            switch(childType){
                case "testng.Test":
                    childNode = new CustomMutableTreeNode(memberChild.getName(), Constants.TYPE_TEST_ACTION);
                    List<Classes> testClasses = new ArrayList<>();
                        testClasses.add(((Test)child).getClasses());
                    addTreeElements(childNode, testClasses);

                    List<Parameter>testsParameters = ((Test)child).getParameter();
                    addTreeElements(childNode, testsParameters);
                    break;
                case "testng.Classes":
                    childNode = new CustomMutableTreeNode(memberChild.getName(), Constants.TYPE_CLASSES);
                    List<Class> testClazzes = ((Classes)child).getClazz();
                    addTreeElements(childNode, testClazzes);

                    List<Parameter>testsClassesParameters = ((Classes)child).getParameter();
                    addTreeElements(childNode, testsClassesParameters);
                    break;
                //todo: Modify "Class" class by adding there List<Parameter> and enable reading/saving suite with parameters inside Class, at the same level with Methods element
                case "testng.Class":
                    childNode = new CustomMutableTreeNode(memberChild.getName(), Constants.TYPE_CLASS);
                    /*
                    DefaultMutableTreeNode finalChildNode = childNode;
                    ((Class)child).getMethods().forEach((methods) -> {
                        List<Object> subMethodsOrParameters = methods.getIncludeOrExcludeOrParameter();
                        addTreeElements(finalChildNode, subMethodsOrParameters);
                    });
                    */
                    List<Methods> testMethods = ((Class)child).getMethods();
                    addTreeElements(childNode, testMethods);
                    break;
                case "testng.Methods":
                    childNode = new CustomMutableTreeNode(memberChild.getName(), Constants.TYPE_METHODS);
                    List<Object> methodsOrParameters = ((Methods)child).getIncludeOrExcludeOrParameter();
                    addTreeElements(childNode, methodsOrParameters);
                    break;
                case "testng.Include":
                    childNode = new CustomMutableTreeNode(memberChild.getName(), Constants.TYPE_METHOD);
                    break;
                case "testng.Exclude":
                    childNode = new CustomMutableTreeNode(memberChild.getName(), Constants.TYPE_METHOD_EXCLUDED);
                    break;
                case "testng.Parameter":
                    String paramName = memberChild.getName();
                    String paramValue = ((Parameter)memberChild).getValue();
                    childNode = new CustomMutableTreeNode(paramName, Constants.TYPE_PARAMETER);
                    DefaultMutableTreeNode value = new CustomMutableTreeNode(paramValue, Constants.TYPE_PARAMETER_VALUE);
                    childNode.add(value);
                    //Add parameters table to parent-node
                    ((CustomMutableTreeNode)parent).addParameterRow(paramName, paramValue);
                    break;
                default:
                    break;
            }
            parent.add(childNode);
        }
    }

}
