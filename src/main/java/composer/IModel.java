package composer;

import testng.Suite;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;

/**
 * Created by ispitkovskyi on 1/9/2017.
 */
public interface IModel {
    Suite treeToSuite(TreeModel actionsTreeModel/*, String suiteName*/);
    void saveSuite(Suite suiteObj, String suiteXmlPath);
    Suite openSuite(String suiteXmlPath);
    DefaultMutableTreeNode suiteToTree(Suite testngSuite);
}
