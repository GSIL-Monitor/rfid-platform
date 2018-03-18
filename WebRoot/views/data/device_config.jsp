<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../baseView.jsp"></jsp:include>
    <script type="text/javascript">
        var basePath = "<%=basePath%>";
    </script>

</head>
<body class="no-skin">
<div class="main-container" id="main-container" style="">
    <script type="text/javascript">
        try {
            ace.settings.check('main-container', 'fixed')
        } catch (e) {
        }
    </script>
    <div class="main-content">

                <div class="col-xs-12">
                            <div class="widget-box widget-color-blue light-border" id="authBox">
                                <div class="widget-header">
                                    <h5 class="widget-title">编辑设备号[${deviceId}]配置</h5>

                                    <div class="widget-toolbar">
                                    <button class="btn btn-xs btn-light bigger" onclick="history.back(-1);">
                                        <i class="ace-icon fa fa-arrow-left"></i>
                                        返回
                                    </button>
                                    </div>
                                    <div class="widget-toolbar no-border">
                                        <button class="btn btn-xs btn-light bigger" onclick="add();">
                                            <i class="ace-icon fa fa-plus"></i>
                                            增加
                                        </button>
                                        <button class="btn btn-xs btn-light bigger" onclick="edit();">
                                            <i class="ace-icon fa fa-edit"></i>
                                            编辑
                                        </button>
                                    </div>
                                </div>

                                <div class="widget-body">
                                    <div class="widget-main no-padding">
                                            <table id="cfgGrid"></table>
                                    </div>
                                </div>
                                <div class="widget-toolbox padding-8 clearfix">
                                  <span>* 只对admin用户开放</span>
                                </div>
                            </div>
                        </div>
             </div>



    <jsp:include page="../layout/footer.jsp"></jsp:include>
</div>


<jsp:include page="../layout/footer_js.jsp"></jsp:include>



<script type="text/javascript">


$(function() {
    initCfgGrid();
});

    function initCfgGrid() {
        $("#cfgGrid").jqGrid({
            height: 500,
            url: basePath + "/data/device/getConfigList.do?deviceId=${deviceId}",
            datatype: "json",
            colModel: [
                {name: 'id', label: 'id', hidden: true, width: 40},
                {name: 'fileName', label: '文件名', width: 100},
                {name: 'path', label: '路径', width: 200},
                {name: 'val', label: '值',  width: 400},
                {name: 'app', label: '程序名',  width: 100},
                {name: 'type', label: '类型',  width: 100}
            ],
            viewrecords: true,
            autowidth: true,
            rownumbers: true,
            altRows: true,
            rowNum:-1,
            multiselect: false,
            shrinkToFit: true,
            ondblClickRow : function(rowId,rowIndex,colIndex,e) {
                var row = $("#cfgGrid").jqGrid('getRowData',rowId);
                $("#edit-dialog").modal("show");
                loadFormData(row);
            }
        });
        var parent_column = $("#main-container");
        $("#cfgGrid").jqGrid( 'setGridWidth', parent_column.width()-20);

    }

 function add(){
     $("#editForm").resetForm();
     $("#edit-dialog").modal('show');
 }

 function edit(){
     var rowId = $("#cfgGrid").jqGrid("getGridParam", "selrow");
     if(rowId) {
         var row = $("#cfgGrid").jqGrid('getRowData',rowId);
         $("#edit-dialog").modal("show");
         loadFormData(row);
     } else {
         bootbox.alert("请选择一项进行修改！");
     }
 }
 function loadFormData(row) {
     $("#form_id").val(row.id);
     $("#form_fileName").val(row.fileName);
     $("#form_path").val(row.path);
     $("#form_app").val(row.app);
     $("#form_type").val(row.type);
     $("#form_val").val(row.val);
 }
</script>
<jsp:include page="device_config_edit.jsp"></jsp:include>
</body>
</html>