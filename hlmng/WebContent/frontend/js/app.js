var app = angular.module('hlmngApp',['stateprovider','speaker','event','voting','social','news',
                                     'push','navbar','eventroom','qrcode','eventitem','media',
                                     'helper','directive','slider','ui.bootstrap']);


var apiUrl = '../rest/adm/';

app.service('dataService', function() {
  var _dataObj = 0;
  this.dataObj = _dataObj;
});

app.directive('errSrc', function() {
  return {
    link: function(scope, element, attrs) {
      element.bind('error', function() {
        if (attrs.src != attrs.errSrc) {
          attrs.$set('src', attrs.errSrc);
        }
      });
    }
  };
});