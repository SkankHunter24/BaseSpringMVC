<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>附件管理</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">

<script type="text/javascript" src="<%=basePath%>/COMMON/jquery/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/COMMON/jquery/jquery.swfobject.1-1-1.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=basePath%>/COMMON/attachment/attachment.css" />
<script type="text/javascript" src="<%=basePath%>/COMMON/attachment/attachment.js"></script>
</head>
<body>
<div style="width:300">
	<input type="file" name="uploadify" id="uploadify" />
	<div onclick = "fileSubmit(ui)">提交</div>
</div>

<script language=javascript>
	var ui = new uploadItem();
	ui.uploaderName = "1";
	ui.uploaderID = "1";
	ui.relationID = "123";
	ui.uploadControlID = "uploadify";
	ui.uploadScriptURL = "fileUpload";
	ui.submitScriptURL = "fileSubmit";
	ui.submitScriptURL = "initLoadFiles"
	initAttachment(ui);
</script>


</body>
</html>