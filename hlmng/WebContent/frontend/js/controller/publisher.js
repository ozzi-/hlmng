var publisherModule = angular.module('publisher', ['ezfb']);

publisherModule.config(function (ezfbProvider) {
	ezfbProvider.setLocale('en_US');
});
	
publisherModule.config(function (ezfbProvider) {
	ezfbProvider.setInitParams({
		appId: facebookAppId,
	    version: 'v2.2' 
	});
});

publisherModule.controller('PublisherController', ['$http','$log','ezfb','RestService','ToolService','dataService', function($http,$log,ezfb,RestService,ToolService,dataService){
	var hlmng = this;
	hlmng.post = RestService.post;
	hlmng.loginStatusFB="";
	hlmng.publishMediaToo="true";
	hlmng.thisElem;
	hlmng.pageId="";

	ezfb.Event.subscribe('auth.statusChange', function (statusRes) {
		 hlmng.loginStatusFB = statusRes.status;
	});
	
	 hlmng.login = function () {
		 $log.log("Login button");
	     ezfb.login(function (res) {
	     }, {scope: 'publish_actions, manage_pages, publish_pages'}) .then(function (res) {
	    	 updateLoginStatus();
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
	    	if(hlmng.loginStatusFB=="connected"){
	    		FB.api('/' + hlmng.pageId, {fields: 'access_token'}, function(resp) {
	    			hlmng.post(resp,'publish/facebook/updatetoken').then(function(data){
	    				$log.log(data);
	    			});
	    		});	    		
	    	}
	      });
	  }
	
	 RestService.list('settings').then(function(data){
			hlmng.pageId=data.pageId;
		});
	
	hlmng.postFB = function(socialobj,socialCtrl) { 
		hlmng.loading=true;
		if(hlmng.publishMediaToo==="false"){
			socialobj.media="";
		}
		hlmng.post(socialobj,'publish/facebook').then(function(data){
			socialCtrl.setPublished(socialobj);
			hlmng.loading=false;
		});

	};
	
	hlmng.postBoth = function(socialobj,socialCtrl) { 
		hlmng.loading=true;
		if(hlmng.publishMediaToo==="false"){
			socialobj.media="";
		}
		hlmng.post(socialobj,'publish/twitter').then(function(data){
			hlmng.post(socialobj,'publish/facebook').then(function(data2){
				socialCtrl.setPublished(socialobj);
				hlmng.loading=false;
			});
		});
	};
		
	hlmng.postTweet = function(socialobj,socialCtrl) { 
		hlmng.loading=true;
		if(hlmng.publishMediaToo==="false"){
			socialobj.media="";
		}
		hlmng.post(socialobj,'publish/twitter').then(function(data){
			socialCtrl.setPublished(socialobj);
			hlmng.loading=false;
		});
	};
}]);
