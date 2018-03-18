/**
 * Created by yushen on 2017/9/19.
 */
$(function () {
    initGrid();
    keyDown();
});
function refresh() {
    location.reload(true);
}
function initGrid() {
    $("#grid").jqGrid({
        height: "auto",
        datatype: "json",
        colModel: [

            {name: 'id', label: '任务编号', editable: true, width: 200},
            {name: 'billNo', label: '单据编号', editable: true, width: 130},
            {
                name: "", label: "查看明细", width: 70, editable: false, align: "center", sortable: false,
                formatter: function (cellValue, options, rowObject) {
                    return "<a href='" + basePath + "/task/cargoTracking/taskDetail.do?id=" + rowObject.id + "&token=" + rowObject.token + "' title='任务明细'><i class='ace-icon fa fa-list'></i></a>"
                        + "<a style='margin-left: 20px' href='" + basePath + "/task/cargoTracking/billDetail.do?billNo=" + rowObject.billNo + "' title='单据明细'><i class='ace-icon fa fa-list'></i></a>";
                }
            },
            {name: 'deviceId', label: '设备号', editable: true, width: 100},
            {name: 'token', label: '出入库方式', editable: true, hidden: false, width: 100},
            {
                name: '', label: '出入库方式', editable: true, width: 100,
                formatter: function (cellValue, options, rowObject) {
                    switch (rowObject.token) {
                        case 8:
                            return "普通入库";
                        case 9:
                            return "仓库盘点";
                        case 10:
                            return "普通出库";
                        case 11:
                            return "加盟商入库";
                        case 12:
                            return "寄售入库";
                        case 13:
                            return "客户仓库退货出库";
                        case 23:
                            return "客户退货入库";
                        case 24:
                            return "调拨出库";
                        case 25:
                            return "调拨入库";
                        case 26:
                            return "退货出库";
                        case 28:
                            return "仓库调整出库";
                        case 29:
                            return "仓库调整入库";
                        case 32:
                            return "仓库其他出库";
                        default:
                            return "";
                    }
                }
            },
            {
                name: 'origUnitId', label: '发货方', editable: true, width: 200,
                formatter: function (value, options, rowObject) {
                    if (cs.isBlank(value)) {
                        return "";
                    }
                    return "[" + value + "]" + rowObject.origUnitName;
                }
            },
            {
                name: 'origId', label: '出库仓库', editable: true, width: 200,
                formatter: function (value, options, rowObject) {
                    if (cs.isBlank(value)) {
                        return "";
                    }
                    return "[" + value + "]" + rowObject.origName;
                }
            },
            {
                name: 'destUnitId', label: '收货方', width: 200,
                formatter: function (value, options, rowObject) {
                    if (value != undefined && rowObject.destUnitName != undefined) {
                        return '[' + value + ']' + rowObject.destUnitName;
                    } else if (value != undefined) {
                        return value;
                    } else {
                        return '';
                    }
                }
            },
            {
                name: 'destId', label: '收货仓库', width: 200, sortable: false,
                formatter: function (value, options, rowObject) {
                    if (cs.isBlank(value)) {
                        return "";
                    }
                    return "[" + value + "]" + rowObject.destName;
                }
            },
            {name: 'totEpc', label: '总数量', editable: true, width: 60},
            {name: 'totSku', label: 'SKU数', width: 60},
            {name: 'beginTime', label: '开始时间', width: 150},
            {name: 'endTime', label: '结束时间', editable: true, width: 150},


        ],
        viewrecords: true,
        autowidth: true,
        rownumbers: true,
        altRows: true,
        rowNum: 20,
        rowList: [20, 50, 100],
        pager: "#grid-pager",
        multiselect: false,
        shrinkToFit: false,
        sortname: 'beginTime',
        sortorder: "desc",
        autoScroll: false
    });
}

function _search() {
    if (($("#search_code").val() && $("#search_code").val() !== null ) || ($("#search_SKU").val() && $("#search_SKU").val() !== null)) {
        var serializeArray = $("#searchForm").serializeArray();
        var params = array2obj(serializeArray);
        $("#grid").jqGrid('setGridParam', {
            page: 1,
            url: basePath + "/task/cargoTracking/page.do",
            postData: params
        });
        $("#grid").trigger("reloadGrid");
    }else {
        $.gritter.add({
            text: "请扫描唯一码或者输入SKU",
            class_name: 'gritter-success  gritter-light'
        });
    }
}


function keyDown() {
    $("#search_code").keydown(function (event) {
        if (event.keyCode === 13) {
            var code = $("#search_code").val();
            $.ajax({
                dataType: "json",
                async: false,
                url: basePath + "/tag/tagReplace/findInfoInTagEpc.do",
                data: {code: code},
                type: "POST",
                success: function (data) {
                    if (data.success) {
                        $("#search_code").val(data.msg);
                        $("#search_SKU").val(data.result.code);
                        $("#search_styleId").val(data.result.styleId);
                        $("#search_colorId").val(data.result.colorId);
                        $("#search_sizeId").val(data.result.sizeId);
                    } else {
                        $("#orig_code").val("");
                        $.gritter.add({
                            text: data.msg,
                            class_name: 'gritter-success  gritter-light'
                        });
                    }
                }
            });
        }
    });
}

