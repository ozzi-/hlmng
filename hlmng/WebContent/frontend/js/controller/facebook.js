var facebookModule = angular.module('facebook', ['ezfb']);

facebookModule.config(function (ezfbProvider) {
	ezfbProvider.setLocale('en_US');
});
	
facebookModule.config(function (ezfbProvider) {
	ezfbProvider.setInitParams({
		appId: appId,
	    version: 'v2.2' 
	});
});
  
facebookModule.controller('FacebookController', ['$http','ezfb','$log','$window','$scope','RestService','ToolService','$stateParams', function($http,ezfb,$log,$window,$scope,RestService,ToolService,$stateParams){

	var hlmng = this;
	hlmng.appId="";
	hlmng.loginStatus="";
	hlmng.pageId="";
	

	ezfb.Event.subscribe('auth.statusChange', function (statusRes) {
		hlmng.loginStatus = statusRes.status;
	});
	
	 hlmng.login = function () {
		 $log.log("Login button");
		 //ezfb.login(null, {scope: 'email,user_likes'});		 
	     ezfb.login(function (res) {
	     }, {scope: 'publish_actions, manage_pages, publish_pages'}) .then(function (res) {
	     });
	 };
	 
	 hlmng.logout = function () {
		 ezfb.logout();
	 };

	 updateLoginStatus();
	 
	 function updateLoginStatus () {
	    return ezfb.getLoginStatus()
	      .then(function (res) {
	    	  hlmng.loginStatus = res.status;
	      });
	  }
	
	 /**
	   * For generating better looking JSON results
	   */
	  var autoToJSON = ['loginStatus', 'apiRes']; 
	  angular.forEach(autoToJSON, function (varName) {
	    $scope.$watch(varName, function (val) {
	      $scope[varName + 'JSON'] = JSON.stringify(val, null, 2);
	    }, true);
	  });


	RestService.list('settings').then(function(data){
		hlmng.appId=data.appId;
		hlmng.pageId=data.pageId;
	});
  	
	hlmng.postMessage = function (socialCtrl, social) {
		$log.log("Starting message post");
		var messagetext = social.text;
		var parent=this;
		this.socialobj=social;
		FB.api('/' + hlmng.pageId, {fields: 'access_token'}, function(resp) {
			if(resp.access_token) {
				FB.api('/' + hlmng.pageId + '/feed', 'post',{ message: messagetext, access_token: resp.access_token },
				function(response) {
	    			if(response.error){
	    				alert(response.error.message);
	    			}else{
	    				alert('Post successful!');
	    				var resArr= response.id.split("_");
	    				var fbLink= "https://facebook.com/permalink.php?story_fbid="+resArr[1]+"&id="+resArr[0];
	    				socialCtrl.setPublished(parent.socialobj,fbLink,"Facebook");
	    			}
				}
	        );
	      }
		});
	};
	
	  hlmng.postImageAndText = function (socialCtrl, social){
		$log.log("Starting message+image post");
		var parent=this;
		this.socialobj=social;
		var messagetext = social.text;
		var messageimage= social.media;
		// FB can't handle self signed cert's, so we removed the https rewrite for pub/media/* 
		messageimage=messageimage.replace("https","http"); 
		
		this.socialobj=social;
	    FB.api('/' + hlmng.pageId, {fields: 'access_token'}, function(resp) {
	      if(resp.access_token) {
	        FB.api('/' + hlmng.pageId + '/photos','post',
	          { 
	    		message: messagetext,
	    		url: messageimage,
	    		access_token: resp.access_token 
	    			},function(response) {
	      			if(response.error){
	      				alert(response.error.message);
	      			}else{
	      				alert('Post successful!');
	    				var resArr= response.id.split("_");
	    				var fbLink= "https://facebook.com/permalink.php?story_fbid="+resArr[1]+"&id="+resArr[0];
	    				socialCtrl.setPublished(parent.socialobj,fbLink,"Facebook");
	      			}
	          });
	      }
	    });
	  };
}]);
