package com.summit.notebook.domain;

import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.roo.addon.test.RooIntegrationTest;
import org.springframework.test.context.ActiveProfiles;

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
}
