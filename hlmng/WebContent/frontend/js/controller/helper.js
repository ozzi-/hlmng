var helperModule = angular.module('helper', ['angularSpinner']);

helperModule.config(['usSpinnerConfigProvider', function (usSpinnerConfigProvider) {
    usSpinnerConfigProvider.setDefaults({color: 'red'});
}]);


helperModule.directive('loadingSpinner', function(){
 return {
      restrict: 'A',
      replace: true,
      transclude: true,
      scope: {
        loadingspinner: '=loadingSpinner'
      },
      templateUrl: 'template/helper/loading.html'
 };
});

helperModule.directive('publisher', function(){
	return {
		restrict: 'E',
		templateUrl: 'template/helper/publisher.html',
		scope:{
			socialobj: "=socialobj",
			socialctrl: "=socialctrl",
			pubctrl: "=pubctrl"
		}
	};
});


helperModule.directive('facebook', function(){
	return {
		restrict: 'E',
		templateUrl: 'template/helper/facebook.html',
		scope:{
			socialobj: "=socialobj",
			socialctrl: "=socialctrl",
			loginstatus: "=loginstatus"
		}
	};
});

helperModule.directive('errSrc', function() {
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

helperModule.directive('autoShortField', function(){
	return {
		restrict: 'E',
		templateUrl: 'template/helper/auto-short-field.html',
		scope:{
			field: "=field",
			threshold: "=threshold"
		}
	};
});

helperModule.directive('datePicker', function(){
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

helperModule.directive('timePicker', function(){
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
