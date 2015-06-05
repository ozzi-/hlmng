var speakerModule = angular.module('speaker', []);


speakerModule.controller('SpeakerNewController', ['$http','RestService','ToolService','dataService', function($http,RestService,ToolService,dataService){
	var hlmng = this;
	hlmng.speaker={};
	hlmng.media={};
	hlmng.postSpeaker = RestService.post;
	hlmng.postAndRedir = function() { 
		hlmng.speaker.mediaIDFK=dataService.dataObj;
		dataService.dataObj=0;
		hlmng.postSpeaker(hlmng.speaker,'speaker').then(function(data){
			hlmng.speaker=data;
			ToolService.redir('speaker.id',{speakerId: hlmng.speaker.speakerID});
		});
	};
}]);

speakerModule.controller('SpeakerListController', ['$http','RestService','ToolService', function($http,RestService,ToolService){
	var hlmng = this;
	hlmng.speakers = [];
	hlmng.currentPage = 1;
	
	hlmng.updatePage = function(){
		ToolService.pagination(hlmng.speakers,hlmng);
	};
	
	RestService.list('speaker').then(function(data){
		hlmng.speakers=data;
		hlmng.updatePage();
	});	
}]);


speakerModule.controller('SpeakerIdController', ['$http','$stateParams','RestService','ToolService','dataService', function($http, $stateParams,RestService,ToolService,dataService){
	var hlmng = this;
	
	hlmng.speaker = {};	
	RestService.get($stateParams.speakerId,'speaker').then(function(data){
		hlmng.speaker=data;
	});

	hlmng.putSpeaker = function(speaker,speakerID){
		if(dataService.dataObj!=0){
			hlmng.speaker.mediaIDFK=dataService.dataObj;
        	hlmng.speaker.media=dataService.dataObj2; 
		}
		dataService.dataObj=0;
		dataService.dataObj2=0;
		
		RestService.put(speaker,speakerID,'speaker');
		ToolService.redir('speaker.id',{speakerId: hlmng.speaker.speakerID}); 
	};

	hlmng.deleteSpeaker = RestService.del;

}]);

