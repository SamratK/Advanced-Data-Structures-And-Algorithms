package com.sam.adaa;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert;

public class IterativeBSTTest {
	
	private static IterativeBST iterativeBST;
	
	@BeforeClass
	public static void setup() {
		iterativeBST = new IterativeBST();
	}
	
	/*
	 * Test recursive methods.
	 */
	@Test
	public void test1(){
		   BSTNode root = null;
	       /* Let us create following BST
	             50
	          /     \
	         30      70
	        /  \    /  \
	       20   40  60   80 */
	       root=iterativeBST.insert2(root,50);
	       root=iterativeBST.insert2(root,30);
	       root=iterativeBST.insert2(root,20);
	       root=iterativeBST.insert2(root,40);
	       root=iterativeBST.insert2(root,70);
	       root=iterativeBST.insert2(root,60);
	       root=iterativeBST.insert2(root,80);
	       
	       System.out.println("inOrder traversal of the given tree");
	       iterativeBST.inOrder(root);
	       
	       System.out.println("\ndelete 70");
	       iterativeBST.delete2(root, 70);
	       
	       System.out.println("inOrder traversal of the modified tree");
	       iterativeBST.inOrder(root);
	       
	       System.out.println("\nsearch 70 - "+iterativeBST.search2(root, 70));
	       Assert.assertNull(iterativeBST.search2(root, 70));
	       
	       System.out.println("\ndelete 20");
	       iterativeBST.delete2(root, 20);
	       
	       System.out.println("inOrder traversal of the modified tree");
	       iterativeBST.inOrder(root);
	       
	       System.out.println("\nsearch 50 - "+iterativeBST.search2(root, 50));
	       Assert.assertNotNull(iterativeBST.search2(root, 50));
	       
	       System.out.println("\ndelete 30");
	       iterativeBST.delete2(root, 30);
	       
	       System.out.println("inOrder traversal of the modified tree");
	       iterativeBST.inOrder(root);
	       
	       System.out.println("\nsearch 20 - "+iterativeBST.search2(root, 20));
	       Assert.assertNull(iterativeBST.search2(root, 20));
	       
	       System.out.println("\ndelete 50");
	       iterativeBST.delete2(root, 50);
	       
	       System.out.println("inOrder traversal of the modified tree");
	       iterativeBST.inOrder(root);
	       
	       System.out.println("\nsearch 80 - "+iterativeBST.search2(root, 80));
	       Assert.assertNotNull(iterativeBST.search2(root, 80));
	       
	}
	
	/*
	 * Test iterative methods.
	 */
	@Test
	public void test2(){
		   BSTNode root = null;
	       /* Let us create following BST
	             50
	          /     \
	         30      70
	        /  \    /  \
	       20   40  60   80 */
	       root=iterativeBST.insert(root,50);
	       root=iterativeBST.insert(root,30);
	       root=iterativeBST.insert(root,20);
	       root=iterativeBST.insert(root,40);
	       root=iterativeBST.insert(root,70);
	       root=iterativeBST.insert(root,60);
	       root=iterativeBST.insert(root,80);
	       
	       System.out.println("inOrder traversal of the given tree");
	       iterativeBST.inOrder(root);
	       
	       System.out.println("\ndelete 70");
	       iterativeBST.delete(root, 70);
	       
	       System.out.println("inOrder traversal of the modified tree");
	       iterativeBST.inOrder(root);
	       
	       System.out.println("\nsearch 70 - "+iterativeBST.search(root, 70));
	       Assert.assertNull(iterativeBST.search(root, 70));
	       
	       System.out.println("\ndelete 20");
	       iterativeBST.delete(root, 20);
	       
	       System.out.println("inOrder traversal of the modified tree");
	       iterativeBST.inOrder(root);
	       
	       System.out.println("\nsearch 50 - "+iterativeBST.search(root, 50));
	       Assert.assertNotNull(iterativeBST.search(root, 50));
	       
	       System.out.println("\ndelete 30");
	       iterativeBST.delete(root, 30);
	       
	       System.out.println("inOrder traversal of the modified tree");
	       iterativeBST.inOrder(root);
	       
	       System.out.println("\nsearch 20 - "+iterativeBST.search(root, 20));
	       Assert.assertNull(iterativeBST.search(root, 20));
	       
	       System.out.println("\ndelete 50");
	       iterativeBST.delete(root, 50);
	       
	       System.out.println("inOrder traversal of the modified tree");
	       iterativeBST.inOrder(root);
	       
	       System.out.println("\nsearch 80 - "+iterativeBST.search(root, 80));
	       Assert.assertNotNull(iterativeBST.search(root, 80));
	       
	}
}
