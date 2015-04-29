var eventModule = angular.module('qrcode', []);

app.controller('QrCodeListController', ['$http','RestService','$stateParams', function($http,RestService,$stateParams){
	var hlmng = this;
	hlmng.qrcodes = [];
	hlmng.apiUrl = apiUrl;
	RestService.list('qrcode').then(function(data){
	    $.each(data, function(i, item){
	    	if(item.eventIDFK==$stateParams.eventId){
	    		item.qrCodeRenderURL = apiUrl+"qrcode/"+item.qrcodeID+"/render";
	    		hlmng.qrcodes.push(item);	
	    	}
	    });
	});
}]);