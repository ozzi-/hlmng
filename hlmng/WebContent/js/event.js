var app = angular.module('event', []);

app.directive('eventInfo', function(){
	return {
		restrict: 'E',
		templateUrl: 'template/event/event-info.html',
		scope: {
			event: '=event'
		}
	};
});

app.directive('eventInfoEdit', function(){
	return {
		restrict: 'E',
		templateUrl: 'template/event/event-info-edit.html',
		scope: {
			event: '=event'
		}
	};
});