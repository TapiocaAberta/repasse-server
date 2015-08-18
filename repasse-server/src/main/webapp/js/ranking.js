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
		});
	}
});

function montarGraficoRanking(ranking) {
	var ano = ranking.ano;
	var titulo = ranking.nome;
	var categorias = [];
	var dados = [];
	for(i in ranking.resultados) {
		var res = ranking.resultados[i];
		categorias.push(res.nomeCidade);
		dados.push(
			 res.valorPerCapita
		);
	}
	$("#graficoRanking").highcharts({
			 chart: {
		            type: 'column'
		        },
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
		        yAxis: {
		            title: {
		                text: 'Valor <i>per capita</i>'
		            },
			        labels: {
			            enabled: false
			        }
		        },
		        tooltip: {
		            headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
		            pointFormat: '<tr><td style="color:{series.color};padding:0">Valor <i>per capita</i>: </td>' +
		                '<td style="padding:0"><b>R$ {point.y:,.3f} </b></td></tr>',
		            footerFormat: '</table>',
		            shared: true,
		            useHTML: true
		        },
		        series: [{
		        	data: dados
		        }]
		});
	
}