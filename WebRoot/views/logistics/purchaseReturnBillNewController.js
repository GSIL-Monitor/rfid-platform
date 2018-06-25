$(function () {
    /*初始化左侧grig*/
    initSearchGrid();
    /*初始化from表单*/
    initSearchAndEditForm();
});
function initSearchGrid() {
    $("#grid").jqGrid({
        height: 'auto',
        datatype: 'json',
        url: basePath + "/logistics/purchaseReturn/page.do",
        mtype: 'POST',
        colModel: [
            {name: 'billNo', label: '单号', editable: true, width: 40},
            {name: 'status', hidden: true},
            {name: 'outStatus', hidden: true},
            {
                name: "", label: '状态', editable: true, width: 20, align: 'center',sortable: false,
                formatter: function (cellValue, options, rowObject) {
                    var html = "";
                    switch (rowObject.status) {
                        case -1 :
                            html = "<i class='fa fa-undo blue' title='撤销'></i>";
                            break;
                        case 0 :
                            html = "<i class='fa fa-caret-square-o-down blue' title='录入'></i>";
                            break;
                        case 1:
                            html = "<i class='fa fa-check-square-o blue' title='审核'></i>";
                            break;
                        case 2 :
                            html = "<i class='fa fa-archive blue' title='结束'></i>";
                            break;
                        case 3:
                            html = "<i class='fa fa-wrench blue' title='操作中'></i>";
                            break;
                        case 4:
                            html = "<i class='fa fa-paper-plane blue' title='申请撤销'></i>";
                            break;
                        default:
                            break;
                    }
                    return html;
                }
            },
            {
                name: 'outStatusImg', label: '出库状态', width: 30, align: 'center',sortable: false,
                formatter: function (cellValue, options, rowObject) {
                    if (rowObject.outStatus == 0) {
                        return '<i class="fa fa-tasks blue" title="订单状态"></i>';
                    } else if (rowObject.outStatus == 2) {
                        return '<i class="fa fa-sign-out blue" title="已出库"></i>';
                    } else if (rowObject.outStatus == 3) {
                        return '<i class="fa fa-truck blue" title="出库中"></i>';
                    } else {
                        return '';
                    }
                }
            },
            {name: 'billDate', label: '单据日期', width: 40},
            {name: 'origUnitId', label: '发货方ID', hidden: true},
            {name: 'origUnitName', label: '发货方', width: 40,hidden: true},
            {name: 'origId', label: '出库仓库ID', hidden: true,hidden: true},
            {name: 'origName', label: '出库仓库', width: 40,hidden: true},
            {name: 'destUnitId', label: '供应商ID', hidden: true},
            {name: 'destUnitName', label: '供应商', width: 40,hidden: true},
            {name: 'destId', label: '收货仓库ID', hidden: true},
            {name: 'destName', label: '收货仓库', editable: true, width: 40,hidden: true},
            {name: 'totOutQty', label: '已出库数量', width: 40,hidden: true},
            {name: "payPrice", label: '退货金额', width: 40,hidden: true},
            {name: 'totQty', label: '单据数量', editable: true, width: 40},
            {name: 'remark', label: '备注', editable: true, width: 40},
        ],
        viewrecords: true,
        autowidth: true,
        rownumbers: true,
        altRows: true,
        rowNum: 20,
        rowList: [20, 50, 100],
        pager: "#grid-pager",
        multiselect: false,
        shrinkToFit: true,
        sortname: 'billNo',
        autoScroll: false,
        footerrow: true,
        gridComplete: function () {
            setFooterData();
        },
        onSelectRow: function (rowid, status) {

        }

    });
    $("#grid").jqGrid("setFrozenColumns");
}
function initSearchAndEditForm() {
    initSelectOrigForm();
    $(".selectpicker").selectpicker('refresh');
}
function initSelectOrigForm() {
    //"/unit/list.do?filter_EQI_type=9&filter_EQS_ownerId=" + $("#search_unitId").val()
    $.ajax({
        url: basePath + "/unit/list.do?filter_EQI_type=9",
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {
            $("#search_origId").empty();
            $("#search_origId").append("<option value=''>--请选择入库仓库--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#search_origId").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
            }
        }
    });
}