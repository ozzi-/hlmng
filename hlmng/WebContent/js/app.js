var app = angular.module('hlmngApp',['ngRoute','speaker','event']);
var apiUrl = 'https://localhost:8443/hlmng/rest/';


app.controller('IndexController', ['$http', function($http){
	$http.get(apiUrl+'backendlogin').success(function(data){
		if(data==='true'){
			// LOGGED IN 
		}else{
			// NOT LOGGED IN, WHAT DO 
			// TODO for all pages
		}
	});
}]);
 	

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
	}
}]);

app.controller('SpeakerListController', ['$http','RestService', function($http,RestService){
	var hlmng = this;
	hlmng.speakers = [];
	
	RestService.list('speaker').then(function(data){
		hlmng.speakers=data;
	});
	
}]);



app.controller('EventIdController', ['$http','$routeParams','RestService', function($http, $routeParams,RestService){
	var hlmng = this;

	hlmng.event = {};
	RestService.get($routeParams.eventId,'event').then(function(data){
		hlmng.event=data;
	});

	hlmng.putEvent = RestService.put;
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

app.controller('EventListController', ['$http','RestService', function($http,RestService){
	var hlmng = this;
	hlmng.events = [];
	RestService.list('event').then(function(data){
		hlmng.events=data;
	});
}]);
