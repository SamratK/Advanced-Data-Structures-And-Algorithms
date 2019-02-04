package com.sam.adaa;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * AVLTree wrapper that keeps track of number of nodes in the tree.
 * 
 * @author SamratK
 * https://github.com/SamratK
 *
 */
public class MCAVLTree {
	private AtomicInteger size;
	private AVLTree avlTree;
	
	MCAVLTree() {
		this.avlTree = new AVLTree();
		this.size = new AtomicInteger();
	}
	
	AVLTreeNode search(int key) {
		AVLTreeNode node = null;
		
		//No write to happen while reading and any number of threads to be allowed to read at a time.
		node = avlTree.search(key);
		
		return node; 
	}
	
	boolean insert(int data) {
		boolean isInserted = false;
	
		//Only one insert operation is to be allowed at a time and not while any thread is reading.
		isInserted = avlTree.insert(data);
			
		if(isInserted) {
			size.incrementAndGet();	
		}
		
		return isInserted;
	}


	boolean delete(int key) {
		boolean isDeleted = false;
		
		//Only one delete operation is to be allowed at a time and not while any thread is reading.
		isDeleted = avlTree.delete(key);
			
		if(isDeleted) {
			size.decrementAndGet();
		}
		
		return isDeleted;
	}
	
	int getSize() {
		return size.get();
	}
	
	AVLTreeNode getRoot() {
		return avlTree.getRoot();
	}
}