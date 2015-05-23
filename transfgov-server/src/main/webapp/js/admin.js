var appExplorar = angular.module('AppAdmin', [ 'datatables' ]);
appExplorar.controller('AdminController', function($scope, $http, $interval) {
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
});