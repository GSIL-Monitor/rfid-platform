<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="modal_prodMediaA" class="modal fade"  tabindex="-1">
    <div class="modal-dialog" >
        <div class="modal-content">
            <div class="modal-header no-padding">
                <div class="table-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        <span class="white">&times;</span>
                    </button>
                   音频上传
                </div>
            </div>

            <div class="modal-body no-padding">
                <div class="widget-body">

                    <div class="widget-main" id="searchPanel">
                        <form class="form-horizontal" role="form" id="AForm">
                            <div class="form-group">
                                <label class="col-xs-2 control-label text-right" for="form_Aname">媒体描述</label>
                                <div class="col-xs-10">
                                    <input  id="form_Aname"  class="form-control" name="name" type="text"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-xs-2 control-label text-right" for="form_AstartTime">开始时间</label>
                                <div class="col-xs-10">
                                    <input  id="form_AstartTime"  class="form-control" name="startTime" type="text"/>
                                </div>
                            </div>
                            <div class="form-group">

                                <label class="col-xs-2 control-label text-right" for="form_AisShow">是否展示</label>
                                <div class="col-xs-4">
                                    <select class="form-control" id="form_AisShow" name="isShow" >
                                        <option value="Y">是</option>
                                        <option value="N">否</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-xs-2 control-label text-right" for="form_Aremark">备注</label>
                                <div class="col-xs-10">
                                    <input  id="form_Aremark"  class="form-control" name="remark" type="text"/>
                                </div>

                            </div>
                            <input  id="form_AstyleId"  class="form-control" name="styleId" type="text" style="display: none"/>
                            <input  id="form_AId"  class="form-control" name="id" type="text" style="display: none"/>


                            <div class="form-group">
                                <label class="col-xs-2 control-label text-right" >文件</label>
                                <div class="col-xs-10">
                                    <input type="file" id="AfileUpload"   required="true" accept="mp3/">
                                </div>

                            </div>

                        </form>
                    </div>
                </div>
            </div>

            <div class="modal-footer no-margin-top">
                <a href="#" class="btn" onclick="closeADialog()">关闭</a>
                <a href="#" class="btn btn-primary" onclick="saveMediaA()">上传</a>
            </div>
        </div>
    </div>
</div>
<script>

    function closeADialog(){
        $("#modal_prodMediaA").modal("hide");
    }
    $('#modal_prodMediaA').on('hidden.bs.modal', function() {
        $("#AForm").data('bootstrapValidator').destroy();
        $('#AForm').data('bootstrapValidator', null);
        initAValidate();
    });
    $(function () {
        //初始化ace-admin文件上传控件
            $('#AfileUpload').ace_file_input({
                style: 'well',
                btn_choose: '点击选择音频或将音频拖到此处',
                btn_change: null,
                no_icon: 'ace-icon fa fa-cloud-upload',
                droppable: true,
                allowExt: ['MP3','mp3'],
                thumbnail: 'small'//large | fit
                //,icon_remove:null//set null, to hide remove/reset button
//                ,before_change:function(files, dropped) {
//						//Check an example below
//						//or examples/file-upload.html
//						return true;
//					}
                /**,before_remove : function() {
						return true;
					}*/
                ,
                preview_error : function(filename, error_code) {
                    //name of the file that failed
                    //error_code values
                    //1 = 'FILE_LOAD_FAILED',
                    //2 = 'IMAGE_LOAD_FAILED',
                    //3 = 'THUMBNAIL_FAILED'
                    //alert(error_code);

                }

            }).on('change', function(){
                //console.log($(this).data('ace_input_files'));
                //console.log($(this).data('ace_input_method'));
            }).on('file.error.ace', function(event, info) {
                bootbox.alert("请上传mp3格式文件");
            });
        initAValidate();

    });

    function initAValidate(){
        $('#AForm').bootstrapValidator({
            message: '输入值无效',
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            fields: {
                name: {
                    validators: {
                        notEmpty: {
                            message: '媒体描述为空'
                        }
                    }
                },
                startTime: {
                    validators: {
                        regexp: {
                            regexp:/^\d+$/,
                            message: "开始时间为数字"
                        }
                    }
                }
            }

        });
    }
</script>
