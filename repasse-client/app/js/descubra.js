var repasseApp = angular.module('RepasseApp', []).factory('repasseService', [ "$http", function($http) {
	return new RepasseService($http)
}]);


repasseApp.controller('DescubraController',["$scope",  "repasseService", function($scope, repasseService) {
	Highcharts.setOptions({
		lang : {
			decimalPoint : ',',
			thousandsSep : '.'
		}
	}); 
	
	repasseService.estados(function(d) {
		$scope.estados = d;
	});
	
	repasseService.grupoIndicadores(function(r) {
		$scope.grupoIndicadores = r;
	});
	
	$scope.carregaMunicipios = function() {
		var sigla = $scope.estadoSelecionado.sigla;
		repasseService.municipiosPorEstado(sigla, function(d) {
			$scope.municipios = d;
		});
	};

	$scope.carregaIndicadores = function() {
		repasseService.indicadores($scope.grupoSelecionado.nome, function(r) {
			$scope.indicadores = r;
		});
	};	

	$scope.carregaFocos = function() {
		repasseService.focosIndicador($scope.grupoSelecionado.nome, $scope.indicadorSelecionado.nome, function(r) {
			$scope.focos = r;
		});
	};		
	
}]);


//TODO: criar e finalizar o gráfico
function montarGraficoDescubra(ranking) {
	var ano = ranking.ano;
	var titulo = ranking.nome;
	var categorias = [], dadosRanking = [], dadosIDH = [];
	// pegamos só os 10 primeiros para montar o gráfico
	var resultados = ranking.resultados.slice(0, 10);
	for(i in resultados) {
		var res = resultados[i];
		categorias.push(res.nomeCidade);
		dadosRanking.push(
			 res.valorPerCapita
		);
		dadosIDH.push(
				 res.idhm
		);		
	}
	$("#graficoRanking").highcharts({

		        title: {
		            text: titulo
		        },			       
		        subtitle: {
		            text: 'Municipios que mais receberam repasse do governo federal (<i>per capita</i>) frente ao IDH'
		        },
		        xAxis: {
		            categories: categorias,
		            crosshair: true
		        },
		        yAxis: [{
		            title: {
		                text: 'Valor <i>per capita</i>'
		            },
			        labels: {			          
		                format: 'R$ {value}',
		                style: {
		                    color: Highcharts.getOptions().colors[0]
		                }
			            
			        }
		        },{
	        	  title: {
		                text: 'IDH'
		            },
		            max: 1.0,
		            min: 0.4, 
			        labels: {
			            style: {
		                    color: Highcharts.getOptions().colors[1]
		                }
			        },
			        opposite: true
		        }],
		        tooltip: {
		            shared: true
		        },
		        series: [{
		            	yAxis: 0,
		        		name: 'Total <i>per capita</i>',
			        	type: 'column',
			        	data: dadosRanking
		        	},
		        	{
		        		yAxis: 1,
		        		name: 'IDH',
		        		type: 'spline',
		        		data:dadosIDH
		        	}
		        ]
		});
	
}
