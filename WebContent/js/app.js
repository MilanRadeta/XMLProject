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

	$scope.newAct = function() {
		$scope.resetAct();
		$scope.showNewAct = true;
	};

	
	$scope.saveNewAct = function() {
		$scope.errorMessages = [];
		if (!$scope.materijalniPravniOsnov) {
			$scope.errorMessages.push("Nedostaje materijalni pravni osnov");
		}
		
		if (!$scope.formalniPravniOsnov) {
			$scope.errorMessages.push("Nedostaje formalni pravni osnov");
		}
		
		if (!$scope.naziv) {
			$scope.errorMessages.push("Nedostaje naziv propisa");
		}
		
		if ($scope.elements.length == 0) {
			$scope.errorMessages.push("Nedostaje sadržaj propisa");
		}
		else {
			for (var index in $scope.elements) {
				var element = $scope.elements[index];
				if (index == 0) {
					switch (element.type) {
					case "Deo":
					case "Glava":
					case "Član":
						break;
					default:
						$scope.errorMessages.push("Prvi element može biti Deo, Glava ili Član.");
					}
					continue;
				}
				else {
					var prevElement = $scope.elements[index - 1]
					switch (prevElement.type) {
					case "Deo":
						if (element.type != "Glava") {
							$scope.errorMessages.push("Posle Dela može ići samo Glava.");
						}
						break;
					case "Glava":
						switch (element.type) {
						case "Odeljak":
						case "Član":
							break;
						default:
							$scope.errorMessages.push("Posle Glave može biti Član ili Odeljak.");
						}
						break;
					case "Odeljak":
						switch (element.type) {
						case "Pododeljak":
						case "Član":
							break;
						default:
							$scope.errorMessages.push("Posle Odeljka može biti Pododeljak ili Član.");
						}
						break;
					case "Pododeljak":
						if (element.type != "Član") {
							$scope.errorMessages.push("Posle Pododeljka može biti samo Član.");
						}
						break;
					case "Član":
						if (element.type != "Stav") {
							$scope.errorMessages.push("Posle Člana može biti samo Stav.");
						}
						break;
					case "Stav":
						switch (element.type) {
						case "Stav":
						case "Tačka":
						case "Član":
						case "Deo":
						case "Glava":
						case "Odeljak":
						case "Pododeljak":
							break;
						default:
							$scope.errorMessages.push("Posle Stava ne može biti Podtačka ili Alineja.");
						}
						break;
					case "Tačka":
						switch (element.type) {
						case "Stav":
						case "Tačka":
						case "Podtačka":
						case "Član":
						case "Deo":
						case "Glava":
						case "Odeljak":
						case "Pododeljak":
							break;
						default:
							$scope.errorMessages.push("Posle Tačke ne može biti Alineja.");
						}
						break;
					case "Podtačka":
					case "Alineja":
						break;
					}
				}
			}
		}
		
		if (!$scope.obrazlozenje) {
			$scope.errorMessages.push("Nedostaje obrazloženje za predlaganje propisa");
		}
		
		if (!$scope.errorMessage) {
			var newAct = {};
			newAct.preambula = {};
			newAct.preambula.pravniOsnov = {};
			newAct.preambula.pravniOsnov.materijalniPravniOsnov = $scope.materijalniPravniOsnov;
			newAct.preambula.pravniOsnov.formalniPravniOsnov = $scope.formalniPravniOsnov;
			newAct.naziv = $scope.naziv;
			newAct.obrazlozenje = $scope.obrazlozenje;
			// TODO
			for (var index in $scope.elements) {
				var element = $scope.elements[index];
				if (index == 0) {
					switch (element.type) {
					case "Deo":
						newAct.deo = [{naziv: element.value, glava: []}];
						break;
					case "Glava":
						newAct.glava = [{naziv: element.value, glava: []}];
					}
				}
				else {
					var prevElement = $scope.elements[index - 1];
				}
			}
		}
	};
	
	$scope.addNewElement = function() {
		$scope.elements.push({type: "Deo", value: ""});
	}
	
	$scope.remove = function(element) {
		var index = $scope.elements.indexOf(element); 
		if (index != -1) {
			$scope.elements.splice(index, 1);
		}
	}
	
	$scope.closeNewAct = function() {
		$scope.showNewAct = false;
		$scope.resetAct();
	};
	
	$scope.resetAct = function() {
		$scope.naziv = null;
		$scope.obrazlozenje = null;
		$scope.materijalniPravniOsnov = null;
		$scope.formalniPravniOsnov = null;
		$scope.nazivDonosiocaPropisa = null;
		$scope.saglasnostOrgana = null;
		$scope.saglasnostNaznaka = null;
		$scope.errorMessages = [];
		$scope.elements = [{type: "Deo", value: ""}];
	}
	
	$scope.resetAct();
});