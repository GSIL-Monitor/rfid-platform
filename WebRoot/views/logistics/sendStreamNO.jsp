<%--
  Created by IntelliJ IDEA.
  User: yushen
  Date: 2017/9/25
  Time: 18:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="show-sendStreamNO-list" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog">
        <div class="modal-header no-padding">
            <div class="table-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">
                    <span class="white">&times;</span>
                </button>
                物流信息推送
            </div>
        </div>
        <div class="modal-content">
            <div class="modal-body">


                <div class="row">
                <%--    <form class="form-horizontal" role="search" id="uniqCode-editForm"  onkeydown="if(event.keyCode==13)return false;">--%>
                        <label class="col-sm-2 control-label no-padding-right"
                               for="add_StreamNO">物流单号</label>
                        <div class="col-xs-12 col-sm-9">
                            <input class="form-control" id="add_StreamNO" name="add_StreamNO" type="text"
                                   placeholder="物流单号"/>
                        </div>

                  <%--  </form>--%>
                </div>
            </div>
        </div>
      <%--  <div class="modal-content">
            <div class="hr hr4"></div>
            <table id="findWxShopListGrid"></table>
        </div>--%>
        <div class="modal-footer">
            <div id="sendStreamNO_buttonGroup">
                <button  type='button' id = 'sendStreamNO_savecode_button'  class='btn btn-primary' onclick='sendStreamNOs()'>保存</button>
            </div>
        </div>
    </div>
</div>

<script>
    //    $(function () {
    //        initUniqueCodeList();
    //    });

    /*function initfindWxShopList() {
        debugger;
        $("#findWxShopListGrid").jqGrid({
            height: 400,
            url: basePath + "/product/SendInventory/findPaymentMessage.do?billno=" + $("#search_billNo").val(),
            datatype: "json",
            mtype: "POST",
            colModel: [
                {name: 'billNo', label: '单号', width: 150},
                {name: 'customName', label: '客户名称', width: 150},
                {name: 'phone', label: '电话',  width: 150},
                {name: 'address',label:'地址',width:150}


            ],
            rownumbers: true,
            viewrecords: true,
            autowidth: true,
            altRows: true,
            multiselect: false,
            shrinkToFit: true,
            sortname: 'code',
            sortorder: "desc",

        });
        var parent_column = $("#findWxShopListGrid").closest('.modal-dialog');
        $("#findWxShopListGrid").jqGrid('setGridWidth', parent_column.width());
    }*/
    function sendStreamNOs() {
        var StreamNO=$("#add_StreamNO").val();
        var billNo=$("#search_billNo").val();
        $.ajax({
            async: false,
            dataType: "json",
            url: basePath + "/product/SendInventory/sendStreamNO.do",
            data: {
                StreamNO: StreamNO,
                billNo: billNo
            },
            type: "POST",
            success: function (data) {
                debugger;
                $.gritter.add({
                    text: data.result,
                    class_name: 'gritter-success  gritter-light'
                });
                $("#add_StreamNO").val("");
                $("#show-sendStreamNO-list").modal('hide');
            }
        });
    }

</script>