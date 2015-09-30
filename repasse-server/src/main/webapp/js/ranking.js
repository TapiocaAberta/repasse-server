angular.module('RepasseApp', []).factory('repasseService', function($http) {
	return new RepasseService($http)
}).controller('RankingController', function($scope, repasseService) {
	Highcharts.setOptions({
		lang : {
			decimalPoint : ',',
			thousandsSep : '.'
		}
	}); /*
		 * REMOVIDO TEMPORARIAMENTE repasseService.anos(function(d) {
		 * $scope.anos = d; });
		 */
	$scope.anos = ANOS;
	
	$scope.carregaRanking = function() {
		var ano  = $scope.anoSelecionado.ano;
		$scope.carregando = true;
		repasseService.rankingPorAno(ano, function(ranking){
			$scope.carregando = false;
			montarGraficoRanking(ranking);
			$scope.resultadosRanking = ranking.resultados;
		});
	}
	$scope.anoSelecionado = ANOS[0];
	$scope.carregaRanking();
	$('#lblCarregar').each(function() {
		var elem = $(this);
		setInterval(function() {
			elem.fadeToggle(600);
		}, 400);
	});
});

function montarGraficoRanking(ranking) {
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
		            text: 'Municípios que mais receberam repasse do governo federal <i>per capita</i>'
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