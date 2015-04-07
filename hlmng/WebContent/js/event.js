var app = angular.module('event', []);

app.directive('eventInfo', function(){
	return {
		restrict: 'E',
		templateUrl: 'template/event/event-info.html'
	};
});