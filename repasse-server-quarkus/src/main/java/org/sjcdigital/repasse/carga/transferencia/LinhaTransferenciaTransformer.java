package org.sjcdigital.repasse.carga.transferencia;

import java.util.Optional;

import org.sjcdigital.repasse.model.transferencia.Transferencia;

public interface LinhaTransferenciaTransformer {
    
    public Optional<Transferencia> transformaLinha(int ano, int mes, String linha); 
    

}