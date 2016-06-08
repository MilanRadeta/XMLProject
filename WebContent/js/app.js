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
		if ($scope.saglasnostOrgana || $scope.saglasnostNaznaka) {
			xw.writeStartElement("Saglasnost");
			xw.writeElementString("NazivOrgana", $scope.saglasnostOrgana);
			xw.writeElementString("Naznaka", $scope.saglasnostNaznaka);
			xw.writeEndElement();
		}
		xw.writeEndElement();

		xw.writeElementString("NazivPropisa", $scope.nazivPropisa);
		
		var elementDict = {};
		var elementPartId = {};
		var toAscii = function(str) {
			return str.replace("Š", "S")
			.replace("Đ", "Dj")
			.replace("Č", "C")
			.replace("Ć", "C")
			.replace("Ž", "Z")
			.replace("š", "s")
			.replace("đ", "dj")
			.replace("č", "c")
			.replace("ć", "c")
			.replace("ž", "z");
		}
		for (var index in $scope.parts) {
			var part = toAscii($scope.parts[index]);
			elementDict[part] = 0;
			elementPartId[part] = 0;
		}
		var resetElementDict = function(elementDict, key, xw) {
			if (elementDict[key]) {
				elementDict[key] = 0;
				xw.writeEndElement();
			}
		}
		// TODO: move to backend?
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
			switch (type) {
			case "Alineja":
				retVal = generateIdPart("Alineja") + retVal;
			case "Podtacka":
				retVal = generateIdPart("Podtacka") + retVal;
			case "Tacka":
				retVal = generateIdPart("Tacka") + retVal;
			case "Stav":
				retVal = generateIdPart("Stav") + retVal;
			case "Clan":
				retVal = generateIdPart("Clan") + retVal;
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
			var type = toAscii(element.type);
			switch (type) {
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
				elementPartId["Clan"] = 0;
			case "Clan":
				resetElementDict(elementDict, "Clan", xw);
				elementPartId["Stav"] = 0;
			case "Stav":
				resetElementDict(elementDict, "Stav", xw);
				elementPartId["Tacka"] = 0;
			case "Tacka":
				resetElementDict(elementDict, "Tacka", xw);
				elementPartId["Podtacka"] = 0;
			case "Podtacka":
				resetElementDict(elementDict, "Podtacka", xw);
				elementPartId["Alineja"] = 0;
			case "Alineja":
				resetElementDict(elementDict, "Alineja", xw);
			}
			xw.writeStartElement(type, "elem");
			elementPartId[type] += 1;
			xw.writeAttributeString("elem:id", generateId(elementPartId, type));
			elementDict[type] += 1;
			switch (type) {
			case "Deo":
			case "Glava":
			case "Odeljak":
			case "Pododeljak":
			case "Clan":
				xw.writeElementString("Naziv", element.value, "elem");
				break;
			case "Stav":
			case "Tacka":
			case "Podtacka":
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
		var xml = xw.flush();
		xw.close();
		

		$http({
			method : "POST",
			url : "api/act/predlogPropisa",
			data: xml,
			headers: {
			   'Content-Type': "application/xml"
			 }
		}).then(function(response) {
			console.log(response);
		});
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
		$scope.nazivPropisa = "";
		$scope.obrazlozenje = "";
		$scope.materijalniPravniOsnov = "";
		$scope.formalniPravniOsnov = "";
		$scope.nazivDonosiocaPropisa = "";
		$scope.saglasnostOrgana = "";
		$scope.saglasnostNaznaka = "";
		$scope.errorMessages = [];
		$scope.elements = [{type: "Deo", value: ""}];
	}
	
	$scope.resetAct();
});