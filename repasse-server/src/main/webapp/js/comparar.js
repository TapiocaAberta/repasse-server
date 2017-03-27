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
	$scope.porRegiao = false;
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
	$scope.montaCor = function(i) {
		var total = Highcharts.getOptions().colors.length;
		var c = (i - parseInt(i/total) * total)
		return { color:   Highcharts.getOptions().colors[c] };	
	}
	
	$scope.selecionaPorRegiao = function() {
		$scope.porRegiao = !$scope.porRegiao
	}		
	$scope.selecionaAgregacao = function(agregacao) {
		$scope.agregacaoSelecionada = agregacao;
		$scope.atualizaTodosGraficos();
	};

	$scope.selecionaAno = function(ano) {
		$scope.anoSelecionado = ano;
		$scope.atualizaTodosGraficos();
	};	
	
	$scope.removeCategoria = function() {
		$.each($scope.categoriaParaRemover, function(i, v){
			 var pos = $scope.todasCategorias.indexOf(v);
			 $scope.todasCategorias.splice(pos,1);
			 $scope.categoriaRemovidas.push(v);
		});
		atualizaTodosGraficos();
		$scope.categoriaParaRemover = null;
	};
	
	$scope.adicionaCategoria = function() {
		$.each($scope.categoriaParaAdicionar, function(i, v){
			 var pos =  $scope.categoriaRemovidas.indexOf(v);
			 $scope.categoriaRemovidas.splice(pos, 1);
			 $scope.todasCategorias.push(v);				
		});			
		atualizaTodosGraficos();
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
		if($scope.porRegiao) {
			$scope.regiaoSelecionada = $scope.municipioSelecionado.regiao;
			$scope.municipiosSelecionados = [];
			$scope.municipiosSelecionados.push(m);
		}
		$scope.municipioSelecionado = null;
		$scope.atualizaTodosGraficos();
	};
	
	$scope.removerMunicipio = function(i) {
		if($scope.carregando)
			return;
		$scope.porRegiao = false;
		$scope.regiaoSelecionada = null;
		$scope.municipiosSelecionados.splice(i, 1);
		$scope.atualizaTodosGraficos();
	};	
	
	$scope.limpar = function() {
		$scope.municipiosSelecionados = [];
		$scope.regiaoSelecionada = null;
		$('#graficoPerCapitaComparacaoAgregacao').html('');
		$("#graficoRanking").html('');
		$scope.atualizaTodosGraficos();
	}
	
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
	
	$scope.atualizaTodosGraficos = function () {
		if(!$scope.anoSelecionado || !$scope.municipiosSelecionados.length){ 
			salvaMapaUrl([]);
			return;
		}
		salvaParametrosSelecionados();
		$scope.carregando = true;
		$scope.carregandoRanking = true;
		$scope.ids = [];
		if($scope.porRegiao) {
			 repasseService.anoAgregadoPerCapitaAreaRegiao($scope.agregacaoSelecionada.valor, $scope.anoSelecionado.ano, $scope.regiaoSelecionada, function(agregacoes){
				 $scope.municipiosSelecionados = [];
				 $.each(agregacoes, function (i, a){
					 a.municipio.estado = a.estado;
					 $scope.municipiosSelecionados.push(a.municipio);
					 $scope.ids.push(a.municipio.id);
				 });	 
				 carregaDadosRanking();
				 $scope.agregacoesPerCapita = agregacoes;
				 montaGraficoPerCapita();
			 });
		 } else {	 
			 $.each($scope.municipiosSelecionados, function (i, m){
				 $scope.ids.push(m.id);			 
			 });
			 carregaDadosRanking();
			 repasseService.anoAgregadoPerCapitaAreaVariosMun($scope.agregacaoSelecionada.valor, $scope.anoSelecionado.ano,  $scope.ids, function(agregacoes){		 
				 $scope.agregacoesPerCapita = agregacoes;
				 montaGraficoPerCapita();
			 });
		 }
	}
	
	var montaGraficoPerCapita = function() {
		organizaColunas($scope.agregacoesPerCapita);
		 atualizaGraficoAgregacao('#graficoPerCapitaComparacaoAgregacao', 'Total per capita (R$)', $scope.agregacoesPerCapita);
	}
	
	var carregaDadosRanking = function() {
		repasseService.rankingMunicipiosSelecionados($scope.anoSelecionado.ano,  $scope.ids, function(resultados){
			 montarGraficoRanking(resultados);
			 $scope.carregandoRanking = false;
		 });		
	}
	
	var atualizaGraficoAgregacao = function(divGrafico, tituloY, agregacoes) {
			$scope.carregando = false;
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
			 
	};
	
	var atualizaTodosGraficos = function () {
		atualizaGraficoAgregacao('#graficoPerCapitaComparacaoAgregacao', 'Total per capita (R$)', $scope.agregacoesPerCapita);
			$scope.carregandoRanking = true;
			 repasseService.rankingMunicipiosSelecionados($scope.anoSelecionado.ano,  $scope.ids, function(resultados){
				 montarGraficoRanking(resultados);
				 $scope.carregandoRanking = false;
			 // vamos atualizar só depois que mostrar o ranking, pois ele é o principal agora.
			 atualizaGraficoAgregacao('#graficoPerCapitaComparacaoAgregacao', 'Total per capita (R$)', $scope.agregacoesPerCapita);
			 });
	}
	
	var salvaParametrosSelecionados = function() {
		var params = {};
		params['ano'] = $scope.anoSelecionado.ano;
		params['municipios'] = new Array();
		$.each($scope.municipiosSelecionados, function(i, m){
			params['municipios'].push(m.id + ';' + encodeURI(m.nome) + ';' + m.estado.sigla);
		});
		if($scope.regiaoSelecionada) {
			params['regiaoSelecionada'] = $scope.regiaoSelecionada
		}
		params['agregacao'] = $scope.agregacaoSelecionada.valor;
		salvaMapaUrl(params);
	}

	// por fim vamos atualizar a tela com os parâmetros de URL	
	var paramsUrl = recuperaMapaUrl(); 
	if(paramsUrl['ano']){
		$scope.anoSelecionado = $.grep($scope.anos, function(a) {
			  return a.ano == paramsUrl['ano'];
			})[0];
	} else {
		$scope.anoSelecionado = ANOS[0];
	}
	if(paramsUrl['agregacao']) {
		$.each($scope.agregacoesSuportadas, function (i, v){ 
			if(v.valor == paramsUrl['agregacao']) {
				$scope.agregacaoSelecionada = v;
			}
		});
	} 
	if(paramsUrl['regiaoSelecionada']) { 
		$scope.regiaoSelecionada = paramsUrl['regiaoSelecionada'];
		$scope.porRegiao = true;
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
		$scope.atualizaTodosGraficos();
	}
	$('#lblCarregar').each(function() {
		var elem = $(this);
		setInterval(function() {
			elem.fadeToggle(600);
		}, 400);
	});
	$('#lblCarregarRanking').each(function() {
		var elem = $(this);
		setInterval(function() {
			elem.fadeToggle(600);
		}, 400);
	});
});
// código copiado do ranking.js
function montarGraficoRanking(resultados) {
	var categorias = [], dadosRanking = [], dadosIDH = [];
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
		            text: 'Total <em>per capita</em> e IDH e posição no ranking'
		        },			       
		        subtitle: {
		            text: 'Repasse anual do governo federal (<i>per capita</i>) frente ao IDH'
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
			        	data: dadosRanking,
			        	 dataLabels: {
			        	//	 	inside:false,
		                        enabled: true,
		                        color: '#000',
		                        style: {fontWeight: 'bolder'},
		                        formatter: function() {	                        	
		                        	var nome = this.x;
		                        	var r = $.grep(resultados, function(res){ 
		                        		return res.nomeCidade == nome; 
		                        	});
		                        	return r[0].posicao + "º";
		                        }
		                    }
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