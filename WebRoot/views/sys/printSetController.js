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
    $("#footExtend").find("span").text($(t).val());
}