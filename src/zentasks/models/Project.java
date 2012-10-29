package zentasks.models;

import java.util.*;
import javax.persistence.*;
import com.avaje.ebean.*;

/**
 *
 * @author miyabetaiji
 */
@Entity
public class Project {
    @Id
    private Integer id;

    private String name;

    private String folder;

    @OneToMany
    private List<Task> tasks;

    public Project() {}

    public Project(String name, String folder) {
        this.name = name;
        this.folder = folder;
    }
    
   public static List<Project> findAll() {
        return Ebean.find(Project.class).findList();
    }
        
    public void save() { Ebean.save(this); }

    public void delete() { Ebean.delete(this); }

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }
    
    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getFolder() { return folder; }

    public void setFolder(String folder) { this.folder = folder; }

    public List<Task> getTasks() { return tasks; }

    public void setTasks(List<Task> tasks) { this.tasks = tasks; }
}
