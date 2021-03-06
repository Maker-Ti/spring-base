package com.maker.demo.recommend.bean;


/**
 * 
 * @author wqd
 *
 */
//this bean create a node like
//userid	libraryid	libraryid	libraryid
//  1           2           3           4
//  2           3			4

public class HabitsBean extends BasicBean {
	private int id ;

	//get the ID
	public int getId() {
		return id;
	}

	//set the ID
	public void setId(int id) {
		this.id = id;
	}
	
	public HabitsBean() {
		this(-1);
	}
	
	//default id is -1,it means the id hadn't been evaluated
	public HabitsBean(int id) {
		this(id, 5);
	}
	
	public HabitsBean(int id, int n) {
		super(false);
		this.id = id;
	}
	
	//Override Object toString() method
	public String toString() {
		StringBuilder str = new StringBuilder("HabitBean " + this.id + " :");
		str.append(super.toString());
		return str.toString();
	}
	
}
