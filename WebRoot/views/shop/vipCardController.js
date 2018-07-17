var searchUrl = basePath + "/shop/vipCard/page.do";
$(function () {
    $("#grid").jqGrid({
        height: "auto",
        url: searchUrl,
        datatype: "json",
        mtype: "POST",
        colModel: [
            {name: 'id', label: 'id', hidden: true, width: 20},
            {name: 'name', label: '会员卡名', width: 40},
            {name: 'rank', label: '会员等级', width: 20},
            {name: 'discount',label:'折扣',width:20},
            {name: 'freeShipping', label: '是否包邮',hidden:true},
            {name: 'freeShippingName', label: '是否包邮', width: 40,
                formatter: function (cellValue, options, rowObject) {
                    if (rowObject.freeShipping == 0) {
                        return '不包邮';
                    } else if (rowObject.freeShipping == 1) {
                        return '包邮';
                    }else {
                        return '';
                    }
                }
            },
            {name: 'upgradeType', label: '升级类型', width: 40,hidden:true},
            {name: 'upgradeTypeName', label: '升级类型', width: 40,
                formatter: function (cellValue, options, rowObject) {
                    if (rowObject.upgradeType == 0) {
                        return '手动升级';
                    } else if (rowObject.upgradeType == 1) {
                        return '自动升级';
                    }else {
                        return '';
                    }
                }},
            {name: 'upgradeDealNo', label: '升级规则&成交数量', width: 40},
            {name: 'upgradeConsumeNo', label: '升级规则&总消费金额', width: 40},
            {name: 'upgradePoints', label: '升级规则&总累计积分', width: 40},
            {name: 'createTime', label: '创建时间',  width: 40},
            {
                name: '', label: '操作', width: 20, align: 'center',
                formatter: function (cellvalue, options, rowObject) {
                    var id = rowObject.rank;
                    var html = "<a style='margin-left: 20px' href='#' onclick=del('" + id + "')><i class='ace-icon fa fa-trash-o red' title='删除'></i></a>";
                    return html;
                }
            },
            {name: 'remark', label: '备注',  width: 60}
        ],
        viewrecords: true,
        autowidth: true,
        rownumbers: true,
        altRows: true,
        rowNum: 20,
        rowList: [20, 50, 100],
        pager: "#grid-pager",
        multiselect: false,
        shrinkToFit: true,
        sortname: 'id',
        sortorder: "desc"
    });
    $("#grid").jqGrid('setFrozenColumns');
});
/*添加*/
function add() {
    pageType="add";
    $("#editVipCardForm").resetForm();
    $("#edit_vipCard_dialog").modal('show');
}
/**
 * 编辑
 */
function edit() {
    pageType="edit";
    var rowId = $("#grid").jqGrid("getGridParam", "selrow");
    if (rowId) {
        var row = $("#grid").jqGrid('getRowData', rowId);
        $("#edit_vipCard_dialog").modal("show");
        $("#editVipCardForm").loadData(row);
    } else {
        bootbox.alert("请选择一项进行修改！");
    }
}
/*
删除
 */
function del(id) {
    $.post(basePath + "/shop/vipCard/del.do?id="+id, function (result) {
        cs.closeProgressBar();
        if (result.success == true || result.success == 'true') {
            $.gritter.add({
                text: result.msg,
                class_name: 'gritter-success  gritter-light'
            });
            _search();
        } else {
            cs.showAlertMsgBox(result.msg);
        }
    }, 'json');

}
/*查询*/
function _search() {
    $("#grid").jqGrid('setGridParam', {
        page: 1,
        url: searchUrl
    });
    $("#grid").trigger("reloadGrid");
}