angular.module('TransfGovApp', []).factory('transfGovService',
		function($http) {
			return new TransfGovService($http)
		}).controller('CompararController', function($scope, transfGovService) {
	
			
	$scope.configurarCategorias = function() {
		$("#dialogoSelecionarCategorias").dialog({
		      resizable: false,
		      height:650,
		      width:800,
		      modal: true,
		      Cancel: function() {
		            this.dialog( "close" );
		      }
		});
	};
			
	$scope.selecionaAgregacao = function(agregacao) {
		$scope.agregacaoSelecionada = agregacao;
		$scope.atualizaGraficos();
	};
	
	$scope.removeCategoria = function() {
		$.each($scope.categoriaParaRemover, function(i, v){
			 var pos = $scope.todasCategorias.indexOf(v);
			 $scope.todasCategorias.splice(pos,1);
			 $scope.categoriaRemovidas.push(v);
		});

		 atualizaGraficoAgregacao($scope.agregacoes);
	};
	
	$scope.adicionaCategoria = function() {
		 var pos =  $scope.categoriaRemovidas.indexOf($scope.categoriaParaAdicionar);
		 $scope.categoriaRemovidas.splice(pos, 1);
		 $scope.todasCategorias.push($scope.categoriaParaAdicionar);		
		 atualizaGraficoAgregacao($scope.agregacoes);
	};

	$scope.isSelected = function(agregacao) {
	    return $scope.agregacaoSelecionada === agregacao;
	}	
	$scope.municipiosSelecionados = new Array();	
	$scope.agregacoesSuportadas = AGREGACOES_SUPORTADAS_COMPARACAO;
	$scope.agregacaoSelecionada = AGREGACOES_SUPORTADAS_COMPARACAO[0];
	
	transfGovService.estados(function(d) {
		$scope.estados = d;
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
		 var ids = new Array();
		 
		 $.each($scope.municipiosSelecionados, function (i, m){
			 ids.push(m.id);
		 });	
		 transfGovService.anoAgregadoAreaVariosMun($scope.agregacaoSelecionada.valor, $scope.anoSelecionado.ano, ids, function(agregacoes){
			 $scope.categoriaRemovidas = new Array();
			 $scope.todasCategorias = new Array();
			 $scope.agregacoes = agregacoes;
			 $.each(agregacoes, function(i, agreg){
				 $.each(agreg.dadosAgregados, function(cat, valor){
					 if($scope.todasCategorias.indexOf(cat) == -1) {
						 $scope.todasCategorias.push(cat);
					 }					 
				 });
			 });
			 atualizaGraficoAgregacao(agregacoes);
		 });
	};
	
	var atualizaGraficoAgregacao = function(agregacoes) {
			 var series = new Array();
			 var categorias = $scope.todasCategorias;	 
			 categorias.sort();
			 $.each(agregacoes, function(i, agreg){
				 	var valoresPorArea = new Array();
				 	$.each(categorias, function(i, a){
				 		var valor = agreg.dadosAgregados[a];
				 		if(!valor) {
				 			valor = 0;
				 		}
				 		valoresPorArea.push(valor);
				 	});
				 	series.push({
				 		name: agreg.municipio.nome + " - " + agreg.estado.sigla,
				 		data: valoresPorArea
				 	}); 
			 });
			 var grafico = $('#graficoComparacaoAgregacao').highcharts({
			        chart: {
			            type: 'column'
			        },
			        title: {
			            text: 'Transferências por ' + $scope.agregacaoSelecionada.nome
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
			                '<td style="padding:0"><b>R$ {point.y:.2f} </b></td></tr>',
			            footerFormat: '</table>',
			            shared: true,
			            useHTML: true
			        },
			        series: series
			    });
			 
	};	
});