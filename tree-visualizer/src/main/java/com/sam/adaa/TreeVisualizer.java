package com.sam.adaa;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * TreeVisualizer is used to visualize a binary tree. This is used by the projects in
 * the github repository - https://github.com/SamratK/Advanced-Data-Structures-And-Algorithms
 * 
 * @author SamratK
 * https://github.com/SamratK
 *
 */
public class TreeVisualizer extends Application {
	static GenericTreeNode root;

	public static void createTree(Object rootNode) {
		try {
			root = createGenericTreeNode(rootNode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		launch(new String[] {});
	}

	public static void createTree(int[] nodes) {
		try {
			root = createGenericTreeNode(nodes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		launch(new String[] {});
	}

	static GenericTreeNode createGenericTreeNode(Object rootNode) throws Exception {
		if (rootNode == null) {
			return null;
		}
		Class<?> classObj = rootNode.getClass();
		GenericTreeNode genericNode = null;
		try {
			Field dataField = classObj.getDeclaredField("data");
			dataField.setAccessible(true);
			int data = (Integer) dataField.get(rootNode);
			genericNode = new GenericTreeNode(String.valueOf(data));
		} catch (NoSuchFieldException nsf) {
			// Check for KDTree nodes.
			nsf.printStackTrace();
			Field dataField = classObj.getDeclaredField("point");
			dataField.setAccessible(true);
			int[] point = (int[]) dataField.get(rootNode);
			StringBuilder pointString = new StringBuilder();
			for (int i = 0; i < point.length; i++)
				pointString.append(point[i] + ",");

			genericNode = new GenericTreeNode(pointString.substring(0, pointString.length() - 1));
		}

		Field left = classObj.getDeclaredField("left");
		left.setAccessible(true);
		Field right = classObj.getDeclaredField("right");
		right.setAccessible(true);
		genericNode.left = createGenericTreeNode(left.get(rootNode));
		genericNode.right = createGenericTreeNode(right.get(rootNode));

		try {
			Field colorField = classObj.getDeclaredField("color");
			colorField.setAccessible(true);
			Color color = (Color) colorField.get(rootNode);
			genericNode.color = Color.LIGHTCORAL;
		} catch (NoSuchFieldException nsf) {
			// Do nothing.
		}

		try {
			Field priorityField = classObj.getDeclaredField("priority");
			priorityField.setAccessible(true);
			int priority = (Integer) priorityField.get(rootNode);
			genericNode.priority = priority;
		} catch (NoSuchFieldException nsf) {
			// Do nothing.
		}

		return genericNode;
	}

	static GenericTreeNode createGenericTreeNode(int[] nodes) throws Exception {
		GenericTreeNode genericNodes[] = new GenericTreeNode[nodes.length];
		GenericTreeNode genericNode = new GenericTreeNode(String.valueOf(nodes[0]));
		GenericTreeNode currNode = genericNode;
		genericNodes[0] = genericNode;
		for (int i = 0; i < nodes.length / 2; i++) {
			currNode = genericNodes[i];
			currNode.left = new GenericTreeNode(nodes[2 * i + 1] + "");
			genericNodes[2 * i + 1] = currNode.left;
			currNode.right = new GenericTreeNode(nodes[2 * i + 2] + "");
			genericNodes[2 * i + 2] = currNode.right;
		}
		return genericNode;
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setWidth(1000);
		stage.setHeight(500);
		Group group = null;
		group = getVisualElements(createVisualElements(), stage.getWidth());
		Scene scene = new Scene(group);
		stage.setScene(scene);
		stage.show();
	}

	ArrayList<VisualTreeNode> createVisualElements() {
		ArrayList<VisualTreeNode> elemList = new ArrayList<VisualTreeNode>();
		GenericTreeNode currNode = null;
		Queue<GenericTreeNode> queue = new LinkedList<GenericTreeNode>();
		queue.add(root);
		queue.add(null);
		Text parentNode = new Text(String.valueOf(root.data));
		if (root.priority != -1) {
			parentNode.setText(parentNode.getText() + " (" + root.priority + ")");
		}

		elemList.add(new VisualTreeNode(parentNode, null, true, root.color));
		elemList.add(null);
		int index = 0;
		while (!queue.isEmpty()) {
			currNode = queue.poll();
			if (currNode != null) {
				if (elemList.get(index) == null) {
					parentNode = elemList.get(++index).text;
				} else {
					parentNode = elemList.get(index).text;
				}
				if (currNode.left != null) {
					queue.add(currNode.left);
					if (currNode.priority != -1) {
						elemList.add(
								new VisualTreeNode(new Text(currNode.left.data + "(" + currNode.left.priority + ")"),
										parentNode, true, currNode.color));
					} else {
						elemList.add(
								new VisualTreeNode(new Text(currNode.left.data), parentNode, true, currNode.color));
					}
				}
				if (currNode.right != null) {
					queue.add(currNode.right);
					if (currNode.priority != -1) {
						elemList.add(
								new VisualTreeNode(new Text(currNode.right.data + " (" + currNode.right.priority + ")"),
										parentNode, false, currNode.color));
					} else {
						elemList.add(
								new VisualTreeNode(new Text(currNode.right.data), parentNode, false, currNode.color));
					}
				}
				index++;
			} else {

				if (!queue.isEmpty()) {
					queue.add(null);
					elemList.add(null);
				}

			}
		}
		return elemList;
	}

	Group getVisualElements(ArrayList<VisualTreeNode> list, double width) {
		Group group = new Group();
		double rootCenterX = width / 2;
		double rootCenterY = 40;
		Line line = null;
		double currentY = rootCenterY;
		double offsetX = 120;
		VisualTreeNode node = list.get(0);
		double fontSize = 20;

		node.text.setLayoutX(rootCenterX);
		node.text.setLayoutY(currentY);
		node.text.setFont(new Font(fontSize));
		group.getChildren().add(node.circle);
		group.getChildren().add(node.text);

		for (int i = 1; i < list.size(); i++) {
			node = list.get(i);
			if (node == null) {
				currentY += 100;
				offsetX = offsetX - 20;
				continue;
			}

			if (node.isLeft) {
				node.text.setLayoutX(node.parent.getLayoutX() - offsetX * 4 / i);// Make elements closer as the levels
																					// go down.
				// node.text.setLayoutX(node.parent.getLayoutX() - offsetX);
				node.text.setLayoutY(currentY);
			} else {
				node.text.setLayoutX(node.parent.getLayoutX() + offsetX * 4 / i);
				// node.text.setLayoutX(node.parent.getLayoutX() + offsetX);
				node.text.setLayoutY(currentY);
			}
			node.text.setFont(new Font(fontSize));
			line = new Line(node.parent.getLayoutX() + node.parent.getLayoutBounds().getWidth() / 2,
					node.parent.getLayoutY() + 14, node.text.getLayoutX() + node.text.getLayoutBounds().getWidth() / 2,
					node.text.getLayoutY() - node.text.getLayoutBounds().getHeight());
			group.getChildren().add(node.circle);
			group.getChildren().add(node.text);
			group.getChildren().add(line);
		}

		return group;
	}

	class VisualTreeNode {
		Text text;
		Text parent;
		boolean isLeft;
		Circle circle;
		Color fillColor;

		VisualTreeNode(final Text text, Text parent, boolean isLeft, Color color) {
			this.text = text;
			this.parent = parent;
			this.isLeft = isLeft;
			this.fillColor = color;
			circle = new Circle(20);
			circle.layoutXProperty().bind(text.layoutXProperty());
			circle.layoutYProperty().bind(text.layoutYProperty());
			circle.setTranslateX(10);
			circle.setTranslateY(-5);
			circle.setFill(Color.WHITE);
			circle.setStroke(Color.BLACK);
			circle.setStrokeWidth(1);
			if (fillColor != null) {
				circle.setFill(fillColor);
			}
		}

		public String toString() {
			if (parent == null) {
				return "[" + text.getText() + "]";
			}
			return "[" + text.getText() + "(" + parent.getText() + ")" + "]";
		}
	}

	static class GenericTreeNode {
		GenericTreeNode left;
		GenericTreeNode right;
		String data;

		GenericTreeNode(String data) {
			this.data = data;
		}

		Color color;
		int priority = -1;
	}
}