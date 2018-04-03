package utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;

import base.BaseUtilObject;

/**
 * 常用字符串及日期处理等等
 *	
 */
public class PubFun extends BaseUtilObject{
	
	
	
	
	/**
	 * 将以ken分隔的字符串每个元素加"
	 * @param s
	 * @param ken
	 * @return
	 */
	
   public static String strWhere(String s,String ken){
	    String r = "";
		if (s!=null){
		  List list = PubFun.strKenList(s,",");
		  for(int i=0;i<list.size();i++){
			r = r+"'"+list.get(i)+"',";
		  }
		}
		if (r.length()==0) r="null";
		return r;
   }
	
	/**
	 * 将两个size相等，分别以ken1、ken2为分隔符的字符串s1、s2合并转换为一个2维数组
	 * @param s1
	 * @param ken1
	 * @param s2
	 * @param ken2
	 * @return
	 * @throws Exception
	 */
   public static String getWeekOfDate(Date dt) {
       String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
       Calendar cal = Calendar.getInstance();
       cal.setTime(dt);
       int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
       if (w < 0)
           w = 0;
       return weekDays[w];
   }
	
	public static String[][] strToArray(String s1,String ken1,String s2,String ken2) throws Exception{
		String[][] sz= null;
		
		try {
			List list1 = PubFun.strKenList(s1,ken1);
			List list2 = PubFun.strKenList(s2,ken2);
			int m = list1.size();
			sz = new String[m][2];		
			for (int i=0;i<m;i++){
				
					sz[i][0] = (String)list1.get(i);
					sz[i][1] = (String)list2.get(i);
			}
			return sz;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			LogManager.getLogger("PubFun").error(e.getMessage());
			return sz;
		}	
	
	}
	
	
	
	
	
	
	
	
	
	/**
	 * 将long转换为Long，如果异常返回null
	 * @param x
	 * @return
	 */
	public static Long longToLong(long x){
		Long d = null;
		
		try {
			d = new Long(x);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			LogManager.getLogger("PubFun").error(e.getMessage());
		}
		
		return d;
	}
	
	/**
	 * 将String转换为Long，如果异常返回null
	 * @param s
	 * @return
	 */
	public static Long strToLong(String s){
		Long d = null;
		try {
			d = new Long(s);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			LogManager.getLogger("PubFun").error(e.getMessage());
		}
		return d;
	}
	/**
	 * 分割字符数据
	 * 字符结果例如：111@222,333@444,
	 * 
	 * @param s 字符
	 * @param f 第一分割符,
	 * @param l 第二分割符@
	 * @return
	 * @throws Exception
	 */
	public static Map<String,String> listByMap(String s,String f,String l) throws Exception{
		Map<String,String> sz= null;
		
		try {
			List list1 = PubFun.strKenList(s,f);
			int m = list1.size();
			sz =new  HashMap<String,String>();
			for (int i=0;i<m;i++){
				String sk = (String)list1.get(i);
				String [] list2= PubFun.strKen(sk, l);
				sz.put(list2[0], list2[1]);
			}
			return sz;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			LogManager.getLogger("PubFun").error(e.getMessage());
			return  null;
		}	
		/*##############################
		 for (int mm1=0;mm1<sz.length;mm1++){
		 for(int nn1=0;nn1<sz[mm1].length;nn1++){
		 System.out.println("sz["+mm1+"]["+nn1+"]"+sz[mm][nn1]);
		 }
		 }
		 ########################################*/
		
		
	}
	/**
	 *  将priv_value (cookie)  中的值转换为2维数组
	 * @param s cookie值
	 * @param n 有n列
	 * @return
	 */
	public static String[][] cookiePrivs(String s,int n) throws Exception{
		String[][] sz= null;
		
		try {
			List list1 = PubFun.strKenList(s,"@");
			int m = list1.size();
			sz = new String[m][n];		
			for (int i=0;i<m;i++){
				String sk = (String)list1.get(i);
				List list2 = PubFun.strKenList(sk,",");
				for(int k=0;k<list2.size();k++){
					sz[i][k] = (String)list2.get(k);
				}
			}
			return sz;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			LogManager.getLogger("PubFun").error(e.getMessage());
			return null;
		}	
		/*############################
		 for (int mm1=0;mm1<sz.length;mm1++){
		 for(int nn1=0;nn1<sz[mm1].length;nn1++){
		 System.out.println("sz["+mm1+"]["+nn1+"]"+sz[mm][nn1]);
		 }
		 }
		 ########################################*/
		
		
	}
	/**
	 * 取字符串(a,b,b,d,)中以ken分隔的第n个值,从0开始
	 * @return 第n个值
	 */
	
	public static String listNoN(String s,int n,String ken){
		String re = "";
		try{
			re = PubFun.strKen(s,ken)[n];
		}
		catch(Exception e){
			System.out.println(e.getMessage());
			LogManager.getLogger("PubFun").error(e.getMessage());
		}
		return re; 
	}
	
	
	/**
	 *将字符串以ken为标志分解赋值给数组
	 */
	public static String[] strKen(String str,String ken){
		try{
			StringTokenizer tk=new StringTokenizer(str,ken);
			int k=tk.countTokens();
			String rs[]=new String[k];
			int i=0;
			while(tk.hasMoreTokens()){   rs[i]=tk.nextToken(); i++;   }
			return rs;
		}catch(Exception e){
			System.out.println(e.getMessage());
			LogManager.getLogger("PubFun").error(e.getMessage());
			return new String[]{};
		}
	}
	

	
	/**
	 * 
	 *将字符串以ken为标志分解赋值给list
	 */
	@SuppressWarnings("unchecked")
	public static List strKenList(String str,String ken){
		if (str==null){str="";}
		List list=new ArrayList();		
		StringTokenizer tk=new StringTokenizer(str,ken);
		while(tk.hasMoreTokens()){   list.add(tk.nextToken());   }
		return list;     
	}
	
	/**
	 * 判断字符串在list中
	 * @param s
	 * @param sList
	 * @return 存在返回1，不在返回0
	 */
	public static int inList(String s,List sList){		
		try {
			if (sList==null)	return 0;
			for(int i=0;i<sList.size();i++){
				if (s.equals(sList.get(i))) return 1;
		     }
			return 0;
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
			LogManager.getLogger("PubFun").error(e.getMessage());
			return 0;
		}
	}
	
	public static void main(String[] args){
		String	newUnionCode="2,3,";
		String  oldUnionCode="2,3,";

		String [] newData =	newUnionCode.split(",");
	
		String addData="";
		for(String nd:newData){
			if(!oldUnionCode.contains(nd)){
				addData+=nd+",";
			}else{
				oldUnionCode=oldUnionCode.replace(nd+",", "");//最后的数据为删除数据
			}
			
			
			
		}
		
		System.out.println(addData);
		System.out.println(oldUnionCode);
		
		
		
	}
	
	/**
	 * 判断字符串a是否在以ken为分隔符的字符串b中
	 * @param a
	 * @param b，ken
	 * @return 存在返回1，不在返回0
	 */
	public static int inStrings(String a,String b,String ken){		
		try {
			if (b==null)	return 0;
			
			List sList = PubFun.strKenList(b,ken);
			
			for(int i=0;i<sList.size();i++){
				if (a.equals(sList.get(i))) return 1;
		     }
			return 0;
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
			LogManager.getLogger("PubFun").error(e.getMessage());
			return 0;
		}
	}
	
	/**
	 * 转换data类型中的日期为字符串，格式为2008x12x8
	 */
	public static String datetolong(java.util.Date d,String x){

		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy"+x+"MM"+x+"dd");
		try {    
			return sdformat.format(d);      
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			LogManager.getLogger("PubFun").error(e.getMessage());
			return "";      
		}
	}
	
	/**
	 * 转换java.util.Date类型转换为String类型，格式为2005-08-08  18:08:08
	 * @param date  java.util.Date类型
	 * @return
	 */

	public static String datetolongTime(java.util.Date date){
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
		try {    
			return sdformat.format(date);      
		}
		catch (Exception e) {        
			System.out.println(e.getMessage());
			LogManager.getLogger("PubFun").error(e.getMessage());
			return "";
		}
	}
	

	
	/**
	 * 将格式为2005-12-12的字符串转换为java.util.Date类型,如果差数格式不对将返回null
	 * @param strDate
	 * @return
	 */
	public static Date strToDate(String strDate) {
		Date uDate = null;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try{
			uDate = simpleDateFormat.parse(strDate);
		}
		catch (Exception e){
			System.out.println(e.getMessage());
			LogManager.getLogger("PubFun").error(e.getMessage());
		}
		return uDate;
		
	}

	/**
	 * 如果字符串最后一个字符为ken，删掉这个ken
	 * @param s
	 * @param ken
	 * @return
	 */
	public static String delLastKen(String s,String ken){
		if (s!=null){
			if(s.endsWith(ken)){
				s = s.substring(0,s.length()-1);
				
			}
		}		
		return s;
		
	}
	
	
	/**
	 * 左截字符串的多少位
	 * @param 字符串s，多少位b,c末尾省略格式
	 * @param ken
	 * @return 返回前b位的字符串，末尾格式为"c"
	 */
	public static String leftStr(String s,int b,String c){
		if (s!=null){
			if(s.length()>b){
				s = s.substring(0,b)+c;
			}
		}		
		return s;
		
	}
	
	
	/**
	 * 如果字符串以ken1分割，并且最后一个字符为ken1，删掉最后这个ken，并将所有ken1替换成ken2
	 * @param s
	 * @param ken
	 * @return
	 */
	public static String delLastKenAndreplaceKen(String s,String ken1,String ken2){
		if (s!=null){
			if(s.endsWith(ken1)){
				s = s.substring(0,s.length()-1);
				s = s.replaceAll(ken1,ken2);
				
			}else{
				s = s.replaceAll(ken1,ken2);
			
			}
		}		
		return s;
		
	}
	
	
	

	/**
	 * 如果字符串以ken1分割将所有ken1替换成ken2
	 * @param s
	 * @param ken
	 * @return
	 */
	public static String replaceKen(String s,String ken1,String ken2){
		if (s!=null){
		
			if(s.indexOf(ken1)>0){
		
				s = s.replaceAll(ken1,ken2);
				
			}
		}		
		return s;
		
	}
	
	
	
	/**
	 * 判断字符串是否是数字
	 * @param s
	 * @param ken
	 * @return
	 */
	
	public   static   boolean   IsNum(String   strin)   
	  {   
		if(strin==null){
			  return   false;   
		}
	  String   s=strin.replaceAll("[0-9;]+","");   
	  if(s.equals(""))   
	  {   
	  return   true;   
	  }else{   
	  return   false;   
	  }   
	  }  
	
	
	
	/**
	 * 文件转字节方法
	 * 
	 * @param file 文件
	 * @return
	 */
	

	/**
	 * 将iso编码转换成utf8编码
	 * @param s
	 * @param ken
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	
   @SuppressWarnings("deprecation")
public static String IsoToUtf8(String str) {
	   String tempVal = URLDecoder.decode(str);
	   try {
		return tempVal=  new String(tempVal.getBytes("ISO-8859-1"), "utf-8");
	   } catch (Exception e) {
		   System.out.println(e.getMessage());
			LogManager.getLogger("PubFun").error(e.getMessage());
	   }
	   return tempVal;
   }

	
	public static byte[] getBytesFromFile(File file) throws IOException {
		try{
		InputStream is = new FileInputStream(file); 
		long length = file.length(); 
		if (length > Integer.MAX_VALUE) { 
			
		}
		byte[] bytes = new byte[(int)length]; 
	    int offset = 0; 
		int numRead = 0; 
		while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
			offset += numRead; 
			} 
		if (offset < bytes.length) { 
		   	  throw new IOException("Could not completely read file "+file.getName()); 
			}
			return bytes;
		
			//return FileCopyUtils.copyToByteArray(file);
	    
		}catch(Exception e){
		
			System.out.println(e.getMessage());
			LogManager.getLogger("PubFun").error(e.getMessage());
			return null;
		}
} 


	
	/**
	 * 得到当前时间
	 * @param s
	 * @param ken
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	
   public static String getTime() {
	   String ut="";
	   try {
			    SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			    ut=sdformat.format(new Date());	

		return ut;
	   } catch (Exception e) {
	    System.out.println(e.getMessage());
		LogManager.getLogger("PubFun").error(e.getMessage());
	   }
	   return ut;
   }
	
	/**
	 * 得到当前时间
	 * @param s
	 * @param ken
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	
  public static String getDate() {
	   String ut="";
	   try {
			    SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
			    ut=sdformat.format(new Date());	

		return ut;
	   } catch (Exception e) {
		   System.out.println(e.getMessage());
			LogManager.getLogger("PubFun").error(e.getMessage());
	   }
	   return ut;
  }

	/**
	 * 得到当前时间
	 * @param s
	 * @param ken
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	
  public static String getTimeId() {
	   String ut="";
	   try {
			    SimpleDateFormat sdformat = new SimpleDateFormat("yyyyMMddHHmmss");
			    ut=sdformat.format(new Date());	

		return ut;
	   } catch (Exception e) {
		   System.out.println(e.getMessage());
			LogManager.getLogger("PubFun").error(e.getMessage());
	   }
	   return ut;
  }

	
  

  /** 
   * 获得一个UUID 
   * @return String UUID 
   */ 
  public static String getUUID(){ 
      String s = UUID.randomUUID().toString(); 
      //去掉“-”符号 
      return s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24); 
  } 


  

	/**
	 * 对调以c分隔的总字符串里的两个字符串
	 * @param 总字符串s，字符串a,字符串b
	 * @param ken
	 * @return 返回对调侯的总字符串
	 */
	public static String strSwap(String s,String a,String b){
		if (s!=null){
			s=s.replaceAll(a, "@");
			s=s.replaceAll(b, "#");			
			s=s.replaceAll("@", b);		
			s=s.replaceAll("#", a);
		}		
		return s;
		
	}
	

	/**
	 * 得到以a分隔的文件后缀名，例如1.jpg得到jpg
	 * @param 总字符串s,分隔符a
	 * @param ken
	 * @return 返回对调侯的总字符串
	 */
	public static String getFileType(String s,String a){
		if (s!=null){
			s=s.substring(s.lastIndexOf(a),s.length());
		}		
		return s;
		
	}


	
	/**
	 * 把文件传换成byte[]形式
	 * @param fileSrc 文件路径
	 * @return 返回byte[]
	 * @@author 
	 */
	public static byte[] fileToByte(String fileSrc) throws IOException{
		InputStream in = null ;
        OutputStream out = null ;
        int BUFFER_SIZE = 16 * 1024 ;
		try{
		in = new BufferedInputStream( new FileInputStream(fileSrc), BUFFER_SIZE);
        out = new BufferedOutputStream( new FileOutputStream(fileSrc), BUFFER_SIZE);
         byte [] buffer = new byte [BUFFER_SIZE];
         while (in.read(buffer) > 0 )  {
            out.write(buffer);
        } 
         return buffer;   
		}catch (Exception e)  {
			System.out.println(e.getMessage());
			LogManager.getLogger("PubFun").error(e.getMessage());
			return null;        
        } finally  {
            if ( null != in)  {
                in.close();
            } 
              if ( null != out)  {
                out.close();
            } 
        }


		
	}
	/**
	 * 把文件传换成byte[]形式
	 * @param fileSrc 文件路径
	 * @return 返回byte[]
	 * @@author 
	 */
	public static byte[] fileToByte(File file) throws IOException{
		FileInputStream in = null ;
		ByteArrayOutputStream out = null ;
        int BUFFER_SIZE = 16 * 1024 ;
		try{
			in=new FileInputStream(file);
    		out =  new ByteArrayOutputStream(BUFFER_SIZE);
            byte [] buffer = new byte [BUFFER_SIZE];
            int n;
            while ((n=in.read(buffer))!=-1)  {
                out.write(buffer,0,n);
            } 

         return out.toByteArray();   
		}catch (Exception e)  {
			System.out.println(e.getMessage());
			LogManager.getLogger("PubFun").error(e.getMessage());
			return null;        
        } finally  {
            if ( null != in)  {
                in.close();
            } 
              if ( null != out)  {
                out.close();
            } 
        }


		
	}
	
	/**
	 * 对输入的日期date进行计算，返回计算结果
	 * @param date Date型日期
	 * @param ken "+"或"-" 其他字符均返回输入的date
	 * @param days 增加或减少的天数
	 * @return 计算后的Date型日期，若有异常返回输入的date
	 * @author 
	 */
	public static Date datecalc(Date date,String ken,int days){
		try{
		if(ken.equals("+")){
			return new Date(date.getTime() + new Long(days) * 24 * 60 * 60 * 1000);
		}else if(ken.equals("-")){
			return new Date(date.getTime() - new Long(days) * 24 * 60 * 60 * 1000);
		}else{
			return date;
		}
		}catch (Exception e) {    
			System.out.println(e.getMessage());
			LogManager.getLogger("PubFun").error(e.getMessage());
			return date;      
		}
	}

	/**
	 * 转换java.util.Date类型转换为String类型，格式为2005x08x08  18:08:08
	 * @param date  java.util.Date类型
	 * @return
	 */

	public static String datetolongTime(java.util.Date date,String x){
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy"+x+"MM"+x+"dd HH:mm:ss");
		try {    
			return sdformat.format(date);      
		}
		catch (Exception e) {        
			System.out.println(e.getMessage());
			LogManager.getLogger("PubFun").error(e.getMessage());
			return "";      
		}
	}
	
	
	/**
	 *将s补齐0到n位
	 *@author 
	 */
	public static String padded(String s,int n){
		if(s!=null){
			
			for(int m=s.length();m<n;m++){
				s="0"+s;
			}
			
		}
		
		return s;
	}
	
	public static boolean isStringEmpty(String s)
	{
		if(s==null||s.equals(""))
		{
			return false;
		}
		return true;
	}
	
}
