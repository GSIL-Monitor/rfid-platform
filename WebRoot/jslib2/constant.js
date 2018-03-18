/**
 * Created by Wing li on 2014/6/5.
 */

var constant={
    unitType:{
        Vender : 0,
Headquarters : 1,
Agent : 2,
Factory : 3,
Shop : 4,
NetShop : 5,
SampleRoom : 6,//
Department : 7,//
Warehouse : 9//
    },
    requestUrl : {
    }
};


var cs = $.extend({}, cs);/* 定义全局对象，类似于命名空间或包的作用 */
cs.isBlank = function(v) {
    return v==undefined || v==null  || v=="" || v==" " || v.length == 0;
};
var csProgerssDialog;
cs.showAlertMsgBox = function(msg) {
    bootbox.alert(msg);
};
cs.showProgressBar = function(msg) {
    var message = msg?msg:"服务器请求中...";
    csProgerssDialog = bootbox.dialog({
        message: '<p><i class="fa fa-spin fa-spinner"></i>'+message+'</p>'
    });
};
cs.closeProgressBar = function() {
    csProgerssDialog.modal('hide');
};
cs.showConfirmMsgBox = function(msg,callbackFunc) {
    bootbox.confirm({
        buttons: {
            confirm: {
                label: '确认',
                className: 'btn-success'
            },
            cancel: {
                label: '取消',
                className: 'btn-danger'
            }
        },
        message: msg,
        callback: callbackFunc
        //title: "bootbox confirm也可以添加标题哦",
    });
};