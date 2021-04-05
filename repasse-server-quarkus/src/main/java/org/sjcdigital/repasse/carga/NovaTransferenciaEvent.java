package org.sjcdigital.repasse.carga;

import org.sjcdigital.repasse.model.transferencia.Transferencia;

public class NovaTransferenciaEvent {

    Transferencia novaTransferencia;

    public NovaTransferenciaEvent(Transferencia novaTransferencia) {
        this.novaTransferencia = novaTransferencia;
    }

    public Transferencia getNovaTransferencia() {
        return novaTransferencia;
    }

}