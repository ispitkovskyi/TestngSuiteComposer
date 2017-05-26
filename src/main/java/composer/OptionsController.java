package composer;

import common.TestLogger;
import composer.ui.OptionsDialog;
import org.apache.log4j.Level;

import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import static common.Constants.*;

/**
 * Created by ispitkovskyi on 2/9/2017.
 */
public class OptionsController {
    private static ArrayList<URL> dependencyPaths;
    private static ArrayList<URL> dependencyPathsTemp;

    private Level logLevel;

    private SuiteComposerController suiteComposerController;
    OptionsDialog optionsDialog;

    public OptionsController(SuiteComposerController suiteComposerController){
        this.suiteComposerController = suiteComposerController;
    }

    public void init(){
        dependencyPaths = new ArrayList<>();
        dependencyPathsTemp = new ArrayList<>();
        optionsDialog = new OptionsDialog(suiteComposerController.getSuiteComposerFrame(), true, this);
        optionsDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void openOptionsPanel(){
        if(dependencyPaths != null && !dependencyPaths.isEmpty()){
            optionsDialog.getClasspathAddedTxtArea().setText("");
            for(int i=0; i<dependencyPaths.size(); i++){
                optionsDialog.getClasspathAddedTxtArea().append(dependencyPaths.get(i).getPath() + "\n");
            }
        }

        //todo: Get LOG LEVEL

        //todo: Get suite structure mode

        optionsDialog.setVisible(true);
    }

    public void clearClasspath(){
        dependencyPathsTemp.clear();
        dependencyPaths.clear();
        optionsDialog.getClasspathAddedTxtArea().setText("");
    }

    public void addClasspath(String path) {
        ArrayList<String> paths = new ArrayList<>();
        if(path.contains(".jar")) {
            paths.add(path);
        }else{
            paths.addAll(getJars(path));
        }

        for(int i=0; i<paths.size(); i++){
            try {
                URL url = new URL("jar:file:" + paths.get(i) + "!/");
                dependencyPathsTemp.add(url);
                optionsDialog.getClasspathAddedTxtArea().append(url.getPath() + "\n");
            } catch (Exception e) {
                TestLogger.logError("Could not add selected file/directory to the CLASSPATH");
            }
        }
    }

    private ArrayList<String> getJars(String path){
        ArrayList<String> jars = new ArrayList<>();
        File dir = new File(path);
        File[] files = dir.listFiles();
        for(int i=0; i<files.length; i++){
            File file = files[i];
            if(file.getName().toLowerCase().contains(".jar")){
                jars.add(file.getAbsolutePath());
            }
        }
        return jars;
    }

    public void changeLogLevel(String levelName){
        switch(levelName){
            case LOG_LEVEL_INFO:
                logLevel = Level.INFO;
                break;
            case LOG_LEVEL_DEBUG:
                logLevel = Level.DEBUG;
                break;
            case LOG_LEVEL_ALL:
                logLevel = Level.ALL;
                break;
            case LOG_LEVEL_ERROR:
                logLevel = Level.ERROR;
                break;
            default:
                logLevel = Level.INFO;
        }
    }

    public void saveOptions() {
        dependencyPaths.addAll(dependencyPathsTemp); //better to merge to avoid duplicates
        dependencyPathsTemp.clear();

        TestLogger.setLogLevel(logLevel);

        //todo: Set suite structure mode

        optionsDialog.dispose();
        TestLogger.logInfo("Options saved successfully");
    }

    public static ArrayList<URL> getDependencyPaths(){
        return dependencyPaths;
    }
}
