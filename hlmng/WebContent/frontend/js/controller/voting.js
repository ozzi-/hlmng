var votingModule = angular.module('voting', []);

votingModule.controller('VotingListController', ['$http','RestService','$stateParams', function($http,RestService,$stateParams){
	var hlmng = this;
	hlmng.votings = [];
	hlmng.votingsUpcoming = [];
	hlmng.votingsRunning = [];
	hlmng.votingsFinished = [];
	
	RestService.list('voting').then(function(data){
	    $.each(data, function(i, item){
	    	if(item.eventIDFK==$stateParams.eventId){
	    		hlmng.votings.push(item);	  
	    		//pre_presentation,presentation,presentation_end,voting,voting_end
	    		//if(item.status=="upcoming")
	    	}
	    });
	});
}]);