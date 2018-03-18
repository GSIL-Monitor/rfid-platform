$(function () {
    iniGrid();
});

function refresh() {
    location.reload(true);
}

function _search() {
    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);

    $("#grid").jqGrid('setGridParam', {
        url: basePath + "/unit/page.do?filter_EQI_type=0",
        page: 1,
        postData: params
    });
    $("#grid").trigger("reloadGrid");
}

function showSearchPannel() {
    $("#search-pannel").slideToggle("fast");
}

function addGuest() {
    location.href = basePath + "/unit/add.do";
}

function editGuest(selrow) {
    // var selrow =$("#grid").jqGrid("getGridParam","selrow");
    // if(selrow){
    var row = $("#grid").jqGrid("getRowData", selrow);
    console.log("unitType="+row.unitType);
    location.href = basePath + "/sys/guest/edit.do?id=" + row.id+"&unitType="+row.unitType;
    // } else {
    //     bootbox.alert("请先选择一项");
    // }
}

function changeStatus(rowId) {
    var row = $("#grid").jqGrid('getRowData', rowId);
    var status = row.status;
    cs.showProgressBar("状态更改中");
    $.ajax({
        url: basePath + '/sys/unit/changeStatus.do',
        dataType: 'json',
        data: {
            id: row.id,
            status: status
        },
        success: function (result) {
            cs.closeProgressBar();
            if (result.success) {
                $.gritter.add({
                    text: result.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                $("#grid").trigger('reloadGrid');
            } else {
                $.gritter.add({
                    text: result.msg,
                    class_name: 'gritter-fail  gritter-light'
                });
            }
        }
    });

}

function iniGrid() {
    $("#grid").jqGrid({
        height: "auto",
        url: basePath + "/unit/page.do?filter_EQI_type=0",
        mtype: "POST",
        datatype: "json",
        colModel: [
            {name: 'id', label: '供应商名称', width: 100, editable: true},
            {name: 'name', label: '联系人', editable: true, width: 100},
            {
                name: "", label: "查看明细", width: 200, editable: false, align: "center",
                formatter: function (cellvalue, options, rowObject) {
                    var guestId = rowObject.id;
                    var html;
                    html = "<a style='margin-left: 20px' href='" + basePath + "/unit/viewStatement.do?id=" + guestId + "'><i class='ace-icon fa fa-list' title='查看流水单'></i></a>";
                    return html;

                }
            },
            {
                name: 'owingValue', label: '欠款', editable: true, width: 100,
                formatter: function (cellvalue, options, rowObject) {
                    return cellvalue.toFixed(2)
                }
            }

        ],
        viewrecords: true,
        autowidth: true,
        rownumbers: true,
        altRows: true,
        rowNum: 20,
        rowList: [20, 50, 100],
        pager: "#gridPager",
        multiselect: false,
        shrinkToFit: true,
        /*sortname: 'createTime',*/
        sortorder: "desc",
        autoScroll: false
    });

}