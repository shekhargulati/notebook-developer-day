package com.summit.notebook.domain;

import com.summit.notebook.service.NoteService;
import java.math.BigInteger;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.roo.addon.test.RooIntegrationTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml")
@Transactional
@RooIntegrationTest(entity = Note.class, transactional = true)
@ActiveProfiles("test")
public class NoteIntegrationTest {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@After
	public void cleanup(){
		mongoTemplate.dropCollection(Note.class);
	}
	
    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    private NoteDataOnDemand dod;

	@Autowired
    NoteService noteService;

	@Test
    public void testCountAllNotes() {
        Assert.assertNotNull("Data on demand for 'Note' failed to initialize correctly", dod.getRandomNote());
        long count = noteService.countAllNotes();
        Assert.assertTrue("Counter for 'Note' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFindNote() {
        Note obj = dod.getRandomNote();
        Assert.assertNotNull("Data on demand for 'Note' failed to initialize correctly", obj);
        BigInteger id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Note' failed to provide an identifier", id);
        obj = noteService.findNote(id);
        Assert.assertNotNull("Find method for 'Note' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Note' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAllNotes() {
        Assert.assertNotNull("Data on demand for 'Note' failed to initialize correctly", dod.getRandomNote());
        long count = noteService.countAllNotes();
        Assert.assertTrue("Too expensive to perform a find all test for 'Note', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Note> result = noteService.findAllNotes();
        Assert.assertNotNull("Find all method for 'Note' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Note' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindNoteEntries() {
        Assert.assertNotNull("Data on demand for 'Note' failed to initialize correctly", dod.getRandomNote());
        long count = noteService.countAllNotes();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Note> result = noteService.findNoteEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'Note' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Note' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testSaveNote() {
        Assert.assertNotNull("Data on demand for 'Note' failed to initialize correctly", dod.getRandomNote());
        Note obj = dod.getNewTransientNote(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Note' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Note' identifier to be null", obj.getId());
        noteService.saveNote(obj);
        Assert.assertNotNull("Expected 'Note' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testDeleteNote() {
        Note obj = dod.getRandomNote();
        Assert.assertNotNull("Data on demand for 'Note' failed to initialize correctly", obj);
        BigInteger id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Note' failed to provide an identifier", id);
        obj = noteService.findNote(id);
        noteService.deleteNote(obj);
        Assert.assertNull("Failed to remove 'Note' with identifier '" + id + "'", noteService.findNote(id));
    }
}
