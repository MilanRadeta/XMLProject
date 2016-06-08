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

	$scope.test = function() {
		var elem = new XMLWriter();

		var xw = new XMLWriter('UTF-8', '1.0');
		xw.writeStartDocument();
		xw.writeStartElement("Stav");
		xw.writeAttributeString("xmlns:elem", "http://www.skupstinans.rs/elementi");
		xw.writeAttributeString("xmlns", "http://www.skupstinans.rs/elementi");
		xw.writeAttributeString("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		xw.writeAttributeString("xmlns:schemaLocation", "http://www.skupstinans.rs/elementi Elementi.xsd");
		xw.writeAttributeString("id", "1");

		xw.writeString("test");
		xw.writeEndElement();
		xw.writeEndDocument();
		console.log(xw.flush());
		console.log(xw.getDocument());
		$http({
			method : "POST",
			url : "api/act/test",
			data: xw.flush(),
			headers: {
			   'Content-Type': "application/xml"
			 }
		}).then(function(response) {
			console.log(response);
		});
	}
	
	$scope.saveNewAct = function() {
		$scope.errorMessages = [];
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

		if (!$scope.materijalniPravniOsnov) {
			$scope.errorMessages.push("Nedostaje materijalni pravni osnov");
		}
		
		if (!$scope.formalniPravniOsnov) {
			$scope.errorMessages.push("Nedostaje formalni pravni osnov");
		}
		
		if (!$scope.nazivDonosiocaPropisa) {
			$scope.errorMessages.push("Nedostaje donosilac propisa");
		}
		
		if (!$scope.nazivPropisa) {
			$scope.errorMessages.push("Nedostaje naziv propisa");
		}
		
		if (!$scope.obrazlozenje) {
			$scope.errorMessages.push("Nedostaje obrazloženje za predlaganje propisa");
		}
		
		if ($scope.errorMessages.length == 0) {
			var xw = new XMLWriter('UTF-8', '1.0');
			xw.writeStartDocument();
			xw.writeStartElement("Propis");
			xw.writeAttributeString("xmlns:elem", "http://www.skupstinans.rs/elementi");
			xw.writeAttributeString("xmlns", "http://www.skupstinans.rs/propis");
			xw.writeAttributeString("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
			xw.writeAttributeString("xmlns:schemaLocation", "http://www.skupstinans.rs/propis Propis.xsd");
			xw.writeAttributeString("xmlns:propis", "http://www.skupstinans.rs/propis");

			xw.writeStartElement("Preambula");
			xw.writeStartElement("PravniOsnov");
			xw.writeElementString("MaterijalniPravniOsnov", $scope.materijalniPravniOsnov);
			xw.writeElementString("FormalniPravniOsnov", $scope.materijalniPravniOsnov);
			xw.writeEndElement();
			xw.writeStartElement("DonosilacPropisa");
			xw.writeElementString("NazivOrgana", $scope.nazivDonosiocaPropisa);
			xw.writeEndElement();
			if (!$scope.saglasnostOrgana || !$scope.saglasnostNaznaka) {
				if (!$scope.saglasnostOrgana && !$scope.saglasnostNaznaka) {
					// OK
				}
				else {
					$scope.errorMessages.push("Za saglasnost su potrebni i naziv saglasnog organa i naznaka saglasnosti");
				}
			}
			else {
				xw.writeStartElement("Saglasnost");
				xw.writeElementString("NazivOrgana", $scope.saglasnostOrgana);
				xw.writeElementString("Naznaka", $scope.saglasnostNaznaka);
				xw.writeEndElement();
			}
			xw.writeEndElement();

			xw.writeElementString("NazivPropisa", $scope.nazivPropisa);
			
			var elementDict = {};
			var elementPartId = {};
			for (var index in $scope.parts) {
				var part = $scope.parts[index];
				elementDict[part] = 0;
				elementPartId[part] = 0;
			}
			var resetElementDict = function(elementDict, key, xw) {
				if (elementDict[key]) {
					elementDict[key] = 0;
					xw.writeEndElement();
				}
			}
			
			var generateId = function(elementPartId, type) {
				var generateIdPart = function(type) {
					if (elementPartId[type]) {
						return "/" + type + elementPartId[type];
					}
					else {
						return "";
					}
				}
				var retVal = "";
				switch (element.type) {
				case "Alineja":
					retVal = generateIdPart("Alineja") + retVal;
				case "Podtačka":
					retVal = generateIdPart("Podtačka") + retVal;
				case "Tačka":
					retVal = generateIdPart("Tačka") + retVal;
				case "Stav":
					retVal = generateIdPart("Stav") + retVal;
				case "Član":
					retVal = generateIdPart("Član") + retVal;
				case "Pododeljak":
					retVal = generateIdPart("Pododeljak") + retVal;
				case "Odeljak":
					retVal = generateIdPart("Odeljak") + retVal;
				case "Glava":
					retVal = generateIdPart("Glava") + retVal;
				case "Deo":
					retVal = generateIdPart("Deo") + retVal;
				}
				return retVal;
			}
			for (var index in $scope.elements) {
				var element = $scope.elements[index];
				switch (element.type) {
				case "Deo":
					resetElementDict(elementDict, "Deo", xw);
					elementPartId["Glava"] = 0;
				case "Glava":
					resetElementDict(elementDict, "Glava", xw);
					elementPartId["Odeljak"] = 0;
				case "Odeljak":
					resetElementDict(elementDict, "Odeljak", xw);
					elementPartId["Pododeljak"] = 0;
				case "Pododeljak":
					resetElementDict(elementDict, "Pododeljak", xw);
					elementPartId["Član"] = 0;
				case "Član":
					resetElementDict(elementDict, "Član", xw);
					elementPartId["Stav"] = 0;
				case "Stav":
					resetElementDict(elementDict, "Stav", xw);
					elementPartId["Tačka"] = 0;
				case "Tačka":
					resetElementDict(elementDict, "Tačka", xw);
					elementPartId["Podtačka"] = 0;
				case "Podtačka":
					resetElementDict(elementDict, "Podtačka", xw);
					elementPartId["Alineja"] = 0;
				case "Alineja":
					resetElementDict(elementDict, "Alineja", xw);
				}
				xw.writeStartElement(element.type, "elem");
				elementPartId[element.type] += 1;
				xw.writeAttributeString("elem:id", generateId(elementPartId, element.type));
				elementDict[element.type] += 1;
				switch (element.type) {
				case "Deo":
				case "Glava":
				case "Odeljak":
				case "Pododeljak":
				case "Član":
					xw.writeElementString("Naziv", element.value, "elem");
					break;
				case "Stav":
				case "Tačka":
				case "Podtačka":
				case "Alineja":
					xw.writeString(element.value);
					break;
				}
			}
			
			for (var index in $scope.parts) {
				var part = $scope.parts[index];
				resetElementDict(elementDict, part, xw);
			}
			

			xw.writeElementString("Obrazlozenje", $scope.obrazlozenje);
			
			xw.writeEndElement();
			xw.writeEndDocument();
			
			// TODO: check Naziv elements of Deo, Glava, Odeljak, Pododeljak and Član.
			// If one Deo has naziv, then all of them must have naziv
			// Član must have naziv, unless it's the only član of Glava, Odeljak or Pododeljak
			
			// TODO: Deo must have at least two Glava parts
			console.log(xw);
			console.log(xw.getDocument());
			console.log(xw.flush());
			xw.close();
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
		$scope.nazivPropisa = null;
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