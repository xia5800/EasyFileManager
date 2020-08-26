layui.extend({
    webuploader: 'uploader_lay/webuploader/webuploader'
}).define(['layer','laytpl','table','element','webuploader'],function(exports){
    var $ = layui.$
        ,webUploader = layui.webuploader
        ,element = layui.element
        ,layer = layui.layer
        ,table = layui.table
        ,rowData = []                               //保存上传文件属性集合,添加table用
        ,uplaod
        ,folderId = '1'                             // 默认当前所在文件夹id
        ,path = ''                                  // 默认当前路径。这个路径的意思是当前所在的文件夹
        ,fileSize = 100 *1024*1024                  // 默认上传文件大小 100M，单位字节
        ,chunkSize = 5 *1024*1024                   // 默认文件片段大小 5M，单位字节
        ,uploadUrl = '/'                            // 默认文件上传地址
        ,fileNumLimit = 10;                         // 默认最大上传文件个数
    //加载样式
    layui.link(layui.cache.base +'uploader_lay/webuploader/webuploader.css');

    var Class = function(options) {
        var that = this;
        that.options = options;

        that.configuration();
        that.register();
        that.init();
        that.events();
    };

    Class.prototype.configuration = function() {
        var that = this,
            options = that.options;
        // 当前所在文件夹id
        folderId = options.folderId ? options.folderId : folderId;

        // 当前所在文件夹
        path = options.path ? options.path : path;

        // 文件上传url
        uploadUrl = options.url ? options.url : uploadUrl;

        // 上传文件大小 单位M
        fileSize = options.fileMaxSize ? options.fileMaxSize * 1024*1024 : fileSize;

        // 文件分片大小 单位M
        chunkSize = options.chunkSize ? options.chunkSize * 1024*1024 : chunkSize;

        // 最大上传文件个数
        fileNumLimit = options.fileNumLimit ? options.fileNumLimit : fileNumLimit;
    }

    Class.prototype.register = function() {
        var that = this,
            fileCheckUrl = uploadUrl + '/beforeSendFile',
            checkChunkUrl = uploadUrl + '/beforeSend',
            mergeChunksUrl = uploadUrl + '/afterSendFile/';

        //监控文件上传的三个时间点(注意：该段代码必须放在WebUploader.create之前)
        //时间点1：:所有分块进行上传之前（1.可以计算文件的唯一标记;2.可以判断是否秒传）
        //时间点2： 如果分块上传，每个分块上传之前（1.询问后台该分块是否已经保存成功，用于断点续传）
        //时间点3：所有分块上传成功之后（1.通知后台进行分块文件的合并工作）
        webUploader.Uploader.register({
            "before-send-file":"beforeSendFile",
            "before-send":"beforeSend",
            "after-send-file":"afterSendFile"
        },{
            //时间点1：所有分块进行上传之前调用此函数
            beforeSendFile: function(file){
                //创建一个deferred
                var deferred = webUploader.Deferred();
                var start = 0;
                var end = chunkSize * 2;
                //1.计算文件的唯一标记，用于断点续传和秒传,获取文件前10m的md5值，越小越快，防止碰撞，把文件名文件大小和md5拼接作为文件唯一标识
                (new webUploader.Uploader()).md5File(file, start, end).progress(function(percentage){
                    //获取操作栏,修改其状态
                    var v = that.getTableHead('validateMd5');
                    var table = $("#extend-uploader-form").next().find('div[class="layui-table-body layui-table-main"]').find('table');
                    var pro = table.find('td[data-field="progress"]');
                    for(var i=0; i<pro.length; i++){
                        var d = $(pro[i]).attr('data-content');
                        if(d === file.id){
                            var t = $(pro[i]).prev();
                            t.empty();
                            t.append('<div class="'+v+'">'+(percentage * 100).toFixed(0)+'%</div>');
                        }
                    }
                }).then(function(val){
                    file.fileMd5 = val;
                    //2.请求后台是否保存过该文件，如果存在，则跳过该文件，实现秒传功能
                    $.ajax({
                        type: "POST",
                        url: fileCheckUrl,
                        data: { fileMd5: val },
                        dataType: "json",
                        success: function(rsp){
                            if(rsp.code === 200) {
                                deferred.reject();   // 跳过
                                uplaod.skipFile(file);
                                that.setTableBtn(file.id,"秒传");
                                element.progress(file.id,'100%');
                            }else{
                                deferred.resolve();
                            }
                        },
                        complete: function(XMLHttpRequest,status){
                            if(status === 'timeout'){
                                // 超时,status还有success,error等值的情况
                                console.log("beforeSendFile: md5校验超时");
                                layer.msg("md5校验超时",{icon: 2, anim: 6, time: 1500});
                                deferred.resolve();
                            }
                        },
                        error: function (xhr, textStatus, errorThrown) {
                            if(xhr.status === 404){
                                console.log("beforeSendFile: 文件校验url不正确，请检查");
                                layer.msg("文件校验url不正确，请检查",{icon: 2, anim: 6, time: 1500});
                                $("#extent-button-uploader").html('<i class="layui-icon layui-icon-upload-drag"></i>开始上传');
                                $("#extent-button-uploader").removeClass('layui-btn-disabled');
                            }
                        }
                    });
                });
                //返回deferred
                return deferred.promise();
            },
            //时间点2：如果有分块上传，则每个分块上传之前调用此函数
            beforeSend: function(block){
                //block:代表当前分块对象
                //向后台发送当前文件的唯一标记，用于后台创建保存分块文件的目录
                //1.请求后台是否保存过当前分块，如果存在，则跳过该分块文件，实现断点续传功能
                var deferred = webUploader.Deferred();
                //请求后台是否保存完成该文件信息，如果保存过，则跳过，如果没有，则发送该分块内容
                (new webUploader.Uploader()).md5File(block.file, block.start, block.end).progress(function(percentage){
                }).then(function(val){
                    block.zoneMd5 = val;
                    $.ajax({
                        type: "POST",
                        url: checkChunkUrl,
                        data:{
                            fileMd5: block.file.fileMd5,
                            zoneMd5: block.zoneMd5
                        },
                        dataType:"json",
                        success:function(rsp){
                            if(rsp.code === 200){
                                // 分块存在，跳过该分块校验
                                deferred.reject();
                            }else{
                                console.log("beforeSend: 分块验证不通过(服务器上不存在该分块)，进行上传分块操作");
                                // 分块验证不通过(服务器上不存在该分块)，进行上传分块操作
                                deferred.resolve();
                            }
                        },
                        complete : function(XMLHttpRequest,status){
                            if(status === 'timeout'){
                                // 超时,status还有success,error等值的情况
                                console.log("beforeSend: 文件块校验超时");
                                deferred.resolve();
                            }
                        },
                        error: function (xhr, textStatus, errorThrown) {
                            if(xhr.status === 404){
                                console.log("beforeSend: 文件块校验url不正确，请检查");
                                layer.msg("文件块校验url不正确，请检查",{icon: 2, anim: 6, time: 1500});
                                $("#extent-button-uploader").html('<i class="layui-icon layui-icon-upload-drag"></i>开始上传');
                                $("#extent-button-uploader").removeClass('layui-btn-disabled');
                            }
                        }
                    });
                });
                return deferred.promise();
            },
            //时间点3：所有分块上传成功之后调用此函数
            afterSendFile: function(file){
                //前台通知后台合并文件
                //1.如果分块上传，则通过后台合并所有分块文件
                //请求后台合并文件
                $.ajax({
                    type: "POST",
                    url: mergeChunksUrl + file.fileMd5,
                    dataType: "JSON",
                    success:function(rsp){
                        if(rsp.code === 200){
                            uplaod.skipFile(file);
                            that.setTableBtn(file.id,'上传成功');
                            element.progress(file.id,'100%');
                        }else{
                            console.log(rsp.msg+",错误分片："+rsp.data);
                        }
                    },
                    complete : function(XMLHttpRequest,status){ //请求完成后最终执行参数
                        if(status === 'timeout'){
                            //超时,status还有success,error等值的情况
                            console.log("afterSendFile: 合并文件超时");
                        }
                    },
                    error: function (xhr, textStatus, errorThrown) {
                        if(xhr.status === 404){
                            console.log("afterSendFile: 文件合并url不正确，请检查");
                            layer.msg("文件合并url不正确，请检查",{icon: 2, anim: 6, time: 1500});
                            $("#extent-button-uploader").html('<i class="layui-icon layui-icon-upload-drag"></i>开始上传');
                            $("#extent-button-uploader").removeClass('layui-btn-disabled');
                        }
                    }
                });
            }
        });
    };

    Class.prototype.init = function() {
        var that = this,
            options = that.options,
            title = '上传文件';
            btnName = '选择文件';
        // 如果是上传文件夹
        if(options.webkitdirectory){
            title = '上传文件夹';
            btnName = '选择文件夹';
        }
        var refreshTb = options.refresh;
        layer.open({
            type: 1,
            area: ['850px', '500px'], //宽高
            resize: false,
            title: title,
            content:
            '<div id="extend-upload-chooseFile" class="layui-btn icon-btn layui-bg-blue" style="float: left;margin-left: 5px;margin-top: 10px;"><i class="layui-icon layui-icon-list"></i>'+btnName+'</div>'+
            '<button id="extent-button-uploader" class="layui-btn icon-btn" style="height: 37px;margin-top: 10px;"><i class="layui-icon layui-icon-upload-drag"></i>开始上传</button>'+
            '<table style="margin-top:-10px;" lay-skin="line" class="layui-table" id="extend-uploader-form" lay-filter="extend-uploader-form">' +
            '  <thead>' +
            '    <tr>' +
            '      <th lay-data="{type:\'numbers\', fixed:\'left\'}"></th>' +
            '      <th lay-data="{field:\'fileName\', width:250, unresize: true}">文件名称</th>' +
            '      <th lay-data="{field:\'fileSize\', width:100, unresize: true}">文件大小</th>' +
            '      <th lay-data="{field:\'validateMd5\', width:120, unresize: true}">文件验证</th>' +
            '      <th lay-data="{field:\'progress\', templet:\'#button-form-optProcess\', unresize: true}">进度</th>' +
            '      <th lay-data="{field:\'oper\', width: 100,templet: \'#button-form-uploadTalbe\', unresize: true}">操作</th>' +
            '    </tr>' +
            '  </thead>'+
            '</table>'+
            '<script type="text/html" id="button-form-uploadTalbe">'+
                '<a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>'+
            '</script>'+
            '<script type="text/html" id="button-form-optProcess">' +
                '<div style="margin-top: 5px;" class="layui-progress layui-progress-big" lay-filter="{{d.fileId}}"  lay-showPercent="true">'+
                  '<div class="layui-progress-bar layui-bg-blue" lay-percent="0%"></div>'+
                '</div>'+
            '</script>'
            ,
            success: function(layero, index){
                table.init('extend-uploader-form',{
                    height: 380,
                    limit: fileNumLimit // 不写limit, 最大显示10个文件
                });
                var conf = {
                    resize: false,                       // 不压缩image
                    server: uploadUrl,                   // 默认文件接收服务端。
                    pick: {
                        id: '#extend-upload-chooseFile', // 指定选择文件的按钮容器，不指定则不创建按钮。
                        multiple: true                   // 开启文件多选
                    },
                    accept: null,                        // 接收文件类型，不限制接收类型
                    compress: null,                      // 不压缩图片，压缩之后获取不到图片相对路径
                    fileNumLimit: fileNumLimit,          // 验证文件总数量, 超出则不允许加入队列,默认值：undefined,如果不配置，则不限制数量
                    fileSizeLimit: 100*1024*1024*1024,   // 验证文件总大小是否超出限制, 超出则不允许加入队列。单位字节
                    fileSingleSizeLimit: fileSize,       // 验证单个文件大小是否超出限制, 超出则不允许加入队列。单位字节
                    chunked: true,                       // 是否开启分片上传
                    threads: 1,                          // 上传并发数。允许同时最大上传进程数。
                    chunkSize: chunkSize,                // 如果要分片，每一片的文件大小
                    prepareNextFile: false               // 是否允许在文件传输时提前把下一个文件准备好,请设置成false，不然开启文件多选你浏览器会卡死
                }
                uplaod = webUploader.create(conf);
            },
            cancel: function(index, layero){
                rowData=[];
                if(refreshTb) refreshTb.reload();
                layer.close(index);
            },
            end: function () {   //可以自行添加按钮关闭,关闭请清空rowData
                rowData=[];
                if(options.success){
                    if(typeof options.success==='function') {
                        options.success();
                    }
                }
            }
        });
    };

    Class.prototype.events = function() {
        var that = this,
            options = that.options;

        // 当文件被加入队列以后触发
        uplaod.on('fileQueued', function(file){
            var fileSize = that.formatFileSize(file.size);
            var row = {
                fileId : file.id,
                fileName : file.name,
                fileSize : fileSize,
                validateMd5 : '0%',
                progress : file.id,
                state : '就绪'
            };
            rowData.push(row);
            that.reloadData(rowData);
            element.render('progress');
        });

        //监听进度条,更新进度条信息
        uplaod.on('uploadProgress', function(file, percentage) {
            element.progress(file.id, (percentage * 100).toFixed(0)+'%');
        });

        //上传之前
        uplaod.on('uploadBeforeSend', function(block, data, headers) {
            if(path !== '' && !path.endsWith("/")){
                path = path + '/'
            }
            // 分片前文件的md5值
            data.fileMd5 = block.file.fileMd5;
            // 分片md5值
            data.zoneMd5 = block.zoneMd5;
            // 分片总数
            data.zoneTotalCount = block.chunks;
            // 当前分片索引
            data.zoneIndex = block.chunk;
            // 文件大小
            data.fileSize = block.total;
            // 分片开始位置
            data.zoneStartSize = block.start;
            // 分片结束位置
            data.zoneEndSize = block.end;
            // 当前所在目录的id
            data.folderId = folderId;
            // 当前所在目录
            data.path = path;
            // 文件相对路径
            data.relativePath = block.file.source.source.webkitRelativePath ? block.file.source.source.webkitRelativePath : '';

        });

        //错误信息监听
        uplaod.on('error', function(handler){
            if(handler === 'F_EXCEED_SIZE'){
                console.log("上传的单个太大!");
                layer.msg('上传的单个文件太大!<br>最大支持'+that.formatFileSize(fileSize)+' <br>操作无法进行,如有需求请联系管理员', {icon: 5, time: 5000});
            }else if(handler === 'Q_TYPE_DENIED'){
                console.log("不允许上传此类文件!");
                layer.msg('不允许上传此类文件!<br>操作无法进行,如有需求请联系管理员', {icon: 5, time: 5000});
            }
        });

        //移除上传的文件
        table.on('tool(extend-uploader-form)', function(obj){
            var data = obj.data;
            if(obj.event === 'del'){
                that.removeArray(rowData, data.fileId);
                uplaod.removeFile(data.fileId, true);
                obj.del();
            }else if(obj.event === 'stop'){ //暂停、继续
                uplaod.stop(true);
            }
        });

        //选择文件
        $("#extend-upload-chooseFile").click(function () {
            //上传文件夹
            if(options.webkitdirectory) {
                $(".webuploader-element-invisible").attr('webkitdirectory','');
            }
            // 清空表格数据
            rowData.forEach(function(item){
                uplaod.removeFile(item.fileId, true);
            });
            rowData = [];
        });

        //开始上传
        $("#extent-button-uploader").click(function () {
            that.uploadToServer();
        });

        //单个文件上传成功
        uplaod.on('uploadSuccess', function( file ) {
            that.setTableBtn(file.id,'完成');
        });
        //单个文件上传失败
        uplaod.on('uploadError', function( file ) {
            that.setTableBtn(file.id,'失败');
        });

        //所有文件上传成功后
        uplaod.on('uploadFinished', function(){
            $("#extent-button-uploader").html('<i class="layui-icon layui-icon-upload-drag"></i>开始上传');
            $("#extent-button-uploader").removeClass('layui-btn-disabled');
        });

    };

    Class.prototype.formatFileSize = function(value) {
        if(null == value || value == ''){
            return "0 B";
        }
        var unitArr = ["B","KB","MB","GB","TB","PB","EB","ZB","YB"];
        var index = 0;
        var srcsize = parseFloat(value);
        index = Math.floor(Math.log(srcsize) / Math.log(1024));
        var size = srcsize / Math.pow(1024, index);
        size = size.toFixed(2);//保留的小数位数
        return size + unitArr[index];
    };

    Class.prototype.buildFileType = function (type) {
        var ts = type.split(',');
        var ty = '';

        for(var i=0;i<ts.length;i++){
            ty =  ty+"."+ts[i]+",";
        }
        return ty.substring(0, ty.length - 1)
    };

    Class.prototype.strIsNull = function(str) {
        if(typeof str == "undefined" || str == null || str == "") {
            return true;
        }
        else {
            return false;
        }
    };

    Class.prototype.reloadData = function(data){
        layui.table.reload('extend-uploader-form',{
            data : data
        });
    };

    /***
     * 注意更改了table列的位置,或自行新增了表格,请自行在这修改
     */
    Class.prototype.getTableHead = function(field) {
        //获取table头的单元格class,保证动态设置table内容后单元格不变形
        var div = $("#extend-uploader-form").next().find('div[class="layui-table-header"]');
        var div2 = div[0];
        var table = $(div2).find('table');
        var td = table.find('th[data-field="'+field+'"]').find('div').attr('class');
        return td;
    };

    /**
     * 修改表格按钮名
     * @param fileId
     * @param val
     */
    Class.prototype.setTableBtn = function(fileId, val) {
        var td = this.getTableHead('oper');
        //获取操作栏,修改其状态
        var table = $("#extend-uploader-form").next().find('div[class="layui-table-body layui-table-main"]').find('table');
        var pro = table.find('td[data-field="progress"]');
        for(var i=0; i<pro.length; i++){
            var d = $(pro[i]).attr('data-content');
            if(d === fileId){
                var t = $(pro[i]).next();
                t.empty();
                t.append('<div class="'+td+'"><a class="layui-btn layui-btn-normal layui-btn-xs" lay-event="ok">'+val+'</a></div>')
            }
        }
    };

    Class.prototype.uploadToServer = function() {
        if(rowData.length <= 0){
            layer.msg('没有要上传的文件', {icon: 5});
            return;
        }
        $("#extent-button-uploader").text("正在上传");
        $("#extent-button-uploader").addClass('layui-btn-disabled');
        uplaod.upload();
    };

    Class.prototype.removeArray = function(array, fileId) {
        for(var i=0; i<array.length; i++){
            if(array[i].fileId === fileId){
                array.splice(i,1);
            }
        }
        return array;
    };

    var layUploader = {
        render: function (options) {
            var inst = new Class(options);
            return inst;
        }

    };

    exports('layUploader', layUploader);
});