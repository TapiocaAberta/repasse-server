package org.jugvale.transfgov.resource.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.PathSegment;

import org.jugvale.transfgov.agregacao.AgregacaoController;
import org.jugvale.transfgov.model.agregacao.Agregacao;
import org.jugvale.transfgov.model.agregacao.TipoAgregacao;
import org.jugvale.transfgov.model.base.Area;
import org.jugvale.transfgov.model.base.DadosMunicipio;
import org.jugvale.transfgov.model.base.Estado;
import org.jugvale.transfgov.model.base.Municipio;
import org.jugvale.transfgov.model.transferencia.Transferencia;
import org.jugvale.transfgov.resource.AgregacaoResource;
import org.jugvale.transfgov.service.impl.AreaService;
import org.jugvale.transfgov.service.impl.DadosMunicipioService;
import org.jugvale.transfgov.service.impl.EstadoService;
import org.jugvale.transfgov.service.impl.MunicipioService;
import org.jugvale.transfgov.service.impl.TransferenciaService;
import org.jugvale.transfgov.utils.JaxrsUtils;

@Stateless
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
	private AreaService areaService;

	@Inject
	private DadosMunicipioService dadosMunicipioService;
	
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
		return criarAgregacaoPorAnoMunicipio(tipoAgregacao, ano, pathSegment, false);
	}

	@Override
	public Agregacao agregaPorAnoMunicipioPercapita(
			TipoAgregacao tipoAgregacao, int ano, long idMunicipio) {
		return criarAgregacaoPorAnoMunicipio(tipoAgregacao, ano, idMunicipio, true);
	}

	@Override
	public List<Agregacao> agrupaPerCapitaPorAnoArea(
			TipoAgregacao tipoAgregacao, int ano, PathSegment pathSegment) {
		return criarAgregacaoPorAnoMunicipio(tipoAgregacao, ano, pathSegment, true);
	}
	
	@Override
	public Map<Object, Double> agrupaPorAnoPerCapita(int ano, long municipioId) {
		return criaAgrupamentoPorAno(ano, municipioId, true);
	}	
		
	private List<Agregacao> criarAgregacaoPorAnoMunicipio(TipoAgregacao tipoAgregacao,
			int ano, PathSegment pathSegment, boolean ehPercapita) {
		JaxrsUtils.lanca404SeFalso(transferenciaService.temTranferencia(ano), String.format(MSG_NAO_HA_DADOS_ANO, ano));
		List<Agregacao> agregacoes = new ArrayList<>();
		Set<String> ids = pathSegment.getMatrixParameters().keySet();
		for (String id : ids) {
			long idMunicipio = Long.parseLong(id);
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
		Agregacao dadosMunAgregados = criarAgregacaoPorAnoMunicipio(TipoAgregacao.AREA, ano, municipioId, true);
		Map<String, Map<Object, Double>> dados = new HashMap<>();
		dados.put(municipio.getNome(), dadosMunAgregados.getDadosAgregados());
		dados.put("Média Nacional", transferenciaService.buscarPorAnoMesAgregadoPorArea(ano, mes, true));
		return dados;
	}
}