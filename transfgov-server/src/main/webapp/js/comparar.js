angular.module('TransfGovApp', []).factory('transfGovService',
		function($http) {
			return new TransfGovService($http)
		}).controller('CompararController', function($scope, transfGovService) {
			
		    Highcharts.setOptions({
		        lang: {
		            decimalPoint: ',',
		            thousandsSep: ' '
		        }
		    });		
	$scope.municipiosSelecionados = new Array();
	$scope.configurarCategorias = function() {
		var d = $("#dialogoSelecionarCategorias").dialog({
		      resizable: false,
		      height:700,
		      width:800,
		      modal: true,
		      buttons: {
		    	  Ok: function() {
		    		  d.dialog( "close" );
		    	  }
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
		atualizaTodosGraficosAgregacao();
		$scope.categoriaParaRemover = null;
	};
	
	$scope.adicionaCategoria = function() {
		$.each($scope.categoriaParaAdicionar, function(i, v){
			 var pos =  $scope.categoriaRemovidas.indexOf(v);
			 $scope.categoriaRemovidas.splice(pos, 1);
			 $scope.todasCategorias.push(v);				
		});			
		atualizaTodosGraficosAgregacao();
		$scope.categoriaParaAdicionar = null;
	};

	$scope.isSelected = function(agregacao) {
	    return $scope.agregacaoSelecionada === agregacao;
	}		
	$scope.agregacoesSuportadas = AGREGACOES_SUPORTADAS_COMPARACAO;
	$scope.agregacaoSelecionada = AGREGACOES_SUPORTADAS_COMPARACAO[0];
	
	transfGovService.estados(function(d) {
		$scope.estados = d;
	});
	/* REMOVIDO TEMPORARIAMENTE
	transfGovService.anos(function(d) {
		$scope.anos = d;
	});
	*/
	$scope.anos = ANOS;
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
		var params = {};
		params['ano'] = $scope.anoSelecionado.ano;
		params['municipios'] = new Array();
		$.each($scope.municipiosSelecionados, function(i, m){
			params['municipios'].push(m.id + ';' + encodeURI(m.nome) + ';' + m.estado.sigla);
		});
		params['agregacao'] = $scope.agregacaoSelecionada.nome + ';' + $scope.agregacaoSelecionada.valor;
		salvaMapaUrl(params);
		
		 var ids = new Array();
		 
		 $.each($scope.municipiosSelecionados, function (i, m){
			 ids.push(m.id);
		 });	
		 
		 transfGovService.anoAgregadoAreaVariosMun($scope.agregacaoSelecionada.valor, $scope.anoSelecionado.ano, ids, function(agregacoes){
			 organizaColunas(agregacoes);
			 $scope.agregacoes = agregacoes;
			 atualizaGraficoAgregacao('#graficoComparacaoAgregacao', agregacoes);
		 });
		 
		 transfGovService.anoAgregadoPerCapitaAreaVariosMun($scope.agregacaoSelecionada.valor, $scope.anoSelecionado.ano, ids, function(agregacoes){
			 organizaColunas(agregacoes);
			 $scope.agregacoesPerCapita = agregacoes;
			 atualizaGraficoAgregacao('#graficoPerCapitaComparacaoAgregacao', agregacoes);
		 });
	};
	
	var organizaColunas = function (agregacoes) {
		 $scope.categoriaRemovidas = new Array();
		 $scope.todasCategorias = new Array();
		 $.each(agregacoes, function(i, agreg){
			 $.each(agreg.dadosAgregados, function(cat, valor){
				 if($scope.todasCategorias.indexOf(cat) == -1) {
					 $scope.todasCategorias.push(cat);
				 }					 
			 });
		 });		
	}
	
	var atualizaTodosGraficosAgregacao = function () {
		atualizaGraficoAgregacao('#graficoComparacaoAgregacao', $scope.agregacoes);
		atualizaGraficoAgregacao('#graficoPerCapitaComparacaoAgregacao', $scope.agregacoesPerCapita);

	}
	
	var atualizaGraficoAgregacao = function(divGrafico, agregacoes) {
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
			 var grafico = $(divGrafico).highcharts({
			        chart: {
			            type: 'column'
			        },
			        title: {
			            text: 'Transferências por ' + $scope.agregacaoSelecionada.nome + ' em ' + $scope.anoSelecionado.ano
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
			 
	};
	
	// por fim vamos atualizar a tela com os parâmetros de URL
	
	var paramsUrl = recuperaMapaUrl(); 
	if(paramsUrl['ano'] && paramsUrl['municipios']){
		$scope.anoSelecionado = { ano: paramsUrl['ano']};
		$.each(paramsUrl['municipios'].split(','), function (i, v) {		
			var campos = v.split(';');
			$scope.municipiosSelecionados.push({
				id: campos[0],
				nome: decodeURI(campos[1]),
				estado: {
					sigla: campos[2]
				}
			});
		});
		if(paramsUrl['agregacao']) {
			var campos = paramsUrl['agregacao'].split(';');
			$scope.agregacaoSelecionada = {
					nome: campos[0],
					valor: campos[1]
			};
			
		}
		$scope.atualizaGraficos();
	}
});