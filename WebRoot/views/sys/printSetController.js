$(function () {
    selectheadPrint();
});
function selectheadPrint() {
    $("#footerPrint").hide();
    $("#headPrint").show();
}
function selectfoorerPrint() {
    $("#footerPrint").show();
    $("#headPrint").hide();
}
function a(sum) {
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