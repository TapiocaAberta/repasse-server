/**
 * 
 * Uma classe service que permite acesso a métodos da API do TransfGov.
 * 
 * Nenhum tratamento de erro por enquanto.
 */

var SIGLAS = {
		"Acre":"AC",
		"Alagoas":"AL",
		"Amapá":"AP",
		"Amazonas":"AM",
		"Bahia":"BA",
		"Ceará":"CE",
		"Distrito Federal":"DF",
		"Espírito Santo":"ES",
		"Goiás":"GO",
		"Maranhão":"MA",
		"Mato Grosso":"MT",
		"Mato Grosso do Sul":"MS",
		"Minas Gerais":"MG",
		"Pará":"PA",
		"Paraíba":"PB",
		"Paraná":"PR",
		"Pernambuco":"PE",
		"Piauí":"PI",
		"Rio de Janeiro":"RJ",
		"Rio Grande do Norte":"RN",
		"Rio Grande do Sul":"RS",
		"Rondônia":"RO",
		"Roraima":"RR",
		"Santa Catarina":"SC",
		"São Paulo":"SP",
		"Sergipe":"SE",
		"Tocantins":"TO"
}

// Agregações suportadas para fazer a comparação de municípios
var AGREGACOES_SUPORTADAS_COMPARACAO = [
      {
    	  nome: "Área",
          valor: "AREA"		  
      },
      {
    	  nome: "Sub Área",
          valor: "SUB_FUNCAO"		  
      },
      {
    	  nome: "Ação",
          valor: "ACAO"		  
      },      
      {
    	  nome: "Programa",
          valor: "PROGRAMA"		  
      },      
      {
    	  nome: "Favorecido",
          valor: "FAVORECIDO"		  
      }
];

var CORES_COLUNAS = {
  "Uso Geral":"#CC0066",
  "Saúde":"#3333FF",
  "Educação":"#FF9900",
  "Cultura":"#00FF00",
  "Urbanismo":"#000029",
  "Assistência Social":"#E6B800"
};

// Isso é temporário até fazermos toda a carga dos dados, assim não fica limpando o cache e deixando o server lerdo em prd
var ANOS = [
	{	
		ano: 2016, meses: [ 1, 2, 3, 4, 5, 6 ],
	},            
	{	
		ano: 2015, meses: [ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 ],
	},
	{	
		ano: 2014, meses: [ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 ]
	},
	{	
		ano: 2013, meses: [ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 ]
	},
	{	
		ano: 2012, meses: [ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 ]
	},
	{	
		ano: 2011, meses: [ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 ]
	}
];

var SEPARADOR_URL = '&';

var MUNICIPIO = "{MUNICIPIO}";
var ANO = "{ANO}";
var MES = "{MES}";
var AGREGACAO = "{AGREGACAO}";
var SIGLA = "{SIGLA}";
var AREA = "{AREA}";

var URL_BASE = "rest/";
var URL_ANOS = URL_BASE + "ano";
var URL_ESTADOS = URL_BASE + "estado";
var URL_AGREGACAO = URL_BASE + "agregacao";
var URL_AREAS = URL_BASE + "area";
var URL_TRANF_POR_MES_MUN = URL_BASE + "transferencia/" + ANO + "/" + MES
		+ "/municipio/" + MUNICIPIO + "/conciso";
var URL_AGREGA_ANO_MES_MUN = URL_AGREGACAO + "/" + AGREGACAO + "/" + ANO + "/"
		+ MES + "/municipio/" + MUNICIPIO;
var URL_AGREGA_ANO_MUN = URL_AGREGACAO + "/ANO/" + ANO + "/" + AGREGACAO
		+ "/municipio/" + MUNICIPIO;
var URL_MUN_POR_ESTADO = URL_ESTADOS + "/" + SIGLA + "/municipios";
var URL_SUBFUNCOES_AREA = URL_AREAS + "/" + AREA+  "/sub-funcao";
var URL_ANO_AGREGA_MUN = URL_AGREGACAO + "/" + AGREGACAO + "/" + ANO + "/municipio/" + MUNICIPIO;

var URL_ANO_AGREGA_MUNICIPIOS = URL_AGREGACAO + "/" + AGREGACAO + "/" + ANO + "/municipios/ids;";
var URL_ANO_AGREGA_MUNICIPIOS_PERCAPITA = URL_AGREGACAO + "/percapita/" + AGREGACAO + "/" + ANO + "/municipios/ids;";

var URL_AGREGADO_COMPARA_PER_CAPITA = URL_AGREGACAO + "/percapita/{AGREGACAO}/ANO/{ANO}/municipio/{MUNICIPIO}/compara";
var URL_ANO_AGREGA_COMPARA_PERCAPITA = URL_AGREGACAO + "/percapita/ANO/{ANO}/municipio/{MUNICIPIO}/compara";

var prefixoMeses = [ "Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago",
		"Set", "Out", "Nov", "Dez" ];

var URL_PORTAL_TRANSP = "http://transparencia.gov.br/PortalTransparenciaListaAcoes.asp?Exercicio="+ANO+"&SelecaoUF=1&SiglaUF="+SIGLA+"&CodMun={SIAFI}&ordem=0"

var RepasseService = function($http) {

	this.anos = function(sucesso) {
		$http.get(URL_ANOS).success(sucesso);
	};

	this.estados = function(sucesso) {
		$http.get(URL_ESTADOS).success(function(estados) {
			estados.sort(function(a, b) {
				if (a.sigla < b.sigla)
					return -1;
				if (a.sigla > b.sigla)
					return 1;
				return 0;

			});
			for( i in estados) {
				var sigla = estados[i].sigla;
				if(sigla == 'OM' || sigla == 'EX'){
					estados.splice(i, 1);
				}
			}
			sucesso(estados);
		});
	};

	this.areas = function(sucesso) {
		$http.get(URL_AREAS).success(sucesso);
	}

	this.municipiosPorEstado = function(sigla, sucesso) {
		var url = URL_MUN_POR_ESTADO.replace(SIGLA, sigla);
		$http.get(url).success(sucesso);
	};
	
	this.subFuncoesPorArea = function(idArea, sucesso) {
		var url = URL_SUBFUNCOES_AREA.replace(AREA, idArea);
		$http.get(url).success(sucesso);
	};

	this.transfPorAnoMesMunicipio = function(ano, mes, id, sucesso) {
		var url = URL_TRANF_POR_MES_MUN.replace(ANO, ano).replace(MES, mes)
				.replace(MUNICIPIO, id);
		$http.get(url).success(sucesso);
	};
	
	this.tranfComparaPorAnoAgregadoPerCapita = function(agregacao, ano, id, sucesso) {
		var url = URL_AGREGADO_COMPARA_PER_CAPITA.replace(AGREGACAO, agregacao)
		.replace(ANO, ano).replace(MUNICIPIO, id);
		$http.get(url).success(sucesso);
	}
	
	this.tranfComparaPorAnoPerCapita = function(ano, id, sucesso) {
		var url = URL_ANO_AGREGA_COMPARA_PERCAPITA.replace(ANO, ano).replace(MUNICIPIO, id);
		$http.get(url).success(sucesso);
	}
	
	this.comparaPorAnoMesAgregaPorArea = function(ano, mes, id, sucesso) {
		var url = 'rest/agregacao/percapita/AREA/ANO/'+ano+'/'+mes+'/municipio/'+id+'/compara'
		$http.get(url).success(sucesso);		
	}

	this.agregacaoPorAnoMesMun = function(agregacao, ano, mes, municipio,
			sucesso) {
		var url = URL_AGREGA_ANO_MES_MUN.replace(AGREGACAO, agregacao).replace(
				ANO, ano).replace(MES, mes).replace(MUNICIPIO, municipio);
		$http.get(url).success(sucesso);
	};

	this.agregacaoPorAnoMun = function(agregacao, ano, municipio, sucesso) {
		var url = URL_AGREGA_ANO_MUN.replace(AGREGACAO, agregacao).replace(ANO,
				ano).replace(MUNICIPIO, municipio);
		$http.get(url).success(sucesso);
	};

	this.anoAgregadoAreaMun = function(agregacao, ano, municipio, sucesso) {
		var url = URL_ANO_AGREGA_MUN.replace(AGREGACAO, agregacao).replace(ANO,
				ano).replace(MUNICIPIO, municipio);
		$http.get(url).success(sucesso);
	};
	
	this.anoAgregadoAreaVariosMun = function(agregacao, ano, municipiosIds, sucesso) {
		var url = URL_ANO_AGREGA_MUNICIPIOS.replace(AGREGACAO, agregacao).replace(ANO,
				ano) + municipiosIds.join(";");
		$http.get(url).success(sucesso);
	};	
	
	this.anoAgregadoPerCapitaAreaVariosMun = function(agregacao, ano, municipiosIds, sucesso) {
		var url = URL_ANO_AGREGA_MUNICIPIOS_PERCAPITA.replace(AGREGACAO, agregacao).replace(ANO,
				ano) + municipiosIds.join(";");
		$http.get(url).success(sucesso);
	};
	
	this.dadosPaginados = function(urlTransfPaginada, sucesso) {
		$http.get(urlTransfPaginada).success(
				function(dados, status, headers, config) {
					var linkAnterior, linkProxima;
					headers('Link').split(",").forEach(function(l) {
						var link = parseLink(l);
						if (link.rel == 'next') {
							linkProxima = link;
						}
						if (link.rel == 'prev') {
							linkAnterior = link;
						}
					});
					sucesso(dados, linkAnterior, linkProxima);
				});
	}

	this.agregacoes = function(sucesso) {
		$http.get(URL_AGREGACAO).success(
				function(agregacoes) {
					var agregacoesFiltradas = $.grep(agregacoes, function(
							agregacao) {
						return agregacao != 'MUNICIPIO' && agregacao != 'MES'
								&& agregacao != 'ANO';
					});
					sucesso(agregacoesFiltradas);
				});
	}
	
	this.rankingPorAno = function(ano, sucesso) {
		$http.get("rest/ranking/" + ano).success(sucesso);
	}
	
	this.rankingPorAnoCidade = function(ano, sigla, nome, sucesso) {
		$http.get("rest/ranking/" + ano + "/" + sigla + "/" + nome).success(sucesso);
	}
	
	this.rankingMunicipiosSelecionados = function(ano, ids, sucesso) {
		$http.get("rest/ranking/" + ano + "/municipios/" + ids.join(",")).success(sucesso);
	}
	
	this.anoAgregadoPerCapitaAreaRegiao = function(agregacao, ano, regiao, sucesso) {
		var url = "rest/agregacao/percapita/" + agregacao + "/" + ano + "/regiao/" + regiao;
		console.log(url);
		$http.get(url).success(sucesso);
	}
	
	this.localizacao = function(sucesso) {
		$http.get("http://ipinfo.io/").then(function(loc){
			var estado = removerAcentos(loc.data.region);
			var sigla = SIGLAS[estado];
			$.each(SIGLAS, function(e, s) { 
				e = removerAcentos(e)
				if(estado == e){
					sigla = s;
				}
			});
			var mun = removerAcentos(loc.data.city).toUpperCase();
			sucesso(mun, sigla)
		});
	}
	
}

/**
 * Irá adicionar os parâmetros do mapa na URL para serem encontrados
 * posteriormente
 * 
 * @param params
 */
function salvaMapaUrl(params) {
	var novaUrl = '';
	$.each(params, function(i, v){
		novaUrl += i + '=' + v + SEPARADOR_URL;
	});
	window.location.hash = novaUrl.substring(0, novaUrl.length - 1);
}

/**
 * Irá retirar os parâmetros do mapa na URL
 * 
 * @param params
 */
function recuperaMapaUrl() {
	var todos = window.location.hash.replace('#',  '');
	var params = {};
	$.each(todos.split(SEPARADOR_URL), function(i, v){
		var campos = v.split('=');
		params[campos[0]] = campos[1];
	});
	return params;
}

/*
 * Gambiarra para fazer parse dos links do header...
 */
function parseLink(l) {
	var link = {};
	var fields = l.split(';');
	link.url = fields[0].replace('<', '').replace('>', '');
	link.rel = fields[1].replace(' rel="', '').replace('"', '');
	link.title = fields[2].replace(' title="', '').replace('"', '');
	return link;
}

function linkFontePorAno(ano, sigla, siafi) {
	return URL_PORTAL_TRANSP.replace(ANO, ano). replace(SIGLA, sigla).replace('{SIAFI}', siafi);
}

// got from https://gist.github.com/alisterlf/3490957
function removerAcentos(t) {
	var accents = 'ÀÁÂÃÄÅàáâãäåÒÓÔÕÕÖØòóôõöøÈÉÊËèéêëðÇçÐÌÍÎÏìíîïÙÚÛÜùúûüÑñŠšŸÿýŽž';
	var accentsOut = "AAAAAAaaaaaaOOOOOOOooooooEEEEeeeeeCcDIIIIiiiiUUUUuuuuNnSsYyyZz";
	var strAccents = t;
	var strAccents = strAccents.split('');
	var strAccentsOut = new Array();
	var strAccentsLen = strAccents.length;
	var hasAccents = false;
	for (var y = 0; y < strAccentsLen; y++) {
		if (accents.indexOf(strAccents[y]) != -1) {
			hasAccents = true;
			strAccentsOut[y] = accentsOut.substr(accents.indexOf(strAccents[y]), 1);
		} else
			strAccentsOut[y] = strAccents[y];
	}
	if(hasAccents) {
		strAccentsOut = strAccentsOut.join('');
		t = strAccentsOut;
	}
	return t;
}

/*
 * 
 * Visualização do IDHM de acordo com o ranking do Atlas
 * 
 * http://www.atlasbrasil.org.br/2013/pt/o_atlas/idhm/
 *
 * 
*/
function dadosIDHM(idhm){
	if(idhm < 0.499) {
		return {texto: "MUITO BAIXO", cor: "#FF0000"};
	} else if(idhm < 0.599)  {
		return {texto: "BAIXO", cor: "#FF9900"};	
	} else if(idhm < 0.699) {
		return {texto: "MÉDIO", cor: "#FFFF00"};	
	} else if(idhm < 0.799) {
		return {texto: "ALTO", cor: "#99FF00"};
	} else if(idhm < 1) {
		return {texto: "MUITO ALTO", cor: "#0000FF"};		
	};
}

/*
 * Cor do MIQLT criada de forma arbritária
 * */
function dadosMIQLT(miqlt){
	if(miqlt < 0.499) {
		return {texto: "MUITO BAIXO", cor: "#FF0000"};
	} else if(miqlt < 0.599)  {
		return {texto: "BAIXO", cor: "#FF9900"};	
	} else if(miqlt < 0.699) {
		return {texto: "MÉDIO", cor: "#FFFF00"};	
	} else if(miqlt < 0.799) {
		return {texto: "ALTO", cor: "#99FF00"};
	} else if(miqlt < 1) {
		return {texto: "MUITO ALTO", cor: "#0000FF"};		
	};
}
