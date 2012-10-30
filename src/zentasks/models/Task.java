package zentasks.models;

import javax.persistence.*;
import com.avaje.ebean.*;
import java.util.*;

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

    public Task() {}

    public Task(String title, String folder, Project project) {
        this.title = title;
        this.folder = folder;
        this.project = project;
    }

    public static List<Task> findAll() {
        return Ebean.find(Task.class).findList();
    }

    public static Map<Project,List<Task>> findAllGroupByProject() {
        Map<Project,List<Task>> map = new HashMap<Project,List<Task>>();
        for (Task task : findAll()) {
            if (map.containsKey(task.getProject())) {
                map.get(task.getProject()).add(task);
            } else {
                List<Task> tasks = new ArrayList<Task>();
                tasks.add(task);
                map.put(task.getProject(), tasks);
            }
        }
        return map;
    }

    public static Map<String,List<Task>> findByProject(Project project) {
        List<Task> tasks = Ebean.find(Task.class)
                .where()
                    .eq("project.id", project.getId())
                    .findList();
        Map<String,List<Task>> map = new HashMap<String,List<Task>>();
        for (Task task : tasks) {
            String key = null;
            for (String folder : map.keySet())
                if (folder.equals(task.getFolder())) key = folder;
            if (key != null) map.get(key).add(task);
            else map.put(task.getFolder(), new ArrayList<Task>(Arrays.asList(task)));
        }
        return map;
    }

    public void save() { Ebean.save(this); }

    public void update() { Ebean.update(this); }
    
    public void delete() { Ebean.delete(this); }

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
