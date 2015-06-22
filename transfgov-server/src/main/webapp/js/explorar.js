var appExplorar = angular.module('TransfGovApp', [ 'datatables' ]).factory(
		'transfGovService', function($http) {
			return new TransfGovService($http)
		}).run(function(DTDefaultOptions) {
	DTDefaultOptions.setDisplayLength(30);
});

var prefixoMeses = [ "Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago",
		"Set", "Out", "Nov", "Dez" ];

appExplorar.controller('ExplorarController',
		function($scope, transfGovService) {
			Highcharts.setOptions({
			    lang: {
			        decimalPoint: ',',
			        thousandsSep: '.',
			        numericSymbols:  [ " mil" , " milhões" , " bilhões" , "T" , "P" , "E"]
			    }
			});	
			$scope.agregacoesSuportadas = AGREGACOES_SUPORTADAS_COMPARACAO;
			$scope.selecionaAgregacao = function(agregacao) {
				$scope.agregacaoSelecionada = agregacao;
				$scope.carregaAgregacaoAno();
			};
			$scope.selecionaAno = function(ano) {
				$scope.anoSelecionado = ano;
				$scope.carregaApp();
			};	
			$scope.ehAgregacaoSelecionada = function(agregacao) {
			    return $scope.agregacaoSelecionada === agregacao;
			};
			$scope.ehAnoSelecionado = function(ano) {
				return $scope.anoSelecionado === ano;
			};
			$scope.anoSelecionado = ANOS[0];
			$scope.agregacaoSelecionada = $scope.agregacoesSuportadas[0];
			var paramsUrl = recuperaMapaUrl();
			$scope.prefixoMeses = prefixoMeses;
			
			/* REMOVIDO TEMPORARIAMENTE ENQUANTO FAZEMOS CARGAS
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
			};			
			$scope.carregaApp = function() {
				$scope.carregaAgregacaoAno();
				$scope.carregaGraficosAgregacao();
				$scope.municipioBusca = $scope.municipioSelecionado;				
				var mapa = {};
				mapa['sigla'] = $scope.estadoSelecionado.sigla;
				mapa['id'] = $scope.municipioSelecionado.id;
				mapa['ano'] =  $scope.anoSelecionado.ano;
				salvaMapaUrl(mapa);
				$scope.linkFontePorAno = linkFontePorAno($scope.anoSelecionado.ano, 
						$scope.estadoSelecionado.sigla, 
					    $scope.municipioSelecionado.codigoSIAFI);
			};
			$scope.carregaAgregacaoAno = function() {
				var ano = $scope.anoSelecionado.ano;
				var id = $scope.municipioSelecionado.id;
				var agreg = $scope.agregacaoSelecionada.valor;
				$scope.anoBusca = ano;
				$scope.mesSelecionado = $scope.anoSelecionado.meses[0];
				$scope.municipioBusca = $scope.municipioSelecionado;
				transfGovService.agregacaoPorAnoMun(agreg, ano, id, criaGraficoAnoArea);
			};
			$scope.carregaDadosMes = function() {
				if (!$scope.mesSelecionado) {
					return;
				}
				var ano = $scope.anoSelecionado.ano;
				var mes = $scope.mesSelecionado;
				var id = $scope.municipioSelecionado.id;
				$scope.carregaGraficosAgregacao();
				transfGovService.transfPorAnoMesMunicipio(ano, mes, id,
						function(transfMes, linkAnterior, linkProxima) {
							$scope.transferenciasMes = transfMes;
							$scope.linkAnterior = linkAnterior;
							$scope.linkProxima = linkProxima;
						});
			};
			$scope.carregaTransferencias = function(url) {
				transfGovService.dadosPaginados(url, function(transfMes,
						linkAnterior, linkProxima) {
					$scope.transferenciasMes = transfMes;
					$scope.linkAnterior = linkAnterior;
					$scope.linkProxima = linkProxima;
				});
			};			
			$scope.carregaGraficosAgregacao = function() {
				var a = $scope.agregacaoSelecionada;
				var ano = $scope.anoSelecionado.ano;
				var mes = $scope.mesSelecionado;
				var id = $scope.municipioSelecionado.id;
				if (!a)
					return;
				$scope.gerandoGraficoAgregacao = true;
				transfGovService.comparaPorAnoMesAgregaPorArea(ano, mes, id, function(dados){
					var categorias = [];
					var series = new Array();
					for(i in dados[$scope.municipioSelecionado.nome]) {
						categorias.push(i);
					}
					for(i in dados) {
						var serie = {};
						serie.name = i;
						serie.data = [];
						for(j in categorias) {
							var area = categorias[j];
							serie.data.push(dados[i][area]);
						}
						series.push(serie);
					}
					// renomear categoria de outras transferências para nome mais popular
					for(i in categorias) {
						if(categorias[i] == 'Encargos Especiais') {
							categorias[i] = 'Uso Geral';
							break;	
						}
					}
					criaGraficoBarra('#containerGraficoAgregacao', 'Comparação <em>per capita</em>', categorias, series);					
				});
				transfGovService.agregacaoPorAnoMesMun('AREA', ano, mes, id,
						function(agregacao) {
							$scope.dadosAgregados = agregacao.dadosAgregados;							
							var dados = new Array();
							$scope.valorTotalMes = 0;
							for (i in agregacao.dadosAgregados) {
								var nome = i; 
								if(i == 'Encargos Especiais') {
									nome  = 'Uso Geral';
								}
								$scope.valorTotalMes += agregacao.dadosAgregados[i];
								dados.push({
									name: nome,
									y:	agregacao.dadosAgregados[i]
								});
							}					
							$('#containerGraficoAgregacaoPizza').highcharts(
									{
										title : {
											text : 'Valores Totais'
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
		var nome = s;
		if(nome == 'Encargos Especiais')
			nome = 'Uso Geral';
		series.push({
			name : nome,
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
			labels: {
				formatter: function() {
					return 'R$ ' + this.value.toLocaleString();
				}				
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
