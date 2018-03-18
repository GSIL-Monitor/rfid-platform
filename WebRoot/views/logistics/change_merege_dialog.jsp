<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="modal-merege-table" class="modal fade" role="dialog" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-header no-padding">
            <div class="table-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    <span class="white">&times;</span>
                </button>
                转换信息
            </div>
        </div>
        <div class="modal-content no-padding" >
            <table id="meregegrid" ></table>
        </div>
        <div class="modal-footer">
            <button type="button"  class="btn btn-primary" onclick="savemerege()">保存</button>
        </div>
    </div>
</div>

<jsp:include page="../base/search_vendor_dialog.jsp"></jsp:include>
<script>

    $(function () {
        initEpcGrid();
    });

    var befornowChange=0;
    var editDtailiRow = null;
    var editDtailiCol = null;
    function initEpcGrid() {
        $("#meregegrid").jqGrid({
            height:  "350",
            datatype: "json",
            colModel: [
                {name: 'id', label: 'id', hidden: true, width: 240},
                {name: 'billNo', label: 'billNo', hidden: true, width: 240},
                {name: 'origUnitId', label: 'origUnitId', hidden: true, width: 240},

                {name: 'isChange', label: 'isChange', hidden: true, width: 240},
                {name:'recordid',label:'款号和颜色',width:200},
                {name:'sizeid',label:'尺寸',width:200},
                {name:'qty',label:'数量',width:200},
                {name:'franchiseeStockQty',label:'加盟店库存数量',width:200},
                {name:'stockQty',label:'库存数量',width:200},
                {name:'alreadyChange',label:'以转换数量',width:200},
                {name:'nowChange',label:'本次转换数量',editable:true,width:200},
                {
                    name: "", label: "供应商", width: 300,editable:false,align:"center",frozen:true,sortable:false,
                    formatter: function (cellvalue, options, rowObject) {
                        var id=rowObject.id;
                        /*  return "<a href='"+basePath+"/task/outbound/detail.do?id="+id+"'><i class='ace-icon fa fa-list'></i></a>";*/
                      /*  var select="  <select class='form-control selectpicker show-tick' data-live-search='true' style='width: 100%;'> <option value='asd'>asd</option></select>"

                        return select;*/
                        var select="<button class='btn btn-sm btn-default'type='button' onclick='openSearchVendorDialog(\""+id+"\")'> <i class='ace-icon fa fa-list'></i> </button>";
                        //var select="<a href='javascript:void(0);' onclick=openSearchVendorDialog('" +rowObject.id + ")'><i class='ace-icon ace-icon fa fa-save' title='选择供应商'></i></a>"
                        return select;
                    }
                },
                {name: 'origUnitName', label: '供应商名称', width: 240}

            ],
            viewrecords: true,
            autowidth: true,
            rownumbers: true,
            altRows: true,
            rowNum:-1,
            shrinkToFit: true,
            sortname: 'code',
            sortorder: "asc",
            cellEdit: true,
            cellsubmit: 'clientArray',
            beforeEditCell: function (rowid, celname, value, iRow, iCol) {
                befornowChange=$('#meregegrid').getCell(rowid, "nowChange");
            },
            afterEditCell: function (rowid, celname, value, iRow, iCol) {
                editDtailiRow = iRow;
                editDtailiCol = iCol;
            },
            afterSaveCell: function (rowid, cellname, value, iRow, iCol) {
                var qty=$('#meregegrid').getCell(rowid, "qty");
                var alreadyChange=$('#meregegrid').getCell(rowid, "alreadyChange");
                var nowChange=$('#meregegrid').getCell(rowid, "nowChange");
                if((parseInt(alreadyChange)+parseInt(nowChange))>parseInt(qty)){
                    $.gritter.add({
                        text: "已超过转换数量",
                        class_name: 'gritter-success  gritter-light'
                    });
                    $('#meregegrid').setCell(rowid, "nowChange", befornowChange);
                }

            }


        });
        var parent_column = $("#meregegrid").closest('.modal-dialog');
        console.log(parent_column.width());
        $("#meregegrid").jqGrid('setGridWidth', parent_column.width() - 5);


    }

   /* function savemerege() {

        $("#meregegrid").saveCell	(editDtailiRow,editDtailiCol);
        editDtailiRow=null;
        editDtailiCol=null;
        var dtlArray = [];
        $.each($("#meregegrid").getDataIDs(), function (index, value) {
            var rowData = $("#meregegrid").getRowData(value);
            dtlArray.push(rowData);
        });
        showWaitingPage();
        $.ajax({
            dataType: "json",
            // async:false,
            url: basePath + "/logistics/mergeReplenishBillController/saveRecordsize.do",
            data: {
                'strDtlList': JSON.stringify(dtlArray),
            },
            type: "POST",
            success: function (msg) {
                hideWaitingPage();
                if (msg.success) {
                    $.gritter.add({
                        text: msg.msg,
                        class_name: 'gritter-success  gritter-light'
                    });
                    $("#search_billNo").val(msg.result);
                    billNo = msg.result;
                    issaleretrun=true;
                } else {
                    bootbox.alert(msg.msg);
                }
            }
        });
        $("#modal-merege-table").modal('hide');

    }*/
    var dialogOpenPage;
    var saveid;
    function openSearchVendorDialog(id) {
        saveid=id;
        dialogOpenPage = "meregegridDialog";
        $("#modal_vendor_search_table").modal('show').on('shown.bs.modal', function () {
            initVendorSelect_Grid();
        });
        $("#searchVendorDialog_buttonGroup").html("" +
            "<button type='button'  class='btn btn-primary' onclick='confirm_selected_VendorId_purchaseOrders(\""+id+"\")'>确认</button>"
        );
    }
    function confirm_selected_VendorId_purchaseOrders(id) {
        debugger;
        var rowId = $("#vendorSelect_Grid").jqGrid("getGridParam", "selrow");
        var rowData = $("#vendorSelect_Grid").jqGrid('getRowData', rowId);
        $('#meregegrid').setCell(id, "origUnitId", rowData.id);
        $('#meregegrid').setCell(id, "origUnitName", rowData.name);
        $("#modal_vendor_search_table").modal('hide');

    }

</script>