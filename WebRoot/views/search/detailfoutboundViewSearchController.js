
   $(function(){
        initMultiSelect();
    	initKendoUIGrid();
    	$(".k-dropdown").css("width", "6em");
    	$(".k-grid-toolbar").css("display","none");//隐藏toolbar

    });
    function initMultiSelect(){
        $("#filter_in_origid").kendoMultiSelect({
            dataTextField: "name",
            dataValueField: "code",
            height: 400,
            suggest: true,
            dataSource: {
                transport: {
                    read:  basePath + "/sys/warehouse/list.do?filter_EQI_type=0"
                }
            }
        });
        $("#filter_in_destid").kendoMultiSelect({
            dataTextField: "name",
            dataValueField: "code",
            height: 400,
            suggest: true,
            dataSource: {
                transport: {
                    read:  basePath + "/sys/warehouse/list.do?filter_INI_type=4,9"
                }
            }
        });
        $("#filter_in_destUnitId").kendoMultiSelect({
            dataTextField: "name",
            dataValueField: "code",
            height: 400,
            suggest: true,
            dataSource: {
                transport: {
                    read:  basePath + "/sys/warehouse/list.do?filter_INI_type=0,1,2"
                }
            }
        })
    }
    function refresh(){
    	resetData();
    }
    function resetData(){
    	var gridData = $("#foutboundGrid").data("kendoGrid");
        gridData.dataSource.filter({
            field: "token",
                operator: "eq",
                value: 7
        });
    }
    function exportExcel(){
    	$(".k-grid-excel").click();
    }
    function showAdvSearchPanel(){
    	 $("#searchPanel").slideToggle("fast");
    }
    function search(){
    	var gridData = $("#foutboundGrid").data("kendoGrid");
    	var filters = serializeToFilter($("#searchForm"));
    	filters.push({
    	    field: "token",
	        operator: "eq",
	        value: 7
	    });
    	console.log(filters);
    	gridData.dataSource.filter({
            logic: "and",
            filters: filters
        });
    	
    }
    function initKendoUIGrid(){
    	
    	$("#foutboundGrid").kendoGrid({
    		toolbar: ["excel"],
            excel: {
                 fileName: "供应商发货明细统计.xlsx",
                 proxyURL: basePath + "/search/detailfoutboundViewSearch/export.do",
                 filterable: true
            },
            excelExport: function(e) {
                var sheet = e.workbook.sheets[0];
                var origTemplate = kendo.template(this.columns[5].template);
                var destTemplate = kendo.template(this.columns[6].template);
                var destUnitTemplate = kendo.template(this.columns[7].template);
                var rowIndex =1;
                var groupNum = this.dataSource._group.length;
                for (var i = 1; i < sheet.rows.length; i++) {
                    var row = sheet.rows[i];
                    if(row.cells[4+groupNum] && row.cells[5+groupNum] && row.cells[6+groupNum]){
                        var gridRow = $("#foutboundGrid").data("kendoGrid").dataItem("tr:eq("+rowIndex+")");
                        var dataItem = {
                            origid:row.cells[5+groupNum].value,
                            destid:row.cells[6+groupNum].value,
                            destUnitId:row.cells[7+groupNum].value,
                            destName:gridRow.destName,
                            destUnitName:gridRow.destUnitName,
                            origName:gridRow.origName

                        };
                        row.cells[5+groupNum].value = origTemplate(dataItem);
                        row.cells[6+groupNum].value = destTemplate(dataItem);
                        row.cells[7+groupNum].value = destUnitTemplate(dataItem);
                        rowIndex++;
                    }



                }
            },
            dataSource: {
            	schema : {  
                    total : "total",  
                    model : {  
                      
                        fields: {
                        	billDate: { type: "date" },
                            billNo: { type: "string" },
                            taskId:{type:"string"},
                            deviceId: { type: "string" },
                            origid: { type: "string" },
                            destid:{ type: "string" },
                            destUnitId:{ type: "string" },
                            origName:{type:"string"},
                            destName:{ type : "string" },
                            destUnitName:{ type: "string" },
                            styleId: { type: "string" },                           
                            styleName: { type: "string" },
                            colorId: { type: "string" },
                            sizeId: { type: "string" },
                            token:{type:"number"},
                            qty: { type: "number" },
                            billQty: { type: "number" },
                            diffQty: { type: "number" },
                            price: { type: "number" }
                        }
                    }, 
                    data : "data",  
                    groups : "data"
                },
                filter:{
                	logic: "and",
                    filters: [{
                    	field: "token",
            	        operator: "eq",
            	        value: 7
                    }]
                },
                sort:[{field: "billDate", dir: "desc"}],
                transport: {
                    read: {
                        url: basePath + "/search/detailOutboundViewSearch/list.do",
                        type:"POST",
                        dataType: "json",
                        contentType:'application/json'
                    },
                    parameterMap : function(options) {  
                        return JSON.stringify(options);  
                    }  
                },         
                pageSize: 500.0,
                serverSorting : true, 
                serverPaging : true,  
                serverGrouping : false ,
                serverFiltering : true,  
                aggregate: [
                           
                            { field: "qty", aggregate: "sum" },   
                            { field: "styleId", aggregate: "count" },   
                            { field: "price", aggregate: "average" },
                            
                        ]

               
            },
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
            group: onGrouping,
            columnMenu: true,
            filterable: {
                extra:false 
            },
         //   selectable: "multiple row",
            reorderable: true,
            resizable: true,
            scrollable: true,
            columns: [
                {
                    title: "日期",
                    field: "billDate",
                    width: "200px",
                    aggregates: [ "count"],
                    filterable: {
                        extra:true,
                        ui: function (element) {
                            element.kendoDatePicker({
                                format: "yyyy-MM-dd",
                                culture:"zh-CN"
                            });
                        }

                    },

                    format: "{0:yyyy-MM-dd HH:mm:ss}",
                    groupHeaderTemplate: function(data) {

                        var totQty = data.aggregates.qty.sum;
                        var value = kendo.toString(data.value, 'yyyy-MM-dd HH:mm:ss');
                        var avgPrice = data.aggregates.price.average;
                        return "日期:"+value +" 总数量:"+totQty+"; 平均价 :"+kendo.toString(avgPrice, '0.00');
                    }
                },
                {
                    title: "任务号",
                    field: "taskId",
                    width: "250px",
                    aggregates: [ "count"],
                    groupHeaderTemplate: function(data) {

                        var totQty = data.aggregates.qty.sum;
                        var value = data.value;
                        var avgPrice = data.aggregates.price.average;
                        return "任务号:"+value +" 总数量:"+totQty+"; 平均价 :"+kendo.toString(avgPrice, '0.00');
                    }
                },
                {
                    title: "单号",
                    field: "billNo",
                    width: "250px",
                    aggregates: [ "count"],
                    groupHeaderTemplate: function(data) {

                        var totQty = data.aggregates.qty.sum;
                        var value = data.value;
                        var avgPrice = data.aggregates.price.average;
                        return "单号:"+value +" 总数量:"+totQty+"; 平均价 :"+kendo.toString(avgPrice, '0.00');
                    }
                },
                {
                    field: "sku",
                    title: "SKU",
                    width:"150px"
                },
                {
                    field: "deviceId",
                    title: "设备号",
                    width:"110px"
                },
                {
                    field:"origid",
                    title:"发货仓库",
                    width:"180px",
                    template:function(data) {
                        if (data.origName) {
                            return "[" + data.origid + "]" + data.origName;
                        } else {
                            return "[" + data.origid + "]";
                        }
                    }
                },
                {
                    field:"destid",
                    title:"收货仓库",
                    width:"180px",
                    template:function(data) {
                        if(data.destName){
                            return "["+data.destid+"]"+data.destName;
                        }else{
                            if(data.destid){
                                return "["+data.destid+"]";

                            }else{
                                return "";
                            }

                        }
                    }
                },
                {
                    field:"destUnitId",
                    title:"收货方",
                    width:"180px",
                    template:function(data) {
                        if(data.destUnitName){
                            return "["+data.destUnitId+"]"+data.destUnitName;
                        }else{
                            if(data.destUnitId){
                                return "["+data.destUnitId+"]";
                            }else{
                                return "";
                            }

                        }
                    }
                },
                { field:"styleId",title:"款号",width:"140px",
                    aggregates: ["count"],
                    groupHeaderTemplate: function(data) {

                        var totQty = data.aggregates.qty.sum;
                        var value = data.value;
                        var avgPrice = data.aggregates.price.average;
                        return "款号:"+value +" 总数量:"+totQty+"; 平均价 :"+kendo.toString(avgPrice, '0.00');
                    }},
                { field:"styleName",title:"款名",width:"140px"},
                { field:"colorId",title:"色号",width:"80px"},
                { field:"sizeId",title:"尺号",width:"80px"},
                { field:"qty",title:"数量",width:"80px",groupable: false,
                    groupable: false,
                    aggregates: ["sum"]},
                { field:"billQty",title:"单据数量",width:"110px",groupable: false,groupable: false},
                { field:"diffQty",title:"差异数量",width:"110px",groupable: false,groupable: false},
                { field:"price",title:"吊牌价",width:"110px",groupable: false,aggregates: ["average"]}
               
               
            ]
        });

    }

    function onGrouping(arg) {
/*
        kendoConsole.log("Group on " + kendo.stringify(arg.groups));
*/
    }
   