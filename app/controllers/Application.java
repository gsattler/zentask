package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;

import views.html.*;

import models.*;
import controllers.Secured;

public class Application extends Controller {

    public Result login() {
    	return ok(login.render(form(Login.class)));
    }
    
    public Result authenticate() {
    	Form<Login> loginForm = form(Login.class).bindFromRequest();
    	if (loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        } else {
            session().clear();
            session("email", loginForm.get().email);
            return redirect(
                routes.Application.index()
            );
        }
    }
    
    public Result logout() {
    	session().clear();
    	flash("success", "You have been logged out");
    	return redirect(routes.Application.login());
    }
    
    public static class Login {
    	public String email;
    	public String password;
    	
    	public String validate() {
    	    if (User.authenticate(email, password) == null) {
    	      return "Invalid user or password";
    	    }
    	    return null;
    	}
    }
    
    @Security.Authenticated(Secured.class)
    public Result index() {
    	return ok(index.render(
    			Project.findInvolving(request().username()),
    			Task.findTodoInvolving(request().username()),
    			User.find.byId(request().username())));
    }
}


