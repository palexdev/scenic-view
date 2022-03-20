/*
 * Scenic View,
 * Copyright (C) 2012 Jonathan Giles, Ander Ruiz, Amy Fowler
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

import javafx.application.Platform;
import javafx.stage.PopupWindow;
import javafx.stage.Window;
import org.fxconnector.StageControllerImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubWindowChecker extends WindowChecker {

	final StageControllerImpl model;

	public SubWindowChecker(final StageControllerImpl model) {
		super(window -> window instanceof PopupWindow, model.getID().toString());
		this.model = model;
	}

	final Map<PopupWindow, Map> previousTree = new HashMap<>();
	final List<PopupWindow> windows = new ArrayList<>();
	final Map<PopupWindow, Map> tree = new HashMap<>();

	@Override
	protected void onWindowsFound(final List<Window> tempPopups) {
		tree.clear();
		windows.clear();

		for (final Window popupWindow : tempPopups) {
			final Map<PopupWindow, Map> pos = valid((PopupWindow) popupWindow, tree);
			if (pos != null) {
				pos.put((PopupWindow) popupWindow, new HashMap<PopupWindow, Map>());
				windows.add((PopupWindow) popupWindow);
			}
		}
		if (!tree.equals(previousTree)) {
			previousTree.clear();
			previousTree.putAll(tree);
			final List<PopupWindow> actualWindows = new ArrayList<>(windows);
			Platform.runLater(() -> {
				// No need for synchronization here
				model.popupWindows.clear();
				model.popupWindows.addAll(actualWindows);
				model.update();

			});

		}

	}

	@SuppressWarnings("unchecked")
	Map<PopupWindow, Map> valid(final PopupWindow window, final Map<PopupWindow, Map> tree) {
		if (window.getOwnerWindow() == model.targetWindow)
			return tree;
		for (final PopupWindow type : tree.keySet()) {
			if (type == window.getOwnerWindow()) {
				return tree.get(type);
			} else {
				final Map<PopupWindow, Map> lower = valid(window, tree.get(type));
				if (lower != null)
					return lower;
			}
		}
		return null;
	}
}