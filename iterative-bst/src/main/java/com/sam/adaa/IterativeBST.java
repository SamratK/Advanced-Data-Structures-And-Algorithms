package com.sam.adaa;

/**
 * Iterative BST
 * Memory efficient implementation of BST.
 * 
 * @author SamratK
 */
public class IterativeBST {

	void inOrder(BSTNode root) {
		if(root == null)
			return;
		
		inOrder(root.left);
		System.out.print(root);
		inOrder(root.right);
	}
	
	/*
	 * Recursive search.
	 */
	public BSTNode search2(BSTNode root, int key) {
	 if(root==null || root.data==key)
	     return root;

	 if(key < root.data)
	     return search2(root.left, key);

	 return search2(root.right, key);
	}
	
	/*
	 * Recursive insert.
	 */
	public BSTNode insert2(BSTNode root, int key) {
		if(root == null) {
			return new BSTNode(key);
		}
		
		if(key < root.data) {
			root.left = insert2(root.left, key);
		}else {
			root.right = insert2(root.right, key);
		}
		
		return root;
	}
	
	/*
	 * Recursive delete.
	 */
	public BSTNode delete2(BSTNode root, int key) {
		if(root == null)
			return root;
		
		if(key < root.data) {
			root.left = delete2(root.left, key);
		}else if(key > root.data){
			root.right = delete2(root.right, key);
		}else {
			if(root.left == null)
				return root.right;
			else if(root.right == null)
				return root.left;
			
			//Find inorder predecessor.
			BSTNode pred = findInOrderPredecessor(root);
			root.data = pred.data;
			
			root.left = delete2(root.left, root.data);
		}
		
		return root;
	}
	
	public BSTNode findInOrderPredecessor(BSTNode node) {
		if(node.left == null)
			return null;
		
		BSTNode pred = node.left;
		
		while(pred.right != null)
			pred = pred.right;
		
		return pred;
	}
	
	public BSTNode search(BSTNode root, int key) {
		 if(root==null || root.data==key)
		     return root;

		 BSTNode currNode = root;
		 
		 while(currNode != null) {
			 if(key < currNode.data) {
				 currNode = currNode.left;
			 }else if(key > currNode.data) {
				 currNode = currNode.right;
			 }else {
				 return currNode;
			 }
		 }
	
		 return currNode;
	}
	
	public BSTNode insert(BSTNode root, int key) {
		if(root == null) {
			return new BSTNode(key);
		}
		
		BSTNode currNode = root;
		
		while(currNode != null) {
			if(key < currNode.data) {
				if(currNode.left == null) {
					currNode.left = new BSTNode(key);
					break;
				}
				currNode = currNode.left;
				
			}else if(key > currNode.data) {
				if(currNode.right == null) {
					currNode.right = new BSTNode(key);
					break;
				}
				currNode = currNode.right;
			}else {
				break;
			}
		}
		
		return root;
	}
	
	public BSTNode delete(BSTNode root, int key) {
		if(root == null)
			return root;
		
		BSTNode currNode = root;
		BSTNode parentNode = null;
		boolean isLeftChild = true;
		
		while(currNode != null) {
			if(key == currNode.data) {
				if(currNode.left == null) {
					if(parentNode == null) {//Root node case.
						return currNode.right;
					}
					
					if(isLeftChild) {
						parentNode.left = currNode.right;
					}else {
						parentNode.right = currNode.right;
					}
					
					break;
				}else if(currNode.right == null) {
					if(parentNode == null) {//Root node case.
						return currNode.left;
					}
					
					if(isLeftChild) {
						parentNode.left = currNode.left;
					}else {
						parentNode.right = currNode.left;
					}
					
					break;
				}
				
				//The node to be deleted has both left and right children.
				//Find inorder predecessor.
				BSTNode pred = findInOrderPredecessor(currNode);
				currNode.data = pred.data;
				key = currNode.data;
				
				parentNode = currNode;
				currNode = currNode.left;
				isLeftChild = true;
			}else {
				parentNode = currNode;	
				if(key < currNode.data) {
					currNode = currNode.left;
					isLeftChild = true;
				}else {
					currNode = currNode.right;
					isLeftChild = false;
				}
			}
		}
		
		return root;
	}
}