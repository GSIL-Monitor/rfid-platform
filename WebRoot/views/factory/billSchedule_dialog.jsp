<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="modal-billSchedule-table" class="modal fade" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header no-padding">
                <div class="table-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        <span class="white">&times;</span>
                    </button>
                    排期流程
                </div>
            </div>

            <div class="modal-body no-padding">
                <div class="widget-body">

                    <div class="widget-main" id="search_shop_Panel">
                        <form class="form-horizontal" role="form" id="billScheduleForm">
                            <div class="form-group">
                                <label class="col-xs-2 control-label text-right" for="form_scheduleToken">流程</label>

                                <div class="col-xs-9">
                                    <select class="chosen-select form-control" id="form_scheduleToken"
                                            name="scheduleToken">
                                    </select>
                                </div>
                            </div>
                        </form>

                    </div>

                    <div class="hr hr-2 hr-dotted"></div>
                    <table id="billSchedule_Grid"></table>
                </div>
            </div>

            <div class="modal-footer no-margin-top">
                <a href="#" class="btn" onclick="deleteData()">删除</a>
                <a href="#" class="btn btn-primary" onclick="addData()">添加</a>
                <a href="#" class="btn btn-primary" onclick="saveAddData()">保存</a>
            </div>
        </div>
    </div>
</div>
<script>
    var editRowId = null;
    $(function () {
        $("#modal-billSchedule-table").on('hide.bs.modal', function () {


        });
        $.ajax({
            dataType: "json",
            url: basePath + "/factory/token/findToken.do",
            async: false,
            type: "post",
            success: function (data) {
                $("#form_scheduleToken").kendoMultiSelect({
                    dataTextField: "name",
                    dataValueField: "token",
                    height: 400,
                    suggest: true,
                    dataSource: {
                        data: data.result
                    }
                });
            }
        });
        initBillScheduleGrid();
    });

    function initBillScheduleGrid() {
        $("#billSchedule_Grid").jqGrid({
            height: "400px",
            mtype: "POST",
            datatype: "json",
            colModel: [
                {name: 'token', label: '流程', hidden: true, width: 280},
                {name: 'tokenName', label: '流程', editable: false, width: 280},
                {
                    name: 'schedule', label: '预计时间', editable: true, width: 280, editoptions: {
                    dataInit: function (e) {
                        $(e).datepicker({
                            language: "zh-CN",//语言
                            autoclose: true,//自动关闭
                            todayBtn: "linked",//
                            format: "yyyy-mm-dd",//时间显示格式
                            onSelect : saveGrid
                        }).on('changeDate',saveGrid);;


                    }
                }
                }
            ],
            viewrecords: true,
            rownumbers: true,
            altRows: true,
            shrinkToFit: false,
            autoScroll: false,
            autowidth: true,
            onSelectRow: function (rowid, status) {
                if(editRowId!=null){
                    $('#billSchedule_Grid').saveRow(editRowId);
                }
                editRowId = rowid;
                $('#billSchedule_Grid').editRow(rowid);
            }
        });
    }

    function saveGrid(){
       if (editRowId!=null){
           $('#billSchedule_Grid').saveRow(editRowId);
       }
    }
    function addData() {
        var multiselect = $("#form_scheduleToken").data("kendoMultiSelect");
        var gridData = $("#billSchedule_Grid").jqGrid("getRowData");
        var dataItem = multiselect.dataItems();
        for (var i = 0; i < dataItem.length; i++) {
            if(!tokenIsExits(dataItem[i].token,gridData)){
                var data = {token: dataItem[i].token, tokenName: dataItem[i].name};
                $("#billSchedule_Grid").jqGrid("addRowData", i, data);
            }
        }
    }
    function tokenIsExits(token,gridData){
        for(var i=0;i<gridData.length;i++){
            if (token==gridData[i].token){
                return true;
            }
        }
        return false;
    }

    function deleteData() {
        var rowId = $("#billSchedule_Grid").jqGrid("getGridParam", "selrow");
        if(rowId) {
            $("#billSchedule_Grid").jqGrid("delRowData",rowId);

        } else {
            bootbox.alert("请选择一项！");
        }
    }


    function saveAddData() {
        var data = $("#billSchedule_Grid").jqGrid("getRowData");

        var sids = $("#grid").jqGrid("getGridParam", "selarrrow");
        var dataArray = [];
        if(sids){
            for(var i=0;i<sids.length;i++){
                var ret = $("#grid").jqGrid("getRowData",sids[i]);
                dataArray.push(ret);
            }
        }
        $.each(dataArray,function(index,value){
            value.totQty = value.billQty;
            value.operator = value.billOperator;
        });
        $.ajax({
            type:"post",
            dataType:"json",
            url:basePath+"/factory/billSchedule/saveMutiSchedule.do",
            data: {billData:JSON.stringify(dataArray),schedule:JSON.stringify(data)},
            success:function (result) {
                if(result.success == true || result.success == 'true') {
                    $("#modal-billSchedule-table").modal('hide');
                    $("#grid").trigger("reloadGrid");
                }else{
                    bootbox.alert("保存失败");
                }
            }
        })
    }
</script>