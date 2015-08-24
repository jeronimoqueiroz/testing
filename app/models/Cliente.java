package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

@Entity
public class Cliente extends Model {
	 
	  @Id
	  public Long id;
	 
	  public String nome;
	 
	  public String telefone;
	  public String email;
	 
	  public static Model.Finder<Long,Cliente> find = new Model.Finder(Long.class, Cliente.class);
	 
	  public static List<Cliente> findAll() {
	    return find.all();
	  }
	 
	  public String toString() {
	    return nome;
	  }

}
