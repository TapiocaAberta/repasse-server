var app = angular.module('TransfGovApp', []).factory('transfGovService',
		function($http) {
			return new TransfGovService($http)
		}).controller('CompararController', function($scope, transfGovService) {
			
	$scope.municipiosSelecionados = new Array();		
	
	transfGovService.estados(function(d) {
		$scope.estados = d;
	});
	
	transfGovService.areas(function(d){
		$scope.areas = d;
	});
	
	transfGovService.anos(function(d) {
		$scope.anos = d;
	});
	
	$scope.carregaMunicipios = function() {
		var sigla = $scope.estadoSelecionado.sigla;
		transfGovService.municipiosPorEstado(sigla, function(d) {
			$scope.municipios = d;
		});
	};
	
	$scope.addMunicipio = function() {
		var m = $scope.municipioSelecionado;
		m.estado = $scope.estadoSelecionado;
		if($scope.municipiosSelecionados.indexOf(m) == -1) {
			$scope.municipiosSelecionados.push(m);		
		}
		$scope.municipioSelecionado = null;
		$scope.atualizaGraficos();
	};
	
	$scope.removerMunicipio = function(i) {
		$scope.municipiosSelecionados.splice(i, 1);
		$scope.atualizaGraficos();
	};
	
	$scope.atualizaGraficos = function() {
		if(!$scope.anoSelecionado) 
			return;
		atualizaGraficoArea();	
	};
	
	var atualizaGraficoArea = function() {
		 var ids = new Array();
		 $.each($scope.municipiosSelecionados, function (i, m){
			 ids.push(m.id);
		 });	
		 transfGovService.anoAgregadoAreaVariosMun('AREA', $scope.anoSelecionado.ano, ids, function(agregacoes){
			 var series = new Array();
			 var categorias = new Array();
			 
			 for(i in $scope.areas) {
				 categorias.push( $scope.areas[i].nome);
			 }
			 $.each(agregacoes, function(i, agreg){
				 	var valoresPorArea = new Array();
				 	$.each($scope.areas, function(i, a){
				 		valoresPorArea.push(agreg.dadosAgregados[a.nome]);
				 	});
				 	series.push({
				 		name: agreg.municipio.nome + " - " + agreg.estado.sigla,
				 		data: valoresPorArea
				 	}); 
			 });
			 var grafico = $('#graficoComparacaoAreas').highcharts({
			        chart: {
			            type: 'column'
			        },
			        title: {
			            text: 'Transferências por área'
			        },
			        subtitle: {
			            text: 'Comparação entre municípios'
			        },
			        xAxis: {
			            categories: categorias,
			            crosshair: true
			        },
			        yAxis: {
			            min: 0,
			            title: {
			                text: 'Total (R$)'
			            }
			        },
			        tooltip: {
			            headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
			            pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
			                '<td style="padding:0"><b>R$ {point.y} </b></td></tr>',
			            footerFormat: '</table>',
			            shared: true,
			            useHTML: true
			        },
			        series: series
			    });
			 
		 });
		 
	};
});