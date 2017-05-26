package common;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

/**
 * Created by ispitkovskyi on 1/20/2017.
 */
public class Utils {

    /** Returns an ImageIcon, or null if the path was invalid. */
    public static ImageIcon createImageIcon(String path, int width, int height) {
        //java.net.URL imgURL = SuiteComposerModel.class.getResource(path);
        java.net.URL imgURL = Utils.class.getClassLoader().getResource(path);
        if (imgURL != null) {
            ImageIcon icn = new ImageIcon(imgURL);
            Image img = icn.getImage();
            Image newimg = img.getScaledInstance(width, height,  java.awt.Image.SCALE_SMOOTH);
            return new ImageIcon(newimg);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    public static void showWarningDialog(JPanel parent, String warningMsg, Consumer<Object> consumer){
        Object[] options = {"OK"};
        int input = JOptionPane.showOptionDialog(parent, warningMsg, "Warning", JOptionPane.PLAIN_MESSAGE, JOptionPane.ERROR_MESSAGE,null, options, options[0]);
        if(input == JOptionPane.OK_OPTION)
        {
            consumer.accept(null);
        }
    }

    public static void showWarningDialog(JFrame parent, String warningMsg, Consumer<Object> consumer){
        Object[] options = {"OK"};
        int input = JOptionPane.showOptionDialog(parent, warningMsg, "Warning", JOptionPane.PLAIN_MESSAGE, JOptionPane.ERROR_MESSAGE,null, options, options[0]);
        if(input == JOptionPane.OK_OPTION)
        {
            consumer.accept(null);
        }
    }

    public static void selectText(JTextField textField){
        textField.setSelectionStart(0);
        textField.setSelectionEnd(textField.getText().length());
    }
}
