var appExplorar = angular.module('AppExplorar', [ 'datatables' ]);

function inicializa($scope, $http) {
	$('#abasPainel a').click(function(e) {
		e.preventDefault();
		$(this).tab('show');
	});

	$('#abasPainel a[href=#tabAgregacao]').click(function(e) {
		$http.get("./rest/agregacao").success(function(agregacoes) {
			$scope.agregacoes =  $.grep(agregacoes, function(agregacao) {
				return agregacao != 'MUNICIPIO' && agregacao != 'MES';
			});
		});
	});
}

appExplorar.controller('ExplorarController', function($scope, $http) {
	inicializa($scope, $http);
	$http.get("./rest/ano").success(function(anos) {
		$scope.anos = anos;
	});
	$http.get("./rest/estado").success(function(estados) {
		$scope.estados = estados;
	});
	$scope.carregaMunicipios = function() {
		var sigla = $scope.estadoSelecionado.sigla;
		var urlEstados = "./rest/estado/" + sigla + "/municipios";
		$http.get(urlEstados).success(function(municipios) {
			$scope.municipios = municipios;
		});
	}
	$scope.carregaApp = function() {
		var ano = $scope.anoSelecionado.ano;
		var mes = $scope.mesSelecionado;
		var id = $scope.municipioSelecionado.id;
		var uriTransf = "rest/transferencia/" + ano + "/" + mes + "/municipio/"
				+ id;
		$http.get(uriTransf).success(function(transferencias) {
			$scope.anoBusca = ano;
			$scope.mesBusca = mes;
			$scope.municipioBusca = $scope.municipioSelecionado;
			$scope.estadoBusca = $scope.estadoSelecionado;
			$scope.transferencias = transferencias;
			$scope.carregaGraficosAgregacao();
		});
	}
	
	$scope.carregaGraficosAgregacao = function() {
		$scope.gerandoGraficoAgregacao = true;
		var ano = $scope.anoSelecionado.ano;
		var mes = $scope.mesSelecionado;
		var id = $scope.municipioSelecionado.id;
		var a = $scope.agregacaoSelecionada;
		var uriAgregacao =  a + "/"+ ano+ "/" + mes + "/municipio/"+ id;
		$http.get("./rest/agregacao/" + uriAgregacao).success(function(agregacao) {
			$scope.dadosAgregados= agregacao.dadosAgregados;
			var dadosGrafico = new Array();
			for(i in agregacao.dadosAgregados) {
				dadosGrafico.push({
                    name: i,
                    y: agregacao.dadosAgregados[i]
                });
			}
			$('#containerPizzaAgregacao').highcharts({
		        chart: {
		            plotBackgroundColor: null,
		            plotBorderWidth: null,
		            plotShadow: false
		        },
		        title: {
		            text: 'Dados agregados por ' + a
		        },
		        tooltip: {
		            pointFormat: '<b>{point.percentage:.1f}%</b>'
		        },
		        plotOptions: {
		            pie: {
		                allowPointSelect: true,
		                cursor: 'pointer',
		                dataLabels: {
		                    enabled: false,
		                },
	                    showInLegend: true
		            }
		        },
		        series: [{
		            type: 'pie',
		            name: 'Dados por ' + a,
		            data: dadosGrafico
		        }]
		    });
		});
		$scope.gerandoGraficoAgregacao = false;
	}

});