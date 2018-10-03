package org.jugvale.transfgov.carga.transferencia;

import java.util.Optional;

import org.jugvale.transfgov.model.transferencia.Transferencia;

public interface LinhaTransferenciaTransformer {
    
    public Optional<Transferencia> transformaLinha(int ano, int mes, String linha); 
    

}
