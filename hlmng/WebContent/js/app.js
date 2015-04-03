var app = angular.module('hlmngApp',['speaker']);
var apiUrl = 'http://localhost:8080/hlmng/rest/';

app.controller('SpeakerController', ['$http', function($http){

	var hlmng = this;
	hlmng.speakers = [];
	$http.get(apiUrl+'speaker').success(function(data){
		hlmng.speakers=data;
	});
}]);
