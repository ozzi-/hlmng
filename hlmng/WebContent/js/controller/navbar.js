var eventModule = angular.module('navbar', []);

app.controller('NavBarController', ['$http','$state','$location','RestService','$stateParams', function($http,$state,$location,RestService,$stateParams){	
	var hlmng = this;
	hlmng.eventName={};

	hlmng.navMainContent = 
		[{	
	        label: 'Event List',
	        state: 'event.list'
	    },{
	        label: 'New Event',
	        state: 'event.new'
	    },{
	        label: 'Speaker List',
	        state: 'speaker.list'
	    },{
	        label: 'New Speaker',
	        state: 'speaker.new'
	    }];
	
	hlmng.navEventActiveContent = 
		[{	
	        label: 'Event Room',
	        state: 'event.active.eventroom',
	        children: [{
	            label: 'Event Room List',
	            state: 'event.active.eventroom.list'
	        },{
	            label: 'New Event Room',
	            state: 'event.active.eventroom.new'
	        }]
	    },{
	        label: 'Event Item',
	        state: 'event.active.eventitem',
	        children: [{
	            label: 'Event Item List',
	            state: 'event.active.eventitem.list'
	        },{
	            label: 'New Event Item',
	            state: 'event.active.eventitem.new'
	        }]
	    },{
	        label: 'QR Code',
	        state: 'event.active.qrcode',
	        children: [{
	            label: 'QR Code List',
	            state: 'event.active.qrcode.list'
	        },{
	            label: 'New QR Code',
	            state: 'event.active.qrcode.new'
	        }]
	    },{
	    	label: 'What\'s Up',
	    	state: 'event.active.whatsup',
	        children: [{
	            label: 'Social Feed',
	            state: 'event.active.whatsup.social.list'
	        },{
	            label: 'News Feed',
	            state: 'event.active.whatsup.news.list'
	        },{
	            label: 'New News',
	            state: 'event.active.whatsup.news.new'
	        }]
	    },{
	    	label: 'Push',
	    	state: 'event.active.push',
	        children: [{
	            label: 'Push Feed',
	            state: 'event.active.push.list'
	        },{
	            label: 'New Push',
	            state: 'event.active.push.new'
	        }]
	    },{
	        label: 'Voting',
	        state: 'event.active.voting',
	        children: [{
	            label: 'Voting List',
	            state: 'event.active.voting.list'
	        },{
	            label: 'New Voting',
	            state: 'event.active.voting.new'
	        }]
	    }];
	
	hlmng.checkEventActive = function(){
		return ($state.includes("event.active"));
    };
	hlmng.getEventActive = function(){
		return ($stateParams.eventId);
    };
       
    if(hlmng.checkEventActive()){
    	hlmng.eventName=RestService.get(hlmng.getEventActive(),'event').then(function(data){
    		hlmng.eventName=data.name;
    	});    	
    }
}]);
