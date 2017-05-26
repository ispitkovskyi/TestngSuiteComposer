package composer;

import common.Constants;
import common.LogMessageAppender;
import common.TestLogger;
import common.Utils;
import composer.ui.SuiteComposerFrame;
import org.apache.log4j.AppenderSkeleton;
import org.json.JSONArray;
import org.json.JSONObject;
import testng.Suite;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.tree.*;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.function.BiFunction;

import static common.Constants.*;

/**
 * Created by igors on 25.12.16.
 */
public class SuiteComposerController implements IController{
    private int maxWidth;
    private int maxHeight;
    private boolean isActionSelected;

    private TreePath actionsTreeSelectedPath;
    private TreePath suiteSelectedPath;

    private SuiteComposerFrame composerFrame;

    private OptionsController optionsController;

    private SuiteComposerModel composerModel;
    private JarParser frameworkJarParser;
    private JTreeSorter treeSorter;
    private JTreeHelper treeHelper;
    private JTableHelper tableHelper;

    public SuiteComposerController() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        maxWidth = (int)(screenSize.getWidth()*0.9);
        maxHeight = (int)(screenSize.getHeight()*0.9);

        isActionSelected = false;

        optionsController = new OptionsController(this);

        composerFrame = new SuiteComposerFrame(this, optionsController);

        frameworkJarParser = new JarParser(this);

        treeSorter = new JTreeSorter();
        treeHelper = new JTreeHelper();
        tableHelper = new JTableHelper();

        composerModel = new SuiteComposerModel();
    }

    public void init(){
        optionsController.init();
        composerFrame.setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        composerFrame.setLocation(dim.width/2-composerFrame.getSize().width/2, dim.height/2-composerFrame.getSize().height/2);

        //TODO: Switching between unfromatted and formatted logging panels should be optimized
        /**
         * Curently to switch between logging panels do:
         * 1. Uncomment one of rows below and comment another (new LogMessageAppender(...))
         * 2. Go to LoggerPanel class and put either "textPaneScrollPane" or "textAreaScrollPane" into "initLayout()" method
         */
        //AppenderSkeleton textAreaAppender = new LogMessageAppender(composerFrame.getLogTextArea());
        AppenderSkeleton textAreaAppender = new LogMessageAppender(composerFrame.getLogTextPane());
        TestLogger.addAppender(textAreaAppender);
    }

    public int getMaxWidth(){
        return maxWidth;
    }

    public int getMaxHeight(){
        return maxHeight;
    }

    public JFrame getSuiteComposerFrame(){
        return composerFrame;
    }

    public TreePath getActionsTreeSelection(){
        return actionsTreeSelectedPath;
    }

    public TreePath getSuiteTreeSelection(){
        return suiteSelectedPath;
    }

    private void resetParametersTable(JTable table){
        table.setModel(defaultTableModel());
    }

    public void clearErrors(){
        composerFrame.getLblErrMsg().setText("");
    }

    public void showError(String errMsg){
        composerFrame.getLblErrMsg().setText(errMsg);
        composerFrame.getLblErrMsg().setForeground(Color.RED);
    }

    public void putParametersIntoTable(DefaultTableModel sourceModel, JTable paramsTable){
        paramsTable.setModel(sourceModel);
        int width = paramsTable.getWidth();
        paramsTable.getColumnModel().getColumn(0).setMaxWidth((int) (width * 0.3));
        paramsTable.getColumnModel().getColumn(1).setMaxWidth((int) (width * 0.8));
        paramsTable.updateUI();
    }

    @Override
    public void updateSuiteTreeItemSelected(TreePath pathSelected){
        suiteSelectedPath = pathSelected;
        int itemType;
        CustomMutableTreeNode selectedNode;
        try{
            selectedNode = ((CustomMutableTreeNode)suiteSelectedPath.getLastPathComponent());
            itemType = selectedNode.getTestngType();
        }catch(NullPointerException ex){
            TestLogger.logInfo("Exception on closing suite-tree node");
            return;
        }
        switch (itemType){
            case TYPE_ROOT:
                composerFrame.getAddActionAfterSelectedButton().setEnabled(true);
                composerFrame.getAddActionBeforeSelectedButton().setEnabled(true);
                composerFrame.getApplyActionChangesButton().setEnabled(true);
                composerFrame.getDeleteActionButton().setEnabled(false);
                composerFrame.getTxtTestName().setText("");
                composerFrame.getTxtTestName().setEnabled(false);
                putParametersIntoTable(((CustomMutableTreeNode) selectedNode).getParameters(), composerFrame.getTableTestParams());
                break;
            case TYPE_TEST_ACTION:
                if(isActionSelected) { //todo: NEED TO HANDLE IF ACTION OF FRAMEWORK SELECTED
                    composerFrame.getAddActionAfterSelectedButton().setEnabled(true);
                    composerFrame.getAddActionBeforeSelectedButton().setEnabled(true);
                }
                composerFrame.getApplyActionChangesButton().setEnabled(true);
                composerFrame.getTxtTestName().setText(selectedNode.toString());
                composerFrame.getTxtTestName().setEnabled(true);
                putParametersIntoTable(((CustomMutableTreeNode) selectedNode).getParameters(), composerFrame.getTableTestParams());
                break;
            case TYPE_CLASSES:
                composerFrame.getAddActionAfterSelectedButton().setEnabled(false);
                composerFrame.getAddActionBeforeSelectedButton().setEnabled(false);
                composerFrame.getDeleteActionButton().setEnabled(true);
                composerFrame.getTxtTestName().setText("");
                composerFrame.getTxtTestName().setEnabled(false);
                if(selectedNode.getTestngType() == Constants.TYPE_CLASSES) {
                    composerFrame.getApplyActionChangesButton().setEnabled(true);
                    putParametersIntoTable(((CustomMutableTreeNode) selectedNode).getParameters(), composerFrame.getTableTestParams());
                }else{
                    composerFrame.getApplyActionChangesButton().setEnabled(false);
                    composerFrame.getTxtTestName().setText("");
                    composerFrame.getTxtTestName().setEnabled(false);
                    resetParametersTable(composerFrame.getTableTestParams());
                }
                break;
            case TYPE_CLASS:
                composerFrame.getAddActionAfterSelectedButton().setEnabled(false);
                composerFrame.getAddActionBeforeSelectedButton().setEnabled(false);
                composerFrame.getDeleteActionButton().setEnabled(true);
                composerFrame.getApplyActionChangesButton().setEnabled(false);
                composerFrame.getTxtTestName().setText("");
                composerFrame.getTxtTestName().setEnabled(false);
                resetParametersTable(composerFrame.getTableTestParams());
                break;
            case TYPE_METHODS:
                composerFrame.getAddActionAfterSelectedButton().setEnabled(false);
                composerFrame.getAddActionBeforeSelectedButton().setEnabled(false);
                composerFrame.getDeleteActionButton().setEnabled(true);
                composerFrame.getApplyActionChangesButton().setEnabled(true);
                composerFrame.getTxtTestName().setText("");
                composerFrame.getTxtTestName().setEnabled(false);
                //composerFrame.getTxtTestName().setText(selectedNode.getParent().getParent().getParent().toString());
                putParametersIntoTable(((CustomMutableTreeNode) selectedNode).getParameters(), composerFrame.getTableTestParams());
                break;
            case TYPE_METHOD:
                composerFrame.getAddActionAfterSelectedButton().setEnabled(false);
                composerFrame.getAddActionBeforeSelectedButton().setEnabled(false);
                composerFrame.getDeleteActionButton().setEnabled(true);
                composerFrame.getApplyActionChangesButton().setEnabled(false);
                composerFrame.getTxtTestName().setText("");
                composerFrame.getTxtTestName().setEnabled(false);
                resetParametersTable(composerFrame.getTableTestParams());
                break;
            case TYPE_PARAMETER:
                composerFrame.getAddActionAfterSelectedButton().setEnabled(false);
                composerFrame.getAddActionBeforeSelectedButton().setEnabled(false);
                composerFrame.getDeleteActionButton().setEnabled(true);
                composerFrame.getApplyActionChangesButton().setEnabled(false);
                composerFrame.getTxtTestName().setText("");
                composerFrame.getTxtTestName().setEnabled(false);
                resetParametersTable(composerFrame.getTableTestParams());
                break;
            case TYPE_PARAMETER_VALUE:
                composerFrame.getAddActionAfterSelectedButton().setEnabled(false);
                composerFrame.getAddActionBeforeSelectedButton().setEnabled(false);
                composerFrame.getDeleteActionButton().setEnabled(true);
                composerFrame.getApplyActionChangesButton().setEnabled(false);
                composerFrame.getTxtTestName().setText("");
                composerFrame.getTxtTestName().setEnabled(false);
                resetParametersTable(composerFrame.getTableTestParams());
                break;
            default:
                composerFrame.getAddActionAfterSelectedButton().setEnabled(false);
                composerFrame.getAddActionBeforeSelectedButton().setEnabled(false);
                composerFrame.getApplyActionChangesButton().setEnabled(false);
                composerFrame.getTxtTestName().setText("");
                composerFrame.getTxtTestName().setEnabled(false);
                composerFrame.getDeleteActionButton().setEnabled(false);
                break;
        }
    }


    @Override
    public void updateTestActionsTreeItemSelected(TreePath pathSelected){
        actionsTreeSelectedPath = pathSelected;
        int nodeType;
        CustomMutableTreeNode selectedNode;
        try{
            selectedNode = ((CustomMutableTreeNode)actionsTreeSelectedPath.getLastPathComponent());
            nodeType = selectedNode.getTestngType();
        }catch(NullPointerException ex){
            TestLogger.logWarning("Exception on closing actions-tree node");
            return;
        }

        switch (nodeType){
            case TYPE_METHOD:
                isActionSelected = true;
                composerFrame.getApplyActionChangesButton().setEnabled(false);
                composerFrame.getDeleteActionButton().setEnabled(false);
                composerFrame.getAddActionAfterSelectedButton().setEnabled(true);
                composerFrame.getAddActionBeforeSelectedButton().setEnabled(true);
                composerFrame.getTxtTestName().setText(Constants.DEFAULT_TEST_NAME);
                composerFrame.getTxtTestName().setEnabled(true);
                putParametersIntoTable(((CustomMutableTreeNode)selectedNode).getParameters(), composerFrame.getTableTestParams());
                break;
            default:
                isActionSelected = false;
                composerFrame.getAddActionAfterSelectedButton().setEnabled(false);
                composerFrame.getAddActionBeforeSelectedButton().setEnabled(false);
                composerFrame.getTxtTestName().setEnabled(false);
                composerFrame.getApplyActionChangesButton().setEnabled(false);
                composerFrame.getDeleteActionButton().setEnabled(false);
                resetParametersTable(composerFrame.getTableTestParams());
                break;
        }
    }

    @Override
    public void openJar(String jarPath) {
        composerFrame.resetActionsTree();
        TreeModel actionsTreeModel = getTestItemsFromJar(jarPath);
        if(actionsTreeModel != null) {
            composerFrame.getActionsTree().setModel(actionsTreeModel);
            composerFrame.getActionsTree().repaint();
            composerFrame.getSortAscendingBtn().setEnabled(true);
            composerFrame.getSortDescendingBtn().setEnabled(true);
        }
    }

    private TreeModel getTestItemsFromJar(String jarPath){
        JSONObject jarTestItems = null;
        TreeModel actionsTree = null;
        clearErrors();
        try {
            jarTestItems = frameworkJarParser.parseJarFile(jarPath);

            composerFrame.getTxtJarOpened().setText(composerFrame.getTxtJarOpened().getText() + jarPath);
            composerFrame.getTxtJarOpened().setCaretPosition(0);
            composerFrame.getTxtJarOpened().setToolTipText(jarPath);
        /*}catch (IOException ioe){
            TestLogger.logError("IOException happened: Could not open framework JAR file with test methods.");
            showError("IOException happened: Could not open framework JAR file with test methods.");
        }catch(ClassNotFoundException cnfe){
            TestLogger.logError("ClassNotFoundException happened " + cnfe.getCause());
            showError("ClassNotFoundException happened " + cnfe.getMessage());
        */
        } catch (InterruptedException e) {
            TestLogger.logError("InterruptedException happened " + e.getCause());
            showError("InterruptedException happened " + e.getCause());
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        actionsTree = jsonToTreeModel(jarTestItems);
        return actionsTree;
    }

    private TreeModel jsonToTreeModel(JSONObject actionsTree)
    {
        if(actionsTree == null || actionsTree.keySet().size() == 0)
            return null;

        // Root node of tree
        DefaultMutableTreeNode root = new CustomMutableTreeNode(TEST_FRAMEWORK, TYPE_ROOT);

        //todo: this should be a recursive function, so I'll need to change JSON structure to have nested JSONObjects instead of JSONArray for parameters
        Iterator classItr = actionsTree.keys();
        while(classItr.hasNext()){
            String className = (String) classItr.next();
            DefaultMutableTreeNode testClass = new CustomMutableTreeNode(className, Constants.TYPE_CLASS);
            root.add(testClass);

            Iterator methItr = actionsTree.getJSONObject(className).keys();
            while(methItr.hasNext()){
                String methodName = (String)methItr.next();
                DefaultMutableTreeNode testMethod = new CustomMutableTreeNode(methodName, Constants.TYPE_METHOD); new DefaultMutableTreeNode(methodName);
                testClass.add(testMethod);

                JSONArray methParams = actionsTree.getJSONObject(className).getJSONArray(methodName);
                for(int i=0; i< methParams.length(); i++){
                    String parameterName = methParams.getString(i);
                    DefaultMutableTreeNode testParam = new CustomMutableTreeNode(parameterName, Constants.TYPE_PARAMETER);
                    testMethod.add(testParam);
                    ((CustomMutableTreeNode)testMethod).addParameterRow(parameterName);
                }
            }
        }
        return new DefaultTreeModel(root);
    }

    public TableModel defaultTableModel(){
        return tableHelper.getDefaultTableModel();
    }



    public void sortAscending(JTree tree){
        treeSorter.sortAscending(tree);
    }

    public void sortDescending(JTree tree){
        treeSorter.sortDescending(tree);
    }

    @Override
    public void addActionBeforeSelected(JTable parameters, String actionName) {
        addActionToSuite(parameters, actionName, (targetRoot, node) -> targetRoot.getIndex(node));
    }

    @Override
    public void addActionAfterSelected(JTable parameters, String actionName) {
        addActionToSuite(parameters, actionName, (targetRoot, node) -> targetRoot.getIndex(node) + 1);
    }

    private int findInsertionPositionIndex(DefaultMutableTreeNode targetRootNode, TreePath targetSelectedNode, BiFunction<DefaultMutableTreeNode, DefaultMutableTreeNode, Integer> positionIndexGetter) {
        int insertPositionIndex = targetRootNode.getChildCount();
        if (targetSelectedNode != null) {
            //if root node selected
            if(targetSelectedNode.getParentPath() == null) {
                insertPositionIndex = 0;
            }else { //if selected node is NOT a Root-Node
                insertPositionIndex = positionIndexGetter.apply(targetRootNode, ((DefaultMutableTreeNode) targetSelectedNode.getPathComponent(Constants.CLASS_LEVEL_IN_FRAMEWORK)));
            }
        }
        return insertPositionIndex;
    }

    private void addActionToSuite(JTable parameters, String actionName, BiFunction<DefaultMutableTreeNode, DefaultMutableTreeNode, Integer> positionIndexGetter){
        JTree actionsTree = composerFrame.getActionsTree();
        JTree suiteTree = composerFrame.getSuiteTree();

        CustomMutableTreeNode selectedAction = (CustomMutableTreeNode) treeHelper.getSelectedNode(actionsTree);
        DefaultMutableTreeNode actionNode = buildActionNode(selectedAction, actionName, parameters);

        DefaultMutableTreeNode targetRoot = (DefaultMutableTreeNode) suiteTree.getModel().getRoot();
        TreePath targetSelection = suiteTree.getSelectionPath();

        int insertPositionIndex = findInsertionPositionIndex(targetRoot, targetSelection, positionIndexGetter);
        targetRoot.insert(actionNode, insertPositionIndex);

        ((DefaultTreeModel)suiteTree.getModel()).reload(targetRoot);
        suiteTree.updateUI();
        TestLogger.logDebug(String.format("Action %s added", actionNode.getUserObject().toString()));
        TestLogger.logDebug("Suite tree reloaded");
    }

    private DefaultMutableTreeNode buildActionNode(CustomMutableTreeNode selectedAction, String actionName, JTable parameters){
        if(selectedAction.getTestngType() != TYPE_METHOD){
            showError("It's not a test-method selected! Cannot build test node for suite.");
            TestLogger.logError("It's not a test-method selected! Cannot build test node for suite.");
            return null;
        }

        CustomMutableTreeNode testAction = new CustomMutableTreeNode(actionName, TYPE_TEST_ACTION);
        CustomMutableTreeNode classes = new CustomMutableTreeNode(CLASSES, TYPE_CLASSES);
        CustomMutableTreeNode testClass = new CustomMutableTreeNode (((CustomMutableTreeNode) selectedAction.getParent()).getUserObject().toString(), TYPE_CLASS);
        CustomMutableTreeNode methods = new CustomMutableTreeNode(METHODS, TYPE_METHODS);
        CustomMutableTreeNode testMethod = new CustomMutableTreeNode(selectedAction.getUserObject().toString(), TYPE_METHOD);

        testAction.add(classes);
        classes.add(testClass);
        testClass.add(methods);
        methods.add(testMethod);

        DefaultTableModel tableModel = tableHelper.getDefaultTableModel();
        DefaultTableModel parametersTableModel = tableHelper.getTableModel(parameters);
        for(int i=0; i<parameters.getRowCount(); i++){
            String value = parametersTableModel.getValueAt(i, 1).toString();
            String name = parametersTableModel.getValueAt(i, 0).toString();
            tableModel.addRow(new Object[]{name, value});
            if(!value.isEmpty()){
                //following 4 lines are not needed for logic. It's just to show parameters as tree-nodes in suite-tree
                DefaultMutableTreeNode pName = new CustomMutableTreeNode(name, Constants.TYPE_PARAMETER);
                DefaultMutableTreeNode pVal = new CustomMutableTreeNode(value, Constants.TYPE_PARAMETER_VALUE);
                pName.add(pVal);
                methods.add(pName); //Parameters will appear at the same level with methods under parent test-class
            }
        }
        methods.setParameters(tableModel);

        return testAction;
    }


    @Override
    public void applyActionChanges(){
        //todo: Need to generalize this method with method which inserts nodes
        JTree treeSuite = composerFrame.getSuiteTree();
        DefaultMutableTreeNode targetRoot = (DefaultMutableTreeNode) treeSuite.getModel().getRoot();
        MutableTreeNode selectedNode;
        int nodeTestngType;
        try{
            selectedNode = treeHelper.getSelectedNode(treeSuite.getSelectionPath());
            nodeTestngType = ((CustomMutableTreeNode) selectedNode).getTestngType();
        }catch(NullPointerException ex){
            TestLogger.logWarning("Exception on closing actions-tree node");
            return;
        }

        if (selectedNode == null) {
            TestLogger.logError("Could not identify selected node (NULL returned)");
            return;
        }

        String testName = null;
        if(((CustomMutableTreeNode) selectedNode).getTestngType() == TYPE_TEST_ACTION){
            testName = composerFrame.getTxtTestName().getText();
            if(testName == null || testName.isEmpty()){
                Utils.showWarningDialog(composerFrame, "You must specify action name first", (any) -> {
                    composerFrame.getTxtTestName().grabFocus();
                    Utils.selectText(composerFrame.getTxtTestName());
                });
                return;
            }
        }

        updateTestNode((CustomMutableTreeNode) selectedNode, testName, composerFrame.getTableTestParams());
        treeSuite.repaint();
        treeSuite.updateUI();
        composerFrame.getApplyActionChangesButton().setEnabled(false);
    }

    private void updateTestNode(CustomMutableTreeNode selectedNode, String actionName, JTable parameters){
        //Set test-action name
        if (selectedNode.getTestngType() == TYPE_TEST_ACTION)
            selectedNode.setUserObject(actionName);

        //Set parameters from table being edited to selected node
        DefaultTableModel editedParametersModel = tableHelper.getTableModel(parameters);


        for(int i=0; i<selectedNode.getChildCount(); i++){
            CustomMutableTreeNode childNode = (CustomMutableTreeNode) selectedNode.getChildAt(i);
            if(childNode.getTestngType() == TYPE_PARAMETER) {
                childNode.removeFromParent();
                i--;
            }
        }

        DefaultTableModel tableModel = tableHelper.getDefaultTableModel();
        for(int i=0; i<parameters.getRowCount(); i++){
            String value = editedParametersModel.getValueAt(i, 1).toString();
            String name = editedParametersModel.getValueAt(i, 0).toString();
            tableModel.addRow(new Object[]{name, value});
            if(!value.isEmpty()){
                //following 4 lines are not needed for logic. It's just to show parameters as tree-nodes in suite-tree
                DefaultMutableTreeNode pName = new CustomMutableTreeNode(name, Constants.TYPE_PARAMETER);
                DefaultMutableTreeNode pVal = new CustomMutableTreeNode(value, Constants.TYPE_PARAMETER_VALUE);
                pName.add(pVal);
                selectedNode.add(pName); //Parameters will appear at the same level with methods under parent test-class
            }
        }
        selectedNode.setParameters(tableModel);
    }

    @Override
    public void deleteAction() {
        //Protection from attempt to delete root node
        JTree suiteTree = composerFrame.getSuiteTree();
        DefaultMutableTreeNode selectedNode = treeHelper.getSelectedNode (suiteTree); //((DefaultMutableTreeNode)suiteTree.getAnchorSelectionPath().getLastPathComponent());
        TreeNode parent = selectedNode.getParent();
        if(parent != null) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) suiteTree.getSelectionPath().getPathComponent(1); //get component at tree-level #1 (next after the root)
            node.removeFromParent();
            suiteTree.updateUI();
            composerFrame.getDeleteActionButton().setEnabled(false);
            composerFrame.getApplyActionChangesButton().setEnabled(false);  //todo: this should be somewhere in update() method of observer
        }
    }

    @Override
    public void saveSuite(JTree suiteTree, String xmlPath) {
        if(!xmlPath.contains(".xml")){
            xmlPath += ".xml";
        }
        Suite xmlSuite = composerModel.treeToSuite(suiteTree.getModel());
        composerModel.saveSuite(xmlSuite, xmlPath);

        composerFrame.getTxtSuiteOpened().setText(composerFrame.getTxtSuiteOpened().getText() + " saved: " + xmlPath);
        composerFrame.getTxtSuiteOpened().setCaretPosition(0);
        composerFrame.getTxtSuiteOpened().setToolTipText(xmlPath);
    }

    @Override
    public void openSuite(String xmlPath, JTree suiteTree) {
        if(!xmlPath.contains(".xml")){
            xmlPath += ".xml";
        }

        Suite suiteObj = composerModel.openSuite(xmlPath);
        DefaultMutableTreeNode suiteRoot = composerModel.suiteToTree(suiteObj);
        ((DefaultTreeModel)suiteTree.getModel()).setRoot(suiteRoot);
        suiteTree.updateUI();

        composerFrame.getTxtSuiteOpened().setText(composerFrame.getTxtSuiteOpened().getText() + " opened: " + xmlPath);
        composerFrame.getTxtSuiteOpened().setCaretPosition(0);
        composerFrame.getTxtSuiteOpened().setToolTipText(xmlPath);
    }

    public void printError(String errMsg){
        showError(errMsg);
    }
}
