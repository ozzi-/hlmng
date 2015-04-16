angular.module('hlmngApp').config(['$routeProvider', function($routeProvider){	
	$routeProvider.
	when('/', {
		templateUrl: "template/index.html",
		controller: "IndexController",
		controllerAs: 'indexCtrl',
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
	when('/event/:eventId', {
		templateUrl: "template/event/event-detail.html",
		controller: "EventIdController",
		controllerAs: 'eventIdCtrl'
	}).
	otherwise({
		redirectTo: '/'
	});
}]);
