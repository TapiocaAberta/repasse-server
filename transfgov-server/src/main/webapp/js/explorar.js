var appExplorar = angular.module('AppExplorar', [ 'datatables' ]).run(
		function(DTDefaultOptions) {
			DTDefaultOptions.setDisplayLength(30);
		});

var prefixoMeses = [ "Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago",
		"Set", "Out", "Nov", "Dez" ];

function inicializa($scope, $http) {
	$('#abasPainel a').click(function(e) {
		e.preventDefault();
		$(this).tab('show');
	});
	$http.get("./rest/agregacao").success(
			function(agregacoes) {
				$scope.agregacoes = $.grep(agregacoes, function(agregacao) {
					return agregacao != 'MUNICIPIO' && agregacao != 'MES'
							&& agregacao != 'ANO';
				});
				$scope.agregacaoSelecionada = $scope.agregacoes[0];
			});

}

function criaGraficoAnoArea(agregacoesAno) {
	var categorias = new Array();
	var seriesMap = {};
	var ano;
	var series = new Array();
	agregacoesAno.forEach(function(agregacaoAno) {
		ano = agregacaoAno.ano;
		categorias.push(agregacaoAno.mes);
		for (a in agregacaoAno.dadosAgregados) {
			if (!seriesMap[a])
				seriesMap[a] = new Array();
			seriesMap[a].push(agregacaoAno.dadosAgregados[a]);
		}
	});
	for (s in seriesMap) {
		series.push({
			name : s,
			data : seriesMap[s]
		});
	}
	$('#divGraficoAreaPorAno').highcharts({
		title : {
			text : 'Tranferências no ano ' + ano,
			x : -20
		// center
		},
		subtitle : {
			text : 'Agregadas todas as transferências no ano de ' + ano,
			x : -20
		},
		xAxis : {
			title : {
				text : 'Mês'
			},
			categories : categorias
		},
		tooltip : {
			valuePrefix : "R$ "
		},
		yAxis : {
			title : {
				text : 'Valor'
			},
			plotLines : [ {
				value : 0,
				width : 1,
				color : '#808080'
			} ]
		},
		legend : {
			itemWidth : 200,
			layout : 'vertical',
			align : 'right',
			verticalAlign : 'middle',
			borderWidth : 0
		},
		series : series
	});
}

appExplorar.controller('ExplorarController', function($scope, $http) {
	inicializa($scope, $http);
	$scope.prefixoMeses = prefixoMeses;
	$http.get("./rest/ano").success(function(anos) {
		$scope.anos = anos;
	});
	$http.get("./rest/estado").success(function(estados) {
		estados.sort(function(a, b) {
			if (a.sigla < b.sigla)
				return -1;
			if (a.sigla > b.sigla)
				return 1;
			return 0;

		});
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
		$scope.carregaAgregacaoAno();
		$scope.municipioBusca = $scope.municipioSelecionado;
	}

	$scope.listenerAgregacao = function() {
		$scope.carregaAgregacaoAno();
		$scope.carregaGraficosAgregacao();
	}

	$scope.carregaAgregacaoAno = function() {
		var ano = $scope.anoSelecionado.ano;
		var id = $scope.municipioSelecionado.id;
		var agreg = $scope.agregacaoSelecionada;
		$scope.anoBusca = ano;
		$scope.mesSelecionado = $scope.anoSelecionado.meses[0];
		$scope.municipioBusca = $scope.municipioSelecionado;
		var uriTransfAno = "rest/agregacao/ANO/" + ano + "/" + agreg
				+ "/municipio/" + id;
		$http.get(uriTransfAno).success(criaGraficoAnoArea);
		$scope.carregaDadosMes();
	}

	$scope.carregaDadosMes = function() {
		if (!$scope.mesSelecionado) {
			return;
		}
		var ano = $scope.anoSelecionado.ano;
		var mes = $scope.mesSelecionado;
		var id = $scope.municipioSelecionado.id;
		var uriTransfMes = "rest/transferencia/" + ano + "/" + mes
				+ "/municipio/" + id;
		$scope.carregaTransferencias(uriTransfMes);
		$scope.carregaGraficosAgregacao();
	}

	$scope.carregaTransferencias = function(url) {
		$http.get(url).success(function(data, status, headers, config) {
			$scope.transferenciasMes = data;
			var linkAnterior, linkProxima;
			headers('Link').split(",").forEach(function(l) {
				var link = parseLink(l);
				if (link.rel == 'next') {
					linkProxima = link;
				}
				if (link.rel == 'prev') {
					linkAnterior = link;
				}
			});
			$scope.linkAnterior = linkAnterior;
			$scope.linkProxima = linkProxima;
		});
	}

	$scope.carregaGraficosAgregacao = function() {
		var a = $scope.agregacaoSelecionada;
		var ano = $scope.anoSelecionado.ano;
		var mes = $scope.mesSelecionado;
		var id = $scope.municipioSelecionado.id;
		if (!a)
			return;
		$scope.gerandoGraficoAgregacao = true;
		var uriAgregacao = a + "/" + ano + "/" + mes + "/municipio/" + id;
		$http.get("./rest/agregacao/" + uriAgregacao).success(
				function(agregacao) {
					$scope.dadosAgregados = agregacao.dadosAgregados;
					var categorias = new Array();
					var valores = new Array();
					for (i in agregacao.dadosAgregados) {
						categorias.push(i);
						valores.push(agregacao.dadosAgregados[i]);
					}
					$('#containerGraficoAgregacao').highcharts(
							{
								title : {
									text : 'Dados agregados no mês ' + mes
											+ ' por ' + a + ''
								},
								chart : {
									type : 'bar'
								},
								xAxis : {
									categories : categorias
								},
								plotOptions : {
									series : {
										allowPointSelect : true
									}
								},
								series : [ {
									data : valores
								} ]
							});
				});

		$scope.gerandoGraficoAgregacao = false;
	}

});

/*
 * Gambiarra para fazer parse dos links do header...
 */
function parseLink(l) {
	var link = {};
	var fields = l.split(';');
	link.url = fields[0].replace('<', '').replace('>', '');
	link.rel = fields[1].replace(' rel="', '').replace('"', '');
	link.title = fields[2].replace(' title="', '').replace('"', '');
	return link;
}