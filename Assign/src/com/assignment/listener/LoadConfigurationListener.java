package com.assignment.listener;

import javax.servlet.ServletContextEvent;


import javax.servlet.ServletContextListener;

import com.assignment.utils.Constant;


public class LoadConfigurationListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		try {
            if (System.getProperties().getProperty(Constant.END_POINT_PROPERTY) != null
                    && System.getProperty(Constant.END_POINT_PROPERTY).length() != 0) {
                Constant.END_POINT = System.getProperty(Constant.END_POINT_PROPERTY);
            }
        } catch (Exception ex) {
        }
	}

}
