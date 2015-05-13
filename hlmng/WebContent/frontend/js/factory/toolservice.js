app.factory('ToolService', ['$location','$state','$log','$http','$q' , function ($location,$state,$log,$http,$q){
	return {
		redir: function(state,params){
			$log.log("Redirecting to state: "+state+" with params: "+params.toSource());
			$state.go(state,params);
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
		},
		pagination:  function(list,hlmng) {
			// Config
			hlmng.itemsPerPage = 5;
			// ------
			hlmng.startItem = (hlmng.currentPage-1)*hlmng.itemsPerPage;
			hlmng.assumedEndItem = hlmng.startItem+hlmng.itemsPerPage;
			hlmng.endItem = (hlmng.assumedEndItem > list.length) ? list.length : hlmng.assumedEndItem;
			hlmng.pageCount =  Math.ceil(list.length / hlmng.itemsPerPage);
			hlmng.itemsPaginated = list.slice(hlmng.startItem,hlmng.endItem);
		}
	};
}]);


