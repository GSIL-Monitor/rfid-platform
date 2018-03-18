   var saleCountList=[];
   $(function(){
        initMutiSelect();
    	initKendoUIGrid(saleCountList);
    	$(".k-dropdown").css("width", "6em");
    	$(".k-grid-toolbar").css("display","none");//隐藏toolbar

    });
   function initMutiSelect(){
       $("#filter_in_warehId").kendoMultiSelect({
           dataTextField: "name",
           dataValueField: "code",
           height: 400,
           suggest: true,
           dataSource: {
               transport: {
                   read:  basePath + "/sys/warehouse/list.do?filter_EQI_type=4"
               }
           }
       })
   }
    function refresh(){
    	resetData();
    }
    function resetData(){
    	var gridData = $("#saleGrid").data("kendoGrid");
        gridData.dataSource.filter({});
    }
    function exportExcel(){
    	$(".k-grid-excel").click();
    }
    function showAdvSearchPanel(){
    	 $("#searchPanel").slideToggle("fast");
    }
    function search(){
        /**
        $.post(basePath+"/chart/saleCount/list.do",
            {"filter_GES_day":"2015-01-01","filter_LES_day":"2015-03-01"},
            function(result) {
                saleCountList = result;
                //$("#pivotgrid").data('kendoPivotGrid').refresh();
                initKendoUIGrid(saleCountList);
            }, 'json');
**/
        var dataSource = $("#pivotgrid").data("kendoPivotGrid").dataSource;
        dataSource.options.transport.read.url = basePath + "/chart/saleCount/list.do?filter_GES_day=2015-01-01&filter_LES_day=2015-03-01";
        dataSource.read();
    }
    function initKendoUIGrid(){
        var pivotgrid = $("#pivotgrid").kendoPivotGrid({
            autoBind:false,
            filterable: true,
            dataBound: function(e) {
                var fields = this.columnFields.add(this.rowFields).add(this.measureFields);

                fields.find(".k-button")
                    .each(function(_, item) {
                        item = $(item);
                        var text = item.data("name").split(".").slice(-1) + "";
                        var contents = item.contents().eq(0);
                        if(text==='day') {
                            text = '日期';
                        }
                        contents.replaceWith(text);
                    });
            },
            messages: {
                fieldMenu: {
                    info: "过滤条件",
                    sortAscending: "升序",
                    sortDescending: "降序",
                    filterFields: "过滤字段",
                    filter: "过滤",
                    include: "包含",
                    title: "包含",
                    clear: "清空",
                    ok: "确定",
                    cancel: "取消",
                    operators: {
                        contains: "包含",
                        doesnotcontain: "不包含",
                        startswith: "开始于",
                        endswith: "结束于",
                        eq: "相等于",
                        neq: "不相等于"
                    }
                }
            },
            columnWidth: 120,
            height: 570,
            dataSource: {
                transport: {
                    read : {
                        type:"POST",
                        dataType: "json",
                        contentType:'application/json'
                    }
                },
                schema: {
                    cube: {
                        dimensions: {
                            "day" : {caption: "所有日期"},
                            "门店" : {caption: "所有门店"},
                            "SKU" : {caption: "所有SKU"},
                            "styleId" : {caption: "所有款号"}
                        },
                        measures: {
                            "总数量": { field: "qty", aggregate: "sum" }
                        }
                    }
                },
                columns: ["day"],
                rows: ["门店"],
                measures: ["总数量"]
            }
        }).data("kendoPivotGrid");

        /*
        $("#configurator").kendoPivotConfigurator({
            dataSource: pivotgrid.dataSource,
            filterable: true,
            height: 570,
            messages: {
                measures: "拖放统计字段于此",
                columns: "拖放列字段于此",
                rows: "拖放行字段于此",
                measuresLabel: "统计",
                columnsLabel: "所有列",
                rowsLabel: "所有行",
                fieldsLabel: "字段",
                fieldMenu: {
                    info: "Show items with value that:",
                    sortAscending: "Sort Ascending",
                    sortDescending: "Sort Descending",
                    filterFields: "Fields Filter",
                    filter: "Filter",
                    include: "Include Fields...",
                    title: "Fields to include",
                    clear: "Clear",
                    ok: "Ok",
                    cancel: "Cancel",
                    operators: {
                        contains: "Contains",
                        doesnotcontain: "Does not contain",
                        startswith: "Starts with",
                        endswith: "Ends with",
                        eq: "Is equal to",
                        neq: "Is not equal to"
                    }
                }
            }
        });
        */

    }


   