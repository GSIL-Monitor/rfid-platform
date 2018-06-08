var printParameter={
    fontSize58mm:9,//58mm小票字体的大小
    fontSize80mm:9,//80mm小票字体的大小
    fontSize110mm:9,//110mm小票字体的大小
    fontSizeA4:12,//A4字体的大小
    receiptWidth58mm:220,//58mm小票的宽度
    receiptWidth80mm:260,//80mm小票的宽度
    receiptWidth110mm:500,//110mm小票的宽度
    receiptWidthA4:780,//A4的宽度
    receiptheight58mm:693,//58mm小票的高度
    receiptheight80mm:741,//80mm小票的高度
    receiptheight110mm:610,//100mm小票的高度
    receiptheightA4:639,//A4的宽度
    aRowheight:25,//每行的高度
    intervalHeight:10,//行之间的间隔
    sizeArry:"S,XS,M,L,XL,XXL,XXXL,F,其他"


};

var LODOP; //声明为全局变量


$(function () {
    debugger
   if(groupid==="JMS") {
       $("#isshowA4").hide();
       $("#receiptType").html(" <option value='SO'>销售单据</option><option value='SR'>销售退货</option>");
   }

   if(address!==""&&address!==undefined&&address.length>7){
       $("#footExtendWrite").val("欢迎来到Ancient Stone<br>地址："+address);
       $("#footExtend").find("span").html("欢迎来到Ancient Stone<br>地址："+address);
   }

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
    if(sum=="A4"){
        var html=lodingTableA4();
        $("#edit-A4-dialog").html(html);
        loadingTablecheck();
        $("#A4Print").show();
        $("#receiptPrint").hide();
        $("#ruleReceiptA4").find("ul").each(function(index,element){
            var b=$("#ruleReceiptA4").find("ul")[index].getAttribute('data-name');
            if(b==sum){
               /* if($(this).attr("class")=="stecs"){*/
                    $(this).attr("class","stecs on") ;
                    findPrintSetA4(sum);
                /*}else{
                    $(this).attr("class","stecs") ;
                }*/
            }else{
                $(this).attr("class","stecs") ;
            }

        });
    }else{
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


}
function selectRuleReceiptA4(sum) {
    if(sum=="A4"){

        $("#ruleReceiptA4").find("ul").each(function(index,element){
            var b=$("#ruleReceiptA4").find("ul")[index].getAttribute('data-name');
            if(b==sum){
                if($(this).attr("class")=="stecs"){
                    $(this).attr("class","stecs on") ;
                    findPrintSetA4(sum);
                }else{
                    $(this).attr("class","stecs") ;
                }
            }else{
                $(this).attr("class","stecs") ;
            }

        });
    }else{
        $("#A4Print").hide();
        $("#receiptPrint").show();
        $("#ruleReceipt").find("ul").each(function(index,element){
            var b=$("#ruleReceipt").find("ul")[index].getAttribute('data-name');
            if(b==sum){
              /*  if($(this).attr("class")=="stecs"){*/
                    $(this).attr("class","stecs on") ;
                    findPrintSet(sum);
               /* }else{
                    $(this).attr("class","stecs") ;
                }*/
            }else{
                $(this).attr("class","stecs") ;
            }

        });
    }


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
    cs.showProgressBar();
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
            cs.closeProgressBar();
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
                    if(address!==""&&address!==undefined&&address.length>7){
                        $("#footExtendWrite").val("欢迎来到Ancient Stone<br>地址："+address);
                        $("#footExtend").find("span").html("欢迎来到Ancient Stone<br>地址："+address);
                    }else{
                        $("#footExtendWrite").val("欢迎来到Ancient Stone");
                        $("#footExtend").find("span").html("欢迎来到Ancient Stone");
                    }

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
function findPrintSetA4(sum) {
    cs.showProgressBar();
    $.ajax({
        dataType: "json",
        async: true,
        url: basePath + "/sys/printset/findPrintSet.do",
        data: {
            ruleReceipt: sum,
            type:$("#receiptTypeA4").val()
        },
        type: "POST",
        success: function (msg) {
            cs.closeProgressBar();
            if (msg.success) {
                var result=msg.result;
                if(result!=undefined){
                    $("#idA4").val(result.id);
                    $("#ownerIdA4").val(result.ownerId);
                    $("#receiptNameA4").val(result.name);
                    $("#receiptTypeA4").val(result.type);
                    $("#commonTypeA4").val(result.commonType);
                    $("#headPrintA4").find("div").each(function (index,element) {
                        var name=$(this).data("name");

                        if(!(result.printCode.indexOf(name)!= -1)){
                            $(this).attr("class","stecs");
                            $("#"+name+"A4").hide();
                        }else{
                            $(this).attr("class","stecs on");
                            $("#"+name+"A4").show();
                        }
                    });
                    $("#footerPrint").find("div").each(function (index,element) {
                        var name=$(this).data("name");

                        if(!(result.printCode.indexOf(name)!= -1)){
                            $(this).attr("class","stecs");
                            $("#"+name+"A4").hide();
                        }else{
                            $(this).attr("class","stecs on");
                            $("#"+name+"A4").show();
                        }
                    });
                    $("#edit-A4-dialog").find("th").each(function (index,element) {
                       /* var name=$(this).attr("class").substring(0,$(this).attr("class").length-2);
                        if(!(result.printTableCode.indexOf(name)!= -1)){
                            $(this).hide();
                        }*/
                    });
                    $("#edit-A4-dialog").find("td").each(function (index,element) {
                       /* var name=$(this).attr("class").substring(0,$(this).attr("class").length-2);
                        if(!(result.printTableCode.indexOf(name)!= -1)){
                            $(this).hide();
                        }*/
                    });
                }else{
                    $("#id").val("");
                    $("#ownerId").val("");
                    $("#receiptName").val("");
                    //$("#receiptType").val("SO");
                    $("#commonType").val("1");
                    $("#headPrint").find("div").each(function (index,element) {
                        var name=$(this).data("name");
                        $(this).attr("class","stecs on");
                        $("#"+name+"A4").show();

                    });
                    $("#footerPrint").find("div").each(function (index,element) {
                        var name=$(this).data("name");
                        $(this).attr("class","stecs on");
                        $("#"+name+"A4").show();

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
function selectThisA4(t,selectId) {
    if($(t).attr("class")=="stecs"){
        $(t).attr("class","stecs on");
        $("#"+selectId).show();
    }else{
        $(t).attr("class","stecs");
        $("#"+selectId).hide();
    }
}
function selectThisA4class(t,selectId) {
    if($(t).attr("class")=="stecs"){
        $(t).attr("class","stecs on");
        //$("."+selectId).show();
        $("#edit-A4-dialog").find("th").each(function (index,element) {
            if($(this).data("name")==selectId){
                $(this).show();
            }
        });
        $("#edit-A4-dialog").find("td").each(function (index,element) {
            if($(this).data("name")==selectId){
                $(this).show();
            }
        });
    }else{
        $(t).attr("class","stecs");
        //$("."+selectId).hide();
        $("#edit-A4-dialog").find("th").each(function (index,element) {
            if($(this).data("name")==selectId){
                $(this).hide();
            }
        });
        $("#edit-A4-dialog").find("td").each(function (index,element) {
            if($(this).data("name")==selectId){
                $(this).hide();
            }
        });
    }
}
function saveA4() {
    var recordRule="";//记录选择的规格
    var isSavePrint=false;//是否保存
    var printCode="";
    var printTableCode="";
    $("#ruleReceiptA4").find("ul").each(function(index,element){
        if($(this).attr("class")!="stecs"){
            recordRule=$(this).data("name");
            isSavePrint=true;
        }
    });
    if($("#receiptNameA4").val()==""||$("#receiptName").val()==undefined){
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
        if(recordRule=="A4"){
            receiptWith=printParameter.receiptWidthA4;
            receiptHight=printParameter.receiptheightA4;
            receiptFontSize=printParameter.fontSizeA4;
        }
        var sum=0;
        var recordsum=0;//记录前两次打印的次数
        LODOP=getLodop();
        var str="LODOP.PRINT_INITA(0,0,"+receiptWith+","+receiptHight+",'打印模板');";
        $("#printTopA4").find("span").each(function (index,element) {
            if(!$(this).is(":hidden")){
                var id="\""+$(this).data("name")+"\"";
                var message="\""+$(this).text()+"\"";
                console.log(id);
                console.log(message);
                if(index==0){
                    str+="LODOP.ADD_PRINT_TEXTA("+id+",0,"+10+","+receiptWith+","+printParameter.aRowheight+","+message+");";
                    str+="LODOP.SET_PRINT_STYLEA(0,\"FontSize\",15);";
                    str+="LODOP.SET_PRINT_STYLEA(0,\"Bold\",1);";
                    str+="LODOP.SET_PRINT_STYLEA(0,\"Alignment\",2);";
                    if(printCode==""){
                        printCode+=$(this).data("name");
                    }else{
                        printCode+=","+$(this).data("name");
                    }

                    recordsum=sum+1;
                }else if(index==1) {
                    var top=(sum*printParameter.aRowheight+sum*printParameter.intervalHeight);
                    str+="LODOP.ADD_PRINT_TEXTA("+id+","+top+","+10+","+receiptWith+","+printParameter.aRowheight+","+message+");";
                    str+="LODOP.SET_PRINT_STYLEA(0,\"FontSize\",15);";
                    str+="LODOP.SET_PRINT_STYLEA(0,\"Bold\",1);";
                    str+="LODOP.SET_PRINT_STYLEA(0,\"Alignment\",2);";
                    if(printCode==""){
                        printCode+=$(this).data("name");
                    }else{
                        printCode+=","+$(this).data("name");
                    }
                    recordsum=sum+1;
                }else{
                    if(recordsum%2!=0) {
                        if ((sum + 1) % 2 == 0) {
                            var num = parseInt((sum + 1) / 2);
                            var top = (num * printParameter.aRowheight + num * printParameter.intervalHeight);
                            str += "LODOP.ADD_PRINT_TEXTA(" + id + "," + top + "," + 10 + "," + parseInt(receiptWith / 2) + "," + printParameter.aRowheight + "," + message + ");";
                            str += "LODOP.SET_PRINT_STYLEA(0,\"FontSize\"," + receiptFontSize + ");";
                            str += "LODOP.SET_PRINT_STYLEA(0,\"Alignment\",2);";
                            if(printCode==""){
                                printCode+=$(this).data("name");
                            }else{
                                printCode+=","+$(this).data("name");
                            }
                        } else {
                            var num = parseInt((sum + 1) / 2);
                            var top = (num * printParameter.aRowheight + num * printParameter.intervalHeight);
                            str += "LODOP.ADD_PRINT_TEXTA(" + id + "," + top + "," + (parseInt(receiptWith / 2) + 10) + "," + parseInt(receiptWith / 2) + "," + printParameter.aRowheight + "," + message + ");";
                            str += "LODOP.SET_PRINT_STYLEA(0,\"FontSize\"," + receiptFontSize + ");";
                            str += "LODOP.SET_PRINT_STYLEA(0,\"Alignment\",2);";
                            if(printCode==""){
                                printCode+=$(this).data("name");
                            }else{
                                printCode+=","+$(this).data("name");
                            }
                        }
                    }else{
                        if ((sum) % 2 == 0) {
                            var num = parseInt((sum) / 2)+1;
                            var top = (num * printParameter.aRowheight + num * printParameter.intervalHeight);
                            str += "LODOP.ADD_PRINT_TEXTA(" + id + "," + top + "," + 10 + "," + parseInt(receiptWith / 2) + "," + printParameter.aRowheight + "," + message + ");";
                            str += "LODOP.SET_PRINT_STYLEA(0,\"FontSize\"," + receiptFontSize + ");";
                            str += "LODOP.SET_PRINT_STYLEA(0,\"Alignment\",2);";
                            if(printCode==""){
                                printCode+=$(this).data("name");
                            }else{
                                printCode+=","+$(this).data("name");
                            }
                        } else {
                            var num = parseInt((sum) / 2)+1;
                            var top = (num * printParameter.aRowheight + num * printParameter.intervalHeight);
                            str += "LODOP.ADD_PRINT_TEXTA(" + id + "," + top + "," + (parseInt(receiptWith / 2) + 10) + "," + parseInt(receiptWith / 2) + "," + printParameter.aRowheight + "," + message + ");";
                            str += "LODOP.SET_PRINT_STYLEA(0,\"FontSize\"," + receiptFontSize + ");";
                            str += "LODOP.SET_PRINT_STYLEA(0,\"Alignment\",2);";
                            if(printCode==""){
                                printCode+=$(this).data("name");
                            }else{
                                printCode+=","+$(this).data("name");
                            }
                        }
                    }

                }

                sum++;
            }
        });
        var top=((sum)*printParameter.aRowheight+(sum)*printParameter.intervalHeight);
        var tabbleth="";
        var html="\"<body><table style='text-align:center;font-size:12px;table-layout:fixed;' border='0' cellspacing='0' cellpadding='0' width='100%' align='center'><thead ><tr>"
        $("#edit-A4-dialog").find("th").each(function (index,element) {
            if(!$(this).is(":hidden")){
                var message=$(this).html();
                var classname=$(this).data("name");
                console.log(message);
                if(printParameter.sizeArry.indexOf(classname)!=-1) {
                    debugger
                    html += "<th align='middle' nowrap='nowrap' style='border:0px;font-size:12px;border:1px solid #000;width: 6%'>" + message + "</th>";
                    tabbleth += "<th align='middle'nowrap='nowrap' style='border:0px;font-size:12px;border:1px solid #000;width: 6%'>" + message + "</th>";
                }else{
                    html += "<th align='middle' nowrap='nowrap' style='border:0px;font-size:12px;border:1px solid #000;'>" + message + "</th>";
                    tabbleth += "<th align='middle'nowrap='nowrap' style='border:0px;font-size:12px;border:1px solid #000;'>" + message + "</th>";
                }

                if(printTableCode==""){
                    printTableCode+=classname;
                }else{
                    printTableCode+=","+classname;
                }
            }
        });
        html+="</tr></thead><tbody id='loadtabA4'><tr style='border-top:1px dashed black;padding-top:5px;border:1px solid #000;'>"
        $("#edit-A4-dialog").find("td").each(function (index,element) {
            if(!$(this).is(":hidden")){
                var message=$(this).html();
                console.log(message);
                if(printParameter.sizeArry.indexOf($(this).data("name"))!=-1){
                    html+="<td align='middle' style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;width: 7%'>"+message+"</td>"
                }else {
                    html+="<td align='middle' style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>"+message+"</td>"
                }

            }
        });
        html+="</tr></tbody></table>\"";
        console.log(html);
        str+="LODOP.ADD_PRINT_HTM("+top+",10,"+receiptWith+","+receiptHight+","+html+");";
        str+="LODOP.SET_PRINT_STYLEA(0,\"ItemName\",\"baseHtml\");";
        sum=0;
        $("#printFootA4").find("span").each(function (index,element) {
            if(!$(this).is(":hidden")){
                var id="\""+$(this).data("name")+"\"";
                var message="\""+$(this).text()+"\"";

                if((sum)%2==0){
                    var num=parseInt((sum)/2);
                    var top=(num*printParameter.aRowheight+num*printParameter.intervalHeight);
                    str+="LODOP.ADD_PRINT_TEXTA("+id+","+top+",10,"+parseInt(receiptWith/2)+","+printParameter.aRowheight+","+message+");";
                    str+="LODOP.SET_PRINT_STYLEA(0,\"LinkedItem\",\"baseHtml\");";
                    str+="LODOP.SET_PRINT_STYLEA(0,\"FontSize\","+receiptFontSize+");";
                    str+="LODOP.SET_PRINT_STYLEA(0,\"Alignment\",2);";
                    printCode+=","+$(this).data("name");
                }else{
                    var num=parseInt((sum)/2);
                    var top=(num*printParameter.aRowheight+num*printParameter.intervalHeight);
                    str+="LODOP.ADD_PRINT_TEXTA("+id+","+top+","+(parseInt(receiptWith/2)+10)+","+parseInt(receiptWith/2)+","+printParameter.aRowheight+","+message+");";
                    str+="LODOP.SET_PRINT_STYLEA(0,\"LinkedItem\",\"baseHtml\");";
                    str+="LODOP.SET_PRINT_STYLEA(0,\"FontSize\","+receiptFontSize+");";
                    str+="LODOP.SET_PRINT_STYLEA(0,\"Alignment\",2);";
                    printCode+=","+$(this).data("name");
                }
              /*  var top=((sum)*printParameter.aRowheight+(sum+1)*printParameter.intervalHeight);
                console.log(id);
                console.log(message);*/


                sum++;
            }
        });
        console.log(str);
        console.log(printTableCode);
        //得到需要保存的数据
        var printSet={
            id:$("#idA4").val(),
            ownerId:$("#ownerIdA4").val(),
            printCont:str,
            printCode:printCode,
            name:$("#receiptNameA4").val(),
            type:$("#receiptTypeA4").val(),
            printTableCode:printTableCode,
            printTableTh:tabbleth,
            //printFootExtend:$("#footExtendWrite").val().replace(/<br>/g,"\\n"),
            ruleReceipt:recordRule,
            commonType:$("#commonTypeA4").val()
        }
        saveAjax(printSet);
        //eval(str);
        //LODOP.PREVIEW();
    }else{
        $.gritter.add({
            text: "请选择小票的规格",
            class_name: 'gritter-success  gritter-light'
        });
    }
}

function lodingTableA4() {
    var tableHtml="<table style='text-align: center;font-size:12px;border-collapse:collapse;border:1px solid #000;'class='col-xs-12 col-sm-12 col-md-12 col-lg-12'>";
    var tableHtmlcont="<tbody id='loadtabA4'><tr style='border-top:1px ;padding-top:5px;border:1px solid #000;'>";
    tableHtml+="<thead >";
    tableHtml+="<tr style='border:1px solid #000;'>";
    tableHtml+="<th align='left' data-name='styleId' style='border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'>款号</th>";
    tableHtml+="<th align='left' data-name='styleName' style='border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'>款名</th>";
    tableHtml+="<th align='left' data-name='colorId' style='border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'>颜色</th>";
    tableHtmlcont+="<td align='left' data-name='styleId' style='border-top:1px ;padding-top:5px;border:1px solid #000;word-wrap:break-word;'>&nbsp;</td>";
    tableHtmlcont+="<td align='left' data-name='styleName' style='border-top:1px ;padding-top:5px;border:1px solid #000;word-wrap:break-word;'>&nbsp;</td>";
    tableHtmlcont+="<td align='left' data-name='colorId' style='border-top:1px ;padding-top:5px;border:1px solid #000;word-wrap:break-word;'>&nbsp;</td>";
        for(var i=0;i<printParameter.sizeArry.split(",").length;i++){
            var classname=printParameter.sizeArry.split(",")[i]
            if(classname=="其他"){
                tableHtml+="<th align='left'  data-name='other' style='border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;width: 7%'>"+printParameter.sizeArry.split(",")[i]+"</th>";
                tableHtmlcont+="<td align='left' data-name='other' style='border-top:1px ;padding-top:5px;border:1px solid #000;word-wrap:break-word;width: 7%'>0</td>";
            }else{
                tableHtml+="<th align='left'  data-name='"+classname+"' style='border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;width: 7%'>"+printParameter.sizeArry.split(",")[i]+"</th>";
                tableHtmlcont+="<td align='left' data-name='"+classname+"' style='border-top:1px ;padding-top:5px;border:1px solid #000;word-wrap:break-word;width: 7%'>0</td>";
            }

        }
    tableHtml+="<th align='left' data-name='qty' style='border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'>小计</th>";
    tableHtml+="<th align='left' data-name='price'  style='border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'>吊牌价</th>";
    tableHtml+="<th align='left' data-name='totPrice'  style='border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'>金额</th></tr></thead>";
    tableHtmlcont+="<td align='left' data-name='qty' style='border-top:1px ;padding-top:5px;border:1px solid #000;word-wrap:break-word;'>&nbsp;</td>";
    tableHtmlcont+="<td align='left' data-name='price' style='border-top:1px ;padding-top:5px;border:1px solid #000;word-wrap:break-word;'>0</td>";
    tableHtmlcont+=" <td align='left' data-name='totPrice' style='border-top:1px ;padding-top:5px;border:1px solid #000;'>&nbsp;</td></tr></tbody></table>";
    var html=tableHtml+tableHtmlcont;
    console.log(tableHtml);
    console.log(tableHtmlcont);
    console.log(html);
    return html;
}
function loadingTablecheck() {
    var tableHtmlCheck="<ul>";
    tableHtmlCheck+="<li class='headTitleLi'><div class='stecs on' data-name='styleId' onclick=selectThisA4class(this,\"styleId\")><i></i><span>款号</span></div></li>";
    tableHtmlCheck+="<li class='headTitleLi'><div class='stecs on' data-name='styleName' onclick=selectThisA4class(this,\"styleName\")><i></i><span>款名</span></div></li>";
    tableHtmlCheck+="<li class='headTitleLi'><div class='stecs on' data-name='colorId' onclick=selectThisA4class(this,\"colorId\")><i></i><span>颜色</span></div></li>";
    for(var i=0;i<printParameter.sizeArry.split(",").length;i++){
        var classname=printParameter.sizeArry.split(",")[i]
        if(classname=="其他"){
            tableHtmlCheck+="<li class='headTitleLi'><div class='stecs on' data-name='other' onclick=selectThisA4class(this,'other')><i></i><span>"+classname+"</span></div></li>";
        }else{
            tableHtmlCheck+="<li class='headTitleLi'><div class='stecs on' data-name='"+classname+"' onclick=selectThisA4class(this,'"+classname+"')><i></i><span>"+classname+"</span></div></li>";
        }


    }
    tableHtmlCheck+="<li class='headTitleLi'><div class='stecs on' data-name='qty' onclick=selectThisA4class(this,\"qty\")><i></i><span>小计</span></div></li>";
    tableHtmlCheck+="<li class='headTitleLi'><div class='stecs on' data-name='price' onclick=selectThisA4class(this,\"price\")><i></i><span>吊牌价</span></div></li>";
    tableHtmlCheck+="<li class='headTitleLi'><div class='stecs on' data-name='totPrice' onclick=selectThisA4class(this,\"totPrice\")><i></i><span>金额</span></div></li>";
    $("#tablePrintA4").html(tableHtmlCheck)
}
