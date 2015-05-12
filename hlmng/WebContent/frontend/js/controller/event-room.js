var eventRoomModule = angular.module('eventroom', []);


eventRoomModule.controller('EventRoomNewController', ['$http','RestService','ToolService','$stateParams', function($http,RestService,ToolService,$stateParams){
	var hlmng = this;
	hlmng.eventroom={}; 
	hlmng.redir=ToolService.redir;
	hlmng.postEventRoom = RestService.post;
	hlmng.postAndRedir = function() {  
		hlmng.eventroom.eventIDFK=$stateParams.eventId;
		hlmng.postEventRoom(hlmng.eventroom,'eventroom').then(function(data){
			hlmng.eventroom=data;
			hlmng.redir('/eventactive/'+$stateParams.eventId+'/eventroom/'+hlmng.eventroom.eventRoomID);
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

eventRoomModule.controller('EventRoomIdController', ['$http','$stateParams','RestService', function($http, $stateParams,RestService){
	var hlmng = this;
	
	RestService.get($stateParams.eventRoomId,'eventroom').then(function(data){
		hlmng.eventroom=data;
	});

	hlmng.deleteEventRoom = RestService.del;
	hlmng.putEventRoom = RestService.put;
}]);