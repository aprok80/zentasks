package controllers;

import models.Project;
import models.Task;
import play.*;
import play.mvc.*;

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
  
}
