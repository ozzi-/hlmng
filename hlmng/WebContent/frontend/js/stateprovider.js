var myapp = angular.module('stateprovider', ["ui.router"]); 
myapp.config(function($stateProvider, $urlRouterProvider){

	$urlRouterProvider.otherwise("/");
	$stateProvider.
    state('index', {
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
    // =====
    // MEDIA
    // =====
	state('uploadmedia', {
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
	state('medialist', {
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
	// =======
    // SPEAKER
    // =======
	state('speaker', {
		url: "/speaker",
	    abstract: true,
		views: {
			"content":{
				template: '<div ui-view="content"></div>',
			},
			"nav":{
				template: '<div ui-view="nav"></div>',
			}
		}
	}).
	state('speaker.list', {
		url: "/list",
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
	state('speaker.new', {
		url: "/new",
		views: {
			"content":{
				templateUrl: "template/speaker/speaker-new.html",
				controller: "SpeakerNewController",
				controllerAs: 'speakerNewCtrl'				
			},
			"nav":{
				templateUrl: "template/nav.html",
				controller: "NavBarController",
            	controllerAs: "navBarCtrl"
			}
		}
	}).
	state('speaker.id', {
		url: "/{speakerId:int}",
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
	// =====
	// EVENT
	// =====
	state('event', {
		url: "/event",
	    abstract: true,
		views: {
			"content":{
				template: '<div ui-view="content"></div>',
			},
			"nav":{
				template: '<div ui-view="nav"></div>',
			}
		}
	}).
	state('event.list', {
		url: "/list",
		views:{
			"content":{
				templateUrl: "template/event/event-list.html",
				controller: "EventListController",
				controllerAs: 'eventListCtrl'
			},
			"nav":{
				templateUrl: "template/nav.html",
				controller: "NavBarController",
            	controllerAs: "navBarCtrl"
			}
		}
	}).
	state('event.new', {
		url: "/new",
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
	state('event.id', {
		url: "/{eventId:int}",
		views:{
			"content":{
				templateUrl: "template/event/event-detail.html",
				controller: "EventIdController",
				controllerAs: 'eventIdCtrl'
			},
			"nav":{
				templateUrl: "template/nav.html",
				controller: "NavBarController",
            	controllerAs: "navBarCtrl"
			}
		}
	}).
	state('event.active', {
		url: "/active/{eventId:int}",
	    abstract: true,
		views: {
			"content":{
				template: '<div ui-view="content"></div>',
			},
			"nav":{
				template: '<div ui-view="nav"></div>',
			}
		}
	}).
	// ==============
	// EVENT OVERVIEW
	// ==============
	state('event.active.overview', {
		url: "/overview", 
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
	// ==========
	// EVENT ROOM
	// ==========
	state('event.active.eventroom', {
		url: "/eventroom",
	    abstract: true,
		views: {
			"content":{
				template: '<div ui-view="content"></div>',
			},
			"nav":{
				template: '<div ui-view="nav"></div>',
			}
		}
	}).
	state('event.active.eventroom.new', {
		url: "/new",
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
	state('event.active.eventroom.id', {
		url: "/{eventRoomId:int}",
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
	state('event.active.eventroom.list', {
		url: "/list",
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
	// ==========
	// EVENT ITEM
	// ==========
	state('event.active.eventitem', {
		url: "/eventitem",
	    abstract: true,
		views: {
			"content":{
				template: '<div ui-view="content"></div>',
			},
			"nav":{
				template: '<div ui-view="nav"></div>',
			}
		}
	}).
	state('event.active.eventitem.new', {
		url: "/new",
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
	state('event.active.eventitem.id', {
		url: "/{eventItemId:int}",
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
	state('event.active.eventitem.list', {
		url: "/list",
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
	// =======
	// QR CODE
	// =======
	state('event.active.qrcode', {
		url: "/qrcode",
	    abstract: true,
		views: {
			"content":{
				template: '<div ui-view="content"></div>',
			},
			"nav":{
				template: '<div ui-view="nav"></div>',
			}
		}
	}).
	state('event.active.qrcode.new', {
		url: "/new",
		views:{
			"content":{
				templateUrl: "template/qrcode/qrcode-new.html",
				controller: "QrcodeNewController",
				controllerAs: 'qrcodeNewCtrl'
			},
			"nav":{
            	templateUrl: "template/nav.html",
            	controller: "NavBarController",
            	controllerAs: "navBarCtrl"
			}
		}
	}). 	
	state('event.active.qrcode.id', {
		url: "/{qrcodeId:int}",
		views:{
			"content":{
				templateUrl: "template/qrcode/qrcode-detail.html",
				controller: "QrcodeIdController",
				controllerAs: 'qrcodeIdCtrl'
			},
			"nav":{
            	templateUrl: "template/nav.html",
            	controller: "NavBarController",
            	controllerAs: "navBarCtrl"
			}
		}
	}).
	state('event.active.qrcode.list', {
		url: "/list",
		views:{
			"content":{
				templateUrl: "template/qrcode/qrcode-list.html",
				controller: "QrcodeListController",
				controllerAs: 'qrCodeListCtrl'
			},
			"nav":{
            	templateUrl: "template/nav.html",
            	controller: "NavBarController",
            	controllerAs: "navBarCtrl"
			}
		}
	}).
	// ********
	// WHATS UP
	// ********
	state('event.active.whatsup', {
		url: "/whatsup",
	    abstract: true,
		views: {
			"content":{
				template: '<div ui-view="content"></div>',
			},
			"nav":{
				template: '<div ui-view="nav"></div>',
			}
		}
	}).
	state('event.active.whatsup.news', {
		url: "/news",
	    abstract: true,
		views: {
			"content":{
				template: '<div ui-view="content"></div>',
			},
			"nav":{
				template: '<div ui-view="nav"></div>',
			}
		}
	}).
	state('event.active.whatsup.news.list', {
		url: "/list",
		views:{
			"content":{
				templateUrl: "template/whatsup/news/news-list.html",
				controller: "NewsListController",
				controllerAs: 'newsListCtrl'
			},
			"nav":{
            	templateUrl: "template/nav.html",
            	controller: "NavBarController",
            	controllerAs: "navBarCtrl"
			}
		}
	}).
	state('event.active.whatsup.news.id', {
		url: "/{newsId:int}",
		views:{
			"content":{
				templateUrl: "template/whatsup/news/news-detail.html",
				controller: "NewsIdController",
				controllerAs: 'newsIdCtrl'
			},
			"nav":{
            	templateUrl: "template/nav.html",
            	controller: "NavBarController",
            	controllerAs: "navBarCtrl"
			}
		}
	}).
	state('event.active.whatsup.news.new', {
		url: "/new",
		views:{
			"content":{
				templateUrl: "template/whatsup/news/news-new.html",
				controller: "NewsNewController",
				controllerAs: 'newsNewCtrl'
			},
			"nav":{
            	templateUrl: "template/nav.html",
            	controller: "NavBarController",
            	controllerAs: "navBarCtrl"
			}
		}
	}).
	state('event.active.whatsup.social', {
		url: "/social",
	    abstract: true,
		views: {
			"content":{
				template: '<div ui-view="content"></div>',
			},
			"nav":{
				template: '<div ui-view="nav"></div>',
			}
		}
	}).
	state('event.active.whatsup.social.id', {
		url: "/{socialId:int}",
		views:{
			"content":{
				templateUrl: "template/whatsup/social/social-detail.html",
				controller: "SocialIdController",
				controllerAs: 'socialIdCtrl'
			},
			"nav":{
            	templateUrl: "template/nav.html",
            	controller: "NavBarController",
            	controllerAs: "navBarCtrl"
			}
		}
	}).
	state('event.active.whatsup.social.list', {
		url: "/list",
		views:{
			"content":{
				templateUrl: "template/whatsup/social/social-list.html",
				controller: "SocialListController",
				controllerAs: 'socialListCtrl'
			},
			"nav":{
            	templateUrl: "template/nav.html",
            	controller: "NavBarController",
            	controllerAs: "navBarCtrl"
			}
		}
	}).
	// ****
	// PUSH
	// ****
	state('push', {
		url: "/push",
	    abstract: true,
		views: {
			"content":{
				template: '<div ui-view="content"></div>',
			},
			"nav":{
				template: '<div ui-view="nav"></div>',
			}
		}
	}).
	state('push.list', {
		url: "/list",
		views:{
			"content":{
				templateUrl: "template/push/push-list.html",
				controller: "PushListController",
				controllerAs: 'pushListCtrl'
			},
			"nav":{
            	templateUrl: "template/nav.html",
            	controller: "NavBarController",
            	controllerAs: "navBarCtrl"
			}
		}
	}).
	state('push.new', {
		url: "/new",
		views:{
			"content":{
				templateUrl: "template/push/push-new.html",
				controller: "PushNewController",
				controllerAs: 'pushNewCtrl'
			},
			"nav":{
            	templateUrl: "template/nav.html",
            	controller: "NavBarController",
            	controllerAs: "navBarCtrl"
			}
		}
	}).
	// ******
	// VOTING
	// ******
	state('event.active.voting', {
		url: "/voting",
	    abstract: true,
		views: {
			"content":{
				template: '<div ui-view="content"></div>',
			},
			"nav":{
				template: '<div ui-view="nav"></div>',
			}
		}
	}).	
	state('event.active.voting.list', {
		url: "/list",
		views:{
			"content":{
				templateUrl: "template/voting/voting-list.html",
				controller: "VotingListController",
				controllerAs: 'votingListCtrl'
			},
			"nav":{
            	templateUrl: "template/nav.html",
            	controller: "NavBarController",
            	controllerAs: "navBarCtrl"
			}
		}
	}).
	state('event.active.voting.new', {
		url: "/new",
		views:{
			"content":{
				templateUrl: "template/voting/voting-new.html",
				controller: "VotingNewController",
				controllerAs: 'votingNewCtrl'
			},
			"nav":{
            	templateUrl: "template/nav.html",
            	controller: "NavBarController",
            	controllerAs: "navBarCtrl"
			}
		}
	}).
	state('event.active.voting.id', {
		url: "/{votingId:int}",
		views:{
			"content":{
				templateUrl: "template/voting/voting-detail.html",
				controller: "VotingIdController",
				controllerAs: 'votingIdCtrl'
			},
			"nav":{
            	templateUrl: "template/nav.html",
            	controller: "NavBarController",
            	controllerAs: "navBarCtrl"
			}
		}
	});
});
