var searchUrl = basePath + "/factory/billDtlSearch/page.do";
$(function () {
    initDate();
    initKendoUIGrid();
    $(".k-dropdown").css("width", "6em");
    $(".k-grid-toolbar").css("display","none");//隐藏toolbar
    initMultiSelect();
});

function initDate(){
    var startDate = getMonthFirstDay("yyyy-MM-dd");
    var endDate = getToDay("yyyy-MM-dd");
    $('.startDate').datepicker('setDate', startDate);
    $('.endDate').datepicker('setDate', endDate);
}

function exportExcel(){
    $(".k-grid-excel").click();
}
function initMultiSelect() {
    $("#filter_in_taskOperator").kendoMultiSelect({
        dataTextField: "name",
        dataValueField: "code",
        height: 400,
        suggest: true,
        dataSource: {
            transport: {
                read:  basePath+"/hall/employee/list.do"
            }
        }
    });
    $.ajax({
        dataType:"json",
        url: basePath + "/factory/token/findCategory.do",
        async:false,
        type: "post",
        success: function(data) {
            $("#filter_in_category").kendoMultiSelect({
                dataTextField: "name",
                dataValueField: "code",
                height: 400,
                suggest: true,
                dataSource: {
                   data:data.result
                }
            });
        }
    });
    $.ajax({
        dataType:"json",
        url: basePath + "/factory/token/findToken.do",
        async:false,
        type: "post",
        success: function(data) {
            $("#filter_in_token").kendoMultiSelect({
                dataTextField: "name",
                dataValueField: "token",
                height: 400,
                suggest: true,
                dataSource: {
                    data:data.result
                }
            });
        }
    });
    $("#filter_in_groupCode").kendoMultiSelect({
        dataTextField: "name",
        dataValueField: "code",
        height: 400,
        suggest: true,
        dataSource: {
            transport: {
                read:  basePath+"/hall/department/list.do"
            }
        }
    });
}

function initKendoUIGrid(){
    var filters = serializeToFilter($("#searchForm"));
    $("#searchGrid").kendoGrid({
        toolbar: ["excel"],
        excel: {
            allPages: true
        },
        dataSource: {
            schema : {
                total : "total",
                model : {
                    fields: {
                        category:{ type:"string"},
                        factory:{ type:"string"},
                        billNo:{ type:"string"},
                        billDate:{ type:"date"},
                        printDate: { type: "date" },
                        endDate: { type :"date"},
                        progress: { type :"string"},
                        taskDate:{type:"date"},
                        totalTime: { type: "string" },
                        taskOperator: { type: "string" },
                        groupCode: { type: "string" },
                        code: { type: "string" },
                        token:{type:"number"},
                        tokenName:{type:"string"},
                        sign: { type: "string" },
                        remark: { type: "string"},
                        groupName:{ type:"string"}
                    }
                },
                data : "data",
                groups:"data"
            },
            group: [{
                field: "groupName",dir:" "
            },{
                field: "tokenName",dir:" "
            }],
            filter:{
                logic: "and",
                filters: filters
            },
            transport: {
                read: {
                    url: searchUrl,
                    type:"POST",
                    dataType: "json",
                    contentType:'application/json'
                },
                parameterMap : function(options) {
                    return JSON.stringify(options);
                }
            },
            sort:[{field: "billNo", dir: "desc"},{field: "taskDate", dir: "desc"}],
            pageSize: 500.0,
            serverSorting :true,
            serverPaging : true,
            serverGrouping :  true,
            serverFiltering : true

        },
        dataBound: function(e) {
            $.each($("#searchGrid").data("kendoGrid").dataItems(),function(index,row){
                if(row.sign == "是"){
                    $("tr[data-uid='"+row.uid+"']").addClass('errorData');
                }
            });
            collapseGroup();
        },

        selectable: "multiple, row",
        sortable: {
            mode: "multiple",
            allowUnsort: true
        },
        pageable: {
            input : true,
            buttonCount: 5,
            pageSize: 500.0,
            pageSizes : [100, 500, 1000, 2000, 5000]
        },

        groupable: true,
        columnMenu: true,
        filterable: {
            extra:false
        },

        reorderable: true,
        resizable: false,
        scrollable: true,
        columns: [
            {field: 'id', title: 'id', hidden: true, width: 180},
            {field: 'category', title: '产品', editable: true, width: 180},
            {field: 'factory', title: '工厂', editable: true, width: 180},
            {field: 'billNo', title: '办单单号', editable: true, width: 180},
            {field: 'groupName', title: '分组', hidden: true, width: 180},
            {field: 'tokenName', title: '任务名称', hidden: true, width: 180},
            {field: 'billDate', title: '发单日期', editable: true, width: 180,
                format: "{0:yyyy-MM-dd HH:mm:ss}"},
            {field: 'printDate', title: '打印时间', editable: true, width: 180,
                format: "{0:yyyy-MM-dd HH:mm:ss}"},
            {field: 'endDate', title: '办期', editable: true, width: 180,
                format: "{0:yyyy-MM-dd HH:mm:ss}"},
            {field: 'progress', title: '办单进度', editable: true, width: 180},
            {field: 'taskDate', title: '任务时间', editable: true, width: 180,
                filterable: {
                    extra:true,
                    ui: function (element) {
                        element.kendoDatePicker({
                            format: "yyyy-MM-dd",
                            culture:"zh-CN"
                        });
                    }
                },
                format: "{0:yyyy-MM-dd HH:mm:ss}"},
            {field: 'totalTime', title: '实际用时', editable: true, width: 180},
            {field: 'taskOperator', title: '操作员', editable: true, width: 180},
            {field: 'groupCode', title: '操作员组别', editable: true, width: 180},
            {field: 'code', title: '唯一号', editable: true, width: 180},
            {field: 'sign', title: '错误数据', editable: true, width: 180},
            {field: 'remark', title: '暂停原因', editable: true, width: 180}
        ]
    });
}

function search(){
    var gridData = $("#searchGrid").data("kendoGrid");
    var filters = serializeToFilter($("#searchForm"));
    console.log(filters);

    gridData.dataSource.filter({
        logic: "and",
        filters: filters
    });

}

function expandGroup(){
    var grid = $("#searchGrid").data("kendoGrid");
    grid.expandRow(".k-grouping-row");
}

function collapseGroup(){
    var grid = $("#searchGrid").data("kendoGrid");
    grid.collapseRow(".k-grouping-row");
}
function _clearSearch() {
    $('#searchForm')[0].reset();
}
function refresh(){
    resetData();
}
function resetData(){
    _clearSearch();
    initDate();
    search();
}
function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");
}

function signBill(){
    var searchGrid = $("#searchGrid").data("kendoGrid");
    var select=searchGrid.select();
    var ids="";
    for (var i=0;i<select.length;i++){
        ids+=searchGrid.dataItem(select[i]).id+",";
    }
    $.ajax({
        dataType:"json",
        url: basePath + "/factory/billDtlSearch/sign.do",
        data:{ids:ids},
        type: "post",
        success: function(data) {
            resetData();
        }
    });
}
function unSignBill(){
    var searchGrid = $("#searchGrid").data("kendoGrid");
    var select=searchGrid.select();
    var ids="";
    for (var i=0;i<select.length;i++){
        ids+=searchGrid.dataItem(select[i]).id+",";
    }
    $.ajax({
        dataType:"json",
        url: basePath + "/factory/billDtlSearch/unSign.do",
        data:{ids:ids},
        type: "post",
        success: function(data) {
            resetData();
        }
    });
}