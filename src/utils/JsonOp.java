package utils;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import base.BaseUtilObject;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.xml.XMLSerializer;

/**
 * 常用JSON处理
 *
 */
public class JsonOp extends BaseUtilObject{
	
	
	/**将JSON数组转化为MAP集合
	 * @param jsonArray JSON数组
	 * @return
	 * @throws Exception
	 */
	public static  List<Map<String,Object>> getMapList(JSONArray jsonArray) throws Exception
	{
	    List<Map<String,Object>> mapListJson = (List)jsonArray;  
        for (int i = 0; i < mapListJson.size(); i++) {  
            Map<String,Object> obj=mapListJson.get(i);  
              
            for(Entry<String,Object> entry : obj.entrySet()){  
                String strkey1 = entry.getKey();  
                Object strval1 = entry.getValue();  
                System.out.println("KEY:"+strkey1+"  -->  Value:"+strval1+"\n");  
            }  
        }
        return mapListJson;
	}
	
	public static JSONArray xmlStr2JsonArr(String xmlStr) throws Exception
	{
		XMLSerializer xmlSerializer=new XMLSerializer();
		JSON json=xmlSerializer.read(xmlStr);
		String s = json.toString();
		s = s.substring(s.indexOf("["),s.length()-1);
		JSONArray jsonArray = JSONArray.fromObject(s);  
		return jsonArray;
	}
	
	
	/**将XML转化为JSON数组
	 * @param xmlStr
	 * @return
	 * @throws Exception
	 */
	public static JSONArray xmlStr2JsonArrTS(String xmlStr) throws Exception
	{
		
		XMLSerializer xmlSerializer=new XMLSerializer();
		xmlStr = xmlStr.substring(xmlStr.indexOf("<diffgr:diffgram xmlns:msdata=\"urn:schemas-microsoft-com:xml-msdata\" xmlns:diffgr=\"urn:schemas-microsoft-com:xml-diffgram-v1\">"),xmlStr.indexOf("</diffgr:diffgram>")+"</diffgr:diffgram>".length());
		JSON json=xmlSerializer.read(xmlStr);
		String s = json.toString();
		s = s.substring(s.indexOf("["),s.lastIndexOf("]")+1);
		JSONArray jsonArray = JSONArray.fromObject(s);  
		return jsonArray;
	}
	
	
	/**将XML转化为JSON字符串
	 * @param xmlStr 
	 * @return
	 * @throws Exception
	 */
	public static String xmlStr2JsonStr(String xmlStr) throws Exception
	{
		XMLSerializer xmlSerializer=new XMLSerializer();
		JSON json=xmlSerializer.read(xmlStr);
		return json.toString();
	}
}
