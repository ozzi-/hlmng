var app = angular.module('hlmngSliderApp',['stateprovider','ui.bootstrap','ui.router']);
var apiUrl = 'https://localhost:8443/hlmng/rest/adm/';


var myapp = angular.module('stateprovider', ["ui.router"]); 
myapp.config(function($stateProvider, $urlRouterProvider){
	$urlRouterProvider.otherwise("/");
	$stateProvider.
    state('event', {
		url: "/event/{eventId:int}/interval/{interval:int}",
		templateUrl: "template/slider/slider.html",
       	controller: "SliderController",
       	controllerAs: "sliderCtrl"
    }).
    state('index', {
		url: "/",
		template: "Use /event/{id}/interval/{ms}"
    });
});


app.controller('SliderController', ['$http','$stateParams','RestService','$scope',function($http,$stateParams,RestService,$scope){
	$scope.socialsAccepted = [];
	$scope.interval = $stateParams.interval;
	RestService.list('social').then(function(data){
	    $.each(data, function(i, item){
	    	if(item.eventIDFK==$stateParams.eventId){
	    		if(item.status=="accepted"){
	    			$scope.socialsAccepted.push(item);	  	    			
	    		}
	    	}
	    });
	});
}]);