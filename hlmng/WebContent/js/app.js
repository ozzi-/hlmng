var app = angular.module('hlmngApp',['ui.router','speaker','event','eventroom','media','helper','directive','ui.bootstrap']);
var apiUrl = 'https://localhost:8443/hlmng/rest/';

app.controller('NavBarController', ['$http','$location','RestService','$stateParams', function($http,$location,RestService,$stateParams){	
	var hlmng = this;
	hlmng.checkEventActive = function(){
		return ($stateParams.eventId!==undefined);
    };
	hlmng.getEventActive = function(){
		return ($stateParams.eventId);
    };


}]);



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