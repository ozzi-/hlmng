var helperModule = angular.module('helper', []);



helperModule.directive('facebook', function(){
	return {
		restrict: 'E',
		templateUrl: 'template/helper/facebook.html',
		scope:{
			messagetext: "=messagetext",
			messageimage: "=messageimage"
		}
	};
});


helperModule.directive('editButton', function(){
	return {
		restrict: 'E',
		templateUrl: 'template/helper/edit-button.html',
		scope:{
			edit: "=edit"
		}
	};
});

helperModule.directive('nothingHereYet', function(){
	return {
		restrict: 'E',
		templateUrl: 'template/helper/nothing-here-yet.html',
		scope:{
			list: "=list"
		}
	};
});

directiveModule.directive('datePicker', function(){
	return {
		restrict: 'E',
		templateUrl: 'template/helper/date-picker.html',
		scope: {
			date: "=date"
		},
    	controller: function($scope,$filter) {
    	
    		$scope.clear = function () {
    			$scope.dt = null;
    		};
    		
    		$scope.$watch('date', function(v){  // override the output format of datepicker
    			if($scope.date!=undefined){
    				$scope.date = $filter('date')($scope.date, "yyyy-MM-dd");
    			}
    		});

    		$scope.open = function($event) {
    			$event.preventDefault();
    			$event.stopPropagation();
    			
    			$scope.opened = true;
    		};
       	}
	};
});

directiveModule.directive('timePicker', function(){
	return {
		restrict: 'E',
		templateUrl: 'template/helper/time-picker.html',
		scope: {
			time: "=time"
		}
		// sadly we can't override the output format, the directive will start to cry: 
		// "Timepicker directive: "ng-model" value must be a Date object, a number of milliseconds 
		// since 01.01.1970 or a string representing an RFC2822 or ISO 8601 date."
	};
});
