var myApp = angular.module('SimpleModule', []);
myApp.controller('MyController', ['$scope', function($scope){
	$scope.title   = 'Module Simple';
	$scope.result  = 0;
	$scope.add     = function(a,b){
		$scope.result = a + b;
	};
	$scope.multiple = function(a,b){
		$scope.result = a * b;
	};
}]);