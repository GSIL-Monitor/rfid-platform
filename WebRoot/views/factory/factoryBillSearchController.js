var searchUrl = basePath + "/factory/billSearch/page.do";
$(function () {
    initDate();
    initGrid();
    initSelect();
    _search();
});

function initDate(){
    var startDate = getMonthFirstDay("yyyy-MM-dd");
    var endDate = getToDay("yyyy-MM-dd");
    $('.startDate').datepicker('setDate', startDate);
    $('.endDate').datepicker('setDate', endDate);
}
function initSelect() {
    $.ajax({
        dataType: "json",
        url: basePath + "/factory/token/findToken.do",
        cache: false,
        async: false,
        type: "POST",
        success: function (data) {
            var json = data.result;
            $("#search_progress").append("<option value=''>-请选择-</option>");
            for (var i = 0; i < json.length; i++) {
                $("#search_progress").append("<option value='" + json[i].token + "'>" + json[i].name + "</option>");
                $("#search_progress").trigger('chosen:updated');
            }
        }
    });
    $.ajax({
        dataType: "json",
        url: basePath + "/factory/token/findCategory.do",
        cache: false,
        async: false,
        type: "POST",
        success: function (data) {
            var json = data.result;
            $("#search_category").append("<option value=''>-请选择-</option>");
            for (var i = 0; i < json.length; i++) {
                $("#search_category").append("<option value='" + json[i].code + "'>" + json[i].name + "</option>");
                $("#search_category").trigger('chosen:updated');
            }
        }
    });
}
function _search() {
    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);
    $("#grid").jqGrid('setGridParam', {
        url: searchUrl,
        page: 1,
        postData: params
    });
    $("#grid").trigger("reloadGrid");
}

function _clearSearch() {
    $("#searchForm").resetForm();
    initDate();
}
function refresh() {
    _clearSearch();
}
function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");
}
function initGrid() {
    $("#grid").jqGrid({
        height: "auto",
        mtype: "POST",
        datatype: "json",
        colModel: [
            {name: 'groupId', label: '组别', editable: true, width: 180},
            {name: 'billOperator', label: '开单人', editable: true, width: 180},
            {name: 'customerId', label: '客户', editable: true, width: 200},
            {name: 'season', label: '季度', editable: true, width: 180},
            {name: 'sex', label: '男/女装', editable: true, width: 180},
            {name: 'factory', label: '工厂', editable: true, width: 180},
            {name: 'billNo', label: '办单单号', editable: true, width: 180},
            {name: 'billDate', label: '发单日期', editable: true, width: 180},
            {name: 'printDate', label: '打印时间', editable: true, width: 180},
            {name: 'type', label: '办类', editable: true, width: 180},
            {name: 'shirtType', label: '衫型', editable: true, width: 180},
            {name: 'billQty', label: '办单件数', editable: true, width: 180},
            {name: 'endDate', label: '办期', editable: true, width: 180},
            {name: 'progress', label: '办单进度', editable: true, width: 180,
                formatter: function (cellValue, options, rowObject) {
                    if(cellValue != undefined){
                        return rowObject.progressName;
                    }else{
                        return "";
                    }
                }
            },
            {name: 'washType', label: '洗水类型', editable: true, width: 180}
        ],
        viewrecords: true,
        rownumbers: true,
        altRows: true,
        rowNum: 20,
        rowList: [20, 50, 100],
        pager: "#grid-pager",
        multiselect: false,
        sortname: 'billDate',
        sortorder: "desc",
        shrinkToFit: false,
        autoScroll: false,
        autowidth: true
    });
}
function exportExcel() {
    var datafrom=$("#searchForm").serialize();
    window.location.href=basePath+"/factory/billSearch/exportExcel.do?"+datafrom;

}
