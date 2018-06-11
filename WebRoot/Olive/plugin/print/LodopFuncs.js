var CreatedOKLodop7766 = null;

function getLodop(oOBJECT, oEMBED) {
    /***************************************************************************
     * 本函数根据浏览器类型决定采用哪个页面元素作为Lodop对象： IE系列、IE内核系列的浏览器采用oOBJECT，
     * 其它浏览器(Firefox系列、Chrome系列、Opera系列、Safari系列等)采用oEMBED,
     * 如果页面没有相关对象元素，则新建一个或使用上次那个,避免重复生成。 64位浏览器指向64位的安装程序install_lodop64.exe。
     **************************************************************************/
    /*var webContext = window.base || "/is";

    var lodopUrl32 = webContext
        + '/template/nadmin/js/libs/lodop/install_lodop32.exe';
    var lodopUrl64 = webContext
        + '/template/nadmin/js/libs/lodop/install_lodop64.exe';
*/
    var lodopUrl32 = basePath
        + '/Olive/plugin/print/install_lodop32.exe';
    var lodopUrl64 = basePath
        + '/Olive/plugin/print/install_lodop64.exe';
    var strHtmInstall = "<div autofocus>打印控件未安装!点击这里<a style='text-decoration: underline;color:rgb(0, 0, 238);' href='" + lodopUrl32 + "' target='_self'>执行安装</a>,安装后请刷新页面或重新进入。</div>";
    var strHtmUpdate = "<div autofocus>打印控件需要升级!点击这里<a href='" + lodopUrl32 + "' target='_self'>执行升级</a>,升级后请重新进入。</div>";
    var strHtm64_Install = "<div autofocus >打印控件未安装!点击这里<a href='" + lodopUrl64 + "' target='_self'>执行安装</a>,安装后请刷新页面或重新进入。</div>";
    var strHtm64_Update = "<div autofocus >打印控件需要升级!点击这里<a href='" + lodopUrl64 + "' target='_self'>执行升级</a>,升级后请重新进入。</div>";
    var strHtmFireFox = "<div>（注意：如曾安装过Lodop旧版附件npActiveXPLugin,请在【工具】->【附加组件】->【扩展】中先卸它）</div>";
    var strHtmChrome = "<div>（新版本谷歌浏览器不支持打印控件，请使用360浏览器；如果此前正常，仅因浏览器升级或重安装而出问题，需重新执行以上安装）</div>";
    var LODOP;
    try {
        // =====判断浏览器类型:===============
        var isIE = (navigator.userAgent.indexOf('MSIE') >= 0)
            || (navigator.userAgent.indexOf('Trident') >= 0);
        var is64IE = isIE && (navigator.userAgent.indexOf('x64') >= 0);
        // =====如果页面有Lodop就直接使用，没有则新建:==========
        if (oOBJECT != undefined || oEMBED != undefined) {
            if (isIE)
                LODOP = oOBJECT;
            else
                LODOP = oEMBED;
        } else {
            if (CreatedOKLodop7766 == null) {
                LODOP = document.createElement("object");
                LODOP.setAttribute("width", 0);
                LODOP.setAttribute("height", 0);
                LODOP
                    .setAttribute("style",
                        "position:absolute;left:0px;top:-100px;width:0px;height:0px;");
                if (isIE)
                    LODOP.setAttribute("classid",
                        "clsid:2105C259-1E0C-4534-8141-A753534CB4CA");
                else
                    LODOP.setAttribute("type", "application/x-print-lodop");
                document.documentElement.appendChild(LODOP);
                CreatedOKLodop7766 = LODOP;
            } else
                LODOP = CreatedOKLodop7766;
        }
        if(document.getElementById('printPluginHint') == null){
            var dialog = '<div id="printPluginHint" style="margin-bottom:30px;margin-top:-20px;"></div>';
            $(dialog).appendTo('body');
            $('#printPluginHint').dialog({
                title:'提示',
                autoOpen:false,
                show: {
                    effect: "blind",
                    duration: 10
                },
                position:{my:'0 0'},
                width:500,
            });
        }
        // =====判断Lodop插件是否安装过，没有安装或版本过低就提示下载安装:==========
        if ((LODOP == null) || (typeof (LODOP.VERSION) == "undefined")) {
            var str = "";
            if (is64IE)
                str += strHtm64_Install;
            else if (isIE)
                str += strHtmInstall;
            else
                str += strHtmInstall;

            if (navigator.userAgent.indexOf('Chrome') >= 0)
                str += strHtmChrome;
            if (navigator.userAgent.indexOf('Firefox') >= 0)
                str += strHtmFireFox;
            $('#printPluginHint').html(str).dialog('open');
            return LODOP;
        } else if (LODOP.VERSION < "6.1.9.8") {
            var str = "";
            if (is64IE)
                str = strHtm64_Update;
            else if (isIE)
                str = strHtmUpdate;
            else
                str = strHtmUpdate;
            return LODOP;
        }
        // =====如下空白位置适合调用统一功能(如注册码、语言选择等):====
        LODOP.SET_LICENSES("", "983941029810310552102114114561",
            "688858710010010811411756128900", "");

        // ============================================================
        return LODOP;
    } catch (err) {
        if (is64IE){
            $('#printPluginHint').html("Error:" + strHtm64_Install);
            $('#printPluginHint').dialog("open");
        }else{
            $('#printPluginHint').html("Error:" + strHtmInstall);
            $('#printPluginHint').dialog("open");
        }
        return LODOP;
    }
}
