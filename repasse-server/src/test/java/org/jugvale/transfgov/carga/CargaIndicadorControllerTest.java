package org.jugvale.transfgov.carga;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jugvale.transfgov.model.base.Area;
import org.jugvale.transfgov.model.base.Estado;
import org.jugvale.transfgov.model.base.Municipio;
import org.jugvale.transfgov.model.carga.DadosCargaIndicador;
import org.jugvale.transfgov.model.carga.LinhaCargaIndicador;
import org.jugvale.transfgov.model.indicador.FocoIndicador;
import org.jugvale.transfgov.model.indicador.GrupoIndicador;
import org.jugvale.transfgov.model.indicador.Indicador;
import org.jugvale.transfgov.model.indicador.ValorIndicador;
import org.jugvale.transfgov.service.impl.AreaService;
import org.jugvale.transfgov.service.impl.EstadoService;
import org.jugvale.transfgov.service.impl.FocoIndicadorService;
import org.jugvale.transfgov.service.impl.GrupoIndicadorService;
import org.jugvale.transfgov.service.impl.IndicadorService;
import org.jugvale.transfgov.service.impl.MunicipioService;
import org.jugvale.transfgov.service.impl.ValorIndicadorService;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class CargaIndicadorControllerTest {

	@Deployment
	public static Archive<?> createDeployment() {
		return ShrinkWrap.create(WebArchive.class)
				.addPackage("org.jugvale.transfgov.agregacao")
				.addPackage("org.jugvale.transfgov.carga")
				.addPackage("org.jugvale.transfgov.model.agregacao")
				.addPackage("org.jugvale.transfgov.model.base")
				.addPackage("org.jugvale.transfgov.model.carga")
				.addPackage("org.jugvale.transfgov.model.indicador")
				.addPackage("org.jugvale.transfgov.model.ranking")
				.addPackage("org.jugvale.transfgov.model.transferencia")
				.addPackage("org.jugvale.transfgov.producer")
				.addPackage("org.jugvale.transfgov.ranking")
				.addPackage("org.jugvale.transfgov.resource")
				.addPackage("org.jugvale.transfgov.resource.config.jsonview")
				.addPackage("org.jugvale.transfgov.resource.impl")
				.addPackage("org.jugvale.transfgov.resource.providers")
				.addPackage("org.jugvale.transfgov.service")
				.addPackage("org.jugvale.transfgov.service.impl")
				.addPackage("org.jugvale.transfgov.utils")
				.addAsResource("META-INF/persistence.xml")
				.addAsWebInfResource("test-ds.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Inject
	MunicipioService municipioService;
	@Inject
	AreaService areaService;
	@Inject
	EstadoService estadoService;
	@Inject
	CargaIndicadorController cargaIndicadorController;
	
	@Inject
	FocoIndicadorService focoIndicadorService;
	
	@Inject 
	GrupoIndicadorService grupoIndicadorService;
	
	@Inject
	IndicadorService indicadorService;
	

	@Inject
	ValorIndicadorService valorIndicadorService;
	
	@Test
	public void testeCargaIndicadores() {
		int ANO1 = 2011;
		int ANO2 = 2012;
		String ESTADO = "XX";
		String MUNICIPIO1 = "MUN 1";
		String MUNICIPIO2 = "MUN 2";
		String MUNICIPIO3 = "MUN 3";
		// simulando um valor da vida real que deverá ser ajustado pela app
		String MUNICIPIO2_REAL = "mún 2";
		
		String AREA = "Educação";
		String FOCO_INDICADOR = "Ensino Básico";
		String GRUPO_INDICADOR = "Prova Nacional";
		String INDICADOR = "Média geral";
		
		Estado e = new Estado(ESTADO);
		estadoService.salvar(e);
		Municipio m1 = new Municipio("", MUNICIPIO1, e);
		Municipio m2 = new Municipio("", MUNICIPIO2, e);
		Municipio m3 = new Municipio("", MUNICIPIO3, e);
		municipioService.salvar(m1);
		municipioService.salvar(m2);
		municipioService.salvar(m3);
		Area area = new Area(1, AREA);
		areaService.salvar(area);

		DadosCargaIndicador dados = new DadosCargaIndicador();
		
		dados.setArea(AREA);
		dados.setFocoIndicador(FOCO_INDICADOR);
		dados.setGrupoIndicador(GRUPO_INDICADOR);
		dados.setIndicador(INDICADOR);
		
		List<LinhaCargaIndicador> dadosCarga = new ArrayList<>();
		
		dadosCarga.add(new LinhaCargaIndicador(MUNICIPIO1, ESTADO, 0.1f, ANO1));
		dadosCarga.add(new LinhaCargaIndicador(MUNICIPIO2_REAL, ESTADO, 0.2f, ANO1));
		dadosCarga.add(new LinhaCargaIndicador(MUNICIPIO3, ESTADO, 0.3f, ANO1));
		// dados deverão ser sobreescritos
		dadosCarga.add( new LinhaCargaIndicador(MUNICIPIO1, ESTADO, 0.5f, ANO1));
		// outro ano
		dadosCarga.add( new LinhaCargaIndicador(MUNICIPIO1, ESTADO, 0.1f, ANO2));
		dadosCarga.add( new LinhaCargaIndicador(MUNICIPIO3, ESTADO, 0.4f, ANO2));
		
		dados.setArea(AREA);
		dados.setIndicador(INDICADOR);
		dados.setFocoIndicador(FOCO_INDICADOR);
		dados.setGrupoIndicador(GRUPO_INDICADOR);
		dados.setLinhas(dadosCarga);
		
		List<String> resultados = cargaIndicadorController.carregaIndicadores(dados);
		
		assertEquals(6, resultados.size());
		resultados.forEach(System.out::println);
		
		FocoIndicador foco = focoIndicadorService.buscaPorNome(FOCO_INDICADOR);
		GrupoIndicador grupoIndicador = grupoIndicadorService.buscaPorNome(GRUPO_INDICADOR);
		Indicador indicador = indicadorService.buscaPorNome(INDICADOR);
		assertNotNull(foco);
		assertNotNull(grupoIndicador);
		assertNotNull(indicador);
		
		List<Indicador> todosIndicadores = indicadorService.todos();
		List<Indicador> indicadoresPorArea = indicadorService.buscaPorArea(area);
		assertEquals(1, todosIndicadores.size());	
		assertEquals(1, indicadoresPorArea.size());
		
		List<ValorIndicador> valores2011 = valorIndicadorService.buscaPorIndicadorEAno(indicador, ANO1);
		List<ValorIndicador> valores2012 = valorIndicadorService.buscaPorIndicadorEAno(indicador, ANO2);
		assertEquals(3, valores2011.size());
		assertEquals(2, valores2012.size());
		
		List<ValorIndicador> valoresMUN1 = valorIndicadorService.buscaPorIndicadorEMunicipio(indicador, m1);
		List<ValorIndicador> valoresMUN2 = valorIndicadorService.buscaPorIndicadorEMunicipio(indicador, m2);
		List<ValorIndicador> valoresMUNANO = valorIndicadorService.buscaPorAnoMunicipio(ANO1, m1);
		List<ValorIndicador> valoresMUNAREAANO = valorIndicadorService.buscaPorMuninipioAreaAno(m1, area, ANO1);
		List<ValorIndicador> valoresMUN = valorIndicadorService.buscaPorMunicipio(m1);
		List<ValorIndicador> valoresMUNAREA = valorIndicadorService.buscaPorMuninipioArea(m1, area);
		assertEquals(2, valoresMUN1.size());
		assertEquals(1, valoresMUN2.size());
		assertEquals(1, valoresMUNANO.size());
		assertEquals(1, valoresMUNAREAANO.size());
		assertEquals(2, valoresMUN.size());
		assertEquals(2, valoresMUNAREA.size());
		
	}

}
