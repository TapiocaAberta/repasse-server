package org.sjcdigital.repasse.carga;

import java.nio.file.Path;

public class RequisicaoCarga {

    private int ano;
    private int mes;
    private boolean override;
    private Path arquivoCSV;

    public RequisicaoCarga(int ano, int mes, Path arquivoCSV, boolean override) {
        this.ano = ano;
        this.mes = mes;
        this.arquivoCSV = arquivoCSV;
        this.override = override;
    }

    public int getAno() {
        return ano;
    }

    public int getMes() {
        return mes;
    }

    public Path getArquivoCSV() {
        return arquivoCSV;
    }

    public boolean isOverride() {
        return override;
    }

}