var myApp = angular.module('MultiController', []);
myApp.controller('Math1', ['$scope', function($scope){
	$scope.result = 0;
	$scope.add    = function(a,b){
		$scope.result = a+b;
	};
}]);

myApp.controller('Math2', ['$scope', function($scope){
	$scope.result   = 0;
	$scope.multiple = function(a,b){
		$scope.result = a*b;
	};
}]);