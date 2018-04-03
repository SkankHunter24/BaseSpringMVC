package utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;



/**
 * 处理webhttp请求
 *
 */
public class WebAccessTools {
	
	
	    /** 
	     * 根据给定的url地址访问网络，得到响应内容(这里为GET方式访问) 
	     * @param url 指定的url地址 
	     * @return web服务器响应的内容，为<code>String</code>类型，当访问失败时，返回为null 
	     */  
	    public static String getWebContent(String url) 
	    {  
	        //创建一个http请求对象   
	    	System.out.println(url);
	        HttpGet request = new HttpGet(url);  
	        //创建HttpParams以用来设置HTTP参数   
	        HttpParams params=new BasicHttpParams();  
	        //设置连接超时或响应超时   
	        HttpConnectionParams.setConnectionTimeout(params, 3000);  
	        HttpConnectionParams.setSoTimeout(params, 5000);  
	        //创建一个网络访问处理对象   
	        HttpClient httpClient = new DefaultHttpClient(params);  
	        try{  
	            //执行请求参数项   
	            HttpResponse response = httpClient.execute(request);  
	            //判断是否请求成功   
	            int status = response.getStatusLine().getStatusCode();
	            if(status == HttpStatus.SC_OK) {  
	                //获得响应信息   
	                String content = EntityUtils.toString(response.getEntity());  
	                
	                return content;  
	            } else {  
	                //网连接失败，使用Toast显示提示信息   
//	                Toast.makeText(context, "网络访问失败，请检查您机器的联网设备!", Toast.LENGTH_LONG).show();  
	            }  
	              
	        }catch(Exception e) {  
	        	LogManager.getLogger("WebAccessTools").error(e.getMessage());
	            e.printStackTrace(); 
	        } finally {  
	            //释放网络连接资源   
	            httpClient.getConnectionManager().shutdown();  
	        }  
	        return null;  
	    }  
	    
	    
	    /**
	     * @param url 请求地址
	     * @param entity 消息体
	     * @param headers 消息头
	     * @return
	     */
	    public static String postWebContent(String url,StringEntity entity,Header[] headers)
	    {  
	        //创建一个http请求对象   
	    	System.out.println(url);
	        HttpPost request = new HttpPost(url);  
	        //创建HttpParams以用来设置HTTP参数   
	        HttpParams params=new BasicHttpParams();  
	        //设置连接超时或响应超时   
	        HttpConnectionParams.setConnectionTimeout(params, 3000);  
	        HttpConnectionParams.setSoTimeout(params, 5000);  
	        //创建一个网络访问处理对象   
	        HttpClient httpClient = new DefaultHttpClient(params);  
	        try{  
	        	

	            request.setEntity(entity);
	            request.setHeaders(headers);
	            //执行请求参数项   
	            HttpResponse response = httpClient.execute(request);  
	            //判断是否请求成功   
	            int status = response.getStatusLine().getStatusCode();
	            if(status == HttpStatus.SC_OK) {  
	                //获得响应信息   
	                String content = EntityUtils.toString(response.getEntity());  
	                
	                return content;  
	            } else {  
	                //网连接失败，使用Toast显示提示信息   
//	                Toast.makeText(context, "网络访问失败，请检查您机器的联网设备!", Toast.LENGTH_LONG).show();  
	            }  
	              
	        }catch(Exception e) {  
	        	LogManager.getLogger("WebAccessTools").error(e.getMessage());
	            e.printStackTrace(); 
	        } finally {  
	            //释放网络连接资源   
	            httpClient.getConnectionManager().shutdown();  
	        }  
	        return null;  
	    }
	    
	    
	    /**soap调用
	     * @param urlAddress 方法地址
	     * @param paraNames  参数名数组
	     * @param pataValues 值数组
	     * @param soapMethod 方法名
	     * @param encoding 编码
	     * @param contentType 
	     * @return
	     */
	    public static String postSoapRequest(String urlAddress,String[] paraNames,String[] pataValues[],String soapMethod,String encoding,String contentType)
	    {
	   		 int code = 0;
	   		 try { 

	   		        URL url = new URL(urlAddress); 
	   		        
	   		        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	   		        
	   		        String data = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"+
	   		        			"<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"+
	   		        			"<soap:Body>"+
	   		        			"<"+soapMethod+" xmlns=\"http://tempuri.org/\">";
	   		        for(int i =0;i<paraNames.length;i++)
	   		        {
	   		        	data = data + "<"+paraNames[i]+">"+pataValues[i]+"</"+paraNames[i]+">";
	   		        }
	   		        data = data + "</"+soapMethod+">"+
	   		        			  "</soap:Body>"+
	   		        			  "</soap:Envelope>";
	   		        
	   		        String soapActionString = "http://tempuri.org/" + soapMethod;

	   		        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();

	   		        httpConn.setRequestProperty("Content-Length",String.valueOf( data.getBytes(encoding).length));
	   		        httpConn.setRequestProperty("Content-Type", contentType);
	   		        httpConn.setRequestProperty("soapActionString", soapActionString);
	   		        httpConn.setRequestMethod("POST");
	   		        httpConn.setDoOutput(true);
	   		        httpConn.setDoInput(true);
	   		        httpConn.setReadTimeout(5000);
	   		        OutputStream out = httpConn.getOutputStream();
	   		        out.write(data.getBytes()); 
	   		        code = httpConn.getResponseCode();
	   		        out.close();
	   		        if (code == 200) {  
	   		    	 // 取回响应的结果  
	                    return changeInputStream(httpConn.getInputStream(),encoding);  
	                }  
	   		 }
	   		catch(Exception e) {  
	   			LogManager.getLogger("WebAccessTools").error(e.getMessage());
	            e.printStackTrace(); 
	        } 
	        return null;  
	    }
	    
	    
	    /**  
	     * 将一个输入流转换成指定编码的字符串  
	     *   
	     * @param inputStream  
	     * @param encode  
	     * @return  
	     */  
	    private static String changeInputStream(InputStream inputStream,String encoding) throws Exception 
	    {  
	        // 内存流  
	        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();  
	        byte[] data = new byte[1024];  
	        int len = 0;  
	        String result = null;  
	        if (inputStream != null) {  
	            try {  
	                while ((len = inputStream.read(data)) != -1) {  
	                    byteArrayOutputStream.write(data, 0, len);  
	                }  
	                result = new String(byteArrayOutputStream.toByteArray(), encoding);  
	            } catch (IOException e) {  
	                throw new Exception(e.getMessage());
	            }  
	        }  
	        return result;  
	    }  
}