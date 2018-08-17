/**
 * Created by lly on 2018/8/17.
 */
var payhtml = "";//支付金额页面
var paywayhtml = "";//支付方式页面
var defaultPayType =null;
var payNum = 0;
$(function () {
    $.ajax({
        url: basePath + "/sys/property/searchByTypeWS.do?type=PT",
        dataType: 'json',
        async:false,//同步
        success: function (result) {
            payhtml += "<li>"
                        +"<span id='payPrice'>待收金额</span>"
                        +"<input id='payPriced' type='text' readonly value='0.00'>"
                        +"</li>";
            payhtml += "<li>"
                    +"<span id='actPayPrice'>实收金额</span>"
                    +"<input id='actPayPriced' type='text' readonly value='0.00'>"
                    +"</li>";
            $.each(result,function(index,value){
                paywayhtml  +="<li class="+result[index].iconCode+">"
                            +"<svg class='icon' aria-hidden='true'>"
                            +"<use xlink:href='#icon-"+result[index].iconCode+"'></use>"
                            +"</svg>"
                            +"<span>"+result[index].name+"</span>"
                            +"</li>";
                if(result[index].isDefault == "1"){
                    payhtml += "<li>"
                            +"<span id="+result[index].iconCode+">"+result[index].name+"</span>"
                            +"<input id="+result[index].iconCode+"d"+" type='text'  value='0.00'>"
                            +"</li>";
                    defaultPayType = result[index].iconCode;
                    payNum += 1;
                }
            });

            $(".pay-settlement-input").html(payhtml);
            $(".paywaylist").html(paywayhtml);
            $("."+defaultPayType).addClass("active");//默认变色
            $("#"+defaultPayType+"d").mynumkb();//input框id加d
            //绑定事件
            $(".paywaylist").find("li").each(function () {
                //默认付款方式
                if($(this).hasClass("active")){
                    $(this).css("background","#31c17b");
                }
                //付款方式点击方法
                $(this).bind("click",function () {
                    if($(this).hasClass("active")){
                        $(this).css("background","#fff");
                        $(this).removeClass("active");
                        console.info($(this));
                        payNum -= 1;
                    }
                    else {
                        if(payNum <3){
                            $(this).css("background","#31c17b");
                            $(this).addClass("active");
                            payNum += 1;
                            payhtml += "<li>"
                                    +"<span id='actPayPrice'>+"$(this).i</span>"
                                    +"<input id='actPayPriced' type='text' readonly value='0.00'>"
                                    +"</li>";
                        }
                        else {
                            return;
                        }
                    }
                });
            })
        }
    });


});
