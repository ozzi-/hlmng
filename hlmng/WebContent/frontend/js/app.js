var app = angular.module('hlmngApp',['stateprovider','speaker','event','voting','social','news','publisher',
                                     'push','navbar','eventroom','qrcode','eventitem','media','angularSpinner',
                                     'helper','directive','slider','ezfb','ui.bootstrap']);

var initInjector = angular.injector(['ng']);
var $http = initInjector.get('$http');


var apiUrl = '../rest/adm/';
var facebookAppId=1589703351304338;

app.service('dataService', function() {
  var _dataObj = 0;
  this.dataObj = _dataObj;
  var _dataObj2 = 0;
  this.dataObj2 = _dataObj2;
});

