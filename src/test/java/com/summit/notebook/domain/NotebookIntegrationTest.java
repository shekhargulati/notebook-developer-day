package com.summit.notebook.domain;

import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.roo.addon.test.RooIntegrationTest;
import org.springframework.test.context.ActiveProfiles;

@RooIntegrationTest(entity = Notebook.class, transactional = true)
@ActiveProfiles("test")
public class NotebookIntegrationTest {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@After
	public void cleanup(){
		mongoTemplate.dropCollection(Notebook.class);
	}
	
    @Test
    public void testMarkerMethod() {
    }
}
