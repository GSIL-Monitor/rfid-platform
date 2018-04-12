<%--
  Created by IntelliJ IDEA.
  User: liutianci
  Date: 2018/3/29
  Time: 17:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="edit_inventoryMergeBillDtl_dialog" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog">
        <div class="modal-header  no-padding">
            <div class="table-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    <span class="white">&times;</span>
                </button>
                已选中信息
            </div>
        </div>
    <div class="modal-content">
        <div class="modal-content">
            <div class="row">
                <form class="form-horizontal" role="search" id="explain_editForm">
                    <div class="form-group">
                    <label class="col-sm-2 control-label no-padding-right"
                        for="reason">原因</label>
                    <div class="col-xs-12 col-sm-9">
                        <input class="form-control" id="reason" name="reason" type="text">
                    </div>
                    </div>
                </form>
            </div>
        </div>
        <div class="hr hr-4">
        </div>
            <table id="grid"></table>
        </div>
        <div id="grid-pager">
            <div class="modal-footer">
                <a href="#" class="btn" onclick="closeEditDialog()">关闭</a>
                <button type="button"  class="btn btn-primary" onclick="saveDtlOut()">保存</button>
            </div>
        </div>
    </div>
    <jsp:include page="../layout/footer.jsp"></jsp:include>
</div>
<script type="text/javascript">
    var billNo = $("#search_id").val();
    var alldate = allrowDate;

    $(function () {
        $('#edit_inventoryMergeBillDtl_dialog').on('show.bs.modal', function () {
            initEditFormValid();
        });
        initGridDialog();
        $("#edit_inventoryMergeBillDtl_dialog").focus();
        $('#edit_inventoryMergeBillDtl_dialog').on('hidden.bs.modal', function () {
            $('#edit_inventoryMergeBillDtl_dialog').focus();
            $("#grid").jqGrid("clearGridData");
        });
    });
    function initEditFormValid() {
        $('#explain_editForm').bootstrapValidator({
            message: '输入值无效',
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            submitHandler: function(validator, form, submitButton) {
                $.post(form.attr('action'), form.serialize(), function(result) {
                    if (result.success == true || result.success == 'true') {
                    } else {
                        $('#explain_editForm').bootstrapValidator('disableSubmitButtons', false);
                    }
                }, 'json');
            },
            fields: {
                reason: {
                    validators: {
                        notEmpty: {
                            message: '原因不能为空'
                        }
                    }
                }
            }
        });
    }

    function initGridDialog() {
        $("#grid").jqGrid({
            height:400,
            datatype: "json",
            colModel: [
                {name: 'id', label: 'id', hidden: true, width: 40},
                {name: 'sku', label: 'SKU', width: 40},
                {name: 'code', label: '唯一码', width: 40},
                {name: 'styleId', label: '款号', width: 40},
                {name: 'styleName', label: '款名', width: 40},
                {name: 'colorId', label: '色码', width: 40},
                {name: 'sizeId', label: '尺码', width: 40},
                {name: 'price', label: '吊牌价', width: 40}
            ],
            viewrecords:true,
            autowidth: true,
            rownumbers: true,
            altRows: true,
            rowNum: -1,
            pager: "#grid-pager",
            shrinkToFit: true,
            sortname: 'sku',
            sortorder: "desc",
            footerrow: true
        });
        var parent_column = $("#grid").closest('.modal-dialog');
        $("#grid").jqGrid('setGridWidth', parent_column.width() - 5);
        $("#grad").jqGrid('setFrozenColumns');
    }

/*    hidden.bs.modal*/

    function closeEditDialog() {
        $("#edit_inventoryMergeBillDtl_dialog").modal('hide');
    }
    function saveDtlOut() {
        $('#explain_editForm').data('bootstrapValidator').validate();
        if(!$('#explain_editForm').data('bootstrapValidator').isValid()){
            return ;
        }
        var reason = $("#reason").val();
        console.log(userId);
        console.log(reason);
        console.log(billNo);
        console.log(alldate);
        console.log(basePath);
        $.ajax({
            dataType:"json",
            url:basePath+"/stock/InventoryMerge/saveDtlOut.do",
            data:{
                reason:reason,
                billNo:billNo,
                setDtlList:JSON.stringify(alldate),
                userId:userId
            },
            type:"POST",
            success: function (msg) {
                if (msg.success) {
                    $.gritter.add({
                        text: msg.msg,
                        class_name: 'gritter-success  gritter-light'
                    });
                    $("#grad").modal('hide');
                } else {
                    bootbox.alert(msg.msg);
                }
            }
        });
        $("#edit_inventoryMergeBillDtl_dialog").modal('hide');
        $("#detailgrid").trigger("reloadGrid");
    }
</script>

