package controllers;

import java.util.Map;

import com.avaje.ebean.Expr;
import com.avaje.ebean.Page;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import models.Cliente;

import play.*;
import play.libs.Json;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    
    public static Result list() {
        /**
         * Obtem parametros de pesquisa caso possua
         */
        Map<String, String[]> params = request().queryString();
     
        Integer iTotalRecords = Cliente.find.findRowCount();
        String filter = params.get("sSearch")[0];
        Integer pageSize = Integer.valueOf(params.get("iDisplayLength")[0]);
        Integer page = Integer.valueOf(params.get("iDisplayStart")[0]) / pageSize;
     
        /**
         * Inicia o sort por nome, e configura o tratamento do sort para as demais colunas
         */
        String sortBy = "nome";
        String order = params.get("sSortDir_0")[0];
     
        switch(Integer.valueOf(params.get("iSortCol_0")[0])) {
          case 0 : sortBy = "nome"; break;
          case 1 : sortBy = "telefone"; break;
          case 2 : sortBy = "email"; break;
        }
     
        /**
         * monta a paginacao pelo banco. e filtro caso possua
         */
        Page<Cliente> contactsPage = Cliente.find.where(
          Expr.or(
            Expr.ilike("nome", "%"+filter+"%"),
            Expr.or(
              Expr.ilike("telefone", "%"+filter+"%"),
              Expr.ilike("email", "%"+filter+"%")
            )
          )
        )
        .orderBy(sortBy + " " + order + ", id " + order)
        .findPagingList(pageSize).setFetchAhead(false)
        .getPage(page);
     
        Integer iTotalDisplayRecords = contactsPage.getTotalRowCount();
     
        /**
         * monta o json
         */
        ObjectNode result = Json.newObject();
     
        result.put("sEcho", Integer.valueOf(params.get("sEcho")[0]));
        result.put("iTotalRecords", iTotalRecords);
        result.put("iTotalDisplayRecords", iTotalDisplayRecords);
     
        ArrayNode an = result.putArray("aaData");
     
        for(Cliente c : contactsPage.getList()) {
          ObjectNode row = Json.newObject();
          row.put("0", c.nome);
          row.put("1", c.telefone);
          row.put("2", c.email);
          an.add(row);
        } 
        return ok(result);
     }
    }
