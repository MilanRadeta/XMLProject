app = angular.module('app', []);

app.controller("appController", function($scope) {
	$scope.loggedIn = false;
	$scope.document = null;
	$scope.username = null;
	$scope.password = null;
	$scope.searchTerm = null;
	$scope.searchResults = [];
});