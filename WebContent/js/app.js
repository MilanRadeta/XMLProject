app = angular.module('app', []);

app.controller("appController", function($scope, $http) {
	$scope.loggedIn = false;
	$scope.document = null;
	$scope.username = null;
	$scope.password = null;
	$scope.searchTerm = null;
	$scope.searchResults = [];
	$scope.showNewAct = false;
	
	$scope.parts = ["Deo", "Glava", "Odeljak", "Pododeljak", "Član", "Stav", "Tačka", "Podtačka", "Alineja"];
	$scope.selectedPart = $scope.parts[0];
	
	$scope.preambula = null;
	$scope.naziv = null;
	$scope.elements = [];
	
	$scope.login = function() {
		$http({
			method : "POST",
			url : "api/user/login",
			data: {username: $scope.username, password:  $scope.password}
		}).then(function(response) {
			if (response.data) {
				$scope.loggedIn = true;
			}
		});
	};
	
	$scope.search = function() {
		
	};
	
	$scope.remove = function(element) {
		
	};

	$scope.newAct = function() {
		
	};

	$scope.addNewElement = function() {
		
	};
	
	$scope.saveNewAct = function() {
		
	};
	
	$scope.closeNewAct = function() {
		
	};
});