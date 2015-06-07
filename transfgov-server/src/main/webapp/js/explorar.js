var appExplorar = angular.module('TransfGovApp', [ 'datatables' ]).factory(
		'transfGovService', function($http) {
			return new TransfGovService($http)
		}).run(function(DTDefaultOptions) {
	DTDefaultOptions.setDisplayLength(30);
});

var prefixoMeses = [ "Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago",
		"Set", "Out", "Nov", "Dez" ];

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

appExplorar.controller('ExplorarController',
		function($scope, transfGovService) {
			$('#abasPainel a').click(function(e) {
				e.preventDefault();
				$(this).tab('show');
			});
			transfGovService.agregacoes(function(agregacoes) {
				$scope.agregacoes = agregacoes;
				$scope.agregacaoSelecionada = $scope.agregacoes[0];
			});

			$scope.prefixoMeses = prefixoMeses;
			transfGovService.anos(function(anos) {
				$scope.anos = anos;
			});
			transfGovService.estados(function(estados) {
				$scope.estados = estados;
			});
			$scope.carregaMunicipios = function() {
				transfGovService.municipiosPorEstado(
						$scope.estadoSelecionado.sigla, function(municipios) {
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
				transfGovService.agregacaoPorAnoMun(agreg, ano, id, criaGraficoAnoArea);
				$scope.carregaDadosMes();
			}

			$scope.carregaDadosMes = function() {
				if (!$scope.mesSelecionado) {
					return;
				}
				var ano = $scope.anoSelecionado.ano;
				var mes = $scope.mesSelecionado;
				var id = $scope.municipioSelecionado.id;
				transfGovService.transfPorAnoMesMunicipio(ano, mes, id,
						function(transfMes, linkAnterior, linkProxima) {
							$scope.transferenciasMes = transfMes;
							$scope.linkAnterior = linkAnterior;
							$scope.linkProxima = linkProxima;
						});
				$scope.carregaGraficosAgregacao();
			}

			$scope.carregaTransferencias = function(url) {
				transfGovService.dadosPaginados(url, function(transfMes,
						linkAnterior, linkProxima) {
					$scope.transferenciasMes = transfMes;
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
				transfGovService.agregacaoPorAnoMesMun(a, ano, mes, id,
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
											text : 'Dados agregados no mês '
													+ mes + ' por ' + a + ''
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