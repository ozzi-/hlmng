var mediaModule = angular.module('media', []);


app.controller('MediaController', ['$scope', 'ToolService','$log', function($scope, ToolService,$log){
	var hlmng = this;
	$scope.uploadedMediaLink={};
	$scope.uploadedMediaID={};
	hlmng.redir=ToolService.redir;
	$scope.uploadFile = function(file){
		$log.log("Starting upload in media controller");
        ToolService.uploadFile(file).then(function(data){
        	$scope.uploadedMediaLink=data.link;
        	$scope.uploadedMediaID=data.mediaID;
        });
    };
}]);