package org.sjcdigital.repasse.resource.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.core.PathSegment;

import org.sjcdigital.repasse.agregacao.AgregacaoController;
import org.sjcdigital.repasse.model.agregacao.Agregacao;
import org.sjcdigital.repasse.model.agregacao.TipoAgregacao;
import org.sjcdigital.repasse.model.base.Area;
import org.sjcdigital.repasse.model.base.DadosMunicipio;
import org.sjcdigital.repasse.model.base.Estado;
import org.sjcdigital.repasse.model.base.Municipio;
import org.sjcdigital.repasse.model.transferencia.Transferencia;
import org.sjcdigital.repasse.resource.AgregacaoResource;
import org.sjcdigital.repasse.service.impl.AreaService;
import org.sjcdigital.repasse.service.impl.DadosMunicipioService;
import org.sjcdigital.repasse.service.impl.EstadoService;
import org.sjcdigital.repasse.service.impl.MunicipioService;
import org.sjcdigital.repasse.service.impl.TransferenciaService;
import org.sjcdigital.repasse.utils.JaxrsUtils;

public class AgregacaoResourceImpl implements AgregacaoResource {
	
	@Inject
	AgregacaoController agregacaoController;
	
	@Inject
	MunicipioService municipioService;
	
	@Inject
	TransferenciaService transferenciaService;

	@Inject
	EstadoService estadoService;

	@Inject
	AreaService areaService;

	@Inject
	DadosMunicipioService dadosMunicipioService;
	
	public TipoAgregacao[] todasAgregacoes() {
		return TipoAgregacao.values();
	}
	
	public Agregacao agregaPorAnoMesMunicipio(TipoAgregacao tipoAgregacao, int ano, int mes, long idMunicipio) {
		JaxrsUtils.lanca404SeFalso(transferenciaService.temTranferencia(ano, mes), String.format(MSG_NAO_HA_DADOS_ANO_MES, ano, mes));
		Municipio municipio = JaxrsUtils.lanca404SeNulo(municipioService.buscarPorId(idMunicipio), Municipio.class);
		List<Transferencia> transferencias = transferenciaService.buscarPorAnoMesMunicipio(ano, mes, municipio);
		return agregacaoController.agregaPorTipo(ano, mes, municipio.getEstado(), municipio, tipoAgregacao, transferencias);
	}
	
	public Agregacao agregaPorAnoMunicipio(TipoAgregacao tipoAgregacao, int ano, long idMunicipio) {
		return criarAgregacaoPorAnoMunicipio(tipoAgregacao, ano, idMunicipio, false);
	}
	

	public Agregacao agregaPorAnoMesAreaMunicipio(long idArea, int ano, int mes, long idMunicipio) {
		JaxrsUtils.lanca404SeFalso(transferenciaService.temTranferencia(ano, mes), String.format(MSG_NAO_HA_DADOS_ANO_MES, ano, mes));
		Area area = JaxrsUtils.lanca404SeNulo(areaService.buscarPorId(idArea), Area.class);
		Municipio municipio = JaxrsUtils.lanca404SeNulo(municipioService.buscarPorId(idMunicipio), Municipio.class);
		List<Transferencia> transferencias = transferenciaService.buscarPorAnoMesAreaMunicipio(ano, mes, area, municipio);
		return agregacaoController.agregaPorTipo(ano, 0, municipio.getEstado(), municipio, TipoAgregacao.SUB_FUNCAO, transferencias);
	}
	
	public Agregacao agregaPorAnoEstado(int ano,  int mes, String siglaEstado) {
		Estado estado = JaxrsUtils.lanca404SeNulo(estadoService.buscaEstadoPorSigla(siglaEstado), Estado.class);
		List<Transferencia> transferencias = transferenciaService.buscarPorAnoMesEstado(ano, mes, estado);
		return agregacaoController.agregaPorTipo(ano, 0, estado, null, TipoAgregacao.MUNICIPIO, transferencias);
	}
	
	@Override
	public Map<Object, Double> agrupaPorAno(int ano, long municipioId) {
		return criaAgrupamentoPorAno(ano, municipioId, false);
	}

	@Override
	public List<Agregacao> agrupaPorAnoArea(TipoAgregacao tipoAgregacao, int ano, long municipioId) {
		Municipio municipio = JaxrsUtils.lanca404SeNulo(municipioService.buscarPorId(municipioId), Municipio.class);
		List<Integer> meses = transferenciaService.mesesDisponiveis(ano);
		List<Agregacao> agregacoes = new ArrayList<>();
		for (int mes : meses) {
			List<Transferencia> transferencias = transferenciaService.buscarPorAnoMesMunicipio(ano, mes, municipio);
			Agregacao agregacao = agregacaoController.agregaPorTipo(ano, mes, municipio.getEstado(), municipio, tipoAgregacao, transferencias);
			agregacoes.add(agregacao);
		}
		return agregacoes;
	}

	@Override
	public List<Agregacao> agrupaPorAnoArea(TipoAgregacao tipoAgregacao,
			int ano, PathSegment pathSegment){
		Set<Long> ids = pathSegment.getMatrixParameters().keySet().stream().map(Long::parseLong).collect(Collectors.toSet());
		return criarAgregacaoPorAnoMunicipio(tipoAgregacao, ano, ids, false);
	}

	@Override
	public Agregacao agregaPorAnoMunicipioPercapita(
			TipoAgregacao tipoAgregacao, int ano, long idMunicipio) {
		return criarAgregacaoPorAnoMunicipio(tipoAgregacao, ano, idMunicipio, true);
	}

	@Override
	public List<Agregacao> agrupaPerCapitaPorAnoArea(
			TipoAgregacao tipoAgregacao, int ano, PathSegment pathSegment) {
		Set<Long> ids = pathSegment.getMatrixParameters().keySet().stream().map(Long::parseLong).collect(Collectors.toSet());
		return criarAgregacaoPorAnoMunicipio(tipoAgregacao, ano, ids, true);
	}
	
	@Override
	public Map<Object, Double> agrupaPorAnoPerCapita(int ano, long municipioId) {
		return criaAgrupamentoPorAno(ano, municipioId, true);
	}	
		

	
	@Override
	public Map<String, Map<Object, Double>> comparaComMediaNacionalPerCapita(int ano, long municipioId) {
		return criaComparacaoComMediaNacional(ano, municipioId, true);
	}

	@Override
	public Map<String, Map<Object, Double>> comparaComMediaNacional(int ano, long municipioId) {
		return criaComparacaoComMediaNacional(ano, municipioId, false);
	}
	
	public Map<String, Map<Object, Double>> criaComparacaoComMediaNacional(int ano, long municipioId, boolean ehPerCapita) {
		Municipio municipio = JaxrsUtils.lanca404SeNulo(municipioService.buscarPorId(municipioId), Municipio.class);
		Map<String, Map<Object, Double>> dados = new HashMap<>();
		dados.put(municipio.getNome(), criaAgrupamentoPorAno(ano, municipioId, ehPerCapita));
		dados.put("Média Nacional", transferenciaService.buscarPorAnoAgregado(TipoAgregacao.ANO, ano, ehPerCapita));
		return dados;
	}
	
	public Map<Object, Double> criaAgrupamentoPorAno(int ano, long municipioId, boolean ehPerCapita) {
		Municipio municipio = JaxrsUtils.lanca404SeNulo(municipioService.buscarPorId(municipioId), Municipio.class);
		JaxrsUtils.lanca404SeFalso(transferenciaService.temTranferencia(ano), String.format(String.format(MSG_NAO_HA_DADOS_ANO, ano), ano));
		return transferenciaService.buscarPorAnoMunicipioAgregaPorMes(ano, municipio, ehPerCapita);
	}

	@Override
	public Map<String, Map<Object, Double>> comparaComMediaNacionalPerCapita(
			TipoAgregacao tipoAgregacao, int ano, long municipioId) {
		Municipio municipio = JaxrsUtils.lanca404SeNulo(municipioService.buscarPorId(municipioId), Municipio.class);
		JaxrsUtils.lanca404SeFalso(transferenciaService.temTranferencia(ano), String.format(MSG_NAO_HA_DADOS_ANO, ano));
		Agregacao dadosMunAgregados = criarAgregacaoPorAnoMunicipio(tipoAgregacao, ano, municipioId, true);
		Map<String, Map<Object, Double>> dados = new HashMap<>();
		dados.put(municipio.getNome(), dadosMunAgregados.getDadosAgregados());
		dados.put("Média Nacional", transferenciaService.buscarPorAnoAgregado(tipoAgregacao, ano, true));
		return dados;
	}

	@Override
	public Map<String, Map<Object, Double>> comparaComMediaNacionalAgrupadoPorAreaPerCapita(
			int ano, int mes, long municipioId) {
		Municipio municipio = JaxrsUtils.lanca404SeNulo(municipioService.buscarPorId(municipioId), Municipio.class);
		JaxrsUtils.lanca404SeFalso(transferenciaService.temTranferencia(ano), String.format(MSG_NAO_HA_DADOS_ANO, ano));	
		DadosMunicipio dadosMun = dadosMunicipioService.buscaPorAnoMunicipioOuMaisRecente(ano, municipio);
		List<Transferencia> transferencias = transferenciaService.buscarPorAnoMesMunicipio(ano, mes, municipio);
		Agregacao dadosMunAgregados = agregacaoController.agregaPercapitaPorTipo(ano, 0, municipio.getEstado(), municipio, TipoAgregacao.AREA, transferencias, dadosMun.getPopulacao());
		Map<String, Map<Object, Double>> dados = new HashMap<>();
		dados.put(municipio.getNome(), dadosMunAgregados.getDadosAgregados());
		dados.put("Média Nacional", transferenciaService.buscarPorAnoMesAgregadoPorArea(ano, mes, true));
		return dados;
	}

	@Override
	public List<Agregacao> agrupaPorAnoAreaRegiao(TipoAgregacao tipoAgregacao,
			int ano, String regiao) {
		List<Municipio> municipios = municipioService.buscaMunicipiosPorRegiao(regiao);
		Set<Long> ids = municipios.stream().map(Municipio::getId).collect(Collectors.toSet());
		return criarAgregacaoPorAnoMunicipio(tipoAgregacao, ano, ids, false);
	}

	@Override
	public List<Agregacao> agrupaPerCapitaPorAnoAreaRegiao(
			TipoAgregacao tipoAgregacao, int ano, String regiao) {
		List<Municipio> municipios = municipioService.buscaMunicipiosPorRegiao(regiao);
		Set<Long> ids = municipios.stream().map(Municipio::getId).collect(Collectors.toSet());
		return criarAgregacaoPorAnoMunicipio(tipoAgregacao, ano, ids, true);
		
	}
	private List<Agregacao> criarAgregacaoPorAnoMunicipio(TipoAgregacao tipoAgregacao,
			int ano, Set<Long> ids, boolean ehPercapita) {
		JaxrsUtils.lanca404SeFalso(transferenciaService.temTranferencia(ano), String.format(MSG_NAO_HA_DADOS_ANO, ano));	
		List<Agregacao> agregacoes = new ArrayList<>();
		for (Long idMunicipio : ids) {
			Municipio municipio = JaxrsUtils.lanca404SeNulo(municipioService.buscarPorId(idMunicipio), Municipio.class);
			List<Transferencia> transferencias = transferenciaService.buscarPorAnoMunicipio(ano, municipio);
			Agregacao agregacao;
			if(ehPercapita) {
				DadosMunicipio dados = dadosMunicipioService.buscaPorAnoMunicipioOuMaisRecente(ano, municipio);
				agregacao = agregacaoController.agregaPercapitaPorTipo(ano, 0, municipio.getEstado(), municipio, tipoAgregacao, transferencias, dados.getPopulacao());
			} else {
				agregacao = agregacaoController.agregaPorTipo(ano, 0, municipio.getEstado(), municipio, tipoAgregacao, transferencias);
			}
			agregacoes.add(agregacao);
		}
		return agregacoes;	
	}

	private Agregacao criarAgregacaoPorAnoMunicipio(TipoAgregacao tipoAgregacao, int ano, long idMunicipio, boolean ehPerCapita) {
		JaxrsUtils.lanca404SeFalso(transferenciaService.temTranferencia(ano), String.format(MSG_NAO_HA_DADOS_ANO, ano));
		Municipio municipio = JaxrsUtils.lanca404SeNulo(municipioService.buscarPorId(idMunicipio), Municipio.class);
		List<Transferencia> transferencias = transferenciaService.buscarPorAnoMunicipio(ano, municipio);
		Agregacao agregacao;
		if(ehPerCapita) {
			DadosMunicipio dados = dadosMunicipioService.buscaPorAnoMunicipioOuMaisRecente(ano, municipio);
			agregacao = agregacaoController.agregaPercapitaPorTipo(ano, 0, municipio.getEstado(), municipio, tipoAgregacao, transferencias, dados.getPopulacao());
		} else {
			agregacao = agregacaoController.agregaPorTipo(ano, 0, municipio.getEstado(), municipio, tipoAgregacao, transferencias);
		}
		return agregacao;
	}
	
}