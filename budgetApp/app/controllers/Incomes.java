package controllers;


import models.Budget;
import models.Income;
import models.ScheduledIncome;
import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

public class Incomes extends Controller {

	public static Result addIncome() {
		
		DynamicForm form = DynamicForm.form().bindFromRequest();
    	String amount = form.get("income_amount");
    	String tags = form.get("income_tag_list");
    	String date = form.get("income_date");
    	String description = form.get("income_description");
		
    	String repeat = form.get("income_repeat");
    	
		if (repeat != null && !repeat.equals("0")) {
			// create with scheduled repeat
	    	long id = 0;
			ScheduledIncome scheduledIncome = new ScheduledIncome(date, repeat);
			id = ScheduledIncome.add(scheduledIncome);
			
			//TODO add method that runs scheduled task on only this new record
			Income income = new Income(session().get("connected_id"), amount, tags, date, description, id);
			Income.add(income);
		} else {
			// create only income
			Income income = new Income(session().get("connected_id"), amount, tags, date, description, (Long) null);
			Income.add(income);
		}
		
		
		return redirect(routes.Application.index());
	}
	
	public static Result incomes() {
    	return ok(income.render(Income.getIncomes(session("connected_id"))));
	}

}

