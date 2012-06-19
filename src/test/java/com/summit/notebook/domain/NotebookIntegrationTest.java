package com.summit.notebook.domain;

import com.summit.notebook.service.NotebookService;
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
@RooIntegrationTest(entity = Notebook.class, transactional = true)
@ActiveProfiles("test")
public class NotebookIntegrationTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @After
    public void cleanup() {
        mongoTemplate.dropCollection(Notebook.class);
    }

    @Test
    public void testMarkerMethod() {
    }

    @Autowired
    private NotebookDataOnDemand dod;

    @Autowired
    NotebookService notebookService;

    @Test
    public void testCountAllNotebooks() {
        Assert.assertNotNull("Data on demand for 'Notebook' failed to initialize correctly", dod.getRandomNotebook());
        long count = notebookService.countAllNotebooks();
        Assert.assertTrue("Counter for 'Notebook' incorrectly reported there were no entries", count > 0);
    }

    @Test
    public void testFindNotebook() {
        Notebook obj = dod.getRandomNotebook();
        Assert.assertNotNull("Data on demand for 'Notebook' failed to initialize correctly", obj);
        BigInteger id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Notebook' failed to provide an identifier", id);
        obj = notebookService.findNotebook(id);
        Assert.assertNotNull("Find method for 'Notebook' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Notebook' returned the incorrect identifier", id, obj.getId());
    }

    @Test
    public void testFindAllNotebooks() {
        Assert.assertNotNull("Data on demand for 'Notebook' failed to initialize correctly", dod.getRandomNotebook());
        long count = notebookService.countAllNotebooks();
        Assert.assertTrue(
                "Too expensive to perform a find all test for 'Notebook', as there are "
                        + count
                        + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test",
                count < 250);
        List<Notebook> result = notebookService.findAllNotebooks();
        Assert.assertNotNull("Find all method for 'Notebook' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Notebook' failed to return any data", result.size() > 0);
    }

    @Test
    public void testFindNotebookEntries() {
        Assert.assertNotNull("Data on demand for 'Notebook' failed to initialize correctly", dod.getRandomNotebook());
        long count = notebookService.countAllNotebooks();
        if (count > 20)
            count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Notebook> result = notebookService.findNotebookEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'Notebook' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Notebook' returned an incorrect number of entries", count, result.size());
    }

    @Test
    public void testSaveNotebook() {
        Assert.assertNotNull("Data on demand for 'Notebook' failed to initialize correctly", dod.getRandomNotebook());
        Notebook obj = dod.getNewTransientNotebook(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Notebook' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Notebook' identifier to be null", obj.getId());
        notebookService.saveNotebook(obj);
        Assert.assertNotNull("Expected 'Notebook' identifier to no longer be null", obj.getId());
    }

    @Test
    public void testDeleteNotebook() {
        Notebook obj = dod.getRandomNotebook();
        Assert.assertNotNull("Data on demand for 'Notebook' failed to initialize correctly", obj);
        BigInteger id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Notebook' failed to provide an identifier", id);
        obj = notebookService.findNotebook(id);
        notebookService.deleteNotebook(obj);
        Assert.assertNull("Failed to remove 'Notebook' with identifier '" + id + "'", notebookService.findNotebook(id));
    }
}
