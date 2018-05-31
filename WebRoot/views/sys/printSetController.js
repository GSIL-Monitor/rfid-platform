var printParameter={
    fontSize58mm:9,//58mm小票字体的大小
    fontSize80mm:9,//80mm小票字体的大小
    fontSize110mm:9,//110mm小票字体的大小
    receiptWidth58mm:220,//58mm小票的宽度
    receiptWidth80mm:300,//80mm小票的宽度
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

function test() {
    $("#ruleReceipt").find("ul").each(function(index,element){
        if($(this).attr("class")!="stecs"){

        }

    });

    var sum=0;

    LODOP=getLodop();

    var str="LODOP.PRINT_INITA(0,0,"+printParameter.receiptWidth58mm+","+printParameter.receiptheight58mm+",'打印模板');";
    $("#printTop").find(".col-xs-12").each(function (index,element) {
        if(index==0){
            if(!$(this).find(".col-xs-4").is(":hidden")){
                var id="\""+$(this).find(".col-xs-4").attr("id")+"\"";
                var message="\""+$(this).find(".col-xs-4").find("span").text()+"\"";
                var width=parseInt(printParameter.receiptWidth58mm/3*1);
                str+="LODOP.ADD_PRINT_TEXTA("+id+",0,10,"+width+","+printParameter.aRowheight+","+message+");";
            }
            if(!$(this).find(".col-xs-8").is(":hidden")){
                var id="\""+$(this).find(".col-xs-8").attr("id")+"\"";
                var message="\""+$(this).find(".col-xs-8").find("span").text()+"\"";
                var left=parseInt(printParameter.receiptWidth58mm/3*1+10);
                var width=parseInt(printParameter.receiptWidth58mm/3*2);
                str+="LODOP.ADD_PRINT_TEXTA("+id+",0,"+left+","+width+","+printParameter.aRowheight+","+message+");";
            }
            if(!$(this).find(".col-xs-4").is(":hidden")||!$(this).find(".col-xs-8").is(":hidden")){
                sum++;
            }
        }else{
            if(!$(this).is(":hidden")){
                var id="\""+$(this).attr("id")+"\"";
                var meessage4="\""+$(this).find(".col-xs-4").find("span").text()+"\"";
                var meessage8="\""+$(this).find(".col-xs-8").find("span").text()+"\"";
                if(sum==0){
                    var width=parseInt(printParameter.receiptWidth58mm/3*1);
                    var widthA=parseInt(printParameter.receiptWidth58mm/3*2);
                    var leftA=width+10
                    str+="LODOP.ADD_PRINT_TEXT(0,10,"+width+","+printParameter.aRowheight+","+printParameter.aRowheight+","+meessage4+");";
                    str+="LODOP.ADD_PRINT_TEXTA("+id+",0,"+leftA+","+widthA+","+printParameter.aRowheight+","+meessage8+");";
                }else{
                    var width=parseInt(printParameter.receiptWidth58mm/3*1);
                    var widthA=parseInt(printParameter.receiptWidth58mm/3*2);
                    var leftA=width+10
                    var top=((sum)*printParameter.aRowheight+(sum)*printParameter.intervalHeight);
                    str+="LODOP.ADD_PRINT_TEXT("+top+",10,"+width+","+printParameter.aRowheight+","+meessage4+");";
                    str+="LODOP.ADD_PRINT_TEXTA("+id+","+top+","+leftA+","+widthA+","+printParameter.aRowheight+","+meessage8+");";
                }
                sum++;
            }
        }
    });
    var top=((sum)*printParameter.aRowheight+(sum)*printParameter.intervalHeight);
    var html="\"<body><table style='text-align: center;font-size:12px;'><thead style='text-align:center' border='0' cellspacing='0' cellpadding='0' width='100%' align='center'><tr><th align='left' nowrap='nowrap' style='border:0px;height: 20px;'>商品</th><th align='right' nowrap='nowrap' style='border:0px;height: 20px;'>数量</th><th align='right' nowrap='nowrap' style='border:0px;height: 20px;'>原价</th><th align='right' nowrap='nowrap' style='border:0px;height: 20px;'>折后价</th><th align='right' nowrap='nowrap' style='border:0px;height: 20px;'>金额</th></tr></thead><tbody><tr style='border-top:1px dashed black;padding-top:5px;'><td align='left' style='border-top:1px dashed black;padding-top:5px;'>合计:</td><td align='right' style='border-top:1px dashed black;padding-top:5px;'>0</td><td style='border-top:1px dashed black;padding-top:5px;'>&nbsp;</td><td style='border-top:1px dashed black;padding-top:5px;'>&nbsp;</td><td align='right' style='border-top:1px dashed black;padding-top:5px;'>0</td></tr></tbody></table></body>\"";
    str+="LODOP.ADD_PRINT_HTM("+top+",10,"+printParameter.receiptWidth58mm+","+printParameter.receiptheight58mm+","+html+");";
    str+="LODOP.SET_PRINT_STYLEA(0,\"ItemName\",\"baseHtml\");";
    sum=0;
    $("#printFoot").find(".col-xs-12").each(function (index,element) {
        if(!$(this).is(":hidden")){
            if($(this).attr("id")!="footExtend"){
                var id="\""+$(this).attr("id")+"\"";
                var meessage4="\""+$(this).find(".col-xs-4").find("span").text()+"\"";
                var meessage8="\""+$(this).find(".col-xs-8").find("span").text()+"\"";
                var width=parseInt(printParameter.receiptWidth58mm/3*1);
                var widthA=parseInt(printParameter.receiptWidth58mm/3*2);
                var leftA=width+10
                var top=((sum)*printParameter.aRowheight+(sum+1)*printParameter.intervalHeight);
                str+="LODOP.ADD_PRINT_TEXT("+top+",10,"+width+","+printParameter.aRowheight+","+meessage4+");";
                str+="LODOP.SET_PRINT_STYLEA(0,\"LinkedItem\",\"baseHtml\");";
                str+="LODOP.ADD_PRINT_TEXTA("+id+","+top+","+leftA+","+widthA+","+printParameter.aRowheight+","+meessage8+");";
                str+="LODOP.SET_PRINT_STYLEA(0,\"LinkedItem\",\"baseHtml\");";

            }else{
                var a=$("#footExtendWrite").val();
                console.log(a);
                var messge=$("#footExtendWrite").val().replace(/<br>/,"\\n")
                console.log(messge);
                messge="\""+messge+"\"";
                console.log(messge);
                str+="LODOP.ADD_PRINT_TEXT("+((sum)*printParameter.aRowheight+(sum+1)*printParameter.intervalHeight)+",10,"+parseInt(printParameter.receiptWidth58mm)+","+printParameter.aRowheight+","+messge+");";
                str+="LODOP.SET_PRINT_STYLEA(0,\"LinkedItem\",\"baseHtml\");";
            }
            sum++;
        }
    });



    console.log(str);
    eval(str);



    LODOP.PREVIEW();

}