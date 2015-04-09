var app = angular.module('hlmngApp',['ngRoute','speaker','event'])
var apiUrl = 'http://localhost:8080/hlmng/rest/';


app.factory('RestService', ['$log','$http','$q', function ($log,$http,$q) {
    return {
        put: function (obj,objId,className) {
    		$http.put(apiUrl+className+'/'+objId, obj).success(function(data) {
    			$log.log('put '+className+' successfully');
    		}).error(function(data){
    			$log.log('couldn\'t put '+className+', ID='+objId);
    		});
        },
    	get: function (className, objId) {
            var deferred = $q.defer(); // we need the q lib -> deferred because else the view would receive the data too late
            $http({ method: "GET", url: apiUrl+className+'/'+objId })
            .success(function (data) {
				$log.log('get '+className+' successfully, ID='+objId);
                deferred.resolve(data);
            }).error(function (data) {
				$log.log('couldn\'t get '+className+', ID='+objId);
                deferred.reject(data);
            });
            return deferred.promise;
		},
    	list: function (className) {
            var deferred = $q.defer(); // we need the q lib -> deferred because else the view would receive the data too late
            $http({ method: "GET", url: apiUrl+className })
            .success(function (data) {
				$log.log('list '+className+' successfully');
                deferred.resolve(data);
            }).error(function (data) {
				$log.log('couldn\'t list '+className);
                deferred.reject(data);
            });
            return deferred.promise;
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
 	


app.controller('SpeakerListController', ['$http','RestService', function($http,RestService){
	var hlmng = this;
	hlmng.speakers = [];
	
	RestService.list('speaker').then(function(data){
		hlmng.speakers=data;
	});
	
}]);



app.controller('EventIdController', ['$http','$routeParams','RestService', function($http, $routeParams,RestService){
	var hlmng = this;

	hlmng.event = {};
	RestService.get('event',$routeParams.eventId).then(function(data){
		hlmng.event=data;
	});

	hlmng.putEvent = RestService.put;
}]);

app.controller('SpeakerIdController', ['$http','$routeParams','RestService', function($http, $routeParams,RestService){
	var hlmng = this;
	
	hlmng.speaker = {};	
	RestService.get('speaker',$routeParams.speakerId).then(function(data){
		hlmng.speaker=data;
	});

	hlmng.putSpeaker = RestService.put;
}]);

app.controller('EventListController', ['$http','RestService', function($http,RestService){
	var hlmng = this;
	hlmng.events = [];
	RestService.list('event').then(function(data){
		hlmng.events=data;
	});
}]);
