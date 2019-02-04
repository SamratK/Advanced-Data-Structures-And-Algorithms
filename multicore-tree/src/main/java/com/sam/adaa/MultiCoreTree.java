package com.sam.adaa;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * MultiCoreTree is an implementation of binary search tree using the parallel processing offered by modern multi-core processors. Instead of using a single balanced BST, this tree will
 * create as many BSTs as configured. Will balance the inserts across the trees. If three cores are used then the operations would be as follows:-
 * search - O(logn/3). Three trees are searched by one thread each in parallel. Which ever finds the element aborts the search of other threads.
 * insert - O(2/3logn). Three trees are searched for element's existence, O(logn/3). Then the tree with minimum size is chosen and element is added in it, O(logn/3). Total, O(2/3logn).
 * delete - O(logn/3). Three trees are searched by one thread each in parallel. Which ever finds the element, deletes it and then aborts the operation of other threads.
 * 
 * Thread to CPU core mapping is beyond the scope of this implementation and left to the user. Thread affinity concepts can be explored for the purpose.
 * This implementation shows how to use Java concurrent features to reduce time complexities of BST operations when each worker thread is linked
 * to a core and all of them can run in parallel.
 * 
 * @author SamratK
 * https://github.com/SamratK
 */

public class MultiCoreTree {
	private int CORES;
	private MCAVLTree[] mavlTrees;
	private ExecutorService executorService;
	private enum MultiCoreOperation {SEARCH, DELETE};
	
	public MultiCoreTree() {
		this(3);//Using 3 cores for a quad core processor.
	}
	
	public MultiCoreTree(int cores) {
		this.CORES = cores;
		initialize();
	}

	private void initialize() {
		mavlTrees = new MCAVLTree[CORES];
		
		for(int i=0;i<CORES;i++) {
			mavlTrees[i] = new MCAVLTree();
		}
				
		//Here, the threads in the thread pool can be assigned to CPU cores.
		executorService = Executors.newFixedThreadPool(CORES);
	}
	
	public static void main(String[] args) throws InterruptedException {
		MultiCoreTree mct = new MultiCoreTree();
		
		for(int i=1;i<=100;i++) {
			mct.insert(i);
		}
		
		System.out.println("Search 100 - "+mct.search(100));
		System.out.println("Insert 100 - "+mct.insert(100));
		System.out.println("Delete 50 - "+mct.delete(50));
		System.out.println("Search 50 - "+mct.search(50));
		System.out.println("Delete 100 - "+mct.delete(100));
		System.out.println("Search 100 - "+mct.search(100));
		System.out.println("Insert 100 - "+mct.insert(100));
		
		mct.inOrder();

		mct.executorService.shutdown();
	}
	
	public void inOrder() {
		for(int i=0;i<mavlTrees.length;i++) {
			System.out.println("------Tree "+i+"------------");
			inOrder(mavlTrees[i].getRoot());
			System.out.println();
		}
	}
	
	private static void inOrder(AVLTreeNode root) {
		if(root == null)
			return;
		
		inOrder(root.left);
		System.out.print(root);
		inOrder(root.right);
	}
	
	/*
	 * Search results could overlap with insert or delete operations similar to the
	 * behavior of ConcurrentHashMap.
	 */
	public AVLTreeNode search(final int key) {
		AtomicInteger result = new AtomicInteger(Integer.MIN_VALUE);
		Thread searchThread = Thread.currentThread();
		AtomicInteger futureProgressCount = new AtomicInteger(0);
		
		//key might be present in any tree, search all the trees.
		Runnable searchTasks[] = new Runnable[CORES];
		Future futures[] = new Future[CORES];

		for(int i=0;i<searchTasks.length;i++) {
			searchTasks[i] = new TreeTask(key, i, futures, futureProgressCount, 
					searchThread, MultiCoreOperation.SEARCH, result);
			futures[i] = executorService.submit(searchTasks[i]);
		}
	
		try {
			Thread.sleep(10000);//Maximum wait of 10 seconds.
		} catch (InterruptedException e1) {
		} finally {
			if(result.get() == Integer.MIN_VALUE)
				result = null;
		}
		
		return result == null? null : new AVLTreeNode(result.get(), null);
	}
	
	/*
	 * When insert is called by a requesting thread all threads calling delete will wait due to implicit lock.
	 * Search can be called.
	 */
	public synchronized boolean insert(int data) {
		boolean inserted = false;
		
		//Search for the element.
		if(search(data) == null) {
			//Insert the element in tree with minimum size.
			int min = mavlTrees[0].getSize();
			int minIndex = 0;
			
			for(int i=1;i<mavlTrees.length;i++) {
				if(mavlTrees[i].getSize() < min) {
					min = mavlTrees[i].getSize();
					minIndex = i;
				}
			}

			mavlTrees[minIndex].insert(data);
			inserted = true;
		}
		
		return inserted;
	}
	
	/*
	 * When delete is called by a requesting thread all threads calling insert will wait due to implicit lock.
	 * Search can be called.
	 */
	public synchronized boolean delete(int key) {
		AtomicBoolean result = new AtomicBoolean(false);
		Thread deleteThread = Thread.currentThread();
		AtomicInteger futureProgressCount = new AtomicInteger(0);
		
		//key might be present in any tree, to delete search all the trees.
		Runnable deleteTasks[] = new Runnable[CORES];
		Future futures[] = new Future[CORES];

		for(int i=0;i<deleteTasks.length;i++) {
			deleteTasks[i] = new TreeTask(key, i, futures, futureProgressCount, 
					deleteThread, MultiCoreOperation.DELETE, result);
			futures[i] = executorService.submit(deleteTasks[i]);
		}
	
		try {
				Thread.sleep(10000);//Maximum wait of 10 seconds.
		} catch (InterruptedException e1) {
		}
		
		return result.get();
	}
	
	class TreeTask implements Runnable{
		private int key;
		private int taskIndex;
		private Future[] futures;
		private AtomicInteger futureProgressCount;
		private Thread thread;
		private MultiCoreOperation operation;
		private Object result;
		
		TreeTask(int key, int taskIndex, Future[] futures, AtomicInteger futureProgressCount,
				Thread callingThread, MultiCoreOperation operation, Object result){
			this.key = key;
			this.taskIndex = taskIndex;
			this.futures = futures;
			this.futureProgressCount = futureProgressCount;
			this.thread = callingThread;
			this.operation = operation;
			this.result = result;
		}

		public void run() {
			if(MultiCoreOperation.SEARCH.equals(operation)) {
				AVLTreeNode node = mavlTrees[taskIndex].search(key);
				if(node != null) {
					//Cancel other threads when current thread found the value.
					for(int k=0;k<futures.length;k++) {
						if(k!=taskIndex) {
							futures[k].cancel(true);
						}
					}
					
					((AtomicInteger)result).set(node.data);
					
					//Interrupt the request thread as the value is found.
					thread.interrupt();
				}
			}else {
				boolean deleted = mavlTrees[taskIndex].delete(key);
				
				if(deleted) {
					//Cancel other threads when current thread deleted the value.
					for(int k=0;k<futures.length;k++) {
						if(k!=taskIndex) {
							futures[k].cancel(true);
						}
					}
					
					((AtomicBoolean)result).set(true);
					thread.interrupt();
				}
			}
			
			//Increment the future progress count when current task is about to complete.
			futureProgressCount.incrementAndGet();
			
			//Check if other tasks are also complete. If yes, then interrupt the requesting thread.
			if(futureProgressCount.get() == futures.length) {
				thread.interrupt();
			}
		}
	}
}