<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="modal-colorAndSize" class="modal fade" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header no-padding">
                <div class="table-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        <span class="white">x</span>
                    </button>
                    添加颜色尺码
                </div>
            </div>
            <div class="modal-body no-padding">
                <div class="widget-body">
                    <div class="widget-main">
                        <form class="form-horizontal" role="form" id="addColorSizeFrom">

                            <div class="form-group" id="colorGroup">
                                <label class="control-label col-sm-4" for="form_colorId_select">选择颜色</label>
                                <div class="col-xs-6 col-sm-6">
                                    <div class ="input-group ">
                                        <select class="chosen-select col-sm-6 form-control" id="form_colorId_select"
                                             name="colorId" multiple="multiple" onchange="changeBackColor()">
                                        </select>
                                        <span class="input-group-addon" title="添加颜色">
                                            <a  href='#'  class="white" onclick="addNewColor()">
                                                <i class="fa fa-plus red"></i>
                                            </a>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group" id="sizeGroup">
                                <label class="control-label col-sm-4" for="form_sizeId">选择尺码</label>
                                <div class="col-xs-6 col-sm-6">
                                    <div class ="input-group ">
                                        <select class="chosen-select col-sm-6 form-control" id="form_sizeId"
                                                name="sizeId" multiple="multiple" data-placeholder="尺码列表">
                                        </select>
                                        <span class="input-group-addon" title="添加尺码">
                                            <a  href='#'  class="white" onclick="addNewSize()">
                                                <i class="fa fa-plus red"></i>
                                            </a>
                                        </span>
                                    </div>

                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <div class="modal-footer no-margin-top">
                <a href="#" class="btn" onclick="colAndSize()">关闭</a>
                <a href="#" class="btn btn-primary" onclick="saveColAndSize()">确定</a>
            </div>
        </div>
    </div>
</div>
<script>
    $(function(){
        $("#modal-colorAndSize").on('show.bs.modal',function(){
            $("#addColorSizeFrom").resetForm();
            initColor();
            inimultiSize();
            $("#form_sizeId").multiselect('refresh');
        });

    });


function colAndSize(){
    $("#form_colorId_select").empty();
    $("#form_sizeId").empty();
    $("#modal-colorAndSize").modal('hide');
}

function addNewColor(){
    $("#editColorForm").resetForm();
    $("#edit_color_dialog").modal('show');
}
function addNewSize(){
    $("#editSizeForm").resetForm();
    $("#edit_size_dialog").modal('show');
}

function saveColAndSize(){
    var colorArray=$("#form_colorId_select").val();
    var sizeArray=$("#form_sizeId").val();
    if(colorArray == null){
        bootbox.alert("请选择颜色");
        return;
    }
    if(sizeArray==null){
        bootbox.alert("请选择尺码");
        return;
    }
   $.each(colorArray,function (colorIndex,color) {
      $.each(sizeArray,function (sizeIndex,size) {
          $("#CSGrid").addRowData($("#addDetailgrid").getRowData().length,{colorId:color,sizeId:size});
      })
   });
    $("#modal-colorAndSize").modal('hide');
}

</script>