package composer.ui;

import common.Constants;
import common.Utils;
import composer.SuiteComposerController;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.function.BiConsumer;

/**
 * Created by ispitkovskyi on 1/21/2017.
 */
public class SuiteManagementPanel extends JPanel implements TreeSelectionListener, ActionListener{
    int width;

    private SuiteComposerController suiteComposerController;

    private JLabel lblTestName;
    private JTextField txtTestName;
    private JTable tableTestParams;
    JScrollPane paramsScrollPane;

    private JButton btnApplyActionChanges;

    private JButton btnAddActionAfterSelected;
    private JButton btnAddActionBeforeSelected;
    private JButton btnDeleteAction;

    private JLabel lblErrMsg;


    public SuiteManagementPanel(SuiteComposerController suiteComposerController){
        width = 550;
        this.suiteComposerController = suiteComposerController;
        this.setSize(new Dimension(width, 660));
        init();
    }

    public void init(){
        lblTestName = new JLabel("Test Action:");

        txtTestName = new JTextField(Constants.DEFAULT_TEST_NAME);
        txtTestName.setEnabled(false);
        txtTestName.setFocusable(true);
        txtTestName.setForeground(Color.BLACK);
        Utils.selectText(txtTestName);

        tableTestParams = new JTable();
        tableTestParams.setRowHeight(20);
        tableTestParams.setForeground(Color.blue);
        tableTestParams.setColumnSelectionAllowed(true);
        tableTestParams.setRowSelectionAllowed(true);
        tableTestParams.setSize(new Dimension(width, 580));
        paramsScrollPane = new JScrollPane(tableTestParams, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        paramsScrollPane.setWheelScrollingEnabled(true);

        btnApplyActionChanges = new JButton(Utils.createImageIcon("images/applyParameters1.png", 50, 50));
        btnApplyActionChanges.setEnabled(false);
        btnApplyActionChanges.setToolTipText("Apply parameters changed");

        btnAddActionBeforeSelected = new JButton();
        btnAddActionBeforeSelected.setIcon(Utils.createImageIcon("images/addBefore2.png", 40, 40));
        btnAddActionBeforeSelected.setToolTipText("Insert before selected action in suite");
        btnAddActionBeforeSelected.setEnabled(false);

        btnAddActionAfterSelected = new JButton();
        btnAddActionAfterSelected.setIcon(Utils.createImageIcon("images/addAfter2.png", 40, 40));
        btnAddActionAfterSelected.setToolTipText("Insert after selected action in suite");
        btnAddActionAfterSelected.setEnabled(false);

        btnDeleteAction = new JButton();
        btnDeleteAction.setIcon(Utils.createImageIcon("images/deleteAction.png", 30, 30));
        btnDeleteAction.setToolTipText("Delete selected action from suite (Delete)");
        btnDeleteAction.setEnabled(false);

        lblErrMsg = new JLabel("Error: ");
        lblErrMsg.setForeground(Color.GRAY);

        paramsScrollPane.setMinimumSize(new Dimension(width, 570));
        lblTestName.setMinimumSize(new Dimension(70, 20));
        txtTestName.setMinimumSize(new Dimension((int) (paramsScrollPane.getMinimumSize().getWidth() - lblTestName.getMinimumSize().getWidth()) - 10, 20));
        btnApplyActionChanges.setMinimumSize(new Dimension(50, 50));
        btnDeleteAction.setMinimumSize(new Dimension(80, 40));
        btnAddActionAfterSelected.setMinimumSize(new Dimension(80, 40));
        btnAddActionBeforeSelected.setMinimumSize(new Dimension(80, 40));
        lblErrMsg.setMinimumSize(new Dimension(this.getWidth() - 20, 20));

        paramsScrollPane.setMaximumSize(new Dimension(width, 570));
        lblTestName.setMaximumSize(new Dimension(70, 20));
        txtTestName.setMaximumSize(new Dimension((int) (paramsScrollPane.getMaximumSize().getWidth() - lblTestName.getMaximumSize().getWidth()) - 10, 20));
        btnApplyActionChanges.setMaximumSize(new Dimension(50, 50));
        btnDeleteAction.setMaximumSize(new Dimension(80, 40));
        btnAddActionAfterSelected.setMaximumSize(new Dimension(80, 40));
        btnAddActionBeforeSelected.setMaximumSize(new Dimension(80, 40));
        lblErrMsg.setMaximumSize(new Dimension(this.getWidth() - 20, 20));

        btnApplyActionChanges.addActionListener(this);
        btnAddActionAfterSelected.addActionListener(this);
        btnAddActionBeforeSelected.addActionListener(this);
        btnDeleteAction.addActionListener(this);


        txtTestName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                btnApplyActionChanges.setEnabled(true);
            }
        });

        tableTestParams.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if(evt.getPropertyName() == "tableCellEditor")
                    btnApplyActionChanges.setEnabled(true);
            }
        });

        //https://docs.oracle.com/javase/tutorial/uiswing/layout/group.html
        GroupLayout layout = new GroupLayout(this);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setVerticalGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(lblTestName)
                                                .addComponent(txtTestName))
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(paramsScrollPane)
                                                .addGroup(layout.createSequentialGroup()
                                                        .addComponent(btnApplyActionChanges)
                                                        .addGap(tableTestParams.getHeight()/3)
                                                        .addComponent(btnAddActionBeforeSelected)
                                                        .addComponent(btnAddActionAfterSelected)
                                                        .addComponent(btnDeleteAction)
                                                )
                                        )
                                        .addComponent(lblErrMsg)
        );
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(lblTestName)
                        .addComponent(txtTestName)
                )
                .addGroup(layout.createSequentialGroup()
                        .addComponent(paramsScrollPane)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(btnApplyActionChanges)
                                .addComponent(btnAddActionBeforeSelected)
                                .addComponent(btnAddActionAfterSelected)
                                .addComponent(btnDeleteAction)
                        )
                )
                .addComponent(lblErrMsg)
        );
        this.setLayout(layout);


    }

    public JButton getDeleteActionButton(){
        return btnDeleteAction;
    }
    public JButton getApplyActionChangesButton(){
        return btnApplyActionChanges;
    }
    public JButton getAddActionAfterSelectedButton(){
        return btnAddActionAfterSelected;
    }
    public JButton getAddActionBeforeSelectedButton(){
        return btnAddActionBeforeSelected;
    }
    public JTextField getTxtTestName(){
        return txtTestName;
    }
    public JTable getTableTestParams(){ return tableTestParams; }
    public JLabel getLblErrMsg(){
        return lblErrMsg;
    }
    public JPanel getSuiteManagementPanel(){return this;}

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        JTree tree = (JTree) e.getSource();
        TreePath actionsTreeSelectedPath = tree.getSelectionPath();
        suiteComposerController.updateSuiteTreeItemSelected(actionsTreeSelectedPath);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAddActionAfterSelected) {
            addActionToSuite(suiteComposerController::addActionAfterSelected);
        } else if (e.getSource() == btnAddActionBeforeSelected) {
            addActionToSuite(suiteComposerController::addActionBeforeSelected);
        } else if (e.getSource() == btnDeleteAction) {
            suiteComposerController.deleteAction();
        } else if (e.getSource() == btnApplyActionChanges) {
            suiteComposerController.applyActionChanges();
        }
    }

    private void addActionToSuite(BiConsumer<JTable, String> actionAdder){
        //if(getSelectedItemLevel(actionsTreeSelectedPath) == Constants.METHOD_LEVEL_IN_FRAMEWORK) {
            //if(suiteSelectedPath == null || getSelectedItemLevel(suiteSelectedPath) <= Constants.CLASS_LEVEL_IN_FRAMEWORK) {  //todo: use constants for levels, like LEVEL_CLASS, LEVEL_METHOD, LEVEL_PARAMETER
                String actionName = txtTestName.getText();
                if(actionName.isEmpty() || actionName.equalsIgnoreCase(Constants.DEFAULT_TEST_NAME)) {
                    Utils.showWarningDialog(this, "You must specify action name first", (any) -> {
                        txtTestName.grabFocus();
                        Utils.selectText(txtTestName);
                    });
                    return;
                }
                //parseTreePath(actionsTreeSelectedPath); //todo: temporary logging. Will be removed
                actionAdder.accept(tableTestParams, actionName);
            //}
        //}
    }
}
