import play.*;
import play.libs.*;
import java.util.*;
import com.avaje.ebean.*;
import models.*;
import java.util.concurrent.*;
 
public class Global extends GlobalSettings {
 
  @Override
  public void onStart(Application app) {
 
    if(Ebean.find(Cliente.class).findRowCount() == 0) {
    
      Map<String,List<Object>> all = (Map<String,List<Object>>)Yaml.load("dados.yml");
      System.out.println(all.get("clientes").size());
      Ebean.save(all.get("clientes"));
    }
  }
}
