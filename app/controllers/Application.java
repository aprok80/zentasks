package controllers;

import models.Project;
import models.Task;
import models.User;
import play.*;
import play.data.Form;
import play.mvc.*;
import static play.data.Form.*;

import views.html.*;

import java.util.List;

public class Application extends Controller {
  
    public static Result index() {

        List<Project> all = Project.find.all();
        return ok(index.render(
                all,
                Task.find.all()
        ));
    }

    public static Result login()
    {
        return ok( login.render( form( Login.class ) ) );
    }

    public static Result authenticate()
    {
        Form<Login> loginForm = form( Login.class ).bindFromRequest();
        if( loginForm.hasErrors() )
        {
            return badRequest( login.render( loginForm ) );
        }
        else
        {
            session().clear();
            session( "email", loginForm.get().email );
            return redirect( routes.Application.index() );
        }
    }

    public static class Login
    {
        public String email;
        public String password;

        public String validate()
        {
            if( User.authenticate( email, password ) == null )
            {
                return "Invalid user or password";
            }
            return null;
        }
    }
}
