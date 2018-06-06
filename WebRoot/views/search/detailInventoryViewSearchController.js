
   $(function(){
       $(".selectpicker").selectpicker({
           noneSelectedText : '--请选择--'//默认显示内容
       });
        initMultiSelect();
     	initKendoUIGrid();
    	$(".k-dropdown").css("width", "6em");
    	$(".k-grid-toolbar").css("display","none");//隐藏toolbar

    });

   function initMultiSelect(){
       $.ajax({
           url:basePath + "/sys/warehouse/list.do?filter_INI_type=4,9",
           cache: false,
           async: false,
           type: "POST",
           success: function (data, textStatus) {
               $("#filter_in_warehId").empty();
               var json = data;
               for (var i = 0; i < json.length; i++) {
                   $("#filter_in_warehId").append("<option value='" + json[i].code + "'>"  + json[i].name + "</option>");
               }
           }
       });
       $('.selectpicker').selectpicker('refresh');
       /*$("#filter_in_warehId").kendoMultiSelect({
           dataTextField: "name",
           dataValueField: "code",
           height: 400,
           suggest: true,
           dataSource: {
               transport: {
                   read:  basePath + "/sys/warehouse/list.do?filter_INI_type=4,9"
               }
           }
       })*/
   }
    function refresh(){
    	resetData();
    }
    function resetData(){
    	var gridData = $("#inventoryGrid").data("kendoGrid");
        gridData.dataSource.filter({});
    }
    function exportExcel(){
    	$(".k-grid-excel").click();
    }
    function showAdvSearchPanel(){
    	 $("#searchPanel").slideToggle("fast");
    }
    function search(){
    	var gridData = $("#inventoryGrid").data("kendoGrid");
    	var filters = serializeToFilter($("#searchForm"));
    	console.log(filters);
    	gridData.dataSource.filter({
            logic: "and",
            filters: filters
        });
    	
    }
    function initKendoUIGrid(){
    	
    	$("#inventoryGrid").kendoGrid({
    		toolbar: ["excel"],
            excel: {
                 fileName: "盘点明细统计.xlsx",
                 proxyURL: basePath + "/search/detailInventoryViewSearch/export.do",
                 filterable: true
            },
            excelExport: function(e) {
                var sheet = e.workbook.sheets[0];
                var warehIdTemplate = kendo.template(this.columns[5].template);
                var diffTemplate = kendo.template(this.columns[12].template);

                var rowIndex =1;
                var groupNum = this.dataSource._group.length;
                for (var i = 1; i < sheet.rows.length; i++) {
                    var row = sheet.rows[i];
                    if(row.cells[5+groupNum]){
                        var gridRow = $("#inventoryGrid").data("kendoGrid").dataItem("tr:eq("+rowIndex+")");
                        var dataItem = {
                            warehId: row.cells[5+groupNum].value,
                            warehName:gridRow.warehName,
                            billNo:gridRow.billNo,
                            qty:gridRow.qty,
                            billQty:gridRow.billQty
                        };
                        row.cells[5+groupNum].value = warehIdTemplate(dataItem);
                        row.cells[12+groupNum].value = diffTemplate(dataItem);
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
                            warehId: { type: "string" },
                            warehName: { type :"string"},
                            styleId: { type: "string" },                           
                            styleName: { type: "string" },
                            colorId: { type: "string" },
                            sizeId: { type: "string" },
                            qty: { type: "number" },
                            billQty: { type: "number" },
                            diffQty: { type: "number" },
                            price: { type: "number" }
                        }
                    }, 
                    data : "data",  
                    groups : "data"  
                },  
               
                transport: {
                    read: {
                        url: basePath + "/search/detailInventoryViewSearch/list.do",
                        type:"POST",
                        dataType: "json",
                        contentType:'application/json'
                    },
                    parameterMap : function(options) {  
                        return JSON.stringify(options);  
                    }  
                },
                sort:[{field: "billDate", dir: "desc"}],
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
                    field:"warehId",
                    title:"仓店编号",
                    width:"180px",
                    template:function(data) {
                        if (data.warehName) {
                            return "[" + data.warehId + "]" + data.warehName;
                        } else {
                            return "[" + data.warehId + "]";
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
                { field:"diffQty",title:"差异数量",width:"110px",groupable: false,groupable: false,
                    template:function(data) {
                        if(data.billNo){
                            return data.billQty-data.qty;
                        }else{
                            return 0;
                        }
                    }},
                { field:"price",title:"吊牌价",width:"110px",groupable: false,aggregates: ["average"]}
               
               
            ]
        });

    }

    function onGrouping(arg) {
/*
        kendoConsole.log("Group on " + kendo.stringify(arg.groups));
*/
    }
   