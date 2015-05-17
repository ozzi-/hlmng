var votingModule = angular.module('voting', []);

votingModule.controller('VotingListController', ['$http','RestService','$stateParams', function($http,RestService,$stateParams){
	var hlmng = this;
	hlmng.votings = [];
	hlmng.votingsUpcomingPrePresentation = [];
	hlmng.votingsUpcomingPresentation = [];
	hlmng.votingsUpcomingEndPresentation = [];
	hlmng.votingsRunning = [];
	hlmng.votingsFinished = [];
	hlmng.votingsList = [hlmng.votingsUpcomingPrePresentation,hlmng.votingsUpcomingPresentation,hlmng.votingsUpcomingEndPresentation,hlmng.votingsRunning,hlmng.votingsFinished];
	
	hlmng.putVoting = RestService.put;
	
	hlmng.startPresentation = function(voting) {  
		voting.status = "presentation";
		hlmng.putVoting(voting,voting.votingID,'voting');
		hlmng.removeFromAll(voting);
		hlmng.votingsUpcomingPresentation.push(voting);
	};
	
	hlmng.endPresentation = function(voting) {  
		voting.status = "presentation_end";
		hlmng.putVoting(voting,voting.votingID,'voting');
		hlmng.removeFromAll(voting);
		hlmng.votingsUpcomingEndPresentation.push(voting);
	};
	
	hlmng.startVoting = function(voting) {  
		voting.status = "voting";
		hlmng.putVoting(voting,voting.votingID,'voting');
		hlmng.removeFromAll(voting);
		hlmng.votingsRunning.push(voting);
	};
	
	hlmng.stopVoting = function(voting) {  
		voting.status = "voting_end";
		hlmng.putVoting(voting,voting.votingID,'voting');
		hlmng.removeFromAll(voting);
		hlmng.votingsFinished.push(voting);
	};
	
	hlmng.removeFromAll = function(voting){	
		for (var index = 0; index < hlmng.votingsList.length; ++index) {
			var i = hlmng.votingsList[index].indexOf(voting);
			if(i != -1) {
				hlmng.votingsList[index].splice(i, 1);
			}
		}
	};
	
	
	RestService.list('voting').then(function(data){
	    $.each(data, function(i, item){
	    	if(item.eventIDFK==$stateParams.eventId){
	    		hlmng.votings.push(item);	  
	    		if (item.status=="pre_presentation"){
	    			hlmng.votingsUpcomingPrePresentation.push(item);
    			}else if (item.status=="presentation"){
    				hlmng.votingsUpcomingPresentation.push(item);
	    		}else if (item.status=="presentation_end"){
	    			hlmng.votingsUpcomingEndPresentation.push(item);
	    		}else if (item.status=="voting"){
	    			hlmng.votingsRunning.push(item);
	    		}else if (item.status=="voting_end"){
	    			hlmng.votingsFinished.push(item);
	    		}
	    	}
	    });
	});
}]);


votingModule.controller('VotingNewController', ['$http','$stateParams','RestService','ToolService','dataService', function($http,$stateParams,RestService,ToolService,dataService){
	var hlmng = this;
	hlmng.voting={};
	hlmng.sliders=[];
	hlmng.voting.sliderMaxValue=10;
	hlmng.postVoting = RestService.post;
	hlmng.postSlider = RestService.post;
	hlmng.votingSettingsMode='voting-default';
	hlmng.voting.arithmeticMode="arithmetic";
	var internalIDCounter=0;


	var defaultSlider='{ "name":"" , "votingIDFK":0, "weight":1,"internalID":3 }';
	var defaultSliderOne='{ "name":"Speech" , "votingIDFK":0, "weight":1,"internalID":0}';
	var defaultSliderTwo='{ "name":"Solution" , "votingIDFK":0, "weight":1,"internalID":1 }';
	var defaultSliderThree='{ "name":"Presentation" , "votingIDFK":0, "weight":1,"internalID":2 }';
	hlmng.sliders.push(JSON.parse(defaultSliderOne));
	hlmng.sliders.push(JSON.parse(defaultSliderTwo));
	hlmng.sliders.push(JSON.parse(defaultSliderThree));
	
	hlmng.postAndRedir = function() { 
		hlmng.voting.status="pre_presentation";
		hlmng.voting.eventIDFK=$stateParams.eventId;
			hlmng.postVoting(hlmng.voting,'voting').then(function(data){
			hlmng.voting=data;
			for (var index = 0; index < hlmng.sliders.length ; ++index) {
				hlmng.sliders[index].votingIDFK= hlmng.voting.votingID;
				hlmng.postSlider(hlmng.sliders[index],'slider').then(function(data){
					if(index==hlmng.sliders.length){
						ToolService.redir('event.active.voting.id',{eventId: $stateParams.eventId, votingId: hlmng.voting.votingID});
					}
				});
			}
		});
			
		
	};
	
	hlmng.addNewSlider = function() {
		var sliderObj = JSON.parse(defaultSlider);
		sliderObj.internalID = internalIDCounter;
		hlmng.sliders.push(sliderObj);
		internalIDCounter++;
	};
	
	hlmng.removeSlider = function(id){
		for (var index = 0; index < hlmng.sliders.length; ++index) {
			if(hlmng.sliders[index].internalID==id){
				hlmng.sliders.splice(index,1);					
			}
		}
	};
	
	
	hlmng.postSliders = function() {
		for(var i = 0; i < hlmng.sliders.length;  i++) {
			RestService.post(hlmng.sliders[i],'slider').then(function(data){
				alert(data);
			});
		}
	};
	
}]);


votingModule.controller('VotingIdController', ['$http','$stateParams','RestService','ToolService', function($http, $stateParams,RestService,ToolService){
	var hlmng = this;
	
	hlmng.voting = {};	
	RestService.get($stateParams.votingId,'voting').then(function(data){
		hlmng.voting=data;
	});

	hlmng.putVoting = RestService.put;
	hlmng.deleteVoting = RestService.del;
}]);

