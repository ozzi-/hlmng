var sliderModule = angular.module('slider', []);

sliderModule.controller('SliderNewController', ['$http','RestService','ToolService','$stateParams', function($http,RestService,ToolService,$stateParams){
}]);


sliderModule.controller('SliderListController', ['$http','RestService','ToolService','$stateParams', function($http,RestService,ToolService,$stateParams){
	var hlmng = this;
	hlmng.sliders = [];
	
	RestService.list('slider').then(function(data){
	    $.each(data, function(i, item){
	    	hlmng.sliders.push(item);	 
	    });
	});
}]);

