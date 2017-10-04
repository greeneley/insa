var app = angular.module('monApp', []);
app.controller('sayHello', ['$scope', function($scope){
	$scope.name = {
		text: "Your Name"
	};
}]);
