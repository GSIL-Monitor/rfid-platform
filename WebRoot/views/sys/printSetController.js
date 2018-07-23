

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
        $("#A4NoSizePrint").hide();
        $("#receiptPrint").hide();
        $("#SanLianPrint").hide();
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
    }else if(sum=="A4N0Size"){
        debugger
        $("#A4NoSizePrint").show();
        $("#SanLianPrint").hide();
        $("#A4Print").hide();
        $("#receiptPrint").hide();
        $("#ruleReceiptA4NoSize").find("ul").each(function(index,element){
            var b=$("#ruleReceiptA4NoSize").find("ul")[index].getAttribute('data-name');
            if(b==sum){
                /* if($(this).attr("class")=="stecs"){*/
                $(this).attr("class","stecs on") ;
                //findPrintSetSanLian(sum);
                /*}else{
                 $(this).attr("class","stecs") ;
                 }*/
            }else{
                $(this).attr("class","stecs") ;
            }

        });
    } else if(sum=="SanLian"){
        var html=lodingTableSanLian();
        $("#edit-SanLian-dialog").html(html);
        loadingTablecheckSanLian();
        $("#SanLianPrint").show();
        $("#A4Print").hide();
        $("#receiptPrint").hide();
        $("#ruleReceiptSanLian").find("ul").each(function(index,element){
            var b=$("#ruleReceiptSanLian").find("ul")[index].getAttribute('data-name');
            if(b==sum){
                /* if($(this).attr("class")=="stecs"){*/
                $(this).attr("class","stecs on") ;
                findPrintSetSanLian(sum);
                /*}else{
                 $(this).attr("class","stecs") ;
                 }*/
            }else{
                $(this).attr("class","stecs") ;
            }

        });
    } else{
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
    }else if(sum=="A4N0Size"){
        $("#A4NoSizePrint").show();
        $("#SanLianPrint").hide();
        $("#A4Print").hide();
        $("#receiptPrint").hide();
        $("#ruleReceiptA4NoSize").find("ul").each(function(index,element){
            var b=$("#ruleReceiptA4NoSize").find("ul")[index].getAttribute('data-name');
            if(b==sum){
                /* if($(this).attr("class")=="stecs"){*/
                $(this).attr("class","stecs on") ;
                //findPrintSetSanLian(sum);
                /*}else{
                 $(this).attr("class","stecs") ;
                 }*/
            }else{
                $(this).attr("class","stecs") ;
            }

        });
    }else if(sum=="SanLian"){
        var html=lodingTableSanLian();
        $("#edit-SanLian-dialog").html(html);
        loadingTablecheckSanLian();
        $("#SanLianPrint").show();
        $("#A4Print").hide();
        $("#receiptPrint").hide();
        $("#ruleReceiptSanLian").find("ul").each(function(index,element){
            var b=$("#ruleReceiptSanLian").find("ul")[index].getAttribute('data-name');
            if(b==sum){
                /* if($(this).attr("class")=="stecs"){*/
                $(this).attr("class","stecs on") ;
                findPrintSetSanLian(sum);
                /*}else{
                 $(this).attr("class","stecs") ;
                 }*/
            }else{
                $(this).attr("class","stecs") ;
            }

        });
    }else{
        $("#A4Print").hide();
        $("#receiptPrint").show();
        $("#SanLianPrint").hide();
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
function selectRuleReceiptA4NoSize(sum) {
    if(sum=="A4N0Size"){
        $("#ruleReceiptA4NoSize").find("ul").each(function(index,element){
            var b=$("#ruleReceiptA4NoSize").find("ul")[index].getAttribute('data-name');
            if(b==sum){
                /* if($(this).attr("class")=="stecs"){*/
                $(this).attr("class","stecs on") ;
                //findPrintSetSanLian(sum);
                /*}else{
                 $(this).attr("class","stecs") ;
                 }*/
            }else{
                $(this).attr("class","stecs") ;
            }

        });
    }else if(sum=="A4"){
        var html=lodingTableA4();
        $("#edit-A4-dialog").html(html);
        loadingTablecheck();
        $("#A4Print").show();
        $("#receiptPrint").hide();
        $("#SanLianPrint").hide();
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
    }else if(sum=="SanLian"){
        var html=lodingTableSanLian();
        $("#edit-SanLian-dialog").html(html);
        loadingTablecheckSanLian();
        $("#SanLianPrint").show();
        $("#A4Print").hide();
        $("#receiptPrint").hide();
        $("#ruleReceiptSanLian").find("ul").each(function(index,element){
            var b=$("#ruleReceiptSanLian").find("ul")[index].getAttribute('data-name');
            if(b==sum){
                /* if($(this).attr("class")=="stecs"){*/
                $(this).attr("class","stecs on") ;
                findPrintSetSanLian(sum);
                /*}else{
                 $(this).attr("class","stecs") ;
                 }*/
            }else{
                $(this).attr("class","stecs") ;
            }

        });
    }else{
        $("#A4Print").hide();
        $("#receiptPrint").show();
        $("#SanLianPrint").hide();
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
function selectRuleReceiptSanLian(sum) {
    if(sum=="SanLian"){

        $("#ruleReceiptSanLian").find("ul").each(function(index,element){
            var b=$("#ruleReceiptSanLian").find("ul")[index].getAttribute('data-name');
            if(b==sum){
                if($(this).attr("class")=="stecs"){
                    $(this).attr("class","stecs on") ;
                    findPrintSetSanLian(sum);
                }else{
                    $(this).attr("class","stecs") ;
                }
            }else{
                $(this).attr("class","stecs") ;
            }

        });
    }else if(sum=="A4"){
        var html=lodingTableA4();
        $("#edit-A4-dialog").html(html);
        loadingTablecheck();
        $("#A4Print").show();
        $("#receiptPrint").hide();
        $("#SanLianPrint").hide();
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
    }else if(sum=="A4N0Size"){
        $("#A4NoSizePrint").show();
        $("#SanLianPrint").hide();
        $("#A4Print").hide();
        $("#receiptPrint").hide();
        $("#ruleReceiptSanLian").find("ul").each(function(index,element){
            var b=$("#ruleReceiptA4NoSize").find("ul")[index].getAttribute('data-name');
            if(b==sum){
                /* if($(this).attr("class")=="stecs"){*/
                $(this).attr("class","stecs on") ;
                //findPrintSetSanLian(sum);
                /*}else{
                 $(this).attr("class","stecs") ;
                 }*/
            }else{
                $(this).attr("class","stecs") ;
            }

        });
    }else{
        $("#A4Print").hide();
        $("#receiptPrint").show();
        $("#SanLianPrint").hide();
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
function writeFootExtendSanLian(t) {
    console.log($(t).val());
    $("#footExtendSanLian").find("span").html($(t).val());
}

function savePrint() {
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
function findPrintSetSanLian(sum) {
    cs.showProgressBar();
    $.ajax({
        dataType: "json",
        async: true,
        url: basePath + "/sys/printset/findPrintSet.do",
        data: {
            ruleReceipt: sum,
            type:$("#receiptTypeSanLian").val()
        },
        type: "POST",
        success: function (msg) {
            cs.closeProgressBar();
            if (msg.success) {
                var result=msg.result;
                if(result!=undefined){
                    $("#idSanLian").val(result.id);
                    $("#ownerIdSanLian").val(result.ownerId);
                    $("#receiptNameSanLian").val(result.name);
                    $("#receiptTypeSanLian").val(result.type);
                    $("#commonTypeSanLian").val(result.commonType);
                    var printFootExtend=""+result.printFootExtend;
                    printFootExtend=printFootExtend.replace(/\\n/g, "<br>");
                    $("#footExtendWriteSanLian").val(printFootExtend);
                    $("#footExtendSanLian").find("span").html(printFootExtend);
                    $("#headPrintSanLian").find("div").each(function (index,element) {
                        var name=$(this).data("name");

                        if(!(result.printCode.indexOf(name)!= -1)){
                            $(this).attr("class","stecs");
                            $("#"+name+"SanLian").hide();
                        }else{
                            $(this).attr("class","stecs on");
                            $("#"+name+"SanLian").show();
                        }
                    });
                    $("#printFootSanLian").find("div").each(function (index,element) {
                        var name=$(this).data("name");

                        if(!(result.printCode.indexOf(name)!= -1)){
                            $(this).attr("class","stecs");
                            $("#"+name+"SanLian").hide();
                        }else{
                            $(this).attr("class","stecs on");
                            $("#"+name+"SanLian").show();
                        }
                    });
                    $("#edit-SanLian-dialog").find("th").each(function (index,element) {
                        /* var name=$(this).attr("class").substring(0,$(this).attr("class").length-2);
                         if(!(result.printTableCode.indexOf(name)!= -1)){
                         $(this).hide();
                         }*/
                    });
                    $("#edit-SanLian-dialog").find("td").each(function (index,element) {
                        /* var name=$(this).attr("class").substring(0,$(this).attr("class").length-2);
                         if(!(result.printTableCode.indexOf(name)!= -1)){
                         $(this).hide();
                         }*/
                    });
                }else{
                    $("#idSanLian").val("");
                    $("#ownerIdSanLian").val("");
                    $("#receiptNameSanLian").val("");
                    //$("#receiptType").val("SO");
                    $("#commonTypeSanLian").val("1");
                    $("#headPrintSanLian").find("div").each(function (index,element) {
                        var name=$(this).data("name");
                        $(this).attr("class","stecs on");
                        $("#"+name+"SanLian").show();

                    });
                    $("#printFootSanLian").find("div").each(function (index,element) {
                        var name=$(this).data("name");
                        $(this).attr("class","stecs on");
                        $("#"+name+"SanLian").show();

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
function selectTablePrintA4NoSize() {
    $("#tablePrintA4NoSize").show();
    $("#footPrintA4NoSize").hide();
}
function selectFootPrintA4NoSize() {
    $("#tablePrintA4NoSize").hide();
    $("#footPrintA4NoSize").show();
}
function selectheadPrintSanLian() {
    $("#headPrintSanLian").show();
    $("#tablePrintSanLian").hide();
    $("#footPrintSanLian").hide();
}
function selectTablePrintSanLian() {
    $("#headPrintSanLian").hide();
    $("#tablePrintSanLian").show();
    $("#footPrintSanLian").hide();
}
function selectFootPrintSanLian() {
    $("#headPrintSanLian").hide();
    $("#tablePrintSanLian").hide();
    $("#footPrintSanLian").show();
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
function selectThisSanLian(t,selectId) {
    if($(t).attr("class")=="stecs"){
        $(t).attr("class","stecs on");
        $("#"+selectId).show();
    }else{
        $(t).attr("class","stecs");
        $("#"+selectId).hide();
    }
}
function selectThisA4NoSizeclass(t,selectId) {
    if($(t).attr("class")=="stecs"){
        $(t).attr("class","stecs on");
        //$("."+selectId).show();
        /*$("#edit-A4-dialog-TR").find("th").each(function (index,element) {
            if($(this).data("name")==selectId){
                console.log("saber:"+$(this).data("name"));
                $(this).show();
            }
        });*/
        $("#edit-A4-dialog-NoSize").find("."+selectId).show();

    }else{
        $(t).attr("class","stecs");
        $("#edit-A4-dialog-NoSize").find("."+selectId).hide();
        //$("."+selectId).hide();
        /*$("#edit-A4-dialog-TR").find("th").each(function (index,element) {
            if($(this).data("name")==selectId){
                console.log("saber:"+$(this).data("name"));
                $(this).hide();
            }
        });*/

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
function selectThisSanLianclass(t,selectId) {
    if($(t).attr("class")=="stecs"){
        $(t).attr("class","stecs on");
        //$("."+selectId).show();
        $("#edit-SanLian-dialog").find("th").each(function (index,element) {
            if($(this).data("name")==selectId){
                $(this).show();
            }
        });
        $("#edit-SanLian-dialog").find("td").each(function (index,element) {
            if($(this).data("name")==selectId){
                $(this).show();
            }
        });
    }else{
        $(t).attr("class","stecs");
        //$("."+selectId).hide();
        $("#edit-SanLian-dialog").find("th").each(function (index,element) {
            if($(this).data("name")==selectId){
                $(this).hide();
            }
        });
        $("#edit-SanLian-dialog").find("td").each(function (index,element) {
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
        var html="\"<table style='text-align:center;font-size:10px;table-layout:fixed;' border='0' cellspacing='0' cellpadding='0' width='100%' align='center'><thead >"
        $("#printTopA4").find("span").each(function (index,element){
            if(index==2||index==4){
               html+="<tr>"
            }

            if(!$(this).is(":hidden")){
                var id=$(this).data("name");
                var message=$(this).text();
                if(index==0||index==1){
                    html+="<tr><th  colspan='24' id='"+id+"' style='font-size:25px;padding-top:5px'>"+message+"</th></tr>'";
                }else if(index==6){
                    html+="<tr><th  colspan='12 ' id='"+id+"' style='font-size:25px;padding-top:5px'>"+message+"</th></tr>'";
                }else{
                    html+="<th  colspan='12 ' id='"+id+"' style='font-size:25px;padding-top:5px'>"+message+"</th>'";
                }
                if(printCode==""){
                    printCode+=id;
                }else{
                    printCode+=","+id;
                }
            }
            if(index==3||index==5){
                html+="</tr>"
            }
        });
        html+="<tr>"
        var tabbleth="";
        var tabblethall="";
        $("#edit-A4-dialog").find("th").each(function (index,element) {
            if(!$(this).is(":hidden")){
                var message=$(this).html();
                var classname=$(this).data("name");
                console.log(message);
                if(printParameter.sizeArry.indexOf(classname)!=-1) {
                    html+="<th align='middle' colspan='1'nowrap='nowrap' style='height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'>"+message+"</th>";
                    tabbleth+="<td align='middle' colspan='1'nowrap='nowrap' style='height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'><font color='black' tdata='SubSum' format='#'>##</font></td>";
                    tabblethall+="<td align='middle' colspan='1'nowrap='nowrap' style='height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'><font color='black' tdata='AllSum' format='#'>##</font></td>";
                }else if(classname=="styleId"){
                    html+="<th align='middle' colspan='3'nowrap='nowrap' style='height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'>"+message+"</th>";
                    tabbleth+="<td align='middle' colspan='3'nowrap='nowrap' style='height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'>本页合计</td>";
                    tabblethall+="<td align='middle' colspan='3'nowrap='nowrap' style='height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'>单据合计</td>";
                }else if(classname=="styleName"){
                    html+="<th align='middle' colspan='3'nowrap='nowrap' style='height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'>"+message+"</th>"
                    tabbleth+="<td align='middle' colspan='3'nowrap='nowrap' style='height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'></td>";
                    tabblethall+="<td align='middle' colspan='3'nowrap='nowrap' style='height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'></td>";
                }else if(classname=="colorId"){
                    html+="<th align='middle' colspan='3'nowrap='nowrap' style='height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'>"+message+"</th>"
                    tabbleth+="<td align='middle' colspan='3'nowrap='nowrap' style='height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'></td>"
                    tabblethall+="<td align='middle' colspan='3'nowrap='nowrap' style='height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'></td>"
                }else if(classname=="qty") {
                    html+="<th align='middle' colspan='2'nowrap='nowrap' style='height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'>"+message+"</th>"
                    tabbleth+="<td align='middle' colspan='2'nowrap='nowrap' style='height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'><font color='black' tdata='SubSum' format='#'>##</font></td>"
                    tabblethall+="<td align='middle' colspan='2'nowrap='nowrap' style='height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'><font color='black' tdata='AllSum' format='#'>##</font></td>"
                }else if(classname=="totPrice"){
                    html+="<th align='middle' colspan='2'nowrap='nowrap' style='height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'>"+message+"</th>"
                    tabbleth+="<td align='middle' colspan='2'nowrap='nowrap' style='height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'><font color='black' tdata='SubSum' format='#.00'>##</font></td>"
                    tabblethall+="<td align='middle' colspan='2'nowrap='nowrap' style='height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'><font color='black' tdata='AllSum' format='#.00'>##</font></td>"
                }else{
                    html+="<th align='middle' colspan='2'nowrap='nowrap' style='height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'>"+message+"</th>"
                    tabbleth+="<td align='middle' colspan='2'nowrap='nowrap' style='height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'></td>"
                    tabblethall+="<td align='middle' colspan='2'nowrap='nowrap' style='height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'></td>"
                }
                if(printTableCode==""){
                    printTableCode+=classname;
                }else{
                    printTableCode+=","+classname;
                }
            }
        });
        html+="</thead><tbody id='loadtabA4'></tbody><tfoot><tr>"+tabbleth+"</tr><tr>"+tabblethall+"</tr><tr>"
        $("#printFootA4").find("span").each(function (index,element) {

            if(!$(this).is(":hidden")){
                var id=$(this).data("name");
                var message=$(this).text();
                html+="<th colspan='10' id='"+id+"' align='middle' style='font-size:15px;padding-top:5px;word-wrap:break-word'>xx</th>"
                if(printCode==""){
                    printCode+=id;
                }else{
                    printCode+=","+id;
                }
            }

        });
        html+="<th colspan='4'  align='middle' style='font-size:15px;padding-top:5px;word-wrap:break-word'>第<font tdata='PageNO' format='0' color='blue'>##</font>页</span>/共<font tdata='PageCount' format='0' color='blue'>##</font></span>页</th></tr>"
        html+="</tfoot></table>\""
        str+="LODOP.ADD_PRINT_TABLE(100,1,"+receiptWith+","+receiptHight+","+html+");"
       /* $("#printTopA4").find("span").each(function (index,element) {
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
        var top=((recordsum+(sum-recordsum)/2+1)*printParameter.aRowheight+(recordsum+(sum-recordsum)/2+1)*printParameter.intervalHeight);
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
                /!*  var top=((sum)*printParameter.aRowheight+(sum+1)*printParameter.intervalHeight);
                 console.log(id);
                 console.log(message);*!/


                sum++;
            }
        });
        console.log(str);
        console.log(printTableCode);*/
        //得到需要保存的数据
        var printSet={
            id:$("#idA4").val(),
            ownerId:$("#ownerIdA4").val(),
            printCont:str,
            printCode:printCode,
            name:$("#receiptNameA4").val(),
            type:$("#receiptTypeA4").val(),
            printTableCode:printTableCode,
            printTableTh:html,
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
function lodingTableSanLian() {
    var tableHtml="<table style='text-align: center;font-size:12px;border-collapse:collapse;border:1px solid #000;'class='col-xs-12 col-sm-12 col-md-12 col-lg-12'>";
    var tableHtmlcont="<tbody id='loadtabSanLian'><tr style='border-top:1px ;padding-top:5px;border:1px solid #000;'>";
    tableHtml+="<thead >";
    tableHtml+="<tr style='border:1px solid #000;'>";
    tableHtml+="<th align='left' data-name='styleId' style='border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'>款号</th>";
    tableHtml+="<th align='left' data-name='styleName' style='border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'>款名</th>";
    tableHtml+="<th align='left' data-name='colorId' style='border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'>颜色</th>";
    tableHtmlcont+="<td align='left' data-name='styleId' style='border-top:1px ;padding-top:5px;border:1px solid #000;word-wrap:break-word;'>&nbsp;</td>";
    tableHtmlcont+="<td align='left' data-name='styleName' style='border-top:1px ;padding-top:5px;border:1px solid #000;word-wrap:break-word;'>&nbsp;</td>";
    tableHtmlcont+="<td align='left' data-name='colorId' style='border-top:1px ;padding-top:5px;border:1px solid #000;word-wrap:break-word;'>&nbsp;</td>";
    for(var i=0;i<printParameter.sizeArrySanLian.split(",").length;i++){
        var classname=printParameter.sizeArrySanLian.split(",")[i]
        tableHtml+="<th align='left'  data-name='"+classname+"' style='border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;width: 7%'>"+printParameter.sizeArrySanLian.split(",")[i]+"</th>";
        tableHtmlcont+="<td align='left' data-name='"+classname+"' style='border-top:1px ;padding-top:5px;border:1px solid #000;word-wrap:break-word;width: 7%'>0</td>";


    }
    tableHtml+="<th align='left' data-name='qty' style='border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'>数量</th>";
    tableHtml+="<th align='left' data-name='price'  style='border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'>单价</th>";
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

function loadingTablecheckSanLian() {
    var tableHtmlCheck="<ul>";
    tableHtmlCheck+="<li class='headTitleLi'><div class='stecs on' data-name='styleId' onclick=selectThisSanLianclass(this,\"styleId\")><i></i><span>款号</span></div></li>";
    tableHtmlCheck+="<li class='headTitleLi'><div class='stecs on' data-name='styleName' onclick=selectThisSanLianclass(this,\"styleName\")><i></i><span>款名</span></div></li>";
    tableHtmlCheck+="<li class='headTitleLi'><div class='stecs on' data-name='colorId' onclick=selectThisSanLianclass(this,\"colorId\")><i></i><span>颜色</span></div></li>";
    for(var i=0;i<printParameter.sizeArrySanLian.split(",").length;i++){
        var classname=printParameter.sizeArrySanLian.split(",")[i]
        tableHtmlCheck+="<li class='headTitleLi'><div class='stecs on' data-name='"+classname+"' onclick=selectThisSanLianclass(this,'"+classname+"')><i></i><span>"+classname+"</span></div></li>";
    }
    tableHtmlCheck+="<li class='headTitleLi'><div class='stecs on' data-name='qty' onclick=selectThisSanLianclass(this,\"qty\")><i></i><span>数量</span></div></li>";
    tableHtmlCheck+="<li class='headTitleLi'><div class='stecs on' data-name='price' onclick=selectThisSanLianclass(this,\"price\")><i></i><span>单价</span></div></li>";
    tableHtmlCheck+="<li class='headTitleLi'><div class='stecs on' data-name='totPrice' onclick=selectThisSanLianclass(this,\"totPrice\")><i></i><span>金额</span></div></li>";
    $("#tablePrintSanLian").html(tableHtmlCheck)
}
function saveSanLian() {
    var recordRule="";//记录选择的规格
    var isSavePrint=false;//是否保存
    var printCode="";
    var printTableCode="";
    $("#ruleReceiptSanLian").find("ul").each(function(index,element){
        console.log("saber"+$(this).data("name"))
        if($(this).attr("class")!="stecs"){
            recordRule=$(this).data("name");
            isSavePrint=true;
        }
    });
    if($("#receiptNameSanLian").val()==""||$("#receiptNameSanLian").val()==undefined){
        $.gritter.add({
            text: "请填写小票名称",
            class_name: 'gritter-success  gritter-light'
        });
        return;
    }
    if(isSavePrint){
        if(recordRule=="SanLian"){
            receiptWith=printParameter.receiptWidthSanLian;
            receiptHight=printParameter.receiptheightSanLian;
            receiptFontSize=printParameter.fontSizeSanLian;
        }
        LODOP=getLodop();
        var str="LODOP.PRINT_INITA(0,0,"+receiptWith+","+receiptHight+",'打印模板');";
        var html="\"<table style='text-align:center;font-size:10px;table-layout:fixed;' border='0' cellspacing='0' cellpadding='0' width='100%' align='center'><thead >"
        $("#printTopSanLian").find("span").each(function (index,element) {
            if(!$(this).is(":hidden")){
                var id=$(this).data("name");
                var message=$(this).text();
                console.log(id);
                console.log(message);
                if(index==0){
                    html+=" <tr><th width='100%' colspan='24' id='"+id+"' style='font-size:15px;padding-top:5px'>香港顺子批发时装批发销售单</th><tr>"
                }else if(index==1){
                    html+=" <th width='100%' colspan='6' id='"+id+"' style='font-size:15px;padding-top:5px'>xx</th>"
                }else{
                    html+=" <th width='100%' colspan='6' id='"+id+"' style='font-size:15px;padding-top:5px'>xx</th>"
                }
                if(printCode==""){
                    if(id!=undefined){
                        printCode+=id;
                    }

                }else{
                    if(id!=undefined){
                        printCode+=","+id;
                    }

                }

            }
        });
        html+="</tr><tr>"
        var tabbleth="";
        var tabblethall="";
        $("#edit-SanLian-dialog").find("th").each(function (index,element) {
            if(!$(this).is(":hidden")){
                var message=$(this).html();
                var classname=$(this).data("name");
                console.log(message);
                if(printParameter.sizeArrySanLian.indexOf(classname)!=-1) {
                    html+="<th align='middle' colspan='1'nowrap='nowrap' style='height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'>"+message+"</th>";
                    tabbleth+="<td align='middle' colspan='1'nowrap='nowrap' style='height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'><font color='black' tdata='SubSum' format='#'>##</font></td>";
                    tabblethall+="<td align='middle' colspan='1'nowrap='nowrap' style='height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'><font color='black' tdata='AllSum' format='#'>##</font></td>";
                }else if(classname=="styleId"){
                    html+="<th align='middle' colspan='3'nowrap='nowrap' style='height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'>"+message+"</th>";
                    tabbleth+="<td align='middle' colspan='3'nowrap='nowrap' style='height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'>本页合计</td>";
                    tabblethall+="<td align='middle' colspan='3'nowrap='nowrap' style='height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'>单据合计</td>";
                }else if(classname=="styleName"){
                    html+="<th align='middle' colspan='3'nowrap='nowrap' style='height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'>"+message+"</th>"
                    tabbleth+="<td align='middle' colspan='3'nowrap='nowrap' style='height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'></td>";
                    tabblethall+="<td align='middle' colspan='3'nowrap='nowrap' style='height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'></td>";
                }else if(classname=="colorId"){
                    html+="<th align='middle' colspan='3'nowrap='nowrap' style='height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'>"+message+"</th>"
                    tabbleth+="<td align='middle' colspan='3'nowrap='nowrap' style='height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'></td>"
                    tabblethall+="<td align='middle' colspan='3'nowrap='nowrap' style='height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'></td>"
                }else if(classname=="qty") {
                    html+="<th align='middle' colspan='2'nowrap='nowrap' style='height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'>"+message+"</th>"
                    tabbleth+="<td align='middle' colspan='2'nowrap='nowrap' style='height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'><font color='black' tdata='SubSum' format='#'>##</font></td>"
                    tabblethall+="<td align='middle' colspan='2'nowrap='nowrap' style='height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'><font color='black' tdata='AllSum' format='#'>##</font></td>"
                }else if(classname=="totPrice"){
                    html+="<th align='middle' colspan='2'nowrap='nowrap' style='height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'>"+message+"</th>"
                    tabbleth+="<td align='middle' colspan='2'nowrap='nowrap' style='height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'><font color='black' tdata='SubSum' format='#.00'>##</font></td>"
                    tabblethall+="<td align='middle' colspan='2'nowrap='nowrap' style='height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'><font color='black' tdata='AllSum' format='#.00'>##</font></td>"
                }else{
                    html+="<th align='middle' colspan='2'nowrap='nowrap' style='height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'>"+message+"</th>"
                    tabbleth+="<td align='middle' colspan='2'nowrap='nowrap' style='height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'></td>"
                    tabblethall+="<td align='middle' colspan='2'nowrap='nowrap' style='height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'></td>"
                }
                if(printTableCode==""){
                    printTableCode+=classname;
                }else{
                    printTableCode+=","+classname;
                }
            }
        });
        html+="</thead ><tbody id='loadtabSanLian'></tbody><tfoot><tr>"+tabbleth+"</tr><tr>"+tabblethall+"</tr><tr>"
        $("#printFootSanLian").find("span").each(function (index,element) {
            if(index==3||index==5||index==7){
                html+="</tr><tr>"
            }
            if(!$(this).is(":hidden")){

                var id=$(this).data("name");
                var message=$(this).text();
                if(index<3){
                    html+="<th colspan='4' id='"+id+"' align='left' style='font-size:15px;padding-top:5px;word-wrap:break-word'>xx</th>"
                }else if(index==3){
                    html+="<th colspan='20' id='"+id+"' align='left' style='font-size:15px;padding-top:5px;word-wrap:break-word'>xx</th>"
                }else if(index==4){
                    html+="<th colspan='4' id='"+id+"' align='left' style='font-size:15px;padding-top:5px;word-wrap:break-word'>xx</th>"
                }else if(index==5){
                    html+="<th colspan='12' id='"+id+"' align='left' style='font-size:15px;padding-top:5px;word-wrap:break-word'>xx</th>"
                }else if(index==6){
                    html+="<th colspan='12' id='"+id+"' align='left' style='font-size:15px;padding-top:5px;word-wrap:break-word'>xx</th>"
                }
                if(printCode==""){
                    printCode+=id;
                }else{
                    printCode+=","+id;
                }
            }

        });
        html+="</tr>"
        debugger
        var writeFootMessage=$("#footExtendWriteSanLian").val();
        var writeFootArray=writeFootMessage.split("<br>");
        for(var i=0;i<writeFootArray.length;i++){
            html+="<tr><th colspan='24'align='left' style='font-size:15px;padding-top:5px;word-wrap:break-word'>"+writeFootArray[i]+"</th></tr>"
        }
        html+="<tr>"
        html+="<th colspan='17' id='printTime' align='middle' style='font-size:15px;padding-top:5px;word-wrap:break-word'>2018-07-16</th>"
        html+="<th colspan='7'  align='middle' style='font-size:15px;padding-top:5px;word-wrap:break-word'>第<font tdata='PageNO' format='0' color='blue'>##</font>页</span>/共<font tdata='PageCount' format='0' color='blue'>##</font></span>页</th>"
        html+="</tr></tfoot></table>\""
        console.log(html)
        printCode+=",printTime";
        var str="LODOP.ADD_PRINT_TABLE(100,1,"+receiptWith+","+receiptHight+","+html+");"
        str+="LODOP.SET_PRINT_STYLEA(0,\"LinkedItem\",\"baseHtml\");";
        str+="LODOP.SET_PRINT_STYLEA(0,\"FontSize\","+receiptFontSize+");";
        /*eval(str);
        LODOP.PREVIEW();*/
        //得到需要保存的数据
        var printSet={
            id:$("#idSanLian").val(),
            ownerId:$("#ownerIdSanLian").val(),
            printCont:str,
            printCode:printCode,
            name:$("#receiptNameSanLian").val(),
            type:$("#receiptTypeSanLian").val(),
            printTableCode:printTableCode,
            printTableTh:html,
            printFootExtend:$("#footExtendWriteSanLian").val().replace(/<br>/g,"\\n"),
            ruleReceipt:recordRule,
            commonType:$("#commonTypeSanLian").val()
        }
        saveAjax(printSet);
    }else{
        $.gritter.add({
            text: "请选择小票的规格",
            class_name: 'gritter-success  gritter-light'
        });
    }

}
function saveA4NoSize() {
    var recordRule="";//记录选择的规格
    var isSavePrint=false;//是否保存
    var printCode="";
    var printTableCode="";
    $("#ruleReceiptA4NoSize").find("ul").each(function(index,element){
        if($(this).attr("class")!="stecs"){
            recordRule=$(this).data("name");
            isSavePrint=true;
        }
    });
    if($("#receiptNameA4NoSize").val()==""||$("#receiptNameSanLian").val()==undefined){
        $.gritter.add({
            text: "请填写小票名称",
            class_name: 'gritter-success  gritter-light'
        });
        return;
    }
    if(isSavePrint){
        if(recordRule=="A4N0Size"){
            receiptWith=printParameter.receiptWidthA4;
            receiptHight=printParameter.receiptheightA4;
            receiptFontSize=printParameter.fontSizeA4;
        }
        LODOP=getLodop();
        var tabbleth="";
        var tabblethall="";
        var tabbleEndFoot=""
        var str="LODOP.PRINT_INITA(0,0,"+receiptWith+","+receiptHight+",'打印模板');";
        var html="\"<table style='text-align:center;font-size:10px;table-layout:fixed;' border='0' cellspacing='0' cellpadding='0' width='100%' align='center'><thead ><tr>"
        $("#head-A4-dialog-NoSize").find("th").each(function (index,element) {
            if(!$(this).is(":hidden")){
                var message=$(this).html();
                var classname=$(this).data("name");
                console.log(classname);
                debugger
                if(classname!=undefined&&classname!=""){
                    tabbleEndFoot+="<tr><th width='100%'align='middle' colspan='15' id='"+classname+"' style='font-size:15px;padding-top:5px'>xx</th></tr>"
                }else{
                    html+="<th align='middle' colspan='3'nowrap='nowrap' style='height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'>"+message+"</th>";
                    tabbleth+="<td align='middle' colspan='3'nowrap='nowrap' style='height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'><font color='black' tdata='SubSum' format='#'>##</font></td>";
                    tabblethall+="<td align='middle' colspan='3'nowrap='nowrap' style='height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;'><font color='black' tdata='AllSum' format='#'>##</font></td>";
                }
            }
        });
        $("#foot-A4-dialog-NoSize").find("th").each(function (index,element) {
            if(!$(this).is(":hidden")){
                var message=$(this).html();
                var classname=$(this).data("name");
                console.log(classname);
                debugger
                if(classname!=undefined&&classname!=""){
                    tabbleEndFoot+="<tr><th width='100%'align='middle' colspan='15' id='"+classname+"' style='font-size:15px;padding-top:5px'>xx</th></tr>"
                }
            }
        });
        html+="</tr>"
        html+="</thead ><tbody id='loadtabNoSize'></tbody><tfoot><tr>"+tabbleth+"</tr><tr>"+tabblethall+"</tr>"+tabbleEndFoot;
        html+="<tr><th colspan='15'  align='middle' style='font-size:15px;padding-top:5px;word-wrap:break-word'>第<font tdata='PageNO' format='0' color='blue'>##</font>页</span>/共<font tdata='PageCount' format='0' color='blue'>##</font></span>页</th></th>"
        html+="</tfoot></table>\"";
        console.log(html);
        var str="LODOP.ADD_PRINT_TABLE(100,1,"+receiptWith+","+receiptHight+","+html+");"
        eval(str);
        LODOP.PREVIEW();
    }
}
