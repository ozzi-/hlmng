var helperModule = angular.module('helper', []);


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