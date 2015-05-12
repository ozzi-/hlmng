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



votingModule.controller('VotingIdController', ['$http','$stateParams','RestService','ToolService', function($http, $stateParams,RestService,ToolService){
	var hlmng = this;
	
	hlmng.voting = {};	
	RestService.get($stateParams.votingId,'voting').then(function(data){
		hlmng.voting=data;
	});

	hlmng.putVoting = RestService.put;
	hlmng.deleteVoting = RestService.del;
}]);

