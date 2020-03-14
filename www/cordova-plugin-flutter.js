var exec = require('cordova/exec');
var Flutter = function () { }

Flutter.prototype.init = function (success, error) {
    exec(success, error, "CordovaFlutter", "init", []);
}

Flutter.prototype.open = function (success, error) {
    exec(success, error, "CordovaFlutter", "open", []);
}

module.exports = new Flutter();

