package org.sjcdigital.repasse.carga;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.sjcdigital.repasse.service.impl.AcaoService;
import org.sjcdigital.repasse.service.impl.AreaService;
import org.sjcdigital.repasse.service.impl.EstadoService;
import org.sjcdigital.repasse.service.impl.FavorecidoService;
import org.sjcdigital.repasse.service.impl.FonteFinalidadeService;
import org.sjcdigital.repasse.service.impl.MunicipioService;
import org.sjcdigital.repasse.service.impl.ProgramaService;
import org.sjcdigital.repasse.service.impl.SubFuncaoService;
import org.sjcdigital.repasse.service.impl.TransferenciaService;

import static javax.transaction.Transactional.TxType.REQUIRES_NEW;

/**
 * Realiza a carga de novas transferências
 *
 */
@Dependent
public class NovaTransferenciaListener {

    @Inject
    MunicipioService municipioService;

    @Inject
    AcaoService acaoService;

    @Inject
    EstadoService estadoService;

    @Inject
    AreaService areaService;

    @Inject
    SubFuncaoService subFuncaoService;

    @Inject
    ProgramaService programaService;

    @Inject
    FonteFinalidadeService fonteFinalidadeService;

    @Inject
    FavorecidoService favorecidoService;

    @Inject
    TransferenciaService transferenciaService;

    @Transactional(REQUIRES_NEW)
    public void salva(@Observes NovaTransferenciaEvent novaTransferencia) {
        var transferencia = novaTransferencia.getNovaTransferencia();
        var estado = transferencia.getMunicipio().getEstado();
        var municipio = transferencia.getMunicipio();
        var area = transferencia.getArea();
        var subFuncao = transferencia.getSubFuncao();
        var programa = transferencia.getPrograma();
        var acao = transferencia.getAcao();
        var favorecido = transferencia.getFavorecido();

        municipio.setEstado(estadoService.buscaEstadoPorSiglaOuCria(estado));

        transferencia.setMunicipio(municipioService.porEstadoNomeESIAFIOuCria(municipio));
        transferencia.setArea(areaService.buscaPorIdOuCria(area.getId(), area));
        transferencia.setSubFuncao(subFuncaoService.buscaPorIdOuCria(subFuncao.getId(), subFuncao));
        transferencia.setPrograma(programaService.buscaPorIdOuCria(programa.getId(), programa));
        transferencia.setAcao(acaoService.buscaPorCodigoOuCria(acao));
        transferencia.setFavorecido(favorecidoService.buscaPorCodigoOuCria(favorecido));

        transferenciaService.salvar(transferencia);

    }

}
