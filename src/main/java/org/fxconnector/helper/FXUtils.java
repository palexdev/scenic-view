/*
 * Scenic View,
 * Copyright (C) 2012 Jonathan Giles, Ander Ruiz, Amy Fowler, Matthieu Brouillard
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fxconnector.helper;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Window;

public class FXUtils {
	/**
	 * Retrieves the parent of the given node
	 *
	 * @param n the node for which the parent is to be found
	 * @return the found parent or null
	 */
	public static Parent parentOf(Node n) {
		if (n == null) {
			return null;
		}
		Parent p = n.getParent();
		if (p != null) {
			return parentOf(p);
		}
		if (n instanceof Parent) {
			return (Parent) n;
		}

		return null;
	}


	/**
	 * Retrieves the root window containing the given node
	 *
	 * @param n the node to look window for
	 * @return the window the node belongs to, or null if it cannot be found
	 */
	public static Window windowOf(Node n) {
		if (n == null) {
			return null;
		}

		Parent p = n.getParent();
		if (p != null) {
			return windowOf(p);
		}

		if (n instanceof Parent container) {

			ObservableList<Window> windows = Window.getWindows();
			for (Window w : windows) {
				if (w.getScene() != null) {
					if (container == w.getScene().getRoot()) {
						return w;
					}
				}
			}
		}

		return null;
	}

	/**
	 * Retrieves the root window containing the given scene
	 *
	 * @param s the scene to look window for
	 * @return the window the scene belongs to, or null if it cannot be found
	 */
	public static Window windowOf(Scene s) {
		if (s == null) {
			return null;
		}

		ObservableList<Window> windows = Window.getWindows();
		for (Window w : windows) {
			if (s == w.getScene()) {
				return w;
			}
		}

		return null;
	}
}
