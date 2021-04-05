package org.sjcdigital.repasse.carga;

import java.nio.file.Path;

public class RequisicaoCarga {

    private int ano;
    private int mes;
    private Path arquivoCSV;

    public RequisicaoCarga(int ano, int mes, Path arquivoCSV) {
        this.ano = ano;
        this.mes = mes;
        this.arquivoCSV = arquivoCSV;
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

}