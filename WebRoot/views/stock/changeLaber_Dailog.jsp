<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<div id="changeLaber-dialog" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog">
        <div class="modal-header no-padding">
            <div class="table-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    <span class="white">&times;</span>
                </button>
                标签转换信息
            </div>
        </div>
        <div class="modal-content">
            <div class="modal-body">
                <form class="form-horizontal" role="form" id="editChangeForm">
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="select_changeType">转变类型</label>

                        <div class="col-xs-14 col-sm-7">
                            <select class="form-control" id="select_changeType"
                                    name="changeType" onchange="onChangeType()">
                                <option value="">--请选择--</option>
                                <option value="CS">系列转变</option>
                                <option value="PC">打折</option>

                            </select>
                        </div>
                    </div>
                    <div id="beforeclass9Div" class="form-group" style="display: none">
                        <label class="col-sm-2 control-label no-padding-right" for="search_beforeclass9">原系列</label>

                        <div class="col-xs-14 col-sm-7">
                            <select class="form-control" id="search_beforeclass9" name="beforeclass9"
                                    style="width: 100%;" >
                            </select>
                        </div>
                    </div>
                    <div id="discountDiv" class="form-group" style="display: none">
                        <label class="col-sm-2 control-label no-padding-right" for="search_discount">折扣</label>
                        <div class="col-xs-14 col-sm-7">
                            <input class="form-control" id="search_discount" name="discount" type="text"
                                   placeholder=""/>
                        </div>
                    </div>

                    <div id="nowclass9Div" class="form-group" style="display: none">
                        <label class="col-sm-2 control-label no-padding-right" for="search_nowclass9">现系列</label>

                        <div class="col-xs-14 col-sm-7">
                            <select class="form-control" id="search_nowclass9" name="nowclass9"
                                    style="width: 100%;" >
                            </select>
                        </div>
                    </div>

                </form>

            </div>
        </div>
        <div class="modal-footer">

            <a href="#" class="btn" onclick="closeEditDialog()">关闭</a>

            <button type="button"  class="btn btn-primary" onclick="chageLaberSave()">保存</button>

        </div>
    </div>
</div>
<script>
    $(function() {
        $("#edit-dialog").on('show.bs.modal', function () {


        });
        $("#edit-dialog").on('hide.bs.modal', function () {
            $("#editForm").data('bootstrapValidator').destroy();
            $('#editForm').data('bootstrapValidator', null);

        });
    });
   function onChangeType() {
       if($("#select_changeType").val()=="CS"){
           $("#beforeclass9Div").show();
           $("#nowclass9Div").show();
           $("#discountDiv").hide();
       }
       if($("#select_changeType").val()=="PC"){
           $("#beforeclass9Div").hide();
           $("#nowclass9Div").hide();
           $("#discountDiv").show();
       }

   }



</script>
