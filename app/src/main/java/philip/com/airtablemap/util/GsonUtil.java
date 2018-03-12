package philip.com.airtablemap.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

/**
 * Made Gson as Singleton, because I think Gson is a huge class.
 */

public class GsonUtil {
    private static final GsonUtil instance = new GsonUtil();
    private final Gson gson;

    public static Gson getInstance() {
        return instance.gson;
    }

    private GsonUtil() {
        gson = new Gson();
    }

    public static Map<String, String> fromJsonHashMap(String json) {
        Map<String, String> result = getInstance().fromJson(json, new TypeToken<HashMap<String, String>>(){}.getType());

        return (result == null) ? new HashMap<String, String>() : result;
    }

    public static String toJson(Object json) {
        return getInstance().toJson(json);
    }
}
