package com.summit.notebook.repository;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.summit.notebook.dao.ProfileJpaDao;
import com.summit.notebook.domain.Profile;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml")
@Transactional
@ActiveProfiles("test")
public class ProfileJpaDaoTest {

    @Autowired
    private ProfileJpaDao profileJpaDao;

    @Test
    public void testPersist() {
        Profile persistedProfile = persistProfile();
        System.out.println(persistedProfile);
        long countAuthors = profileJpaDao.countProfiles();
        assertEquals(1, countAuthors);

    }

    private Profile persistProfile() {
        Profile author = new Profile();
        author.setEmail("test@gmail.com");
        author.setPassword("password");
        author.setFullName("Shekhar Gulati");
        author.setUsername("shekhargulati");
        profileJpaDao.persist(author);
        return author;
    }

    @Test
    public void testFindAllAuthors() {
        persistProfile();
        List<Profile> authors = profileJpaDao.findAllProfiles();
        assertEquals(1, authors.size());
    }

    @Test
    public void testFindAuthor() {
        Profile persistAuthor = persistProfile();
        Profile findAuthor = profileJpaDao.findProfile(persistAuthor.getId());
        assertEquals("test@gmail.com", findAuthor.getEmail());
    }

    @Test
    public void testFindByEmailAndPassword() {
        persistProfile();
        Profile author = profileJpaDao.findAuthorByUsernameAndPassword("shekhargulati", "password");
        Assert.assertNotNull(author);
    }

}
