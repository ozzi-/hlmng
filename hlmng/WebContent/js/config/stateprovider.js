	
angular.module('hlmngApp').config(function($stateProvider, $urlRouterProvider) {

	$urlRouterProvider.otherwise("/");


	$stateProvider.
	state('/', {
		url: "/",
		templateUrl: "template/index.html",
	}).	
	state('/uploadmedia', {
		url: "/uploadmedia",
		templateUrl: "template/media/upload.html",
		controller: "MediaController",
		controllerAs: 'mediaCtrl',
	}).
	state('/medialist/', {
		url: "/medialist",
		templateUrl: "template/media/media-list.html",
		controller: "MediaController",
		controllerAs: 'mediaCtrl',
	}).	
	state('/speakerlist', {
		url: "/speakerlist",
		templateUrl: "template/speaker/speaker-list.html",
		controller: "SpeakerListController",
		controllerAs: 'speakerListCtrl'
	}).
	state('/newspeaker', {
		url: "/newspeaker",
		templateUrl: "template/speaker/speaker-new.html",
		controller: "SpeakerNewController",
		controllerAs: 'speakerNewCtrl'
	}).
	state('/speaker/:speakerId', {
		url: "/speaker/:speakerId",
		templateUrl: "template/speaker/speaker-detail.html",
		controller: "SpeakerIdController",
		controllerAs: 'speakerIdCtrl'
	}).
	state('/eventlist', {
		url: "/eventlist",
		templateUrl: "template/event/event-list.html",
		controller: "EventListController",
		controllerAs: 'eventListCtrl'
	}).
	state('/newevent', {
		url: "/newevent",
		templateUrl: "template/event/event-new.html",
		controller: "EventNewController",
		controllerAs: 'eventNewCtrl'
	}).
	state('/event/:eventId', {
		url: "/event/:eventId",
		templateUrl: "template/event/event-detail.html",
		controller: "EventIdController",
		controllerAs: 'eventIdCtrl'
	}).
	state('/eventactive/:eventId/news-new', {
		// ..
	}).
	state('/eventactive/neweventroom', {
		url: "/eventactive/:eventId/neweventroom",
		templateUrl: "template/eventroom/eventroom-new.html",
		controller: "EventRoomNewController",
		controllerAs: 'eventRoomNewCtrl'
	}).
	state('/eventactive/eventroom', {
		url: "/eventactive/:eventId/eventroom/:eventRoomId",
		templateUrl: "template/eventroom/eventroom-detail.html",
		controller: "EventRoomIdController",
		controllerAs: 'eventRoomIdCtrl'
	}). 
	state('/eventactive/eventroomlist', {
		url: "/eventactive/:eventId/eventroomlist",
		templateUrl: "template/eventroom/eventroom-list.html",
		controller: "EventRoomListController",
		controllerAs: 'eventRoomListCtrl'
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
