angular.module('userApp.services',[]).factory('User',function($resource){
    return $resource('http://localhost:8080/hlmng/rest/user/:id',{id:'@_id'},{
        update: {
            method: 'PUT'
        }
    });
}).service('popupService',function($window){
    this.showPopup=function(message){
        return $window.confirm(message);
    }
});