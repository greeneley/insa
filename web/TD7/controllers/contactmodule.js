var myApp = angular.module('ContactModule', []);
myApp.controller('ContactController', ['$scope', function($scope){
	$scope.contact = {
		name: "Thanh Luu",
		email: "truong.luu@insa-cvl.fr",
		phone: "0623456789"
	};
	$scope.calculate = function(a,b){
		return (a+b)*3;
	};
}]);