var searchUrl = basePath + "/logistics/accountStatementView/page.do?filter_EQS_unitId="+$("#search_id").val();
$(function () {
    initDate();
    iniGrid();

});

function refresh() {
    location.reload(true);
}
function initDate(){
    var startDate = getMonthFirstDay("yyyy-MM-dd");
    var endDate = getToDay("yyyy-MM-dd");
    $('.startDate').datepicker('setDate', startDate);
    $('.endDate').datepicker('setDate', endDate);
}
function _search() {
    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);

    $("#minxigrid").jqGrid('setGridParam', {
        url: searchUrl,
        page: 1,
        postData: params
    });
    $("#minxigrid").trigger("reloadGrid");
}
function iniGrid() {
    /*$("#minxigrid").jqGrid({
        height: "auto",
        url: basePath + "/sys/unit/page.do?filter_EQS_id="+$("#edit_id").val(),
        mtype: "POST",
        datatype: "json",
        colModel: [
            {name: 'id', label: '供应商名称', width: 100, editable: true},
            {name: 'name', label: '联系人', editable: true, width: 100},
            {
                name: 'owingValue', label: '欠款', editable: true, width: 100
            }

        ],
        viewrecords: true,
        autowidth: true,
        rownumbers: true,
        altRows: true,
        rowNum: 20,
        rowList: [20, 50, 100],
        pager: "#minxiPager",
        multiselect: false,
        shrinkToFit: true,
        /!*sortname: 'createTime',*!/
        sortorder: "desc",
        autoScroll: false
    });*/
    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);
    $("#minxigrid").jqGrid({
        height: "auto",
        mtype: "POST",
        url: searchUrl,
        datatype: "json",
        postData: params,
        colModel: [
            {name: 'billNo', label: '编号', sortable: false, width: 200},
            {name: 'billDate', label: '单据日期', sortable: false, width: 200},
            {name: 'billType', label: '单据类型', sortable: false, width: 150},
            {name: 'unitType', label: '对象类型', sortable: false, width: 150},
            {name: 'unitId', label: '对象', sortable: false, hidden: true},
            {name: 'actPrice', label: '实际价格', sortable: false, width: 150},
            {name: 'payPrice', label: '支付价格', sortable: false, width: 150},
            {name: 'diffPrice', label: '本单差额', sortable: false, width: 150},
            {name: 'oprId', label: '操作人', sortable: false, width: 150},
            {name: 'remark', label: '备注', sortable: false, width: 150},
            {name: 'ownerId', label: '单据所属', sortable: false, width: 150},
            {name: 'groupId', label: '组号', hidden: true},
            {name: 'totalOwingVal',label:'累计欠款',sortable:false,width:150}

        ],
        viewrecords: true,
        autowidth: true,
        rownumbers: true,
        altRows: true,
        rowNum: 50,
        rowList: [20, 50, 100],
        pager: "#minxiPager",
        multiselect: false,
        shrinkToFit: true,
        // sortname: 'billDate',
        // sortorder: 'asc',
        autoScroll: false,

        viewrecords: true,
        grouping: true,
        groupingView: {
            groupField: ['groupId'],
            groupColumnShow: [false],
            groupText: ['<b>{0} - {1} 项</b>'],
            plusicon: 'ace-icon fa fa-plus',
            minusicon: 'ace-icon fa fa-minus'
        },
    });


}
function add(){
    $("#editForm").resetForm();
    $("#form_code").attr("readOnly",false);
    /*$("#form_isAdmin").find("option[value='1']").removeAttr("selected");
     $("#form_isAdmin").find("option[value='0']").attr("selected",true);*/
    $("#edit-dialog").modal('show');
    $("#form_vendorId").val($("#search_id").val());
    $("#form_owingValue").val($("#search_owingValue").val());
}

function save(){
  /*  $('#editForm').data('bootstrapValidator').validate();
    if(!$('#editForm').data('bootstrapValidator').isValid()){
        return ;
    }*/
  debugger;
    var progressDialog = bootbox.dialog({
        message: '<p><i class="fa fa-spin fa-spinner"></i> 数据上传中...</p>'
    });
    $.post(basePath+"/logistics/paymentGatheringBill/save.do",
        $("#editForm").serialize(),
        function(result) {
            debugger;
            if(result.success == true || result.success == 'true') {
                progressDialog.modal('hide');
                $("#edit-dialog").modal('hide');
                $("#grid").trigger("reloadGrid");
            }else{
                bootbox.alert(result.msg);
            }
        }, 'json');
}
function closeEditDialog() {
    $("#edit-dialog").modal('hide');
}



