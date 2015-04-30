var mediaModule = angular.module('media', []);


app.controller('MediaController', ['$scope', 'ToolService','$log','dataService', function($scope, ToolService,$log,dataService){
	var hlmng = this;
	$scope.uploadedMediaLink={};
	$scope.uploadedMediaID={};
	hlmng.redir=ToolService.redir;
	$scope.uploadFile = function(file,saveto){
		$log.log("Starting upload in media controller");
        ToolService.uploadFile(file).then(function(data){
        	$scope.uploadedMediaLink=data.link;
        	$scope.uploadedMediaID=data.mediaID;
        	dataService.dataObj =data.mediaID; 
        });
    };
}]);