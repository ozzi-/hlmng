var eventModule = angular.module('qrcode', []);


app.controller('QrcodeNewController', ['$http','RestService','ToolService','dataService', function($http,RestService,ToolService,dataService){
	var hlmng = this;
	hlmng.qrcode={};
	hlmng.postQrcode = RestService.post;
	hlmng.redir=ToolService.redir;
	hlmng.postAndRedir = function(fQrcode) {  
		hlmng.postEvent(fQrcode,'qrcode').then(function(data){
			hlmng.event=data;
			hlmng.redir('/eventactive/'+$stateParams.eventId+'/qrcode/'+hlmng.qrcode.qrcodeID);
		});
	};
}]);


app.controller('QrcodeIdController', ['$http','$stateParams','RestService','ToolService', function($http, $stateParams,RestService,ToolService){
	var hlmng = this;
	
	hlmng.qrcode = {};	
	RestService.get($stateParams.qrcodeId,'qrcode').then(function(data){
		hlmng.qrcode=data;
		hlmng.qrCodeRenderURL = apiUrl+"qrcode/"+hlmng.qrcode.qrcodeID+"/render";
	});

	hlmng.putQrcode = RestService.put;
	hlmng.deleteQrcode = RestService.del;
	hlmng.redir=ToolService.redir;
}]);



app.controller('QrcodeListController', ['$http','RestService','$stateParams', function($http,RestService,$stateParams){
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