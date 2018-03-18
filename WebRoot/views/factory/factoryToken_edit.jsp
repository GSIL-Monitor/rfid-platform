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
                        <label class="col-sm-3 control-label no-padding-right" for="form_token">token</label>

                        <div class="col-xs-14 col-sm-7" >
                            <input class="form-control" id="form_token" name="token" type="text" placeholder=""/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label no-padding-right" for="form_name">流程名</label>

                        <div class="col-xs-14 col-sm-7">
                            <input class="form-control" id="form_name" name="name" type="text" placeholder=""/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label no-padding-right" for="form_types">过程</label>

                        <div class="col-xs-14 col-sm-7">
                            <input type="hidden" disabled="disabled" class="form-control" id="form_typesId" name="typesId"  />
                            <select multiple="" class="multiselect"  data-placeholder="-请选择-" id="form_types" name="types" required="true" onchange="selectOnchang()">
                                <option value="I">开始</option>
                                <option value="O">结束</option>
                                <option value="P">暂停</option>
                                <option value="R">恢复</option>
                            </select>
                        </div>
                    </div>

                    <!-- #section:elements.form -->
                    <div class="form-group">
                        <label class="col-sm-3 control-label no-padding-right" for="form_necessary">必须流程</label>

                        <div class="col-xs-14 col-sm-7">
                            <select id="form_necessary" name="necessary" class="chosen-select form-control" >
                                <option value="N">否</option>
                                <option value="Y">是</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label no-padding-right" for="form_lastToken">上一个必须流程</label>

                        <div class="col-xs-14 col-sm-7">
                            <select class="chosen-select form-control" id="form_lastToken" name="lastToken" >

                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label no-padding-right" for="form_multiple">多次扫描</label>
                        <div class="col-xs-14 col-sm-7">
                            <select class="chosen-select form-control" id="form_multiple" name="multiple">
                                <option value="N">不允许</option>
                                <option value="Y">允许</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label no-padding-right" for="form_isLast">第一个流程</label>
                        <div class="col-xs-14 col-sm-7">
                            <select class="chosen-select form-control" id="form_isFirst" name="isFirst">
                                <option value="N">否</option>
                                <option value="Y">是</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label no-padding-right" for="form_isLast">最后一个流程</label>
                        <div class="col-xs-14 col-sm-7">
                            <select class="chosen-select form-control" id="form_isLast" name="isLast">
                                <option value="N">否</option>
                                <option value="Y">是</option>
                            </select>
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
    var oldTokenName;
    $(function() {
        $("#edit-dialog").on('show.bs.modal', function () {
            initSelect();
            initEditFormValid();

        });
        $('.multiselect').multiselect({
            enableFiltering: true,
            enableHTML: true,
            buttonWidth: '100%',
            buttonClass: 'btn btn-white btn-primary',
            templates: {
                button: '<button type="button" class="multiselect dropdown-toggle" data-toggle="dropdown"><span class="multiselect-selected-text"></span> &nbsp;<b class="fa fa-caret-down"></b></button>',
                ul: '<ul class="multiselect-container dropdown-menu"></ul>',
                filter: '<li class="multiselect-item filter"><div class="input-group"><span class="input-group-addon"><i class="fa fa-search"></i></span><input class="form-control multiselect-search" type="text"></div></li>',
                filterClearBtn: '<span class="input-group-btn"><button class="btn btn-default btn-white btn-grey multiselect-clear-filter" type="button"><i class="fa fa-times-circle red2"></i></button></span>',
                li: '<li><a tabindex="0"><label></label></a></li>',
                divider: '<li class="multiselect-item divider"></li>',
                liGroup: '<li class="multiselect-item multiselect-group"><label></label></li>'
            }
        });

        $("#edit-dialog").on('hide.bs.modal',function(){
            $("#editForm").data('bootstrapValidator').destroy();
            $('#editForm').data('bootstrapValidator', null);
            initEditFormValid();
            $("#form_lastToken").empty();
                $('#form_types').multiselect('deselect', 'I');
                $('#form_types').multiselect('deselect', 'P');
                $('#form_types').multiselect('deselect', 'O');
                $('#form_types').multiselect('deselect', 'R');
        });
    });

    function selectOnchang() {
        var tmpSelected = $('#form_types').val();
        if (tmpSelected != null) {
            $('#form_typesId').val(tmpSelected);
        } else {
            $('#form_typesId').val("");
        }
    }

    function closeEditDialog(){
        $("#edit-dialog").modal('hide');
    }

    function initSelect(){

        $.ajax({
            dataType: "json",
            url : basePath + "/factory/token/findToken.do",
            cache : false,
            async : false,
            type : "POST",
            success : function (data){
                var json= data.result;
                $("#form_lastToken").append("<option value=''>-请选择-</option>");
                for(var i=0;i<json.length;i++){
                    $("#form_lastToken").append("<option value='"+json[i].token+"'>"+json[i].name+"</option>");

                    $("#form_lastToken").trigger('chosen:updated');
                }
            }
        });

    }

    function initEditFormValid() {
        $('#editForm').bootstrapValidator({
            message: '输入值无效',
            excluded : [':disabled'],
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            fields: {
                name: {
                    validators: {
                        notEmpty: {
                            message: '名称不能为空'
                        },
                        remote: {//ajax验证。server result:{"valid",true or false} 向服务发送当前input name值，获得一个json数据。例表示正确：{"valid",true}
                            url: basePath + "/factory/token/checkName.do",//验证地址
                            message: '流程已存在',//提示消息,
                            data:{oldName:oldTokenName},
                            delay: 2000,//每输入一个字符，就发ajax请求，服务器压力还是太大，设置2秒发送一次ajax（默认输入一个字符，提交一次，服务器压力太大）
                            type: 'POST'//请求方式
                        }
                    }
                }
            }
        });
    }

</script>
