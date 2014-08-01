(function($) {
	if (window.Etb == undefined) {
		window.Etb = {};
	}
	var Etb = window.Etb;
	
	Etb.defaultAjaxOption = {
		targetId: null,	//这个id用于支持zone = "^"的用法
		url: null,
		params: null,
		async: true, //是否异步请求
		type: "POST",
		dataType: null,
		paramsId: null,
		zone: "#",
		purge: false, //是否清理内存
		showBeforeInit: true,
		setup: null,
		beforeSend: null,
		beforeHandle: null,
		afterHandle: null,
		cleanup: null,
		error: null
	};
	
	Etb.ajax = function(option) {
		option = $.extend({}, Etb.defaultAjaxOption, option);
			
		var targetId = option.targetId;
		var url = option.url;
		var params = option.params;
		var type = option.type;
		var async = option.async;
		var dataType = option.dataType;
		var paramsId = option.paramsId;
		var zone = option.zone;
		var error = option.error;
		var purge = option.purge;
		var effectColor = option.effectColor;
		var showBeforeInit = option.showBeforeInit;
		
		option.setup && option.setup();
		
		Etb.ajax.showLoading();
		
		if("^" == zone)
			zone = option.zone = $('#'+targetId).parent('.t-zone').attr('id');
			
		option.beforeSend && option.beforeSend();
		
		var ajaxOptions = {
			type: type,
			dataType: dataType,
			traditional: true,//保证数据参数a[] -> a
			async: async,
			data: Etb.joinParam(params, paramsId, zone),
			error: function(XMLHttpRequest, textStatus, errorThrown){
				
				Tapestry.error('#{url},#{textStatus}:#{message}.', {
					url : this.url,
					textStatus : textStatus,
					message : errorThrown.message
				});
				
				Etb.ajax.hideLoading();
				
				this.cleanup && this.cleanup(ret, textStatus, jqXHR);
			},
			success: function(ret, textStatus, jqXHR){
				try{
					Etb.ajaxSuccessHandler(option, ret, textStatus, jqXHR);
				}catch(e){
					console.error(e);
					
					//以下不要在finally里执行
					Etb.ajax.hideLoading();
					this.cleanup && this.cleanup(ret, textStatus, jqXHR);
				}
			}
		};
		 
		$.ajax(url, ajaxOptions);
	};
	
	Etb.ajax.showLoading = function(){
		if(!Etb.browser.phoneAccess()){
			$("body").showLoading({"showLoadingIcon": false});
		}
//		$("body").showLoading({"hPos":"right","vPos":"top"});
		
	};
	
	Etb.ajax.hideLoading = function(){
		if(!Etb.browser.phoneAccess()){
			$("body").hideLoading();
		}
	};
	
	Etb.ajaxSuccessHandler = function(option, ret, textStatus, jqXHR){
		var zone = option.zone;
		var purge = option.purge;
		var showBeforeInit = option.showBeforeInit;
		
		option.beforeHandle && option.beforeHandle(ret, textStatus, jqXHR);
				
		if(!ret) return;
		
		ret.zoneId = zone;
		
		if (ret.redirectURL) {
			window.location.href = ret.redirectURL;
			return;
		}
		
		var zoneIds = [];
		
		// 刷新zone
		if(ret.zones){
			$.each(ret.zones, function(zoneId, zoneContent) {
				Etb.prepareZoneContent(zoneId, zoneContent, zoneIds, showBeforeInit);
			});
		}
		
		// block的刷新zone
		if(ret.zoneId && ret.content != null){
			Etb.prepareZoneContent(ret.zoneId, ret.content, zoneIds, showBeforeInit);
		}
		
		// 请求当前页面未载入的css文件
		Tapestry.ScriptManager.addStylesheets(ret.stylesheets);
		
		//请求当前页面未载入的js文件,并执行所有初始化js
		Tapestry.ScriptManager.addScripts(ret.scripts, function () {
			Tapestry.executeInits(ret.inits);
			Etb.showZoneContent(zoneIds, purge, showBeforeInit);
			option.afterHandle && option.afterHandle(ret, textStatus, jqXHR);
			Etb.ajax.hideLoading();
			option.cleanup && option.cleanup(ret, textStatus, jqXHR);
		});
	}
				
	
	/**先将zone内容放在页面隐藏区内，等待初始化*/
	Etb.prepareZoneContent = function(zoneId, content, zoneIds, showBeforeInit){
		//特殊标志，用于仅为触发ajax而非renderZone的场景
		if("#" == zoneId)
			return;
			
		var element = $('#'+zoneId);
		//判断zone是否存在
		var zoneManager = Tapestry.findZoneManagerForZone(zoneId);
		
		if(content){
			zoneIds.push(zoneId);
			if(!showBeforeInit)
				element.css("visibility", "hidden");
			
			element.children().each(function(){
				if("SELECT" == this.tagName){
					$(this).remove();	
				}else{
					this.remove();
				}
			});
			
			element.html(content);
			
			var func = zoneManager.element.visible() ? zoneManager.updateFunc : zoneManager.showFunc;
			var hiddenEffect = func.call(zoneManager);
			if(hiddenEffect) element.css("display", "none");	
			zoneManager.effectFunc = func;
		}else{
			Etb.effect(element, "fade", null, function(){
				element.html(content);
			});
		}
	};
	
	/**显示初始化好后的zoneContent*/
	Etb.showZoneContent = function(zoneIds, purge, showBeforeInit){
		$(zoneIds).each(function(index, zoneId){
			
			var element = $('#' + zoneId);
			
			Etb.reload(zoneId);
			
			var zoneManager = Tapestry.findZoneManagerForZone(zoneId);
			
			if(purge){
		 		Tapestry.purgeChildren(zoneManager.updateElement); //t5的内存清理
			}
			if(!showBeforeInit)
				element.css("visibility", "visible");
			
			//zone更新的动画效果，基于prototype的，先去掉
			var func = zoneManager.effectFunc;
			func.call(zoneManager, zoneManager.element);
		 	
		 	//TODO prototype的事件触发,以后干掉，目前有artDialog组件用到 
			zoneManager.element.fire(Tapestry.ZONE_UPDATED_EVENT);
			 
			//$的事件触发
			element.trigger(Tapestry.ZONE_UPDATED_EVENT);
		}); 
	};
	
	Etb.effect = function(element, effectType, options, callback){
		if(!element){//这里对于需要先隐藏再显示的效果提供了初始化条件
			if("highlight" == effectType){
				return false;
			}else if("show" == effectType){
				return true;
			}else if("slidedown" == effectType){
				return true;
			}else if("slideup" == effectType){
				return false;
			}else if("fade" == effectType){
				return false;
			}
		}
		
		if(!element.effect)
			element = $(element);
			
		var effecting = element.attr("effecting");
		if(effecting && "" != effecting){
			if(callback) callback();
			return;
		}
		element.attr("effecting", "true");
		
		var callbackHandler = function(){
			element.attr("effecting", "");
			if(callback) callback();
		};
		
		effectType = effectType.toLowerCase();
		if("highlight" == effectType){
			//修正table元素的highlight刷新错误
			var effectElement = element;
			var loopElement = element;
			while(loopElement.children().size() == 1){
				loopElement = loopElement.children().eq(0);
				if(loopElement.is("table")){
					effectElement = $("td", loopElement);
					break;
				}
			}
			effectElement.effect("highlight", options, 1000, callbackHandler);
		}else if("show" == effectType){
			element.fadeIn(callbackHandler);
		}else if("slidedown" == effectType){
			element.slideDown(600, callbackHandler);
		}else if("slideup" == effectType){
			element.slideUp(callbackHandler);
		}else if("fade" == effectType){
			element.fadeOut(callbackHandler);
		}
		return element;
	}
	
	//重写T5 Effect效果，指向Etb.effect
	Tapestry.ElementEffect = {

	    /** Fades in the element. */
	    show: function (element) {
	    	return Etb.effect(element, "show");
	    },
	
	    /** The classic yellow background fade. */
	    highlight: function (element, color) {
	    	var option = {"color" : color, "mode" : "update"};
	        return Etb.effect(element, "highlight", option);
	    },
	
	    /** Scrolls the content down. */
	    slidedown: function (element) {
	        return Etb.effect(element, "slidedown");
	    },
	
	    /** Slids the content back up (opposite of slidedown). */
	    slideup: function (element) {
	        return Etb.effect(element, "slideup");
	    },
	
	    /** Fades the content out (opposite of show). */
	    fade: function (element) {
	        return Etb.effect(element, "fade");
	    },
	
	    none: function (element) {
	    	return element;
	    }
	};
	
	/**
	 * 自定义的内存清理
	 */
	Etb.purge = function(element){	
		if(!element)
			return;
			
		var children = element.children();
		children.each(function(){
			Etb.purge($(this));
		});
		$.event.remove(element);
		$.cleanData(element);
		children = null;
		element = null;
	};
	
	/** 用于储存参数js*/
	Etb.Params = {};
	
	/**
	 * 拼接参数
	 */
	Etb.joinParam = function(params, paramsId, zoneId){
			
		if(!params){
			params = new Object();
		}
		
		//支持Params
		if(paramsId){
			var p = paramsId.split(",")
			for(var i = 0; i < p.length; i++){
				paramsId = p[i]
				if(!paramsId || "null" == paramsId)
					continue;
				
				var array = Etb.Params[paramsId];
				if(!array){
					Tapestry.error('[' + paramsId + ']' + "Params不存在!");
					return;
				}
				
				$.each(array, function(i, item){
					try{
						var v;
						if(item.serialize){
							v = item.value;
						}else {
							v = eval(item.value).toString();
						}
						if(v){
							params[item.key] = v;
						}
					}catch(e){}
				});
			}
			
		}
		
		//支持block刷新
		if(zoneId && "#" != zoneId){
			var ZONEKEY = "t:zoneid";
			var manager = Tapestry.findZoneManagerForZone(zoneId);
			if(!manager){
				Tapestry.error('[' + zoneId + ']' + "Zone不存在!");
				return;
			}
			
			var parameters = manager.specParameters;
			
			params[ZONEKEY] = zoneId;
			$.extend(params, parameters);
		}
		
		return params;
	};
	
	//自定义的表单元素序列化
	Etb.formSerialize = function(form) {
		var json = {};
		if(!form) return json;
		
		if(typeof form == 'string'){
			form = $('#'+form);
		}
		
		var elements = form.find('*');
		elements.each(function(index, item){
			var tag = item.tagName.toLowerCase();
			var type = item.type ? item.type.toLowerCase() : undefined;
			if(('input' == tag && 'submit' != type) 
				|| 'select' == tag 
				|| 'textarea' == tag){
				if (item.name && item.getValue()) {
					if (!json[item.name])
						json[item.name] = [];
	
					json[item.name].push(item.getValue());
				}
			}
		});
		return json;
	};
	
	Etb.reload = function(zoneId){
		//donothing, for extends
	};
	
	Etb.browser = {};
	
	Etb.browser.getIEVersion = function() {
		var rv = -1;
		if (navigator.appName == 'Microsoft Internet Explorer') {
			var ua = navigator.userAgent;
			var re = new RegExp('MSIE ([0-9]{1,}[\.0-9]{0,})');
	
			if (re.exec(ua) != null) {
				rv = parseFloat(RegExp.$1);
			}
		}
		return rv;
	};
	
	//是否移动端访问
	Etb.browser.phoneAccess = function(){
//		var sUserAgent = navigator.userAgent.toLowerCase();  
//	    var bIsIpad = sUserAgent.match(/ipad/i) == "ipad";  
//	    var bIsIphoneOs = sUserAgent.match(/iphone os/i) == "iphone os";  
//	    var bIsMidp = sUserAgent.match(/midp/i) == "midp";  
//	    var bIsUc7 = sUserAgent.match(/rv:1.2.3.4/i) == "rv:1.2.3.4";  
//	    var bIsUc = sUserAgent.match(/ucweb/i) == "ucweb";  
//	    var bIsAndroid = sUserAgent.match(/android/i) == "android";  
//	    var bIsCE = sUserAgent.match(/windows ce/i) == "windows ce";  
//	    var bIsWM = sUserAgent.match(/windows mobile/i) == "windows mobile";  
//	    return bIsIpad || bIsIphoneOs || bIsMidp || bIsUc7 || bIsUc || bIsAndroid || bIsCE || bIsWM;
		//TODO 暂时先按照宽度判断
		return document.body.scrollWidth < 768;
	};
	
	//让$ 1.9.1支持browser属性
	if(!$.browser){
		$.browser = new Object();
		$.browser.version = Etb.browser.getIEVersion();//要验证版本基本上都是ie了，鄙视
		$.browser.msie = /msie/.test(navigator.userAgent.toLowerCase());
		$.browser.mozilla = /firefox/.test(navigator.userAgent.toLowerCase());
		$.browser.webkit = /webkit/.test(navigator.userAgent.toLowerCase());
		$.browser.opera = /opera/.test(navigator.userAgent.toLowerCase());
		$.browser.chrome = /chrome/.test(navigator.userAgent.toLowerCase());
	}
})(jQuery);