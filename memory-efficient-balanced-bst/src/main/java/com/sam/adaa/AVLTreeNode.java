package com.sam.adaa;

/***
 * AVLTreeNode that contains parent node reference.
 * 
 * @author SamratK
 * https://github.com/SamratK
 */
public class AVLTreeNode {
	int data;
	int height;
	
	AVLTreeNode(int data, AVLTreeNode parent){
		this.data = data;
		this.parent = parent;
		this.height = 1;
	}
	
	AVLTreeNode left;
	AVLTreeNode right;
	AVLTreeNode parent;
	
	@Override
	public String toString() {
		return "["+data+"]";
	}
}
