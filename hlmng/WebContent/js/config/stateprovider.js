var myapp = angular.module('stateprovider', ["ui.router"]); 
myapp.config(function($stateProvider, $urlRouterProvider){

	$urlRouterProvider.otherwise("/");
	
	$stateProvider.
    state('/', {
        url: "/",
        views: { 
        	"content": {
        		templateUrl: "template/index.html"
        	},
            "nav": {
            	templateUrl: "template/nav.html",
            	controller: "NavBarController",
            	controllerAs: "navBarCtrl"
            }
        }
    }).
	state('/uploadmedia', {
		url: "/uploadmedia",
		views: {
			"content":{
				controller: "MediaController",
				controllerAs: 'mediaCtrl',
				templateUrl: "template/media/upload.html"
			},
			"nav":{
            	templateUrl: "template/nav.html",
            	controller: "NavBarController",
            	controllerAs: "navBarCtrl"
			}
		}
	}).
	state('/medialist/', {
		url: "/medialist",
		views: {
			"content":{
				controller: "MediaController",
				controllerAs: 'mediaCtrl',
				templateUrl: "template/media/media-list.html"
			},
			"nav":{
            	templateUrl: "template/nav.html",
            	controller: "NavBarController",
            	controllerAs: "navBarCtrl"
			}
		}
	}).	
	state('/speakerlist', {
		url: "/speakerlist",
		views: {
			"content":{
				templateUrl: "template/speaker/speaker-list.html",
				controller: "SpeakerListController",
				controllerAs: 'speakerListCtrl'							
			},
			"nav":{
            	templateUrl: "template/nav.html",
            	controller: "NavBarController",
            	controllerAs: "navBarCtrl"
			}
		}
	}).
	state('/newspeaker', {
		url: "/newspeaker",
		views: {
			"content":{
				templateUrl: "template/speaker/speaker-new.html",
				controller: "SpeakerNewController",
				controllerAs: 'speakerNewCtrl'				
			},
			"nav":{
				templateUrl: "template/nav.html"
			}
		}
	}).
	state('/speaker/:speakerId', {
		url: "/speaker/:speakerId",
		views:{
			"content":{
				templateUrl: "template/speaker/speaker-detail.html",
				controller: "SpeakerIdController",
				controllerAs: 'speakerIdCtrl'				
			},
			"nav":{
            	templateUrl: "template/nav.html",
            	controller: "NavBarController",
            	controllerAs: "navBarCtrl"
			}
		}
	}).
	state('/eventlist', {
		url: "/eventlist",
		views:{
			"content":{
				templateUrl: "template/event/event-list.html",
				controller: "EventListController",
				controllerAs: 'eventListCtrl'
			},
			"nav":{
				templateUrl: "template/nav.html"
			}
		}
	}).
	state('/newevent', {
		url: "/newevent",
		views:{
			"content":{
				templateUrl: "template/event/event-new.html",
				controller: "EventNewController",
				controllerAs: 'eventNewCtrl'
			},
			"nav":{
            	templateUrl: "template/nav.html",
            	controller: "NavBarController",
            	controllerAs: "navBarCtrl"
			}
		}
	}).
	state('/event/:eventId', {
		url: "/event/:eventId",
		views:{
			"content":{
				templateUrl: "template/event/event-detail.html",
				controller: "EventIdController",
				controllerAs: 'eventIdCtrl'
			},
			"nav":{
				templateUrl: "template/nav.html"
			}
		}
	}).
	state('/eventactive/', {
		url: "/eventactive/:eventId/",
		views:{
			"content":{
				templateUrl: "template/event/event-overview.html",
				controller: "EventOverviewController",
				controllerAs: "eventOverviewCtrl"
			},
			"nav":{
            	templateUrl: "template/nav.html",
            	controller: "NavBarController",
            	controllerAs: "navBarCtrl"
			}
		}
	}).
	state('/eventactive/:eventId/news-new', {
		// ..
	}).
	state('/eventactive/neweventroom', {
		url: "/eventactive/:eventId/neweventroom",
		views:{
			"content":{
				templateUrl: "template/eventroom/eventroom-new.html",
				controller: "EventRoomNewController",
				controllerAs: 'eventRoomNewCtrl'
			},
			"nav":{
				templateUrl: "template/nav.html",
            	controller: "NavBarController",
            	controllerAs: "navBarCtrl"
			}
		}
	}).
	state('/eventactive/eventroom', {
		url: "/eventactive/:eventId/eventroom/:eventRoomId",
		views:{
			"content":{
				templateUrl: "template/eventroom/eventroom-detail.html",
				controller: "EventRoomIdController",
				controllerAs: 'eventRoomIdCtrl'
			},
			"nav":{
            	templateUrl: "template/nav.html",
            	controller: "NavBarController",
            	controllerAs: "navBarCtrl"
			}
		}
	}). 
	state('/eventactive/eventroomlist', {
		url: "/eventactive/:eventId/eventroomlist",
		views:{
			"content":{
				templateUrl: "template/eventroom/eventroom-list.html",
				controller: "EventRoomListController",
				controllerAs: 'eventRoomListCtrl'
			},
			"nav":{
            	templateUrl: "template/nav.html",
            	controller: "NavBarController",
            	controllerAs: "navBarCtrl"
			}
		}
	}). 
	// eventItem
	state('/eventactive/neweventitem', {
		url: "/eventactive/:eventId/neweventitem",
		views:{
			"content":{
				templateUrl: "template/eventitem/eventitem-new.html",
				controller: "EventItemNewController",
				controllerAs: 'eventItemNewCtrl'
			},
			"nav":{
				templateUrl: "template/nav.html",
            	controller: "NavBarController",
            	controllerAs: "navBarCtrl"
			}
		}
	}).
	state('/eventactive/eventitem', {
		url: "/eventactive/:eventId/eventitem/:eventItemId",
		views:{
			"content":{
				templateUrl: "template/eventitem/eventitem-detail.html",
				controller: "EventItemIdController",
				controllerAs: 'eventItemIdCtrl'
			},
			"nav":{
            	templateUrl: "template/nav.html",
            	controller: "NavBarController",
            	controllerAs: "navBarCtrl"
			}
		}
	}). 
	state('/eventactive/eventitemlist', {
		url: "/eventactive/:eventId/eventitemlist",
		views:{
			"content":{
				templateUrl: "template/eventitem/eventitem-list.html",
				controller: "EventItemListController",
				controllerAs: 'eventItemListCtrl'
			},
			"nav":{
            	templateUrl: "template/nav.html",
            	controller: "NavBarController",
            	controllerAs: "navBarCtrl"
			}
		}
	}). 
	state('/eventactive/qrcodelist', {
		url: "/eventactive/:eventId/qrcodelist",
		views:{
			"content":{
				templateUrl: "template/qrcode/qrcode-list.html",
				controller: "QrCodeListController",
				controllerAs: 'qrCodeListCtrl'
			},
			"nav":{
            	templateUrl: "template/nav.html",
            	controller: "NavBarController",
            	controllerAs: "navBarCtrl"
			}
		}
	}). 	
	state('/eventactive/:eventId/social', {
		// ..
	}).
	state('/eventactive/:eventId/newpush', {
		// ..
	}).
	state('/eventactive/:eventId/pushlist', {
		// ..
	}).
	state('/eventactive/:eventId/newvote', {
		// ..
	}).
	state('/eventactive/:eventId/votelist', {
		// ..
	}).
	state('/eventactive/:eventId/vote/:voteId', {
		// ..
	}).
	state('/eventactive/:eventId/vote/:voteId/statistics', {
		// ..
	}).
	state('/eventactive/:eventId/newqrcode', {
		// ..
	}).
	state('/eventactive/:eventId/qrcodelist', {
		// ..
	});
});
