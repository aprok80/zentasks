import com.avaje.ebean.Ebean;
import models.User;
import play.Application;
import play.GlobalSettings;
import play.libs.Yaml;

import java.util.List;

public class Global extends GlobalSettings
{
    @Override
    public void onStart(Application application)
    {
        if( User.find.findRowCount() == 0 )
        {
            Ebean.save( (List) Yaml.load( "initial-data.yml" ) );

        }
    }
}
