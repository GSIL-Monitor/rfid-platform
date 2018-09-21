<%--
  Created by IntelliJ IDEA.
  User: yushen
  Date: 2017/9/25
  Time: 18:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="show-in-uniqueCode-list" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog">
        <div class="modal-header no-padding">
            <div class="table-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">
                    <span class="white">&times;</span>
                </button>
                唯一码明细
            </div>
        </div>
        <div class="modal-content">
            <div class="hr hr4"></div>
            <table id="inuniqueCodeListGrid"></table>
        </div>
        <div class="modal-footer no-margin-top">
            <button type='button' id='fb_comfirm_in' class='btn btn-primary' onclick='confirm_warehousing(epcinArray)'>确认入库</button>
        </div>
    </div>
</div>

<script>
    //    $(function () {
    //        initUniqueCodeList();
    //    });
    var epcinArray=[];
    function initUniqueCodeListin(uniqueCodes) {
        debugger;
        $("#inuniqueCodeListGrid").jqGrid({
            height: 400,
            url: basePath + "/stock/warehStock/findCodeinList.do?uniqueCodes=" + uniqueCodes+"&origId="+$("#search_origId").val()+"&destId="+$("#search_destId").val(),
            datatype: "json",
            mtype: "POST",
            colModel: [
                {name: 'code', label: '唯一码', width: 150},
                {name: 'sku', label: 'SKU', width: 150},
                {name: 'warehouseId', label: '仓库', hidden:true},
                {name: 'styleId', label: '款号', width: 150},
                {name: 'colorId', label: '色码', width: 150},
                {name: 'sizeId', label: '尺码', width: 150},
                {name: 'floor',label:'仓库名',width:150},
                {name: 'inStock', label: '库存状态', hidden: true},
                {
                    name: '', label: '库存状态', width: 110,
                    formatter: function (cellValue, options, rowObject) {
                        switch (rowObject.inStock) {
                            case 1:
                                return "在库";
                            case 0:
                                return "不在库";
                            default:
                                return "";
                        }
                    }
                }
            ],
            rownumbers: true,
            viewrecords: true,
            autowidth: true,
            altRows: true,
            rowNum:-1,
            multiselect: true,
            shrinkToFit: true,
            sortname: 'code',
            sortorder: "desc",
            onSelectRow: function (rowId,status) {
                debugger;
                var row = $("#inuniqueCodeListGrid").jqGrid('getRowData', rowId);
                var status=status;
                if(status){
                    epcinArray.push(row)
                }else{
                    for(var i=0;i<epcinArray.length;i++){
                        if(epcinArray[i].code==row.code){
                            epcinArray.splice(i,1);
                        }
                    }
                }
            },
            onSelectAll:function (aRowids,status) {
                debugger;
                var status=status;
                if(status){
                    epcinArray=[];
                    for(var i=0;i<aRowids.length;i++){
                        var row = $("#inuniqueCodeListGrid").jqGrid('getRowData', aRowids[i]);
                        epcinArray.push(row)
                    }
                }else{
                    epcinArray=[];
                    for(var i=0;i<aRowids.length;i++){
                        var row = $("#inuniqueCodeListGrid").jqGrid('getRowData', aRowids[i]);
                        for(var a=0;a<epcinArray.length;a++){
                            if(epcinArray[a].code==row.code){
                                epcinArray.splice(a,1);
                            }
                        }
                    }
                }

            }
        });
        var parent_column = $("#inuniqueCodeListGrid").closest('.modal-dialog');
        $("#inuniqueCodeListGrid").jqGrid('setGridWidth', parent_column.width());
    }

    function codeListReloadin(uniqueCodes) {
        $("#inuniqueCodeListGrid").clearGridData();
        $("#inuniqueCodeListGrid").jqGrid('setGridParam', {
            url: basePath + "/stock/warehStock/findCodeinList.do?uniqueCodes=" + uniqueCodes+"&origId="+$("#search_origId").val()+"&destId="+$("#search_destId").val()
        }).trigger("reloadGrid");
    }
</script>