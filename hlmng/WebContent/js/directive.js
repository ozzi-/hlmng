var directiveModule = angular.module('directive', []);

directiveModule.directive('speakerInfo', function(){
	return {
		restrict: 'E',
		templateUrl: 'template/speaker/speaker-info.html',
		scope: {
			speaker: "=speaker"
		}
	};
});

directiveModule.directive('speakerCountriesOption', function(){
	return {
		restrict: 'E',
		templateUrl: 'template/speaker/speaker-countries-option.html',
		scope: {
			nationality: "=nationality"
		}
	};
});

directiveModule.directive('speakerInfoEdit', function(){
	return {
		restrict: 'E',
		templateUrl: 'template/speaker/speaker-info-edit.html',
		scope: {
			speaker: "=speaker"
		}
	};
});

directiveModule.directive('eventInfo', function(){
	return {
		restrict: 'E',
		templateUrl: 'template/event/event-info.html',
		scope: {
			event: "=event"
		}
	};
});

directiveModule.directive('eventInfoEdit', function(){
	return {
		restrict: 'E',
		templateUrl: 'template/event/event-info-edit.html',
		scope: {
			event: "=event"
		}
	};
});

directiveModule.directive('eventItemInfoEdit', function(){
	return {
		restrict: 'E',
		templateUrl: 'template/eventitem/eventitem-info-edit.html',
		scope: {
			eventitem: "=eventitem"
		}
	};
});

directiveModule.directive('eventItemInfo', function(){
	return {
		restrict: 'E',
		templateUrl: 'template/eventitem/eventitem-info.html',
		scope: {
			eventitem: "=eventitem"
		}
	};
});


directiveModule.directive('eventRoomInfoEdit', function(){
	return {
		restrict: 'E',
		templateUrl: 'template/eventroom/eventroom-info-edit.html',
		scope: {
			eventroom: "=eventroom"
		}
	};
});

directiveModule.directive('eventRoomInfo', function(){
	return {
		restrict: 'E',
		templateUrl: 'template/eventroom/eventroom-info.html',
		scope: {
			eventroom: "=eventroom"
		}
	};
});




directiveModule.directive('mediaUpload', function(){
	return {
		restrict: 'E',
		templateUrl: 'template/media/upload.html',
		scope:{
		}
	};
});

	
// This code comes from: https://uncorkedstudios.com/blog/multipartformdata-file-upload-with-angularjs
directiveModule.directive('fileModel', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;
            
            element.bind('change', function(){
                scope.$apply(function(){
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };
}]);