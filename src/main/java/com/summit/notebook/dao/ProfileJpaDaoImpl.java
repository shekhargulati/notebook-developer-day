package com.summit.notebook.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.summit.notebook.domain.Profile;
import com.summit.notebook.social.signup.UsernameAlreadyInUseException;

@Repository
@Transactional
public class ProfileJpaDaoImpl implements ProfileJpaDao {

    @PersistenceContext
    private EntityManager entityManager;

    /*
     * (non-Javadoc)
     * 
     * @see com.summit.notebook.jpa.ProfileJpaDao#countProfiles()
     */
    @Override
    public long countProfiles() {
        return entityManager.createQuery("SELECT COUNT(o) FROM Profile o",
                Long.class).getSingleResult();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.summit.notebook.jpa.ProfileJpaDao#findAllProfiles()
     */
    @Override
    public List<Profile> findAllProfiles() {
        return entityManager
                .createQuery("SELECT o FROM Profile o", Profile.class)
                .getResultList();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.summit.notebook.jpa.ProfileJpaDao#findAuthor(java.lang.Long)
     */
    @Override
    public Profile findProfile(Long id) {
        if (id == null)
            return null;
        return entityManager.find(Profile.class, id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.summit.notebook.jpa.ProfileJpaDao#findAuthorEntries(int, int)
     */
    @Override
    public List<Profile> findProfileEntries(int firstResult, int maxResults) {
        return entityManager
                .createQuery("SELECT o FROM Profile o", Profile.class)
                .setFirstResult(firstResult).setMaxResults(maxResults)
                .getResultList();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.summit.notebook.jpa.ProfileJpaDao#findAuthorByEmailAndPassword
     * (java.lang.String, java.lang.String)
     */
    @Override
    public Profile findAuthorByUsernameAndPassword(String username, String password) {
        TypedQuery<Profile> query = entityManager
                .createQuery(
                        "SELECT o FROM Profile o where o.username = :username and o.password = :password",
                        Profile.class);
        query.setParameter("username", username);
        query.setParameter("password", password);
        return query.getSingleResult();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.summit.notebook.jpa.ProfileJpaDao#persist(com.summit.notebook
     * .domain.Author)
     */
    @Override
    public void persist(Profile profile) {
        if (findProfileByUsername(profile.getUsername()) != null) {
            throw new UsernameAlreadyInUseException(profile.getUsername());
        }
        entityManager.persist(profile);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.summit.notebook.jpa.ProfileJpaDao#remove(com.summit.notebook.
     * domain.Author)
     */
    @Override
    public void remove(Profile profile) {
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Profile attached = findProfile(profile.getId());
            this.entityManager.remove(attached);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.summit.notebook.jpa.ProfileJpaDao#flush()
     */
    @Override
    public void flush() {
        entityManager.flush();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.summit.notebook.jpa.ProfileJpaDao#clear()
     */
    @Override
    public void clear() {
        entityManager.clear();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.summit.notebook.jpa.ProfileJpaDao#merge(com.summit.notebook.domain
     * .Author)
     */
    @Override
    public Profile merge(Profile profile) {
        Profile merged = entityManager.merge(profile);
        entityManager.flush();
        return merged;
    }

    @Override
    public Profile findProfileByUsername(String username) {
        TypedQuery<Profile> query = entityManager
                .createQuery(
                        "SELECT o FROM Profile o where o.username = :username",
                        Profile.class);
        query.setParameter("username", username);
        try {
            Profile profile = query.getSingleResult();
            return profile;
        } catch (NoResultException e) {
            return null;
        }
    }
}
