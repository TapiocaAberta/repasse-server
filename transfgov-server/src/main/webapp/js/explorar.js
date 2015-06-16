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
	var nomesSeries = new Array();
	// coleta as categorias (meses disponíveis) e todas séries
	agregacoesAno.forEach(function(agregacaoAno) {
		ano = agregacaoAno.ano;
		categorias.push(prefixoMeses[agregacaoAno.mes - 1]);
		// coleta todas séries possíveis
		for (a in agregacaoAno.dadosAgregados) {
			if(nomesSeries.indexOf(a) == -1)
				nomesSeries.push(a);
		}		
	});
	//para cada mês, vamos ver se tem valor, senão tiver, é 0!
	agregacoesAno.forEach(function(agregacaoAno) {
		for (a in nomesSeries) {
			var s = nomesSeries[a];
			if (!seriesMap[s])
				seriesMap[s] = new Array();
			var valor = 0;
			if(agregacaoAno.dadosAgregados[s]) {
				valor = agregacaoAno.dadosAgregados[s];
			}
			seriesMap[s].push(valor);
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
			text : 'Transferências no ano ' + ano,
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
			valuePrefix : "R$ ",
			headerFormat: "{series.name} <br/>", 
	        pointFormat: '<b>R$ {point.y:,.3f}</b>'
	    },
		yAxis : {
			title : {
				text : 'Valor'
			},
			min: 0,
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
			Highcharts.setOptions({
			    lang: {
			        decimalPoint: ',',
			        thousandsSep: '.'
			    }
			});		
			$('#abasPainel a').click(function(e) {
				e.preventDefault();
				$(this).tab('show');
			});
			var paramsUrl = recuperaMapaUrl();
			transfGovService.agregacoes(function(agregacoes) {
				$scope.agregacoes = agregacoes;
				$scope.agregacaoSelecionada = $scope.agregacoes[0];
			});

			$scope.prefixoMeses = prefixoMeses;
			
			/* REMOVIDO TEMPORARIAMENTE ATÉ RESOLVER #74 
			transfGovService.anos(function(anos) {
				$scope.anos = anos;
				$.each(anos, function(i, ano) { 
					if(ano.ano == paramsUrl['ano']){
						$scope.anoSelecionado = ano;
					}
				});
			});*/
			$scope.anos = ANOS;
			$.each(ANOS, function(i, ano) { 
				if(ano.ano == paramsUrl['ano']){
					$scope.anoSelecionado = ano;
				}
			});
			
			transfGovService.estados(function(estados) {
				$scope.estados = estados;
				$.each(estados, function(i, estado) {
					if(estado.sigla == paramsUrl['sigla']) {
						$scope.estadoSelecionado = estado;
						$scope.carregaMunicipios();
					}
				});
			});
			$scope.carregaMunicipios = function() {
				transfGovService.municipiosPorEstado(
						$scope.estadoSelecionado.sigla, function(municipios) {
							$scope.municipios = municipios;
							$.each(municipios, function(i, m) {
								if(m.id == paramsUrl['id']){
									$scope.municipioSelecionado = m;
									$scope.carregaApp();
								}
							});
				});

			}
			$scope.carregaApp = function() {
				$scope.carregaAgregacaoAno();
				$scope.municipioBusca = $scope.municipioSelecionado;				
				var mapa = {};
				mapa['sigla'] = $scope.estadoSelecionado.sigla;
				mapa['id'] = $scope.municipioSelecionado.id;
				mapa['ano'] =  $scope.anoSelecionado.ano;
				salvaMapaUrl(mapa);
				$scope.linkFontePorAno = linkFontePorAno($scope.anoSelecionado.ano, 
						$scope.estadoSelecionado.sigla, 
					    $scope.municipioSelecionado.codigoSIAFI);
			}

			$scope.listenerAgregacao = function() {
				$scope.carregaAgregacaoAno();
				$scope.carregaGraficosAgregacao();
				carregaDadosComparacao();
			}

			$scope.carregaAgregacaoAno = function() {
				var ano = $scope.anoSelecionado.ano;
				var id = $scope.municipioSelecionado.id;
				var agreg = $scope.agregacaoSelecionada;
				$scope.anoBusca = ano;
				$scope.mesSelecionado = $scope.anoSelecionado.meses[0];
				$scope.municipioBusca = $scope.municipioSelecionado;
				transfGovService.agregacaoPorAnoMun(agreg, ano, id,
						criaGraficoAnoArea);
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
			
			var montaGraficoComparacao = function(elemento, titulo, dados) {
				var categorias = [];
				var series = new Array();
				for(i in dados) {
					var serie = {};
					serie.name = i;
					serie.data = [];
					for(mes in dados[i]) {
						categorias.push(prefixoMeses[mes-1]);
						serie.data.push(dados[i][mes]); 
					}
					series.push(serie);
				}
				criaGraficoBarra(elemento, titulo, categorias, series);
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
							var dados = new Array();
							for (i in agregacao.dadosAgregados) {
								categorias.push(i);
								valores.push(agregacao.dadosAgregados[i]);
								dados.push({
									name: i,
									y:	agregacao.dadosAgregados[i]
								});
							}
							criaGraficoBarra('#containerGraficoAgregacao', '', categorias, [
								{
									name: $scope.municipioSelecionado.nome,
									data : valores
								}
							]);
							$('#containerGraficoAgregacaoPizza').highcharts(
									{
										title : {
											text : ''
										},
										chart : {
											type : 'pie',
								            options3d: {
								                enabled: true
								            }
										},
										tooltip : {
									        pointFormat: 'R$ {point.y:,.3f}'
									    },
										series : [ {
											name: "",
											type: 'pie',
											data : dados
										} ]
									});							
						});				
				$scope.gerandoGraficoAgregacao = false;
			}
		});

function criaGraficoBarra(elemento, titulo, categorias, series) {
	$(elemento).highcharts(
			{
				title : {
					text : titulo
				},
				chart : {
					type : 'bar'
				},
				tooltip : {
					headerFormat: '{point.key} - {series.name}<br />',
			        pointFormat: 'R$ {point.y:,.3f}'
			    },
				xAxis : {
					categories : categorias
				},
				plotOptions : {
					series : {
						allowPointSelect : true
					}
				},
				series : series
			});
}