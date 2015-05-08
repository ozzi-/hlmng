app.controller('SocialListController', ['$http','RestService','$stateParams', function($http,RestService,$stateParams){
	var hlmng = this;
	hlmng.socials = [];  // contains all
	hlmng.socialsAccepted = [];
	hlmng.socialsRejected = [];
	hlmng.socialsPending  = [];
	

	hlmng.setAccepted = function(social) {  
		social.status = "accepted";
		hlmng.putSocial(social,social.socialID,'social');
		hlmng.removeFromAll(social);
		hlmng.socialsAccepted.push(social);
	}; 
	hlmng.setRejected = function(social) {  
		social.status = "rejected";
		hlmng.putSocial(social,social.socialID,'social');
		hlmng.removeFromAll(social);
		hlmng.socialsRejected.push(social);
	};
	hlmng.setPending = function(social) {  
		social.status = "pending";
		hlmng.putSocial(social,social.socialID,'social');
		hlmng.removeFromAll(social);
		hlmng.socialsPending.push(social);
	};
	
	hlmng.putSocial = RestService.put;
	
	hlmng.removeFromAll = function(social){
		var i = hlmng.socialsAccepted.indexOf(social);
		if(i != -1) {
			hlmng.socialsAccepted.splice(i, 1);
		}
		var i = hlmng.socialsPending.indexOf(social);
		if(i != -1) {
			hlmng.socialsPending.splice(i, 1);
		}
		var i = hlmng.socialsRejected.indexOf(social);
		if(i != -1) {
			hlmng.socialsRejected.splice(i, 1);
		}
	};
	
	RestService.list('social').then(function(data){
	    $.each(data, function(i, item){
	    	if(item.eventIDFK==$stateParams.eventId){
	    		hlmng.socials.push(item);
	    		if(item.status=="pending"){
	    			hlmng.socialsPending.push(item);	  	    			
	    		}
	    		if(item.status=="accepted"){
	    			hlmng.socialsAccepted.push(item);	  	    			
	    		}
	    		if(item.status=="rejected"){
	    			hlmng.socialsRejected.push(item);	  	    			
	    		}
	    	}
	    });
	});
}]);

RestService.list('event').then(function(data){
	var curDate = new Date().setHours(0,0,0,0);
    $.each(data, function(i, item){
    	var itemStartDate = new Date(item.startDate).setHours(0,0,0,0);
    	var itemEndDate = new Date(item.endDate).setHours(0,0,0,0);
    	if(itemStartDate<=curDate && itemEndDate>=curDate){
    		hlmng.eventsRunning.push(item);
    	}
    	if(itemEndDate<curDate && itemStartDate<curDate){
    		hlmng.eventsFinished.push(item);
    	}
    	if(itemStartDate>curDate && itemEndDate>curDate){
    		hlmng.eventsUpcoming.push(item);
    	}
    	hlmng.events.push(item);
    });	
});

app.controller('SocialIdController', ['$http','$stateParams','RestService','ToolService', function($http, $stateParams,RestService,ToolService){
	var hlmng = this;
	
	hlmng.social = {};	
	RestService.get($stateParams.socialId,'social').then(function(data){
		hlmng.social=data;
	});

	hlmng.putSocial = RestService.put;
	hlmng.deleteSocial = RestService.del;
	hlmng.redir=ToolService.redir;
}]);