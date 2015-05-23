var pushModule = angular.module('push', []);

pushModule.controller('PushListController', ['$http','RestService','$stateParams','ToolService', function($http,RestService,$stateParams,ToolService){
	var hlmng = this;
	hlmng.pushes = [];
	hlmng.pushesPaginated=[];
	hlmng.currentPage = 1;
	
	hlmng.updatePage = function(){
		ToolService.pagination(hlmng.pushes,hlmng);
	};

	RestService.list('push').then(function(data){
	    $.each(data, function(i, item){
	    	hlmng.pushes.push(item);	 
			hlmng.updatePage();
	    });
	});
}]);

pushModule.controller('PushNewController', ['$http','RestService','ToolService','$stateParams', function($http,RestService,ToolService,$stateParams){
	var hlmng = this;
	hlmng.push={}; 
	hlmng.postPush = RestService.post;
	hlmng.postAndRedir = function() {  
		hlmng.postPush(hlmng.push,'push').then(function(data){
			hlmng.push=data;
			ToolService.redir('push.id',{pushId: hlmng.push.pushID}); 
		}); 
	};
}]);
