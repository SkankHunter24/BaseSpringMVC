package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* * 分页数据存储类 * *
* 
* 
*/
public class Pagination {

	
	
	/**
	 * 构造函数 根据传入的信息初始化分页数据 切割数据集
	 * @param numPerPage 每页多少行
	 * @param currentPage  当前页
	 * @param jumpAction   切换页码跳转地址
	 * @param resultList       数据集
	 */
	public Pagination(int numPerPage,int currentPage,List resultList)
	{
		this.numPerPage = numPerPage;
		this.currentPage = currentPage;
		this.resultList = resultList;
		this.setTotalPages();
		this.cutResult();
	}

// 每页显示的记录数
private int numPerPage;


// 记录总数
private int totalRows;

//分页跳转地址
private String jumpAction;

// 总页数
private int totalPages;

// 当前页码
private int currentPage;

// 结果集存放List
private List resultList;

private String pageStr;



public int getCurrentPage() {
   return currentPage;
}

public void setCurrentPage(int currentPage) {
   this.currentPage = currentPage;
}

public int getNumPerPage() {
   return numPerPage;
}

public void setNumPerPage(int numPerPage) {
   this.numPerPage = numPerPage;
}

public List getResultList() {
   return resultList;
}

public void setResultList(List resultList) {
   this.resultList = resultList;
}

public int getTotalPages() {
   return totalPages;
}

// 计算总页数
public void setTotalPages() {
	if(resultList!=null) this.totalRows =  resultList.size();
	else this.totalRows = 0;
   if (totalRows % numPerPage == 0) {
    this.totalPages = totalRows / numPerPage;
   } else {
    this.totalPages = (totalRows / numPerPage) + 1;
   }
}

//根據页数行数切割数据集
public void cutResult(){
	int startIndex = this.currentPage*this.numPerPage-this.numPerPage;
	int endIndex = this.currentPage*this.numPerPage-1;
	List list = new ArrayList();
	if(resultList!=null) 
	{

		for(int i =0;i<resultList.size();i++)
		{
			if(i>=startIndex&&i<=endIndex)
			{
				list.add(resultList.get(i));
			}
		}
		this.setResultList(list);
	}
}

public int getTotalRows() {
	return this.totalRows;
   
}

public void setTotalRows(int totalRows) {
   this.totalRows = totalRows;
}


public String getPageNStr(String n,String emphasis) {

	if(emphasis.equals("yes")){
		return "<input type=\"button\" value=\""+n+"\" style=\"width:26px;height:23px;border: solid 1px;CURSOR: hand\"  onClick=\"changePage("+(n)+")\"> ";
	}else{
		return "<input type=\"button\" value=\""+n+"\" style=\"width:26px;height:23px;border: solid 1px;CURSOR: hand\"  onClick=\"changePage("+(n)+")\"> ";
	}
	
}

//得到分页显示代码
public String getPageStr() {
	
	
	this.pageStr="";//初始化
	String prev_show="<input type=\"button\" value=\"<<上一页\" style=\"height:23px;CURSOR: hand\" onClick=\"changePage("+(currentPage-1)+")\"> ";
	String prev_no="<input type=\"button\" value=\"<<上一页\" disabled style=\"height:23px;\"> ";
	String next_show="<input type=\"button\" value=\"下一页>>\" style=\"height:23px;CURSOR: hand\" onClick=\"changePage("+(currentPage+1)+")\"> ";
	String next_no="<input type=\"button\" value=\"下一页>>\" disabled style=\"height:23px;\" > ";
	String dig="<span style=\"width:10 px\"></span>...<span style=\"width:10 px\"></span>";

	
	if(totalPages>=9){//一、如果总页数大于等于9时的情况下，出现省略号
	    if(currentPage==1){//1、当前页等于1的时候，前面到3为止，后面出现省略号，上页禁用。例：(no)上一页 1 2 3..45 46 下一页
	    	this.pageStr=prev_no+getPageNStr(String.valueOf(1),"yes")+getPageNStr(String.valueOf(2),"no")+getPageNStr(String.valueOf(3),"no")+dig+getPageNStr(String.valueOf(totalPages-1),"no")+getPageNStr(String.valueOf(totalPages),"no")+next_show;
	    }else if(currentPage==2){//2、当前页等于2和3时，前面到3为止，上页启用，后面出现省略号。例：上一页 1 2 3..45 46 下一页
	    	this.pageStr=prev_show+getPageNStr(String.valueOf(1),"no")+getPageNStr(String.valueOf(2),"yes")+getPageNStr(String.valueOf(3),"no")+dig+getPageNStr(String.valueOf(totalPages-1),"no")+getPageNStr(String.valueOf(totalPages),"no")+next_show;
	    }else if(currentPage==3){//3、当前页等于3时，前面到3为止，上页启用，后面出现省略号。例：上一页 1 2 3 4..45 46 下一页
	    	this.pageStr=prev_show+getPageNStr(String.valueOf(1),"no")+getPageNStr(String.valueOf(2),"no")+getPageNStr(String.valueOf(3),"yes")+getPageNStr(String.valueOf(4),"no")+dig+getPageNStr(String.valueOf(totalPages-1),"no")+getPageNStr(String.valueOf(totalPages),"no")+next_show;
	    }else if(currentPage==4){//4、当前页等于3时，前面到4为止，上页启用，后面出现省略号。例：上一页 1 2 3 4 5..45 46 下一页
	    	this.pageStr=prev_show+getPageNStr(String.valueOf(1),"no")+getPageNStr(String.valueOf(2),"no")+getPageNStr(String.valueOf(3),"no")+getPageNStr(String.valueOf(4),"yes")+getPageNStr(String.valueOf(5),"no")+dig+getPageNStr(String.valueOf(totalPages-1),"no")+getPageNStr(String.valueOf(totalPages),"no")+next_show;
	    }else if(currentPage==5){//5、当前页等于5时，前面到5为止，上页启用，后面出现省略号。例：上一页 1 2 3 4 5..45 46 下一页
	    	this.pageStr=prev_show+getPageNStr(String.valueOf(1),"no")+getPageNStr(String.valueOf(2),"no")+getPageNStr(String.valueOf(3),"no")+getPageNStr(String.valueOf(4),"no")+getPageNStr(String.valueOf(5),"yes")+getPageNStr(String.valueOf(6),"no")+dig+getPageNStr(String.valueOf(totalPages-1),"no")+getPageNStr(String.valueOf(totalPages),"no")+next_show;	   

	    
	    }else if(currentPage>=6&&currentPage<(totalPages-4)){//1、如果当前页大于等于6时，小于总页数减2，前后都出现省略号
			this.pageStr=prev_show+getPageNStr(String.valueOf(1),"no")+getPageNStr(String.valueOf(2),"no")+dig;
			this.pageStr=this.pageStr+getPageNStr(String.valueOf(currentPage-1),"no")+getPageNStr(String.valueOf(currentPage),"yes")+getPageNStr(String.valueOf(currentPage+1),"no");
			this.pageStr=this.pageStr+dig+getPageNStr(String.valueOf(totalPages-1),"no")+getPageNStr(String.valueOf(totalPages),"no")+next_show;
		
		}else if(currentPage==(totalPages-4)){//倒数第5个,上一页 1 2..41 42 43 44 45 46 下一页
			this.pageStr=prev_show+getPageNStr(String.valueOf(1),"no")+getPageNStr(String.valueOf(2),"no");
			this.pageStr=this.pageStr+dig;
			this.pageStr=this.pageStr+getPageNStr(String.valueOf(totalPages-5),"no")+getPageNStr(String.valueOf(totalPages-4),"yes")+getPageNStr(String.valueOf(totalPages-3),"no")+getPageNStr(String.valueOf(totalPages-2),"no")+getPageNStr(String.valueOf(totalPages-1),"no")+getPageNStr(String.valueOf(totalPages),"no")+next_show;
		}else if(currentPage==(totalPages-3)){//倒数第4个,上一页 1 2..42 43 44 45 46 下一页
			this.pageStr=prev_show+getPageNStr(String.valueOf(1),"no")+getPageNStr(String.valueOf(2),"no");
			this.pageStr=this.pageStr+dig;
			this.pageStr=this.pageStr+getPageNStr(String.valueOf(totalPages-4),"no")+getPageNStr(String.valueOf(totalPages-3),"yes")+getPageNStr(String.valueOf(totalPages-2),"no")+getPageNStr(String.valueOf(totalPages-1),"no")+getPageNStr(String.valueOf(totalPages),"no")+next_show;
		}else if(currentPage==(totalPages-2)){//倒数第3个,上一页 1 2..43 44 45 46 下一页
			this.pageStr=prev_show+getPageNStr(String.valueOf(1),"no")+getPageNStr(String.valueOf(2),"no");
			this.pageStr=this.pageStr+dig;
			this.pageStr=this.pageStr+getPageNStr(String.valueOf(totalPages-3),"no")+getPageNStr(String.valueOf(totalPages-2),"yes")+getPageNStr(String.valueOf(totalPages-1),"no")+getPageNStr(String.valueOf(totalPages),"no")+next_show;
		}else if(currentPage==(totalPages-1)){//倒数第2个,上一页 1 2..44 45 46 下一页
			this.pageStr=prev_show+getPageNStr(String.valueOf(1),"no")+getPageNStr(String.valueOf(2),"no");
			this.pageStr=this.pageStr+dig;
			this.pageStr=this.pageStr+getPageNStr(String.valueOf(totalPages-2),"no")+getPageNStr(String.valueOf(totalPages-1),"yes")+getPageNStr(String.valueOf(totalPages),"no")+next_show;
		}else if(currentPage==(totalPages)){//倒数第1个,上一页 1 2..44 45 46 下一页(no)
			this.pageStr=prev_show+getPageNStr(String.valueOf(1),"no")+getPageNStr(String.valueOf(2),"no");
			this.pageStr=this.pageStr+dig;
			this.pageStr=this.pageStr+getPageNStr(String.valueOf(totalPages-2),"no")+getPageNStr(String.valueOf(totalPages-1),"no")+getPageNStr(String.valueOf(totalPages),"yes")+next_no;
		}else{
			
		}
		
	}else{//如果总页数小于9的时候
	
			if(currentPage==1&&currentPage!=totalPages){
				this.pageStr=prev_no;
				for(int i=1;i<=totalPages;i++){
					if(i==1){
						this.pageStr=this.pageStr+getPageNStr(String.valueOf(i),"yes");
					}else{
						this.pageStr=this.pageStr+getPageNStr(String.valueOf(i),"no");
					}
					
				}
				this.pageStr=this.pageStr+next_show;
			}else if(currentPage==1&&currentPage==totalPages){
				        this.pageStr=prev_no;
						this.pageStr=this.pageStr+getPageNStr(String.valueOf(1),"yes");
				        this.pageStr=this.pageStr+next_no;
				
				
			}else if(currentPage!=1&&currentPage==totalPages){
				this.pageStr=prev_show;
				for(int i=1;i<=totalPages;i++){
					if(i==currentPage){
						this.pageStr=this.pageStr+getPageNStr(String.valueOf(i),"yes");
					}else{
						this.pageStr=this.pageStr+getPageNStr(String.valueOf(i),"no");
					}
					
				}
				this.pageStr=this.pageStr+next_no;	
			}else{
				this.pageStr=prev_show;
				for(int i=1;i<=totalPages;i++){
					if(i==currentPage){
						this.pageStr=this.pageStr+getPageNStr(String.valueOf(i),"yes");
					}else{
						this.pageStr=this.pageStr+getPageNStr(String.valueOf(i),"no");
					}
					
				}
				this.pageStr=this.pageStr+next_show;	
			}
		

	}
	
	if(totalPages==0){
		this.pageStr="";
	}


return pageStr;
}

public static int getPageNumber(String page){
	int ss=1;
	try{
		
	  if(page==null||"null".equals(page)){
		  ss= 1;
      }else{
		ss=Integer.parseInt(page);
		if(ss<=0){
			 ss=1;
		}	  			  
      }	
    }catch(Exception e){
		return 1;
	}
    return ss;
}
}

