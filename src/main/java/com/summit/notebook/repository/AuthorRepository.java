package com.summit.notebook.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.summit.notebook.domain.Author;

@Repository
public class AuthorRepository {

	@PersistenceContext
	private EntityManager entityManager;
	
	public long countAuthors() {
        return entityManager.createQuery("SELECT COUNT(o) FROM Author o", Long.class).getSingleResult();
    }
    
    public List<Author> findAllAuthors() {
        return entityManager.createQuery("SELECT o FROM Author o", Author.class).getResultList();
    }
    
    public Author findAuthor(Long id) {
        if (id == null) return null;
        return entityManager.find(Author.class, id);
    }
    
    public List<Author> findAuthorEntries(int firstResult, int maxResults) {
        return entityManager.createQuery("SELECT o FROM Author o", Author.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public Author findAuthorByEmailAndPassword(String email, String password){
    	TypedQuery<Author> query = entityManager.createQuery("SELECT o FROM Author o where o.email = :email and o.password = :password", Author.class);
    	query.setParameter("email", email);
    	query.setParameter("password", password);
		return query.getSingleResult();
    }
    @Transactional
    public void persist(Author author) {
        entityManager.persist(author);
    }
    
    @Transactional
    public void remove(Author author) {
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Author attached = findAuthor(author.getId());
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void flush() {
        this.entityManager.flush();
    }
    
    @Transactional
    public void clear() {
        this.entityManager.clear();
    }
    
    @Transactional
    public Author merge(Author author) {
        Author merged = entityManager.merge(author);
        this.entityManager.flush();
        return merged;
    }
}
