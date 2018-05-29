var printParameter={
    fontSize58mm:9,//58mm小票字体的大小
    fontSize80mm:9,//80mm小票字体的大小
    fontSize110mm:9,//110mm小票字体的大小
    

};


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