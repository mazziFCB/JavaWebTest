package com.assign.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import utils.Constant;

public class LoadConfigurationListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		try {
            if (System.getProperties().getProperty(Constant.DATABASE_URL_PROPERTY) != null
                    && System.getProperty(Constant.DATABASE_URL_PROPERTY).length() != 0) {
                Constant.DATABASE_URL = System.getProperty(Constant.DATABASE_URL_PROPERTY);
            }
        } catch (Exception ex) {
        }
	}

}
