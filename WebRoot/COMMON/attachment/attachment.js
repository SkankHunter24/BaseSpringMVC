
/*
UploadiFive 1.2.2
Copyright (c) 2012 Reactive Apps, Ronnie Garcia
Released under the UploadiFive Standard License <http://www.uploadify.com/uploadifive-standard-license>
*/
;(function(b) {
	var a = {
		init : function(c) {
			return this.each(function() {
				var g = b(this);
				g.data("uploadifive", {
					inputs : {},
					inputCount : 0,
					fileID : 0,
					queue : {
						count : 0,
						selected : 0,
						replaced : 0,
						errors : 0,
						queued : 0,
						cancelled : 0
					},
					uploads : {
						current : 0,
						attempts : 0,
						successful : 0,
						errors : 0,
						count : 0
					}
				});
				var d = g.data("uploadifive");
				var f = d.settings = b.extend({
					auto : true,
					buttonClass : false,
					buttonText : "Select Files",
					checkScript : false,
					dnd : true,
					dropTarget : false,
					fileObjName : "Filedata",
					fileSizeLimit : 0,
					fileType : false,
					formData : {},
					height : 30,
					itemTemplate : false,
					method : "post",
					multi : true,
					overrideEvents : [],
					queueID : false,
					queueSizeLimit : 0,
					removeCompleted : false,
					simUploadLimit : 0,
					truncateLength : 0,
					uploadLimit : 0,
					uploadScript : "uploadifive.php",
					width : 100
				}, c);
				if (isNaN(f.fileSizeLimit)) {
					var e = parseInt(f.fileSizeLimit) * 1.024;
					if (f.fileSizeLimit.indexOf("KB") > -1) {
						f.fileSizeLimit = e * 1000;
					} else {
						if (f.fileSizeLimit.indexOf("MB") > -1) {
							f.fileSizeLimit = e * 1000000;
						} else {
							if (f.fileSizeLimit.indexOf("GB") > -1) {
								f.fileSizeLimit = e * 1000000000;
							}
						}
					}
				} else {
					f.fileSizeLimit = f.fileSizeLimit * 1024;
				}
				d.inputTemplate = b('<input type="file">').css({
					"font-size" : f.height + "px",
					opacity : 0,
					position : "absolute",
					right : "-3px",
					top : "-3px",
					"z-index" : 999
				});
				d.createInput = function() {
					var j = d.inputTemplate.clone();
					var k = j.name = "input" + d.inputCount++;
					if (f.multi) {
						j.attr("multiple", true);
					}
					if (f.fileType) {
						j.attr("accept", f.fileType);
					}
					j.bind("change", function() {
						d.queue.selected = 0;
						d.queue.replaced = 0;
						d.queue.errors = 0;
						d.queue.queued = 0;
						var l = this.files.length;
						d.queue.selected = l;
						if ((d.queue.count + l) > f.queueSizeLimit && f.queueSizeLimit !== 0) {
							if (b.inArray("onError", f.overrideEvents) < 0) {
								alert("The maximum number of queue items has been reached (" + f.queueSizeLimit + ").  Please select fewer files.");
							}
							if (typeof f.onError === "function") {
								f.onError.call(g, "QUEUE_LIMIT_EXCEEDED");
							}
						} else {
							for (var m = 0; m < l; m++) {
								file = this.files[m];d.addQueueItem(file);
							}
							d.inputs[k] = this;d.createInput();
						}
						if (f.auto) {
							a.upload.call(g);
						}
						if (typeof f.onSelect === "function") {
							f.onSelect.call(this.files, d.queue);
						}
					});
					if (d.currentInput) {
						d.currentInput.hide();
					}
					d.button.append(j);
					d.currentInput = j;
				};
				d.destroyInput = function(j) {
					b(d.inputs[j]).remove();
					delete d.inputs[j];
					d.inputCount--;
				};
				d.drop = function(m) {
					d.queue.selected = 0;
					d.queue.replaced = 0;
					d.queue.errors = 0;
					d.queue.queued = 0;
					var l = m.dataTransfer;
					var k = l.name = "input" + d.inputCount++;
					var j = l.files.length;
					d.queue.selected = j;
					if ((d.queue.count + j) > f.queueSizeLimit && f.queueSizeLimit !== 0) {
						if (b.inArray("onError", f.overrideEvents) < 0) {
							alert("The maximum number of queue items has been reached (" + f.queueSizeLimit + ").  Please select fewer files.");
						}
						if (typeof f.onError === "function") {
							f.onError.call(g, "QUEUE_LIMIT_EXCEEDED");
						}
					} else {
						for (var o = 0; o < j; o++) {
							file = l.files[o];d.addQueueItem(file);
						}
						d.inputs[k] = l;
					}
					if (f.auto) {
						a.upload.call(g);
					}
					if (typeof f.onDrop === "function") {
						f.onDrop.call(g, l.files, l.files.length);
					}
					m.preventDefault();m.stopPropagation();
				};
				d.fileExistsInQueue = function(k) {
					for (var j in d.inputs) {
						input = d.inputs[j];
						limit = input.files.length;
						for (var l = 0; l < limit; l++) {
							existingFile = input.files[l];
							if (existingFile.name == k.name && !existingFile.complete) {
								return true;
							}
						}
					}
					return false;
				};
				d.removeExistingFile = function(k) {
					for (var j in d.inputs) {
						input = d.inputs[j];
						limit = input.files.length;
						for (var l = 0; l < limit; l++) {
							existingFile = input.files[l];
							if (existingFile.name == k.name && !existingFile.complete) {
								d.queue.replaced++;a.cancel.call(g, existingFile, true);
							}
						}
					}
				};
				if (f.itemTemplate == false) {
					d.queueItem = b('<div class="uploadifive-queue-item">                        <a class="close" href="#">X</a>                        <div><span class="filename"></span><span class="fileinfo"></span></div>                        <div class="progress">                            <div class="progress-bar"></div>                        </div>                    </div>');
				} else {
					d.queueItem = b(f.itemTemplate);
				}
				d.addQueueItem = function(j) {
					if (b.inArray("onAddQueueItem", f.overrideEvents) < 0) {
						d.removeExistingFile(j);
						j.queueItem = d.queueItem.clone();j.queueItem.attr("id", f.id + "-file-" + d.fileID++);j.queueItem.find(".close").bind("click", function() {
							a.cancel.call(g, j);return false;
						});
						var k = j.name;
						if (k.length > f.truncateLength && f.truncateLength != 0) {
							k = k.substring(0, f.truncateLength) + "...";
						}
						j.queueItem.find(".filename").html(k);j.queueItem.data("file", j);d.queueEl.append(j.queueItem);
					}
					if (typeof f.onAddQueueItem === "function") {
						f.onAddQueueItem.call(g, j);
					}
					if (j.size > f.fileSizeLimit && f.fileSizeLimit != 0) {
						d.error("FILE_SIZE_LIMIT_EXCEEDED", j);
					} else {
						d.queue.queued++;d.queue.count++;
					}
				};
				d.removeQueueItem = function(m, l, k) {
					if (!k) {
						k = 0;
					}
					var j = l ? 0 : 500;
					if (m.queueItem) {
						if (m.queueItem.find(".fileinfo").html() != " - 上传完成") {
							m.queueItem.find(".fileinfo").html(" - 已取消");
						}
						m.queueItem.find(".progress-bar").width(0);m.queueItem.delay(k).fadeOut(j, function() {
							b(this).remove();
						});
						delete m.queueItem;
						d.queue.count--;
					}
				};
				d.filesToUpload = function() {
					var k = 0;
					for (var j in d.inputs) {
						input = d.inputs[j];
						limit = input.files.length;
						for (var l = 0; l < limit; l++) {
							file = input.files[l];
							if (!file.skip && !file.complete) {
								k++;
							}
						}
					}
					return k;
				};
				d.checkExists = function(k) {
					if (b.inArray("onCheck", f.overrideEvents) < 0) {
						b.ajaxSetup({
							async : false
						});
						var j = b.extend(f.formData, {
							filename : k.name
						});
						b.post(f.checkScript, j, function(l) {
							k.exists = parseInt(l);
						});
						if (k.exists) {
							if (!confirm("A file named " + k.name + " already exists in the upload folder.\nWould you like to replace it?")) {
								a.cancel.call(g, k);return true;
							}
						}
					}
					if (typeof f.onCheck === "function") {
						f.onCheck.call(g, k, k.exists);
					}
					return false;
				};
				d.uploadFile = function(k, l) {
					if (!k.skip && !k.complete && !k.uploading) {
						k.uploading = true;d.uploads.current++;d.uploads.attempted++;
						xhr = k.xhr = new XMLHttpRequest();
						if (typeof FormData === "function" || typeof FormData === "object") {
							var m = new FormData();
							m.append(f.fileObjName, k);
							for (i in f.formData) {
								m.append(i, f.formData[i]);
							}
							xhr.open(f.method, f.uploadScript, true);xhr.upload.addEventListener("progress", function(n) {
								if (n.lengthComputable) {
									d.progress(n, k);
								}
							}, false);xhr.addEventListener("load", function(n) {
								if (this.readyState == 4) {
									k.uploading = false;
									if (this.status == 200) {
										if (k.xhr.responseText !== "Invalid file type.") {
											d.uploadComplete(n, k, l);
										} else {
											d.error(k.xhr.responseText, k, l);
										}
									} else {
										if (this.status == 404) {
											d.error("404_FILE_NOT_FOUND", k, l);
										} else {
											if (this.status == 403) {
												d.error("403_FORBIDDEN", k, l);
											} else {
												d.error("Unknown Error", k, l);
											}
										}
									}
								}
							});xhr.send(m);
						} else {
							var j = new FileReader();
							j.onload = function(q) {
								var t = "-------------------------" + (new Date).getTime(),
									p = "--",
									o = "\r\n",
									s = "";
								s += p + t + o;
								s += 'Content-Disposition: form-data; name="' + f.fileObjName + '"';
								if (k.name) {
									s += '; filename="' + k.name + '"';
								}
								s += o;
								s += "Content-Type: application/octet-stream" + o + o;
								s += q.target.result + o;
								for (key in f.formData) {
									s += p + t + o;
									s += 'Content-Disposition: form-data; name="' + key + '"' + o + o;
									s += f.formData[key] + o;
								}
								s += p + t + p + o;xhr.upload.addEventListener("progress", function(u) {
									d.progress(u, k);
								}, false);xhr.addEventListener("load", function(v) {
									k.uploading = false;
									var u = this.status;
									if (u == 404) {
										d.error("404_FILE_NOT_FOUND", k, l);
									} else {
										if (k.xhr.responseText != "Invalid file type.") {
											d.uploadComplete(v, k, l);
										} else {
											d.error(k.xhr.responseText, k, l);
										}
									}
								}, false);
								var n = f.uploadScript;
								if (f.method == "get") {
									var r = b(f.formData).param();
									n += r;
								}
								xhr.open(f.method, f.uploadScript, true);xhr.setRequestHeader("Content-Type", "multipart/form-data; boundary=" + t);
								if (typeof f.onUploadFile === "function") {
									f.onUploadFile.call(g, k);
								}
								xhr.sendAsBinary(s);
							};j.readAsBinaryString(k);
						}
					}
				};
				d.progress = function(l, j) {
					if (b.inArray("onProgress", f.overrideEvents) < 0) {
						if (l.lengthComputable) {
							var k = Math.round((l.loaded / l.total) * 100);
						}
						j.queueItem.find(".fileinfo").html(" - " + k + "%");j.queueItem.find(".progress-bar").css("width", k + "%");
					}
					if (typeof f.onProgress === "function") {
						f.onProgress.call(g, j, l);
					}
				};
				d.error = function(l, j, k) {
					if (b.inArray("onError", f.overrideEvents) < 0) {
						switch (l) {
						case "404_FILE_NOT_FOUND":
							errorMsg = "文件上传地址无法访问";
							break;case "403_FORBIDDEN":
							errorMsg = "文件上传地址访问被拒绝";
							break;case "FORBIDDEN_FILE_TYPE":
							errorMsg = "无法上传该类型的文件";
							break;case "FILE_SIZE_LIMIT_EXCEEDED":
							errorMsg = "文件过大";
							break;default:
							errorMsg = "Unknown Error";
							break;
						}
						j.queueItem.addClass("error").find(".fileinfo").html(" - " + errorMsg);j.queueItem.find(".progress").remove();
					}
					if (typeof f.onError === "function") {
						f.onError.call(g, l, j);
					}
					j.skip = true;
					if (l == "404_FILE_NOT_FOUND") {
						d.uploads.errors++;
					} else {
						d.queue.errors++;
					}
					if (k) {
						a.upload.call(g, null, true);
					}
				};
				d.uploadComplete = function(l, j, k) {
					if (b.inArray("onUploadComplete", f.overrideEvents) < 0) {
						j.queueItem.find(".progress-bar").css("width", "100%");j.queueItem.find(".fileinfo").html(" - 上传完成");j.queueItem.find(".progress").slideUp(250);j.queueItem.addClass("complete");
					}
					if (typeof f.onUploadComplete === "function") {
						f.onUploadComplete.call(g, j, j.xhr.responseText);
					}
					if (f.removeCompleted) {
						setTimeout(function() {
							a.cancel.call(g, j);
						}, 3000);
					}
					j.complete = true;d.uploads.successful++;d.uploads.count++;d.uploads.current--;
					delete j.xhr;
					if (k) {
						a.upload.call(g, null, true);
					}
				};
				d.queueComplete = function() {
					if (typeof f.onQueueComplete === "function") {
						f.onQueueComplete.call(g, d.uploads);
					}
				};
				if (window.File && window.FileList && window.Blob && (window.FileReader || window.FormData)) {
					f.id = "uploadifive-" + g.attr("id");
					d.button = b('<div id="' + f.id + '" class="uploadifive-button">' + f.buttonText + "</div>");
					if (f.buttonClass) {
						d.button.addClass(f.buttonClass);
					}
					d.button.css({
						height : f.height,
						"line-height" : f.height + "px",
						overflow : "hidden",
						position : "relative",
						"text-align" : "center",
						width : f.width
					});g.before(d.button).appendTo(d.button).hide();d.createInput.call(g);
					if (!f.queueID) {
						f.queueID = f.id + "-queue";
						d.queueEl = b('<div id="' + f.queueID + '" class="uploadifive-queue" />');d.button.after(d.queueEl);
					} else {
						d.queueEl = b("#" + f.queueID);
					}
					if (f.dnd) {
						var h = f.dropTarget ? b(f.dropTarget) : d.queueEl.get(0);
						h.addEventListener("dragleave", function(j) {
							j.preventDefault();j.stopPropagation();
						}, false);h.addEventListener("dragenter", function(j) {
							j.preventDefault();j.stopPropagation();
						}, false);h.addEventListener("dragover", function(j) {
							j.preventDefault();j.stopPropagation();
						}, false);h.addEventListener("drop", d.drop, false);
					}
					if (!XMLHttpRequest.prototype.sendAsBinary) {
						XMLHttpRequest.prototype.sendAsBinary = function(k) {
							function l(n) {
								return n.charCodeAt(0) & 255;
							}
							var m = Array.prototype.map.call(k, l);
							var j = new Uint8Array(m);
							this.send(j.buffer);
						};
					}
					if (typeof f.onInit === "function") {
						f.onInit.call(g);
					}
				} else {
					if (typeof f.onFallback === "function") {
						f.onFallback.call(g);
					}
					return false;
				}
			});
		},
		debug : function() {
			return this.each(function() {
				console.log(b(this).data("uploadifive"));
			});
		},
		clearQueue : function() {
			this.each(function() {
				var f = b(this),
					c = f.data("uploadifive"),
					e = c.settings;
				for (var d in c.inputs) {
					input = c.inputs[d];
					limit = input.files.length;
					for (i = 0; i < limit; i++) {
						file = input.files[i];a.cancel.call(f, file);
					}
				}
				if (typeof e.onClearQueue === "function") {
					e.onClearQueue.call(f, b("#" + c.settings.queueID));
				}
			});
		},
		cancel : function(d, c) {
			this.each(function() {
				var g = b(this),
					e = g.data("uploadifive"),
					f = e.settings;

				if (typeof d === "string") {
					if (!isNaN(d)) {
						fileID = "uploadifive-" + b(this).attr("id") + "-file-" + d;
					
					}
					d = b("#" + fileID).data("file");
				}
				d.skip = true;e.filesCancelled++;
				if (d.uploading) {
					e.uploads.current--;
					d.uploading = false;d.xhr.abort();
					delete d.xhr;
					a.upload.call(g);
				}
				if (b.inArray("onCancel", f.overrideEvents) < 0) {
					e.removeQueueItem(d, c);
				}
				if (typeof f.onCancel === "function") {
					f.onCancel.call(g, d);
				}
			});
		},
		upload : function(c, d) {
			this.each(function() {
				var h = b(this),
					e = h.data("uploadifive"),
					f = e.settings;
				if (c) {
					e.uploadFile.call(h, c);
				} else {
					if ((e.uploads.count + e.uploads.current) < f.uploadLimit || f.uploadLimit == 0) {
						if (!d) {
							e.uploads.attempted = 0;
							e.uploads.successsful = 0;
							e.uploads.errors = 0;
							var g = e.filesToUpload();
							if (typeof f.onUpload === "function") {
								f.onUpload.call(h, g);
							}
						}
						b("#" + f.queueID).find(".uploadifive-queue-item").not(".error, .complete").each(function() {
							_file = b(this).data("file");
							if ((e.uploads.current >= f.simUploadLimit && f.simUploadLimit !== 0) || (e.uploads.current >= f.uploadLimit && f.uploadLimit !== 0) || (e.uploads.count >= f.uploadLimit && f.uploadLimit !== 0)) {
								return false;
							}
							if (f.checkScript) {
								_file.checking = true;
								skipFile = e.checkExists(_file);
								_file.checking = false;
								if (!skipFile) {
									e.uploadFile(_file, true);
								}
							} else {
								e.uploadFile(_file, true);
							}
						});
						if (b("#" + f.queueID).find(".uploadifive-queue-item").not(".error, .complete").size() == 0) {
							e.queueComplete();
						}
					} else {
						if (e.uploads.current == 0) {
							if (b.inArray("onError", f.overrideEvents) < 0) {
								if (e.filesToUpload() > 0 && f.uploadLimit != 0) {
									alert("The maximum upload limit has been reached.");
								}
							}
							if (typeof f.onError === "function") {
								f.onError.call(h, "UPLOAD_LIMIT_EXCEEDED", e.filesToUpload());
							}
						}
					}
				}
			});
		},
		destroy : function() {
			this.each(function() {
				var e = b(this),
					c = e.data("uploadifive"),
					d = c.settings;
				a.clearQueue.call(e);
				if (!d.queueID) {
					b("#" + d.queueID).remove();
				}
				e.siblings("input").remove();e.show().insertBefore(c.button);c.button.remove();
				if (typeof d.onDestroy === "function") {
					d.onDestroy.call(e);
				}
			});
		}
	};
	b.fn.uploadifive = function(c) {
		if (a[c]) {
			return a[c].apply(this, Array.prototype.slice.call(arguments, 1));
		} else {
			if (typeof c === "object" || !c) {
				return a.init.apply(this, arguments);
			} else {
				b.error("The method " + c + " does not exist in $.uploadify");
			}
		}
	};
})(jQuery);

var deletedFiles = "";


//传入初始化参数
function uploadItem(){
	this.uploaderName = "";
	this.uploaderID = "";
	this.uploadControlID = "";
	this.uploadScriptURL = "";
	this.submitScriptURL = "";
	this.loadScriptURL = "";
	this.relationID = "";
	this.attachmentDisplayDiv = "";
	this.displayByRelationId = 1;
	this.displayByUploaderId = 0;
	this.displayByControlId = 0;
}

//执行初始化
function initAttachment(uploadItem)
{
	$('#'+uploadItem.uploadControlID).uploadifive({
		auto : true,
		multi : true,
		fileSizeLimit : 1024,
		showUploadedPercent : true,
		fileObjName : 'upload',
		showUploadedSize : true,
		removeTimeout : 9999999,
		uploadScript : uploadItem.uploadScriptURL,
		formData : {'uploaderID':uploadItem.uploaderID,
					'uploaderName':uploadItem.uploaderName,
					'uploadControlID':uploadItem.uploadControlID,
					'relationID':uploadItem.relationID,
					'fileName':''},

		onInit : function() {
			console.log('初始化');
		},
		onUpload : function() {
			$('#'+uploadItem.uploadControlID).data('uploadifive').settings.formData.fileName = file.name; 
		},
		onSelect : function() {
			//当前this是文件列表 修改formdata为文件名

        },
		onUploadComplete : function(file, data) {
			data = eval("(" + data + ")");
			if (data.result == "success") {
				$(file).attr("ID",data.ID);

				console.log('上传完成');
			} else {
				console.log("文件上传失败！");
			}
		},
		onCancel : function(file) {
			deletedFiles = deletedFiles + file.ID + ",";
			console.log(file);
		}
	});
	
	fillAttachment(uploadItem);

}

function fillAttachment(uploadItem){
	
	var a,b,c;
	if(displayByRelationId==1){
		a = uploadItem.relationID;
	}if(displayByUploaderId==1){
		b = uploadItem.uploaderID;
	}if(displayByControlId==1){
		c = uploadItem.uploadControlID;
	}
	$.ajax({
		url : uploadItem.loadScriptURL + "?ran=" + Math.random(),
		type : "POST",
		contentType : "application/x-www-form-urlencoded;charset=utf-8",
		data : {
			'uploadControlID':c,
			'uploaderID':b,
			'relationID':a
		},
		dataType : "text",
		async : false,
		success : function(data) {
			if (data != null && data != "") {
				var data = eval("(" + data + ")");
				if (data.result == "success") {
					console.log('读取成功');
					if(document.getElementById(uploadItem.attachmentDisplayDiv!=null&&uploadItem.attachmentDisplayDiv!="")){
						var displayDiv = document.getElementById(uploadItem.attachmentDisplayDiv);
						if(displayDiv){
							for(var i=0;i<data.data.length;i++){
								
							}
						}
					}
					
				}
			}
		},
		error : function(msg) {
			alert(msg);

		}
	})
}


//操作完毕 提交所有更改
function fileSubmit(uploadItem){
	$.ajax({
		url : uploadItem.submitScriptURL + "?ran=" + Math.random(),
		type : "POST",
		contentType : "application/x-www-form-urlencoded;charset=utf-8",
		data : {
			"deletedFiles" : deletedFiles,
			'uploaderID':uploadItem.uploaderID,
			'uploadControlID':uploadItem.uploadControlID,
			'relationID':uploadItem.relationID
		},
		dataType : "text",
		async : false,
		success : function(data) {
			if (data != null && data != "") {
				var data = eval("(" + data + ")");
				if (data.result == "success") {
					console.log('提交成功');
					return true;
				}
			}
		},
		error : function(msg) {
			alert(msg);
			return false;
		}
	})
}



