/** EasyWeb iframe v3.1.8 date:2020-05-04 License By http://easyweb.vip */
layui.config({
    version: '318',
    base: getProjectUrl() + 'assets/module/'
}).extend({
    dropdown: 'dropdown/dropdown',
    layUploader: 'uploader_lay/layUploader'
}).use(['layer'], function () {
    var $ = layui.jquery;
    var layer = layui.layer;

});

/** 获取当前项目的根路径，通过获取layui.js全路径截取assets之前的地址 */
function getProjectUrl() {
    var layuiDir = layui.cache.dir;
    if (!layuiDir) {
        var js = document.scripts, last = js.length - 1, src;
        for (var i = last; i > 0; i--) {
            if (js[i].readyState === 'interactive') {
                src = js[i].src;
                break;
            }
        }
        var jsPath = src || js[last].src;
        layuiDir = jsPath.substring(0, jsPath.lastIndexOf('/') + 1);
    }
    return layuiDir.substring(0, layuiDir.indexOf('assets'));
}
