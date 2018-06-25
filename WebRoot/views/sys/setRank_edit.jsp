<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<div id="edit_setRank_dialog" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog">
        <div class="modal-header no-padding">
            <div class="table-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    <span class="white">&times;</span>
                </button>
                等级设置
            </div>
        </div>
        <div class="modal-content">
            <div class="modal-body">
                <form class="form-horizontal" role="form" id="editRankForm">
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right"><span class="text-danger"></span>会员等级:</label>
                        <div class="col-xs-10 col-sm-5">
                            <select class="chosen-select form-control selectpicker show-tick" data-live-search="true" id="rank" name="rank">
                                <option value="">请选择等级</option>
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

<script type="text/javascript">
    function closeSizeDialog() {
        $("#edit_setRank_dialog").modal('hide');
    }
    function saveSize() {
        var rank = $("#rank").val();
        $.ajax({
            url: basePath + "/shop/vipCard/updateRank.do?",
            cache: false,
            async: false,
            data:{
                idCard :rank
            },
            type: "POST",
            success: function (data, textStatus) {
                $("#rank").empty();
                $("#rank").append("<option value=''>--请选择等级--</option>");
                var json = data;
                for (var i = 0; i < json.length; i++) {
                    $("#rank").append("<option value='" + json[i].id + "'>" + "[" + json[i].rank + "]" + json[i].name + "</option>");
                }
                $(".selectpicker").selectpicker('refresh');
            }
        });

    }
    $(function () {
        $.ajax({
            url: basePath + "/shop/vipCard/list.do?filter_EQS_upgradeType=0",
            cache: false,
            async: false,
            type: "POST",
            success: function (data, textStatus) {
                $("#rank").empty();
                $("#rank").append("<option value=''>--请选择等级--</option>");
                var json = data;
                for (var i = 0; i < json.length; i++) {
                    $("#rank").append("<option value='" + json[i].id + "'>" + "[" + json[i].rank + "]" + json[i].name + "</option>");
                }
                $(".selectpicker").selectpicker('refresh');
            }
        });
    })
</script>
