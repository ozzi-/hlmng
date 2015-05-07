var mediaModule = angular.module('media', []);


app.controller('MediaController', ['$scope', 'ToolService','$log','dataService', function($scope, ToolService,$log,dataService){
	var hlmng = this;
	$scope.uploadedMediaLink={};
	$scope.uploadedMediaID={};
	hlmng.redir=ToolService.redir;
	$scope.showUploadButton=false;
	$scope.showUploadPicture=false;
	$scope.showUploadErrorCode=false;
	$scope.uploadErrorCode={};
	$scope.uploadFile = function(file){
		$log.log("Starting upload in media controller");
        ToolService.uploadFile(file).then(function(data){
        	$scope.uploadedMediaLink=data.link;
        	$scope.uploadedMediaID=data.mediaID;
        	$scope.showUploadPicture=true;
        	$scope.showUploadErrorCode=false;
        	dataService.dataObj =data.mediaID; 
        }, function (reason){
        	$scope.showUploadPicture=false;
        	$scope.showUploadErrorCode=true;
        	$scope.uploadErrorCode=reason;
        });
    };
}]);