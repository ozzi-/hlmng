app.factory('ToolService', ['$location','$log','$http','$q' , function ($location,$log,$http,$q){
	return {
		redir: function(path){
			$log.log("Redirecting to: "+path);
			$location.path(path);
		},
		uploadFile: function(file){
			var deferred = $q.defer();
	        var fd = new FormData();
	        fd.append('file', file);
	        $http.post(apiUrl+"media/uploadbackend", fd, {
	            transformRequest: angular.identity,
	            headers: {'Content-Type': undefined}
	        })
	        .success(function(data){
	        	$log.log("Successfully uploaded media!"+data.mediaID);
	            deferred.resolve(data);
	        })
	        .error(function(data){
	        	$log.log("Error when uploading media!"+data);
	            deferred.reject(data);
	        });
	        return deferred.promise;
		}
	};
}]);
