var eventModule = angular.module('event', []);


app.controller('EventNewController', ['$http','RestService','ToolService', function($http,RestService,ToolService){
	var hlmng = this;
	hlmng.event={};
	hlmng.postEvent = RestService.post;
	hlmng.redir=ToolService.redir;
	hlmng.postAndRedir = function() {  
		hlmng.postEvent(hlmng.event,'event').then(function(data){
			hlmng.event=data;
			hlmng.redir('/eventactive/'+hlmng.event.eventID+"/");
		});
	};
}]);



app.controller('EventOverviewController', ['$http','$stateParams','RestService', function($http, $stateParams,RestService){
	var hlmng = this;
	hlmng.eventroomsCount = 0;
	hlmng.eventitemsCount = 0;
	
	RestService.list('eventroom').then(function(data){
	    $.each(data, function(i, item){
	    	if(item.eventIDFK==$stateParams.eventId){
	    		hlmng.eventroomsCount++;
	    	}
	    });
	});
	
	RestService.list('eventitem').then(function(data){
	    $.each(data, function(i, item){
	    	if(item.eventIDFK==$stateParams.eventId){
	    		hlmng.eventitemsCount++;
	    	}
	    });
	});

}]);

app.controller('EventIdController', ['$http','$stateParams','RestService', function($http, $stateParams,RestService){
	var hlmng = this;
	hlmng.form = {};
	
	RestService.get($stateParams.eventId,'event').then(function(data){
		hlmng.event=data;
	});

	hlmng.putEvent = RestService.put;
}]);

app.controller('EventListController', ['$http','RestService', function($http,RestService){
	var hlmng = this;
	hlmng.eventsUpcoming = [];
	hlmng.eventsRunning = [];
	hlmng.eventsFinished = [];
	hlmng.events = []; // contains all
	
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
}]);