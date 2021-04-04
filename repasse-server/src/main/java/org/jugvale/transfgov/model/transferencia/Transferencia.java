package org.jugvale.transfgov.model.transferencia;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.jugvale.transfgov.model.base.Area;
import org.jugvale.transfgov.model.base.Municipio;

/**
 * 
 * Coluna que representa os dados de transferência vindos do portal da
 * transparências para anos depois de 2011, pois antes os dados tinham outro
 * formato
 * 
 * @author wsiqueir
 *
 */
@XmlRootElement
@Entity
@Table(name = "transferencia", indexes = {@Index(columnList = "tra_ano"),
                                          @Index(columnList = "tra_mes")})
@NamedQueries({
               @NamedQuery(name = "Transferencia.porAnoMesMunicipio", query = "SELECT t from Transferencia t WHERE t.ano = :ano AND t.mes = :mes AND t.municipio = :municipio"),
               @NamedQuery(name = "Transferencia.quantidadePorMesEAno", query = "SELECT COUNT(t) FROM Transferencia t WHERE t.ano = :ano AND t.mes = :mes", hints = {@QueryHint(name = "org.hibernate.cacheable",
                                                                                                                                                                                value = "true")}),
               @NamedQuery(name = "Transferencia.quantidadeAnoMesMunicipio", query = "SELECT COUNT(t) FROM Transferencia t WHERE t.ano = :ano AND t.mes = :mes AND t.municipio = :municipio", hints = {@QueryHint(
                                                                                                                                                                                                                  name = "org.hibernate.cacheable",
                                                                                                                                                                                                                  value = "true")}),
               @NamedQuery(name = "Transferencia.quantidadePorAno", query = "SELECT COUNT(t) FROM Transferencia t WHERE t.ano = :ano", hints = {@QueryHint(name = "org.hibernate.cacheable", value = "true")}),
               @NamedQuery(name = "Transferencia.porAnoMunicipio", query = "SELECT t from Transferencia t WHERE t.ano = :ano AND t.municipio = :municipio", hints = {@QueryHint(name = "org.hibernate.cacheable",
                                                                                                                                                                                value = "true")}),
               @NamedQuery(name = "Transferencia.porAnoMesEstado", query = "SELECT t from Transferencia t WHERE t.ano = :ano AND t.mes = :mes AND t.municipio.estado = :estado"),
               @NamedQuery(name = "Transferencia.porAnoMesAreaMunicipio", query = "SELECT t from Transferencia t WHERE t.ano = :ano AND t.mes = :mes AND t.municipio = :municipio AND t.area = :area"),
               @NamedQuery(name = "Transferencia.todosAnos", query = "SELECT DISTINCT t.ano from Transferencia t", hints = {@QueryHint(name = "org.hibernate.cacheable", value = "true")}),
               @NamedQuery(name = "Transferencia.mesesPorAno", query = "SELECT DISTINCT t.mes from Transferencia t WHERE t.ano = :ano ORDER BY t.mes", hints = {@QueryHint(name = "org.hibernate.cacheable",
                                                                                                                                                                           value = "true")}),
               @NamedQuery(name = "Transferencia.removePorMesAno", query = "DELETE FROM Transferencia t WHERE t.ano = :ano AND t.mes = :mes"),
               @NamedQuery(name = "Transferencia.porAnoMunicipioAgrupadoPorMes", query = "SELECT t.mes, sum(t.valor) from Transferencia t WHERE t.ano = :ano AND t.municipio = :municipio GROUP BY t.mes", hints = {
                                                                                                                                                                                                                    @QueryHint(name = "org.hibernate.cacheable",
                                                                                                                                                                                                                               value = "true")}),
               @NamedQuery(name = "Transferencia.porAnoAgrupadoPorMes",
                           query = "SELECT t.mes, sum(t.valor) from Transferencia t WHERE t.ano = :ano AND t.municipio.nome <> 'Governo do Estado' AND t.municipio.estado.sigla NOT IN ('OM', 'EX') GROUP BY t.mes",
                           hints = {@QueryHint(name = "org.hibernate.cacheable", value = "true")}),
               @NamedQuery(name = "Transferencia.porAnoAgrupadoPorArea",
                           query = "SELECT t.area.nome, sum(t.valor) from Transferencia t WHERE t.ano = :ano AND t.municipio.nome <> 'Governo do Estado' AND t.municipio.estado.sigla NOT IN ('OM', 'EX') GROUP BY t.area",
                           hints = {@QueryHint(name = "org.hibernate.cacheable", value = "true")}),
               @NamedQuery(name = "Transferencia.porAnoMesAgrupadoPorArea",
                           query = "SELECT t.area.nome, sum(t.valor) from Transferencia t WHERE t.ano = :ano AND t.mes = :mes AND t.municipio.nome <> 'Governo do Estado' AND t.municipio.estado.sigla NOT IN ('OM', 'EX') GROUP BY t.area",
                           hints = {@QueryHint(name = "org.hibernate.cacheable", value = "true")}),
               @NamedQuery(name = "Transferencia.porAnoAgrupadoPorAcao",
                           query = "SELECT concat(t.acao.nome, ' - ', t.acao.codigo), sum(t.valor) from Transferencia t WHERE t.ano = :ano AND t.municipio.nome <> 'Governo do Estado' AND t.municipio.estado.sigla NOT IN ('OM', 'EX') GROUP BY t.acao",
                           hints = {@QueryHint(name = "org.hibernate.cacheable", value = "true")}),
               @NamedQuery(name = "Transferencia.porAnoAgrupadoPorPrograma",
                           query = "SELECT t.programa.nome, sum(t.valor) from Transferencia t WHERE t.ano = :ano AND t.municipio.nome <> 'Governo do Estado' AND t.municipio.estado.sigla NOT IN ('OM', 'EX') GROUP BY t.programa",
                           hints = {@QueryHint(name = "org.hibernate.cacheable", value = "true")}),
               @NamedQuery(name = "Transferencia.porAnoAgrupadoPorSubFuncao",
                           query = "SELECT t.subFuncao, sum(t.valor) from Transferencia t WHERE t.ano = :ano AND t.municipio.nome <> 'Governo do Estado' AND t.municipio.estado.sigla NOT IN ('OM', 'EX') GROUP BY t.subFuncao",
                           hints = {@QueryHint(name = "org.hibernate.cacheable", value = "true")}),
               @NamedQuery(name = "Transferencia.porAnoAgrupadoPorFavorecido",
                           query = "SELECT t.favorecido, sum(t.valor) from Transferencia t WHERE t.ano = :ano AND t.municipio.nome <> 'Governo do Estado' AND t.municipio.estado.sigla NOT IN ('OM', 'EX') GROUP BY t.favorecido",
                           hints = {@QueryHint(name = "org.hibernate.cacheable", value = "true")}),
// @NamedQuery(name = "Ranking.porAno", query =
// "select concat(mun_nome, ' - ', est_sigla) as MUNICIPIO, truncate(sum(t.tra_valor) / d.populacao, 2), sum(t.tra_valor), d.populacao from transferencia t inner join municipio on t.municipio_mun_id = mun_id  inner join dados_municipio d on mun_id = d.municipio_mun_id inner join estado on estado_est_id = est_id where t.tra_ano = :ano AND  d.ano = :ano group by MUNICIPIO order by truncate(sum(t.tra_valor) / populacao, 2) desc",
// hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
})
// essa query tem que ser nativa... mais performática que a gerada pelo JPA
@NamedNativeQueries({@NamedNativeQuery(name = "Transferencia.Ranking.porAno",
                                       query = "select concat(mun_nome, ' - ', est_sigla) as MUNICIPIO, truncate(sum(t.tra_valor) / d.populacao, 2), sum(t.tra_valor), d.populacao, municipio.mun_id from transferencia t inner join municipio on t.municipio_mun_id = mun_id  inner join dados_municipio d on mun_id = d.municipio_mun_id inner join estado on estado_est_id = est_id where t.tra_ano = :ano AND  d.ano = :ano group by MUNICIPIO order by truncate(sum(t.tra_valor) / populacao, 2) desc")})
public class Transferencia {

    @Id
    @GeneratedValue
    @Column(name = "tra_id")
    private long id;

    @Column(name = "tra_ano")
    private int ano;

    @Column(name = "tra_mes")
    private int mes;

    @Column(name = "tra_valor")
    private float valor;

    @ManyToOne
    @JoinColumn(name = "municipio_mun_id")
    private Municipio municipio;

    @ManyToOne
    @JoinColumn(name = "area_area_id")
    private Area area;

    @ManyToOne
    @JoinColumn(name = "programa_pro_id")
    private Programa programa;

    @ManyToOne
    @JoinColumn(name = "acao_acao_id")
    private Acao acao;

    @ManyToOne
    @JoinColumn(name = "fonte_finalidade_ffi_id")
    private FonteFinalidade fonteFinalidade;

    @ManyToOne
    @JoinColumn(name = "sub_funcao_sfu_id")
    private SubFuncao subFuncao;

    @ManyToOne
    @JoinColumn(name = "favorecido_fav_id")
    private Favorecido favorecido;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public Programa getPrograma() {
        return programa;
    }

    public void setPrograma(Programa programa) {
        this.programa = programa;
    }

    public Acao getAcao() {
        return acao;
    }

    public void setAcao(Acao acao) {
        this.acao = acao;
    }

    public FonteFinalidade getFonteFinalidade() {
        return fonteFinalidade;
    }

    public void setFonteFinalidade(FonteFinalidade fonteFinalidade) {
        this.fonteFinalidade = fonteFinalidade;
    }

    public SubFuncao getSubFuncao() {
        return subFuncao;
    }

    public void setSubFuncao(SubFuncao subFuncao) {
        this.subFuncao = subFuncao;
    }

    public Favorecido getFavorecido() {
        return favorecido;
    }

    public void setFavorecido(Favorecido favorecido) {
        this.favorecido = favorecido;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    @Override
    public String toString() {
        return "Transferencia [id=" + id + ", ano=" + ano + ", mes=" + mes + ", valor=" + valor + ", municipio=" + municipio + ", area=" + area + ", programa=" + programa + ", acao=" + acao + ", fonteFinalidade=" +
               fonteFinalidade + ", subFuncao=" + subFuncao + ", favorecido=" + favorecido + "]";
    }

}