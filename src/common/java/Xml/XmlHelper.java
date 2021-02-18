package common.java.Xml;

import common.java.nLogger.nLogger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Iterator;
import java.util.Properties;

public class XmlHelper {
	public final static JSONObject xml2json(String xmlstr){
		return xml2jsonex(xmlstr,new JSONObject());
	}
	/**xml转json
	 * @param xmldoc
	 * @return
	 */
	public final static JSONObject xml2json(Document xmldoc){
		return xml2jsonex(xmldoc,new JSONObject());
	}
	public final static JSONObject xml2jsonex(String xmldoc,JSONObject rs){
		JSONObject json;
		try {
			json = xml2jsonex(DocumentHelper.parseText(xmldoc),rs);
		} catch (DocumentException e) {
			json = null;
		}
		return json;
	}
	/**把xmldoc转换成JSON后添加到rs里
	 * @param xmldoc
	 * @param rs
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public final static JSONObject xml2jsonex(Document xmldoc,JSONObject rs){
		JSONObject robj;
		Element root = xmldoc.getRootElement();
		robj = chilrenxml2json(root);
		rs.putAll(robj);
		return rs;
	}
	public final static Document json2xml(String jsonStr){
        return json2xml(JSONObject.toJSON(jsonStr));
	}
	public final static Document json2xml(JSONObject json){
		Element element;
		Document document = DocumentHelper.createDocument();
		if( json != null){
			for( Object _obj : json.keySet() ){
				element = DocumentHelper.createElement((String)_obj);
				element.add(DocumentHelper.createCDATA((String)json.get(_obj)));
				document.add(element);
			}
		}
		return document;
	}
	public final static Element appendjson2xml(Element fatherNode,JSONObject json){
		String key;
		Object _item;
		Element contentNode;
		for( Object _obj : json.keySet()){
			key = _obj.toString();
			contentNode = DocumentHelper.createElement( key );//创建
			_item = json.get(key);
			if( _item instanceof JSONObject ){//内容是JSON
				contentNode = appendjson2xml(contentNode,(JSONObject)_item);
			}
			else if(_item instanceof JSONArray){
				JSONArray ary = (JSONArray)_item;
				for( Object _json : ary ){
					contentNode = appendjson2xml(contentNode,(JSONObject)_json);
				}
			}
			else {
				contentNode.add( DocumentHelper.createCDATA( json.get(key).toString() ));
			}
			fatherNode.add(contentNode);
		}
		return fatherNode;
	}
	@SuppressWarnings("unchecked")
	public final static JSONObject chilrenxml2json(Element fatherNode){
		String key;
		Element element;
		Object tmp;
		Object array;
		JSONObject rs = new JSONObject();//生成的结果只有最后一个节点
		JSONObject tmpRS;
		for ( Iterator<Element> i = fatherNode.elementIterator(); i.hasNext(); ) {
			element = i.next();
			tmpRS = chilrenxml2json(element);
			//遇到名字相同的，生成jsonarray;
			key = element.getName();
			if( tmpRS.containsKey(key) ){
				tmp = tmpRS.get(key);
				array = ( tmp instanceof JSONArray ) ? ((JSONArray)tmp) : new JSONArray();
				((JSONArray)array).add(element.getData());
			}
			else{
				array = element.getData();
			}
			rs.put(element.getName(), array);
		}
		return rs;
	}
	public final static Element newElementCDATA(String nodeName,String nodeValue){
		Element node = DocumentHelper.createElement(nodeName);
		node.add(DocumentHelper.createCDATA(nodeValue));
		return node;
	}
	public final static Element newElementLong(String nodeName,Long nodeValue){
		Element node = DocumentHelper.createElement(nodeName);
		node.add(DocumentHelper.createText(String.valueOf(nodeValue) ));
		return node;
	}
	public final static Document string2xml(String xmlString){
		try {
			return DocumentHelper.parseText(xmlString);
		} catch (DocumentException e) {
			nLogger.logInfo(e);
			return null;
		}
	}
	public final static String xml2string(Document xmldoc){
		return xmldoc.asXML();
	}
	public final static Element newElement(String nodeName){
		return DocumentHelper.createElement(nodeName);
	}
	
	public final static Properties json2Properties(JSONObject json){
		Object obj2;
		String key;
		Properties pro = new Properties();
		for(Object obj : json.keySet()){
			key = (String)obj;
			obj2 =  json.get( key );
			if( obj2 instanceof Long ){
				obj2 = ((Long)obj2).intValue();
			}
			pro.put(key, obj2) ;
		}
		return pro.size() > 0 ? pro : null;
	}
}
