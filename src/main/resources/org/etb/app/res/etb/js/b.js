(function($) {
	// 正则验证
	$.validator.addMethod("regexp", function(value, element, pattern) {
		var regexp = new RegExp(pattern);
		return this.optional(element) || regexp.test(value);
	});
	
	
	// 字符验证       
	$.validator.addMethod("stringCheck", function(value, element) {       
	    return this.optional(element) || /^[\u0391-\uFFE5\w]+$/.test(value);       
	 });   
	 
	 // 中文字符长度      
	$.validator.addMethod("byteRangeLength", function(value, element, param) {       
	   var length = value.length;       
	   for(var i = 0; i < value.length; i++){       
	        if(value.charCodeAt(i) > 127){       
	         length++;       
	         }       
	   }       
	   return this.optional(element) || ( length >= param[0] && length <= param[1] );       
	 });   
	  
	// 身份证号码验证       
	$.validator.addMethod("idCard", function(value, element) {
		var pattern = /^((11|12|13|14|15|21|22|23|31|32|33|34|35|36|37|41|42|43|44|45|46|50|51|52|53|54|61|62|63|64|65|71|81|82|91)\d{4})((((19|20)(([02468][048])|([13579][26]))0229))|((20[0-9][0-9])|(19[0-9][0-9]))((((0[1-9])|(1[0-2]))((0[1-9])|(1\d)|(2[0-8])))|((((0[1,3-9])|(1[0-2]))(29|30))|(((0[13578])|(1[02]))31))))((\d{3}(x|X))|(\d{4}))$/;
		return this.optional(element) || pattern.test(value);       
	});
	    
	// 手机号码验证       
	$.validator.addMethod("mobile", function(value, element) {        
	   var mobile = /^((13[0-9])|(15[^4,\D])|(18[0,5-9]))\d{8}$/;
	   return this.optional(element) || (mobile.test(value));       
	});       
	    
	 // 电话号码验证       
	$.validator.addMethod("telephone", function(value, element) {       
		var tel = /^\d{3,4}-?\d{7,9}$/;
	    return this.optional(element) || (tel.test(value));       
	});   
	 
	// 联系电话(手机/电话皆可)验证   
	$.validator.addMethod("phone", function(value,element) {     
	    var mobile = /^((13[0-9])|(15[^4,\D])|(18[0,5-9]))\d{8}$/;
	    var tel = /^\d{3,4}-?\d{7,9}$/;
	    return this.optional(element) || (tel.test(value) || mobile.test(value));   
	 
	});
	      
	 // 邮政编码验证       
	 $.validator.addMethod("zipCode", function(value, element) {       
	     var tel = /^[0-9]{6}$/;       
	    return this.optional(element) || (tel.test(value));       
	 });
	 
	 
	 Tapestry.Initializer.Select2 = function(spec) {
		 var clientId = spec.clientId;
		 var placeholder = spec.placeholder;
		 var onSelectZone = spec.onSelectZone;
		 var onSelectParams = spec.onSelectParams;
		 var selectUrl = spec.selectUrl;
		 var data = spec.data;
		 var multiple = spec.multiple;
		 
		 var select = $('#' + clientId);
		 var selectText = $('#' + clientId + "_text");
		 
		 if(!data){
			 data = [];
		 }
		 
		 var options = {
		 	placeholder: placeholder,
		 	allowClear: true,
		 	data: data,
		 	multiple: multiple,
		 	escapeMarkup: function(m) { return m; }
		 };
 
		 select.select2(options).on('change', function(){
			var vs = select.val();
		 	var text = '';
		 	if(vs && data){
		 		var arr = vs.split(',');
		 		for(var i = 0; i < arr.length; i++){
		 			var v = arr[i];
		 			
		 			for(var k = 0; k < data.length; k++){
		 				var dv = data[k];
		 				var id = dv.id;
		 				if(v == id){
		 					if(text){
		 						text += ",";
		 					}
		 					text += dv.text;
		 					break;
		 				}
		 			}
		 		}
		 	}
		 	selectText.val(text);
		 	
		 	if(onSelectZone){
		 		var p = {};
				p["_select_value"] = select.val();
				p["_select_text"] = selectText.val();
				Etb.ajax({
					targetId: clientId,
					url: selectUrl,
					params: p,
					paramsId: onSelectParams,
					zone: onSelectZone
				});
		 	}
		 	
			$(this).closest('form').validate().element($(this));
		 });
	 }
	 
	 Tapestry.Initializer.Tooltip = function(spec) {
		 var clientId = spec.clientId;
		 var pos = spec.pos;
		 var title = spec.title;
		 var color = spec.color;
		 
		 var target = $('#' + clientId);
		 
		 target.addClass(color);
		 
		 target.tooltip({
			 title:title, 
			 html:false,
			 placement:pos
		 });
	 }
	 
	 Tapestry.Initializer.GritterNotify = function(spec) {
		 var color = spec.color;
		 var dark = spec.dark;
		 var title = spec.title;
		 var text = spec.text;
		 var image = spec.image
		 var sticky = spec.sticky;
		 
		 if(!color)
			 color = '';
		 
		 var className = dark ? color : color + ' gritter-light';
		 
		 $.gritter.add({
			title: title,
			text: text,
			image: image,
			sticky: sticky,
			time: '',
			class_name: className
		});
	 }
	 
	 $.widget("ui.dialog", $.extend({}, $.ui.dialog.prototype, {
		_title: function(title) {
			var $title = this.options.title || '&nbsp;'
			if( ("title_html" in this.options) && this.options.title_html == true )
				title.html($title);
			else title.text($title);
		}
	 }));
	 
	 Tapestry.Initializer.jQDialog = function(spec) {
	 	 $('.ui-dialog').children().remove();//避免一个未知的错误，当dialog打开第三次，dialog内部所有事件都不生效了
		 var clientId = spec.clientId;
		 var dialog = $('#' + clientId);
		 if(dialog.size() != 0){
			 dialog.remove();
		 }
		 
		 var title = spec.title;
		 var content = spec.content;
		 var buttons = spec.buttons;
		 var dialogDiv = $("<div id='" + clientId + "' class='hide'>" + content + "</div>");
		 dialogDiv.appendTo(document.body);
		 
		 var options = {
			modal: true,
			title: "<div class='widget-header widget-header-small'><h4 class='smaller'>" + title + "</h4></div>",
			title_html: true,
			closeText: ''
		};
		 
		 if(buttons){
			 options = $.extend(options, {
				 buttons: [{
					 html: "",
					 "class" : "hide"
				 }]
			 });
		 }
	 
		 dialogDiv.removeClass('hide').dialog(options);
		 
		 if(buttons){
			 $('.ui-dialog-buttonset', dialogDiv.parent()).html(buttons);
		 }else{
		 	$('.ui-dialog-buttonpane', dialog).remove();
		 }
	 }
	 
	 Tapestry.Initializer.jQDialog2 = function(spec) {
	 	 $('.ui-dialog').children().remove();//避免一个未知的错误，当dialog打开第三次，dialog内部所有事件都不生效了
	 	 var clientId = spec.clientId;
	 	 var width = spec.width;
	 	 var height = spec.height;
	 	 var phoneWidth = spec.phoneWidth;
	 	 var phoneHeight = spec.phoneHeight;
	 	 var resize = spec.resize;
	 	 
	 	 var phoneAccess = Etb.browser.phoneAccess();
	 	 if(phoneAccess){
	 	 	if(phoneWidth) width = phoneWidth;
	 	 	if(phoneHeight) height = phoneHeight;
	 	 }
	 	 
	 	 var content = $('#' + clientId + '_content');
	 	 var title = $('#' + clientId + '_title');
	 	 var buttons = $('#' + clientId + '_buttons');
	 	 
	 	 content.dialog({
			resizable: resize,
			modal: true,
			title: "<div class='widget-header widget-header-small'><h4 class='smaller'></h4></div>",
			title_html: true,
			closeText: '',
			width: width,
			height: height,
			buttons: [{
				html: "",
				"class" : "hide"
			}]
		});
		
		var dialog = content.closest('.ui-dialog');
		if(title){
			$('.widget-header', dialog).html(title);
		}
		
		if(buttons.html()){
			$('.ui-dialog-buttonset', dialog).html(buttons);
		}else{
		 	$('.ui-dialog-buttonpane', dialog).remove();
		}
	 }
	 
	 Tapestry.Initializer.jQDialogClose = function(spec){
		 var dialogId = spec.clientId;
		 $('#' + dialogId).dialog( "close" );
	 }
	 
	 Tapestry.Initializer.DialogClose = function(spec) {
		 var clientId = spec.clientId;
		 var dialogId = spec.dialogId;
		 
		 var button = $('#' + clientId);
		 
		 button.click(function(){
			 
			 if(dialogId){
				 $('#' + dialogId).dialog( "close" );
			 }else{
				 button.closest('.ui-dialog').children('.ui-dialog-content').dialog( "close" );
			 }
		 });
	 }
	 
	 Tapestry.Initializer.Pager = function(spec) {
			var clientId = spec.clientId;
			var zone = spec.zone;
			var paramsId = spec.paramsId;
			var enterUrl = spec.enterUrl;
			var selectUrl = spec.selectUrl;
			var limit = spec.limit;
			var defaultLimit = spec.defaultLimit;
			var defaultLimitOption = spec.defaultLimitOption;
			
			var input = $('#' + clientId + '_pager_input');
			input.bind('keydown', function (e) {
		        var key = e.which;
		        if (key == 13) {
		        	e.preventDefault();
		            var _paramsId = '';
		            if(typeof(paramsId) == "undefined"){
		                _paramsId = clientId + '_params';
		            }else{
		                _paramsId = paramsId + ',' + clientId + '_params';
		            }
		            Etb.ajax({
		            	url: enterUrl,
		            	params: {"requestCurrentPage2":input.val()},
		            	paramsId: _paramsId,
		            	zone:zone
		            });
		        }

		    });
			
			//设定每页显示行数
			var onePagerSize = $('#' + clientId + '_one_pager_size');
			var selectArr = [10, 30, 50];
			var selectOpt = '';
			var allReadyAdded = false;
			for(i = 0 ; i < selectArr.length ; i++) {
				if(defaultLimitOption == selectArr[i]) {
					allReadyAdded = true;
				}
				if(!allReadyAdded && defaultLimitOption != null && !isNaN(defaultLimitOption) && defaultLimitOption < selectArr[i]) {
					selectOpt += '<option>' + defaultLimitOption + "</option>";
					allReadyAdded = true;
				}
				selectOpt += '<option>' + selectArr[i] + "</option>";
			}
			onePagerSize.html(selectOpt);
			if(defaultLimit != undefined && defaultLimit != null) {
				onePagerSize.val(defaultLimit);
			}
			onePagerSize.bind('change', function (e) {
				var pId = paramsId ? paramsId + "," + clientId + "_params" : clientId + "_params";
		        Etb.ajax({
		        	url: selectUrl,
		        	params: {"requestPagerSize":onePagerSize.val()},
		        	paramsId: pId,
		        	zone:zone
		        });
		    });
		}
	 
	 	Tapestry.Initializer.RenderGridCheckbox = function(spec){
			var clientId = spec.clientId;
			var grid = jQuery('#' + clientId);
			var checkall = jQuery('.checkall', grid);
			if(checkall.size() != 0){
				checkall.click(function(){
					var checked = checkall.is(":checked");
					$("tbody tr td:nth-child(1) input[type=checkbox]", grid).prop("checked",checked);
				});
			}
		}

	 	Tapestry.Initializer.GridCheckedRows = function(clientId) {
			var grid = jQuery('#' + clientId);
			
			var rowIds = "";
			var i = 0;
			
			$("tbody tr td:nth-child(1) input[type=checkbox]:checked", grid).each(function(){
				if (i++ > 0){
					rowIds += ",";
				}
				var rowid = jQuery(this).attr('rowid');
				if(rowid){
					rowIds += rowid;
				}
			});
			return rowIds;
		};
		
		Tapestry.Initializer.BErrors = function(spec){
			var clientId = spec.clientId;
			var errors = $('#' + clientId);
			$('i.icon-remove', errors).on('click',function(){
				errors.hide();
			});
			
			var ul = $("ul.list-unstyled", errors);
			errors.on("addError", function(e, text){
				ul.append("<li>" + text + "</li>");
				
				if(!errors.is(':visible')){
					errors.show();
				}
			});
			
			errors.on("validateHasError", function(e){
				if($("li", ul).size() == 0){
					errors.hide();
				}else{
					errors.show();
				}
			});
			
			errors.on("removeAllError", function(e){
				$("li", ul).remove();
			});
		}
		
		Tapestry.Initializer.BRadioGroup = function(spec){
			var clientId = spec.clientId;
			var zone = spec.zone;
			var paramsId = spec.params;
			var url = spec.url;
			
			if(url){
				var inner = $('#' + clientId + '_inner');
				$("input[type=radio]", inner).click(function(){
					var radio = $(this);
					var params = {"_radio_value": radio.val()};
					Etb.ajax({
						url: url,
						zone: zone,
						paramsId: paramsId,
						params: params
					});
				});
			}
		}
		
		Tapestry.Initializer.WidgetBox = function(spec){
			var clientId = spec.clientId;
			var reloadUrl = spec.reloadUrl;
			var reloadZone = spec.reloadZone;
			var reloadParams = spec.reloadParams;
			var closeUrl = spec.closeUrl;
			var closeZone = spec.closeZone;
			var closeParams = spec.closeParams;
			var box = $('#' + clientId);
			if(reloadUrl){
				box.on('reload.ace.widget', function(e){
					e.preventDefault();
					$('.widget-toolbar a[data-action="reload"]', box).blur();
					
					var $remove = false;
					if(box.css('position') == 'static') {
						$remove = true; 
						box.addClass('position-relative');
					}
					box.append('<div class="widget-box-overlay"><i class="icon-spinner icon-spin icon-2x white"></i></div>');
					Etb.ajax({
						url : reloadUrl,
						zone : reloadZone,
						paramsId: reloadParams,
						cleanup: function(){
							box.find('.widget-box-overlay').remove();
							if($remove){
								box.removeClass('position-relative');
							}
						}
					});
				});
			}
			
			if(closeUrl){
				box.on('close.ace.widget', function(){
					Etb.ajax({
						url : closeUrl,
						zone : closeZone,
						paramsId: closeParams
					});
				});
			}
		};
		
		Tapestry.Initializer.Upload = function(spec){
			var clientId = spec.clientId;
			var whitelist = spec.whitelist;
			var limitSize = spec.limitSize;
			var limit = spec.limit;
			var url = spec.url;
			var swfUrl = spec.swfUrl;
			var xapUrl = spec.xapUrl;
			var data = spec.data;
			
			var select = $('#' + clientId + '_select');
			var start = $('#' + clientId + '_start');
			var uploadlist = $('#' + clientId + '_uploadlist');
			
			var input = $('#' + clientId);
			var inputText = $('#' + clientId + '_text');
			
			if(data){
				var idStr = "";
				var textStr = "";
				for(var i = 0; i < data.length; i++){
					if(i > 0){
						idStr += "?";
						textStr += "?";
					}
					var item = data[i];
					idStr += item.id;
					textStr += item.text;
					
					var fileIcon = 'icon-file';
					var filename = item.text;
					if((/\.(jpe?g|png|gif|svg|bmp|tiff?)$/i).test(filename)) {
						fileIcon = 'icon-picture';
					} else if((/\.(mpe?g|flv|mov|avi|swf|mp4|mkv|webm|wmv|3gp)$/i).test(filename)){
						fileIcon = 'icon-film';
					} else if((/\.(mp3|ogg|wav|wma|amr|aac)$/i).test(filename)){
						fileIcon = 'icon-music';
					}
					var uploaditem = $("<div class='uploaditem' id='" + clientId + "_files_" + item.id + "'>"
						+ "<div class='detail'>"
						+ "<i class='" + fileIcon + "'></i>"
						+ item.text
						+ "&nbsp;&nbsp;<a>删除</a></div></div>");
					uploaditem.data("id",item.id);
					uploaditem.data("text",item.text);
					uploadlist.append(uploaditem);	
					
					$("a", uploaditem).click(function(){
						var upitem = $(this).closest(".uploaditem");
		        		var value = new StringList(input.val(), "?");
						value.remove(upitem.data("id"));
						input.val(value.toString());
						var text = new StringList(inputText.val(), "?");
						text.remove(upitem.data("text"));
						inputText.val(text.toString());
						upitem.remove();
					});
				}
				input.val(idStr);
				inputText.val(textStr);
			}
			
			var filters = {};
			if(limitSize){
				filters = $.extend(filters, {max_file_size : limitSize});
			}
			
			if(whitelist){
				filters = $.extend(filters, {
					mime_types: [
			            {title : whitelist, extensions : whitelist}
			        ]
			    });
			}
			
			var uploader = new plupload.Uploader({
			    runtimes : 'html5,flash,silverlight,html4',
			     
			    browse_button : clientId + '_select',
			    container: clientId + "_container",
			     
			    url : url,
			    multi_selection : false,
			    filters : filters,
			 
			    // Flash settings
			    flash_swf_url : swfUrl,
			 
			    // Silverlight settings
			    silverlight_xap_url : xapUrl,
			     
			 
			    init: {
			        PostInit: function() {
			        },
			 
			        FilesAdded: function(up, files) {
			        	if(limit){//不为0
			        		var items = $(".uploaditem", uploadlist);
			        		var more = items.size() + files.length - limit;
			        		if(more > 0){
			        			//up.files.length
			        			alert("上传文件最多" + limit + "个");
			        			for(var i = 0; i < files.length; i++){
			        				up.removeFile(files[i]);
			        			}
			        			return;
			        		}
			        	}
			        	
						$.each(up.files, function (i, file) {
							var item = $('#' + clientId + '_' + file.id);
							
							if(item.size() == 0){
								var fileIcon = 'icon-file';
								var filename = file.name;
								if((/\.(jpe?g|png|gif|svg|bmp|tiff?)$/i).test(filename)) {
									fileIcon = 'icon-picture';
								} else if((/\.(mpe?g|flv|mov|avi|swf|mp4|mkv|webm|wmv|3gp)$/i).test(filename)){
									fileIcon = 'icon-film';
								} else if((/\.(mp3|ogg|wav|wma|amr|aac)$/i).test(filename)){
									fileIcon = 'icon-music';
								}
								uploadlist.append("<div class='uploaditem' id='" + clientId + "_" + file.id + "'>"
									+ "<div id='" + clientId + "_" + file.id + "_progress' class='mb5 h5'></div>"
									+ "<div class='detail'><i class='" + fileIcon + "'></i>" + file.name + "&nbsp;&nbsp;<a>取消</a></div></div>");
								
								$('#' + clientId + "_" + file.id + ' a').click(function(){
									$(this).closest('.uploaditem').remove();
									up.removeFile(file);
								});
								var progress = $('#' + clientId + '_' + file.id + '_progress');
								progress.progressbar({
									value: 0,
									create: function( event, ui ) {
										$(this).addClass('progress progress-striped active')
											   .children(0).addClass('progress-bar progress-bar-success');
									}
								});
								file.progress = progress;
							}
					    });
			        },
			 
			        UploadProgress: function(up, file) {
			        	var progress = file.progress;
			        	progress.progressbar({value:file.percent});
			        },
			 
			        Error: function(up, err) {
			        	var msg;
			        	if(err.code == -600){
			        		msg = "文件不能大于" + limitSize;
			        	}else{
			        		msg = err.message;
			        	}
			        	alert(msg);
			        },
			        
			        UploadComplete: function(up, file){
			        },
			        
			        FileUploaded: function(up, file, res){
			        	var json = $.parseJSON(res.response);
			        	var item = $('#' + clientId + '_' + file.id);
			        	$('.ui-progressbar', item).remove();
			        	$('a', item).remove();
			        	
			        	var detail = $('.detail', item);
			        	detail.append("&nbsp;<a>删除</a>");
			        	
			        	var del = $("a", detail);
			        	del.click(function(){
			        		up.removeFile(file);
			        		item.remove();
			        		
			        		var value = new StringList(input.val(), "?");
							value.remove(json.fileId);
							input.val(value.toString());
							var text = new StringList(inputText.val(), "?");
							text.remove(file.name);
							inputText.val(text.toString());
			        	});
			        	detail.append(del);

						var value = new StringList(input.val(), "?");
						value.add(json.fileId);
						input.val(value.toString());
						var text = new StringList(inputText.val(), "?");
						text.add(file.name);
						inputText.val(text.toString());
			        }
			    }
			});
			 
			uploader.init();
			
			start.click(function(){
				uploader.start();
			});
		};
		
		Tapestry.Initializer.Download = function(spec){
			var iframeId = spec.iframeId;
			var iframe = document.getElementById(iframeId);
			if(!iframe){
				iframe = document.createElement('IFRAME');
				iframe.id = iframeId;
				iframe.style.display = 'none';
				document.body.appendChild(iframe);
			}
		};
		
		Tapestry.Initializer.DownloadTrigger = function(spec){
			var iframeId = spec.iframeId;
			var iframe = document.getElementById(iframeId);
			
			var url = spec.url;
			iframe.contentWindow.location.href = url;
		};
		
		Tapestry.Initializer.AudioPlayer = function(spec){
			var clientId = spec.clientId;
			var swfPath = spec.swfPath;
			var url = spec.url;
			
			var solution;
			if(typeof(Worker) != "undefined"){
				solution = "html";
			}else{
				solution = "flash";
			}
			$("#" + clientId).jPlayer({
				ready: function () {
					$(this).jPlayer("setMedia", {
						mp3: url
					});
				},
				solution: solution,
				cssSelectorAncestor: "#" + clientId + "_container",
				swfPath: swfPath,
				supplied: "mp3",
				wmode: "window",
				smoothPlayBar: true,
				keyEnabled: true,
				remainingDuration: false,
				toggleDuration: true
			});
		};
		
		Tapestry.Initializer.XHTextEditor = function(spec) {
			var clientId = spec.richTextId;
			var editor_tools = spec.tools;
		
		    var textaid = jQuery('#' + clientId);
		    var form = textaid.closest("form");

		    //不可编辑
		    if(spec.disabled) {
		    	return;
		    }
		    
			var plugins = {};
		    var editor = textaid.xheditor({
		    	tools:editor_tools,
		    	skin:'default',
				clientId: clientId,
		    	internalStyle:false,
		    	width:spec.width,
		    	height:spec.height,
		    	plugins:plugins
		    });
		
		    form[0].observe(Tapestry.FORM_VALIDATE_EVENT, function() {
		        var str = editor.getSource();
		        textaid.val(str);
		
		    });
		
			//修正高度
			var span = textaid.next().next();
			var tool_height = $(".xheTool", span).height();
			$(".xheIframeArea", span).height(spec.height - tool_height);
		};
		
		Tapestry.Initializer.BSwitch = function(spec) {
			var clientId = spec.clientId;
			var zoneId = spec.zoneId;
			var paramsId = spec.paramsId;
			var url = spec.url;
			
			
			var input = $('#' + clientId);
			if(zoneId){
				input.click(function(){
					Etb.ajax({
						targetId : clientId,
						url : url,
						paramsId : paramsId,
						zone : zoneId,
						params: {"_switchValue": input.prop("checked")}
					});
				});
			}
		};
		
}(window.jQuery));

var StringList = function(str, split){
	var data = {};
	if(str && split){
		var arr = str.split(split);
		for(var i = 0; i < arr.length; i++){
			var v = arr[i].trim();
			if(v){
				data[v] = true;
			}
		}
	}
	
	return {
        add: function (key) {
            if( key != undefined ){
            	data[key] = true;
            }
        },
        remove: function (key) {
            delete data[key];
        },
        clear: function() {
            data = {};
        },
        toString: function(){
        	var text = "";
        	for(var key in data){
        		if(text) text += split;
        		
        		text += key;
        	}
        	return text;
        }
    };
};

Etb.etbFormSubmit = function(formId){
	var form = $(formId);
	if(form.ajaxSubmit){
		jQuery(form).trigger("validateFormSubmit");
	}else{
		form.submit();
	}
};

Tapestry.Initializer.formEventManager = function (spec) {
	$T(spec.formId).formEventManager = new Tapestry.FormEventManager(spec);
	
	var formError = spec.formError;
	if("ERRORS" == formError){
		var errorsId = spec.errorsId;
		if(!errorsId){
			Tapestry.error('当formError为ERRORS时，必须绑定errorsId');
			return;
		}
		var errors = jQuery('#' + errorsId);
		jQuery('#' + spec.formId).validate({
			ignore: "",//隐藏域也需要验证
			focusInvalid: false,
			highlight: function (e) {
			},
			success: function (e, element) {
			},
			errorPlacement: function (error, element) {
			},
			submitHandler:function(form){
				Etb.etbFormSubmit(spec.formId);
		    },
		    invalidHandler:function(form, validator) {
		    	errors.trigger("removeAllError");
		    	
		    	var errorMsg = "";
		    			
				for(var key in validator.invalid){
					var value = validator.invalid[key];
					if(value)
						errors.trigger("addError", [value]);
				}
				
				errors.trigger("validateHasError");
		    }
		});
	}else if("NOTIFY" == formError){
		jQuery('#' + spec.formId).validate({
			ignore: "",//隐藏域也需要验证
			focusInvalid: false,
			highlight: function (e) {
			},
			success: function (e, element) {
			},
			errorPlacement: function (error, element) {
			},
			submitHandler:function(form){
				Etb.etbFormSubmit(spec.formId);
		    },
		    invalidHandler:function(form, validator) {
		    	var errorMsg = "";
		    			
				for(var key in validator.invalid){
					var value = validator.invalid[key];
					if(value)
						errorMsg += value + "<br/>";
				}
				
		    	if(errorMsg){
		    		Tapestry.Initializer.GritterNotify({
		    			title: "请修正以下错误后继续.",
		    			text: errorMsg,
		    			color: 'gritter-error'
		    		});
		    	}
		    }
		});
	}else{
		jQuery('#' + spec.formId).validate({
			ignore: "",//隐藏域也需要验证
			errorElement: 'div',
			errorClass: 'help-block',
			focusInvalid: false,
			highlight: function (e) {
				jQuery(e).closest('.form-group').removeClass('has-info').addClass('has-error');
			},
	
			success: function (e) {
				jQuery(e).closest('.form-group').removeClass('has-error').addClass('has-info');
				jQuery(e).remove();
			},
	
			errorPlacement: function (error, element) {
				if(element.is(':checkbox') || element.is(':radio')) {
					var controls = element.closest('div[class*="col-"]');
					if(controls.find(':checkbox,:radio').length > 1) controls.append(error);
					else error.insertAfter(element.nextAll('.lbl:eq(0)').eq(0));
				}
				else if(element.is('.select2')) {
					error.insertAfter(element);
				}
				else if(element.is('.chosen-select')) {
					error.insertAfter(element.siblings('[class*="chosen-container"]:eq(0)'));
				}
				else error.insertAfter(element.parent());
			},
			
			submitHandler:function(form){
				Etb.etbFormSubmit(spec.formId);
		    }
		});
	}
};

Tapestry.Initializer.EtbForm = function(spec) {
	var jQForm = jQuery('#' + spec.clientId);
	var form = $(spec.clientId);
	
	//标志位ajax提交
	form.ajaxSubmit = true;
	
	jQForm.on('validateFormSubmit', function(e){
		e.preventDefault();
		
		//donothing, for custom handler
		jQForm.triggerHandler('beforeSubmit');
		
		var json = Etb.formSerialize(spec.clientId);

		if (!form.option)
			form.option = {};
		
		if(form.extraParams){
			json = jQuery.extend(form.extraParams, json); 
		}

		jQuery.extend(form.option,{
			targetId : spec.clientId,
			url : spec.url,
			params : json,
			zone : spec.zoneId,
			paramsId : spec.paramsId,
			afterHandle : function(ret) {
				form.extraParams = {};
			}
		});

		Etb.ajax(form.option);
	});
};

Tapestry.Initializer.FormSubmit = function(spec) {
	var clientId = spec.clientId;
	var formId = spec.formId;
	var key = spec.key;
	var value = spec.value;
	
	var target = jQuery('#' + clientId);
	var form;
	if(formId){
		form = jQuery('#' + formId);
	}else{
		form = target.closest('form');
	}
	
	target.click(function() {
		form[0].extraParams = {};
		if(key){
			form[0].extraParams[key] = value;
		}
		jQuery('input[type=submit]', form).click();
	});
}


Tapestry.Initializer.validate = function (masterSpec) {
    $H(masterSpec)
        .each(
        function (pair) {

            var field = $(pair.key);

            /*
             * Force the creation of the field event
             * manager.
             */

//            $(field).getFieldEventManager(); [*]Alex

            $A(pair.value)
                .each(function (spec) {
                    /*
                     * Each pair value is an array of specs, each spec is a 2 or 3 element array. validator function name, message, optional constraint
                     */

                    var name = spec[0];
                    var message = spec[1];
                    var constraint = spec[2];

                    var vfunc = Tapestry.Validator[name];

                    if (vfunc == undefined) {
                        Tapestry
                            .error(Tapestry.Messages.missingValidator, {
                            name: name,
                            fieldName: field.id
                        });
                        return;
                    }

                    /*
                     * Pass the extended field, the provided message, and the constraint object to the Tapestry.Validator function, so that it can, typically, invoke field.addValidator().
                     */
                    vfunc.call(this, field, message, constraint);
                });
        });
};

Tapestry.Validator = {

	required : function(field, message) {
		var f = jQuery("#" + field.id);
		if(f.hasClass("checkboxes")){
			var name = jQuery(".checkboxes-container input[type=checkbox]",f).eq(0).attr("name");
			var settings = jQuery.data(f.closest('form')[0]).validator.settings;
			settings.rules[name] = {
				required : true
			};
			
			settings.messages[name] = {
				required : message
			};
		}else if(f.hasClass("radiogroup")){
			var name = jQuery(".radiogroup-container input[type=radio]",f).eq(0).attr("name");
			var settings = jQuery.data(f.closest('form')[0]).validator.settings;
			settings.rules[name] = {
				required : true
			};
			
			settings.messages[name] = {
				required : message
			};
		}else {
			f.rules("add", {
				required : true,
				messages : {
					required : message
				}
			});
		}
	},

	/** Supplies a client-side numeric translator for the field. */
	numericformat : function(field, message, isInteger) {
		var f = jQuery("#" + field.id);
		if(isInteger){
			f.rules("add", {
				digits : true,
				messages : {
					digits : message
				}
			});
		}else{
			f.rules("add", {
				number : true,
				messages : {
					number : message
				}
			});
		}
	},

	minlength : function(field, message, length) {	
		jQuery("#" + field.id).rules("add", {
			minlength: length,
			messages : {
				minlength : message
			}
		});
	},

	maxlength : function(field, message, maxlength) {
		jQuery("#" + field.id).rules("add", {
			maxlength: maxlength,
			messages : {
				maxlength : message
			}
		});
	},

	min : function(field, message, minValue) {		
		jQuery("#" + field.id).rules("add", {
			min: minValue,
			messages : {
				min : message
			}
		});
	},

	max : function(field, message, maxValue) {
		jQuery("#" + field.id).rules("add", {
			max: maxValue,
			messages : {
				max : message
			}
		});
	},

	regexp : function(field, message, pattern) {
		jQuery("#" + field.id).rules("add", {
			regexp: pattern,
			messages : {
				regexp : message
			}
		});
	},
	
	email : function(field, message) {
		jQuery("#" + field.id).rules("add", {
			email: true,
			messages : {
				email : message
			}
		});
	},
	
	stringCheck : function(field, message) {
		jQuery("#" + field.id).rules("add", {
			stringCheck: true,
			messages : {
				stringCheck : message
			}
		});
	},
	
	byteRangeLength : function(field, message, arrange) {
		jQuery("#" + field.id).rules("add", {
			byteRangeLength: [arrange["min"],arrange["max"]],
			messages : {
				byteRangeLength : message
			}
		});
	},
	
	idCard : function(field, message) {
		jQuery("#" + field.id).rules("add", {
			idCard: true,
			messages : {
				idCard : message
			}
		});
	},
	
	mobile : function(field, message) {
		jQuery("#" + field.id).rules("add", {
			mobile: true,
			messages : {
				mobile : message
			}
		});
	},
	
	telephone : function(field, message) {
		jQuery("#" + field.id).rules("add", {
			telephone: true,
			messages : {
				telephone : message
			}
		});
	},
	
	phone : function(field, message) {
		jQuery("#" + field.id).rules("add", {
			phone: true,
			messages : {
				phone : message
			}
		});
	},
	
	zipCode : function(field, message) {
		jQuery("#" + field.id).rules("add", {
			zipCode: true,
			messages : {
				zipCode : message
			}
		});
	}
	
};