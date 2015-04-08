var app = angular.module('hlmngApp',['ngRoute','speaker','event'])
var apiUrl = 'http://localhost:8080/hlmng/rest/';


app.factory('PutService', ['$log','$http', function ($log,$http) {
    return {
        put: function (event) {
    		$http.put(apiUrl+'event/'+event.eventID, event).success(function(data) {
    			$log.log('put event successfully'); // TODO change so its not only ffor event -> pass string with type
    		});
        }
    }
}]);




app.config(['$routeProvider', function($routeProvider){	
	$routeProvider.
	when('/', {
		templateUrl: "template/index.html",
		controller: "IndexController",
		controllerAs: 'indexCtrl'
	}).
	when('/speakerlist', {
		templateUrl: "template/speaker/speaker-list.html",
		controller: "SpeakerListController",
		controllerAs: 'speakerListCtrl'
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




app.controller('IndexController', ['$http', function($http){
	$http.get(apiUrl+'backendlogin').success(function(data){
		if(data==='true'){
			// LOGGED IN 
		}else{
			// NOT LOGGED IN, WHAT DO 
			// TODO for all pages
		}
	});
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



app.controller('EventIdController', ['$http','$routeParams','PutService', function($http, $routeParams,PutService){
	var hlmng = this;
	hlmng.event = {};
	$http.get(apiUrl+'event/'+$routeParams.eventId).success(function(data){
		hlmng.event=data;
	});
	hlmng.putEvent = PutService.put;
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
