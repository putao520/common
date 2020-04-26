package common.java.soap;

import common.java.nlogger.nlogger;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;
import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;

public class soapWS {  
	private String wsdlUrl;
	private Service service;
	private static ConcurrentHashMap<String,Call> callCache;
	static {
		callCache = new ConcurrentHashMap<>();
	}
	public soapWS(String wsdlURL){
		wsdlUrl = wsdlURL; 
		service = new Service();
	}
	
	private QName getType(Object value){
		//String fix = "xsi:";
		QName rQName = null;
		if( value instanceof Integer ){
			rQName = XMLType.XSD_INTEGER;
		}
		else if( value instanceof Long ){
			rQName = XMLType.XSD_LONG;
		}
		else if( value instanceof Boolean ){
			rQName = XMLType.XSD_BOOLEAN;
		}
		else if( value instanceof Double ){
			rQName = XMLType.XSD_DOUBLE;
		}
		else if( value instanceof Float ){
			rQName = XMLType.XSD_FLOAT;
		}
		else if( value instanceof QName) {
			rQName = (QName)value;
		}
		else {
			rQName = XMLType.XSD_STRING;
		}
		return rQName;
	}
	public soapWS addCall(String fName,JSONArray params) throws ServiceException{
		Call call = (Call) service.createCall();
		call.setTargetEndpointAddress(wsdlUrl);
		call.setOperationName(fName);
		JSONObject json;
        for(Object obj : params){
        	json = (JSONObject)obj;
        	for(Object key : json.keySet()) {
        		call.addParameter( (String)key , getType( json.get(key) ), ParameterMode.IN);
        	}
        }
        call.setReturnType(XMLType.XSD_STRING);  
        callCache.put(fName, call);
        return this;
	}
	public Object call(String fName,Object[] params) {
		Object ro = null;
		Call _call =  callCache.get(fName);
		try {
			ro = _call.invoke(params);
		} catch (RemoteException e) {
			nlogger.logInfo(e);
		}
		return ro;
	}
	/*
	public Document call(String fName,String rTagName,JSONObject params) throws SOAPException{
		SOAPMessage msg = MessageFactory.newInstance().createMessage();  
        SOAPEnvelope envelope = msg.getSOAPPart().getEnvelope();
        
        SOAPBody body = envelope.getBody();  
        //4、创建QName来指定消息中传递数据  
        //QName ename2 = new QName(nsUrl,fName,"xsi");//<nn:add xmlns="xx"/>  
        Name ename = envelope.createName(fName,"xsi",nsUrl);
        SOAPBodyElement ele = body.addBodyElement(ename);
        ele.setEncodingStyle(SOAPConstants.URI_NS_SOAP_ENCODING);
        //传递参数     
        //XMLType.XSD_STRING
        SOAPElement eles;
        Object value;
        for(Object key : params.keySet()){
        	eles = ele.addChildElement(
        			envelope.createName( (String)key )
        			);
        	value = params.get(key);
        	eles.setValue(value.toString());
        	eles = setType("xsi",eles,value);
        }

        SOAPMessage response = dispatch.invoke(msg);
        org.w3c.dom.Document doc = response.getSOAPPart().getEnvelope().getBody().getOwnerDocument();
        String str = doc.getElementsByTagName(rTagName).item(0).getNodeValue();
        return XmlHelper.string2xml(str);
	}
	
    public static void test() throws Exception {  
        String ns = "http://webservice.coolwei.com/axis/ProxyRemoteSpellCheck.jws";  
        String wsdlUrl = "http://webservice.coolwei.com/axis/ProxyRemoteSpellCheck.jws?wsdl";  
        //1、创建服务(Service)  
        URL url = new URL(wsdlUrl);  
        QName sname = new QName(ns,"ProxyRemoteSpellCheckService");  
        Service service = Service.create(url,sname);  
                      
        //2、创建Dispatch  
        Dispatch<SOAPMessage> dispatch = service.createDispatch(new QName(ns,"ProxyRemoteSpellCheck"),SOAPMessage.class,Service.Mode.MESSAGE);
       
        dispatch = service.createDispatch(new QName(ns,"ProxyRemoteSpellCheck"),SOAPMessage.class,Service.Mode.MESSAGE);
                      
        //3、创建SOAPMessage  
        SOAPMessage msg = MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL).createMessage();  
        SOAPEnvelope envelope = msg.getSOAPPart().getEnvelope();  
        SOAPBody body = envelope.getBody();  
                      
        //4、创建QName来指定消息中传递数据  
        QName ename = new QName(ns,"Check","axis");//<nn:add xmlns="xx"/>  
        SOAPBodyElement ele = body.addBodyElement(ename);
        //传递参数     
        ele.addChildElement("content").setValue("ptuao520,2温家宝和温家包是谁？");
        ele.addChildElement("userkey").setValue("377c9dc160bff6cfa3cc0cbc749bb11a");

        msg.writeTo(System.out);  
        System.out.println("\n invoking.....");  
                              
        //5、通过Dispatch传递消息,会返回响应消息  
        SOAPMessage response = dispatch.invoke(msg);
         
        ByteArrayOutputStream resp = new ByteArrayOutputStream();
        response.writeTo(resp);
        String repString = new String( resp.toByteArray() );
        System.out.println( repString );
        //soapenv:Body
        //6、响应消息处理,将响应的消息转换为dom对象  
        
        org.w3c.dom.Document doc = response.getSOAPPart().getEnvelope().getBody().extractContentAsDocument();  
        String str = doc.getElementsByTagName("CheckReturn").item(0).getTextContent();
        Document ndoc = XmlHelper.string2xml(str);
        System.out.println(ndoc.getRootElement().element("CONTENT").getStringValue()); 
        String oString = "test";
        System.out.println(oString);
  
    }  
	 */
}  