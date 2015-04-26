var eventModule = angular.module('eventitem', []);


app.controller('EventItemNewController', ['$http','RestService','ToolService','$stateParams', function($http,RestService,ToolService,$stateParams){
	var hlmng = this;
	hlmng.eventitem={}; 
	hlmng.redir=ToolService.redir;
	hlmng.postEventItem = RestService.post;
	hlmng.postAndRedir = function(fEventItem) {  
		fEventItem.eventIDFK=$stateParams.eventId;
		hlmng.postEventItem(fEventItem,'eventitem').then(function(data){
			hlmng.eventitem=data;
			hlmng.redir('/eventactive/'+$stateParams.eventId+'/eventitem/'+hlmng.eventitem.eventItemID);
		});
	};
}]);

app.controller('EventItemListController', ['$http','RestService','$stateParams', function($http,RestService,$stateParams){
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

app.controller('EventItemIdController', ['$http','$stateParams','RestService', function($http, $stateParams,RestService){
	var hlmng = this;
	
	RestService.get($stateParams.eventItemId,'eventitem').then(function(data){
		hlmng.eventitem=data;
	});

	hlmng.putEvent = RestService.put;
}]);