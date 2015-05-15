var app = angular.module('hlmngSocialSliderApp',['stateprovider','ui.bootstrap','ui.router']);
var apiUrl = '../rest/adm/';

var myapp = angular.module('stateprovider', ["ui.router"]); 
myapp.config(function($stateProvider, $urlRouterProvider){
	$urlRouterProvider.otherwise("/");
	$stateProvider.
    state('event', {
		url: "/event/{eventId:int}/interval/{interval:int}/updateinterval/{updateinterval:int}",
		templateUrl: "template/socialslider/socialslider.html",
       	controller: "SocialSliderController",
       	controllerAs: "sliderCtrl"
    }).
    state('index', {
		url: "/",
		template: "Use /event/{id}/interval/{ms}/updateinterval/{ms}"
    });
});


app.controller('SocialSliderController', ['$http','$log','$stateParams','RestService','$scope','$interval',function($http,$log,$stateParams,RestService,$scope,$interval){
	
	var refreshData = function() {
		
		RestService.list('social').then(function(data){
		    $.each(data, function(i, item){
		    	if(item.eventIDFK==$stateParams.eventId){
		    		var there=false;
		    		var thereIndex=0;
	    			for(var i=0; i<$scope.socialsAccepted.length; i++) {
    			        if ($scope.socialsAccepted[i].socialID == item.socialID){
    			        	there=true;
    			        	thereIndex=i;
    			        }
    			    }
	    			if(there==false && item.status=="accepted"){
    					$scope.socialsAccepted.push(item);	
	    			}
	    			if(there==true && item.status!="accepted"){
    					$scope.socialsAccepted.splice(thereIndex,1);	 
	    			}
		    	}
		    });
		});
	};

	var promise = $interval(refreshData, $stateParams.updateinterval);
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