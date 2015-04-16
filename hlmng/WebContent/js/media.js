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


mediaModule.directive('mediaUpload', function(){
	return {
		restrict: 'E',
		templateUrl: 'template/media/upload.html',
		scope:{
		}
	};
});

	
// This code comes from: https://uncorkedstudios.com/blog/multipartformdata-file-upload-with-angularjs
mediaModule.directive('fileModel', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;
            
            element.bind('change', function(){
                scope.$apply(function(){
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };
}]);