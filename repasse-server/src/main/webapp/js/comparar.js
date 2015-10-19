angular.module('RepasseApp', []).factory('repasseService',
		function($http) {
			return new RepasseService($http)
		}).controller('CompararController', function($scope, repasseService) {
	Highcharts.setOptions({
	    lang: {
	        decimalPoint: ',',
	        thousandsSep: '.'
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

	$scope.selecionaAno = function(ano) {
		$scope.anoSelecionado = ano;
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

	$scope.ehAgregacaoSelecionada = function(agregacao) {
	    return $scope.agregacaoSelecionada === agregacao;
	}		
	
	$scope.ehAnoSelecionado = function(ano) {
		return $scope.anoSelecionado === ano;
	}
	$scope.agregacoesSuportadas = [];
	for(i in AGREGACOES_SUPORTADAS_COMPARACAO) {
		var a = AGREGACOES_SUPORTADAS_COMPARACAO[i];
		if(a.valor != 'FAVORECIDO') {
			$scope.agregacoesSuportadas.push(a);
		}
	};
	
	repasseService.estados(function(d) {
		$scope.estados = d;
	});
	/* REMOVIDO TEMPORARIAMENTE
	repasseService.anos(function(d) {
		$scope.anos = d;
	});
	*/
	$scope.anos = ANOS;
	$scope.carregaMunicipios = function() {
		var sigla = $scope.estadoSelecionado.sigla;
		repasseService.municipiosPorEstado(sigla, function(d) {
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
		if(!$scope.anoSelecionado || !$scope.municipiosSelecionados.length){ 
			salvaMapaUrl([]);
			return;
		}
		$scope.carregando = true;
		var params = {};
		params['ano'] = $scope.anoSelecionado.ano;
		params['municipios'] = new Array();
		$.each($scope.municipiosSelecionados, function(i, m){
			params['municipios'].push(m.id + ';' + encodeURI(m.nome) + ';' + m.estado.sigla);
		});
		params['agregacao'] = $scope.agregacaoSelecionada.valor;
		salvaMapaUrl(params);
		
		 var ids = [];
		 
		 $.each($scope.municipiosSelecionados, function (i, m){
			 ids.push(m.id);
		 });	
		 
		 repasseService.anoAgregadoAreaVariosMun($scope.agregacaoSelecionada.valor, $scope.anoSelecionado.ano, ids, function(agregacoes){
			 organizaColunas(agregacoes);
			 $scope.agregacoes = agregacoes;
			 atualizaTodosGraficosAgregacao();
		 });
		 
		 repasseService.anoAgregadoPerCapitaAreaVariosMun($scope.agregacaoSelecionada.valor, $scope.anoSelecionado.ano, ids, function(agregacoes){
			 organizaColunas(agregacoes);
			 $scope.agregacoesPerCapita = agregacoes;
			 atualizaTodosGraficosAgregacao();
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
	//	atualizaGraficoAgregacao('#graficoComparacaoAgregacao', 'Total (R$)', $scope.agregacoes);
		atualizaGraficoAgregacao('#graficoPerCapitaComparacaoAgregacao', 'Total per capita (R$)', $scope.agregacoesPerCapita);
	}
	
	var atualizaGraficoAgregacao = function(divGrafico, tituloY, agregacoes) {
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
			 $(divGrafico).highcharts({
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
			                text: tituloY
			            }
			        },
			        tooltip: {
			            headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
			            pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
			                '<td style="padding:0"><b>R$ {point.y:,.3f} </b></td></tr>',
			            footerFormat: '</table>',
			            shared: true,
			            useHTML: true
			        },
			        series: series
			    });
			 $scope.carregando = false;
			 
	};

	// por fim vamos atualizar a tela com os parâmetros de URL	
	var paramsUrl = recuperaMapaUrl(); 
	if(paramsUrl['ano']) {
		$.each($scope.anos, function (i, v){
			if(i.ano == paramsUrl['ano']){
				$scope.anoSelecionado = i;
			}
		})		
	}
	if(!$scope.anoSelecionado) {
		$scope.anoSelecionado = ANOS[0];
	}
	if(paramsUrl['agregacao']) {
		$.each($scope.agregacoesSuportadas, function (i, v){ 
			if(v.valor == paramsUrl['agregacao']) {
				$scope.agregacaoSelecionada = v;
			}
		});
	} 
	if(!$scope.agregacaoSelecionada){
		$scope.agregacaoSelecionada = AGREGACOES_SUPORTADAS_COMPARACAO[0];
	}
	if(paramsUrl['municipios']) {
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
		$scope.atualizaGraficos();
	}
	$('#lblCarregar').each(function() {
		var elem = $(this);
		setInterval(function() {
			elem.fadeToggle(600);
		}, 400);
	});
});