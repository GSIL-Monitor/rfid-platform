var printParameter={
    fontSize58mm:9,//58mm小票字体的大小
    fontSize80mm:9,//80mm小票字体的大小
    fontSize110mm:9,//110mm小票字体的大小
    receiptWidth58mm:220,//58mm小票的宽度
    receiptWidth80mm:260,//80mm小票的宽度
    receiptWidth110mm:500,//110mm小票的宽度
    receiptheight58mm:693,//58mm小票的高度
    receiptheight80mm:741,//80mm小票的高度
    receiptheight110mm:610,//100mm小票的高度
    aRowheight:25,//每行的高度
    intervalHeight:10//行之间的间隔


};

var LODOP; //声明为全局变量


$(function () {

});
function selectheadPrint() {
    $("#footerPrint").hide();
    $("#headPrint").show();
}
function selectfoorerPrint() {
    $("#footerPrint").show();
    $("#headPrint").hide();
}
function selectRuleReceipt(sum) {
    $("#ruleReceipt").find("ul").each(function(index,element){
        var b=$("#ruleReceipt").find("ul")[index].getAttribute('data-name');
        if(b==sum){
            if($(this).attr("class")=="stecs"){
                $(this).attr("class","stecs on") ;
                findPrintSet(sum);
            }else{
                $(this).attr("class","stecs") ;
            }
        }else{
            $(this).attr("class","stecs") ;
        }

    });

}
function selectThis(t,selectId) {
    if($(t).attr("class")=="stecs"){
        $(t).attr("class","stecs on");
        $("#"+selectId).show();
    }else{
        $(t).attr("class","stecs");
        $("#"+selectId).hide();
    }
}

function writeFootExtend(t) {
    console.log($(t).val());
    $("#footExtend").find("span").html($(t).val());
}

function save() {
    var recordRule="";//记录选择的规格
    var isSavePrint=false;//是否保存
    var printCode="";
    $("#ruleReceipt").find("ul").each(function(index,element){
        if($(this).attr("class")!="stecs"){
            recordRule=$(this).data("name");
            isSavePrint=true;
        }
    });
    if($("#receiptName").val()==""||$("#receiptName").val()==undefined){
        $.gritter.add({
            text: "请填写小票名称",
            class_name: 'gritter-success  gritter-light'
        });
        return;
    }

    if(isSavePrint){
        //规定的小票的宽度的高度字体的大小
        var receiptWith;
        var receiptHight;
        var receiptFontSize;
        if(recordRule=="58"){
            receiptWith=printParameter.receiptWidth58mm;
            receiptHight=printParameter.receiptheight58mm;
            receiptFontSize=printParameter.fontSize58mm;
        }
        if(recordRule=="80"){
            receiptWith=printParameter.receiptWidth80mm;
            receiptHight=printParameter.receiptheight80mm;
            receiptFontSize=printParameter.fontSize80mm;
        }
        if(recordRule=="110"){
            receiptWith=printParameter.receiptWidth110mm;
            receiptHight=printParameter.receiptheight110mm;
            receiptFontSize=printParameter.fontSize110mm;
        }
        var sum=0;
        LODOP=getLodop();
        var str="LODOP.PRINT_INITA(0,0,"+receiptWith+","+receiptHight+",'打印模板');";
        $("#printTop").find(".Print-Bg-Top-div").each(function (index,element) {
            if(index==0){
                if(!$(this).find(".col-xs-8").is(":hidden")){
                    var id="\""+$(this).find(".col-xs-8").attr("id")+"\"";
                    var message="\""+$(this).find(".col-xs-8").find("span").text()+"\"";
                    str+="LODOP.ADD_PRINT_TEXTA("+id+",0,"+10+","+receiptWith+","+printParameter.aRowheight+","+message+");";
                    str+="LODOP.SET_PRINT_STYLEA(0,\"FontSize\",20);";
                    str+="LODOP.SET_PRINT_STYLEA(0,\"Bold\",1);";
                    str+="LODOP.SET_PRINT_STYLEA(0,\"Alignment\",2);";
                    if(printCode==""){
                        printCode+=$(this).find(".col-xs-8").attr("id");
                    }else {
                        printCode+=","+$(this).find(".col-xs-8").attr("id");
                    }
                }
                if(!$(this).find(".col-xs-8").is(":hidden")){
                    sum++;
                }
            }else if(index==1){
                if(!$(this).find(".col-xs-8").is(":hidden")){
                    var id="\""+$(this).find(".col-xs-8").attr("id")+"\"";
                    var message="\""+$(this).find(".col-xs-8").find("span").text()+"\"";
                    var top=((sum)*printParameter.aRowheight+(sum)*printParameter.intervalHeight);
                    str+="LODOP.ADD_PRINT_TEXTA("+id+","+top+","+10+","+receiptWith+","+printParameter.aRowheight+","+message+");";
                    str+="LODOP.SET_PRINT_STYLEA(0,\"FontSize\",20);";
                    str+="LODOP.SET_PRINT_STYLEA(0,\"Bold\",1);";
                    str+="LODOP.SET_PRINT_STYLEA(0,\"Alignment\",2);";
                    if(printCode==""){
                        printCode+=$(this).find(".col-xs-8").attr("id");
                    }else {
                        printCode+=","+$(this).find(".col-xs-8").attr("id");
                    }
                }
                if(!$(this).find(".col-xs-8").is(":hidden")){
                    sum++;
                }
            }else{
                if(!$(this).is(":hidden")){
                    var id="\""+$(this).attr("id")+"\"";
                    var meessage4="\""+$(this).find(".col-xs-4").find("span").text()+"\"";
                    var meessage8="\""+$(this).find(".col-xs-8").find("span").text()+"\"";
                    if(sum==0){
                        var width=parseInt(receiptWith/3*1);
                        var widthA=parseInt(receiptWith/3*2);
                        var leftA=width+10
                        str+="LODOP.ADD_PRINT_TEXT(0,10,"+width+","+printParameter.aRowheight+","+printParameter.aRowheight+","+meessage4+");";
                        str+="LODOP.SET_PRINT_STYLEA(0,\"FontSize\","+receiptFontSize+");";
                        str+="LODOP.ADD_PRINT_TEXTA("+id+",0,"+leftA+","+widthA+","+printParameter.aRowheight+","+meessage8+");";
                        str+="LODOP.SET_PRINT_STYLEA(0,\"FontSize\","+receiptFontSize+");";
                        if(printCode==""){
                            printCode+=$(this).attr("id");
                        }else {
                            printCode+=","+$(this).attr("id");
                        }

                    }else{
                        var width=parseInt(receiptWith/3*1);
                        var widthA=parseInt(receiptWith/3*2);
                        var leftA=width+10
                        var top=((sum)*printParameter.aRowheight+(sum)*printParameter.intervalHeight);
                        str+="LODOP.ADD_PRINT_TEXT("+top+",10,"+width+","+printParameter.aRowheight+","+meessage4+");";
                        str+="LODOP.SET_PRINT_STYLEA(0,\"FontSize\","+receiptFontSize+");";
                        str+="LODOP.ADD_PRINT_TEXTA("+id+","+top+","+leftA+","+widthA+","+printParameter.aRowheight+","+meessage8+");";
                        str+="LODOP.SET_PRINT_STYLEA(0,\"FontSize\","+receiptFontSize+");";
                        if(printCode==""){
                            printCode+=$(this).attr("id");
                        }else {
                            printCode+=","+$(this).attr("id");
                        }
                    }
                    sum++;
                }
            }
        });
        var top=((sum)*printParameter.aRowheight+(sum)*printParameter.intervalHeight);
        var html="\"<body><table style='text-align: center;font-size:12px;'><thead style='text-align:center' border='0' cellspacing='0' cellpadding='0' width='100%' align='center'><tr><th align='left' nowrap='nowrap' style='border:0px;height: 20px;'>商品</th><th align='right' nowrap='nowrap' style='border:0px;height: 20px;'>数量</th><th align='right' nowrap='nowrap' style='border:0px;height: 20px;'>原价</th><th align='right' nowrap='nowrap' style='border:0px;height: 20px;'>折后价</th><th align='right' nowrap='nowrap' style='border:0px;height: 20px;'>金额</th></tr></thead><tbody><tr style='border-top:1px dashed black;padding-top:5px;'><td align='left' style='border-top:1px dashed black;padding-top:5px;'>合计:</td><td align='right' style='border-top:1px dashed black;padding-top:5px;'>0</td><td style='border-top:1px dashed black;padding-top:5px;'>&nbsp;</td><td style='border-top:1px dashed black;padding-top:5px;'>&nbsp;</td><td align='right' style='border-top:1px dashed black;padding-top:5px;'>0</td></tr></tbody></table></body>\"";
        str+="LODOP.ADD_PRINT_HTM("+top+",10,"+receiptWith+","+receiptHight+","+html+");";
        str+="LODOP.SET_PRINT_STYLEA(0,\"ItemName\",\"baseHtml\");";
        sum=0;
        $("#printFoot").find(".col-xs-12").each(function (index,element) {
            if(!$(this).is(":hidden")){
                if($(this).attr("id")!="footExtend"){
                    var id="\""+$(this).attr("id")+"\"";
                    var meessage4="\""+$(this).find(".col-xs-4").find("span").text()+"\"";
                    var meessage8="\""+$(this).find(".col-xs-8").find("span").text()+"\"";
                    var width=parseInt(receiptWith/3*1);
                    var widthA=parseInt(receiptWith/3*2);
                    var leftA=width+10
                    var top=((sum)*printParameter.aRowheight+(sum+1)*printParameter.intervalHeight);
                    str+="LODOP.ADD_PRINT_TEXT("+top+",10,"+width+","+printParameter.aRowheight+","+meessage4+");";
                    str+="LODOP.SET_PRINT_STYLEA(0,\"LinkedItem\",\"baseHtml\");";
                    str+="LODOP.SET_PRINT_STYLEA(0,\"FontSize\","+receiptFontSize+");";
                    str+="LODOP.ADD_PRINT_TEXTA("+id+","+top+","+leftA+","+widthA+","+printParameter.aRowheight+","+meessage8+");";
                    str+="LODOP.SET_PRINT_STYLEA(0,\"LinkedItem\",\"baseHtml\");";
                    str+="LODOP.SET_PRINT_STYLEA(0,\"FontSize\","+receiptFontSize+");";
                    if(printCode==""){
                        if($(this).attr("id")!=undefined){
                            printCode+=$(this).attr("id");
                        }
                    }else {
                        if($(this).attr("id")!=undefined) {
                            printCode += "," + $(this).attr("id");
                        }
                    }

                }else{
                    var a=$("#footExtendWrite").val();
                    console.log(a);
                    var messge=$("#footExtendWrite").val().replace(/<br>/g,"\\n")
                    console.log(messge);
                    messge="\""+messge+"\"";
                    console.log(messge);
                    str+="LODOP.ADD_PRINT_TEXT("+((sum)*printParameter.aRowheight+(sum+1)*printParameter.intervalHeight)+",10,"+parseInt(receiptWith)+","+printParameter.aRowheight+","+messge+");";
                    str+="LODOP.SET_PRINT_STYLEA(0,\"LinkedItem\",\"baseHtml\");";
                    str+="LODOP.SET_PRINT_STYLEA(0,\"FontSize\","+receiptFontSize+");";
                }
                sum++;
            }
        });
        console.log(str);
        console.log(printCode);
        //得到需要保存的数据
        var printSet={
            id:$("#id").val(),
            ownerId:$("#ownerId").val(),
            printCont:str,
            printCode:printCode,
            name:$("#receiptName").val(),
            type:$("#receiptType").val(),
            printFootExtend:$("#footExtendWrite").val().replace(/<br>/g,"\\n"),
            ruleReceipt:recordRule,
            commonType:$("#commonType").val()
        }
        saveAjax(printSet);
        //eval(str);
       // LODOP.PREVIEW();
    }else{
        $.gritter.add({
            text: "请选择小票的规格",
            class_name: 'gritter-success  gritter-light'
        });
    }
}

function saveAjax(printSet) {
    cs.showProgressBar();
    $.ajax({
        dataType: "json",
        async: true,
        url: basePath + "/sys/printset/savePrintSetMessage.do",
        data: {
            printSet: JSON.stringify(printSet)
        },
        type: "POST",
        success: function (msg) {
            cs.closeProgressBar();
            if (msg.success) {
                $.gritter.add({
                    text: msg.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                $("#id").val(msg.result.id);
                $("#ownerId").val(msg.result.ownerId);
            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
}
function findPrintSet(sum) {
    $.ajax({
        dataType: "json",
        async: true,
        url: basePath + "/sys/printset/findPrintSet.do",
        data: {
            ruleReceipt: sum,
            type:$("#receiptType").val()
        },
        type: "POST",
        success: function (msg) {

            if (msg.success) {
                var result=msg.result;
                if(result!=undefined){
                    $("#id").val(result.id);
                    $("#ownerId").val(result.ownerId);
                    $("#receiptName").val(result.name);
                    $("#receiptType").val(result.type);
                    $("#commonType").val(result.commonType);
                    var printFootExtend=""+result.printFootExtend;
                    printFootExtend=printFootExtend.replace(/\\n/g, "<br>");
                    $("#footExtendWrite").val(printFootExtend);
                    $("#footExtend").find("span").html(printFootExtend);
                    $("#headPrint").find("div").each(function (index,element) {
                        var name=$(this).data("name");

                        if(!(result.printCode.indexOf(name)!= -1)){
                            $(this).attr("class","stecs");
                            $("#"+name).hide();
                        }else{
                            $(this).attr("class","stecs on");
                            $("#"+name).show();
                        }
                    });
                    $("#footerPrint").find("div").each(function (index,element) {
                        var name=$(this).data("name");

                        if(!(result.printCode.indexOf(name)!= -1)){
                            $(this).attr("class","stecs");
                            $("#"+name).hide();
                        }else{
                            $(this).attr("class","stecs on");
                            $("#"+name).show();
                        }
                    });
                }else{
                    $("#id").val("");
                    $("#ownerId").val("");
                    $("#receiptName").val("");
                    //$("#receiptType").val("SO");
                    $("#commonType").val("1");
                    $("#footExtendWrite").val("欢迎来到Ancient Stone");
                    $("#footExtend").find("span").html("欢迎来到Ancient Stone");
                    $("#headPrint").find("div").each(function (index,element) {
                        var name=$(this).data("name");
                        $(this).attr("class","stecs on");
                        $("#"+name).show();

                    });
                    $("#footerPrint").find("div").each(function (index,element) {
                        var name=$(this).data("name");
                        $(this).attr("class","stecs on");
                        $("#"+name).show();

                    });
                }

            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
}
function receiptTypeSelect() {
    var sum;

    $("#ruleReceipt").find("ul").each(function(index,element){

        if(!($(this).attr("class")=="stecs")) {
            debugger
            sum=$(this).data("name")
            findPrintSet(sum);
        }
    });

}

function selectheadPrintA4() {
    $("#headPrintA4").show();
    $("#tablePrintA4").hide();
    $("#footPrintA4").hide();
}
function selectTablePrintA4() {
    $("#headPrintA4").hide();
    $("#tablePrintA4").show();
    $("#footPrintA4").hide();
}
function selectFootPrintA4() {
    $("#headPrintA4").hide();
    $("#tablePrintA4").hide();
    $("#footPrintA4").show();
}