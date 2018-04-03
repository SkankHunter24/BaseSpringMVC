package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.model.AttachmentService;
import com.model.vo.Attachment;

import base.BaseControllerSupport;
import base.SerializableWrite;
import base.ParallelizationWrite;
import utils.PubFun;

@Controller
@Scope("prototype")

public class AttachmentController extends BaseControllerSupport {
	private static final long serialVersionUID = 1L;
	@Autowired
	public AttachmentService attachmentService;

	@RequestMapping("/fileUpload")
	@ResponseBody
	@ParallelizationWrite
	public void fileUpload(@RequestParam("upload") MultipartFile file) {
		try {
			Attachment a = new Attachment();

			// 读取页面传过来的参数
			String wholeFileName = this.getRequest().getParameter("fileName");
			String uploaderID = this.getRequest().getParameter("uploaderID");
			String uploaderName = this.getRequest().getParameter("uploaderName");
			String uploadControlID = this.getRequest().getParameter("uploadControlID");
			String relationID = this.getRequest().getParameter("relationID");
			////////////////////////////////////////////////////
			String fileType = "";
			if (wholeFileName.contains(".")) {
				int index = wholeFileName.lastIndexOf(".");
				fileType = wholeFileName.substring(index + 1);
			}

			// 填充附件的实体类

			a.setAttachmentId(PubFun.getUUID());
			a.setAttachmentFlag("0");// 附件生效前 默认为0
			a.setAttachmentName(wholeFileName);
			a.setAttachmentSize(String.valueOf(file.getSize()));
			a.setAttachmentType(fileType);
			a.setAttachmentUploaderId(uploaderID);
			a.setAttachmentControlId(uploadControlID);
			a.setAttachmentRelationId(relationID);
			a.setAttachmentUploaderName(uploaderName);
			//////////////////////////////////////////

			// 保存后给前台返回保存成功
			attachmentService.saveFile(file, a);
			this.textToResponse(this.getResponse(), "{'result':'success','ID':'" + a.getAttachmentId() + "'}");
		} catch (Exception e) {
			this.getLogger("AttachmentController").error(e.getMessage());
			e.printStackTrace();
		}
	}

	@RequestMapping("/fileSubmit")
	@ResponseBody
	@ParallelizationWrite
	public void fileSubmit() throws Exception {
		try {
			//一般在提交表单时同时调用 将本次操作的所有附件更新为可用 并且删除删除的附件
			String deleteFiles = this.getRequest().getParameter("deletedFiles");
			String relationId = this.getRequest().getParameter("relationID");
			String uploaderId = this.getRequest().getParameter("uploaderID");
			String uploadControlId = this.getRequest().getParameter("uploadControlID");
			attachmentService.updateFiles_OperationDone(relationId, deleteFiles,uploaderId,uploadControlId);
			this.textToResponse(this.getResponse(), "{'result':'success'}");
		}
		catch (Exception e) {
			this.getLogger("AttachmentController").error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/initLoadFiles")
	@ResponseBody
	@SerializableWrite
	public void initLoadFiles() throws Exception {
		try {
			//附件控件初始化 获取所有的附件信息
			String relationId = this.getRequest().getParameter("relationID");
			String controlId = this.getRequest().getParameter("uploadControlID");
			String uploaderId = this.getRequest().getParameter("uploaderID");
			String jsonResult = attachmentService.getFilesJson(relationId, controlId,uploaderId);
			this.textToResponse(this.getResponse(), jsonResult);

		}
		catch (Exception e) {
			this.getLogger("AttachmentController").error(e.getMessage());
			e.printStackTrace();
		}
	}

}
