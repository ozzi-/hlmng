var eventModule = angular.module('eventroom', []);


app.controller('EventRoomNewController', ['$http','RestService','ToolService','$stateParams', function($http,RestService,ToolService,$stateParams){
	var hlmng = this;
	hlmng.eventroom={}; 
	hlmng.redir=ToolService.redir;
	hlmng.postEventRoom = RestService.post;
	hlmng.postAndRedir = function(fEventRoom) {  
		fEventRoom.eventIDFK=$stateParams.eventId;
		hlmng.postEventRoom(fEventRoom,'eventroom').then(function(data){
			hlmng.eventroom=data;
			hlmng.redir('/eventactive/'+$stateParams.eventId+'/eventroom/'+hlmng.eventroom.eventRoomID);
		});
	};
}]);

app.controller('EventRoomListController', ['$http','RestService','$stateParams', function($http,RestService,$stateParams){
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

app.controller('EventRoomIdController', ['$http','$stateParams','RestService', function($http, $stateParams,RestService){
	var hlmng = this;
	
	RestService.get($stateParams.eventRoomId,'eventroom').then(function(data){
		hlmng.eventroom=data;
	});

	hlmng.putEvent = RestService.put;
}]);