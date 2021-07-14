package jira;

import java.time.LocalDate;

import org.apache.log4j.Logger;

public class JiraRelease {
	private String name;
	private LocalDate date;
	private int id;			
	
	public JiraRelease(String name, LocalDate date) {
		this.name = name;
		this.date = date;
	}
	
	/*
	 * [DEBUG] Printer
	 */
	public void print() {
		String output = String.format("Name: %s%ndate: %s%nID: %d",this.name,this.date,this.id);
		Logger.getLogger(JiraRelease.class.getName()).info(output);
	}
	
	
	/*===============================================================================================
	 * Getters & Setters
	 */
	public JiraRelease(String name) {
		this.name = name;
	}
	
	public JiraRelease() {
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public LocalDate getReleaseDate() {
		return date;
	}
	
	public void setReleaseDate(LocalDate releaseDate) {
		this.date = releaseDate;
	}
	
	public int getID() {
		return id;
	}
	
	public void setID(int id) {
		this.id = id;
	}
}
