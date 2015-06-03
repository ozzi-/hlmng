var votingModule = angular.module('voting', []);

votingModule.controller('VotingListController', ['$http','RestService','$stateParams','$interval','$scope','ToolService', function($http,RestService,$stateParams,$interval,$scope,ToolService){
	var hlmng = this;
	hlmng.votings = [];
	hlmng.votingsUpcomingPrePresentation = [];
	hlmng.votingsUpcomingPresentation = [];
	hlmng.votingsUpcomingEndPresentation = [];
	hlmng.votingsRunning = [];
	hlmng.eventID= $stateParams.eventId;
	hlmng.votingsFinished = [];
	hlmng.votingsList = [hlmng.votingsUpcomingPrePresentation,hlmng.votingsUpcomingPresentation,
	                     hlmng.votingsUpcomingEndPresentation,hlmng.votingsRunning,hlmng.votingsFinished];
	
	hlmng.putVoting = RestService.put;
	hlmng.postPausePresentation= RestService.post;

	hlmng.pausePresentation = function(voting) {
		if(voting.ispaused==false){
			var pause='{ "start":"00:00:00" , "votingIDFK":0}';
			var pauseObj=JSON.parse(pause);
			pauseObj.start = ToolService.getCurTime();
			pauseObj.votingIDFK= voting.votingID;
			hlmng.postPausePresentation(pauseObj,'presentationpause').then(function(data){
				voting.ispaused=true;
			});
		}
	};
	
	hlmng.resumePresentation = function(voting) {
		if(voting.ispaused){
			RestService.get(voting.votingID,'voting','getpause').then(function(data){
				var pauseObj = data;
				pauseObj.stop= ToolService.getCurTime();
				RestService.put(pauseObj,pauseObj.presentationpauseID,'presentationpause');
				voting.ispaused=false;
			});
		}
	};
	
	hlmng.startPresentation = function(voting) {  
		voting.status = "presentation";
		voting.presentationStarted=ToolService.getCurTime();
		voting.ispaused=false;
		hlmng.putVoting(voting,voting.votingID,'voting');
		hlmng.removeFromAll(voting);
		hlmng.votingsUpcomingPresentation.push(voting);
	};
	
	hlmng.endPresentation = function(voting) {  
		voting.status = "presentation_end";
		voting.presentationEnded=ToolService.getCurTime();
		hlmng.putVoting(voting,voting.votingID,'voting');
		hlmng.removeFromAll(voting);
		hlmng.votingsUpcomingEndPresentation.push(voting);
	};
	
	hlmng.startVoting = function(voting) {  
		voting.status = "voting";
		voting.votingStarted=ToolService.getCurTime();
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
	
	hlmng.newRound = function(voting) {  
	    if (confirm("Are you sure?")) {
	    	voting.round += 1;
			hlmng.endPresentation(voting);
	    }
	};
	
	hlmng.removeFromAll = function(voting){	
		for (var index = 0; index < hlmng.votingsList.length; ++index) {
			var i = hlmng.votingsList[index].indexOf(voting);
			if(i != -1) {
				hlmng.votingsList[index].splice(i, 1);
			}
		}
	};

	var refreshData = function() {
		var index;
		for (index = 0; index < hlmng.votingsRunning.length; ++index) {		
			refreshRunningVoting(index);
		}
		for (index = 0; index < hlmng.votingsUpcomingPresentation.length; ++index) {		
			refreshRunningPresentation(index);
		}
		
	};
	
	var refreshRunningPresentation = function(index){
		var voting = hlmng.votingsUpcomingPresentation[index];
		RestService.get(hlmng.votingsUpcomingPresentation[index].votingID,'voting','duration').then(function(data){	
			if(data){
				voting.duration=data;				
			}
		});
	};
	
	var refreshRunningVoting = function(index){
		var voting = hlmng.votingsRunning[index];
		RestService.get(hlmng.votingsRunning[index].votingID,'voting','audiencevotingtimeleft').then(function(data){
			if(data){
				voting.audiencevotingtimeleft=data;
			}
		});
		RestService.get(hlmng.votingsRunning[index].votingID,'voting','votes/audience/count').then(function(data){	
			voting.votesAudienceCount=data;
	
	
			RestService.get(voting.votingID,'voting','audiencevotingover').then(function(data){
				if(data){
					voting.audiencevotingover=true;
				}
			});
			RestService.get(voting.votingID,'voting','votes/jury/count').then(function(data){
				voting.votesJuryCount=data;
			});
		});
	};
	

	var promise = $interval(refreshData,4000);
	// Cancel interval on page changes
	$scope.$on('$destroy', function(){
	    if (angular.isDefined(promise)) {
	        $interval.cancel(promise);
	        promise = undefined;
	    }
	});
	
	RestService.get($stateParams.eventId,'event','votings').then(function(data){
	    $.each(data, function(i, item){
    		hlmng.votings.push(item);	 
    		item.audiencevotingover=false;
    		if (item.status=="pre_presentation"){
    			hlmng.votingsUpcomingPrePresentation.push(item);
			}else if (item.status=="presentation"){
				RestService.get(item.votingID,'voting','ispaused').then(function(data){
					item.ispaused=data;
					hlmng.votingsUpcomingPresentation.push(item);
				});
    		}else if (item.status=="presentation_end"){
    			hlmng.votingsUpcomingEndPresentation.push(item);
    		}else if (item.status=="voting"){
    			hlmng.votingsRunning.push(item);
    		}else if (item.status=="voting_end"){
    			hlmng.votingsFinished.push(item);
    		}
	    });
	    refreshData();
	});
}]);


votingModule.controller('VotingNewController', ['$http','$stateParams','RestService','ToolService','dataService', function($http,$stateParams,RestService,ToolService,dataService){
	var hlmng = this;
	hlmng.voting={};
	hlmng.sliders=[];
	hlmng.postVoting = RestService.post;
	hlmng.postSlider = RestService.post;
	var internalIDCounter=0;
	var defaultSlider='{ "name":"" , "votingIDFK":0, "weight":1,"internalID":0 }';

	
	hlmng.resetDefault = function() { 
		hlmng.sliders.length =0 ;
		var defaultSliderOne='{ "name":"Talk Intro" , "votingIDFK":0, "weight":1,"internalID":0}';
		var defaultSliderTwo='{ "name":"Clear Statement" , "votingIDFK":0, "weight":1,"internalID":1 }';
		var defaultSliderThree='{ "name":"Lesson Learned" , "votingIDFK":0, "weight":1,"internalID":2 }';
		var defaultSliderFour='{ "name":"Rhetoric" , "votingIDFK":0, "weight":1,"internalID":3 }';
		var defaultSliderFive='{ "name":"Like Factor" , "votingIDFK":0, "weight":1,"internalID":4 }';
		hlmng.sliders.push(JSON.parse(defaultSliderOne));
		hlmng.sliders.push(JSON.parse(defaultSliderTwo));
		hlmng.sliders.push(JSON.parse(defaultSliderThree));
		hlmng.sliders.push(JSON.parse(defaultSliderFour));
		hlmng.sliders.push(JSON.parse(defaultSliderFive));
		hlmng.votingSettingsMode='voting-default';
		hlmng.voting.arithmeticMode="arithmetic";
		hlmng.voting.sliderMaxValue=10;
		hlmng.voting.inTimeScoreWeight=1;
	};

	hlmng.resetDefault();
	
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
			});
		}
	};
	
}]);


votingModule.controller('VotingStatsController', ['$http','$stateParams','RestService','ToolService', function($http, $stateParams,RestService,ToolService){
	var hlmng = this;
	
	hlmng.voting = {};	
	
	
	hlmng.getVoteScores = function(slider) {	
		RestService.get(slider.sliderID,'slider','votescorejury').then(function(data){
			slider.voteScoreJury=data;
		});
		RestService.get(slider.sliderID,'slider','votescoreaudience').then(function(data){
			slider.voteScoreAudience=data;
		});
		return slider;
	};
	hlmng.getTotalVoteScores = function(voting) {	
		RestService.get(voting.votingID,'voting','totalscorejury').then(function(data){
			voting.totalVoteScoreJury=data;
		});
		RestService.get(voting.votingID,'voting','totalscoreaudience').then(function(data){
			voting.totalVoteScoreAudience=data;
		});
		return voting;
	};
	
	RestService.get($stateParams.votingId,'voting').then(function(data){
		hlmng.voting=data;
		hlmng.sliders={};
		hlmng.inTime="-";
		RestService.get($stateParams.votingId,'voting','presentationintime').then(function(data){
			if(data==true){
				hlmng.inTime="Yes";				
			}else{
				hlmng.inTime="No";
			}
			RestService.get($stateParams.votingId,'voting','sliders').then(function(data){
				hlmng.sliders=data;
				for (var index = 0; index < hlmng.sliders.length; ++index) {
					hlmng.sliders[index] = hlmng.getVoteScores(hlmng.sliders[index]);
				}
				hlmng.voting=hlmng.getTotalVoteScores(hlmng.voting);
				hlmng.fields = [{	
			       	label: 'Voting',
			       	value: '<hr>'
			    },{
					label: 'Name',
					value: hlmng.voting.name
			    },{
					label: 'Voting ID',
					value: hlmng.voting.votingID
			    },{
			       	label: 'Voting Started',
			       	value: hlmng.voting.votingStarted
			    },{
			       	label: 'Voting Duration',
			       	value: hlmng.voting.votingDuration
			    },{
			       	label: 'Jury Count',
			       	value: hlmng.voting.juryCount
			    },{
			       	label: 'Mode',
			       	value: hlmng.voting.arithmeticMode
			    },{
			       	label: 'Presentation',
			       	value: '<hr>'
			    },{
			       	label: 'Presentation Min Time',
			       	value: hlmng.voting.presentationMinTime
			    },{
			       	label: 'Presentation Min Time',
			       	value: hlmng.voting.presentationMinTime
			    },{
			       	label: 'Presentation Max Time',
			       	value: hlmng.voting.presentationMaxTime
			    },{
			       	label: 'Presentation Started',
			       	value: hlmng.voting.presentationStarted
			    },{
			       	label: 'Presentation Ended',
			       	value: hlmng.voting.presentationEnded
			    },{
			       	label: 'Presentation was in Time',
			       	value: hlmng.inTime
				}];
			});
			
		});		
	});
}]);




votingModule.controller('VotingIdController', ['$http','$stateParams','RestService','ToolService', function($http, $stateParams,RestService,ToolService){
	var hlmng = this;
	
	hlmng.voting = {};
	hlmng.sliders ={};
	
	RestService.get($stateParams.votingId,'voting').then(function(data){
		hlmng.voting=data;
	});
	RestService.get($stateParams.votingId,'voting','sliders').then(function(data){
		hlmng.sliders=data;
	});

	hlmng.putVoting = RestService.put;
	hlmng.deleteVoting = RestService.del;
}]);

