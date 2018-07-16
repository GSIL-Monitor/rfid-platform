<%--
  Created by IntelliJ IDEA.
  User: yushen
  Date: 2017/9/25
  Time: 18:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="show-findRetrunNo-list" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog">
        <div class="modal-header no-padding">
            <div class="table-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">
                    <span class="white">&times;</span>
                </button>
                退货单
            </div>
        </div>
        <div class="modal-content">
            <div class="hr hr4"></div>
            <table id="findRetrunNoListGrid"></table>
        </div>
    </div>
</div>

<script>
    //    $(function () {
    //        initUniqueCodeList();
    //    });

    function initUniqueretrunList() {
        debugger;
        console.log($("#edit_billNo").val());
        $("#findRetrunNoListGrid").jqGrid({
            height: 400,
            url: basePath + "/logistics/Consignment/findSaleOrderReturnBillNo.do?billno=" + $("#edit_billNo").val(),
            datatype: "json",
            mtype: "POST",
            colModel: [
                {name: 'id', label: '单号', width: 150},
                {name: 'billDate', label: '时间', width: 150},
                {name: 'origUnitName', label: '退货客户',  width: 150},
                {name: 'destUnitName',label:'收货方',width:150},
                {name: 'totQty', label: '数量',  width: 150},

            ],
            rownumbers: true,
            viewrecords: true,
            autowidth: true,
            altRows: true,
            multiselect: false,
            shrinkToFit: true,
            sortname: 'code',
            sortorder: "desc",
            ondblClickRow: function (rowid) {
                var row = $("#findRetrunNoListGrid").jqGrid("getRowData", rowid);
               /* $(viewId).val(row.code);
                $(viewTextId).val(row.name);
                closeUnitSelectDialog();*/

               if(billNo.indexOf("CM")!=-1){
                   $.ajax({
                       url: basePath +"/logistics/Consignment/quit.do?billNo=" +billNo,
                       cache: false,
                       async: false,
                       type: "POST",
                       success: function (data, textStatus) {

                           if(textStatus=="success"){
                               $.gritter.add({
                                   text: billNo+"可以编辑",
                                   class_name: 'gritter-success  gritter-light'
                               });
                               window.location.href=basePath+"/logistics/saleOrderReturn/findsaleReturn.do?billNo="+row.id+"&url=/logistics/Consignment/index.do";
                           }

                       }
                   });

               }
               if(billNo.indexOf("SO")!=-1){
                   $.ajax({
                       url: basePath +"/logistics/saleOrder/quit.do?billNo=" +billNo,
                       cache: false,
                       async: false,
                       type: "POST",
                       success: function (data, textStatus) {

                           if(textStatus=="success"){
                               $.gritter.add({
                                   text: billNo+"可以编辑",
                                   class_name: 'gritter-success  gritter-light'
                               });
                               window.location.href=basePath+"/logistics/saleOrderReturn/findsaleReturn.do?billNo="+row.id+"&url=/logistics/saleOrder/index.do";
                           }

                       }
                   });

               }



            }
        });
        var parent_column = $("#findRetrunNoListGrid").closest('.modal-dialog');
        $("#findRetrunNoListGrid").jqGrid('setGridWidth', parent_column.width());
    }

    function retrunListReload() {
        debugger;
        $("#uniqueCodeListGrid").clearGridData();
        $("#uniqueCodeListGrid").jqGrid('setGridParam', {
            url: basePath + "/logistics/Consignment/findSaleOrderReturnBillNo.do?billno=" + $("#edit_billNo").val()
        }).trigger("reloadGrid");
    }
</script>