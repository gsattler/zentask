package models;

import java.util.*;

import javax.persistence.*;

import play.api.libs.ws.ssl.Equal;
import play.db.ebean.*;
import com.avaje.ebean.Model;
import com.avaje.ebean.Model.Finder;
import com.avaje.ebean.Ebean;
import com.avaje.ebean.*;
import javax.persistence.*;

@Entity
public class Task extends Model {
	@Id
	public long id;
	public String title;
	public boolean done = false;
	public Date dueDate;
	@ManyToOne
	public User assignedTo;
	public String folder;
	@ManyToOne
	public Project project;
	
	public static Finder<Long, Task> find = new Finder<Long,Task>(Task.class);
	
	public static List<Task> findTodoInvolving(String user) {
		return find.fetch("project").where()
				.eq("done", false)
				.eq("project.members.email", user)
			.findList();
	}
	
	public static Task create(Task task, Long project, String folder) {
		task.project = Project.find.ref(project);
		task.folder = folder;
		task.save();
		return task;
	}

}
