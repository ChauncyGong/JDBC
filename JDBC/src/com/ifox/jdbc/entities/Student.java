package com.ifox.jdbc.entities;

public class Student {
	
	private int id;
	
	private String flowId;
	
	private String name;
	
	private String idCard;
	
	private String examNum;
	
	private String subject;
	
	private int grade;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getExamNum() {
		return examNum;
	}

	public void setExamNum(String examNum) {
		this.examNum = examNum;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public Student() {}

	public Student(int id, String flowId, String name, String idCard,
			String examNum, String subject, int grade) {
		super();
		this.id = id;
		this.flowId = flowId;
		this.name = name;
		this.idCard = idCard;
		this.examNum = examNum;
		this.subject = subject;
		this.grade = grade;
	}

	@Override
	public String toString() {
		return "Student [id=" + id + ", flowId=" + flowId + ", name=" + name
				+ ", idCard=" + idCard + ", examNum=" + examNum + ", subject="
				+ subject + ", grade=" + grade + "]  \n";
	}

}
