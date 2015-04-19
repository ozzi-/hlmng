angular.module('hlmngApp').config(['$routeProvider', function($routeProvider){	
	$routeProvider.
	when('/', {
		templateUrl: "template/index.html",
	}).	
	when('/uploadmedia', {
		templateUrl: "template/media/upload.html",
		controller: "MediaController",
		controllerAs: 'mediaCtrl',
	}).
	when('/medialist/', {
		templateUrl: "template/media/media-list.html",
		controller: "MediaController",
		controllerAs: 'mediaCtrl',
	}).	
	when('/speakerlist', {
		templateUrl: "template/speaker/speaker-list.html",
		controller: "SpeakerListController",
		controllerAs: 'speakerListCtrl'
	}).
	when('/newspeaker', {
		templateUrl: "template/speaker/speaker-new.html",
		controller: "SpeakerNewController",
		controllerAs: 'speakerNewCtrl'
	}).
	when('/speaker/:speakerId', {
		templateUrl: "template/speaker/speaker-detail.html",
		controller: "SpeakerIdController",
		controllerAs: 'speakerIdCtrl'
	}).
	when('/eventlist', {
		templateUrl: "template/event/event-list.html",
		controller: "EventListController",
		controllerAs: 'eventListCtrl'
	}).
	when('/newevent', {
		templateUrl: "template/event/event-new.html",
		controller: "EventNewController",
		controllerAs: 'eventNewCtrl'
	}).
	when('/event/:eventId', {
		templateUrl: "template/event/event-detail.html",
		controller: "EventIdController",
		controllerAs: 'eventIdCtrl'
	}).
	when('/eventactive/:eventId/newnews', {
		// ..
	}).
	when('/eventactive/:eventId/social', {
		// ..
	}).
	when('/eventactive/:eventId/newpush', {
		// ..
	}).
	when('/eventactive/:eventId/pushlist', {
		// ..
	}).
	when('/eventactive/:eventId/newvote', {
		// ..
	}).
	when('/eventactive/:eventId/votelist', {
		// ..
	}).
	when('/eventactive/:eventId/vote/:voteId', {
		// ..
	}).
	when('/eventactive/:eventId/vote/:voteId/statistics', {
		// ..
	}).
	when('/eventactive/:eventId/newqrcode', {
		// ..
	}).
	when('/eventactive/:eventId/qrcodelist', {
		// ..
	}).
	otherwise({
		redirectTo: '/'
	});
}]);
