/* MIT (c) Juho Vepsalainen */
(function ($) {
    function ellipsis($elem, options) {
        var a = check('li', $elem, options);
        var b = check('dt', $elem, options);

        if(!(a || b)) {
            checkText($elem, options);
        }
    }

    function check(name, $elem, options) {
        var $elems = $(name, $elem);

//        if($elems.length > options.visible) {
//            var $slice = $elem.children().slice($($elems[options.visible]).index()).hide();
//
//            $more(name, options.more, function() {
//                if(options.showCb) {
//                    options.showCb($slice);
//                }
//                else {
//                    $slice.show();
//                }
//            }).appendTo($elem);
//        }

        if($elems.length) {
            return true;
        }
    }

    function checkText($elem, options) {
    	var parent = $elem.parent();
    	var width = parent.width() - options.marginright;
    	var characterLength = parseInt(width/(options.fontSize*0.56));
    	
    	var title = $elem.attr("title");
    	var isReload = false;
    	if(title)
    		isReload = true;
    		
        var origText = isReload ? title : $elem.text();
        
        var origLength = origText.replace(/[^\x00-\xff]/g,"**").length;
        
        var target = $elem.children()[0];
        if(!target){
        	target = $elem;
        }else{
        	target = jQuery(target);
        }
        
        if(origLength > characterLength) {
        	var text = "";
        	characterLength -= options.more.replace(/[^\x00-\xff]/g,"**").length;
        	for(var i = 0; i < characterLength; i++){
        		text += origText[i];
        		if(origText[i].match(/[^\x00-\xff]/g)){
        			characterLength--;
        		}
        	}
        	
        	if(options.atFront){
        		text = options.more + text;
        	}else{
        		text += options.more;
        	}
        	
            target.text(text);

//            var $m = $more('span', options.more, function() {
//                if(options.showCb) {
//                    options.showCb($elem, origText);
//                }
//                else {
//                    $elem.text(origText);
//                }
//            });
//            var $m = $more('span', options.more);
            
            $elem.attr("title", origText);

//            if(options.atFront) $m.prependTo(target);
//            else $m.appendTo(target);

            return true;
        }else if(isReload){
        	target.text(title);
        }
    }

//    function $more(name, text, showCb) {
//        var $m = $('<' + name + ' class="more">' + text +'</' + name + '>');
//
//        $m.bind('click', function() {
//            showCb();
//            $m.remove();
//        });
//
//        return $m;
//    }

    var defaults = {
        more: '&hellip;',
        showCb: null,
        atFront: false
    };
    $.fn.ellipsis = function(options) {
        return this.each(function () {
            var $elem = $(this);
            var opts = $.extend({}, defaults, options);

            ellipsis($elem, opts);
        });
    };
    $.fn.ellipsis.options = defaults;
})(jQuery);
