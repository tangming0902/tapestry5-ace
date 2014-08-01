(function($) {
	//js Date增强
	Date.prototype.format = function(mask) {     
	    
	    var d = this;     
	    
	    var zeroize = function (value, length) {     
	    
	        if (!length) length = 2;     
	    
	        value = String(value);     
	    
	        for (var i = 0, zeros = ''; i < (length - value.length); i++) {     
	    
	            zeros += '0';     
	    
	        }     
	    
	        return zeros + value;     
	    
	    };       
	    
	    return mask.replace(/"[^"]*"|'[^']*'|\b(?:d{1,4}|m{1,4}|yy(?:yy)?|([hHMstT])\1?|[lLZ])\b/g, function($0) {     
	    
	        switch($0) {     
	    
	            case 'd':   return d.getDate();     
	    
	            case 'dd':  return zeroize(d.getDate());     
	    
	            case 'ddd': return ['Sun','Mon','Tue','Wed','Thr','Fri','Sat'][d.getDay()];     
	    
	            case 'dddd':    return ['Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday'][d.getDay()];     
	    
	            case 'M':   return d.getMonth() + 1;     
	    
	            case 'MM':  return zeroize(d.getMonth() + 1);     
	    
	            case 'MMM': return ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'][d.getMonth()];     
	    
	            case 'MMMM':    return ['January','February','March','April','May','June','July','August','September','October','November','December'][d.getMonth()];     
	    
	            case 'yy':  return String(d.getFullYear()).substr(2);     
	    
	            case 'yyyy':    return d.getFullYear();     
	    
	            case 'h':   return d.getHours() % 12 || 12;     
	    
	            case 'hh':  return zeroize(d.getHours() % 12 || 12);     
	    
	            case 'H':   return d.getHours();     
	    
	            case 'HH':  return zeroize(d.getHours());     
	    
	            case 'm':   return d.getMinutes();     
	    
	            case 'mm':  return zeroize(d.getMinutes());     
	    
	            case 's':   return d.getSeconds();     
	    
	            case 'ss':  return zeroize(d.getSeconds());     
	    
	            case 'l':   return zeroize(d.getMilliseconds(), 3);     
	    
	            case 'L':   var m = d.getMilliseconds();     
	    
	                    if (m > 99) m = Math.round(m / 10);     
	    
	                    return zeroize(m);     
	    
	            case 'tt':  return d.getHours() < 12 ? 'am' : 'pm';     
	    
	            case 'TT':  return d.getHours() < 12 ? 'AM' : 'PM';     
	    
	            case 'Z':   return d.toUTCString().match(/[A-Z]+$/);     
	    
	            // Return quoted strings with the surrounding quotes removed     
	    
	            default:    return $0.substr(1, $0.length - 2);     
	    
	        }     
	    
	    });     
	    
	};
	
	Tapestry.Initializer.SimpleLink = function(spec) {
		var clientId = spec.clientId;
		var link = $('#' + clientId);

		if (spec.url) {
			link.click(function(event) {
				if (!spec.bubbleEvent) {
					event = event ? event : window.event;
					if (event.stopPropagation) {
						event.stopPropagation();
					} else {
						event.cancelBubble = true;
					}
				}

				Etb.ajax({
					targetId : clientId,
					url : spec.url,
					paramsId : spec.paramsId,
					zone : spec.zoneId
				});
			});
		}
	}

	jQuery.timeago.settings.allowFuture = true;
	Tapestry.Initializer.TimeAgo = function(spec) {
		var clientId = spec.clientId;
		jQuery('#' + clientId).timeago();
	}
	
	Tapestry.Initializer.AjaxLoop = function(spec) {
		if (window.Etb.AjaxLoop == undefined) {
			window.Etb.AjaxLoop = {};
		}
		var ajaxLoop = window.Etb.AjaxLoop;
		
		var clientId = spec.clientId;
		var addUrl = spec.addUrl;
		var max = spec.max;
		var rowNum = spec.rowNum;
		var index = spec.index;
		var tailId = spec.tailId;
		var rowClass = spec.rowClass;
		var removeCallback = spec.removeCallback;
		
		var ajaxLoopItem = ajaxLoop[clientId] = {};
		ajaxLoopItem.clientId = clientId;
		ajaxLoopItem.addUrl = addUrl;
		ajaxLoopItem.max = max;
		ajaxLoopItem.rowNum = rowNum;
		ajaxLoopItem.index = index;
		ajaxLoopItem.tailId = tailId;
		ajaxLoopItem.removeCallback = removeCallback;
		
		var tail = $('#' + tailId);
		tail.on('rowRemoveEvent', function(e, rowId){
			ajaxLoopItem.rowNum = ajaxLoopItem.rowNum - 1;
			if(ajaxLoopItem.rowNum == 0){
				$('#' + clientId + '_nulls').show();
			}
			
			if(ajaxLoopItem.removeCallback){
				eval(ajaxLoopItem.removeCallback + "('" + rowId + "');");
			}
		});
		
		ajaxLoopItem.triggerAddRow = function(name, value){
			var item = this;
			var clientId = this.clientId;
			var addUrl = this.addUrl;
			var max = this.max;
			var rowNum = this.rowNum;
			var index = this.index;
			var tailId = this.tailId;
			var rowClass = this.rowClass;
			
			if(max > 0 && rowNum + 1 > max){
				alert('最多只能有' + max + '条');
				return;
			}
			var params = {};
			if(value)
				params[name] = value;
				
			params["_index"] = index;
				
			Etb.ajax({
				url: addUrl,
				params: params,
				beforeHandle: function(ret){
					var content = ret.addRowContent.content;
					tail.before(content);
					item.rowNum = item.rowNum + 1;
					item.index = item.index + 1;
					$('#' + clientId + '_nulls').hide();
				}
			});
			
		}
		
	};
	
	Tapestry.Initializer.AddRow = function(spec){
		var clientId = spec.clientId;
		var loopId = spec.loopId;
		var name = spec.name;
		var value = spec.value;
		
		$('#' + clientId).click(function(){
			if (window.Etb.AjaxLoop == undefined) {
				return;
			}
			var ajaxLoopItem = window.Etb.AjaxLoop[loopId];
			if(ajaxLoopItem == undefined){
				return;
			}
			
			ajaxLoopItem.triggerAddRow(name, value);
		});
	};
	
	Tapestry.Initializer.RemoveRow = function(spec){
		var clientId = spec.clientId;
		var rowClass = spec.rowClass;
		var tailClass = spec.tailClass;
		var loopId = spec.loopId;
		
		$('#' + clientId).click(function(){
			if (window.Etb.AjaxLoop == undefined) {
				return;
			}
			
			if(!loopId){
				var row = $(this).closest('.' + rowClass).eq(0);
				
				if(row.size() != 0){
					if(row.attr('row-removing') != null)
						return;
					
					row.attr('row-removing', 'true');
					var tail = row.next('.' + tailClass).eq(0);
					if(tail.size() == 0){
						tail = row.nextUntil('.' + tailClass).next();
					}
					
					tail.trigger('rowRemoveEvent', [row.attr('id')]);
					row.slideUp('normal', function(){
						row.remove();
					});
				}
			}else{
				if (window.Etb.AjaxLoop == undefined) {
					return;
				}
				var ajaxLoopItem = window.Etb.AjaxLoop[loopId];
				if(ajaxLoopItem == undefined){
					return;
				}
				
				var tailId = ajaxLoopItem.tailId;
				var tail = $('#' + tailId);
				var row = tail.prev('.' + rowClass);
				
				if(row.size() != 0){
					if(row.attr('row-removing') != null)
						return;
					
					row.attr('row-removing', 'true');
					
					tail.trigger('rowRemoveEvent', [row.attr('id')]);
					row.slideUp('normal', function(){
						row.remove();
					});
				}

                ajaxLoopItem.rowNum = ajaxLoopItem.rowNum - 1;
                ajaxLoopItem.index = ajaxLoopItem.index - 1;
			}
		});
	};
	
	Tapestry.Initializer.LazyImage = function(spec){
		
	};
	
	// mixins start
	Tapestry.Initializer.FormSubmit = function(spec) {
		var clientId = spec.clientId;
		var key = spec.key;
		var value = spec.value;
		
		var target = $('#' + clientId);
		target.click(function() {
			var form = target.closest('form')[0];
			if(key){
				form.extraParams = {};
				form.extraParams[key] = value;
			}
			form.fire(Tapestry.FORM_PROCESS_SUBMIT_EVENT);
		});
	};
	
	Tapestry.Initializer.ellipsisList = {};
	
	$(window).resize(function(){
		var ellipsisList = Tapestry.Initializer.ellipsisList;
		for(var key in ellipsisList){
			var ellipsis = ellipsisList[key];
			
			var clientId = ellipsis.clientId;
			var fontSize = ellipsis.fontSize;
			var marginright = ellipsis.marginright;
			jQuery('#' + clientId).ellipsis({
				fontSize: fontSize, 
				marginright: marginright,
				more: '…', 
				atFront: false
			});
		}
	});
	
	Tapestry.Initializer.Ellipsis = function(spec) {
		var clientId = spec.clientId;
		var fontSize = spec.fontSize;
		var marginright = spec.marginright;
		
		jQuery('#' + clientId).ellipsis({
			fontSize: fontSize, 
			marginright: marginright,
			more: '…', 
			atFront: false
		});
		
		Tapestry.Initializer.ellipsisList[clientId] = spec;
	};
	
	Tapestry.Initializer.HoverShow = function(spec) {
		var clientId = spec.clientId;
		var target = spec.target;
		
		var showItem = $('#' + clientId);
		$('#' + target).hover(function(){
			showItem.show();
		},function(){
			showItem.hide();
		});
	};
	// mixins end
}(window.jQuery));