<%--
  Created by IntelliJ IDEA.
  User: czf
  Date: 2018/5/15

  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="show-findBirthNo-list" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog">
        <div class="modal-header no-padding">
            <div class="table-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">
                    <span class="white">&times;</span>
                </button>
                标签初始化
            </div>
        </div>
        <div class="modal-content">
            <div class="hr hr4"></div>
            <table id="findBirthNoListGrid"></table>
        </div>
    </div>
</div>

<script>
    //    $(function () {
    //        initUniqueCodeList();
    //    });

    function initBirthNoList() {
        debugger;
        $("#findBirthNoListGrid").jqGrid({
            height: 400,
            url: basePath + "/logistics/labelChangeBill/findInitByLabel.do?billNo=" + $("#search_billNo").val(),
            datatype: "json",
            mtype: "POST",
            colModel: [
                {name: 'billNo', label: '批次号', width: 150},
                {name: 'billDate', label: '导入时间', width: 150},
                {name: 'fileName', label: '导入文件名',  width: 150},
                {name: 'totEpc',label:'总数量',width:150}


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
                var row = $("#findBirthNoListGrid").jqGrid("getRowData", rowid);
                /* $(viewId).val(row.code);
                 $(viewTextId).val(row.name);
                 closeUnitSelectDialog();*/
                window.location.href=basePath+"/tag/birth/detail.do?billNo="+row.billNo;

                /*if(billNo.indexOf("SO")!=-1){
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

                }*/



            }
        });
        var parent_column = $("#findBirthNoListGrid").closest('.modal-dialog');
        $("#findBirthNoListGrid").jqGrid('setGridWidth', parent_column.width());
    }


</script>