package com.sam.adaa;

public class TreeNode {
	public int data;
	public TreeNode left;
	public TreeNode right;

	public TreeNode(int data) {
		this.data = data;
	}

	public String toString() {
		return "[" + data + "]";
	}
}