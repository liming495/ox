package com.ox;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by
 * User: LiMing
 * Date: 2015/8/11
 */
public class JsonUtil {
    private static ObjectMapper mapper = new ObjectMapper();
    static{
        //创建只输出非空属性到Json字符串的Binder.
        mapper.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        mapper.getDeserializationConfig().set(org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper.getSerializationConfig().setDateFormat(df);
        mapper.getDeserializationConfig().setDateFormat(df);
    }
    /**
     * 将对象转换成Json格式的字符串
     * @param obj
     * @throws Exception
     */
    public static String bean2Str(Object obj){
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static <T> T str2Bean(final String message,Class<T> cls){
        T object = null ;
        try{
            object = mapper.readValue(message, cls);
        }catch (JsonParseException e){
            e.printStackTrace();
        }catch (JsonMappingException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return object;
    }
    public static String getNodeValue(String message,String nodeKey){
        try {
            return mapper.readTree(message).get(nodeKey).getTextValue();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }// 这里的JsonNode和XML里面的Node很像
        return null;
    }
}
