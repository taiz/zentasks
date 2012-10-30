package zentasks;

import java.io.*;
import javafx.fxml.FXMLLoader;

/**
 *
 * @author miyabetaiji
 */
public class Util {
    //private static String rootPath = "zentasks/";
    
    public static Object loadFXML(Object o, String path) throws FXMLLoadException {
        try {
            //ClassLoader cloader = Thread.currentThread().getContextClassLoader();
            //InputStream fxmlStream = new FileInputStream(rootPath + path);
            //InputStream fxmlStream = cloader.getResourceAsStream(path);
            FXMLLoader loader = new FXMLLoader();
            loader.load(o.getClass().getResourceAsStream(path));
            return loader.getController();
        } catch (IOException ex) {
            throw new FXMLLoadException("Failed to load " + path, ex);
        }
    }    
}
