.
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()+"/";
%>
<script type="text/javascript">
    window.jQuery || document.write("<script src='<%=basePath%>Olive/assets/js/jquery.js'>"+"<"+"/script>");
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
<script src="<%=basePath%>Olive/assets/js/jquery-ui.js"></script>
<script src="<%=basePath%>Olive/assets/js/jquery-ui.custom.js"></script>
<script src="<%=basePath%>Olive/assets/js/jquery.ui.touch-punch.js"></script>
<script src="<%=basePath%>Olive/assets/js/jqGrid/js/jquery.jqGrid.min.js"></script>
<script src="<%=basePath%>Olive/assets/js/jqGrid/i18n/grid.locale-cn.js"></script>
<script src="<%=basePath%>/Olive/assets/js/jqGrid/src/grid.common.js"></script>
<script src="<%=basePath%>/Olive/assets/js/jqGrid/src/grid.celledit.js"></script>
<script src="<%=basePath%>Olive/assets/js/bootbox.js"></script>
<script src="<%=basePath%>Olive/assets/js/jquery.gritter.js"></script>
<script src="<%=basePath%>Olive/assets/js/bootstrap-multiselect.js"></script>
<script src="<%=basePath%>/Olive/assets/js/bootstrap-select.js"></script>

<!-- page specific plugin scripts -->

<script src="<%=basePath%>Olive/assets/js/date-time/bootstrap-datepicker.js"></script>
<script src="<%=basePath%>Olive/assets/js/date-time/bootstrap-timepicker.js"></script>
<script src="<%=basePath%>Olive/assets/js/date-time/moment.js"></script>
<script src="<%=basePath%>Olive/plugin/bootstrap-validator/js/bootstrapValidator.js"></script>
<script src="<%=basePath%>Olive/plugin/bootstrap-validator/js/language/zh_CN.js"></script>
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
<script src="<%=basePath%>Olive/plugin/fileInput/js/fileinput.min.js" type="text/javascript"></script>
<script src="<%=basePath%>Olive/plugin/fileInput/js/fileinput_locale_zh.js" type="text/javascript"></script>
<script src="<%=basePath%>Olive/assets/js/dialog.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=basePath%>/jslib2/constant.js"></script>

<script>
    function array2obj(array) {
        var params = $({});
        $.each(array, function(i) {
            var $param = $(this)[0];
            if($param.name.indexOf("_LED_")>=0&&$param.value!=""){
            	var vim=$param.value;
            	$param.value+=" 23:59:59";
            }
            params.attr($param.name, $param.value);
        });
        return params[0];
    }

    function initLayout() {
        $("table[rel=jqgridForm]").each(function() {
            var rel = $(this).attr("rel");
            if (rel) {
                var $form = $("#" + rel);
                var tableWidth = $form.width();
                $(this).setGridWidth(tableWidth, true);
            }
        });
    }

   // $.jgrid.defaults.width = 780;
   // $.jgrid.defaults.styleUI = 'Bootstrap';
    jQuery.extend(jQuery.jgrid.defaults,{loadComplete: function(data) {
        var table = this;
        setTimeout(function () {
            updatePagerIcons(table);
        }, 0);

    }});
    
    //replace icons with FontAwesome icons like above
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

    $(function() {
        $('.input-daterange').datepicker({autoclose:true});
        //datepicker plugin
        //link
        $('.date-picker').datepicker({
            autoclose: true,
            todayHighlight: true
        }).next().on(ace.click_event, function(){
                    $(this).prev().focus();
                });
        var parent_column = $("#grid").closest('.col-xs-12');
        //resize to fit page size
        $(window).on('resize.jqGrid', function () {
            $("#grid").jqGrid( 'setGridWidth', parent_column.width() );
        });

        $(window).resize(function(){
            $(window).triggerHandler('resize.jqGrid');
            initLayout();
        });
        
        $("#grid").css("background","#FFFFFF");
    });

</script>
