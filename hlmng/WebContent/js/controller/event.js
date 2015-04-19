var eventModule = angular.module('event', []);


app.controller('EventNewController', ['$http','RestService','ToolService', function($http,RestService,ToolService){
	var hlmng = this;
	hlmng.event={};
	hlmng.postEvent = RestService.post;
	hlmng.redir=ToolService.redir;
	hlmng.postAndRedir = function(fEvent) {  
		hlmng.postEvent(fEvent,'event').then(function(data){
			hlmng.event=data;
			hlmng.redir('/event/'+hlmng.event.eventID);
		});
	};
}]);

app.controller('EventIdController', ['$http','$routeParams','RestService', function($http, $routeParams,RestService){
	var hlmng = this;
	hlmng.form = {};
	
	RestService.get($routeParams.eventId,'event').then(function(data){
		hlmng.event=data;
	});

	hlmng.putEvent = RestService.put;
}]);

app.controller('EventListController', ['$http','RestService', function($http,RestService){
	var hlmng = this;
	hlmng.events = [];
	RestService.list('event').then(function(data){
		hlmng.events=data;
	});
}]);