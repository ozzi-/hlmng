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
			speaker: "=speaker",
			hidechangeimage:"=hidechangeimage"
		}
	};
});

directiveModule.directive('qrcodeInfo', function(){
	return {
		restrict: 'E',
		templateUrl: 'template/qrcode/qrcode-info.html',
		scope: {
			qrcode: "=qrcode"
		}
	};
});

directiveModule.directive('qrcodeInfoNew', function(){
	return {
		restrict: 'E',
		templateUrl: 'template/qrcode/qrcode-info-new.html',
		scope: {
			qrcode: "=qrcode",
			count: "=count"
		}
	};
});

directiveModule.directive('qrcodeInfoEdit', function(){
	return {
		restrict: 'E',
		templateUrl: 'template/qrcode/qrcode-info-edit.html',
		scope: {
			qrcode: "=qrcode"
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
			event: "=event",
			hidechangeimage:"=hidechangeimage"
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
			eventitem: "=eventitem",
			details: "=details"
		}
	};
});

directiveModule.directive('socialInfo', function(){
	return {
		restrict: 'E',
		templateUrl: 'template/whatsup/social/social-info.html',
		scope: {
			social: "=social"
		}
	};
});

directiveModule.directive('socialInfoEdit', function(){
	return {
		restrict: 'E',
		templateUrl: 'template/whatsup/social/social-info-edit.html',
		scope: {
			social: "=social"
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


directiveModule.directive('pushInfo', function(){
	return {
		restrict: 'E',
		templateUrl: 'template/push/push-info.html',
		scope: {
			push: "=push"
		}
	};
});


directiveModule.directive('pushInfoEdit', function(){
	return {
		restrict: 'E',
		templateUrl: 'template/push/push-info-edit.html',
		scope: {
			push: "=push"
		}
	};
});

directiveModule.directive('newsInfo', function(){
	return {
		restrict: 'E',
		templateUrl: 'template/whatsup/news/news-info.html',
		scope: {
			news: "=news"
		}
	};
});


directiveModule.directive('newsInfoEdit', function(){
	return {
		restrict: 'E',
		templateUrl: 'template/whatsup/news/news-info-edit.html',
		scope: {
			news: "=news"
		}
	};
});

directiveModule.directive('votingInfo', function(){
	return {
		restrict: 'E',
		templateUrl: 'template/voting/voting-info.html',
		scope: {
			voting: "=voting"
		}
	};
});

directiveModule.directive('votingInfoEdit', function(){
	return {
		restrict: 'E',
		templateUrl: 'template/voting/voting-info-edit.html',
		scope: {
			voting: "=voting",
			votingSettingsMode: "=votingsettingsmode"
		}
	};
});

directiveModule.directive('votingInfoEditNew', function(){
	return {
		restrict: 'E',
		templateUrl: 'template/voting/voting-info-edit-new.html',
		scope: {
			voting: "=voting",
			votingSettingsMode: "=votingsettingsmode",
			ctrl: "=ctrl"
		}
	};
});

directiveModule.directive('sliderInfo', function(){
	return {
		restrict: 'E',
		templateUrl: 'template/slider/slider-info.html',
		scope: {
			slider: "=slider"
		}
	};
});

directiveModule.directive('sliderInfoEdit', function(){
	return {
		restrict: 'E',
		templateUrl: 'template/slider/slider-info-edit.html',
		scope: {
			slider: "=slider"
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