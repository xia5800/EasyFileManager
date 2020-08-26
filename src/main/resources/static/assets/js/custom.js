/** 文件后缀对应图标 */
var fileIcons = [{
    suffix: ['ppt', 'pptx'],
    icon: 'ppt'
}, {
    suffix: ['doc', 'docx'],
    icon: 'doc'
}, {
    suffix: ['xls', 'xlsx'],
    icon: 'xls'
}, {
    suffix: ['pdf'],
    icon: 'pdf'
}, {
    suffix: ['html', 'htm'],
    icon: 'htm'
}, {
    suffix: ['txt'],
    icon: 'txt'
}, {
    suffix: ['swf', 'docx'],
    icon: 'flash'
}, {
    suffix: ['zip', 'rar', '7z'],
    icon: 'zip'
}, {
    suffix: ['mp3', 'wav'],
    icon: 'mp3'
}, {
    suffix: ['mp4', '3gp', 'rmvb', 'avi', 'flv'],
    icon: 'mp4'
}, {
    suffix: ['psd'],
    icon: 'psd'
}, {
    suffix: ['ttf'],
    icon: 'ttf'
}, {
    suffix: ['apk'],
    icon: 'apk'
}, {
    suffix: ['exe'],
    icon: 'exe'
}, {
    suffix: ['torrent'],
    icon: 'bt'
}, {
    suffix: ['gif', 'png', 'jpeg', 'jpg', 'bmp'],
    icon: 'img'
}];


function getFileType(suffix) {
    var type = 'file';
    for (var i = 0; i < fileIcons.length; i++) {
        for (var j = 0; j < fileIcons[i].suffix.length; j++) {
            if (suffix.toLowerCase() == fileIcons[i].suffix[j]) {
                type = fileIcons[i].icon;
                break;
            }
        }
    }
    return type;
}