package org.jugvale.transparencia.transf.model.transferencia;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.jugvale.transparencia.transf.model.base.Municipio;

@XmlRootElement
@Entity
@Table(name = "transferencia")
@NamedQueries({
		@NamedQuery(name = "Transferencia.porAnoMesMunicipio", query = "SELECT t from Transferencia t WHERE t.ano = :ano AND t.mes = :mes AND t.municipio = :municipio"),
		@NamedQuery(name = "Transferencia.quantidadePorMesEAno", query = "SELECT COUNT(t) FROM Transferencia t WHERE t.ano = :ano AND t.mes = :mes"),
		@NamedQuery(name = "Transferencia.quantidadePorAno", query = "SELECT COUNT(t) FROM Transferencia t WHERE t.ano = :ano"),
		@NamedQuery(name = "Transferencia.porAnoMunicipio", query = "SELECT t from Transferencia t WHERE t.ano = :ano AND t.municipio = :municipio"),
		@NamedQuery(name = "Transferencia.porAnoMesEstado", query = "SELECT t from Transferencia t WHERE t.ano = :ano AND t.mes = :mes AND t.municipio.estado = :estado"),
		@NamedQuery(name = "Transferencia.porAnoMesAreaMunicipio", query = "SELECT t from Transferencia t WHERE t.ano = :ano AND t.mes = :mes AND t.municipio = :municipio AND t.subFuncao.area = :area") })
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
}