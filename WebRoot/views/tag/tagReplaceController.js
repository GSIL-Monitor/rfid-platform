/**
 * Created by yushen on 2017/8/23.
 */

$(function () {
    searchCode_keydown();
    initGrid();
    initOrigTagFormValid();
    initNewTagFormValid();
});

function searchCode_keydown() {
    $("#orig_code").keydown(function (event) {
        if (event.keyCode === 13) {
            var code = $("#orig_code").val();
            $.ajax({
                dataType: "json",
                async: false,
                url: basePath + "/tag/tagReplace/findInfo.do",
                data: {code: code},
                type: "POST",
                success: function (data) {
                    if (data.success) {
                        $("#orig_code").val(data.msg);
                        $("#orig_styleId").val(data.result.styleId);
                        $("#orig_styleName").val(data.result.styleName);
                        $("#orig_colorId").val(data.result.colorId);
                        $("#orig_sizeId").val(data.result.sizeId);
                        $("#orig_price").val(data.result.price);

                        $("#replace_styleId").val(data.result.styleId);
                        $("#replace_styleName").val(data.result.styleName);
                        $("#replace_price").val(data.result.price);
                        setUrl();

                        $("#newTag_code").val("");
                        $("#newTag_styleId").val("");
                        $("#newTag_styleName").val("");
                        $("#newTag_colorId").val("");
                        $("#newTag_sizeId").val("");
                        $("#newTag_price").val("");
                        //更换其他原始吊牌信息后重置 更新标签信息 按钮
                        $("#confirmReplaceTag").removeAttr("disabled");
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

    $("#newTag_code").keydown(function (event) {
        if (event.keyCode === 13) {
            var code = $("#newTag_code").val();
            $.ajax({
                dataType: "json",
                async: false,
                url: basePath + "/tag/tagReplace/findInfoInTagEpc.do",
                data: {code: code},
                type: "POST",
                success: function (data) {
                    if (data.success) {
                        $("#newTag_code").val(data.msg);
                        $("#newTag_styleId").val(data.result.styleId);
                        $("#newTag_styleName").val(data.result.styleName);
                        $("#newTag_colorId").val(data.result.colorId);
                        $("#newTag_sizeId").val(data.result.sizeId);
                        $("#newTag_price").val(data.result.price);
                    } else {
                        $("#newTag_code").val("");
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

function setUrl() {
    $("#codeReplace_CSGrid").jqGrid("setGridParam", {
        url: basePath + "/prod/product/page.do?filter_EQS_styleId=" + $("#replace_styleId").val(),
        page: 1
    });
    $("#codeReplace_CSGrid").trigger("reloadGrid");
}

function initGrid() {
    $("#codeReplace_CSGrid").jqGrid({
        height: "200px",
        datatype: 'json',
        mtype: 'POST',
        colModel: [
            {name: 'styleId', label: '货号'},
            {name: 'colorId', label: '颜色'},
            {name: 'sizeId', label: '尺寸'},
            {name: 'barcode', label: '条码'}
        ],
        viewrecords: true,
        autoWidth: true,
        rownumbers: true,
        altRows: true,
        rowNum: 20,
        rowList: [20, 50, 100],
        pager: "#codeReplace_CSPager",
        multiselect: false,
        shrinkToFit: true,
        autoScroll: false,
        sortname: 'colorId',
        sortorder: "asc"
    });
}

function produceNewTag() {

    $("#tagOrigForm").data('bootstrapValidator').destroy();
    $('#tagOrigForm').data('bootstrapValidator', null);
    initOrigTagFormValid();
    $('#tagOrigForm').data('bootstrapValidator').validate();
    if (!$('#tagOrigForm').data('bootstrapValidator').isValid()) {
        return;
    }

    var rowId = $("#codeReplace_CSGrid").jqGrid("getGridParam", "selrow");
    if (rowId === "" || rowId === null || rowId === undefined) {
        $.gritter.add({
            text: "请在色码列表中选择款色码！",
            class_name: 'gritter-success  gritter-light'
        });
        return;
    }
    var rowData = $("#codeReplace_CSGrid").jqGrid('getRowData', rowId);
    $.ajax({
        url: basePath + "/tag/tagReplace/produceNewTag.do",
        data: {
            colorSizeStr: JSON.stringify(rowData)
        },
        datatype: "json",
        type: "POST",
        success: function (data) {
            if (data.success) {
                $.gritter.add({
                    text: data.msg,
                    class_name: 'gritter-success  gritter-light'
                });
            } else {
                $.gritter.add({
                    text: data.msg,
                    class_name: 'gritter-success  gritter-light'
                });
            }
        }
    });
}

function confirmReplaceTag() {

    $("#tagOrigForm").data('bootstrapValidator').destroy();
    $('#tagOrigForm').data('bootstrapValidator', null);
    $("#newTagForm").data('bootstrapValidator').destroy();
    $('#newTagForm').data('bootstrapValidator', null);
    initOrigTagFormValid();
    initNewTagFormValid();
    $('#tagOrigForm').data('bootstrapValidator').validate();
    $('#newTagForm').data('bootstrapValidator').validate();
    if (!$('#tagOrigForm').data('bootstrapValidator').isValid() && !$('#newTagForm').data('bootstrapValidator').isValid()) {
        return;
    }
    if ($("#orig_styleId").val() !== $("#newTag_styleId").val()) {
        $.gritter.add({
            text: "替换标签和原标签的款号必须相同",
            class_name: 'gritter-success  gritter-light'
        });
        return;
    }
    //点击后禁止 更新标签信息 按钮，防止重复更新。
    $("#confirmReplaceTag").attr({"disabled": "disabled"});
    $.ajax({
        async: false,
        url: basePath + "/tag/tagReplace/replaceTag.do",
        data: {
            origInfoStr: JSON.stringify(array2obj($("#tagOrigForm").serializeArray())),
            newInfoStr: JSON.stringify(array2obj($("#newTagForm").serializeArray()))
        },
        datatype: "json",
        type: "POST",
        success: function (data) {
            if (data.success) {
                $.gritter.add({
                    text: data.msg,
                    class_name: 'gritter-success  gritter-light'
                });
            } else {
                $.gritter.add({
                    text: data.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                //更新失败重置 更新标签信息 按钮
                $("#confirmReplaceTag").removeAttr("disabled");
            }
        }
    });
}

function viewReplaceRecord() {
    window.location.href = basePath + "/tag/tagReplace/viewReplaceRecord.do";
}

function initOrigTagFormValid() {
    $('#tagOrigForm').bootstrapValidator({
        message: '输入值无效',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            code: {
                validators: {
                    notEmpty: {
                        message: '原始吊牌唯一码不能为空'
                    }
                }
            },
            styleId: {
                validators: {
                    notEmpty: {
                        message: '原始吊牌款号不能为空'
                    }
                }
            },
            colorId: {
                validators: {
                    notEmpty: {
                        message: '原始吊牌颜色不能为空'
                    }
                }
            },
            sizeId: {
                validators: {
                    notEmpty: {
                        message: '原始吊牌尺寸不能为空'
                    }
                }
            }
        }
    });
}
function initNewTagFormValid() {
    $('#newTagForm').bootstrapValidator({
        message: '输入值无效',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            code: {
                validators: {
                    notEmpty: {
                        message: '新吊牌唯一码不能为空'
                    }
                }
            },
            styleId: {
                validators: {
                    notEmpty: {
                        message: '新吊牌款号不能为空'
                    }
                }
            },
            colorId: {
                validators: {
                    notEmpty: {
                        message: '新吊牌颜色不能为空'
                    }
                }
            },
            sizeId: {
                validators: {
                    notEmpty: {
                        message: '新吊牌尺寸不能为空'
                    }
                }
            }
        }
    })
}