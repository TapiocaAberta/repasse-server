var appExplorar = angular.module('AppAdmin', []);
appExplorar.controller('AdminController', ["$scope", "$http", "$interval", function($scope, $http, $interval) {
    
    createTableFilter("#tabelaCarga", "#filtroCarga");
    
	var respostaCarga = function(msg) {
		$scope.msgResposta = msg;
		$scope.fazendoAcao = false;
		atualizaCargas();
	}
	var sucesso = function(msg) {
		$scope.sucesso = true;
		respostaCarga(msg);
	}
	var erro = function(msg) {
		$scope.sucesso = false;
		respostaCarga(msg);
	}
	$scope.realizaCarga = function() {
		var url = "./rest/carga/transferencia/" + $scope.anoCarga + "/"
				+ $scope.mesCarga;
		$scope.fazendoAcao = true;
		$http.post(url).success(sucesso).error(erro);
	}

	$http.post("./rest/carga/transferencia/resumo").success(function(resumo) {
		$scope.dadosResumo = resumo;
	});
	
	$scope.apagaDadosTransferencia = function() {
		var url = "./rest/transferencia/" + $scope.anoCarga + "/"
				+ $scope.mesCarga;
		$scope.fazendoAcao = true;
		$http.delete(url).success(sucesso).error(erro);
	}
	var atualizaCargas = function() {
		$http.get("./rest/carga/transferencia").success(
				function(infoCargasTransf) {
					$scope.infoCargasTransf = infoCargasTransf;
					$('#tblCargaTransf').hide().fadeIn(1000);
				});
	};
	$scope.carregaDadosPop = function() {
		$scope.cargaPopulacao = true;
		$scope.resultadoCargaPopulacao = null;
		$http.post("./rest/carga/populacao").success(
				function(resultado) {
					$("#divResCargaPop").html(resultado);
					$scope.resultadoCargaPopulacao = resultado;
					$scope.cargaPopulacao = false;
				});
	};
	$scope.atualizaCargas = atualizaCargas;
	var relogioCargaInfo;
	$scope.mudaAtualizarCarga = function() {
		if ($scope.atualizarCargaFlag) {
			relogioCargaInfo = $interval(atualizaCargas, 10000);
		} else {
			$interval.cancel(relogioCargaInfo);
		}
	}
	atualizaCargas();
}]);

// copying the function here, won't add a whole JS due this function, right?
function createTableFilter(table, field) {
    var filter =  $(field);
    filter.keyup(function () {
        var rex = new RegExp(filter.val(), 'i');
        $(table + ' tr').hide();
        $(table +' tr').filter(function () {
            return rex.test($(this).text());
        }).show();
      });
}
