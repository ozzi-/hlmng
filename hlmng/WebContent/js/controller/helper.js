var mediaModule = angular.module('helper', []);


mediaModule.directive('editButton', function(){
	return {
		restrict: 'E',
		templateUrl: 'template/helper/edit-button.html',
		scope:{
			edit: "=edit"
		}
	};
});
