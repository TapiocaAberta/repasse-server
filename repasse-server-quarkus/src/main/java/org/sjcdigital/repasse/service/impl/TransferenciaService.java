package org.sjcdigital.repasse.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.sjcdigital.repasse.model.agregacao.TipoAgregacao;
import org.sjcdigital.repasse.model.base.Area;
import org.sjcdigital.repasse.model.base.Estado;
import org.sjcdigital.repasse.model.base.Municipio;
import org.sjcdigital.repasse.model.transferencia.Transferencia;
import org.sjcdigital.repasse.service.Service;

@RequestScoped
public class TransferenciaService extends Service<Transferencia> {
	
	@Inject
	DadosMunicipioService dadosMunicipioService;

	public Map<Object, Double> buscarPorAnoMunicipioAgregaPorMes(int ano, Municipio municipio, boolean perCapita){
		double divisor = 1;
		TypedQuery<Object[]> buscaTransferencia = em.createNamedQuery("Transferencia.porAnoMunicipioAgrupadoPorMes", Object[].class);
		buscaTransferencia.setParameter("ano", ano);
		buscaTransferencia.setParameter("municipio", municipio);
		if(perCapita) {
			divisor = dadosMunicipioService.buscaPorAnoMunicipioOuMaisRecente(ano, municipio).getPopulacao();
		}
		return montaMapaAgregacao(buscaTransferencia.getResultList(), divisor);
	}

	public Map<Object, Double> buscarPorAnoAgregado(TipoAgregacao tipoAgregacao, int ano, boolean perCapita){
		double divisor = 1;
		String namedQuery = "";
		switch(tipoAgregacao) {
			case ACAO:
				namedQuery = "Transferencia.porAnoAgrupadoPorAcao";
				break;
			case AREA:
				namedQuery = "Transferencia.porAnoAgrupadoPorArea";
				break;
			case FAVORECIDO:
				namedQuery = "Transferencia.porAnoAgrupadoPorFavorecido";								
				break;
			case PROGRAMA:
				namedQuery = "Transferencia.porAnoAgrupadoPorPrograma";								
				break;
			case SUB_FUNCAO:
				namedQuery = "Transferencia.porAnoAgrupadoPorSubFuncao";
				break;
			case ANO: 
				namedQuery = "Transferencia.porAnoAgrupadoPorMes";
				break;
			default:
				throw new IllegalArgumentException("Tipo de agregação não suportada aqui");
			
		}
		TypedQuery<Object[]> buscaTransferencia = em.createNamedQuery(namedQuery, Object[].class);
		buscaTransferencia.setParameter("ano", ano);
		if(perCapita) {
			divisor = dadosMunicipioService.somaPorAnoOuMaisRecente(ano);
		}
		return montaMapaAgregacao(buscaTransferencia.getResultList(), divisor);
	}	
	
	/**
	 * Limpa todos os dados de transferencias para esse ano e mes
	 * 
	 * @param ano
	 * @param mes
	 */
	@Transactional
	public void apagaTransferencias(int ano, int mes) {
		Query apagaTransferencia = em.createNamedQuery("Transferencia.removePorMesAno");
		apagaTransferencia.setParameter("ano", ano);
		apagaTransferencia.setParameter("mes", mes);
		apagaTransferencia.executeUpdate();
	}

	/**
	 * 
	 * Verifica se há transferências para aquele ano e mês
	 * @param ano
	 * @param mes
	 * @return
	 */
	public boolean temTranferencia(int ano, int mes) {		
		TypedQuery<Long> buscaTransferencia = em.createNamedQuery("Transferencia.quantidadePorMesEAno", Long.class);
		buscaTransferencia.setParameter("ano", ano);
		buscaTransferencia.setParameter("mes", mes);
		return buscaTransferencia.getSingleResult() > 0;
	}
	
	public boolean temTranferencia(int ano) {		
		TypedQuery<Long> buscaTransferencia = em.createNamedQuery("Transferencia.quantidadePorAno", Long.class);
		buscaTransferencia.setParameter("ano", ano);
		return buscaTransferencia.getSingleResult() > 0;
	}	

	public List<Transferencia> buscarPorAnoMesMunicipio(int ano, int mes, Municipio municipio) {
		return queryPorAnoMesMunicipio(ano, mes, municipio).getResultList();
	}
	
	public List<Transferencia> buscarPorAnoMunicipio(int ano, Municipio municipio) {
		TypedQuery<Transferencia> buscaTransferencia = em.createNamedQuery("Transferencia.porAnoMunicipio", Transferencia.class);
		buscaTransferencia.setParameter("ano", ano);
		buscaTransferencia.setParameter("municipio", municipio);
		return buscaTransferencia.getResultList();
	}

	public List<Transferencia> buscarPorAnoMesEstado(int ano, int mes, Estado estado) {
		TypedQuery<Transferencia> buscaTransferencia = em.createNamedQuery("Transferencia.porAnoMesEstado", Transferencia.class);
		buscaTransferencia.setParameter("ano", ano);
		buscaTransferencia.setParameter("mes", mes);
		buscaTransferencia.setParameter("estado", estado);
		return buscaTransferencia.getResultList();
	}

	public List<Transferencia> buscarPorAnoMesAreaMunicipio(int ano, int mes, Area area, Municipio municipio) {
		TypedQuery<Transferencia> buscaTransferencia = em.createNamedQuery("Transferencia.porAnoMesAreaMunicipio", Transferencia.class);
		buscaTransferencia.setParameter("ano", ano);
		buscaTransferencia.setParameter("mes", mes);
		buscaTransferencia.setParameter("area", area);
		buscaTransferencia.setParameter("municipio", municipio);
		return buscaTransferencia.getResultList();
	}

	public List<Integer> todosAnos() {
		return em.createNamedQuery("Transferencia.todosAnos", Integer.class).getResultList();
	}

	public List<Integer> mesesDisponiveis(int ano) {
		TypedQuery<Integer> buscaMeses = em.createNamedQuery("Transferencia.mesesPorAno", Integer.class);
		buscaMeses.setParameter("ano", ano);
		return buscaMeses.getResultList();
	}

	public List<Transferencia> buscarPorAnoMesMunicipioPaginado(int ano,
			int mes, Municipio municipio, int totalPorPagina, int pg) {
		TypedQuery<Transferencia> queryPorAnoMesMunicipio = queryPorAnoMesMunicipio(ano, mes, municipio);
		queryPorAnoMesMunicipio.setFirstResult(totalPorPagina * pg);
		queryPorAnoMesMunicipio.setMaxResults(totalPorPagina);
		return queryPorAnoMesMunicipio.getResultList();
	}	
	
	public long contaPorAnoMesMunicipio(int ano, int mes, Municipio municipio){
		TypedQuery<Long> buscaTransferencia = em.createNamedQuery("Transferencia.quantidadeAnoMesMunicipio", Long.class);
		buscaTransferencia.setParameter("ano", ano);
		buscaTransferencia.setParameter("mes", mes);		
		buscaTransferencia.setParameter("municipio", municipio);
		return buscaTransferencia.getSingleResult();
	}
	
	private TypedQuery<Transferencia> queryPorAnoMesMunicipio(int ano, int mes, Municipio municipio) {
		TypedQuery<Transferencia> buscaTransferencia = em.createNamedQuery("Transferencia.porAnoMesMunicipio", Transferencia.class);
		buscaTransferencia.setParameter("ano", ano);
		buscaTransferencia.setParameter("mes", mes);
		buscaTransferencia.setParameter("municipio", municipio);
		return buscaTransferencia;
	}

	public long contaPorAnoMesMunicipio(int ano, int mes) {
		TypedQuery<Long> buscaTransferencia = em.createNamedQuery("Transferencia.quantidadePorMesEAno", Long.class);
		buscaTransferencia.setParameter("ano", ano);
		buscaTransferencia.setParameter("mes", mes);		
		return buscaTransferencia.getSingleResult();
	}
	
	private Map<Object, Double> montaMapaAgregacao(List<Object[]> linhas, double divisor) {
		return linhas.stream().collect(Collectors.toMap(l -> l[0], l -> {
			double valor = (double) l[1];
			return valor / divisor;
		}));
	}

	public Map<Object, Double> buscarPorAnoMesAgregadoPorArea(int ano, int mes,
			boolean perCapita) {
		long divisor = 1;
		TypedQuery<Object[]> buscaTransferencia = em.createNamedQuery("Transferencia.porAnoMesAgrupadoPorArea", Object[].class);
		buscaTransferencia.setParameter("ano", ano);
		buscaTransferencia.setParameter("mes", mes);
		if(perCapita) {
			divisor = dadosMunicipioService.somaPorAnoOuMaisRecente(ano);
		}
		return montaMapaAgregacao(buscaTransferencia.getResultList(), divisor);
	}
}