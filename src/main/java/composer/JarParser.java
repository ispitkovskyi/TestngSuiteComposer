package composer;

import common.TestLogger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.concurrent.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by igors on 18.12.16.
 */
public class JarParser {
    SuiteComposerController suiteComposerController;

    public JarParser(SuiteComposerController suiteComposerController){
        this.suiteComposerController = suiteComposerController;
    }

    public JSONObject parseJarFile(String pathToJar) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<JSONObject> callable = new Callable<JSONObject>() {
            @Override
            public JSONObject call() throws IOException, ClassNotFoundException {
                return parse(pathToJar);
            }
        };
        Future<JSONObject> futureJson = executor.submit(callable);
        executor.shutdown();
        return futureJson.get();
    }

    private JSONObject parse(String pathToJar)throws IOException, ClassNotFoundException{
        JSONObject testActionsJson = new JSONObject();
        JarFile jarFile = new JarFile(pathToJar);
        Enumeration<JarEntry> jarEntries = jarFile.entries();
        URLClassLoader cl = prepareClassLoader(pathToJar);

        while (jarEntries.hasMoreElements()) {
            JarEntry je = jarEntries.nextElement();
            if(je.isDirectory() || !je.getName().endsWith(".class")){
                continue;
            }

            // -6 because of .class - truncate last 6 characters to remove .class extension
            String className = je.getName().substring(0,je.getName().length()-6);
            className = className.replace('/', '.');
            try {
                Class cls = cl.loadClass(className);
                TestLogger.logDebug("Load class " + className);

                Method[] methods = cls.getDeclaredMethods();
                for(int j=0; j<methods.length; j++) {
                    Test testMethod = methods[j].getDeclaredAnnotation(Test.class);
                    TestLogger.logDebug("Test annotation found for method = " + methods[j].getName() + " in class " + className);

                    if(testMethod != null){
                        if(!testActionsJson.keySet().contains(className)/*!testActions.containsKey(className)*/) {
                            testActionsJson.put(className, new JSONObject());
                        }
                        String methodName = methods[j].getName();
                        testActionsJson.getJSONObject(className).put(methodName, new JSONArray());

                        Parameters parameters = methods[j].getAnnotation(Parameters.class);
                        if(parameters != null){
                            TestLogger.logDebug("\tParameters annotation found for method = " + methods[j].getName() + " in class " + className);
                            String [] parameterValues = parameters.value();
                            for(int i=0; i<parameterValues.length; i++){
                                TestLogger.logDebug("\t\tParameter value = " + parameterValues[i] + " found for method = " + methods[j].getName() + " in class " + className);
                                testActionsJson.getJSONObject(className).getJSONArray(methods[j].getName()).put(parameterValues[i]);
                            }
                        }
                    }
                }
            }catch (NoClassDefFoundError err){
                TestLogger.logError("Could not load class " + className + "\n" + err.getMessage());
                suiteComposerController.printError("Could not load some classes from JAR. Check classpath.");
            }
        }
        return testActionsJson;
    }

    private URLClassLoader prepareClassLoader(String jarPath){
        URLClassLoader loader = null;
        File f = new File(jarPath);
        if(f != null){
            String parentDir = f.getParent();
            //URL[] urls = {new URL("jar:file:" + jarPath + "!/"), new URL("file://" + parentDir + "/")};
            File [] jarDependencies = new File(parentDir).listFiles();
            if(jarDependencies != null) {
                TestLogger.logDebug("Number of dependencies (jar files): " + jarDependencies.length);
                ArrayList<URL> dependencyPaths = new ArrayList<URL>();
                for(int i=0; i<jarDependencies.length; i++) {
                    try {
                        dependencyPaths.add(new URL("jar:file:" + jarDependencies[i] + "!/"));
                    } catch (MalformedURLException urle) {
                        TestLogger.logError("Could not add dependencies to the CLASSPATH: " + urle.getMessage());
                    }
                }

                //Add CLASSPATH entries saved in Options
                dependencyPaths.addAll(OptionsController.getDependencyPaths());

                loader = URLClassLoader.newInstance(dependencyPaths.toArray(new URL [dependencyPaths.size()]));
            }
        }
        return loader;
    }

}
