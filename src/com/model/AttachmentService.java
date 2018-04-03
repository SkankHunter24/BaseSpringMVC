package com.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.dao.AttachmentMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.vo.Attachment;

import base.BaseServiceSupport;
import utils.PubFun;

@Service("AttachmentService")
@Scope("prototype")
@Transactional
public class AttachmentService  extends BaseServiceSupport{

	/**
	 * 保存附件
	 * @param file 文件
	 * @param a 附件对象
	 * @throws Exception
	 */
	public void saveFile(MultipartFile file,Attachment a) throws Exception {

			//先从资源文件中拿到储存路径
			Properties prop = new Properties();
			String path = this.getClass().getClassLoader().getResource("/").getPath();
			path = path.substring(1, path.indexOf("classes"));
			path += "/classes/config/resource.properties";
			path = path.replaceAll("%20", " ");
			prop.load(new FileInputStream(path));
			String uploadPath = prop.getProperty( "attachment_upload_path" ).trim();    
			//上传目录地址 = 根地址+自定义上传目录
			String root = System.getProperty("webApp.root");
			uploadPath =  root + uploadPath;
			///////////////////////////////////////////////
			
			//复制文件到新路径下
			File toFile = new File(uploadPath+"\\"+a.getAttachmentId()+"."+a.getAttachmentType());
			a.setAttachmentPath(toFile.getPath());
			FileInputStream ins =(FileInputStream)file.getInputStream();
			FileOutputStream out = new FileOutputStream(toFile);
			byte[] b = new byte[1024];
			int n = 0;
			while ((n = ins.read(b)) != -1) {
				out.write(b, 0, n);
			}
			ins.close();
			out.close();
			/////////////////////////////////////////////////
			
			//插入记录
			AttachmentMapper mapper  = this.getMapper(AttachmentMapper.class);
			mapper.insert(a);

	}
	
	
	/**
	 * 获取该关联ID下的所有附件做初始化
	 * @param relationID
	 * @return
	 * @throws Exception
	 */
	public String getFilesJson(String relationID,String controlID,String uploaderId) throws Exception{
		AttachmentMapper mapper  = this.getMapper(AttachmentMapper.class);
		List<Attachment> list = mapper.selectByRelationId_controlId_uploaderId(relationID,controlID,uploaderId);
		ObjectMapper obmapper = new ObjectMapper();  
		String result = "";
		if(list==null&&list.size()==0){
			result = "{'result':'success','count':'0'}";
		}
		else{
			result = obmapper.writeValueAsString(list);
			result = "{'result':'success','count':'"+String.valueOf(list.size())+"',data:"+result+"}";
		}
		return result;
	}
	
	/**
	 * 附件上传结束 更新为全部可用 删除取消附件
	 * @param a 附件对象（仅作sql传参）
	 * 	@param deleteFiles 所需删除的附件id字符串
	 * @throws Exception
	 */
	public void updateFiles_OperationDone(String relationId ,String deleteFiles,String uploaderId,String uploaderControlId) throws Exception{
		Date date = new Date();
		String dateNow = PubFun.datetolongTime(date, "-");
		AttachmentMapper mapper  = this.getMapper(AttachmentMapper.class);

		if(deleteFiles!=null){
			String[] files = deleteFiles.split(",");
			for(int i =0;i<files.length;i++){
					mapper.deleteByPrimaryKey(files[i]);
			}
		}
		
		List<Attachment> list = mapper.selectByRelationId_singleOpreation(relationId,uploaderId,uploaderControlId);
		//将所有该关联ID下的附件更新为可用
		for(int i =0;i<list.size();i++){
			Attachment a =list.get(i);
			a.setAttachmentUploadTime(dateNow);
			a.setAttachmentFlag("1");
			mapper.updateByPrimaryKey(a);
		}
		
		//将其他附件更新为可用
		//attachmentDao.update
	}
}
