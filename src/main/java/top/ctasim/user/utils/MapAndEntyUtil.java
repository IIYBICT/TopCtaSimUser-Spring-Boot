package top.ctasim.user.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
public class MapAndEntyUtil {

    public static final char UNDERLINE = '_';


    /**
     * @param obj
     * @return
     */
    public static Map<String, Object> entityToMap(Object obj) {
        Map<String, Object> map = new HashMap<>();
        if (obj == null) {
            return map;
        }
        Class clasz = obj.getClass();
        Field[] fields = clasz.getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                map.put(field.getName(), field.get(obj));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.remove("serialVersionUID");
        return map;
    }

    /**
     * @param map
     * @param clasz
     * @return
     */
    public static Object mapToEntity(Map<String, Object> map, Class<?> clasz) {
        if (map == null) {
            return null;
        }
        Object obj = null;
        try {
            obj = clasz.newInstance();

            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }
                field.setAccessible(true);
                field.set(obj, map.get(field.getName()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * @param o
     * @return
     */
    public static String underlineToCamel(Object o) {
        String param = (o == null) ? "" : o.toString();
        if ("".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (c == UNDERLINE) {
                if (++i < len) {
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * @param list
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> getObjectList(List<Map<String, Object>> list, Class<?> clazz) {
        if (list != null && list.size() > 0) {
            List<T> listResult = new ArrayList<T>();
            for (int i = 0; i < list.size(); i++) {
                //1.如果map里的key是数据库的字段，去掉"_"
                String mapStr = underlineToCamel(list.get(i));
                //2.将mapStr重新转换为map形式（Map<String, Object>）
                Map<String, Object> map = getMapByStr2(mapStr);
                System.out.println("map===" + map);
                //3.将map转换为实体,放入list
                listResult.add((T) mapToEntity(map, clazz));
            }
            return listResult;
        } else {
            return null;
        }
    }

    /**
     * @param mapStr
     * @return
     */
    public static Map<String, Object> getMapByStr(String mapStr) {
        Map<String, Object> map = new HashMap<>();
        if (mapStr != null) {
            String str = "";
            String key = "";
            Object value = "";

            char[] charList = mapStr.toCharArray();
            boolean valueBegin = false;
            for (int i = 0; i < charList.length; i++) {
                char c = charList[i];
                if (c == '{') {
                    if (valueBegin == true) {
                        value = getMapByStr(mapStr.substring(i, mapStr.length()));
                        i = mapStr.indexOf('}', i) + 1;
                        map.put(key, value);
                    }
                } else if (c == '=') {
                    valueBegin = true;
                    key = str;
                    str = "";
                } else if (c == ',') {
                    valueBegin = false;
                    value = str;
                    str = "";
                    map.put(key, value);
                } else if (c == '}') {
                    if (str != "") {
                        value = str;
                    }
                    map.put(key, value);
                    return map;
                } else if (c != ' ') {
                    str += c;
                }
            }
            return map;
        } else {
            return map;
        }
    }

    /**
     * @param mapStr
     * @return
     */
    public static Map<String, Object> getMapByStr2(String mapStr) {

        Map<String, Object> resultMap = new HashMap<String, Object>();

        mapStr = (mapStr == null) ? "" : mapStr;
        if ("".equals(mapStr.trim())) {
            return resultMap;
        } else {
            mapStr = mapStr.replace("{", "").replace("}", "");
            String[] resultMapStr = mapStr.split(",");
            for (String rs : resultMapStr) {
                //System.out.println("rs:" + rs);
                String[] kv = rs.split("=");
                resultMap.put(kv[0], kv[1]);
            }
            return resultMap;
        }
    }

}
