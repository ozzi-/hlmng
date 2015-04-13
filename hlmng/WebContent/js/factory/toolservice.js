app.factory('ToolService', ['$location','$log' , function ($location,$log){
	return {
		redir: function(path){
			$location.path(path);
		}
	};
}]);