package composer;

import common.Constants;

import javax.swing.*;
import javax.swing.tree.*;

import static common.Constants.TYPE_ROOT;

/**
 * Created by ispitkovskyi on 2/10/2017.
 */
public class JTreeHelper {

    /**
     * Create default "blank" tree with only a root-node for future suite
     */
    public static TreeModel getDefaultTreeModel(String rootName)
    {
        DefaultMutableTreeNode root = new CustomMutableTreeNode(rootName, TYPE_ROOT);
        return new DefaultTreeModel(root);
    }

    public DefaultMutableTreeNode getSelectedNode(JTree tree){
        return (DefaultMutableTreeNode)getSelectedNode(tree.getSelectionPath()/*tree.getAnchorSelectionPath()*/);
    }

    public DefaultMutableTreeNode getSelectedNode(TreePath treeSelectedPath){
        return ((DefaultMutableTreeNode)treeSelectedPath.getLastPathComponent());
    }

    public int getSelectedNodeLevel(TreePath treeSelectedPath){
        return getSelectedNode(treeSelectedPath).getLevel();
    }

    public MutableTreeNode getTreeRoot(JTree tree){
        return (DefaultMutableTreeNode) tree.getModel().getRoot();
    }

    public int getSelectedNodeIndex(JTree tree, MutableTreeNode node){
        MutableTreeNode treeRoot = getTreeRoot(tree);
        return treeRoot.getIndex(node);
    }

    /**
     * Returns top parent node for a node passed as parameter.
     * @return
     */
    public CustomMutableTreeNode getTopNodeOfBranch(CustomMutableTreeNode node){
        CustomMutableTreeNode parent = (CustomMutableTreeNode) node.getParent();
        if(parent != null){
            parent = getTopNodeOfBranch(parent);
        }
        return parent;
    }

    public static TreeModel createTreeModelStub() {
        String treeRoot = "TestActions";
        String[]   nodes = new String[]  {"TestClass1", "TestClass2"};
        String[][] leafs = new String[][]{{"TestMethod1.1", "TestMethod1.2", "TestMethod1.3", "TestMethod1.4", "TestMethod1.5"},
                {"TestMethod2.1", "TestMethod2.2", "TestMethod2.3", "TestMethod2.4"}};
        // Root node of tree
        DefaultMutableTreeNode root = new CustomMutableTreeNode(treeRoot, TYPE_ROOT);
        // Adding nodes - children of 2nd level
        DefaultMutableTreeNode ApplicationTest = new CustomMutableTreeNode(nodes[0], Constants.TYPE_CLASS);
        DefaultMutableTreeNode ComponentTest = new CustomMutableTreeNode(nodes[1], Constants.TYPE_CLASS);
        // Добавление ветвей к корневой записи
        root.add(ApplicationTest);
        root.add(ComponentTest);
        // Добавление листьев - потомков 2-го уровня
        for ( int i = 0; i < leafs[0].length; i++)
            ApplicationTest.add(new CustomMutableTreeNode(leafs[0][i], Constants.TYPE_METHOD));
        for ( int i = 0; i < leafs[1].length; i++)
            ComponentTest.add(new CustomMutableTreeNode(leafs[1][i], Constants.TYPE_METHOD));
        // Создание стандартной модели
        return new DefaultTreeModel(root);
    }
}
