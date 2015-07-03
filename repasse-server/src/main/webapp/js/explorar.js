var appExplorar = angular.module('RepasseApp', [ 'datatables' ]).factory(
		'repasseService', function($http) {
			return new RepasseService($http)
		}).run(function(DTDefaultOptions) {
	DTDefaultOptions.setDisplayLength(30);
});

var prefixoMeses = [ "Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago",
		"Set", "Out", "Nov", "Dez" ];

appExplorar.controller('ExplorarController',
		function($scope, repasseService) {
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
			repasseService.anos(function(anos) {
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
			
			repasseService.estados(function(estados) {
				$scope.estados = estados;
				$.each(estados, function(i, estado) {
					if(estado.sigla == paramsUrl['sigla']) {
						$scope.estadoSelecionado = estado;
						$scope.carregaMunicipios();
					}
				});
			});
			$scope.carregaMunicipios = function() {
				$scope.municipioSelecionado = null;
				$scope.municipios = null;
				repasseService.municipiosPorEstado(
						$scope.estadoSelecionado.sigla, function(municipios) {
							var nomesMunicipios = [];
							$scope.municipios = municipios;
							$.each(municipios, function(i, m) {
								nomesMunicipios.push(m.nome);
								if(m.id == paramsUrl['id']){
									$scope.municipioSelecionado = m;
									$("#municipiosAutoComplete").val(m.nome);
									$scope.carregaApp();
								}
							});
							$("#municipiosAutoComplete").autocomplete({
							      source: nomesMunicipios,
							      change: function(evt, ui){
							    	  var e = evt.target
							    	  if(!$scope.municipioSelecionado){
							    		  e.value = "";
							    	  }
							    	  if($scope.municipioSelecionado.nome != e.value){
							    		  e.value = $scope.municipioSelecionado.nome;
							    	  }
							      },
							      select: function(evt, ui) {
							    	  $.each(municipios, function(i, m) {							    
							    		  if(m.nome == ui.item.value){							    			  
							    			  $scope.municipioSelecionado = m;
							    			  $scope.$apply();
							    		  }
							    	  });
							      }
						    });							
				});
			};			
			$scope.carregaApp = function() {
				if(!$scope.municipioSelecionado) 
					return;
				$scope.carregaAgregacaoAno();
				$scope.carregaGraficosAgregacao();
				$scope.carregaDadosMes();
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
				repasseService.agregacaoPorAnoMun(agreg, ano, id, criaGraficoAnoArea);
			};
			$scope.carregaDadosMes = function() {
				if (!$scope.mesSelecionado) {
					return;
				}
				var ano = $scope.anoSelecionado.ano;
				var mes = $scope.mesSelecionado;
				var id = $scope.municipioSelecionado.id;
				$scope.carregaGraficosAgregacao();
				repasseService.transfPorAnoMesMunicipio(ano, mes, id,
						function(transfMes) {
							$scope.transferenciasMes = transfMes;
						});
			};
			$scope.carregaTransferencias = function(url) {
				repasseService.dadosPaginados(url, function(transfMes,
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
				repasseService.comparaPorAnoMesAgregaPorArea(ano, mes, id, function(dados){
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
					criaGraficoBarra('#containerGraficoAgregacao', 'Comparação <em>per capita</em>', categorias, series);					
				});
				repasseService.agregacaoPorAnoMesMun('AREA', ano, mes, id,
						function(agregacao) {
							$scope.dadosAgregados = agregacao.dadosAgregados;							
							var dados = new Array();
							$scope.valorTotalMes = 0;
							for (i in agregacao.dadosAgregados) {
								$scope.valorTotalMes += agregacao.dadosAgregados[i];
								dados.push({
									name: i,
									y:	agregacao.dadosAgregados[i],
									color: CORES_COLUNAS[i]
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
		series.push({
			name : s,
			data : seriesMap[s],
			color: CORES_COLUNAS[s]
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
