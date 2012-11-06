package zentasks;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author miyabetaiji
 */
public class Context {
    private static Context instance = new Context();
    private Map<String, Object> data = new HashMap<String, Object>();

    private Context() {}

    public static Context getInstance() { return instance; }

    public Map<String, Object> data() { return data; }
}
