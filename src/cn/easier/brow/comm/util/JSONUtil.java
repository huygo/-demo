package cn.easier.brow.comm.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * JSON操作
 * @author yanlu.zhang
 *
 */
public class JSONUtil {
    
    private static final Logger LOG = LoggerFactory.getLogger(JSONUtil.class);
	
	/**
	 * "[]" => List
	 * @param json
	 * @return
	 */
    @SuppressWarnings("unchecked")
    public static List<Object> parseToList(String json) {
        List<Object> list = jsonToObject(json, List.class);
        return list == null ? new ArrayList<Object>(0) : list;
    }
    
	/**
	 * "{}" => Map
	 * @param json
	 * @return
	 */
	@SuppressWarnings({"unchecked" })
	public static Map<String, Object> parseMap(String json){
		Map<String, Object> map = jsonToObject(json, Map.class);
		return map == null ? new HashMap<String, Object>(0) : map;
	}
	
	/**
	 * Object => "{}"/"[]"
	 * @param o
	 * @return
	 */
	public static String toJSONString(Object o){
		if (o == null) {
			return null;
		}
		try{
			return JSON.toJSONString(o);
		}catch(Exception e){
			LOG.error("", e);
			return null;
		}
	}
	
	public static String toJSONStringWithNullField(Object o) {
		if (o == null) {
			return null;
		}
		try{
			return JSON.toJSONString(o, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullBooleanAsFalse, SerializerFeature.WriteNullListAsEmpty,
					SerializerFeature.WriteNullNumberAsZero, SerializerFeature.WriteNullStringAsEmpty);
		}catch(Exception e){
			LOG.error("", e);
			return null;
		}
	}
	
	public static String toJSONString(Object o,String def){
		String s = toJSONString(o);
		return s == null ? def :s;
	}
	
	public static <T> T jsonToObject(String json, Class<T> clazz){
        if(StringUtils.isNotBlank(json)) {
            json = json.trim();
            if((json.startsWith("{") && json.endsWith("}")) || (json.startsWith("[") && json.endsWith("]"))) {
                try{
                    return JSON.parseObject(json, clazz);
                }catch(Exception e){
                    LOG.error(json, e);
                }   
            }
        }
	    return null;
    }
    
}
