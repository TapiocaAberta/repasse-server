package org.jugvale.transfgov.service;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.jugvale.transfgov.model.base.Estado;

/**
 * 
 * Uma classe Repositorio abstrata para uso com as entidades do nosso sistema
 * 
 * @author Pedro Hos
 * @author william
 * 
 */
@Stateless
public abstract class Service<T> {

    protected Class<T> tipo = retornaTipo();
    @PersistenceContext(unitName = "primary")
    protected EntityManager em;

    public void salvar(T entidade) {
        em.persist(entidade);
    }

    public void remover(T entidade) {
        em.remove(entidade);
    }

    @SuppressWarnings("unchecked")
    public List<T> todos() {
        CriteriaQuery<Object> cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(tipo));
        return (List<T>) em.createQuery(cq).getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<T> todosPaginado(int total, int pg) {
        CriteriaQuery<Object> cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(tipo));
        Query busca = em.createQuery(cq);
        busca.setFirstResult(pg * total);
        busca.setMaxResults(total);
        return (List<T>) busca.getResultList();
    }

    public long contaTodos() {
        CriteriaBuilder qb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = qb.createQuery(Long.class);
        cq.select(qb.count(cq.from(tipo)));
        return em.createQuery(cq).getSingleResult();
    }

    public T buscarPorId(long id) {
        return em.find(tipo, id);
    }

    public T atualizar(T entidade) {
        return em.merge(entidade);
    }

    public T buscaPorIdOuCria(long id, Supplier<T> entity) {
        T obj = buscarPorId(id);
        if (Objects.isNull(obj)) {
            obj = entity.get();
            this.salvar(obj);
        }
        return obj;
    }

    public T buscaPorIdOuCria(long id, T entity) {
        return buscaPorIdOuCria(id, () -> entity);
    }

    public T getFistResult(TypedQuery<T> query) {
        List<T> result = query.getResultList();
        if (result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
    }

    /**
     * @author Pedro Hos<br>
     * 
     *         Utilizando Exemplo de Eduardo Guerra!
     *         https://groups.google.com/forum
     *         /#!topic/projeto-oo-guiado-por-padroes/pOIiOD9cifs
     * 
     *         Este método retorna o tipo da Classe, dessa maneira não é
     *         necessário cada Service expor seu tipo!!!!
     * 
     * @return Class<T>
     */
    @SuppressWarnings({"unchecked"})
    private Class<T> retornaTipo() {
        Class<?> clazz = this.getClass();
        while (!clazz.getSuperclass().equals(Service.class)) {
            clazz = clazz.getSuperclass();
        }
        ParameterizedType tipoGenerico = (ParameterizedType) clazz
                                                                  .getGenericSuperclass();
        return (Class<T>) tipoGenerico.getActualTypeArguments()[0];
    }
}