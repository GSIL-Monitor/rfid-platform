<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<div id="edit_size_dialog" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog">
        <div class="modal-header no-padding">
            <div class="table-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    <span class="white">&times;</span>
                </button>
                尺寸编辑
            </div>
        </div>
        <div class="modal-content">
            <div class="modal-body">
                <form class="form-horizontal" role="form" id="editSizeForm">
                    <div class="form-group">
                        <div id="sizeId_div" hidden="hidden">
                            <label class="col-sm-2 control-label no-padding-right" for="form_sizeId"><span class="text-danger">* </span>尺寸编码</label>
                            <div class="col-xs-10 col-sm-5">
                                <input class="form-control" id="form_sizeId" name="sizeId"
                                       type="text" readonly/>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_sizeName"><span class="text-danger">* </span>尺寸</label>

                        <div class="col-xs-10 col-sm-5">
                            <input class="form-control" id="form_sizeName" name="sizeName"
                                   type="text"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_sortId"><span class="text-danger">* </span>尺寸组</label>

                        <div class="col-xs-10 col-sm-5">
                            <%--<input class="form-control" id="form_sortId" name="sortId"--%>
                            <%--type="text"/>--%>
                            <select class="form-control" id="form_sortId" name="sortId" placeholder="">
                            </select>
                        </div>
                    </div>
                </form>

            </div>
        </div>
        <div class="modal-footer">

            <a href="#" class="btn" onclick="closeSizeDialog()">关闭</a>

            <button type="button" class="btn btn-primary" onclick="saveSize()">保存</button>

        </div>
    </div>
</div>
<script>

    function initSelectSize() {
        $("#form_sortId").empty();
        $.ajax({
            url: basePath + "/prod/size/listSizeSort.do?",
            cache: false,
            async: false,
            type: "POST",
            success: function (data, textStatus) {
                var json = data;
                for (var i = 0; i < json.length; i++) {
                    $("#form_sortId").append("<option value='" + json[i].id + "'>" + "[" + json[i].id + "]" + json[i].sortName + "</option>");
                    $("#form_sortId").trigger('chosen:updated');
                }
                if ("${pageType}" == "edit") {
                    $("#form_sortId").find("option[value='${style.sizeSortId}']").attr("selected", true);
                }
            }
        });
    }

    function saveSize() {
        $("#form_sizeId").val($("#form_sizeName").val());
        $('#editSizeForm').data('bootstrapValidator').validate();
        if (!$('#editSizeForm').data('bootstrapValidator').isValid()) {
            return;
        }
        $("#form_sortId").removeAttr("disabled");
        var progressDialog = bootbox.dialog({
            message: '<p><i class="fa fa-spin fa-spinner"></i> 数据上传中...</p>'
        });
        $.post(basePath + "/prod/size/save.do",
            $("#editSizeForm").serialize(),
            function (result) {
                if (result.success == true || result.success == 'true') {
                    progressDialog.modal('hide');
                    $("#edit_size_dialog").modal('hide');
                    $("#setgrid").trigger("reloadGrid");
                    $("#form_sizeId").empty();
                    inimultiSize();
                    /*var text = $("#form_sortId").find("option:selected").text();
                    var index =text.substr(2,3);
                    $("#form_sizeId").append("<optgroup label='"+text+"' class= 'group"+index+"'>");
                    $("#form_sizeId").append("<option value='" + result.result.sizeId + "' selected>" + result.result.sizeName + "</option>");
                    $("#form_sizeId").multiselect('rebuild');*/
                }
            }, 'json');
        $("#form_sortId").empty();
    }
    function closeSizeDialog() {
        $("#edit_size_dialog").modal('hide');
    }

    $(function () {
        $("#edit_size_dialog").on('show.bs.modal', function () {
            initeditSizeFormValid();
            initSelectSize();
        });
    });

    function initeditSizeFormValid() {
        $('#editSizeForm').bootstrapValidator({
            message: '输入值无效',
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            submitHandler: function (validator, form, submitButton) {
                $.post(form.attr('action'), form.serialize(), function (result) {
                    if (result.success == true || result.success == 'true') {

                    } else {
                        $('#editSizeForm').bootstrapValidator('disableSubmitButtons', false);
                    }
                }, 'json');
            },
            fields: {
                sizeName: {
                    validators: {
                        notEmpty: {
                            message: '尺寸不能为空'
                        }
                    }
                },
                sortId: {
                   validators: {
                       notEmpty: {
                           message: '尺寸组不能为空'
                       }
                    }
               }
            }
        });
    }

</script>
