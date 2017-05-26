package composer;

import javax.swing.*;
import javax.swing.tree.TreePath;

/**
 * Created by igors on 25.12.16.
 */
public interface IController {
    void openJar(String jarPath);
    void updateSuiteTreeItemSelected(TreePath pathSelected);
    void updateTestActionsTreeItemSelected(TreePath pathSelected);
    void addActionBeforeSelected(JTable parameters, String testName);
    void addActionAfterSelected(JTable parameters, String testName);
    void applyActionChanges();
    void deleteAction();
    void saveSuite(JTree suiteTree, String xmlPath);
    void openSuite(String xmlPath, JTree suiteTree);
}
