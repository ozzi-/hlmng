var newsModule = angular.module('news', []);

app.controller('NewsListController', ['$http','RestService','$stateParams', function($http,RestService,$stateParams){
	var hlmng = this;
	hlmng.newsList = [];
	
	RestService.list('news').then(function(data){
	    $.each(data, function(i, item){
	    	hlmng.newsList.push(item);	 
	    });
	});
}]);

app.controller('NewsNewController', ['$http','RestService','ToolService','$stateParams','dataService', function($http,RestService,ToolService,$stateParams,dataService){
	var hlmng = this;
	hlmng.news={}; 
	hlmng.media={};
	hlmng.redir=ToolService.redir;
	hlmng.postNews = RestService.post;
	hlmng.postAndRedir = function() {  
		hlmng.news.eventIDFK=$stateParams.eventId;
		hlmng.news.mediaIDFK=dataService.dataObj;
		dataService.dataObj=0;
		hlmng.postNews(hlmng.news,'news').then(function(data){
			hlmng.news=data;
			//hlmng.redir('/eventactive/'+$stateParams.eventId+'/eventroom/'+hlmng.eventroom.eventRoomID); // TODO change redir, on error show msg.
		}); 
	};
}]);

app.controller('NewsIdController', ['$http','$state','$stateParams','RestService','ToolService', function($http,$state, $stateParams,RestService,ToolService){
	var hlmng = this;
	
	hlmng.news = {};	
	RestService.get($stateParams.newsId,'news').then(function(data){
		hlmng.news=data;
	});
	
	hlmng.putNews = RestService.put;
	hlmng.deleteNews = RestService.del;
	hlmng.redir=ToolService.redir;
}]);

