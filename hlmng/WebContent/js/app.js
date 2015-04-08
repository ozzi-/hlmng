var app = angular.module('hlmngApp',['ngRoute','speaker','event']);
var apiUrl = 'http://localhost:8080/hlmng/rest/';

app.config(['$routeProvider', function($routeProvider){	
	$routeProvider.
	when('/', {
		templateUrl: "template/index.html",
		controller: "IndexController"
	}).
	when('/speakerlist', {
		templateUrl: "template/speaker/speaker-list.html",
		controller: "SpeakerListController"
	}).
	when('/speaker/:speakerId', {
		templateUrl: "template/speaker/speaker-detail.html",
		controller: "SpeakerIdController"
	}).
	when('/eventlist', {
		templateUrl: "template/event/event-list.html",
		controller: "EventListController"
	}).
	otherwise({
		redirectTo: '/'
	});
}]);


app.controller('IndexController', ['$http', function($http){
}]);

app.controller('SpeakerListController', ['$http', function($http){
	var hlmng = this;
	hlmng.speakers = [];
	$http.get(apiUrl+'speaker').success(function(data){
		hlmng.speakers=data;
	}).error(function(data){
		// TODO better way?
		alert("API Call failed"); 
	});
}]);


app.controller('SpeakerIdController', ['$http','$routeParams', function($http, $routeParams){
	var hlmng = this;
	hlmng.speaker = {};
	$http.get(apiUrl+'speaker/'+$routeParams.speakerId).success(function(data){
		hlmng.speaker=data;
	});
}]);

app.controller('EventListController', ['$http', function($http){
	var hlmng = this;
	hlmng.events = [];
	$http.get(apiUrl+'event').success(function(data){
		hlmng.events=data;
	});
}]);
