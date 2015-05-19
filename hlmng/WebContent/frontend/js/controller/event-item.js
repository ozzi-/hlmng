var eventItemModule = angular.module('eventitem', []);


eventItemModule.controller('EventItemNewController', ['$http','RestService','ToolService','$stateParams','$filter', function($http,RestService,ToolService,$stateParams,$filter){
	var hlmng = this;
	hlmng.eventitem={}; 
	hlmng.postEventItem = RestService.post;
	hlmng.postAndRedir = function() {  
		hlmng.eventitem.startTime = $filter('date')(hlmng.eventitem.startTime, "HH:mm");
		hlmng.eventitem.endTime = $filter('date')(hlmng.eventitem.endTime, "HH:mm");

		hlmng.eventitem.eventIDFK=$stateParams.eventId;
		hlmng.postEventItem(hlmng.eventitem,'eventitem').then(function(data){
			hlmng.eventitem=data;
			ToolService.redir('event.active.eventitem.id',{eventId:$stateParams.eventId, eventItemId: hlmng.eventitem.eventItemID});
		});
	};
}]);

eventItemModule.controller('EventItemListController', ['$http','RestService','$stateParams', function($http,RestService,$stateParams){
	var hlmng = this;
	hlmng.eventitems = [];
	
	RestService.list('eventitem').then(function(data){
	    $.each(data, function(i, item){
	    	if(item.eventIDFK==$stateParams.eventId){
	    		hlmng.eventitems.push(item);	  
	    	}
	    });
	});
}]);

eventItemModule.controller('EventItemIdController', ['$http','$stateParams','RestService','ToolService', function($http, $stateParams,RestService,ToolService){
	var hlmng = this;
	hlmng.eventitem = {};
	
	RestService.get($stateParams.eventItemId,'eventitem').then(function(data){
		hlmng.eventitem=data;
		ToolService.idSolver(hlmng.eventitem.roomIDFK,'eventroom').then(function(data){
			hlmng.eventitem.roomName=data.name;
		});
		ToolService.idSolver(hlmng.eventitem.speakerIDFK,'speaker').then(function(data){
			hlmng.eventitem.speakerName=data.name;
		});
	});
	
	hlmng.redirToList = function() { 
		ToolService.redir('event.active.eventitem.list',{eventId: $stateParams.eventId});
	};
	
	hlmng.deleteEventItem = RestService.del;

}]);