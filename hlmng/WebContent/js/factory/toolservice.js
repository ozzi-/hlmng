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
	        $http.post(apiUrl+"media/upload", fd, {
	            transformRequest: angular.identity,
	            headers: {'Content-Type': undefined} // the browser will fill in the correct type
	        })
	        .success(function(data){
	        	$log.log("Successfully uploaded media!\n"+data.mediaID);
	            deferred.resolve(data);
	        })
	        .error(function(data, status){
	        	$log.log("Error when uploading media!\n"+data);
	            deferred.reject(status);
	        });
	        return deferred.promise;
		}
	};
}]);
