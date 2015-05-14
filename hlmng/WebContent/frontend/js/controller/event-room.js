var eventRoomModule = angular.module('eventroom', []);


eventRoomModule.controller('EventRoomNewController', ['$http','RestService','ToolService','$stateParams', function($http,RestService,ToolService,$stateParams){
	var hlmng = this;
	hlmng.eventroom={}; 
	hlmng.postEventRoom = RestService.post;
	hlmng.postAndRedir = function() {  
		hlmng.eventroom.eventIDFK=$stateParams.eventId;
		hlmng.postEventRoom(hlmng.eventroom,'eventroom').then(function(data){
			hlmng.eventroom=data;
			ToolService.redir('event.active.eventroom.id',{eventId: $stateParams.eventId, eventRoomId: hlmng.eventroom.eventRoomID});
		});
	};
}]);

eventRoomModule.controller('EventRoomListController', ['$http','RestService','$stateParams', function($http,RestService,$stateParams){
	var hlmng = this;
	hlmng.eventrooms = [];
	
	RestService.list('eventroom').then(function(data){
	    $.each(data, function(i, item){
	    	if(item.eventIDFK==$stateParams.eventId){
	    		hlmng.eventrooms.push(item);	  
	    	}
	    });
	});
}]);



eventRoomModule.controller('EventRoomIdController', ['$http','$stateParams','RestService','ToolService', function($http, $stateParams,RestService,ToolService){
	var hlmng = this;
	
	RestService.get($stateParams.eventRoomId,'eventroom').then(function(data){
		hlmng.eventroom=data;
	});
	hlmng.redirToList = function() { 
		ToolService.redir('event.active.eventroom.list',{eventId: $stateParams.eventId});
	};
	hlmng.deleteEventRoom = RestService.del;
	hlmng.putEventRoom = RestService.put;
}]);