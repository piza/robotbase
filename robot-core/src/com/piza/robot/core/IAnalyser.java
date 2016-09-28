package com.piza.robot.core;


public interface IAnalyser {

    public String getName();

	public Analysis analyse(String input);
	
	public String getTaskName();
	
}
