import composer.SuiteComposerController;

import java.io.File;

/**
 * Created by igors on 17.12.16.
 */
public class SuiteComposerRunner {
    public static void main(String args[]){
        String rootPath = new File("").getAbsolutePath();
        System.setProperty("TestSuiteComposer.home", rootPath);
        System.setProperty("javax.xml.accessExternalSchema", "all");

        SuiteComposerController suiteComposerController = new SuiteComposerController();
        suiteComposerController.init();
    }
}
