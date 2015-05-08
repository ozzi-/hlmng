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


app.controller('SliderController', ['$http','$stateParams','RestService','$scope','$interval',function($http,$stateParams,RestService,$scope,$interval){
	
	var refreshData = function() {
		RestService.list('social').then(function(data){
		    $.each(data, function(i, item){
		    	if(item.eventIDFK==$stateParams.eventId){
		    		if(item.status=="accepted"){
		    			var there=false;
		    			for(var i=0; i<$scope.socialsAccepted.length; i++) {
	    			        if ($scope.socialsAccepted[i].media == item.media){
	    			        	there=true;
	    			        }
	    			    }
		    			if(there==false){
		    				$scope.socialsAccepted.push(item);	 
		    			}
		    		}
		    	}
		    });
		});
	};

	var promise = $interval(refreshData, 5000);

	// Cancel interval on page changes
	$scope.$on('$destroy', function(){
	    if (angular.isDefined(promise)) {
	        $interval.cancel(promise);
	        promise = undefined;
	    }
	});

	
	
	$scope.socialsAccepted = [];
	$scope.intervalSlide = $stateParams.interval;
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