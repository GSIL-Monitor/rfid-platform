$(function () {
    //初始化
    initGrid();
    keydown();
    $(".k-dropdown").css("width", "6em");
    $(".k-grid-toolbar").css("display", "none");//隐藏toolbar

});
function refresh() {
    location.reload(true);
}
function exportExcel() {
    $(".k-grid-excel").click();
}
function initGrid() {
    $("#grid").kendoGrid({
        toolbar: ["excel"],
        excel: {
            allPages: true
        },
        dataSource: {
            schema: {
                total: "total",
                model: {
                    fields: {
                        epc: {type: "string"},
                        code: {type: "string"},
                        updateTime: {type: "string"},
                        styleId: {type: "string"},
                        colorId: {type: "string"},
                        sizeId: {type: "string"},
                        styleName: {type: "string"},
                        colorName: {type: "string"},
                        sizeName: {type: "string"},
                        version: {type: "string"},
                        disabled: {type: "boolean"}
                    }
                },
                data: "data"
            },
            filter: { field: "disabled", operator: "neq", value: true},
            group: [{
                field: "code",
                aggregates: [
                    {field: "epc", aggregate: "count"}
                ]
            }],
            transport: {
                read: {
                    url: basePath + "/tag/bind/page.do",
                    type: "POST",
                    dataType: "json",
                    contentType: 'application/json'
                },
                parameterMap: function (options) {
                    return JSON.stringify(options);
                }
            },
            sort: [{field: "updateTime", dir: "desc"}],
            pageSize: 500.0,
            serverSorting: true,
            serverPaging: true,
            serverGrouping: false,
            serverAggregates: true,
            serverFiltering: true,
            aggregate: [
                {field: "epc", aggregate: "count"},
            ]
        },
        sortable: {
            mode: "multiple",
            allowUnsort: true
        },
        pageable: {
            input: true,
            buttonCount: 5,
            pageSize: 500.0,
            pageSizes: [100, 500, 1000, 2000, 5000]
        },

        groupable: true,
        columnMenu: true,
        filterable: {
            extra: false
        },
        columns: [
            {
                field: "disabled", title: "状态", width: "120px",sortable:false, template: function (data) {
                if (data.disabled == true) {
                    return "已解绑";
                } else {
                    return "已绑定";
                }
            }
            },
            {
                field: "code", title: "条码", width: "180px",/* sortable: false,*/ aggregates: ["count"],
                groupHeaderTemplate: function (data) {
                    if (data.aggregates.epc != undefined) {
                        var countepc = data.aggregates.epc.count;
                        if (countepc == null) {
                            countepc = 0;
                        }
                        var value = data.value;
                        if (value == null) {
                            value = "无";
                        }
                        return "条码:" + value + " EPC数量:" + countepc;
                    } else {
                        return value;
                    }

                },
                sortable: {
                    compare: function(a, b) {
                        return numbers[a.updateTime] - numbers[b.updateTime];
                    }
                }
            },
            {field: "epc", title: "EPC", width: "200px", sortable: false, groupable: false, aggregates: ["count"]},
            {field: "updateTime", title: "日期", width: "160px", sortable: true, groupable: false},
            {field: "styleId", title: "款号", width: "120px", sortable: false, groupable: false},
            {field: " styleName", title: "款名", width: "120px", sortable: false, groupable: false},
            {field: "colorId", title: "色码", width: "70px", sortable: false, groupable: false},
            {field: "colorName", title: "颜色", width: "180px", sortable: false, groupable: false},
            {field: "sizeId", title: "尺码", width: "70px", sortable: false, groupable: false},
            {field: "sizeName", title: "尺寸", width: "140px", sortable: false, groupable: false},
            {field: "version", title: "版本", width: "70px", sortable: false, groupable: false}
        ]
    });
}
function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");

}
function initunbindGrid() {

    $("#unbindgrid").jqGrid({
        height: 400,
        datatype: "json",
        colModel: [
            {name: 'epc', label: 'EPC', editable: true, width: 40},
            {name: 'code', label: '条码', editable: true, width: 40},
            {name: 'updateTime', label: '修改时间', hidden: true, width: 40},
            //{name: 'styleId', label: '款号', editable: true, width: 40},
            //{name: 'colorId', label: '色码', editable: true, width: 40},
            //{name: 'sizeId', label: '尺码', editable: true, width: 40},
        ],
        rownumbers: true,
        viewrecords: true,
        autowidth: true,
        altRows: true,
        multiselect: false,
        shrinkToFit: true,
        sortname: 'updateTime',
        sortorder: "desc"
    });
    var parent_column = $("#unbindgrid").closest('.modal-dialog');
    $("#unbindgrid").jqGrid('setGridWidth', parent_column.width() - 5);

}


function _search() {
   /* var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);
    $("#grid").jqGrid('setGridParam', {
        page: 1,
        postData: params
    });
    $("#grid").trigger("reloadGrid");*/
    var gridData = $("#grid").data("kendoGrid");
    var filters = serializeToFilter($("#searchForm"));
    filters.push({
        field: "disabled", operator: "neq", value: true
    });
     gridData.dataSource.filter({
        logic: "and",
        filters: filters
    });
}

function bind() {
    initbindGrid();
    $("#edit-dialog").modal({backdrop: 'static', keyboard: false});
}
function del() {
    $("#unbind-dialog").modal({backdrop: 'static', keyboard: false});
    initunbindGrid();


}


function initbindGrid() {

    $("#bindgrid").jqGrid({
        height: 400,
        datatype: "json",
        colModel: [
            {name: 'code', label: '条码', editable: true, width: 280},
            {name: 'epc', label: 'EPC', editable: true, width: 280},
            {name: 'updateTime', label: '修改时间', hidden: true, width: 40},
        ],
        viewrecords: true,
        rownumbers: true,
        autowidth: true,
        altRows: true,
        multiselect: false,
        shrinkToFit: true,
        sortname: 'updateTime',
        sortorder: "asc"
    });
    var parent_column = $("#bindgrid").closest('.modal-dialog');
    $("#bindgrid").jqGrid('setGridWidth', parent_column.width() - 5);
}

function keydown() {
    //监听回车键
    $("#form_code").keyup(function (event) {
        if (event.keyCode == 13) {
            var code = document.getElementById("form_code").value;//获取文本框的值
            $.ajax({
                url: basePath + "/tag/bind/findproduct.do",
                data: {
                    code: code
                },
                datatype: "json",
                type: "POST",
                success: function (data) {
                    if (data.success) {
                        document.getElementById("form_styleName").value = data.result.styleName;
                        document.getElementById("form_colorId").value = data.result.colorId;
                        document.getElementById("form_colorName").value = data.result.colorName;
                        document.getElementById("form_sizeId").value = data.result.sizeId;
                        document.getElementById("form_sizeName").value = data.result.sizeName;
                        document.getElementById("form_styleId").value = data.result.styleId;
                        $("#code_validate").html(null);
                        $("#form_epc").select();//焦点移动到下一个文本框
                    } else {
                        $("#code_validate").html(data.msg);

                    }

                }
            });


        }
    });
    $("#form_epc").keyup(function (event) {
        var epc = document.getElementById("form_epc").value;
        if(epc.length==12||epc.length==24||epc.length==32){
            $("#epc_validate").html(null);
            if (event.keyCode == 13) {
                var code = document.getElementById("form_code").value;
                $.ajax({
                    url: basePath + "/tag/bind/bindcode.do",
                    data: {code: code, epc: epc},
                    datatype: "json",
                    type: "POST",
                    success: function (data) {
                        if (data.success) {
                            $("#bindgrid").addRowData(0, data.result);
                            cleantxt();
                        } else {
                            bootbox.alert(data.msg);
                        }
                    }
                });
            }
        }else{
            $("#epc_validate").html("epc长度为12、24、32");
        }

    });
    $("#unbind_epc").keydown(function (event) {
        if (event.keyCode == 13) {
            $('#editForm').data('bootstrapValidator').validate();
            if (!$('#editForm').data('bootstrapValidator').isValid()) {
                return;
            }
            var epc = document.getElementById("unbind_epc").value;
            var progressDialog = bootbox.dialog({
                message: '<p><i class="fa fa-spin fa-spinner"></i> 解绑中...</p>'
            });
            $.ajax({
                url: basePath + "/tag/bind/unbind.do",
                data: {epc: epc},
                datatype: "json",
                type: "POST",
                success: function (data) {
                    if (data.success) {
                        $("#unbindgrid").addRowData(0, data.result);
                        cleantxt();
                    } else {
                    }
                    progressDialog.modal('hide');
                }
            });
        }
    });
}
function getRadioBoxValue(radioName) {
    var obj = document.getElementsByName(radioName);
    for (i = 0; i < obj.length; i++) {

        if (obj[i].checked) {
            return obj[i].value;
        }
    }
    return "undefined";
}
function cleantxt() {
    //document.getElementById("form_code").value="";
    if (getRadioBoxValue("flag") == 0) {
        $('#bindEditForm').clearForm();
        $("#form_code").select();
    }
    document.getElementById("form_epc").value = "";
    document.getElementById("unbind_epc").value = "";
}

function _clearSearch() {
    $("#searchForm").resetForm();
}