<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<div id="edit-dialog" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog">
        <div class="modal-header no-padding">
            <div class="table-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    <span class="white">&times;</span>
                </button>
                流程信息
            </div>
        </div>
        <div class="modal-content">
            <div class="modal-body">
                <form class="form-horizontal" role="form" id="editForm">
                    <div class="form-group" style="display: none">
                        <label class="col-sm-3 control-label no-padding-right" for="form_id">id</label>

                        <div class="col-xs-14 col-sm-7" >
                            <input class="form-control" id="form_id" name="id" type="text" placeholder=""/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label no-padding-right" for="form_token">流程</label>

                        <div class="col-xs-14 col-sm-7">
                            <select class="chosen-select form-control" id="form_token" name="token" >

                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label no-padding-right" for="form_reason">暂停原因</label>

                        <div class="col-xs-14 col-sm-7">
                            <textarea type="text" class="form-control" rows="3" id="form_reason" name="reason"  ></textarea>
                        </div>
                    </div>

                </form>

            </div>
        </div>
        <div class="modal-footer">

            <a href="#" class="btn" onclick="closeEditDialog()">关闭</a>

            <button type="button" href="#" class="btn btn-primary" onclick="save()">保存</button>

        </div>
    </div>
</div>
<script>

    $(function() {
        $("#edit-dialog").on('show.bs.modal', function () {
            initSelect();
            initEditFormValid();

        }).on('hidden.bs.modal', function() {
            $("#editForm").data('bootstrapValidator').destroy();
            $('#editForm').data('bootstrapValidator', null);
        });

    });
    function closeEditDialog(){
        $("#edit-dialog").modal('hide');
    }

    function initSelect(){
        $("#form_token").empty();
        $.ajax({
            dataType: "json",
            url : basePath + "/factory/pauseReason/findToken.do",
            cache : false,
            async : false,
            type : "POST",
            success : function (data){
                var json= data.result;
                for(var i=0;i<json.length;i++){
                    $("#form_token").append("<option value='"+json[i].token+"'>"+json[i].name+"</option>");

                    $("#form_token").trigger('chosen:updated');
                }
            }
        });

    }

    function initEditFormValid() {
        $('#editForm').bootstrapValidator({
            message: '输入值无效',
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
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


</script>
