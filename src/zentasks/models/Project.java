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
    private Long id;

    private String name;

    private String folder;

    @OneToMany
    private List<Task> tasks;

    @ManyToMany
    private List<User> members = new ArrayList<User>();

    public Project() {}

    public Project(String name, String folder, User owner) {
        this.name = name;
        this.folder = folder;
        this.members.add(owner);
    }
    
    /**
     * Retrieve project for user
     */
    public static List<Project> findInvolving(String user) {
        return Ebean.find(Project.class).where()
            .eq("members.email", user)
            .findList();
    }

    public void save() {
        Ebean.save(this);
        Ebean.saveManyToManyAssociations(this, "members");
    }

    /**
     * Delete all project in a folder
     */
    public static void deleteInFolder(String folder) {
        Ebean.createSqlUpdate(
            "delete from project where folder = :folder"
        ).setParameter("folder", folder).execute();
    }
    
    public void delete() { Ebean.delete(this); }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getFolder() { return folder; }

    public void setFolder(String folder) { this.folder = folder; }

    public List<Task> getTasks() { return tasks; }

    public void setTasks(List<Task> tasks) { this.tasks = tasks; }

    public List<User> getMembers() { return members; }

    public void setMembers(List<User> members) { this.members = members; }
    
}
