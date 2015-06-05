var mediaModule = angular.module('media', []);


mediaModule.controller('MediaController', ['$scope', 'ToolService','$log','dataService', function($scope, ToolService,$log,dataService){
	var hlmng = this;
	
	$scope.uploadedMediaLink={};
	$scope.uploadedMediaID={};
	$scope.uploadErrorCode={};
	
	hlmng.redir=ToolService.redir;
	$scope.showUploadButton=false;
	$scope.showUploadPicture=false;
	$scope.showUploadErrorCode=false;
	$scope.loading=false;
	
	$scope.uploadFile = function(file){
		$scope.loading=true;
		$log.log("Starting upload in media controller");
        ToolService.uploadFile(file).then(function(data){
        	$scope.uploadedMediaLink=data.link;
        	$scope.uploadedMediaID=data.mediaID;
        	$scope.showUploadPicture=true;
        	$scope.showUploadErrorCode=false;
        	dataService.dataObj =data.mediaID;
        	dataService.dataObj2 =data.link; 
        	$scope.loading=false;
        }, function (reason){
        	$scope.showUploadPicture=false;
        	$scope.showUploadErrorCode=true;
        	$scope.uploadErrorCode=reason;
        	$scope.loading=false;
        });
    };
}]);