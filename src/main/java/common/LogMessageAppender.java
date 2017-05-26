package common;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

import javax.swing.*;
import javax.swing.text.*;

import java.awt.*;

import static common.Constants.ERROR_PREFIX;

/**
 * Created by ispitkovskyi on 1/20/2017.
 */
public class LogMessageAppender extends AppenderSkeleton {
    private JTextArea jLogArea;
    private JTextPane jLogPane;

    private LogMessageAppender(){
        this.setLayout(new PatternLayout("[%p] %d{yyyy-MM-dd HH:mm:ss} - %m%n"));  //does NOT work for now. Need to investigate
        //this.setLayout(new SimpleLayout());
    }

    public LogMessageAppender(JTextPane logPane) {
        this();
        jLogPane = logPane;

    }

    public LogMessageAppender(JTextArea jLogArea) {
        this();
        this.jLogArea = jLogArea;
    }

    protected void append(LoggingEvent event)
    {
        String message = event.getMessage().toString();
        Color color = Color.BLACK;

        if(jLogArea != null) {
            jLogArea.setForeground(color);
            if (event.getLevel().equals(TestLogger.getLogLevel())) {
                jLogArea.append(message + "\n");
            } else {
                jLogArea.append(message + "\n");
            }
        }

        if(jLogPane != null) {
            if(message.contains(ERROR_PREFIX)){
                color = Color.RED;
            }

            try {
                appendToPane(jLogPane, message, color);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }

    private void appendToPane(JTextPane tp, String msg, Color c) throws BadLocationException {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tp.replaceSelection(msg);
        tp.getStyledDocument().insertString(len, "\n" + msg, aset);
        tp.scrollRectToVisible(
                new Rectangle(0,tp.getHeight()-2,1,1));
    }

    public void close()
    {
    }
    public boolean requiresLayout()
    {
        return false;
    }
}
