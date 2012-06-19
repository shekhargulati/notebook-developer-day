package com.summit.notebook.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.summit.notebook.domain.Author;

@Repository
@Transactional
public class AuthorRepository implements IAuthorRepository {

	@PersistenceContext
	private EntityManager entityManager;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.summit.notebook.jpa.IAuthorRepository#countAuthors()
	 */
	@Override
	public long countAuthors() {
		return entityManager.createQuery("SELECT COUNT(o) FROM Author o",
				Long.class).getSingleResult();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.summit.notebook.jpa.IAuthorRepository#findAllAuthors()
	 */
	@Override
	public List<Author> findAllAuthors() {
		return entityManager
				.createQuery("SELECT o FROM Author o", Author.class)
				.getResultList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.summit.notebook.jpa.IAuthorRepository#findAuthor(java.lang.Long)
	 */
	@Override
	public Author findAuthor(Long id) {
		if (id == null)
			return null;
		return entityManager.find(Author.class, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.summit.notebook.jpa.IAuthorRepository#findAuthorEntries(int,
	 * int)
	 */
	@Override
	public List<Author> findAuthorEntries(int firstResult, int maxResults) {
		return entityManager
				.createQuery("SELECT o FROM Author o", Author.class)
				.setFirstResult(firstResult).setMaxResults(maxResults)
				.getResultList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.summit.notebook.jpa.IAuthorRepository#findAuthorByEmailAndPassword
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public Author findAuthorByUsernameAndPassword(String username, String password) {
		TypedQuery<Author> query = entityManager
				.createQuery(
						"SELECT o FROM Author o where o.username = :username and o.password = :password",
						Author.class);
		query.setParameter("username", username);
		query.setParameter("password", password);
		return query.getSingleResult();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.summit.notebook.jpa.IAuthorRepository#persist(com.summit.notebook
	 * .domain.Author)
	 */
	@Override
	public void persist(Author author) {
		entityManager.persist(author);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.summit.notebook.jpa.IAuthorRepository#remove(com.summit.notebook.
	 * domain.Author)
	 */
	@Override
	public void remove(Author author) {
		if (this.entityManager.contains(this)) {
			this.entityManager.remove(this);
		} else {
			Author attached = findAuthor(author.getId());
			this.entityManager.remove(attached);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.summit.notebook.jpa.IAuthorRepository#flush()
	 */
	@Override
	public void flush() {
		entityManager.flush();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.summit.notebook.jpa.IAuthorRepository#clear()
	 */
	@Override
	public void clear() {
		entityManager.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.summit.notebook.jpa.IAuthorRepository#merge(com.summit.notebook.domain
	 * .Author)
	 */
	@Override
	public Author merge(Author author) {
		Author merged = entityManager.merge(author);
		entityManager.flush();
		return merged;
	}

	@Override
	public Author findAuthorByUsername(String username) {
		TypedQuery<Author> query = entityManager
				.createQuery(
						"SELECT o FROM Author o where o.username = :username",
						Author.class);
		query.setParameter("username", username);
		return query.getSingleResult();
	}
}
