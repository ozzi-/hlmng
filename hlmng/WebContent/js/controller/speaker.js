var speakerModule = angular.module('speaker', []);


app.controller('SpeakerNewController', ['$http','RestService','ToolService','dataService', function($http,RestService,ToolService,dataService){
	var hlmng = this;
	hlmng.speaker={};
	hlmng.media={};
	hlmng.postSpeaker = RestService.post;
	hlmng.redir=ToolService.redir;
	hlmng.postAndRedir = function(fSpeaker) { 
		hlmng.speaker.mediaIDFK=dataService.dataObj;
		dataService.dataObj=0;
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


app.controller('SpeakerIdController', ['$http','$stateParams','RestService','ToolService', function($http, $stateParams,RestService,ToolService){
	var hlmng = this;
	
	hlmng.speaker = {};	
	RestService.get($stateParams.speakerId,'speaker').then(function(data){
		hlmng.speaker=data;
	});

	hlmng.putSpeaker = RestService.put;
	hlmng.deleteSpeaker = RestService.del;
	hlmng.redir=ToolService.redir;
}]);

