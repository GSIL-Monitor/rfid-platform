<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <style>
        #grid-pager_center{
          display: none;
        }
    </style>
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
        <div class="main-content-inner">
            <div id="page-content">
                <div class="col-xs-12">
                    <div class="widget-box widget-color-blue  light-border">
                        <div class="widget-header">
                            <h5 class="widget-title">接口模型设置</h5>
                            <div class="widget-toolbar no-border">
                                <button class="btn btn-xs btn-light bigger" onclick="javascript:history.back(-1);">
                                    <i class="ace-icon fa fa-arrow-left"></i>
                                    返回
                                </button>
                            </div>
                        </div>

                        <div class="widget-body">
                            <div class="widget-main padding-12">
                                <form class="form-horizontal" role="form" id="editForm">
                                    <div class="form-group">
                                        <label class="col-xs-offset-2 col-xs-1  control-label text-right"
                                               for="form_origDs">源 数据源</label>
                                        <div class="col-xs-3">
                                            <select class="chosen-select form-control" id="form_origDs" name="origDs"
                                                    onchange="initTableSelect(this.options[this.options.selectedIndex].value,$('#form_origTable'))">
                                                <option value="">--请选择--</option>
                                            </select>
                                        </div>

                                        <label class=" col-xs-1 control-label text-right"
                                               for="form_destDs">目标 数据源</label>
                                        <div class="col-xs-3">
                                            <select class="chosen-select form-control" id="form_destDs" name="destDs"
                                                    onchange="initTableSelect(this.options[this.options.selectedIndex].value,$('#form_destTable'))">
                                                <option value="">--请选择--</option>
                                            </select>
                                        </div>
                                    </div>


                                    <div class="form-group">
                                        <label class="col-xs-offset-2 col-xs-1 control-label text-right"
                                               for="form_origTable">源 对应表</label>
                                        <div class="col-xs-3">
                                            <select class="chosen-select form-control" id="form_origTable"
                                                    name="origTable">
                                                <option value="">--请选择--</option>
                                            </select>
                                        </div>

                                        <label class="col-xs-1  control-label text-right"
                                               for="form_destTable">目标 对应表</label>
                                        <div class="col-xs-3">
                                            <select class="chosen-select form-control" id="form_destTable"
                                                    name="destTable">
                                                <option value="">--请选择--</option>
                                            </select>
                                        </div>
                                    </div>

                                    <div class="form-group" id="btnSave">
                                        <div class="col-xs-12 center">
                                            <button type="button" class="btn btn-sm btn-primary"
                                                    onclick="saveInterfaceModel()">
                                                <i class="ace-icon fa fa-save"></i>
                                                <span class="bigger-110">确认</span>
                                            </button>
                                        </div>
                                    </div>

                                </form>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-xs-12">
                <div class="widget-box" id="widget-box-10">
                    <div class="widget-header widget-header-small">
                        <div class="widget-toolbar no-border" style="float: left;">
                            <ul class="nav nav-tabs" id="myTab">
                                <li class="active">
                                    <a data-toggle="tab" href="#columnDetail">字段对照明细</a>
                                </li>

                                <li>
                                    <a data-toggle="tab" href="#paramSetting">参数设置</a>
                                </li>

                                <li>
                                    <a data-toggle="tab" href="#origSetting">源设置</a>
                                </li>
                                <li>
                                    <a data-toggle="tab" href="#destSetting">目标设置</a>
                                </li>
                            </ul>
                        </div>
                    </div>

                    <div class="widget-body">
                        <div class="widget-main" style="padding: 0 20px 0 0">
                            <div class="tab-content" style="padding: 6px ">
                                <div id="columnDetail" class="tab-pane in active">
                                    <table id="grid"></table>
                                    <div id="grid-pager"></div>
                                </div>

                                <div id="paramSetting" class="tab-pane">
                                    <p>参数设置</p>
                                </div>

                                <div id="origSetting" class="tab-pane">
                                    <p>源设置</p>
                                </div>

                                <div id="destSetting" class="tab-pane">
                                    <p>目标设置</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                </div>

            </div>
        </div>
    </div>
<jsp:include page="../layout/footer.jsp"></jsp:include>
</div>

<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<script type="text/javascript" src="<%=basePath%>/views/exchange/interfaceModel_edit.js"></script>
<script>
    $(function () {
       if ("${isEdit}"=="Y"){
           $("#form_origDs").val("${InterfaceModel.origDs}");
           $("#form_destDs").val("${InterfaceModel.destDs}");
           initTableSelect("${InterfaceModel.origDs}",$('#form_origTable'));
           initTableSelect("${InterfaceModel.destDs}",$('#form_destTable'));
           $("#form_origTable").val("${InterfaceModel.origTable}");
           $("#form_destTable").val("${InterfaceModel.destTable}");


           $("#form_origDs").attr("disabled",true);
           $("#form_destDs").attr("disabled",true);
           $("#form_origTable").attr("disabled",true);
           $("#form_destTable").attr("disabled",true);
           $("#btnSave").css("display","none");
           modelCode="${InterfaceModel.code}";
           var progressDialog = bootbox.dialog({
               message: '<p><i class="fa fa-spin fa-spinner"></i> 获取数据中...</p>'
           });

           $.ajax({
               url: basePath + "/exchange/interfaceModel/get2Columns.do",
               dataType: 'json',
               type: "POST",
               data:{"origDsId":"${InterfaceModel.origDs}","origTableName":"${InterfaceModel.origTable}","destDsId":"${InterfaceModel.destDs}","destTableName":"${InterfaceModel.destTable}"},
               success: function (data) {
                   if (data.success == true || data.success == 'true') {

                       var origArray=data.result.orig;
                       for (var i=0;i<origArray.length;i++){
                           if (i==origArray.length-1){
                               orig+=origArray[i]+":"+origArray[i];
                           }else{
                               orig+=origArray[i]+":"+origArray[i]+";";
                           }
                       }
                       var destAray=data.result.dest;
                       for (var i=0;i<destAray.length;i++){
                           if (i==destAray.length-1){
                               dest+=destAray[i]+":"+destAray[i];
                           }else{
                               dest+=destAray[i]+":"+destAray[i]+";";
                           }
                       }
                       initGrid();
                       progressDialog.modal('hide');

                   }
               }
           });
       }

    });
</script>
</body>
</html>