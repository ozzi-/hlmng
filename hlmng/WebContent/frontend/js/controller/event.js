var eventModule = angular.module('event', []);


eventModule.controller('EventNewController', ['$http','RestService','ToolService','$scope','$filter', function($http,RestService,ToolService,$scope,$filter){
	var hlmng = this;
	hlmng.event={};
	hlmng.postEvent = RestService.post;
	hlmng.postAndRedir = function() {  
		hlmng.postEvent(hlmng.event,'event').then(function(data){
			hlmng.event=data;
			ToolService.redir('event.active.overview',{eventId:hlmng.event.eventID});
		});
	};
}]);



eventModule.controller('EventOverviewController', ['$http','$stateParams','RestService', function($http, $stateParams,RestService){
	var hlmng = this;
	hlmng.eventroomsCount = 0;
	hlmng.eventitemsCount = 0;
	hlmng.qrcodeCount = 0;
	hlmng.socialCount = 0;
	hlmng.newsCount = 0;
	hlmng.socialPendingCount = 0;
	
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
	
	RestService.list('qrcode').then(function(data){
	    $.each(data, function(i, item){
	    	if(item.eventIDFK==$stateParams.eventId){
	    		hlmng.qrcodeCount++;
	    	}
	    });
	});
	
	RestService.list('news').then(function(data){
	    $.each(data, function(i, item){
	    	if(item.eventIDFK==$stateParams.eventId){
	    		hlmng.newsCount++;
	    	}
	    });
	});
	
	RestService.list('social').then(function(data){
	    $.each(data, function(i, item){
	    	if(item.eventIDFK==$stateParams.eventId){
	    		if(item.status=="pending"){
	    			hlmng.socialPendingCount++;	
	    		}
	    		hlmng.socialCount++;
	    	}
	    });
	});

}]);

eventModule.controller('EventIdController', ['$http','$stateParams','RestService', function($http, $stateParams,RestService){
	var hlmng = this;
	hlmng.form = {};
	
	RestService.get($stateParams.eventId,'event').then(function(data){
		hlmng.event=data;
	});

	hlmng.putEvent = RestService.put;
}]);

eventModule.controller('EventListController', ['$http','RestService', function($http,RestService){
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