package com.summit.notebook.domain;

import com.summit.notebook.service.NotebookService;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.dod.RooDataOnDemand;
import org.springframework.stereotype.Component;

@Configurable
@Component
@RooDataOnDemand(entity = Notebook.class)
public class NotebookDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<Notebook> data;

	@Autowired
    NotebookService notebookService;

	public Notebook getNewTransientNotebook(int index) {
        Notebook obj = new Notebook();
        setAuthor(obj, index);
        setCreated(obj, index);
        setDescription(obj, index);
        setName(obj, index);
        setTags(obj, index);
        return obj;
    }

	public void setAuthor(Notebook obj, int index) {
        String author = "author_" + index;
        obj.setAuthor(author);
    }

	public void setCreated(Notebook obj, int index) {
        Date created = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setCreated(created);
    }

	public void setDescription(Notebook obj, int index) {
        String description = "description_" + index;
        if (description.length() > 4000) {
            description = description.substring(0, 4000);
        }
        obj.setDescription(description);
    }

	public void setName(Notebook obj, int index) {
        String name = "name_" + index;
        obj.setName(name);
    }

	public void setTags(Notebook obj, int index) {
        String[] tags = { "Y", "N" };
        obj.setTags(tags);
    }

	public Notebook getSpecificNotebook(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Notebook obj = data.get(index);
        BigInteger id = obj.getId();
        return notebookService.findNotebook(id);
    }

	public Notebook getRandomNotebook() {
        init();
        Notebook obj = data.get(rnd.nextInt(data.size()));
        BigInteger id = obj.getId();
        return notebookService.findNotebook(id);
    }

	public boolean modifyNotebook(Notebook obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = notebookService.findNotebookEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Notebook' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Notebook>();
        for (int i = 0; i < 10; i++) {
            Notebook obj = getNewTransientNotebook(i);
            try {
                notebookService.saveNotebook(obj);
            } catch (ConstraintViolationException e) {
                StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getConstraintDescriptor()).append(":").append(cv.getMessage()).append("=").append(cv.getInvalidValue()).append("]");
                }
                throw new RuntimeException(msg.toString(), e);
            }
            data.add(obj);
        }
    }
}
