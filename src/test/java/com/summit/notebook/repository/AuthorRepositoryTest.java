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

import com.summit.notebook.domain.Author;
import com.summit.notebook.jpa.IAuthorRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml")
@Transactional
@ActiveProfiles("test")
public class AuthorRepositoryTest {

	@Autowired
	private IAuthorRepository authorRepository;
	
	@Test
	public void testPersist() {
		Author persistAuthor = persistAuthor();
		System.out.println(persistAuthor);
		long countAuthors = authorRepository.countAuthors();
		assertEquals(1, countAuthors);
		 
	}
	private Author persistAuthor() {
		Author author = new Author();
		author.setEmail("test@gmail.com");
		author.setPassword("password");
		author.setFullName("Shekhar Gulati");
		authorRepository.persist(author);
		return author;
	}

	@Test
	public void testFindAllAuthors() {
		persistAuthor();
		List<Author> authors = authorRepository.findAllAuthors();
		assertEquals(1, authors.size());
	}

	@Test
	public void testFindAuthor() {
		Author persistAuthor = persistAuthor();
		Author findAuthor = authorRepository.findAuthor(persistAuthor.getId());
		assertEquals("test@gmail.com", findAuthor.getEmail());
	}
	
	@Test
	public void testFindByEmailAndPassword(){
		persistAuthor();
		Author author = authorRepository.findAuthorByEmailAndPassword("test@gmail.com", "password");
		Assert.assertNotNull(author);
	}

}
