package composer.ui;

import common.Constants;
import common.TestLogger;
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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by ispitkovskyi on 1/21/2017.
 */
public class SuiteActionsPanel extends JPanel implements TreeSelectionListener, ActionListener, KeyListener{
    int width;

    private SuiteComposerController suiteComposerController;
    private JTree treeSuite;
    JScrollPane suiteScrollPaneTree;
    private JFileChooser fc;

    private JButton btnOpenSuite;
    private JButton btnSaveSuite;
    private JTextField txtSuiteOpened;

    public SuiteActionsPanel(SuiteComposerController suiteComposerController){
        width = 600;
        this.suiteComposerController = suiteComposerController;
        this.setSize(new Dimension(width, 660));
        init();
    }

    public void init(){
        treeSuite = new JTree(JTreeHelper.getDefaultTreeModel(Constants.TEST_SUITE));
        TreeSelectionModel suiteSelectionModel = new DefaultTreeSelectionModel();
        suiteSelectionModel.setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
        treeSuite.setSelectionModel(suiteSelectionModel);
        treeSuite.setScrollsOnExpand(true);
        treeSuite.setCellRenderer(new CustomTreeCellRenderer());
        btnSaveSuite = new JButton("Save Suite");
        btnOpenSuite = new JButton("Open Suite");

        suiteScrollPaneTree = new JScrollPane(treeSuite, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        suiteScrollPaneTree.setMinimumSize(new Dimension(width - 100, 600));
        btnOpenSuite.setPreferredSize(new Dimension( 100, 40));

        suiteScrollPaneTree.setMaximumSize(new Dimension(width - 100, 600));
        btnOpenSuite.setPreferredSize(new Dimension( 100, 40));


        treeSuite.addTreeSelectionListener(this);
        treeSuite.addKeyListener(this);

        btnOpenSuite.addActionListener(this);
        btnSaveSuite.addActionListener(this);

        txtSuiteOpened = new JTextField("Suite.xml");
        txtSuiteOpened.setEditable(false);
        txtSuiteOpened.setPreferredSize(new Dimension((int)this.getMinimumSize().getWidth() - (int)btnOpenSuite.getMinimumSize().getWidth(), 20));
        txtSuiteOpened.setFont(new Font("Courier", Font.BOLD,12));
        txtSuiteOpened.setBorder(null);
        txtSuiteOpened.setCaretPosition(0);

        fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("XML Files", "xml");
        fc.setFileFilter(filter);

        //https://docs.oracle.com/javase/tutorial/uiswing/layout/group.html
        GroupLayout layout = new GroupLayout(this);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(suiteScrollPaneTree)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(btnOpenSuite)
                        .addComponent(btnSaveSuite)
                )
                .addComponent(txtSuiteOpened)
        );
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(suiteScrollPaneTree)
                )
                .addGroup(layout.createSequentialGroup()
                        .addComponent(btnOpenSuite)
                        .addComponent(btnSaveSuite)
                )
                .addComponent(txtSuiteOpened)
        );
        this.setLayout(layout);
    }

    public JButton getOpenSuiteBtn(){
        return btnOpenSuite;
    }

    public JButton getSaveSuiteBtn(){
        return btnSaveSuite;
    }

    public JTextField getTxtSuiteOpened(){
        return txtSuiteOpened;
    }

    public JTree getSuiteTree(){
        return treeSuite;
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        JTree tree = (JTree) e.getSource();
        TreePath suiteTreeSelectedPath = tree.getSelectionPath();
        if(suiteTreeSelectedPath != null) //While adding item to the tree, it somehow looses previously set selection on tree-node and suiteTreeSelectedPath becomse null
            suiteComposerController.updateSuiteTreeItemSelected(suiteTreeSelectedPath);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int dlgAction;
        if(e.getSource() == btnOpenSuite){
            dlgAction = fc.showOpenDialog(this);
            if(dlgAction == fc.APPROVE_OPTION){
                TestLogger.logInfo(String.format("File selected: %s", fc.getSelectedFile().getAbsolutePath()));
                suiteComposerController.openSuite(fc.getSelectedFile().getAbsolutePath(), treeSuite);
            }else{
                TestLogger.logDebug("Unknown action occurred");
            }
        } else if(e.getSource() == btnSaveSuite){
            dlgAction = fc.showSaveDialog(this);
            if(dlgAction == fc.APPROVE_OPTION){
                TestLogger.logInfo(String.format("File selected: %s", fc.getSelectedFile().getAbsolutePath()));
                suiteComposerController.saveSuite(treeSuite, fc.getSelectedFile().getAbsolutePath());
            }else{
                TestLogger.logDebug("Unknown action occurred");
            }
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {
        //no implementation
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //no implementation
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()){
            case KeyEvent.VK_BACK_SPACE:
            case KeyEvent.VK_DELETE:
                suiteComposerController.deleteAction();
                break;
            default:
                break;
        }
    }
}
