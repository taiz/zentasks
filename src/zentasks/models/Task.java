package zentasks.models;

import javax.persistence.*;
import com.avaje.ebean.*;
import java.util.Date;
import java.util.List;

/**
 *
 * @author miyabetaiji
 */
@Entity
public class Task {
    @Id
    private Long id;
    
    private String title;
    
    private boolean done = false;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dueDate;

    //@ManyToOne
    //private User assignedTo;
    
    private String folder;
    
    @ManyToOne
    private Project project;    

    public static List<Task> findAll() {
        return Ebean.find(Task.class).findList();
    }
       
    public Long getId() { return id; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }
    
    public void setId(Long id) { this.id = id; }

    public boolean isDone() { return done; }

    public void setDone(boolean done) { this.done = done; }

    public Date getDueDate() { return dueDate; }

    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }

    public String getFolder() { return folder; }

    public void setFolder(String folder) { this.folder = folder; }

    public Project getProject() { return project; }

    public void setProject(Project project) { this.project = project; }

}
