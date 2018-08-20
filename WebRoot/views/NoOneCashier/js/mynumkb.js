/**
 * 数字键盘插件 js（基于jquery)
 * Date:2018-08-15
 * author:lly
 * email:853692043@qq.com
 */

//jQuery.proxy( function, context )
/**function将要改变上下文语境的函数。
 ** context函数的上下文语境(`this`)会被设置成这个 object 对象。
 **/

/*比如我们要开发一个插件，做一个特殊的编辑框，当它被点击时，便alert 当前编辑框里的内容。可以这么做：
$.fn.extend({
    alertWhileClick:function(){
        $(this).click(function(){
            alert($(this).val());
        });
    }
});
$("#input1").alertWhileClick();*/

(function($){
    var _count = 0;
	var Mynumkb = function(element, options){
        _count++;
        this.count = _count;
        this.$element = $(element);
        this.$element.attr("data-count",this.count);
		this.options = $.extend({},$.fn.mynumkb.defaults, options);//合并对象
		this.init();
	};
	Mynumkb.prototype = {
	    constructor: Mynumkb, 
		init:function(){
			var me= this;
			me.render();
			me.bind();
            me.initHtml();
		},
		render:function(){
			var me = this;
            me.$eparentNode = me.$element.parent();
		},
		bind:function(){
            var me = this;
            me.$element.on("click",$.proxy(me['_eClick'],me));

            $(document).on("mousedown","#mykeyboard"+me.count+" li",$.proxy(me['_limousedown'],me));
            $(document).on("mouseup","#mykeyboard"+me.count+" li",$.proxy(me['_limouseup'],me));
            $(document).on("click","#mykeyboard"+me.count+" li",$.proxy(me['_liclick'],me));
		},
		initHtml:function(){
            var me = this;
            var _html = [
                '<div class="mykb-box" id="mykeyboard'+me.count+'">',
                    '<ul class="num-key clearfix">',
                        '<li class="num">1</li>',
                        '<li class="num">2</li>',
                        '<li class="num">3</li>',
                        '<li class="func del">←</li>',
                        '<li class="num">4</li>',
                        '<li class="num">5</li>',
                        '<li class="num">6</li>',
                        //'<li class="func clearall">清除</li>',
                        '<li class="num">7</li>',
                        '<li class="num">8</li>',
                        '<li class="num">9</li>',
                        '<li class="func clearall">清除</li>',
                        '<li class="num">0</li>',
                        '<li class="num">.</li>',
                        '<li class="num two">00</li>',
                    '</ul>',
                '</div>',
            ].join("");
            
            $(".right-foot").append(_html);
            me.setPosition();
        },
        setPosition:function(){
            var me = this;
            var parentNode = me.$eparentNode;
            var $element = me.$element;
            $("body").css("position","relative");
            var height = $element.outerHeight();
            var width = $element.outerWidth();
            var position = $element.position();
            var $keyboard = $("#mykeyboard"+me.count);
            var ulWidth = $keyboard.outerWidth();
            var ulHeight = $keyboard.outerHeight();
            var left = position.left;
            /*$keyboard.css({
                top:position.top+height+30+"px",
                left:left+width+30+"px"
            });*/
            $keyboard.fadeIn(100).siblings(".mykb-box").hide();
        },
        /*_eClick:function(e){
            var me = this;
            console.info("11");
            var count = me.$element.data("count");
            var $keyboard = $("#mykeyboard"+count);
            $keyboard.fadeIn(100).siblings(".mykb-box").hide();
        },*/
        _liclick:function(e){
            var me = this;
            console.info("22");
            var $target = $(e.target);
            if($target.hasClass("del")){//退格
                me.setValue("del");
            }else if($target.hasClass("exit")||$target.hasClass("sure")){//退出--确定
                me.close();
            }else if($target.hasClass("clearall")){//清除
                me.$element.val("");
            }else if($target.hasClass("two")){//00
                var str = $target.text();
                me.setValue("addtwo",str);
            }else{//输入其他数字
                var str = $target.text();
                console.info(me.$element);
                me.setValue("add",str);
            }
            //js键盘输入绑定
            console.info(me.$element.val());
            changeValue();
        },
		_limousedown:function(e){
            var me = this;
            console.info("33");
            var val = $(e.target).html();
            $(e.target).addClass("active").siblings().removeClass("active");
        },
		_limouseup:function(e){
            var me = this;
            console.info("44");
            var val = $(e.target).html();
            $(e.target).removeClass("active");
            
        },
        setValue:function(type,str){
            var me = this;
            console.info("55");
            var curpos = me.getCursorPosition();
            var val = me.$element.val();
            var newstr = "";
            if(type == 'add'){
                newstr = me.insertstr(val,str,curpos);
                me.$element.val(newstr);
                me.$element.textFocus(curpos+1);
            }else if(type == 'addtwo'){
                newstr = me.insertstr(val,str,curpos);
                me.$element.val(newstr);
                me.$element.textFocus(curpos+2);
            }else if(type == 'del'){
                newstr = me.delstr(val,curpos);
                me.$element.val(newstr);
                me.$element.textFocus(curpos-1);
            }
        	
        },
        insertstr:function(str,insertstr,pos){
            var str = str.toString();
            var newstr = str.substr(0,pos)+''+insertstr+''+str.substr(pos);
            return newstr;
        },
        delstr:function(str,pos){
            var str = str.toString();
            var newstr = "";
            if(pos != 0){
                newstr = str.substr(0,pos-1)+str.substr(pos);
            }else{
                newstr = str;
            }
            return newstr;
        },
        getCursorPosition:function(){
            var me = this;
            console.info("66");
            var $element = me.$element[0];
            var cursurPosition=-1;
            if($element.selectionStart!=undefined && $element.selectionStart!=null){//非IE浏览器
                cursurPosition= $element.selectionStart;
            }else{//IE
                var range = document.selection.createRange();
                range.moveStart("character",-$element.value.length);
                cursurPosition=range.text.length;
            }
            return cursurPosition;
        },
        close:function(){
            var me = this;
            console.info("77");
            var count = me.$element.data("count");
            var $keyboard = $("#mykeyboard"+count);
            $keyboard.fadeOut(100);
            me.$element.attr("isshowkb","false");
        }
	};
	$.fn.mynumkb = function (option) {
	    return this.each(function () {
            var options = typeof option == 'object' ? option : {};
	        var data = new Mynumkb(this, options);
	    })
    };
	$.fn.mynumkb.defaults = {
		
	};
	$.fn.mynumkb.Constructor = Mynumkb;
	
	
})(jQuery);
(function($){
    $.fn.textFocus=function(v){
        var range,len,v=v===undefined?0:parseInt(v);
        this.each(function(){
            if(navigator.userAgent.msie){
                range=this.createTextRange();
                v===0?range.collapse(false):range.move("character",v);
                range.select();
            }else{
                len=this.value.length;
                v===0?this.setSelectionRange(len,len):this.setSelectionRange(v,v);
            }
            this.focus();
        });
        return this;
    }
})(jQuery);