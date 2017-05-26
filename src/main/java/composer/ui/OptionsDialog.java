package composer.ui;

import common.TestLogger;
import common.Utils;
import composer.OptionsController;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import static common.Constants.*;

/**
 * Created by ispitkovskyi on 2/9/2017.
 */
public class OptionsDialog extends JDialog implements ActionListener, ItemListener {
    private JPanel panel;

    private OptionsController optionsController;

    //private JCheckBox chkForceKeydriven;

    private JLabel classpathHeader;
    private JTextArea classpathAddedTxtArea;
    JScrollPane classpathAddedTxtAreaScrollPane;
    private JButton btnAddClasspath;
    private JButton btnClearClasspath;
    private JFileChooser fc;
    private JTextArea txtClasspathHint;

    private JButton btnSave;
    private JButton btnClose;

    private JLabel logLevelsLbl;
    private JComboBox<String> logLevels;

    public OptionsDialog(JFrame parentJFrame, boolean isModal, OptionsController optionsController){
        super(parentJFrame, isModal);
        this.optionsController = optionsController;
        int width = 500;
        int height = 320;

        this.setSize(new Dimension(width, height));
        this.setLocation(parentJFrame.getWidth()/2 - width/2, parentJFrame.getHeight()/2 - height/2);
        this.setTitle("Options");
        this.setIconImage(Utils.createImageIcon("images/settings.png", 20, 20).getImage());

        panel = new JPanel();
        panel.setBounds(0, 0, width, height);

        /*
        chkForceKeydriven = new JCheckBox("Force Keydriven Suite Structure");
        chkForceKeydriven.setSelected(true);
        chkForceKeydriven.setEnabled(false);

        chkForceKeydriven.setMinimumSize(new Dimension(100, 20));
        chkForceKeydriven.setMaximumSize(new Dimension(250, 20));
*/
        classpathHeader = new JLabel("CLASSPATH:");

        btnAddClasspath = new JButton("Add Classpath...");
        btnAddClasspath.setMaximumSize(new Dimension(120, 20));
        btnAddClasspath.setToolTipText("Add directory to the classpath");

        btnAddClasspath.addActionListener(this);

        btnClearClasspath = new JButton("Clear All");
        btnClearClasspath.setMaximumSize(new Dimension(120, 20));
        btnClearClasspath.setToolTipText("Clear classpath settings");

        btnClearClasspath.addActionListener(this);


        fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JAR Files", "jar");
        fc.setFileFilter(filter);


        classpathAddedTxtArea = new JTextArea();
        classpathAddedTxtArea.setEditable(false);
        DefaultCaret caret = (DefaultCaret) classpathAddedTxtArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        classpathAddedTxtAreaScrollPane = new JScrollPane(classpathAddedTxtArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        classpathAddedTxtAreaScrollPane.setWheelScrollingEnabled(true);

        classpathAddedTxtAreaScrollPane.setMinimumSize(new Dimension((int)(this.getWidth() * 0.65), this.getHeight()/3));
        classpathAddedTxtAreaScrollPane.setMaximumSize(new Dimension((int)(this.getWidth() * 0.65), this.getHeight()/3));

        txtClasspathHint = new JTextArea("If you see error saying: \"Could not load class ...\", there must be some dependencies (JARs) required to open the framework *.jar. Add these dependencies explicitly here.");
        txtClasspathHint.setEditable(false);
        txtClasspathHint.setLineWrap(true);
        txtClasspathHint.setWrapStyleWord(true);
        txtClasspathHint.setBackground(this.getContentPane().getBackground());
        txtClasspathHint.setMinimumSize(new Dimension(width - 60, 30));
        txtClasspathHint.setMaximumSize(new Dimension(width - 60, 30));
        txtClasspathHint.setFont(new Font("Courier", Font.BOLD,10));
        txtClasspathHint.setBorder(null);
        txtClasspathHint.setCaretPosition(0);


        logLevelsLbl = new JLabel("Logging Level:");
        logLevelsLbl.setMaximumSize(new Dimension(60, 30));

        logLevels = new JComboBox<>(new String [] {LOG_LEVEL_INFO, LOG_LEVEL_DEBUG, LOG_LEVEL_ERROR, LOG_LEVEL_ALL});
        logLevels.setMaximumSize(new Dimension(80, 30));
        logLevels.addItemListener(this);

        btnSave = new JButton("Save");
        btnSave.setSize(50, 20);
        btnSave.addActionListener(this);

        btnClose = new JButton("Close");
        btnClose.setSize(50, 20);
        btnClose.addActionListener(this);

        JSeparator separator = new JSeparator();
        JSeparator separator2 = new JSeparator();

        GroupLayout layout = new GroupLayout(panel);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setVerticalGroup(layout.createSequentialGroup()
                        /*.addGroup(layout.createParallelGroup()
                            .addComponent(chkForceKeydriven)
                        )*/
                        .addGap(10)
                        .addComponent(separator)
                        .addGap(10)
                        .addComponent(classpathHeader)
                        .addGroup(layout.createParallelGroup()
                             .addComponent(classpathAddedTxtAreaScrollPane)
                             .addGroup(layout.createSequentialGroup()
                                 .addComponent(btnAddClasspath)
                                 .addComponent(btnClearClasspath)
                             )
                        )
                        .addComponent(txtClasspathHint)
                        .addGap(10)
                        .addComponent(separator2)
                        .addGap(10)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(logLevelsLbl)
                        .addComponent(logLevels)
                )
        );
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        //.addComponent(chkForceKeydriven)
                        .addComponent(separator)
                        .addComponent(classpathHeader)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(classpathAddedTxtAreaScrollPane)
                            .addGroup(layout.createParallelGroup()
                                .addComponent(btnAddClasspath)
                                .addComponent(btnClearClasspath)
                            )
                        )
                        .addComponent(txtClasspathHint)
                        .addComponent(separator2)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(logLevelsLbl)
                                .addComponent(logLevels)
                        )
        );
        panel.setLayout(layout);

        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnSave);
        buttonPanel.add(btnClose);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    public JTextArea getClasspathAddedTxtArea(){return classpathAddedTxtArea;}

    @Override
    public void actionPerformed(ActionEvent e) {
        int dlgAction;
        if(e.getSource() == btnAddClasspath){
            dlgAction = fc.showOpenDialog(this);
            if(dlgAction == fc.APPROVE_OPTION){
                TestLogger.logInfo(String.format("Adding selection to the CLASSPATH: %s", fc.getSelectedFile().getAbsolutePath()));
                optionsController.addClasspath(fc.getSelectedFile().getAbsolutePath());
            }else{
                TestLogger.logDebug("Unknown action occurred");
            }
        }else if (e.getSource() == btnClearClasspath){
            optionsController.clearClasspath();
        }else if(e.getSource() == btnSave){
            optionsController.saveOptions();
        }else if(e.getSource() == btnClose){
            this.dispose();
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        String logLevel = e.getItem().toString();
        optionsController.changeLogLevel(logLevel);
    }
}
