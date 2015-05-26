var facebookModule = angular.module('facebook', []);

function statusChangeCallback(response) {
	console.log('statusChangeCallback');
	console.log(response);
    if (response.status === 'connected') {
      document.getElementById('status').innerHTML = 'Logged in';
      document.getElementById('fb-root').style.visibility="hidden";
      document.getElementById('fb-root').style.display="none";
    } else if (response.status === 'not_authorized') {
        document.getElementById('status').innerHTML = 'Please log into this app first (on facebook.com)';
        document.getElementById('fb-root').style.visibility="visible";
        document.getElementById('fb-root').style.display="inline";
    } else {
        document.getElementById('status').innerHTML = '';
        document.getElementById('fb-root').style.visibility="visible";
        document.getElementById('fb-root').style.display="inline";
    }
};

// This function is called when someone finishes with the Login
// Button.  See the onlogin handler attached to it in the sample
// code below.
function checkLoginState() {
  FB.getLoginStatus(function(response) {
    statusChangeCallback(response);
  });
}

window.fbAsyncInit = function() {
    FB.init({
      appId      : '1589703351304338',
      cookie     : true,  // enable cookies to allow the server to access the session
      xfbml      : true,  // parse social plugins on this page
      version    : 'v2.2' // use version 2.2
    });

    FB.getLoginStatus(function(response) {
      statusChangeCallback(response);
    });
 };

  // Load the SDK asynchronously
  (function(d, s, id) {
    var js, fjs = d.getElementsByTagName(s)[0];
    if (d.getElementById(id)) return;
    js = d.createElement(s); js.id = id;
    js.src = "https://connect.facebook.net/en_US/sdk.js";
    fjs.parentNode.insertBefore(js, fjs);
  }(document, 'script', 'facebook-jssdk'));

  
  
facebookModule.controller('FacebookController', ['$http','$window','$scope','RestService','ToolService','$stateParams', function($http,$window,$scope,RestService,ToolService,$stateParams){
	
	$window.fbAsyncInit();
	var hlmng = this;
	hlmng.appId="";
	hlmng.pageId="";
	
	
	
	RestService.list('settings').then(function(data){
		hlmng.appId=data.appId;
		hlmng.pageId=data.pageId;
	});
  	
	hlmng.postMessage = function (socialCtrl, social) {
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
	    				socialCtrl.setPublished(parent.socialobj,fbLink);
	    			}
				}
	        );
	      }
		});
	};
	
	  hlmng.postImageAndText = function (socialCtrl, social){
		var messagetext = social.text;
		var messageimage= social.media;
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
	    				socialCtrl.setPublished(parent.socialobj,fbLink);
	      			}
	          });
	      }
	    });
	  };
}]);
