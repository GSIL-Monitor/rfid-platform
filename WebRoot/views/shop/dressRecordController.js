/**
 * Created by yushen on 2017/9/8.
 */
$(function () {
    //初始化
    initGrid();
    dressCode_keyDown();
    initSelectBusinessIdForm();
});

function initSelectBusinessIdForm() {
    $.ajax({
        url: basePath + "/sys/user/list.do?filter_EQI_type=4&filter_EQS_ownerId=" + curOwnerId,
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {
            $("#form_businessId").empty();
            $("#form_businessId").append("<option value='' >--选择销售员--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#form_businessId").append("<option value='" + json[i].id + "'>" + json[i].name + "</option>");
                // $("#search_busnissId").trigger('chosen:updated');
            }
            $('.selectpicker').selectpicker('refresh');
        }
    });
}

function initGrid() {
    $("#grid").jqGrid({
        height: "auto",
        mtype: "POST",
        url: basePath + "/shop/dressRecord/page.do",
        datatype: "json",
        colModel: [
            {name: 'id', hidden: true},
            {name: 'recordStartTime', label: '起始时间', sortable: true, width: 200},
            {name: 'recordEndTime', label: '归还时间', sortable: true, width: 200},
            {name: 'businessId', label: '销售员Id', hidden: true},
            {name: 'businessName', label: '销售员名', width: 150},
            {name: 'ownerId', label: '所属门店', width: 150},
            {name: 'status', label: '当前状态', hidden: true, width: 150},
            {
                name: '', label: '当前状态', width: 150,
                formatter: function (cellValue, options, rowObject) {
                    if (rowObject.status === 0) {
                        return "已归还"
                    } else if (rowObject.status === 1) {
                        return "穿着中"
                    }
                }
            },
            {name: 'dressCode', label: '唯一码', editable: true, width: 150},
            {name: 'styleId', label: '款号', width: 100},
            {name: 'styleName', label: '款名', width: 100},
            {name: 'colorId', label: '款号', width: 100},
            {name: 'sizeId', label: '款号', width: 100},
            {name: 'remark', label: '备注', width: 200}
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
        sortname: 'recordStartTime',
        sortorder: "desc",
        autoScroll: false
    });
}

function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");
}

function dressCode_keyDown() {
    $("#form_dressCode").keydown(function (event) {
        if (event.keyCode === 13) {
            $.ajax({
                dataType: "json",
                async: false,
                url: basePath + "/shop/dressRecord/findInfo.do",
                data: {code: $("#form_dressCode").val()},
                type: "POST",
                success: function (data) {
                    if (data.success) {
                        $("#form_dressCode").val(data.result);
                    } else {
                        $("#form_dressCode").val("");
                        $.gritter.add({
                            text: data.msg,
                            class_name: 'gritter-success  gritter-light'
                        });
                    }
                }
            });
        }
    })
}

function dressing() {
    if ($("#form_dressCode").val() && $("#form_dressCode").val() !== null &&
        $("#form_businessId").val() && $("#form_businessId").val() !== null) {
        $.ajax({
            dataType: "json",
            async: false,
            url: basePath + "/shop/dressRecord/dressing.do",
            data: {
                code: $("#form_dressCode").val(),
                businessId: $("#form_businessId").val()
            },
            type: "POST",
            success: function (data) {
                if (data.success) {
                    $("#form_dressCode").val("");

                    $.gritter.add({
                        text: data.msg,
                        class_name: 'gritter-success  gritter-light'
                    });

                    $("#grid").jqGrid('setGridParam', {
                        url: basePath + "/shop/dressRecord/page.do",
                        page: 1
                    });
                    $("#grid").trigger("reloadGrid");
                } else {
                    $("#form_dressCode").val("");
                    $.gritter.add({
                        text: data.msg,
                        class_name: 'gritter-success  gritter-light'
                    });
                }
            }
        });
    } else {
        $.gritter.add({
            text: "唯一码和销售员都不能为空",
            class_name: 'gritter-success  gritter-light'
        });
    }
}

function returnBack() {
    if ($("#form_dressCode").val() && $("#form_dressCode").val() !== null &&
        $("#form_businessId").val() && $("#form_businessId").val() !== null) {
        $.ajax({
            dataType: "json",
            async: false,
            url: basePath + "/shop/dressRecord/returnBack.do",
            data: {
                code: $("#form_dressCode").val(),
                businessId: $("#form_businessId").val()
            },
            type: "POST",
            success: function (data) {
                if (data.success) {
                    $("#form_dressCode").val("");

                    $.gritter.add({
                        text: data.msg,
                        class_name: 'gritter-success  gritter-light'
                    });

                    $("#grid").jqGrid('setGridParam', {
                        url: basePath + "/shop/dressRecord/page.do",
                        page: 1
                    });
                    $("#grid").trigger("reloadGrid");
                } else {
                    $("#form_dressCode").val("");
                    $.gritter.add({
                        text: data.msg,
                        class_name: 'gritter-success  gritter-light'
                    });
                }
            }
        });
    } else {
        $.gritter.add({
            text: "唯一码和销售员都不能为空",
            class_name: 'gritter-success  gritter-light'
        });
    }
}

function refresh() {
    location.reload(true);
}

function _search() {

    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);

    $("#grid").jqGrid('setGridParam', {
        url: basePath + "/shop/cashier/page.do",
        page: 1,
        postData: params
    });
    $("#grid").trigger("reloadGrid");
}

function _clearSearch() {

}
