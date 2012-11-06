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
            return loadFXML(o.getClass().getResourceAsStream(path));
        } catch (FXMLLoadException ex) {
            throw new FXMLLoadException("Failed to load" + path, ex);
        }
    }
    
    public static Object loadFXML(InputStream is) throws FXMLLoadException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.load(is);
            return loader.getController();
        } catch (IOException ex) {
            throw new FXMLLoadException("Failed to load", ex);
        }
    }
}
