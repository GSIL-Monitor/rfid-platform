<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="modal_duration" class="modal fade"  tabindex="-1">
    <div class="modal-dialog" style="width:800px">
        <div class="modal-content">
            <div class="modal-header no-padding">
                <div class="table-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        <span class="white">&times;</span>
                    </button>
                    播放时长设置
                </div>
            </div>

            <div class="modal-body no-padding">
                <div class="widget-body">

                    <div class="widget-main" id="searchPanel">
                        <form class="form-horizontal" role="form" id="durationForm">

                            <div class="form-group">
                                <label class="col-xs-offset-1 col-xs-2 control-label text-right" for="form_duration">播放时长（S）</label>
                                <div class="col-xs-8">
                                    <input  id="form_duration"  class="form-control" name="duration" type="text"/>
                                </div>
                            </div>

                        </form>
                    </div>
                </div>
            </div>

            <div class="modal-footer no-margin-top">
                <a href="#" class="btn" onclick="closeDurationDialog()">关闭</a>
                <a href="#" class="btn btn-primary" onclick="saveDuration()">保存</a>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>
<script>
    function closeDurationDialog(){
        $("#modal_duration").modal("hide");
    }

    function saveDuration(){
        $.ajax({
            type: "POST",
            url: basePath + "/neoen/prodMedia/changeDuration.do?styleId=" + $("#form_styleId").val() + "&duration=" + $("#form_duration").val(),
            dataType: "json",
            success: function (result) {
                $("#gridIV").trigger("reloadGrid");//刷新grid
                $("#modal_duration").modal("hide");
            }
        });
    }

</script>
