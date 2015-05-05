var eventModule = angular.module('qrcode', []);


app.controller('QrcodeNewController', ['$http','$stateParams','RestService','ToolService','dataService', function($http,$stateParams,RestService,ToolService,dataService){
	var hlmng = this;
	hlmng.qrcode={};
	hlmng.count=1;
	hlmng.postQrcode = RestService.post;
	hlmng.redir=ToolService.redir;
	hlmng.postAndRedir = function() {
		var asynci = 0;
		for(var i = 0; i < hlmng.count;  i++) {
			hlmng.postQrcode(hlmng.qrcode,'qrcode').then(function(data){
				hlmng.qrcode=data;
				asynci ++;
				if(hlmng.count===1){
					hlmng.redir('/event/active/'+$stateParams.eventId+'/qrcode/'+hlmng.qrcode.qrcodeID);			
				}else if(asynci===hlmng.count){
					hlmng.redir('/event/active/'+$stateParams.eventId+'/qrcode/list');			
				}
			});
		}
	};
}]);


app.controller('QrcodeIdController', ['$http','$state','$stateParams','RestService','ToolService', function($http,$state, $stateParams,RestService,ToolService){
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



app.controller('QrcodeListController', ['$http','RestService','$state','$stateParams', function($http,RestService,$state,$stateParams){
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