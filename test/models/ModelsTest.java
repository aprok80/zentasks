package models;

import com.avaje.ebean.Ebean;
import org.junit.*;
import play.libs.Yaml;
import play.test.WithApplication;

import java.util.List;

import static junit.framework.Assert.*;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;

public class ModelsTest extends WithApplication
{
    @Before
    public void setUp()
    {
        start( fakeApplication( inMemoryDatabase() ) );
    }

    @Test
    public void createAndRetrieveUser()
    {
        new User("horst@p.d","Horst","password").save();
        User horst = User.find.where().eq("email","horst@p.d").findUnique();
        assertNotNull(horst);
        assertEquals( "Horst", horst.name );
    }

    @Test
    public void tryAuthenticateUser()
    {
        new User("horst@p.d","Horst","password").save();

        assertNotNull( User.authenticate("horst@p.d","password"));
        assertNull(User.authenticate("bla", "password"));
        assertNull( User.authenticate( "horst@p.d", "password44" )  );
    }

    @Test
    public void findProjectsInvolving()
    {
        new User( "horst@g.m", "Horst", "secret1" ).save();
        new User( "heinz@g.m", "Heinz", "secret1" ).save();

        Project.create( "Kacken", "Klo", "horst@g.m" );
        Project.create( "Pullern", "Klo", "heinz@g.m" );

        List<Project> involving = Project.findInvolving("heinz@g.m");
        assertEquals( 1, involving.size() );
        assertEquals( "Klo", involving.get(0).folder );
        assertEquals( "Pullern", involving.get(0).name );
    }

    @Test
    public void findTodoTasksInvolving()
    {
        User horst = new User("horst@g.m", "Horst", "secret1");
        horst.save();

        Project project = Project.create( "Kacken", "Klo", "horst@g.m" );

        Task task = new Task();
        task.title = "Klobrille hoch";
        task.assignedTo = horst;
        task.done = true;
        task.save();

        task = new Task();
        task.title = "Schub";
        task.project = project;
        task.save();

        List<Task> result = Task.findTodoInvolving("horst@g.m");
        assertEquals( 1, result.size() );
        assertEquals( "Schub", result.get(0).title );
    }

    @Test
    public void fullTest()
    {
        Ebean.save((List) Yaml.load("test-data.yml"));

        // Count things
        assertEquals(3, User.find.findRowCount());
        assertEquals(7, Project.find.findRowCount());
        assertEquals(5, Task.find.findRowCount());

        // Try to authenticate as users
        assertNotNull(User.authenticate("bob@example.com", "secret"));
        assertNotNull(User.authenticate("jane@example.com", "secret"));
        assertNull(User.authenticate("jeff@example.com", "badpassword"));
        assertNull(User.authenticate("tom@example.com", "secret"));

        // Find all Bob's projects
        List<Project> bobsProjects = Project.findInvolving("bob@example.com");
        assertEquals(5, bobsProjects.size());

        // Find all Bob's todo tasks
        List<Task> bobsTasks = Task.findTodoInvolving("bob@example.com");
        assertEquals(4, bobsTasks.size());
    }
}
