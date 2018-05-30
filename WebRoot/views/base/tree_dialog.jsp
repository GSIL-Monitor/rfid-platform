<%--
  Created by IntelliJ IDEA.
  User: yushen
  Date: 2018/5/30
  Time: 10:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="modal_unit_wwner_table" class="modal fade" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header no-padding">
                <div class="table-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        <span class="white">&times;</span>
                    </button>
                    选择所属方
                </div>
            </div>

            <div class="modal-body no-padding">
                <div class="widget-body">
                    <div class="widget-main" id="search_unit_Panel">
                        <form class="form-horizontal" role="form" id="search_unit_Form">
                            <div class="form-group">
                                <input class="form-control" id="ownerId" type="text" hidden/>
                                <input class="form-control" id="ownerName" type="text" hidden/>
                                <label class="col-xs-2 control-label text-right" for="search_unit_code">名称</label>
                                <div class="col-xs-4">
                                    <input class="form-control" id="search_unit_code"
                                           type="text" placeholder="模糊查询"/>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
                <div>
                    <div id="jstree"></div>
                </div>
            </div>

            <div class="modal-footer no-margin-top">
                <a href="#" class="btn" onclick="closeUnitSelectDialog()">关闭</a>
                <a href="#" class="btn btn-primary" onclick="selected_Owner_Unit();">确定</a>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>
<script>

</script>
