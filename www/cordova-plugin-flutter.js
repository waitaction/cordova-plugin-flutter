var exec = require('cordova/exec');
var Flutter = function () { }

Flutter.prototype.init = function (success, error) {
    exec(success, error, "CordovaFlutter", "init", []);
}

Flutter.prototype.openPage = function (success, error) {
    exec(success, error, "CordovaFlutter", "openPage", []);
}

module.exports = new Flutter();

