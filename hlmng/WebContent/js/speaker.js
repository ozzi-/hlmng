var app = angular.module('speaker', []);

app.directive('speakerInfo', function(){
	return {
		restrict: 'E',
		templateUrl: 'template/speaker/speaker-info.html',
		scope: {
			speaker: '=speaker'
		}
	};
});

app.directive('speakerInfoEdit', function(){
	return {
		restrict: 'E',
		templateUrl: 'template/speaker/speaker-info-edit.html',
		scope: {
			speaker: '=speaker'
		}
	};
});