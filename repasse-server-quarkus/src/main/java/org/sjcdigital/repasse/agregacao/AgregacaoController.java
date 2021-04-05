package org.sjcdigital.repasse.agregacao;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.sjcdigital.repasse.model.agregacao.Agregacao;
import org.sjcdigital.repasse.model.agregacao.TipoAgregacao;
import org.sjcdigital.repasse.model.base.Estado;
import org.sjcdigital.repasse.model.base.Municipio;
import org.sjcdigital.repasse.model.transferencia.Transferencia;
import org.sjcdigital.repasse.service.impl.TransferenciaService;

/**
 * 
 * Cria as agregações de dados. Essa classe deverá ter seus resultados mantidos em cache usando infinispan
 * 
 * @author wsiqueir
 *
 */
@RequestScoped
public class AgregacaoController {
	
	@Inject
	TransferenciaService transferenciaService;
	
	public Agregacao agregaPorTipo(int ano, int mes, Estado estado, Municipio municipio, TipoAgregacao tipoAgregacao, List<Transferencia> transferencias) {
		Function<Transferencia, ?> funcao = criaFuncao(tipoAgregacao);
		Map<Object, Double> dadosSomados =  agregaESoma(transferencias,  funcao);
		return new Agregacao(ano, mes, estado, municipio, tipoAgregacao, dadosSomados);		
	}
	
	public Agregacao agregaPercapitaPorTipo(int ano, int mes, Estado estado, Municipio municipio, TipoAgregacao tipoAgregacao, List<Transferencia> transferencias, long populacao) {
		Function<Transferencia, ?> funcao = criaFuncao(tipoAgregacao);
		Map<Object, Double> dadosSomados =  agregaESomaPerCapita(transferencias,  funcao, populacao);
		return new Agregacao(ano, mes, estado, municipio, tipoAgregacao, dadosSomados);		
	}
	
	/**
	 * 
	 * Cria a função usada para fazer agregação
	 * 
	 * @param tipoAgregacao
	 *  O tipo da agregação que se deseja fazer 
	 * @return
	 */
	private Function<Transferencia, ?>  criaFuncao(TipoAgregacao tipoAgregacao) {
		// TODO: Melhorar usando polimorfismo
		switch (tipoAgregacao) {	
		case ACAO:
			return Transferencia::getAcao;
		case PROGRAMA:
			return Transferencia::getPrograma;
		case SUB_FUNCAO:
			return Transferencia::getSubFuncao;
		case MES:
			return Transferencia::getMes;
		case AREA:
			return Transferencia::getArea;
		case FAVORECIDO:
			return Transferencia::getFavorecido;
		case MUNICIPIO:
			return Transferencia::getMunicipio;
		default:
			throw new IllegalArgumentException("Tipo de agregação não conhecida: " + tipoAgregacao);
		}		
		
	}
	
	private Map<Object, Double> agregaESoma(List<Transferencia> transferencias, Function<? super Transferencia, ?> agregador){
		return transferencias.stream().collect(Collectors.groupingBy(agregador, Collectors.summingDouble(Transferencia::getValor)));
	} 
	
	private Map<Object, Double> agregaESomaPerCapita(List<Transferencia> transferencias, Function<? super Transferencia, ?> agregador, long populacao){
		return transferencias.stream().collect(Collectors.groupingBy(agregador, Collectors.summingDouble(t -> {
			return ((Transferencia) t).getValor() / populacao;
		})));
	} 

}