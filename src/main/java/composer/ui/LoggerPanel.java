package composer.ui;

import composer.SuiteComposerController;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;

/**
 * Created by ispitkovskyi on 1/21/2017.
 */
public class LoggerPanel extends JPanel{
    private SuiteComposerController suiteComposerController;
    private JTextArea textArea;
    JScrollPane textAreaScrollPane;

    private JTextPane textPane;
    JScrollPane textPaneScrollPane;

    public LoggerPanel(SuiteComposerController suiteComposerController){
        this.suiteComposerController = suiteComposerController;
        this.setMinimumSize(new Dimension(suiteComposerController.getMaxWidth()-50, 300));
        this.setMaximumSize(new Dimension(suiteComposerController.getMaxWidth()-50, 300));
        init();
    }

    public void init(){
        textArea= new JTextArea();
        textArea.setEditable(false);
        DefaultCaret caret = (DefaultCaret) textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        textAreaScrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        textAreaScrollPane.setWheelScrollingEnabled(true);

        textAreaScrollPane.setPreferredSize(new Dimension((int)this.getMinimumSize().getWidth()-20, (int)this.getMinimumSize().getHeight()-20));

        textPane= new JTextPane();
        textPane.setEditable(false);
        DefaultCaret caretPane = (DefaultCaret) textPane.getCaret();
        caretPane.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);;

        textPaneScrollPane = new JScrollPane(textPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        textPaneScrollPane.setWheelScrollingEnabled(true);

        textPaneScrollPane.setPreferredSize(new Dimension((int)this.getMinimumSize().getWidth()-20, (int)this.getMinimumSize().getHeight()-20));

        initLayout();
    }

    private void initLayout(){
        //https://docs.oracle.com/javase/tutorial/uiswing/layout/group.html
        GroupLayout layout = new GroupLayout(this);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(textPaneScrollPane)
        );
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(textPaneScrollPane)
        );
        this.setLayout(layout);
    }

    public JTextArea getTextArea(){
        return textArea;
    }

    public JTextPane getTextPane() {return textPane;}

}
