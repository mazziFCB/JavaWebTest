package com.assignment.utils;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

import org.primefaces.PrimeFaces;

public class Global {

	
	public static void showMessage(String message){
		showMessage(message, "Info", FacesMessage.SEVERITY_INFO);

	}
	public static void showMessage(String message,Severity severity){
		String title = "Info";
		if(severity == FacesMessage.SEVERITY_WARN){
			title = "Warning";
		}else if(severity == FacesMessage.SEVERITY_ERROR){
			title = "Error";
		}else if(severity == FacesMessage.SEVERITY_FATAL){
			title = "Error";
		}

		showMessage(message, title, severity);
	}
	
	public static void showMessage(String message,String title,Severity severity){
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(severity, title, message));
		PrimeFaces.current().ajax().update("growl");

	}
	
    public static void redirectUserTo(String page) {

//      RequestContext.getCurrentInstance().execute("window.onbeforeunload = null;window.location.href = '" + page + "'");
      PrimeFaces.current().executeScript("window.onbeforeunload = null;window.location.href = '" + page + "'");

  }

}
