package com.hofman.sudoku;

import java.util.HashSet;
import java.util.Set;

public class Box {

	Set<Integer> possSet = new HashSet<Integer>();
	int value;
	
	public Box() {
		this.value = 0;
		for (int i=1; i<10; i++) this.possSet.add(i);
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}
	
	public void showSetPoss() {
		System.out.println(this.possSet.toString());
	}
	
	public void removePoss(int remove) {
		this.possSet.remove(remove);
	}
}
