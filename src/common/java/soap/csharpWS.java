package common.java.soap;

import common.java.string.StringHelper;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class csharpWS {
	private String url;
	private String nameSpace;
	private Service service;
	private JSONArray paramArray;
	private static HashMap<String, Call> callCache;
	static {
		callCache = new HashMap<>();
	}
	public csharpWS(String url ,String nameSpace) {
		this.url = url;
		this.nameSpace = nameSpace;
		service =  new Service();
		reset();
	}
	private void reset() {
		paramArray = new JSONArray();
	}
	@SuppressWarnings("unchecked")
	public csharpWS addParameter(String name,QName type,ParameterMode mode,Object val) {
		paramArray.add((new JSONObject("name",name)).puts("type", type).puts("mode", mode).puts("value", val));
		return this;
	}
	public JSONArray buildParameter() {
		JSONArray rArray = paramArray;
		reset();
		return rArray;
	}
	public String remoteCall(String action) {
		JSONArray param =  null;
		if( paramArray.size() >  0 ) {
			param = paramArray;
		}
		String rs = remoteCall(action,param);
		reset();
		return rs;
	}
	public String remoteCall(String action,JSONArray paramJSON) {
		Call call;
		String result = "";
		try {
			call = callCache.get(nameSpace + "@" + action);
			if( call == null ) {
				call = (Call) service.createCall();
				/*
				 * call.setUsername("your username"); // 用户名（如果需要验证）  
				 * call.setPassword("your password"); // 密码  
				 * */
				call.setTargetEndpointAddress(new URL(url));
				call.setUseSOAPAction(true);  
				call.setSOAPActionURI( StringHelper.build(nameSpace).trimFrom('/').toString() + "/" + action); // action uri
				call.setOperationName(new QName(nameSpace, action));// 设置要调用哪个方法
				JSONObject json;
				//List<Object> paramsList = new ArrayList<Object>();
				for(Object paramObj : paramJSON) {
					json = (JSONObject)paramObj;
					call.addParameter(
							new QName(nameSpace, json.getString("name")),
							(QName)json.get("type"),
							(ParameterMode)json.get("mode")
							);
					//paramsList.add( json.get("value") );
				}
				call.setReturnType(XMLType.XSD_STRING);//要返回的数据类型
				callCache.put(nameSpace + "@" + action, call);
			}
			JSONObject json;
			List<Object> paramsList = new ArrayList<Object>();
			for(Object paramObj : paramJSON) {
				json = (JSONObject)paramObj;
				paramsList.add( json.get("value") );
			}
			Object[] params = new Object[paramsList.size()];
			paramsList.toArray(params);
			result = (String)call.invoke(params);
		} catch (ServiceException | MalformedURLException | RemoteException e1) {
			// TODO Auto-generated catch block
			result = null;
		}
		return result;
	}
}
