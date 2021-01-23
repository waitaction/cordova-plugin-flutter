var exec = require('cordova/exec');
var Flutter = function () { }

Flutter.prototype.init = function (success, error) {
    exec(success, error, "CordovaFlutter", "init", []);
}

Flutter.prototype.open = function (routerName, success, error) {
    var routerNameParams = [];
    if (routerName) {
      routerNameParams = [routerName];
    }
    exec(success, error, 'CordovaFlutter', 'open', routerNameParams);
};

module.exports = new Flutter();

