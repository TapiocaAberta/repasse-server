package org.sjcdigital.repasse.model.base;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@XmlRootElement
@Entity
@Table(name = "estado", uniqueConstraints = {@UniqueConstraint(columnNames = {"est_sigla", "est_nome"})})
@NamedQueries({@NamedQuery(name = "Estado.porSigla", query = "SELECT e from Estado e WHERE e.sigla = :sigla")})
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "cache-classes-basicas")
public class Estado {

    @Id
    @GeneratedValue
    @Column(name = "est_id")
    private long id;

    @Column(name = "est_nome")
    private String nome;

    @Column(name = "est_sigla")
    private String sigla;

    @Column(name = "est_regiao")
    private String regiao;

    public Estado() {
        super();
    }

    public Estado(String sigla) {
        super();
        this.sigla = sigla;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getRegiao() {
        return regiao;
    }

    public void setRegiao(String regiao) {
        this.regiao = regiao;
    }

    @Override
    public String toString() {
        return "Estado [id=" + id + ", nome=" + nome + ", sigla=" + sigla + ", regiao=" + regiao + "]";
    }

}