$(function () {
 //加载数据
    findButton();
});
var LODOP; //声明为全局变量
function findButton() {
    $.ajax({
        dataType: "json",
        url: basePath + "/sys/print/findAll.do",
        type: "POST",
        success: function (msg) {
            if(msg.success){
                debugger;
                var addcont="";
                for(var i=0;i<msg.result.length;i++){
                    addcont+="<div class='btn-group btn-group-sm pull-left'onclick=set('"+msg.result[i].id+"','"+msg.result[i].type+"') title='"+msg.result[i].name+"'>"+
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
    });
}


function refresh() {
    //location.reload(true);
    LODOP=getLodop();
    //var strBodyStyle="<style>"+document.getElementById("style1").innerHTML+"</style>";
    var strFormHtml="<body>"+document.getElementById("edit-dialog").innerHTML+"</body>";
    LODOP.PRINT_INIT("打印控件功能演示_Lodop功能_样式风格");
    LODOP.ADD_PRINT_TEXT(50,50,260,39,"打印与显示样式一致：");
    LODOP.ADD_PRINT_HTM(88,50,300,200,strFormHtml);
    LODOP.PREVIEW();

}

function set(id,type){
    debugger;
    $("#type").val(type);
    $("#saveID").val(id);
    //LODOP=getLodop();
    LODOP=getLodop(document.getElementById('LODOP2'),document.getElementById('LODOP_EM2'));
    $.ajax({
        dataType: "json",
        data:{"id":id},
        url: basePath + "/sys/print/findPrint.do",
        type: "POST",
        success: function (msg) {
            if(msg.success){
                debugger;
                $("#name").val(msg.result.name);
                eval(msg.result.printCont);
                LODOP.SET_SHOW_MODE("DESIGN_IN_BROWSE",1);
                LODOP.SET_SHOW_MODE("SETUP_ENABLESS","11111111000000");//隐藏关闭(叉)按钮
                LODOP.SET_SHOW_MODE("HIDE_GROUND_LOCK",true);//隐藏纸钉按钮
                LODOP.PRINT_DESIGN();
                var printCode=msg.result.printCode;
                var printCodes=printCode.split(",");
                for(var i=0;i<printCodes.length;i++){
                    $("#printCode").find("input").each(function(){
                        debugger;
                        if($(this).val()==printCodes[i]){
                            $(this).attr("checked", true);
                        }

                    });
                }
                var printtabCode=msg.result.printtabCode;
                var printtabCodes=printtabCode.split(",");
                for(var a=0;a<printtabCodes.length;a++){
                    $("#printtabCode").find("input").each(function(){
                        debugger;
                        if($(this).val()==printtabCodes[a]){
                            $(this).attr("checked", true);
                        }

                    });
                }

            }else{
                bootbox.alert(msg.msg);
            }
        }
    });
}

function savetype() {
    LODOP = getLodop(document.getElementById('LODOP2'), document.getElementById('LODOP_EM2'));
    var value=LODOP.GET_VALUE("ProgramCodes", 0);
    var print=[];
   var saveprint={};
   /*var name={"name":"55mm小票基础模板"};*/
    saveprint.name= $("#name").val();
    saveprint.type=$("#type").val();
    saveprint.printCont=value;
    var printCodes="";
    var printtabCodes="";
   var ioone=true;
   var isone=true;
   debugger;
    $("#printCode").find("input").each(function(){
        debugger;
        if($(this).is(':checked')==true){
            if(ioone){
                printCodes+= $(this).val();
                ioone=false;
            }else{
                printCodes+= ","+$(this).val();
            }
        }

    });
    $("#printtabCode").find("input").each(function(){
        debugger;
        if($(this).is(':checked')==true){
            if(isone){
                printtabCodes+= $(this).val();
                ioone=false;
            }else{
                printtabCodes+= ","+$(this).val();
            }
        }

    });
    saveprint.printCode=printCodes;
    saveprint.printtabCode=printtabCodes;
   /* saveprint.printCode="companyName,storehouseName,price,operatorName,remark,ordersSn,price";*/
    print.push(saveprint);
    $.ajax({
        dataType: "json",
        url: basePath + "/sys/print/saveprint.do",
        data:{printList:JSON.stringify(saveprint)},
        type: "POST",
        success: function (msg) {
            if(msg.success){
                $.gritter.add({
                    text: msg.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                $("#modal-addEpc-table").modal('hide');
                $("#editDetailgrid").trigger("reloadGrid");
            }else{
                bootbox.alert(msg.msg);
            }
        }
    });
}

function test() {
 /*   LODOP=getLodop(document.getElementById('LODOP2'),document.getElementById('LODOP_EM2'));
    var value="LODOP.PRINT_INITA(0,0,760,321,'打印控件功能演示_Lodop功能_在线编辑获得程序代码');LODOP.ADD_PRINT_TEXT(10,50,175,30,'先加的内容');";

    eval(value);
    LODOP.SET_SHOW_MODE("DESIGN_IN_BROWSE",1);
    LODOP.SET_SHOW_MODE("SETUP_ENABLESS","11111111000000");//隐藏关闭(叉)按钮
    LODOP.SET_SHOW_MODE("HIDE_GROUND_LOCK",true);//隐藏纸钉按钮
    LODOP.PRINT_DESIGN();*/
    LODOP = getLodop(document.getElementById('LODOP2'), document.getElementById('LODOP_EM2'));
    var value=LODOP.GET_VALUE("ProgramCodes", 0);
    var print=[];
    var saveprint={};
    /*var name={"name":"55mm小票基础模板"};*/
    saveprint.name=$("#name").val();
    saveprint.type=$("#type").val();
    saveprint.printCont=value;
    var printCodes="";
    var printtabCodes="";
    var ioone=true;
    var isone=true;
    debugger;
    $("#printCode").find("input").each(function(){
        debugger;
        if($(this).is(':checked')==true){
            if(ioone){
                printCodes+= $(this).val();
                ioone=false;
            }else{
                printCodes+= ","+$(this).val();
            }
        }

    });
    $("#printtabCode").find("input").each(function(){
        debugger;
        if($(this).is(':checked')==true){
            if(isone){
                printtabCodes+= $(this).val();
                ioone=false;
            }else{
                printtabCodes+= ","+$(this).val();
            }
        }

    });
    saveprint.printCode=printCodes;
    saveprint.printtabCode=printtabCodes;
    saveprint.id=$("#saveID").val();
    /* saveprint.printCode="companyName,storehouseName,price,operatorName,remark,ordersSn,price";*/
    print.push(saveprint);
    $.ajax({
        dataType: "json",
        url: basePath + "/sys/print/updateprint.do",
        data:{printList:JSON.stringify(saveprint)},
        type: "POST",
        success: function (msg) {
            if(msg.success){
                $.gritter.add({
                    text: msg.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                $("#modal-addEpc-table").modal('hide');
                $("#editDetailgrid").trigger("reloadGrid");
            }else{
                bootbox.alert(msg.msg);
            }
        }
    });



}


function ordersSn() {
    if(document.getElementById("ordersSn").checked){
        LODOP.ADD_PRINT_TEXTA('ordersSn',31,50,159,25,'');
    }else{
        LODOP.SET_PRINT_STYLEA('ordersSn','Deleted',true);
    }
}

/*function colour() {
    if(document.getElementById("colour").checked){
        LODOP.ADD_PRINT_TEXTA('colour',31,50,159,25,'');
    }else{
        LODOP.SET_PRINT_STYLEA('colour','Deleted',true);
    }
}
function size() {
    if(document.getElementById("size").checked){
        LODOP.ADD_PRINT_TEXTA('size',31,50,159,25,'');
    }else{
        LODOP.SET_PRINT_STYLEA('size','Deleted',true);
    }
}
function number() {
    if(document.getElementById("number").checked){
        LODOP.ADD_PRINT_TEXTA('number',31,50,159,25,'');
    }else{
        LODOP.SET_PRINT_STYLEA('number','Deleted',true);
    }
}*/
function companyName() {
    if(document.getElementById("companyName").checked){
        LODOP.ADD_PRINT_TEXTA('companyName',31,50,159,25,'');
    }else{
        LODOP.SET_PRINT_STYLEA('companyName','Deleted',true);
    }
}
function price() {
    if(document.getElementById("price").checked){
        LODOP.ADD_PRINT_TEXTA('price',31,50,159,25,'');
    }else{
        LODOP.SET_PRINT_STYLEA('price','Deleted',true);
    }
}
function remark() {
    if(document.getElementById("remark").checked){
        LODOP.ADD_PRINT_TEXTA('remark',31,50,159,25,'');
    }else{
        LODOP.SET_PRINT_STYLEA('remark','Deleted',true);
    }
}
function custerm() {
    if(document.getElementById("custerm").checked){
        LODOP.ADD_PRINT_TEXTA('custerm',31,50,159,25,'');
    }else{
        LODOP.SET_PRINT_STYLEA('custerm','Deleted',true);
    }
}
function operatorName() {
    if(document.getElementById("operatorName").checked){
        LODOP.ADD_PRINT_TEXTA('operatorName',31,50,159,25,'');
    }else{
        LODOP.SET_PRINT_STYLEA('operatorName','Deleted',true);
    }
}
function storehouseName() {
    if(document.getElementById("storehouseName").checked){
        LODOP.ADD_PRINT_TEXTA('storehouseName',31,50,159,25,'');
    }else{
        LODOP.SET_PRINT_STYLEA('storehouseName','Deleted',true);
    }
}

function orderDate() {
    if(document.getElementById("orderDate").checked){
        LODOP.ADD_PRINT_TEXTA('orderDate',31,50,159,25,'');
    }else{
        LODOP.SET_PRINT_STYLEA('orderDate','Deleted',true);
    }
}
function printTime() {
    if(document.getElementById("printTime").checked){
        LODOP.ADD_PRINT_TEXTA('printTime',31,50,159,25,'');
    }else{
        LODOP.SET_PRINT_STYLEA('printTime','Deleted',true);
    }
}
function billtype() {
    if(document.getElementById("billtype").checked){
        LODOP.ADD_PRINT_TEXTA('billtype',31,50,159,25,'');
    }else{
        LODOP.SET_PRINT_STYLEA('billtype','Deleted',true);
    }
}







