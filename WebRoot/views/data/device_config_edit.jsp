<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<div id="edit-dialog" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog">
        <div class="modal-header no-padding">
            <div class="table-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    <span class="white">&times;</span>
                </button>
                设备配置
            </div>
        </div>
        <div class="modal-content">
            <div class="modal-body">
                <form class="form-horizontal" role="form" id="editForm">
                    <input type="hidden" id="form_id" name="id"/>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_deviceId">设备编号</label>

                        <div class="col-xs-14 col-sm-7">
                            <input class="form-control" id="form_deviceId" name="deviceId" readonly value="${deviceId}"
                                   type="text" placeholder=""/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_fileName">文件名</label>

                        <div class="col-xs-14 col-sm-7">
                            <input class="form-control" id="form_fileName" name="fileName"
                                   type="text" placeholder=""/>
                        </div>

                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_path">文件路径</label>

                        <div class="col-xs-14 col-sm-7">
                            <input class="form-control" id="form_path" name="path"
                                   type="text" placeholder=""/>
                        </div>

                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_app">应用程序</label>

                        <div class="col-xs-14 col-sm-7">
                            <input class="form-control" id="form_app" name="app"
                                   type="text" placeholder=""/>
                        </div>

                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_type">类型</label>

                        <div class="col-xs-14 col-sm-7">
                            <select class="chosen-select form-control" id="form_type" name="type" >
                                <option value="XML" selected>XML</option>
                                <option value="BIN">二进制</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_val">值</label>
                        <div class="col-xs-14 col-sm-7">
                                <textarea class="form-control" id="form_val" name="val">
                                </textarea>
                        </div>

                    </div>

                </form>

            </div>
        </div>
        <div class="modal-footer">

            <a href="#" class="btn" onclick="closeEditDialog()">关闭</a>

            <button type="button"  class="btn btn-primary" onclick="save()">保存</button>

        </div>
    </div>
</div>
<script>

    function save() {
//        $('#editForm').data('bootstrapValidator').validate();
//        if(!$('#editForm').data('bootstrapValidator').isValid()){
//            return ;
//        }
        cs.showProgressBar();
        $.post(basePath+"/data/device/saveConfig.do",
            $("#editForm").serialize(),
            function(result) {
                cs.closeProgressBar();
                if(result.success == true || result.success == 'true') {
                    $.gritter.add({
                        text: result.msg,
                        class_name: 'gritter-success  gritter-light'
                    });
                    $("#edit-dialog").modal("hide");
                } else {
                    cs.showAlertMsgBox(result.msg);
                }
                $("#cfgGrid").trigger("reloadGrid")
            }, 'json');
    }
</script>

