package com.sam.adaa;

/**
 * 
 * Memory Efficient AVLTree is a memory efficient implementation of AVLTree. This uses iterative approach for BST operations
 * of search, insert and delete that makes use of constant auxiliary space instead of O(h) space where h is the height
 * of the BST in recursive approach.
 * 
 * @author SamratK
 * https://github.com/SamratK
 */
public class AVLTree {
	private AVLTreeNode root;
	private boolean isTreeUpdated;
	
	public boolean isBalanced(AVLTreeNode root) {
		if(root == null)
			return true;
		
		boolean leftBalanced = isBalanced(root.left);
		boolean rightBalanced = isBalanced(root.right);
		
		int balance = getBalance(root);
		
		return leftBalanced && rightBalanced && (balance >= -1 && balance <= 1);
	}
	
	public void inOrder(AVLTreeNode node){
		if(node==null)
			return;
		inOrder(node.left);
		System.out.print(node.data+"("+node.height+") ");
		inOrder(node.right);
	}
	
	public AVLTreeNode search(int key) {
		return search(root, key);
	}
	
	synchronized public boolean insert(int key) {
		isTreeUpdated = false;
		
		root = insert(root, key);
		
		return isTreeUpdated;
	}
	
	synchronized public boolean delete(int key) {
		isTreeUpdated = false;
		
		root = delete(root, key);
		
		return isTreeUpdated;
	}
	
	
	AVLTreeNode search(AVLTreeNode root, int key) {
		if(root == null || root.data == key)
			return root;
		
		AVLTreeNode currNode = root;
		
		while(currNode != null) {
			if(currNode.data == key) {
				return currNode;
			}else if(key < currNode.data) {
				currNode = currNode.left;
			}else {
				currNode = currNode.right;
			}
		}
		
		return currNode;
	}
	
	AVLTreeNode insert(AVLTreeNode root, int data) {
		if(root == null) {
			isTreeUpdated = true;
			return new AVLTreeNode(data, null);
		}
		
		AVLTreeNode currNode = root;
		AVLTreeNode prevNode = null;
		int balance = 0;
		AVLTreeNode parent = null;
		boolean isLeftChild = true;
		
		while(currNode != null) {
			if(data < currNode.data) {
				if(currNode.left == null) {
					currNode.left = new AVLTreeNode(data, currNode);
					isTreeUpdated = true;
					break;
				}
				currNode = currNode.left;
			}else if(data > currNode.data) {
				if(currNode.right == null) {
					currNode.right = new AVLTreeNode(data, currNode);
					isTreeUpdated = true;
					break;
				}
				currNode = currNode.right;
			}else {
				return root;
			}
		}
				
		while(currNode != null) {
			//Update height of ancestor node which might need to change due to rotations.
			currNode.height = 1 + Math.max(height(currNode.left), height(currNode.right));
			balance = getBalance(currNode);
			
			parent = currNode.parent;
			if(parent!=null) {//Can be handled outside the loop.
				if(currNode == parent.left) {
					isLeftChild = true;
				}else {
					isLeftChild = false;
				}
			}
			
	    	//Left Left case
	    	if(balance > 1 && getBalance(currNode.left) >= 0){
	    		currNode = rightRotate(currNode);
	    	}//Left Right case
	    	else if(balance > 1 && getBalance(currNode.left) < 0){
	    		currNode.left = leftRotate(currNode.left);
	    		currNode = rightRotate(currNode);
	    	}//Right Right case
	    	else if(balance < -1 && getBalance(currNode.right)<=0){
	    		currNode = leftRotate(currNode);
	    	}//Right Left case
	    	else if(balance < -1 && getBalance(currNode.right) > 0){
	    		currNode.right = rightRotate(currNode.right);
	    		currNode = leftRotate(currNode);
	    	}
			
			if(parent != null) {
				if(isLeftChild) {
					parent.left = currNode;
				}else {
					parent.right = currNode;
				}
			}
			
			prevNode = currNode;
			currNode = currNode.parent;
		}
		
		return prevNode;
	}
	
	AVLTreeNode delete(AVLTreeNode root, int key) {
		if(root == null)
			return null;
		
		AVLTreeNode currNode = root;
		AVLTreeNode parentNode = null;
		AVLTreeNode pred = null;
		boolean isLeftChild = true;
		boolean found = false;
		
		while(currNode != null) {
			if(key == currNode.data) {
				found = true;
				isTreeUpdated = true;
				
				if(currNode.left == null) {
					if(parentNode == null) {//Root node case.
						if(currNode.right != null)
							currNode.right.parent = null;
						
						return currNode.right;
					}
					
					if(isLeftChild) {
						parentNode.left = currNode.right;
					}else {
						parentNode.right = currNode.right;
					}
					
					if(currNode.right != null)
						currNode.right.parent = parentNode;
					
					break;
				}else if(currNode.right == null) {
					if(parentNode == null) {//Root node case.
						if(currNode.left != null)
							currNode.left.parent = null;
						
						return currNode.left;
					}
					
					if(isLeftChild) {
						parentNode.left = currNode.left;
					}else {
						parentNode.right = currNode.left;
					}
					
					if(currNode.left != null)
						currNode.left.parent = parentNode;
					
					break;
				}
				
				//The node to be deleted has both left and right children.
				//Find inorder predecessor.
				pred = findInOrderPredecessor(currNode);
				currNode.data = pred.data;
				key = currNode.data;
				
				parentNode = currNode;
				currNode = currNode.left;
				isLeftChild = true;
			} else {
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
		
		if(!found) {//If key is not found then nothing is deleted and hence no balancing is required.
			return root;
		}
		
		currNode = parentNode;
		parentNode = null;
		AVLTreeNode prevNode = null;
		int balance = 0;
		
		while(currNode != null) {
			currNode.height = 1 + Math.max(height(currNode.left), height(currNode.right));
			balance = getBalance(currNode);
			
			parentNode = currNode.parent;
			if(parentNode != null) {
				if(currNode == parentNode.left) {
					isLeftChild = true;
				}else {
					isLeftChild = false;
				}
			}
			
	    	//Left Left case
	    	if(balance > 1 && getBalance(currNode.left) >= 0){
	    		currNode = rightRotate(currNode);
	    	}//Left Right case
	    	else if(balance > 1 && getBalance(currNode.left) < 0){
	    		currNode.left = leftRotate(currNode.left);
	    		currNode = rightRotate(currNode);
	    	}//Right Right case
	    	else if(balance < -1 && getBalance(currNode.right)<=0){
	    		currNode = leftRotate(currNode);
	    	}//Right Left case
	    	else if(balance < -1 && getBalance(currNode.right) > 0){
	    		currNode.right = rightRotate(currNode.right);
	    		currNode = leftRotate(currNode);
	    	}
			
			if(parentNode != null) {
				if(isLeftChild) {
					parentNode.left = currNode;
				}else {
					parentNode.right = currNode;
				}
			}
			
			prevNode = currNode;
			currNode = currNode.parent;
		}
		
		return prevNode;
	}
	
	AVLTreeNode findInOrderPredecessor(AVLTreeNode node) {
		if(node.left == null)
			return node;
		
		AVLTreeNode pred = node.left;
		
		while(pred.right != null)
			pred = pred.right;
		
		return pred;
	}
	
	/*
	 * 		 node           T
	 * 			 \         / \
	 *	          T  => node  y
	 *			 / \      \
	 *			x   y      x
	 */
	AVLTreeNode leftRotate(AVLTreeNode node) {
		AVLTreeNode T = node.right;
		AVLTreeNode x = T.left;
		
		T.left = node;
		node.right = x;
		
		//Update heights.
		node.height = 1 + Math.max(height(node.left), height(node.right));
		T.height = 1 + Math.max(height(T.left), height(T.right));
		
		//Update parents.
		T.parent = node.parent;
		node.parent = T;
		
		if(x!=null)
			x.parent = node;

		return T;
	}
	
	/*
	 * 		 node       T
	 * 		/          / \
	 *	   T      =>  x   node
	 *	  / \            /
	 *	 x   y          y
	 */
	AVLTreeNode rightRotate(AVLTreeNode node) {
		AVLTreeNode T = node.left;
		AVLTreeNode y = T.right;
		
		T.right = node;
		node.left = y;
		
		//Update heights.
		node.height = 1 + Math.max(height(node.left), height(node.right));
		T.height = 1 + Math.max(height(T.left), height(T.right));
		
		//Update parents.
		T.parent = node.parent;
		node.parent = T;
		
		if(y!=null)
			y.parent = node;

		return T;
	}
	
	int height(AVLTreeNode node) {
		if(node == null)
			return 0;
		
		return node.height;
	}
	
	int getBalance(AVLTreeNode node) {
		if(node == null)
			return 0;
		
		return height(node.left) - height(node.right);
	}
	
	public AVLTreeNode getRoot() {
		return root;
	}
}