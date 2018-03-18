<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<div id="edit-dialog-print" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog">
        <%--<div class="modal-header no-padding" style="display: none">
            <object id="LODOP2" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=810 height=407>
                <embed id="LODOP_EM2" TYPE="application/x-print-lodop" width=100% height=750 PLUGINSPAGE="install_lodop.exe">
            </object>
        </div>--%>
        <div class="modal-header no-padding">
            <div class="table-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true" onclick="closeEditDialog()">
                    <span class="white">&times;</span>
                </button>
                打印
            </div>
        </div>
        <div class="modal-content">
            <div class="modal-body" id="addbutton">

                <%--<input type="hidden" name="id"/>

                <input type="hidden" id="form_creatorId" name='creatorId' hidden="true">
                <input type="hidden" id="form_creatorTime" name='createTime' hidden="true">
                <div class="form-group">
                    <label class="col-sm-2 control-label no-padding-right" for="form_code">编号</label>

                    <div class="col-xs-14 col-sm-7">
                        <input class="form-control" id="form_code" name="code"
                               onkeyup="this.value=this.value.toUpperCase()"
                               type="text"
                               placeholder=""/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label no-padding-right" for="form_name">名称</label>

                    <div class="col-xs-14 col-sm-7">
                        <input class="form-control" id="form_name" name="name"
                               type="text" placeholder=""/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label no-padding-right" for="form_groupId">分类</label>

                    <div class="col-xs-14 col-sm-7">
                        <select class="chosen-select form-control" id="form_groupId"
                                name="groupId">
                            <option value="">-请选择-</option>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label no-padding-right" for="form_ownerId">所属方</label>

                    <div class="col-xs-14 col-sm-7">
                        <div class="input-group">
                            <input class="form-control" id="form_ownerId"
                                   type="text" name="ownerId" readonly/>
                            <span class="input-group-btn">
                                <button class="btn btn-sm btn-default" type="button"
                                        onclick="openUnitDialog('#form_ownerId','#form_unitName',null,'withShop')">
                                    <i class="ace-icon fa fa-list"></i>
                                </button>
                            </span>
                            <input class="form-control" id="form_unitName"
                                   type="text" name="unitName" readonly/>
                        </div>
                    </div>

                </div>
                <!-- #section:elements.form -->
                <div class="form-group">
                    <label class="col-sm-2 control-label no-padding-right" for="from_tel">联系电话</label>

                    <div class="col-xs-14 col-sm-7">
                        <input class="form-control" id="from_tel" name="tel"
                               type="text"
                               placeholder=""/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label no-padding-right" for="form_linkman">联系人</label>

                    <div class="col-xs-14 col-sm-7">
                        <input class="form-control" id="form_linkman" name="linkman"
                               type="text" placeholder=""/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label no-padding-right" for="form_email">邮箱</label>

                    <div class="col-xs-14 col-sm-7">
                        <input class="form-control" id="form_email" name="email"
                               type="text" placeholder=""/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label no-padding-right" for="form_provinceId">所在省份</label>

                    <div class="col-xs-14 col-sm-7">
                        <input class="form-control" id="form_provinceId" name="provinceId"
                               type="text" placeholder=""/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label no-padding-right" for="form_cityId">所在城市</label>

                    <div class="col-xs-14 col-sm-7">
                        <input class="form-control" id="form_cityId" name="cityId"
                               type="text" placeholder=""/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label no-padding-right" for="form_address">街道地址</label>

                    <div class="col-xs-14 col-sm-7">
                        <input class="form-control" id="form_address" name="address"
                               type="text" placeholder=""/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label no-padding-right" for="form_remark">备注</label>

                    <div class="col-xs-14 col-sm-7">
                        <input class="form-control" id="form_remark" name="remark"
                               type="textarea" placeholder=""/>
                    </div>
                </div>--%>


            </div>
        </div>
        <div class="modal-footer" style="display: none">
            <input type="hidden" id="billno" name='creatorId' hidden="true">
            <%--<input type="hidden" id="id" name='creatorId' hidden="true">--%>

            <%--<a href="#" class="btn" onclick="closeEditDialog()">关闭</a>--%>

            <%--<button type="button" href="#" class="btn btn-primary" onclick="printSave()">打印</button>--%>

        </div>
        <%--<div class="modal-footer" id="loadtab">


        </div>--%>
    </div>
</div>
<script>
    $(function () {
        /*$.ajax({
         dataType: "json",
         url: basePath + "/sys/print/findAll.do",
         type: "POST",
         success: function (msg) {
         if(msg.success){
         debugger;
         var addcont="";
         for(var i=0;i<msg.result.length;i++){
         addcont+="<div class='form-group' onclick=set('"+msg.result[i].id+"') title='"+msg.result[i].name+"'>"+
         "<button class='btn btn-info'>"+
         "<i class='cae-icon fa fa-refresh'></i>"+
         "<span class='bigger-10'>套打"+(i+1)+"</span>"+
         "</button>"+
         "</div>"
         }
         $("#addbutton").html(addcont);

         }else{
         bootbox.alert(msg.msg);
         }
         }
         });*/

    });
    function closeEditDialog() {
        $("#edit-dialog-print").modal('hide');
    }
</script>
