package com.summit.notebook.domain;

import org.junit.Test;
import org.springframework.roo.addon.test.RooIntegrationTest;

@RooIntegrationTest(entity = Note.class, transactional = false)
public class NoteIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }
}
