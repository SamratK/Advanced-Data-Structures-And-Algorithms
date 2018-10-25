package com.sam.adaa;

public class BSTNode{
	int data;
	BSTNode left;
	BSTNode right;
	
	BSTNode(int data){
		this.data = data;
	}
	
	public String toString() {
		return "["+data+"]";
	}
}