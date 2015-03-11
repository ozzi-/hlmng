function getHTTP($scope, $http) {
    $http.get("http://localhost:8080/hlmng/rest/albums").
        success(function(data) {
            $scope.httpResponse = data;
        });
}