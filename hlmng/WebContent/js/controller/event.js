var eventModule = angular.module('event', []);


app.controller('EventNewController', ['$http','RestService','ToolService', function($http,RestService,ToolService){
	var hlmng = this;
	hlmng.event={};
	hlmng.postEvent = RestService.post;
	hlmng.redir=ToolService.redir;
	hlmng.postAndRedir = function(fEvent) {  
		hlmng.postEvent(fEvent,'event').then(function(data){
			hlmng.event=data;
			hlmng.redir('/event/'+hlmng.event.eventID);
		});
	};
}]);

app.controller('EventIdController', ['$http','$routeParams','RestService', function($http, $routeParams,RestService){
	var hlmng = this;
	hlmng.form = {};
	
	RestService.get($routeParams.eventId,'event').then(function(data){
		hlmng.event=data;
	});

	hlmng.putEvent = RestService.put;
}]);

app.controller('EventListController', ['$http','RestService', function($http,RestService){
	var hlmng = this;
	hlmng.eventsUpcoming = [];
	hlmng.eventsRunning = [];
	hlmng.eventsFinished = [];
	
	
	RestService.list('event').then(function(data){
		var curDate = new Date().setHours(0,0,0,0);
        $.each(data, function(i, item){
        	var itemStartDate = new Date(item.startDate).setHours(0,0,0,0);
        	var itemEndDate = new Date(item.endDate).setHours(0,0,0,0);
        	if(itemStartDate<=curDate && itemEndDate>=curDate){
        		//alert("RUNNING: "+itemStartDate+" - "+itemEndDate+" CURR: "+curDate);
        		hlmng.eventsRunning.push(item);
        	}
        	if(itemEndDate<curDate && itemStartDate<curDate){
        		//alert("FINISHED: "+itemStartDate+" - "+itemEndDate+" CURR: "+curDate);
        		hlmng.eventsFinished.push(item);
        	}
        	if(itemStartDate>curDate && itemEndDate>curDate){
        		//alert("UPCOMING: "+itemStartDate+" - "+itemEndDate+" CURR: "+curDate);
        		hlmng.eventsUpcoming.push(item);
        	}
        });	
	});
}]);