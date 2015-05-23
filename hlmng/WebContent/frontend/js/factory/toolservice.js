app.factory('ToolService', ['$location','RestService','$state','$log','$http','$q' , function ($location,RestService,$state,$log,$http,$q){
	return {
		
		redir: function(state,params){
			$log.log("Redirecting to state: "+state+" with params: "+params.toSource());
			$state.go(state,params);
		},
		
		getCurTime: function (){
			var time = new Date();
			return ("0" + time.getHours()).slice(-2)   + ":" +  ("0" + time.getMinutes()).slice(-2) + ":" +  ("0" + time.getSeconds()).slice(-2);
		},
		
		dateCompare: function(time1,time2) {
		  var t1 = new Date();
		  var parts = time1.split(":");
		  t1.setHours(parts[0],parts[1],parts[2],0);
		  var t2 = new Date();
		  parts = time2.split(":");
		  t2.setHours(parts[0],parts[1],parts[2],0);
		  // returns 1 if greater, -1 if less and 0 if the same
		  if (t1.getTime()>t2.getTime()) return 1;
		  if (t1.getTime()<t2.getTime()) return -1;
		  return 0;
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
		},
		
		idSolver: function(id,name){
            var deferred = $q.defer();
		   	RestService.get(id,name).then(function(data){
                deferred.resolve(data);
		   	});	
            return deferred.promise;

		}
	};
}]);


