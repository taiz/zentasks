package zentasks;

import java.io.*;
import java.net.URL;
import javafx.fxml.FXMLLoader;

/**
 *
 * @author miyabetaiji
 */
public class Util {
    public static Object loadFXML(Object o, String path) throws FXMLLoadException {
        try {
            URL url = o.getClass().getResource(path);
            return loadFXML(o.getClass().getResourceAsStream(path), url);
        } catch (FXMLLoadException ex) {
            throw new FXMLLoadException("Failed to load" + path, ex);
        }
    }
    
    public static Object loadFXML(InputStream is, URL url) throws FXMLLoadException {
        try {
            FXMLLoader loader = new FXMLLoader(url);
            loader.load(is);
            return loader.getController();
        } catch (IOException ex) {
            throw new FXMLLoadException("Failed to load", ex);
        }
    }
}
