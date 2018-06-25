var express = require('express');
var path = require('path')
var fs = require('fs');
var crypto = require('crypto');

var router = express.Router();

/* GET home page. */
router.get('/', function (req, res, next) {
    res.render('index', {title: 'Express'});
});
//获取文件信息
router.get('/update_info', function (req, res, next) {
    var apkVersionCode = req.query.versionCode;
    console.log('current version:' + apkVersionCode);

    var info = {
        'url': '/ota_file/update.zip',
        'updateMessage': 'Fix bugs.',
        'versionName': 'v1.10',
        'versionCode': 1100,
        'md5': '',
        'diffUpdate': true
    };

    if (apkVersionCode >= info.versionCode) {
        info.updateMessage = "已经是最新版本，无需更新"
    } else {
        info.updateMessage = "有新版本，请进行升级"
        var dir = process.cwd() + '/ota_file'
        console.log('process.cwd():' + process.cwd())
        var filePath
        if (info.diffUpdate) {
            filePath = path.join(dir, 'update.patch');
        } else {
            filePath = path.join(dir, 'ota-v1.1.0.apk');
        }
        var md5 = getFileMD5(filePath);
        info.md5 = md5;
    }

    res.writeHead(200, {'Content-Type': 'application/json;charset=utf-8'});
    res.end(JSON.stringify(info));
});

// 文件下载路径
router.get('/ota_file/:filename', function (req, res, next) {
    var filename = req.params.filename;
    var dir = process.cwd() + '/ota_file'
    var filePath = path.join(dir, filename);

    fs.exists(filePath, function (exist) {
        if (exist) {
            console.log('downloading:' + filename);
            res.download(filePath);
        } else {
            res.set('Content-type', 'text/html');
            res.end('File not exist.');
        }
    });
});

function getFileMD5(filePath) {
    var buffer = fs.readFileSync(filePath);
    var fsHash = crypto.createHash('md5');

    fsHash.update(buffer);
    var md5 = fsHash.digest('hex');
    console.log("文件的MD5是：%s", md5);

    return md5;
}

module.exports = router;
