var eventModule = angular.module('event', []);

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



eventModule.directive('eventInfo', function(){
	return {
		restrict: 'E',
		templateUrl: 'template/event/event-info.html',
		scope: {
			event: "=event"
		}
	};
});

eventModule.directive('eventInfoEdit', function(){
	return {
		restrict: 'E',
		templateUrl: 'template/event/event-info-edit.html',
		scope: {
			event: "=event"
		}
	};
});