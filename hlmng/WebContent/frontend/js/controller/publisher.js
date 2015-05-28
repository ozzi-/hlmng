var publisherModule = angular.module('publisher', ['ezfb']);

publisherModule.config(function (ezfbProvider) {
	ezfbProvider.setLocale('en_US');
});
	
publisherModule.config(function (ezfbProvider) {
	ezfbProvider.setInitParams({
		appId: appId,
	    version: 'v2.2' 
	});
});

publisherModule.controller('PublisherController', ['$http','ezfb','RestService','ToolService','dataService', function($http,ezfb,RestService,ToolService,dataService){
	var hlmng = this;
	hlmng.post = RestService.post;
	hlmng.loginStatusFB="";
	hlmng.appId="";
	hlmng.pageId="";

	ezfb.Event.subscribe('auth.statusChange', function (statusRes) {
		hlmng.loginStatusFB = statusRes.status;
	});
	
	 hlmng.login = function () {
		 $log.log("Login button");
		 //ezfb.login(null, {scope: 'email,user_likes'});		 
	     ezfb.login(function (res) {
	     }, {scope: 'publish_actions, manage_pages, publish_pages'}) .then(function (res) {
	 		FB.api('/' + hlmng.pageId, {fields: 'access_token'}, function(resp) {
				if(resp.access_token) {
					alert(resp.access_token);
				}
	 		});
	     });
	 };
	 
	 hlmng.logout = function () {
		 ezfb.logout();
	 };

	 updateLoginStatus();
	 
	 function updateLoginStatus () {
	    return ezfb.getLoginStatus()
	      .then(function (res) {
	    	  hlmng.loginStatusFB = res.status;
	  		FB.api('/' + hlmng.pageId, {fields: 'access_token'}, function(resp) {
	  			alert(resp.access_token);
	  		});
	      });
	  }
	
	 RestService.list('settings').then(function(data){
			hlmng.appId=data.appId;
			hlmng.pageId=data.pageId;
		});
	
	hlmng.postFB = function(socialobj,socialCtrl,postMedia) { 
		if(postMedia==false){
			socialobj.media="";
		}
		hlmng.post(socialobj,'publish/twitter').then(function(data){
			socialCtrl.setPublished(socialobj,data.publishedlink,data.publisher);
		});
		hlmng.post(socialobj,'publish/facebook').then(function(data){
			socialCtrl.setPublished(socialobj,data.publishedlink,data.publisher);
		});

	};
	
	hlmng.postBoth = function(socialobj,socialCtrl,postMedia) { 
		if(postMedia==false){
			socialobj.media="";
		}
		hlmng.post(socialobj,'publish/facebook').then(function(data){
			socialCtrl.setPublished(socialobj,data.publishedlink,data.publisher);
		});
	};
		
	hlmng.postTweet = function(socialobj,socialCtrl,postMedia) { 
		if(postMedia==false){
			socialobj.media="";
		}
		hlmng.post(socialobj,'publish/twitter').then(function(data){
			socialCtrl.setPublished(socialobj,data.publishedlink,data.publisher);
		});
	};
}]);
