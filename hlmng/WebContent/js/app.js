var app = angular.module('hlmngApp',['stateprovider','speaker','event','navbar','eventroom','qrcode','eventitem','media','helper','directive','ui.bootstrap']);
var apiUrl = 'https://localhost:8443/hlmng/rest/adm/';


app.service('dataService', function() {
  var _dataObj = {};
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