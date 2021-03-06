var socialModule = angular.module('social', []);

socialModule.controller('SocialListController', ['$http','RestService','$stateParams', function($http,RestService,$stateParams){
	var hlmng = this;
	hlmng.socials = [];  // contains all
	hlmng.socialsAccepted = [];
	hlmng.socialsPublished = [];
	hlmng.socialsRejected = [];
	hlmng.socialsPending  = [];
	
	hlmng.setAccepted = function(social) {  
		social.status = "accepted";
		hlmng.putSocial(social,social.socialID,'social');
		hlmng.removeFromAll(social);
		hlmng.socialsAccepted.push(social);
	}; 
	hlmng.setPublished = function(social) {  
		RestService.get(social.socialID,'social').then(function(updatedsocial){
			updatedsocial.status = "published";
			hlmng.putSocial(updatedsocial,updatedsocial.socialID,'social');
			hlmng.removeFromAll(social);
			RestService.get(updatedsocial.socialID,'social','publications').then(function(publicationsdata){
				updatedsocial.publications=publicationsdata;
				hlmng.socialsPublished.push(updatedsocial);			
			});
		});
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
		var i = hlmng.socialsPublished.indexOf(social);
		if(i != -1) {
			hlmng.socialsPublished.splice(i, 1);
		}
	};
	RestService.get($stateParams.eventId,'event','socials').then(function(data){
	    $.each(data, function(i, item){
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
    		if(item.status=="published"){
    			RestService.get(item.socialID,'social','publications').then(function(data){
    				item.publications=data;
        			hlmng.socialsPublished.push(item);	
    			});
    		}
	    });
	});
}]);


socialModule.controller('SocialIdController', ['$http','$stateParams','RestService','ToolService', function($http, $stateParams,RestService,ToolService){
	var hlmng = this;
	
	hlmng.social = {};	
	RestService.get($stateParams.socialId,'social').then(function(data){
		hlmng.social=data;
	});

	hlmng.putSocial = RestService.put;
	hlmng.deleteSocial = RestService.del;
}]);

