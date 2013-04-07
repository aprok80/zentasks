package models;

import play.db.ebean.Model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Project extends Model
{
    @Id
    public Long id;
    public String folder;
    public String name;
    @ManyToMany(cascade = CascadeType.REMOVE)
    public List<User> members = new ArrayList<>();

    public Project( String name, String folder, User owner )
    {
        this.folder = folder;
        this.name = name;
        this.members.add( owner );
    }

    public static Finder<Long, Project> find = new Finder<>( Long.class, Project.class );

    public static Project create(  String name, String folder, String owner )
    {
        Project project = new Project(name, folder, User.find.ref(owner) );
        project.save();
        project.saveManyToManyAssociations("members");
        return project;
    }

    public static List<Project> findInvolving( String user )
    {
        return find.where().eq("members.email",user).findList();
    }
}
