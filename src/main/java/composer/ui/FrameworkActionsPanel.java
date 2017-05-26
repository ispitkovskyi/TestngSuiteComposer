package composer.ui;

import common.TestLogger;
import common.Utils;
import composer.JTreeHelper;
import composer.SuiteComposerController;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static common.Constants.TEST_FRAMEWORK;

/**
 * Created by ispitkovskyi on 1/21/2017.
 */
public class FrameworkActionsPanel extends JPanel implements TreeSelectionListener, ActionListener{
    int width;

    private SuiteComposerController suiteComposerController;

    private JButton btnSortAscending;
    private JButton btnSortDescending;

    private JTree treeActions;
    JScrollPane actionsScrollPaneTree;
    private JFileChooser fc;

    private JButton btnOpenFramework;
    private JTextField txtJarOpened;

    public FrameworkActionsPanel(SuiteComposerController suiteComposerController){
        width = 400;
        this.suiteComposerController = suiteComposerController;
        this.setPreferredSize(new Dimension(width, 660));
        init();
    }

    public void init(){
        btnSortAscending = new JButton(Utils.createImageIcon("images/sortascend.png", 26, 26));
        btnSortAscending.setPreferredSize(new Dimension(30, 30));
        btnSortAscending.setToolTipText("Sort ascending");
        btnSortAscending.setEnabled(false);

        btnSortDescending = new JButton(Utils.createImageIcon("images/sortdescend.png", 26, 26));
        btnSortDescending.setPreferredSize(new Dimension(30, 30));
        btnSortDescending.setToolTipText("Sort descending");
        btnSortDescending.setEnabled(false);

        //treeActions = new JTree(JTreeHelper.createTreeModelStub());
        treeActions = new JTree(JTreeHelper.getDefaultTreeModel(TEST_FRAMEWORK));
        TreeSelectionModel actionsSelectionModel = new DefaultTreeSelectionModel();
        actionsSelectionModel.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        treeActions.setSelectionModel(actionsSelectionModel);
        treeActions.setScrollsOnExpand(true);
        treeActions.setCellRenderer(new CustomTreeCellRenderer());

        actionsScrollPaneTree = new JScrollPane(treeActions, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        actionsScrollPaneTree.setWheelScrollingEnabled(true);
        actionsScrollPaneTree.setPreferredSize(new Dimension(width, 600));

        btnOpenFramework = new JButton("Open Jar");
        btnOpenFramework.setPreferredSize(new Dimension(100, 40));
        btnOpenFramework.setToolTipText("Open compiled JAR file with test classes and methods");

        txtJarOpened = new JTextField("Jar opened: ");
        txtJarOpened.setEditable(false);
        txtJarOpened.setPreferredSize(new Dimension((int)this.getWidth() - (int)btnOpenFramework.getWidth(), 20));
        txtJarOpened.setFont(new Font("Courier", Font.BOLD,12));
        txtJarOpened.setBorder(null);
        txtJarOpened.setCaretPosition(0);


        fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JAR Files", "jar");
        fc.setFileFilter(filter);

        treeActions.addTreeSelectionListener(this);
        btnOpenFramework.addActionListener(this);
        btnSortAscending.addActionListener(this);
        btnSortDescending.addActionListener(this);


        //https://docs.oracle.com/javase/tutorial/uiswing/layout/group.html
        GroupLayout layout = new GroupLayout(this);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(actionsScrollPaneTree)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(btnOpenFramework)
                        .addComponent(btnSortAscending)
                        .addComponent(btnSortDescending)
                )
                .addComponent(txtJarOpened)
        );
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(actionsScrollPaneTree)
                    .addGroup(layout.createSequentialGroup()
                            .addComponent(btnOpenFramework)
                            .addGap(230)
                            .addComponent(btnSortAscending)
                            .addComponent(btnSortDescending)
                    )
                .addComponent(txtJarOpened)
        );
        this.setLayout(layout);
    }

    public JButton getBtnSortAscending() {return btnSortAscending;}

    public JButton getBtnSortDescending() {return btnSortDescending;}

    public JButton getOpenFrameworkBtn(){
        return btnOpenFramework;
    }

    public JTextField getTxtJarOpened(){
        return txtJarOpened;
    }

    public JTree getActionsTree(){
        return treeActions;
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        JTree tree = (JTree) e.getSource();
        TreePath actionsTreeSelectedPath = tree.getSelectionPath();
        suiteComposerController.updateTestActionsTreeItemSelected(actionsTreeSelectedPath);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int dlgAction;
        if(e.getSource() == btnOpenFramework){
            dlgAction = fc.showOpenDialog(this);
            if(dlgAction == fc.APPROVE_OPTION){
                TestLogger.logInfo(String.format("Jar file opened: %s", fc.getSelectedFile().getAbsolutePath()));
                suiteComposerController.openJar(fc.getSelectedFile().getAbsolutePath());
                /*TreeModel treeModelActions = suiteComposerController.openJar(fc.getSelectedFile().getAbsolutePath());
                if(treeModelActions != null) {
                    treeActions.setModel(treeModelActions);
                    treeActions.repaint();
                    composerFrame.getSortAscendingBtn().setEnabled(true);
                    composerFrame.getSortDescendingBtn().setEnabled(true);
                }*/
            }else{
                TestLogger.logDebug("Unknown action occurred");
            }
        }
        if(e.getSource() == btnSortAscending){
            suiteComposerController.sortAscending(getActionsTree());
        }
        if(e.getSource() == btnSortDescending){
            suiteComposerController.sortDescending(getActionsTree());
        }
    }
}
