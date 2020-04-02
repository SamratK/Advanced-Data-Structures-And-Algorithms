package com.sam.adaa;

/**
 * Post Order traversal of a Binary Tree using O(n) time complexity and O(1)
 * space.
 * 
 * @author SamratK
 */
public class PostOrder {
	TreeNode root;

	public static void main(String[] args) {
		PostOrder tree = new PostOrder();
		tree.root = new TreeNode(1);
		tree.root.left = new TreeNode(2);
		tree.root.right = new TreeNode(3);
		tree.root.left.left = new TreeNode(4);
		tree.root.left.right = new TreeNode(5);
		tree.root.right.left = new TreeNode(6);
		tree.root.right.right = new TreeNode(7);
		tree.root.left.right.left = new TreeNode(8);
		tree.root.left.right.right = new TreeNode(9);

		System.out.println("Post order traversal :-");
		tree.postOrder(tree.root);
		System.out.println("\n");
		System.out.println("Constant space post order traversal method 1 :-");
		tree.postOrderTraversalConstantSpace1(tree.root);
		System.out.println("\n");
		System.out.println("Constant space post order traversal method 2 :-");
		tree.postOrderTraversalConstantSpace2(tree.root);
		System.out.println("\n");
		System.out.println("Constant space post order traversal method 3 :-");
		tree.postOrderTraversalConstantSpace3(tree.root);
		TreeVisualizer.createTree(tree.root);
	}

	public static void main2(String[] args) {
		PostOrder tree = new PostOrder();
		tree.root = new TreeNode(1);
		tree.root.left = new TreeNode(2);
		tree.root.right = new TreeNode(3);
		tree.root.left.left = new TreeNode(4);
		tree.root.left.left.left = new TreeNode(8);
		tree.root.left.left.right = new TreeNode(9);
		tree.root.left.left.right.left = new TreeNode(12);
		tree.root.left.right = new TreeNode(5);
		tree.root.right.left = new TreeNode(6);
		tree.root.right.right = new TreeNode(7);
		tree.root.right.right.left = new TreeNode(10);
		tree.root.right.right.right = new TreeNode(11);
		tree.root.right.right.left.left = new TreeNode(13);
		tree.root.right.right.right.right = new TreeNode(14);
		tree.root.right.right.left.left.right = new TreeNode(15);
		tree.root.right.right.left.left.right.left = new TreeNode(16);

		System.out.println("Post order traversal :-");
		tree.postOrder(tree.root);
		System.out.println();
		System.out.println();
		System.out.println("Constant space post order traversal method 1 :-");
		tree.postOrderTraversalConstantSpace1(tree.root);
		System.out.println();
		System.out.println();
		System.out.println("Constant space post order traversal method 2 :-");
		tree.postOrderTraversalConstantSpace2(tree.root);
		System.out.println();
		System.out.println();
		System.out.println("Constant space post order traversal method 3 :-");
		tree.postOrderTraversalConstantSpace3(tree.root);
		TreeVisualizer.createTree(tree.root);
	}

	/**
	 * Using pointers to predecessors and dummy root node.
	 * 
	 * @param root
	 */
	void postOrderTraversalConstantSpace1(TreeNode root) {
		if (root == null)
			return;

		TreeNode current = new TreeNode(-1), pre = null, prevNode = null;
		current.left = root;

		while (current != null) {
			if (current.left == null) {
				current.left = prevNode;
				prevNode = current;
				current = current.right;
			} else {
				pre = current.left;
				while (pre.right != null && pre.right != current)
					pre = pre.right;

				if (pre.right == null) {
					pre.right = current;
					current = current.left;
				} else {
					pre.right = null;
					System.out.print(pre);

					TreeNode ptr = pre;
					TreeNode netChild = pre;
					TreeNode prevPtr = pre;
					while (ptr != null) {
						if (ptr.right == netChild) {
							System.out.print(ptr);
							netChild = ptr;
							prevPtr.left = null;
						}
						if (ptr == current.left)
							break;

						prevPtr = ptr;
						ptr = ptr.left;
					}

					prevNode = current;
					current = current.right;
				}
			}
		}
	}

	/**
	 * Using pointers to predecessors and without dummy root node.
	 * 
	 * @param root
	 */
	void postOrderTraversalConstantSpace2(TreeNode root) {
		if (root == null)
			return;

		TreeNode current = null, prevNode = null, pre = null, ptr = null, netChild = null, prevPtr = null;
		current = root;

		while (current != null) {
			if (current.left == null) {
				current.left = prevNode;
				prevNode = current;
				current = current.right;
			} else {
				pre = current.left;
				while (pre.right != null && pre.right != current)
					pre = pre.right;

				if (pre.right == null) {
					pre.right = current;
					current = current.left;
				} else {
					pre.right = null;
					System.out.print(pre);

					ptr = pre;
					netChild = pre;
					prevPtr = pre;
					while (ptr != null) {
						if (ptr.right == netChild) {
							System.out.print(ptr);
							netChild = ptr;
							prevPtr.left = null;
						}
						if (ptr == current.left)
							break;

						prevPtr = ptr;
						ptr = ptr.left;
					}

					prevNode = current;
					current = current.right;
				}
			}
		}

		System.out.print(prevNode);

		// Last path traversal that includes the root.
		ptr = prevNode;
		netChild = prevNode;
		prevPtr = prevNode;
		while (ptr != null) {
			if (ptr.right == netChild) {
				System.out.print(ptr);
				netChild = ptr;
				prevPtr.left = null;
			}
			if (ptr == root)
				break;

			prevPtr = ptr;
			ptr = ptr.left;
		}
	}

	/**
	 * Using dummy root node without using pointers to predecessors.
	 * 
	 * @param root
	 */
	void postOrderTraversalConstantSpace3(TreeNode root) {
		if (root == null)
			return;

		TreeNode current = new TreeNode(-1), pre = null, temp = null, prev = null, succ = null;
		current.left = root;

		while (current != null) {
			if (current.left == null) {
				current = current.right;
			} else {
				pre = current.left;
				while (pre.right != null && pre.right != current)
					pre = pre.right;

				if (pre.right == null) {
					pre.right = current;
					current = current.left;
				} else {
					pre.right = null;
					succ = current;
					current = current.left;
					prev = null;

					while (current != null) {
						temp = current.right;
						current.right = prev;
						prev = current;
						current = temp;
					}

					while (prev != null) {
						System.out.print(prev);
						temp = prev.right;
						prev.right = current;
						current = prev;
						prev = temp;
					}

					current = succ;
					current = current.right;
				}
			}
		}
	}

	void postOrder(TreeNode root) {
		if (root == null)
			return;

		postOrder(root.left);
		postOrder(root.right);
		System.out.print(root);
	}
}