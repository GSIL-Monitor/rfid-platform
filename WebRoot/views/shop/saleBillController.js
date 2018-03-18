/**
 * Created by WingLi on 2017-01-04.
 */
$(function () {
    initGrid();
    setTimeout(function() {
        _search();
    },500);
});

function initGrid() {

    $("#grid").jqGrid({
        height: "auto",
        datatype: "json",
        mtype:"POST",
        colModel: [

            {name: 'billNo', label: '销售单号', editable:true,width: 150,frozen:true},
            {
                name: "", label: "查看明细", width: 70,editable:false,align:"center",sortable:false,frozen:true,
                formatter: function (cellvalue, options, rowObject) {
                    var id=rowObject.id;
                    return "<a href='"+basePath+"/shop/saleBill/listDtlPage.do?billId="+id+"'><i class='ace-icon fa fa-list'></i></a>";
                }
            },
            {name: 'type', label: '单据类型', editable:true,width: 100,formatter: function (cellvalue, options, rowObject) {
                if(cellvalue == 0) {
                    return "零售";
                } else {
                    return "退货";
                }
            }},
            {name: 'billDate', label: '销售日期', editable:true,width: 150},
            {name: 'shopId', label: '门店编号', editable:true,width: 100},
            {name: 'shopName', label: '门店名称', editable:true,width: 100,sortable:false},
            {name: 'payWay', label: '支付类型', editable:true,width: 100,formatter: function (cellvalue, options, rowObject) {
                return cs.isBlank(rowObject.payWayName)?"":rowObject.payWayName;
            }},
            {name: 'clientId', label: '收银员编号',editable:true, width: 100},
            {name: 'clientName', label: '收银员名称',editable:true, width: 100,sortable:false},
            {name: 'client2Id', label: '会员编号', width: 100},
            {name: 'client2Name', label: '会员名称', width: 100,sortable:false},
            {name: 'totOrderQty', label: '数量', width: 100},
            {name: 'totOrderValue', label: '吊牌金额', width: 100},
            {name: 'totActValue', label: '销售额',editable:true, width: 50},
            {name: 'balance', label: '差额',editable:true, width: 50},
            {name: 'toZero', label: '抹零',editable:true, width: 50},
            {name: 'remark', label: '备注',editable:true, width: 100}
        ],
        viewrecords: true,
        autowidth: true,
        rownumbers: true,
        altRows: true,
        rowNum: 20,
        rowList: [20, 50, 100],
        pager: "#grid-pager",
        multiselect: false,
        shrinkToFit: false,
        sortname : 'billDate',
        sortorder : "desc",
        autoScroll:false

    });
    $("#grid").jqGrid("setFrozenColumns");
}

	function refresh(){
		location.reload(true);
	}

function _search() {
    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);
    $("#grid").jqGrid('setGridParam', {
        page : 1,
        postData : params,
        url: basePath + "/shop/saleBill/page.do"
    });
    $("#grid").trigger("reloadGrid");
}


function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");

}

function add(){
}