app.factory('RestService', ['$log','$http','$q', function ($log,$http,$q) {
    return {
        post: function (obj,className) {
        	var deferred = $q.defer();       
        	$log.log(obj);
            $http({ method: "POST", url: apiUrl+className, data: obj })
            .success(function (data) {
    			$log.log('post '+className+' successfully!');  
                deferred.resolve(data);
            }).error(function (data) {
    			$log.log('couldn\'t post '+className);
                deferred.reject(data);
            });
            return deferred.promise;
        },
        put: function (obj,objId,className) {
    		$http.put(apiUrl+className+'/'+objId, obj).success(function(data) {
    			$log.log('put '+className+' successfully');
    		}).error(function(data){
    			$log.log('couldn\'t put '+className+', ID='+objId);
    		});
        },
    	get: function (objId,className) {
            var deferred = $q.defer(); // we need the q lib -> deferred because else the view would receive the data too late
            $http({ method: "GET", url: apiUrl+className+'/'+objId })
            .success(function (data) {
				$log.log('get '+className+' successfully, ID='+objId);
                deferred.resolve(data);
            }).error(function (data) {
				$log.log('couldn\'t get '+className+', ID='+objId);
                deferred.reject(data);
            });
            return deferred.promise;
		},
    	list: function (className) {
            var deferred = $q.defer(); // we need the q lib -> deferred because else the view would receive the data too late
            $http({ method: "GET", url: apiUrl+className })
            .success(function (data) {
				$log.log('list '+className+' successfully');
                deferred.resolve(data);
            }).error(function (data) {
				$log.log('couldn\'t list '+className);
                deferred.reject(data);
            });
            return deferred.promise;
		},
    	del: function (objId,className) {
            $http({ method: "DELETE", url: apiUrl+className+'/'+objId })
            .success(function (data) {
				$log.log('deleted '+className+' successfully, ID='+objId);
            }).error(function (data) {
				$log.log('couldn\'t delete '+className+', ID='+objId);
            });
		}
    };
}]);

