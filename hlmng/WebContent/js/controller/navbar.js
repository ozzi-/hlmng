var eventModule = angular.module('navbar', []);

app.controller('NavBarController', ['$http','$location','RestService','$stateParams', function($http,$location,RestService,$stateParams){	
	var hlmng = this;
	hlmng.eventName={};

	hlmng.checkEventActive = function(){
		return ($stateParams.eventId!==undefined);
    };
	hlmng.getEventActive = function(){
		return ($stateParams.eventId);
    };
 
      
    if(hlmng.checkEventActive()){
    	hlmng.eventName=RestService.get(hlmng.getEventActive(),'event').then(function(data){
    		hlmng.eventName=data.name;
    	});    	
    }

}]);
