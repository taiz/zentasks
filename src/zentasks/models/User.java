/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zentasks.models;

/**
 *
 * @author miyabetaiji
 */
import java.util.*;
import javax.persistence.*;

import com.avaje.ebean.*;

/**
 * User entity managed by Ebean
 */
@Entity 
@Table(name="account")
public class User {

    @Id
    private String email;
    
    private String name;
    
    private String password;
    
    // -- Queries

    public static User authenticate(String email, String password) {
        return Ebean.find(User.class).where()
            .eq("email", email)
            .eq("password", password)
            .findUnique();
    }
    
    public static List<User> findAll() {
        return Ebean.find(User.class).findList();
    }
    
    public static User find(String email) {
        return Ebean.find(User.class, email);
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
    public String toString() {
        return "User(" + email + ")";
    }
}