var speakerModule = angular.module('speaker', []);

app.controller('SpeakerNewController', ['$http','RestService','ToolService', function($http,RestService,ToolService){
	var hlmng = this;
	hlmng.speaker={};
	hlmng.postSpeaker = RestService.post;
	hlmng.redir=ToolService.redir;
	hlmng.postAndRedir = function(fSpeaker) {  
		hlmng.postSpeaker(fSpeaker,'speaker').then(function(data){
			hlmng.speaker=data;
			hlmng.redir('/speaker/'+hlmng.speaker.speakerID);
		});
	};
}]);

app.controller('SpeakerListController', ['$http','RestService', function($http,RestService){
	var hlmng = this;
	hlmng.speakers = [];
	
	RestService.list('speaker').then(function(data){
		hlmng.speakers=data;
	});
	
}]);




app.controller('SpeakerIdController', ['$http','$routeParams','RestService','ToolService', function($http, $routeParams,RestService,ToolService){
	var hlmng = this;
	
	hlmng.speaker = {};	
	RestService.get($routeParams.speakerId,'speaker').then(function(data){
		hlmng.speaker=data;
	});

	hlmng.putSpeaker = RestService.put;
	hlmng.deleteSpeaker = RestService.del;
	hlmng.redir=ToolService.redir;
}]);

speakerModule.directive('speakerInfo', function(){
	return {
		restrict: 'E',
		templateUrl: 'template/speaker/speaker-info.html',
		scope: {
			speaker: '=speaker'
		}
	};
});

speakerModule.directive('speakerInfoEdit', function(){
	return {
		restrict: 'E',
		templateUrl: 'template/speaker/speaker-info-edit.html',
		scope: {
			speaker: '=speaker'
		}
	};
});