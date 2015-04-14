var app = angular.module('hlmngApp',['ngRoute','speaker','event']);
var apiUrl = 'https://localhost:8443/hlmng/rest/';

app.controller('IndexController', ['$http', function($http){
	$http.get(apiUrl+'backendlogin').success(function(data){
		if(data==='true'){
			// LOGGED IN 
		}else{
			// NOT LOGGED IN, WHAT DO 
			// TODO for all pages
		}
	});
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
	  }
});