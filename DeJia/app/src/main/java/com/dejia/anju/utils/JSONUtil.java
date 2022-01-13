package com.dejia.anju.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.hjq.gson.factory.GsonFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 将json串转换面对象列表
 *
 * @author MoringSun
 */
public class JSONUtil {

    /**
     * Map转换成JSON字符?build JSON String
     *
     * @param parameters
     * @return JSONObjectString
     * @throws JSONException
     */
    public static String toJSONString(Map<String, Object> parameters)
            throws Exception {
        JSONObject json = new JSONObject();

        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            json.put(entry.getKey(), entry.getValue());
        }
        return json.toString();
    }

    public static String toJSONString2(Map<String, JSONObject> parameters)
            throws Exception {
        JSONObject json = new JSONObject();

        for (Map.Entry<String, JSONObject> entry : parameters.entrySet()) {
            json.put(entry.getKey(), entry.getValue());
        }
        return json.toString();
    }

    /**
     * 解析只有一个键值对的json串
     *
     * @param jsonStr
     */
    public static String resolveJson(String jsonStr, String type) {
        String result = "";
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            result = jsonObject.getString(type);
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析没有数组的json字符串成javaBean
     *
     * @param jsonStr
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T TransformSingleBean(String jsonStr, Class<T> clazz) {
        Gson gson = GsonFactory.newGsonBuilder()
//                new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        return gson.fromJson(jsonStr, clazz);
    }

    public static <T> T TransformSingleBean(JsonReader jsonStr, Class<T> clazz) {
        return GsonFactory.getSingletonGson().fromJson(jsonStr, clazz);
    }

    /**
     * String转map
     *
     * @param str
     * @return
     */
    public static Map<String, Object> getStringToMap(String str) {
        //根据逗号截取字符串数组
        String[] str1 = str.split(",");
        //创建Map对象
        Map<String, Object> map = new HashMap<>();
        //循环加入map集合
        for (int i = 0; i < str1.length; i++) {
            //根据":"截取字符串数组
            String[] str2 = str1[i].split(":");
            //str2[0]为KEY,str2[1]为值
            map.put(str2[0], str2[1]);
        }
        return map;
    }


    /**
     * 解析有数组的json字符串成javabean
     *
     * @param jsonStr
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> ArrayList<T> jsonToArrayList(JsonReader jsonStr, Class<T> clazz) {
        Type type = new TypeToken<ArrayList<JsonObject>>() {
        }.getType();
        ArrayList<JsonObject> jsonObjects = GsonFactory.getSingletonGson().fromJson(jsonStr, type);

        ArrayList<T> arrayList = new ArrayList<>();
        for (JsonObject jsonObject : jsonObjects) {
            arrayList.add(GsonFactory.getSingletonGson().fromJson(jsonObject, clazz));
        }
        return arrayList;
    }

    /**
     * 解析有数组的json字符串成javabean
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> ArrayList<T> jsonToArrayList(String json, Class<T> clazz) {
        Type type = new TypeToken<ArrayList<JsonObject>>() {
        }.getType();
        Gson gson = GsonFactory.getSingletonGson();
        ArrayList<JsonObject> jsonObjects = gson.fromJson(json, type);

        ArrayList<T> arrayList = new ArrayList<>();
        if (jsonObjects != null) {
            for (JsonObject jsonObject : jsonObjects) {
                arrayList.add(gson.fromJson(jsonObject, clazz));
            }
        }
        return arrayList;
    }


    /**
     * 将map集合转为json格式
     *
     * @param obj
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static String mapTojson(Object obj) throws IllegalArgumentException, IllegalAccessException {

        StringBuffer buffer = new StringBuffer();
        Class<? extends Object> clazz = obj.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        Field.setAccessible(declaredFields, true);
        buffer.append("[");
        Map<Object, Object> map = (Map<Object, Object>) obj;

        //通过Map.entrySet使用iterator(迭代器)遍历key和value
        Set<Map.Entry<Object, Object>> set = map.entrySet();
        Iterator iterator = set.iterator();
        buffer.append("{");

        while (iterator.hasNext()) {

            //使用Map.Entry接到通过迭代器循环出的set的值
            Map.Entry mapentry = (Map.Entry) iterator.next();
            Object value = mapentry.getValue();

            //使用getKey()获取map的键，getValue()获取键对应的值
            String valuename = value.getClass().getSimpleName();
            if (valuename.equals("String")) {

                buffer.append("\"" + mapentry.getKey() + "\":\"" + mapentry.getValue() + "\",");
            } else if (valuename.equals("Boolean") || valuename.equals("Integer") || valuename.equals("Double") || valuename.equals("Float") || valuename.equals("Long")) {

                buffer.append("\"" + mapentry.getKey() + "\":" + mapentry.getValue() + ",");
            } else if (valuename.equals("Date")) {
                Date date = (Date) value;
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String simdate = simpleDateFormat.format(date);
                buffer.append("\"" + mapentry.getKey() + "\":\"" + simdate + "\",");
            } else if (valuename.equals("ArrayList") || valuename.equals("LinkedList")) {
                List<Object> list = (List<Object>) value;
                buffer.append("\"" + mapentry.getKey() + "\":[");
                buffer = listTojson(buffer, list).append("]");
            } else if (valuename.equals("HashSet") || valuename.equals("TreeSet")) {
                buffer.append("\"" + mapentry.getKey() + "\":[");
                Set<Object> sets = (Set<Object>) value;
                buffer = setTojson(sets, buffer).append("]");
            } else if (valuename.equals("HashMap") || valuename.equals("HashTable")) {
                buffer.append("\"" + mapentry.getKey() + "\":");
                StringBuffer mapbuffer = new StringBuffer(mapTojson(value));
                mapbuffer.deleteCharAt(0);
                buffer.append(mapbuffer);
            } else {
                buffer.append("\"" + mapentry.getKey() + "\":");
                buffer.append("{");

                Class<? extends Object> class1 = value.getClass();
                Field[] fields = class1.getDeclaredFields();
                Field.setAccessible(fields, true);

                for (Field field : fields) {

                    Object object = field.get(value);
                    String fieldName = field.getType().getSimpleName();

                    if (object == null) {
                        if (fieldName.equals("String")) {
                            buffer.append("\"" + field.getName() + "\":\"\",");
                        } else {
                            buffer.append("\"" + field.getName() + "\":null,");
                        }

                    } else {

                        Class<? extends Object> fieldclass = field.get(value).getClass();
                        String simpleName = fieldclass.getSimpleName();
                        if (simpleName.equals("String")) {

                            buffer.append("\"" + field.getName() + "\":\"" + field.get(value) + "\",");
                        } else if (simpleName.equals("Boolean") || simpleName.equals("Integer") || simpleName.equals("Double") || simpleName.equals("Float") || simpleName.equals("Long")) {

                            buffer.append("\"" + field.getName() + "\":" + field.get(value) + ",");
                        } else if (simpleName.equals("Date")) {
                            Date date = (Date) object;
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            String simdate = simpleDateFormat.format(date);
                            buffer.append("\"" + field.getName() + "\":\"" + simdate + "\",");
                        } else if (simpleName.equals("ArrayList") || simpleName.equals("LinkedList")) {

                            List<Object> list = (List<Object>) object;
                            buffer.append("\"" + field.getName() + "\":[");
                            StringBuffer append = listTojson(buffer, list).append("]");
                            buffer.append(append);
                        } else if (simpleName.equals("HashSet") || simpleName.equals("TreeSet")) {

                            buffer.append("\"" + field.getName() + "\":[");
                            Set<Object> sets = (Set<Object>) object;
                            buffer = setTojson(sets, buffer).append("]");
                        } else if (simpleName.equals("HashMap") || simpleName.equals("HashTable")) {

                            buffer.append("\"" + field.getName() + "\":");
                            StringBuffer mapbuffer = new StringBuffer(mapTojson(object));
                            mapbuffer.deleteCharAt(0);
                            buffer.append(mapbuffer);
                        } else {
                            buffer = beanTojson(object, buffer).append(",");

                        }
                    }

                }

                buffer = new StringBuffer("" + buffer.substring(0, buffer.length() - 1) + "");
                buffer.append("},");
            }

        }

        buffer = new StringBuffer("" + buffer.substring(0, buffer.length() - 1) + "");
        return buffer.toString() + "}]";
    }


    /**
     * 将不是基本类型的字段转为json格式
     *
     * @param obj
     * @param buffer
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static StringBuffer beanTojson(Object obj, StringBuffer buffer) throws IllegalArgumentException, IllegalAccessException {

        Class<? extends Object> clazz = obj.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        Field.setAccessible(declaredFields, true);

        buffer.append("\"" + clazz.getSimpleName() + "\":{");

        for (Field field : declaredFields) {

            Object object = field.get(obj);
            String fieldName = field.getType().getSimpleName();

            if (object == null) {
                if (fieldName.equals("String")) {
                    buffer.append("\"" + field.getName() + "\":\"\",");
                } else {
                    buffer.append("\"" + field.getName() + "\":null,");
                }

            } else {

                Class<? extends Object> fieldclass = object.getClass();
                String simpleName = fieldclass.getSimpleName();

                if (simpleName.equals("String")) {

                    buffer.append("\"" + field.getName() + "\":\"" + field.get(obj) + "\",");
                } else if (simpleName.equals("Boolean") || simpleName.equals("Integer") || simpleName.equals("Double") || simpleName.equals("Float") || simpleName.equals("Long")) {

                    buffer.append("\"" + field.getName() + "\":" + field.get(obj) + ",");
                } else if (simpleName.equals("Date")) {

                    Date date = (Date) object;
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    String simdate = simpleDateFormat.format(date);
                    buffer.append("\"" + field.getName() + "\":\"" + simdate + "\",");
                } else if (simpleName.equals("ArrayList") || simpleName.equals("LinkedList")) {

                    List<Object> list = (List<Object>) object;
                    buffer = listTojson(buffer, list);
                } else if (simpleName.equals("HashSet") || simpleName.equals("TreeSet")) {

                    Set<Object> set = (Set<Object>) object;
                    buffer = setTojson(set, buffer);
                } else if (simpleName.equals("HashMap") || simpleName.equals("HashTable")) {

                    buffer.append("\"" + field.getName() + "\":");
                    StringBuffer mapbuffer = new StringBuffer(mapTojson(object));
                    mapbuffer.deleteCharAt(0);
                    buffer.append(mapbuffer);
                } else {
                    buffer = beanTojson(object, buffer).append("}");
                }
            }

        }

        buffer = new StringBuffer("" + buffer.substring(0, buffer.length() - 1) + "");
        buffer.append("}");

        return buffer;
    }


    /**
     * 将list数组转为json格式
     *
     * @param buffer
     * @param list
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static StringBuffer listTojson(StringBuffer buffer, List list) throws IllegalArgumentException, IllegalAccessException {

        //遍历传过来的list数组
        for (Object object : list) {

            //判断遍历出的值是否为空
            if (object == null) {
                buffer.append(",");
            } else {

                Class<? extends Object> class1 = object.getClass();
                String simpleName = class1.getSimpleName();

                if (simpleName.equals("String")) {

                    buffer.append("\"" + object.toString() + "\",");
                } else if (simpleName.equals("Boolean") || simpleName.equals("Integer") || simpleName.equals("Double") || simpleName.equals("Float") || simpleName.equals("Long")) {

                    buffer.append("" + object.toString() + ",");
                } else if (simpleName.equals("Date")) {
                    Date date = (Date) object;
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    String simdate = simpleDateFormat.format(date);
                    buffer.append("" + simdate + ",");
                } else {

                    Class<? extends Object> class2 = object.getClass();
                    Field[] fields = class2.getDeclaredFields();
                    Field.setAccessible(fields, true);
                    buffer.append("{");
                    //遍历对象中的所有字段获取字段值和字段名称拼成json字符串
                    for (Field field : fields) {

                        Object fieldobj = field.get(object);
                        String fieldName = field.getType().getSimpleName();

                        if (fieldobj == null) {

                            if (fieldName.equals("String")) {
                                buffer.append("\"" + field.getName() + "\":\"\",");
                            } else {
                                buffer.append("\"" + field.getName() + "\":null,");
                            }

                        } else {

                            String fsimpleName = fieldobj.getClass().getSimpleName();

                            if (fsimpleName.equals("String")) {

                                buffer.append("\"" + field.getName() + "\":\"" + field.get(object) + "\",");
                            } else if (fsimpleName.equals("Boolean") || fsimpleName.equals("Integer") || fsimpleName.equals("Double") || fsimpleName.equals("Float") || fsimpleName.equals("Long")) {

                                buffer.append("\"" + field.getName() + "\":" + field.get(object) + ",");
                            } else if (fsimpleName.equals("Date")) {

                                Date date = (Date) object;
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                String simdate = simpleDateFormat.format(date);
                                buffer.append("\"" + field.getName() + "\":" + simdate + ",");
                            } else {

                                buffer = beanTojson(fieldobj, buffer).append(",");
                            }
                        }

                    }

                    buffer = new StringBuffer("" + buffer.substring(0, buffer.length() - 1) + "");
                    buffer.append("},");
                }
            }

        }

        buffer = new StringBuffer("" + buffer.substring(0, buffer.length() - 1) + "");
        buffer.append("]");

        return buffer;
    }


    /**
     * 将set数组转为json格式
     *
     * @param set
     * @param buffer
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static StringBuffer setTojson(Set set, StringBuffer buffer) throws IllegalArgumentException, IllegalAccessException {

        for (Object object : set) {
            if (object == null) {
                buffer.append("" + "null" + ",");
            } else {

                Class<? extends Object> class1 = object.getClass();

                //判断集合中的值是否为java基本类型
                String simpleName = class1.getSimpleName();
                if (simpleName.equals("String")) {

                    buffer.append("\"" + object.toString() + "\",");
                } else if (simpleName.equals("Boolean") || simpleName.equals("Integer") || simpleName.equals("Double") || simpleName.equals("Float") || simpleName.equals("Long")) {

                    buffer.append("" + object.toString() + ",");
                } else if (simpleName.equals("Date")) {

                    Date date = (Date) object;
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    String simdate = simpleDateFormat.format(date);
                    buffer.append("" + simdate + ",");
                } else {

                    Class<? extends Object> class2 = object.getClass();
                    Field[] fields = class2.getDeclaredFields();
                    Field.setAccessible(fields, true);
                    buffer.append("{");

                    //遍历对象中的所有字段获取字段值和字段名称拼成json字符串
                    for (Field field : fields) {

                        Object fieldobj = field.get(object);
                        String fieldName = field.getType().getSimpleName();

                        if (object == null) {
                            if (fieldName.equals("String")) {
                                buffer.append("\"" + field.getName() + "\":\"\",");
                            } else {
                                buffer.append("\"" + field.getName() + "\":null,");
                            }

                        } else {

                            String fsimpleName = fieldobj.getClass().getSimpleName();
                            if (fsimpleName.equals("String")) {

                                buffer.append("\"" + field.getName() + "\":\"" + field.get(object) + "\",");
                            } else if (fsimpleName.equals("Boolean") || fsimpleName.equals("Integer") || fsimpleName.equals("Double") || fsimpleName.equals("Float") || fsimpleName.equals("Long")) {

                                buffer.append("\"" + field.getName() + "\":" + field.get(object) + ",");
                            } else if (fsimpleName.equals("Date")) {

                                Date date = (Date) object;
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                String simdate = simpleDateFormat.format(date);
                                buffer.append("\"" + field.getName() + "\":" + simdate + ",");
                            } else {

                                buffer = beanTojson(fieldobj, buffer).append(",");
                            }
                        }
                    }

                    buffer = new StringBuffer("" + buffer.substring(0, buffer.length() - 1) + "");
                    buffer.append("},");
                }
            }
        }

        buffer = new StringBuffer("" + buffer.substring(0, buffer.length() - 1) + "");
        return buffer;
    }

    /**
     * Json 转成 Map<>
     *
     * @param jsonStr
     * @return
     */
    public static Map<String, Object> getMapForJson(String jsonStr) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonStr);

            Iterator<String> keyIter = jsonObject.keys();
            String key;
            Object value;
            Map<String, Object> valueMap = new HashMap<String, Object>();
            while (keyIter.hasNext()) {
                key = keyIter.next();
                value = jsonObject.get(key);
                valueMap.put(key, value);
            }
            return valueMap;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }


}
