package composer.ui;

import common.Utils;
import composer.OptionsController;
import composer.SuiteComposerController;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * Created by ispitkovskyi on 1/21/2017.
 */
public class SuiteComposerFrame extends JFrame{
    private int windowWidth = 1024 ;
    private int windowHeight = 768;

    private JPanel panel;

    FrameworkActionsPanel frameworkActionsPanel;
    SuiteActionsPanel suiteActionsPanel;
    SuiteManagementPanel suiteManagementPanel;
    LoggerPanel loggerPanel;

    SuiteComposerController suiteComposerController;
    OptionsController optionsController;

    JMenuBar menuBar;
    JMenu toolsMenu;
    JMenuItem optionsItem;

    public SuiteComposerFrame(SuiteComposerController suiteComposerController, OptionsController optionsController) {
        this.suiteComposerController = suiteComposerController;
        this.optionsController = optionsController;

        windowWidth = suiteComposerController.getMaxWidth();
        windowHeight = suiteComposerController.getMaxHeight();

        this.setTitle("TestNG Suite Composer");
        this.setIconImage(new ImageIcon(Utils.class.getClassLoader().getResource("images/title.png")).getImage());
        this.setBounds(0, 0, windowWidth, windowHeight);
        this.setPreferredSize(new Dimension(windowWidth, windowHeight));
        this.setLayout(new FlowLayout());
        this.setResizable(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frameworkActionsPanel = new FrameworkActionsPanel(this.suiteComposerController);
        suiteActionsPanel = new SuiteActionsPanel(this.suiteComposerController);
        suiteManagementPanel = new SuiteManagementPanel(this.suiteComposerController);
        loggerPanel = new LoggerPanel(this.suiteComposerController);

        panel = new JPanel();
        panel.setBounds(0, 0, windowWidth, windowHeight);

        JMenuBar menuBar = initMenu();

        this.setJMenuBar(menuBar);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JScrollPane mainPanelScrollers = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        mainPanelScrollers.setWheelScrollingEnabled(true);
        this.setContentPane(mainPanelScrollers);

        GroupLayout layout = new GroupLayout(panel);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        JSeparator s1 = new JSeparator(JPopupMenu.Separator.VERTICAL);
        JSeparator s2 = new JSeparator(JPopupMenu.Separator.VERTICAL);
        JSeparator sh1 = new JSeparator(JPopupMenu.Separator.HORIZONTAL);
        JSeparator sh2 = new JSeparator(JPopupMenu.Separator.HORIZONTAL);
        layout.setVerticalGroup(layout.createSequentialGroup()
                                //.addComponent(sh2)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(frameworkActionsPanel)
                                        .addComponent(s1)
                                        .addComponent(suiteManagementPanel)
                                        //.addComponent(s2)
                                        .addComponent(suiteActionsPanel)
                                )
                                //.addComponent(sh1)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                        .addComponent(loggerPanel)
                                )
        );
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                //.addComponent(sh2)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(frameworkActionsPanel)
                        .addComponent(s1)
                        .addComponent(suiteManagementPanel)
                        //.addComponent(s2)
                        .addComponent(suiteActionsPanel)
                )
                //.addComponent(sh1)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(loggerPanel)
                )
        );
        panel.setLayout(layout);

    }

    public JButton getOpenFrameworkBtn(){
        return frameworkActionsPanel.getOpenFrameworkBtn();
    }

    public JTextField getTxtJarOpened(){
        return frameworkActionsPanel.getTxtJarOpened();
    }

    public JButton getSortAscendingBtn() {return frameworkActionsPanel.getBtnSortAscending();}

    public JButton getSortDescendingBtn() {return frameworkActionsPanel.getBtnSortDescending();}

    public JTree getActionsTree(){
        return frameworkActionsPanel.getActionsTree();
    }

    public JButton getOpenSuiteBtn(){
        return suiteActionsPanel.getOpenSuiteBtn();
    }

    public JButton getSaveSuiteBtn(){
        return suiteActionsPanel.getSaveSuiteBtn();
    }

    public JTree getSuiteTree(){
        return suiteActionsPanel.getSuiteTree();
    }

    public JTextField getTxtSuiteOpened(){
        return suiteActionsPanel.getTxtSuiteOpened();
    }

    public JButton getAddActionAfterSelectedButton(){
        return suiteManagementPanel.getAddActionAfterSelectedButton();
    }

    public JButton getAddActionBeforeSelectedButton(){
        return suiteManagementPanel.getAddActionBeforeSelectedButton();
    }

    public JButton getDeleteActionButton(){
        return suiteManagementPanel.getDeleteActionButton();
    }

    public JButton getApplyActionChangesButton(){
        return suiteManagementPanel.getApplyActionChangesButton();
    }

    public JTextField getTxtTestName(){
        return suiteManagementPanel.getTxtTestName();
    }

    public JTable getTableTestParams(){
        return suiteManagementPanel.getTableTestParams();
    }

    public JTextArea getLogTextArea(){
        return loggerPanel.getTextArea();
    }

    public JTextPane getLogTextPane(){
        return loggerPanel.getTextPane();
    }

    public JLabel getLblErrMsg(){
        return suiteManagementPanel.getLblErrMsg();
    }

    private JMenuBar initMenu(){
        menuBar = new JMenuBar();
        toolsMenu = new JMenu("Tools");
        toolsMenu.setMnemonic(KeyEvent.VK_O);
        toolsMenu.getAccessibleContext().setAccessibleDescription(
                "Preferences settings");
        menuBar.add(toolsMenu);

        optionsItem = new JMenuItem("Options...");
        optionsItem.addActionListener(new MenuActionAdapter());
        toolsMenu.add(optionsItem);

        return menuBar;
    }

    public void resetActionsTree(){
        JTree actionsTree = getActionsTree();
        TreeModel model = (TreeModel) actionsTree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
        root.removeAllChildren();
        actionsTree.setModel(model);
        actionsTree.updateUI();
    }

    class MenuActionAdapter implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == optionsItem){
                optionsController.openOptionsPanel();
            }
        }
    }
}
