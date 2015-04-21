var eventModule = angular.module('eventroom', []);


app.controller('EventRoomNewController', ['$http','RestService','ToolService', function($http,RestService,ToolService){
	var hlmng = this;
	hlmng.eventroom={};
	hlmng.postEventRoom = RestService.post;
	hlmng.postAndRedir = function(fEventRoom) {  
		hlmng.postEventRoom(fEventRoom,'eventroom').then(function(data){
			hlmng.eventroom=data;
			hlmng.redir('/eventroom/'+hlmng.eventroom.eventroomID);
		});
	};
}]);

app.controller('EventRoomListController', ['$http','RestService','$routeParams', function($http,RestService,$routeParams){
	var hlmng = this;
	hlmng.eventrooms = [];
	
	RestService.list('eventroom').then(function(data){
	    $.each(data, function(i, item){
	    	if(item.eventIDFK==$routeParams.eventId){
	    		hlmng.eventrooms.push(item);	  
	    	}
	    });
	});
}]);
