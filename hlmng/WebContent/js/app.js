var app = angular.module('hlmngApp',['ngRoute','speaker','event','eventroom','media','helper','directive','ui.bootstrap']);
var apiUrl = 'https://localhost:8443/hlmng/rest/';


app.controller('NavBarController', ['$http','$scope','$location','$log', function($http,$scope,$location,$log){	
	$scope.isActive = function (viewLocation) {
	    var active = (viewLocation === $location.path());
	    return active; 
	};
}]);



app.factory('RestService', ['$log','$http','$q', function ($log,$http,$q) {
    return {
        post: function (obj,className) {
        	var deferred = $q.defer();
            $http({ method: "POST", url: apiUrl+className, data: obj })
            .success(function (data) {
    			$log.log('post '+className+' successfully');
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


app.directive('errSrc', function() {
	  return {
	    link: function(scope, element, attrs) {
	      element.bind('error', function() {
	        if (attrs.src != attrs.errSrc) {
	          attrs.$set('src', attrs.errSrc);
	        }
	      });
	    }
	  };
});