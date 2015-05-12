var qrCodeModule = angular.module('qrcode', []);


qrCodeModule.controller('QrcodeNewController', ['$http','$stateParams','RestService','ToolService','dataService', function($http,$stateParams,RestService,ToolService,dataService){
	var hlmng = this;
	hlmng.qrcode={};
	hlmng.count=1;
	hlmng.postQrcode = RestService.post;
	hlmng.postAndRedir = function() {
		var asynci = 0;
		hlmng.qrcode.eventIDFK=$stateParams.eventId;
		for(var i = 0; i < hlmng.count;  i++) {
			hlmng.postQrcode(hlmng.qrcode,'qrcode').then(function(data){
				hlmng.qrcode=data;
				asynci ++;
				if(hlmng.count===1){
					ToolService.redir('event.active.qrcode.id',{eventId: $stateParams.eventId, qrcodeId: hlmng.qrcode.qrcodeID});			
				}else if(asynci===hlmng.count){
					ToolService.redir('event.active.qrcode.list',{eventId: $stateParams.eventId});			
				}
			});
		}
	};
}]);


qrCodeModule.controller('QrcodeIdController', ['$http','$state','$stateParams','RestService','ToolService', function($http,$state, $stateParams,RestService,ToolService){
	var hlmng = this;
	
	hlmng.qrcode = {};	
	RestService.get($stateParams.qrcodeId,'qrcode').then(function(data){
		hlmng.qrcode=data;
		hlmng.qrcode.qrCodeRenderURL = apiUrl+"qrcode/"+hlmng.qrcode.qrcodeID+"/render";
	});
	
	hlmng.putQrcode = RestService.put;
	hlmng.deleteQrcode = RestService.del;

}]);



qrCodeModule.controller('QrcodeListController', ['$http','RestService','$state','$stateParams', function($http,RestService,$state,$stateParams){
	var hlmng = this;
	hlmng.qrcodes = [];
	hlmng.apiUrl = apiUrl;
	
	hlmng.printQr = function(qrCode){
		printWindow=window.open('','HLMNG - QR Code Print','width=500,height=500');
		printWindow.document.write('<img class="qrcode-image" src="'+qrCode.qrCodeRenderURL+'"/>');
		printWindow.document.close();
		printWindow.focus();
		printWindow.print(); 
	};
	
	RestService.list('qrcode').then(function(data){
	    $.each(data, function(i, item){
	    	if(item.eventIDFK==$stateParams.eventId){
	    		item.qrCodeRenderURL = apiUrl+"qrcode/"+item.qrcodeID+"/render";
	    		hlmng.qrcodes.push(item);	
	    	}
	    });
	});
}]);

