var app = angular.module('speaker', []);

app.directive('speakerInfo', function(){
	return {
		restrict: 'E',
		templateUrl: 'template/speaker/speaker-info.html'
	};
});
