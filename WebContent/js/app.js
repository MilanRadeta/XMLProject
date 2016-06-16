(function(angular) {
	// TODO: pravni osnov može da sadrži reference, promeniti šemu
	app = angular.module('app', []);
	
	app.controller("appController", function($scope, $http, $window) {
		$scope.document = null;
		$scope.username = null;
		$scope.password = null;
		$scope.searchTerm = null;
		$scope.searchResults = [];
		$scope.showNewAct = false;
		$scope.loggedInUser = null;
		$scope.loginFail = false;

		var writeElements = function(xw) {
			var elementDict = {};
			
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
					if ($scope.amendmentAct) {
						if ($scope.amendmentType == 'Izmena') {
							// TODO: error
						}
					}
				case "Clan":
					resetElementDict(elementDict, "Clan", xw);
					//TODO: text elements, such as Referenca, SkraceniNaziv, StraniIzraz...
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
			
		};
		
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

		$scope.parts = ["Deo", "Glava", "Odeljak", "Pododeljak", "Član", "Stav", "Tačka", "Podtačka", "Alineja"];
		$scope.subparts = ["Član", "Stav", "Tačka", "Podtačka", "Alineja"];
		
		$scope.inputType = {
				"Deo": "input",
				"Glava": "input",
				"Odeljak": "input",
				"Pododeljak": "input",
				"Član": "input",
				"Stav": "textarea",	
				"Tačka": "textarea",	
				"Podtačka": "textarea",	
				"Alineja": "textarea"	
		};
		
		$scope.amendmentTypes = ["Dopuna", "Izmena", "Brisanje"];
		$scope.amendmentType = $scope.amendmentTypes[0];
		
		$scope.login = function(init) {
			$http({
				method : "POST",
				url : "api/user/login",
				data: {username: $scope.username, password:  $scope.password}
			}).then(function(response) {
				if (response.data) {
					$scope.loggedInUser = response.data;
					$scope.loginFail = false;
					$scope.username = null;
					$scope.password = null;
					$scope.getMyActs();
					$scope.getMyAmendments();
					$scope.getSuggestedActs();
				}
				else {
					if (!init) {
						$scope.loginFail = true;
					}
				}
			});
		};
		
		$scope.logout = function() {
			$scope.document = null;
			$scope.username = null;
			$scope.password = null;
			$scope.showNewAct = false;
			$scope.loggedInUser = null;
			$scope.loginFail = false;
			$http({
				method : "POST",
				url : "api/user/logout"
			});
		};
		
		$scope.getSuggestedActs = function() {
			$http({
				method : "GET",
				url : "api/act/findBy",
				params: {predlog: true, inProcedure: true}
			}).then(function(response) {
				$scope.suggestedActs = response.data;
				$scope.suggestedActs.removeable = false;
				$scope.getMyAmendments();
			});
		};
		
		$scope.getActsInProcedure = function() {
			$http({
				method : "GET",
				url : "api/act/findBy",
				params: {inProcedure: true}
			}).then(function(response) {
				$scope.actsInProcedure = response.data;
			});
		};

		$scope.getMyActs = function() {
			$http({
				method : "GET",
				url : "api/act/findBy",
				params: {username: $scope.loggedInUser.username, predlog: true, inProcedure: true}
			}).then(function(response) {
				$scope.myActs = response.data;
			});
		};

		$scope.getMyAmendments = function() {
			$http({
				method : "GET",
				url : "api/act/findAmendmentsBy",
				params: {username: $scope.loggedInUser.username, notUsvojen: true}
			}).then(function(response) {
				var myAmendments = response.data;
				$scope.myAmendments = [];
				for (var index in myAmendments) {
					var amendment = {};
					var data = myAmendments[index];
					amendment.brojPropisa = data.id.split("/")[0];
					for (var actIndex in $scope.suggestedActs) {
						var act = $scope.suggestedActs[actIndex];
						if (act.brojPropisa == amendment.brojPropisa) {
							amendment.Naziv = act.Naziv;
							break;
						}
					}
					for (var key in data.content[1]) {
						amendment.type = key;
					}
					amendment.references = data.references;
					amendment.referenceString = formatId(data.references);
					amendment.id = data.id;
					$scope.myAmendments.push(amendment);
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
			/* TODO: omogućiti samo pri usvajanju akta u celosti
			xw.writeStartElement("Preambula");
			xw.writeElementString("PravniOsnov", $scope.pravniOsnov);
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
			 */
			xw.writeElementString("Naziv", $scope.nazivPropisa, "elem");

			writeElements(xw);
	
			xw.writeElementString("Obrazlozenje", $scope.obrazlozenje, "elem");
			
			xw.writeEndElement();
			xw.writeEndDocument();
			
			console.log(xw);
			console.log(xw.getDocument());
			console.log(xw.flush());
			var xml = xw.flush();
			xw.close();
			
			$scope.uploadingNewAct = true;
			$http({
				method : "POST",
				url : "api/act/predlogPropisa",
				data: xml,
				headers: {
				   'Content-Type': "application/xml"
				 }
			}).then(function(response) {
				$scope.uploadingNewAct = false;
				$scope.errorMessages = response.data;
				if (response.data.length == 0) {
					$scope.closeNewAct();
					$scope.getMyActs();
					$scope.getSuggestedActs();
				}
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
			$scope.actInSession = null;
			$scope.nazivPropisa = "";
			$scope.obrazlozenje = "";
			$scope.materijalniPravniOsnov = "";
			$scope.formalniPravniOsnov = "";
			$scope.nazivDonosiocaPropisa = "";
			$scope.saglasnostOrgana = "";
			$scope.saglasnostNaznaka = "";
			$scope.errorMessages = [];
			$scope.elements = [{type: "Deo", value: ""}];
			$scope.amendmentAct = null;
			$scope.amendmentActParts = [];
			$scope.amendmentActPartsIds = [];
			$scope.errorMessages = [];
		}
		
		$scope.removeAct = function(act) {

			$http({
				method : "DELETE",
				url : "api/act/povuciPredlogPropisa/" + act.brojPropisa
			}).then(function(response) {
				$scope.getSuggestedActs();
				$scope.getMyActs();
				$scope.getMyAmendments();
			});
		}
		
		$scope.removeAmendment = function(amendment) {
			console.log(amendment.id);
			$http({
				method : "DELETE",
				url : "api/act/povuciPredlogAmandmana/" + amendment.id
			}).then(function(response) {
				$scope.getMyAmendments();
				$scope.actInSession = null;
			});
		}
		
		$scope.createAmendment = function(act) {
			$scope.closeNewAct();
			$scope.amendmentAct = act;
			var process = function(parent, partIndex) {
				if ($scope.parts[partIndex]) {
					var part = toAscii($scope.parts[partIndex]);
					if (parent[part] != null && parent[part].length > 0) {
						for (var index in parent[part]) {
							var elem = parent[part][index];
							$scope.amendmentActPartsIds.push(elem.id);
							process(elem, partIndex + 1);
						}
					}
					else {
						process(parent, partIndex + 1);
					}
				}
			}
			process(act, 0);
			var formatParts = function() {
				for (var actPartIndex in $scope.amendmentActPartsIds) {
					var part = $scope.amendmentActPartsIds[actPartIndex];
					formatString = formatId(part);
					$scope.amendmentActParts.push(formatString);
				}
			}
			formatParts();
		}
		
		var formatId = function(id) {
			var partIndex = -1;
			var splits = id.split('/');
			var formatString = "";
			for (var index in splits) {
				if (index == 0) {
					switch (splits[index][0]) {
					case "d":
						formatString += "Deo " + splits[index].substring(1);
						partIndex = 0;
						break;
					case "g":
						formatString += "Glava " + splits[index].substring(1);
						partIndex = 1;
						break;
					case "c":
						formatString += "Član " + splits[index].substring(1);
						partIndex = 4;
						break;
					}
				}
				else {
					if (splits[index].startsWith("c")) {
						formatString = "Član " + splits[index].substring(1);
					}
					else {
						formatString += " " + $scope.parts[parseInt(partIndex) + parseInt(index)] + " " + splits[index].substring(1);
					}
				}
			}
			return formatString;
		}
		
		$scope.saveAmendment = function() {
			if ($scope.referenceParts.length != 1) {
				$scope.errorMessages = ["Moguće je izabrati samo jednu referencu"];
				 return;
			}
			
	    	var refPart = $scope.referenceParts[0];
	    	var refPartId = $scope.amendmentActPartsIds[$scope.amendmentActParts.indexOf(refPart)];
	    	
			var xw = new XMLWriter('UTF-8', '1.0');
			xw.formatting="none";
			xw.writeStartDocument();
			xw.writeStartElement("Amandman", "am");
			xw.writeAttributeString("xmlns:elem", "http://www.skupstinans.rs/elementi");
			xw.writeAttributeString("xmlns:am", "http://www.skupstinans.rs/amandman");
			xw.writeAttributeString("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
			xw.writeAttributeString("xmlns:schemaLocation", "http://www.skupstinans.rs/amandman Amandman.xsd");
			xw.writeAttributeString("elem:references", refPartId);

			xw.writeStartElement($scope.amendmentType, "am");
			
			if ($scope.amendmentType != 'Brisanje') {
				writeElements(xw);
			}

			xw.writeEndElement();

			xw.writeElementString("Obrazlozenje", $scope.obrazlozenje, "elem");
			
			xw.writeEndElement();
			xw.writeEndElement();
			xw.writeEndDocument();

			console.log(xw);
			console.log(xw.getDocument());
			console.log(xw.flush());
			
			var xml = xw.flush();
			
			xw.close();
				
			$scope.uploadingNewAct = true;
			$http({
				method : "POST",
				url : "api/act/predlogAmandmana/" + $scope.amendmentAct.brojPropisa,
				data: xml,
				headers: {
				   'Content-Type': "application/xml"
				 }
			}).then(function(response) {
				$scope.errorMessages = response.data;
				if (response.data.length == 0) {
					$scope.closeNewAct();
					$scope.getMyAmendments();
				}
				$scope.uploadingNewAct = false;
			});
		};

		$scope.downloadPropis = function(url) { 
			$http({
				method : "GET",
				url : url,
				responseType: 'arraybuffer'
			}).then(function(response) {
				if (!response.data || response.data.byteLength == 0) {
					return;
				}
				var file = new Blob([response.data]);
				var fileURL = URL.createObjectURL(file);
				$window.open(fileURL);
			});
		}
		
		$scope.downloadXML = function(act) {
			$scope.downloadPropis("api/act/getPropisAsXML/" + act.brojPropisa);
			$scope.downloadPropis("api/act/getAmendmentsAsXML/" + act.brojPropisa);
		};	
		$scope.downloadXHTML = function(act) {
			$scope.downloadPropis("api/act/getPropisAsHTML/" + act.brojPropisa);
			$scope.downloadPropis("api/act/getAmendmentsAsHTML/" + act.brojPropisa);
		};	
		$scope.downloadPDF = function(act) {
			$scope.downloadPropis("api/act/getPropisAsPDF/" + act.brojPropisa);
			$scope.downloadPropis("api/act/getAmendmentsAsPDF/" + act.brojPropisa);
		};	
		$scope.downloadAmendmentXML = function(act) {
			$scope.downloadPropis("api/act/getAmendmentAsXML/" + act.id);
		};	
		$scope.downloadAmendmentXHTML = function(act) {
			$scope.downloadPropis("api/act/getAmendmentAsHTML/" + act.id);
		};	
		$scope.downloadAmendmentPDF = function(act) {
			$scope.downloadPropis("api/act/getAmendmentAsPDF/" + act.id);
		};	
		
		$scope.startSession = function(act) {
			$http({
				method : "GET",
				url : "api/act/getAmendmentsForId/" + act.brojPropisa
			}).then(function(response) {
				console.log(response.data);
				$scope.actInSession = act;
				$scope.actInSession.amendments = [];
				var amendments = response.data;
				if (amendments) {
					$scope.actInSession.orgAmendments = amendments;
					amendments = amendments.Amandman
					for (var index in amendments) {
						var am = amendments[index];
						am.viewRef = formatId(am.references);
						for (var contentIndex in am.content) {
							for (var key in am.content[contentIndex]) {
								switch (key) {
								case "Izmena":
								case "Brisanje":
								case "Dopuna":
									am.type = key;
								}
								if (am.type) {
									break;
								}
							}
							if (am.type) {
								break;
							}
						}
						$scope.actInSession.amendments.push(am);
					}
				}
			});
		}

		$scope.acceptActGenerally = function() {

			$http({
				method : "POST",
				url : "api/act/usvojiPropisUNacelu",
				data : $scope.actInSession.brojPropisa
			}).then(function(response) {
				$scope.actInSession = null;
				$scope.suggestedActs = [];
				$scope.getSuggestedActs();
			});
		}
		$scope.dismissAct = function() {
			$http({
				method : "DELETE",
				url : "api/act/odbaciPredlogPropisa/" + $scope.actInSession.brojPropisa
			}).then(function(response) {
				$scope.actInSession = null;
				$scope.suggestedActs = [];
				$scope.myActs = [];
				$scope.getSuggestedActs();
				$scope.getMyActs();
			});
		}
		$scope.acceptActCompletely = function() {
			$http({
				method : "POST",
				url : "api/act/usvojiPropisUCelosti/" + $scope.actInSession.brojPropisa
			}).then(function(response) {
				$scope.actInSession = null;
				$scope.suggestedActs = [];
				$scope.myActs = [];
				$scope.getSuggestedActs();
				$scope.getMyActs();
			});
		}
		
		$scope.confirmAmendments = function() {
			var data = [];
			var ams = $scope.actInSession.amendments;
			$scope.actInSession.errorStatus = false;
			for (var index in ams) {
				var am = ams[index];
				if (am.usvojen == null || am.usvojen == undefined) {
					$scope.actInSession.errorStatus = true;
					return;
				}
				if (am.usvojen) {
					data.push(am.id);
				}
			}
			
			if (!$scope.actInSession.errorStatus) {
				$http({
					method : "POST",
					url : "api/act/usvojiPropisUPojedinostima/" + 	$scope.actInSession.brojPropisa,
					data : data
				}).then(function(response) {
					$scope.actInSession = null;
					$scope.suggestedActs = [];
					$scope.myActs = [];
					$scope.getSuggestedActs();
					$scope.getMyActs();
				});
			}
		}
		
		$scope.login(true);
		$scope.resetAct();
	});
}(angular));