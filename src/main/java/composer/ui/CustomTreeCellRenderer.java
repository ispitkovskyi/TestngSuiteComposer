package composer.ui;

import common.Constants;
import common.Utils;
import composer.CustomMutableTreeNode;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

/**
 * Created by ispitkovskyi on 1/8/2017.
 */
public class CustomTreeCellRenderer extends DefaultTreeCellRenderer {
    ImageIcon suiteIcn;
    ImageIcon testIcn;
    ImageIcon classIcn;
    ImageIcon methodIcn;
    ImageIcon paramIcn;
    ImageIcon paramValueIcn;
    ImageIcon containerIcn;

    public CustomTreeCellRenderer() {
        suiteIcn = Utils.createImageIcon("images/testng.png", 60, 15);
        testIcn = Utils.createImageIcon("images/action4.jpg", 30, 30);
        classIcn = Utils.createImageIcon("images/class.png", 22, 22);
        methodIcn = Utils.createImageIcon("images/method3.jpg", 24, 24);
        paramIcn = Utils.createImageIcon("images/parameter.png", 15, 15);
        paramValueIcn = Utils.createImageIcon("images/parameter_value2.png", 22, 22);
        containerIcn = Utils.createImageIcon("images/elementsContainer.jpg", 42, 20);
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {

        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row,
                hasFocus);
        if (isTest(value)) {
            setIcon(testIcn);
            setToolTipText("Keyword test action");
        } else if(isClass(value)) {
            setIcon(classIcn);
            setToolTipText("Test class");
        } else if(isMethod(value)) {
            setIcon(methodIcn);
            setToolTipText("Test method");
        } else if(isParameter(value)) {
            setIcon(paramIcn);
            setToolTipText("Parameter for test-method");
        } else if(isParameterValue(value)) {
            setIcon(paramValueIcn);
            setToolTipText("Value of parameter for test-method");
        } else if(isRoot(value)) {
            setIcon(suiteIcn);
            setToolTipText("TestNG Suite");
        }else if(isClassesOrMethods(value)){
            setIcon(containerIcn);
        }else{
            setToolTipText(null);
        }

        return this;
    }

    private boolean isTest(Object value){
        return ((CustomMutableTreeNode)value).getTestngType() == Constants.TYPE_TEST_ACTION;
    }

    private boolean isClass (Object value){
        return ((CustomMutableTreeNode)value).getTestngType() == Constants.TYPE_CLASS;
    }

    private boolean isMethod (Object value){
        return ((CustomMutableTreeNode)value).getTestngType() == Constants.TYPE_METHOD;
    }

    private boolean isParameter (Object value){
        return ((CustomMutableTreeNode)value).getTestngType() == Constants.TYPE_PARAMETER;
    }

    private boolean isParameterValue (Object value){
        return ((CustomMutableTreeNode)value).getTestngType() == Constants.TYPE_PARAMETER_VALUE;
    }

    private boolean isRoot (Object value){
        return ((CustomMutableTreeNode)value).getTestngType() == Constants.TYPE_ROOT;
    }

    private boolean isClassesOrMethods(Object value){
        return (((CustomMutableTreeNode)value).getTestngType() == Constants.TYPE_CLASSES || ((CustomMutableTreeNode)value).getTestngType() == Constants.TYPE_METHODS);
    }
}
