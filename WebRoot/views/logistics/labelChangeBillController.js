$(function () {
    initGrid();
    initSelectOrigForm();
    initSelectclass9();
});
function showAdvSearchPanel() {

    $("#searchPanel").slideToggle("fast");

}
function initGrid() {
    $("#grid").jqGrid({
        height: "auto",
        url: basePath + "/logistics/labelChangeBill/page.do",
        datatype: "json",
        mtype: 'POST',
        sortorder: 'desc',
        colModel: [

            {name: 'billNo', label: '单据编号', sortable: true, width: 40},
            {
                name: "", label: "操作", width: 60, editable: false, align: "center",
                formatter: function (cellvalue, options, rowObject) {
                    var billNo = rowObject.billNo;
                    var html;
                    html = "<a style='margin-left: 20px' href='" + basePath + "/logistics/labelChangeBill/edit.do?billNo=" + billNo + "'><i class='ace-icon fa fa-edit' title='编辑'></i></a>";
                    if(rowObject.status!=2){
                        html += "<a style='margin-left: 20px' href='#' onclick=cancel('" + billNo + "')><i class='ace-icon fa fa-undo' title='撤销'></i></a>";
                    }
                    return html;
                }
            },
            {name: 'status', hidden: true},
            {name: 'billDate', label: '单据日期', sortable: true, width: 40},
            {name: 'origName', label: '仓库', width: 40},
            {name: 'beforeclass9Name', label: '原系列', width: 40},
            {name: 'nowclass9Name', label: '现系列', width: 40},
            {name: 'changeTypeName', label: '类型', width: 40},
            {name: 'remark', label: '备注', sortable: false, width: 40},
            {name: 'id', hidden: true}
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
            //setFooterData();
        },
        onSelectRow: function (rowid, status) {
        }
    });
}

function add(type) {
    location.href = basePath + "/logistics/labelChangeBill/add.do?type="+type;
}

function cancel(billNo) {
    cs.showProgressBar();
    $.ajax({
        dataType: "json",
        async: true,
        url: basePath + "/logistics/labelChangeBill/cancel.do",
        data: {
            billNo: billNo
        },
        type: "POST",
        success: function (msg) {
            cs.closeProgressBar();

            if (msg.success) {
                $.gritter.add({
                    text: msg.msg,
                    class_name: 'gritter-success  gritter-light'
                });
            } else {
                bootbox.alert(msg.msg);
            }
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
        url: basePath + "/logistics/labelChangeBill/page.do",
        postData: params
    });
    $("#grid").trigger("reloadGrid");
}
function initSelectOrigForm() {
    $.ajax({
        url: basePath + "/unit/list.do?filter_EQI_type=9",
        cache: false,
        async: true,
        type: "POST",
        success: function (data, textStatus) {
            $("#search_origId").empty();
            $("#search_origId").append("<option value=''>--请选择仓库--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#search_origId").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
            }
            $('.selectpicker').selectpicker('refresh');
        }
    });
}
function initSelectclass9() {
    $.ajax({
        url: basePath + "/sys/property/findclass9name.do?filter_EQS_type=C9",
        cache: false,
        async: true,
        type: "POST",
        success: function (data, textStatus) {
            $("#search_beforeclass9").empty();
            $("#search_nowclass9").empty();
            $("#search_beforeclass9").append("<option value='' style='background-color: #eeeeee'>--请选择原系列--</option>");
            $("#search_nowclass9").append("<option value='' style='background-color: #eeeeee'>--请选择原系列--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#search_beforeclass9").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
                $("#search_nowclass9").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
                $("#search_beforeclass9").trigger('chosen:updated');
                $("#search_nowclass9").trigger('chosen:updated');
            }
        }
    });


}