/**
 * Created by yushen on 2017/7/12.
 */
var searchUrl = basePath + "/logistics/accountStatementView/page.do";

$(function () {
    initDate();
    initGrid();
});

function initDate() {
    var startDate = getMonthFirstDay("yyyy-MM-dd");
    var endDate = getToDay("yyyy-MM-dd");
    $('.startDate').datepicker('setDate', startDate);
    $('.endDate').datepicker('setDate', endDate);
}

function initGrid() {
    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);
    $("#grid").jqGrid({
        height: "auto",
        mtype: "POST",
        datatype: "json",
        url: searchUrl,
        postData: params,
        colModel: [
            {name: 'billNo', label: '编号', sortable: false, width: 200},
            {name: 'billDate', label: '单据日期', sortable: false, width: 200},
            {name: 'billType', label: '单据类型', sortable: false, width: 150},
            {name: 'unitType', label: '对象类型', sortable: false, width: 150, hidden: true},
            {
                name: '', label: '对象类型', sortable: false, width: 150, hidden: true,
                formatter: function (cellValue, options, rowObject) {
                    if(rowObject.unitType === "1"){
                        return "客户";
                    }else if(rowObject.unitType === "0"){
                        return "供应商";
                    }
                }
            },
            {name: 'unitId', label: '对象', sortable: false, hidden: true},
            {name: 'actPrice', label: '实际价格', sortable: false, width: 150},
            {name: 'payPrice', label: '支付价格', sortable: false, width: 150},
            {name: 'diffPrice', label: '本单差额', sortable: false, width: 150},
            {name: 'oprId', label: '操作人', sortable: false, width: 150},
            {name: 'remark', label: '备注', sortable: false, width: 150},
            {name: 'ownerId', label: '单据所属', sortable: false, width: 150},
            {name: 'groupId', label: '组号', hidden: true},
            {name: 'totalOwingVal', label: '累计欠款', sortable: false, width: 150,
                formatter: function (cellValue, options, rowObject) {
                    if(cellValue){
                        return cellValue.toFixed(2);
                    }else{
                        return cellValue;
                    }
                }
            },
            {name: 'donationPrice', label: '赠送金额', width: 150}

        ],
        viewrecords: true,
        autowidth: true,
        rownumbers: true,
        altRows: true,
        rowNum: 200,
        rowList: [200, 500, 1000],
        pager: "#grid-pager",
        multiselect: false,
        shrinkToFit: true,
        // sortname: 'billDate',
        // sortorder: 'asc',
        autoScroll: false,

        grouping: true,
        groupingView: {
            groupField: ['groupId'],
            groupColumnShow: [false],
            groupText: ['<b>{0}</b>'],
            plusicon: 'ace-icon fa fa-plus',
            minusicon: 'ace-icon fa fa-minus'
        }
    });
}

function refresh() {
    location.reload(true);
}


function _search() {

    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);
    $("#grid").jqGrid('setGridParam', {
        page: 1,
        url: searchUrl,
        postData: params
    });
    $("#grid").trigger("reloadGrid");
}

function gathering() {
    $("#gatheringForm").resetForm();
    $("#gathering_dialog").modal('show').on('hidden.bs.modal',function (){
        refresh();
    });
    $("#form_guestId").val($("#search_guestId").val());
    // $("#form_guestName").val($("#search_guestName").val());

    var owing = $("#search_guestOwingValue").val();
    $("#form_owingValue").val(owing);
    var date = getToDay("yyyy-MM-dd");
    $("#form_billDate").val(date);
}

function initValueModify() {
    $("#initialAdjustmentForm").resetForm();
    $("#initialAdjustment_dialog").modal('show').on('hidden.bs.modal',function () {
        refresh();
    });
    $("#form_unitId").val($("#search_guestId").val());
    var nowDate=new Date();
    var yearStr = nowDate.getFullYear();
    var monthStr = nowDate.getMonth()+1;
    $("#form_month").val(yearStr+"-"+monthStr);
    $.ajax({
        dataType: "json",
        url: basePath + "/logistics/monthAccountStatement/getMas.do",
        data: {
            masId: masId
        },
        type: "POST",
        success: function (result) {
            if (result.success) {
                $("#form_preVal").val(result.result.totVal);
            } else {
                $("#form_preVal").val(0);
            }
        }
    });

}

