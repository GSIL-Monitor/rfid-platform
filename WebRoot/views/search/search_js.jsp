<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()+"/";
%>
<script type="text/javascript">
    window.jQuery || document.write("<script src='<%=basePath%>Olive/assets/js/jquery.js'>"+"<"+"/script>");
</script>


<script type="text/javascript">
function collapseGroup(searchGrid) {

        var grid = $(searchGrid).data('kendoGrid');
        var allGroupRows = grid.tbody.find(">tr.k-grouping-row");
        for (var i = 0; i < allGroupRows.length; i++) {
                grid.collapseGroup(allGroupRows.eq(i));
        }

}
function expandGroup(searchGrid) {

        var grid = $(searchGrid).data('kendoGrid');
        var allGroupRows = grid.tbody.find(">tr.k-grouping-row");
        for (var i = 0; i < allGroupRows.length; i++) {
                grid.expandGroup(allGroupRows.eq(i));
        }

}
</script>

<!-- <![endif]-->

<!--[if IE]>
<script type="text/javascript">
window.jQuery || document.write("<script src='<%=basePath%>Olive/assets/js/jquery1x.js'>"+"<"+"/script>");
</script>
<![endif]-->
<script type="text/javascript">
    if('ontouchstart' in document.documentElement) document.write("<script src='<%=basePath%>Olive/assets/js/jquery.mobile.custom.js'>"+"<"+"/script>");
</script>

<script src="<%=basePath%>Olive/assets/js/bootstrap.js"></script>
<!-- page specific plugin scripts -->

<!--[if lte IE 8]>
<script src="<%=basePath%>Olive/assets/js/excanvas.js"></script>
<![endif]-->
<script src="<%=basePath%>Olive/assets/js/jquery-ui.custom.js"></script>
<script src="<%=basePath%>Olive/assets/js/jquery.ui.touch-punch.js"></script>

<script src="<%=basePath%>Olive/assets/js/bootbox.js"></script>
<!-- page specific plugin scripts -->

<script src="<%=basePath%>Olive/plugin/bootstrap-validator/js/bootstrapValidator.min.js"></script>
<script src="<%=basePath%>Olive/plugin/jquery.form.js"></script>

<!-- ace scripts -->
<script src="<%=basePath%>Olive/assets/js/ace/elements.scroller.js"></script>
<script src="<%=basePath%>Olive/assets/js/ace/elements.colorpicker.js"></script>
<script src="<%=basePath%>Olive/assets/js/ace/elements.fileinput.js"></script>
<script src="<%=basePath%>Olive/assets/js/ace/elements.typeahead.js"></script>
<script src="<%=basePath%>Olive/assets/js/ace/elements.wysiwyg.js"></script>
<script src="<%=basePath%>Olive/assets/js/ace/elements.spinner.js"></script>
<script src="<%=basePath%>Olive/assets/js/ace/elements.treeview.js"></script>
<script src="<%=basePath%>Olive/assets/js/ace/elements.wizard.js"></script>
<script src="<%=basePath%>Olive/assets/js/ace/elements.aside.js"></script>
<script src="<%=basePath%>Olive/assets/js/ace/ace.js"></script>
<script src="<%=basePath%>Olive/assets/js/ace/ace.ajax-content.js"></script>
<script src="<%=basePath%>Olive/assets/js/ace/ace.touch-drag.js"></script>
<script src="<%=basePath%>Olive/assets/js/ace/ace.sidebar.js"></script>
<script src="<%=basePath%>Olive/assets/js/ace/ace.sidebar-scroll-1.js"></script>
<script src="<%=basePath%>Olive/assets/js/ace/ace.submenu-hover.js"></script>
<script src="<%=basePath%>Olive/assets/js/ace/ace.widget-box.js"></script>
<script src="<%=basePath%>Olive/assets/js/ace/ace.settings.js"></script>
<script src="<%=basePath%>Olive/assets/js/ace/ace.settings-rtl.js"></script>
<script src="<%=basePath%>Olive/assets/js/ace/ace.settings-skin.js"></script>
<script src="<%=basePath%>Olive/assets/js/ace/ace.widget-on-reload.js"></script>
<script src="<%=basePath%>Olive/assets/js/ace/ace.searchbox-autocomplete.js"></script>
<script src="<%=basePath%>/kendoUI/js/jszip.min.js"></script> 
<script src="<%=basePath%>/kendoUI/js/jquery.min.js"></script> 
<script src="<%=basePath%>/kendoUI/js/kendo.all.min.js"></script>    
<script src="<%=basePath%>/kendoUI/js/kendo.timezones.min.js"></script>
<%--<script src="<%=basePath%>/kendoUI/shared/js/console.js"></script>
<script src="<%=basePath%>/kendoUI/shared/js/prettify.js"></script>--%>
<script src="<%=basePath%>/kendoUI/js/cultures/kendo.culture.zh-CN.min.js"></script>
<script src="<%=basePath%>/kendoUI/js/messages/kendo.messages.zh-CN.min.js"></script>
<script src="<%=basePath%>Olive/assets/js/date-time/bootstrap-datepicker.js"></script>
<script src="<%=basePath%>Olive/assets/js/date-time/moment.js"></script>

<script src="<%=basePath%>Olive/assets/js/bootstrap.js"></script>
<script src="<%=basePath%>Olive/assets/js/jqGrid/js/jquery.jqGrid.min.js"></script>
<script src="<%=basePath%>Olive/assets/js/jqGrid/i18n/grid.locale-cn.js"></script>



<script type="text/javascript">
    function array2obj(array) {
        var params = $({});
        $.each(array, function(i) {
            var $param = $(this)[0];
            params.attr($param.name, $param.value);
        });
        return params[0];
    }

$(function() {

    $('.input-daterange').datepicker({autoclose:true});
    //datepicker plugin
    //link
    $('.date-picker').datepicker({
        autoclose: true,
        todayHighlight: true
    }) .next().on(ace.click_event, function(){
                $(this).prev().focus();
    });

});

jQuery.extend(jQuery.jgrid.defaults,{loadComplete: function(data) {
    var table = this;
    setTimeout(function () {
        updatePagerIcons(table);
    }, 0);

}});
function updatePagerIcons(table) {
    //ui-icon ui-icon-circlesmall-minus
    var replacement =
    {
        'ui-icon-seek-first' : 'ace-icon fa fa-angle-double-left bigger-140',
        'ui-icon-seek-prev' : 'ace-icon fa fa-angle-left bigger-140',
        'ui-icon-seek-next' : 'ace-icon fa fa-angle-right bigger-140',
        'ui-icon-seek-end' : 'ace-icon fa fa-angle-double-right bigger-140'
    };
    $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function(){
        var icon = $(this);
        var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

        if($class in replacement) icon.attr('class', 'ui-icon '+replacement[$class]);
    });

}


kendo.ui.FilterMenu.prototype.options.operators =           
  $.extend(kendo.ui.FilterMenu.prototype.options.operators, {


	  string: {
  	      contains: "包含",
  	      startswith: "开头为",
  	      eq: "等于",
  	      neq: "不等于",
  	      doesnotcontain: "不包含",
  	      endswith: "结尾为"
  	  },
  	  number: {
  	      eq: "等于",
  	      neq: "不等于",
  	      gte: "大于等于",
  	      gt: "大于",
  	      lte: "小于等于",
  	      lt: "小于"
  	  },
  	  date: {
  	     
  	      gte: "大于等于",
  	      gt: "大于",
  	      lte: "小于等于",
  	      lt: "小于"
  	  },
  	  enums: {
  	      eq: "等于",
  	      neq: "不等于"
  	  }
 
});
 function serializeToFilter(from){
	 var filters = [];
     var o = {};
     var fromDatafrom=from.serializeArray();
	 $.each(fromDatafrom, function(index) {

         if (o[this['name']]) {
             o[this['name']] = o[this['name']] + "," + this['value'];
         } else {
             o[this['name']] = this['value'];
         }


     });
     for(var key in o){
         var obj = key.split("_");
         var value ;
         if(obj[2].indexOf("Date") != -1){

             if(o[key] != ""){
                 value = new Date(o[key]);
                 if(obj[1].indexOf("lte") != -1){
                     value = new Date(o[key]);
                     value = new Date(value.getTime()+24*60*60*1000-1);
                 }
                 filters.push({
                     field: obj[2],
                     operator: obj[1],
                     value: value
                 });
             }
         }else{
             value = o[key];
             if(value !=""){
                 filters.push({
                     field: obj[2],
                     operator: obj[1],
                     value: value
                 });
             }

         }
     }
	 return filters;
 }
/* function startChange() {
     var startDate = start.value(),
     endDate = end.value();

     if (startDate) {
         startDate = new Date(startDate);
         startDate.setDate(startDate.getDate());
         end.min(startDate);
     } else if (endDate) {
         start.max(new Date(endDate));
     } else {
         endDate = new Date();
         start.max(endDate);
         end.min(endDate);
     }
 }

 function endChange() {
     var endDate = end.value(),
     startDate = start.value();

     if (endDate) {
         endDate = new Date(endDate);
         endDate.setDate(endDate.getDate());
         start.max(endDate);
     } else if (startDate) {
         end.min(new Date(startDate));
     } else {
         endDate = new Date();
         start.max(endDate);
         end.min(endDate);
     }
 }*/


   var ExtGrid = kendo.ui.Grid.extend({

       init: function (element, options) {
           var that = this;

           that._orderNumber(options);

           kendo.ui.Grid.fn.init.call(that, element, options);
           that._RegisterRowNumber(options);
       },
       _orderNumber: function (options) {
           if (options.rowNumber) {
               var that = this;
               var rowTemplate = '#= count #';
               var renderRowCount = function () {

                   that.options._count += 1;
                   return kendo.render(kendo.template(rowTemplate), [{ count: that.options._count }]);
               };
               if (options.rowNumber) {
                   if (options.columns) {
                       //1. 添加行号列
                       options.columns.splice(0, 0, { attributes: { 'class': 'tight-cell' }, editor: null, editable: false, title: '', template: renderRowCount, width: "45px" });
                   }
               }
           }
       },
       _RegisterRowNumber: function () {
           var that = this;
           if (that.options.rowNumber) {
               var that = this;
               that.bind('dataBinding', function () {
                   that.options._count = (that.dataSource.page() - 1) * that.dataSource.pageSize();
                   that.options._count = isNaN(that.options._count) ? 0 : that.options._count;
               });
           }
       },
       options: {
           name: 'ExtGrid',
           _count: 0,
           rowNumber: false
       }
   });

   kendo.ui.plugin(ExtGrid);
</script>


