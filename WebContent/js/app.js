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
		xw.formatting="none";
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
		xw.writeElementString("Naziv", $scope.nazivDonosiocaPropisa, "elem");
		xw.writeEndElement();
		if ($scope.saglasnostOrgana || $scope.saglasnostNaznaka) {
			xw.writeStartElement("Saglasnost");
			xw.writeElementString("Naziv", $scope.saglasnostOrgana, "elem");
			xw.writeElementString("Naznaka", $scope.saglasnostNaznaka);
			xw.writeEndElement();
		}
		xw.writeEndElement();

		xw.writeElementString("Naziv", $scope.nazivPropisa, "elem");
		
		var elementDict = {};
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
		}
		var resetElementDict = function(elementDict, key, xw) {
			if (elementDict[key]) {
				elementDict[key] = 0;
				xw.writeEndElement();
			}
		}
		for (var index in $scope.elements) {
			var element = $scope.elements[index];
			var type = toAscii(element.type);
			switch (type) {
			case "Deo":
				resetElementDict(elementDict, "Deo", xw);
			case "Glava":
				resetElementDict(elementDict, "Glava", xw);
			case "Odeljak":
				resetElementDict(elementDict, "Odeljak", xw);
			case "Pododeljak":
				resetElementDict(elementDict, "Pododeljak", xw);
			case "Clan":
				resetElementDict(elementDict, "Clan", xw);
			case "Stav":
				resetElementDict(elementDict, "Stav", xw);
			case "Tacka":
				resetElementDict(elementDict, "Tacka", xw);
			case "Podtacka":
				resetElementDict(elementDict, "Podtacka", xw);
			case "Alineja":
				resetElementDict(elementDict, "Alineja", xw);
			}
			xw.writeStartElement(type, "elem");
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
			var part = toAscii($scope.parts[index]);
			resetElementDict(elementDict, part, xw);
		}
		

		xw.writeElementString("Obrazlozenje", $scope.obrazlozenje, "elem");
		
		xw.writeEndElement();
		xw.writeEndDocument();
		
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
			$scope.errorMessages = response.data;
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