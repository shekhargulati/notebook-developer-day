package com.summit.notebook.dao;

import java.util.List;

import com.summit.notebook.domain.Profile;

public interface ProfileJpaDao {

    public abstract long countProfiles();

    public abstract List<Profile> findAllProfiles();

    public abstract Profile findProfile(Long id);

    public abstract List<Profile> findProfileEntries(int firstResult,
            int maxResults);

    public abstract Profile findAuthorByUsernameAndPassword(String email,
            String password);

    public abstract void persist(Profile author);

    public abstract void remove(Profile author);

    public abstract void flush();

    public abstract void clear();

    public abstract Profile merge(Profile author);

    public abstract Profile findProfileByUsername(String username);

}