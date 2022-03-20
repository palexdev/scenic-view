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
package org.scenicview.model.update;

import javafx.application.Platform;
import org.fxconnector.AppController;
import org.fxconnector.StageController;
import org.scenicview.utils.Logger;
import org.scenicview.view.ScenicViewGui;

import java.util.ArrayList;
import java.util.List;

public final class AppsRepository {

	private final List<AppController> apps = new ArrayList<>();
	private final ScenicViewGui scenicView;

	public AppsRepository(ScenicViewGui scenicView) {
		this.scenicView = scenicView;
	}

	public List<AppController> getApps() {
		return apps;
	}

	private int findAppControllerIndex(final int appID) {
		for (int i = 0; i < apps.size(); i++) {
			if (apps.get(i).getID() == appID) {
				return i;
			}
		}
		return -1;
	}

	private int findStageIndex(final List<StageController> stages, final int stageID) {
		for (int i = 0; i < stages.size(); i++) {
			if (stages.get(i).getID().getStageID() == stageID) {
				return i;
			}
		}
		return -1;
	}

	public void stageRemoved(final StageController stageController) {
		Platform.runLater(() -> {
			dumpStatus("stageRemovedStart", stageController.getID().getStageID());
			final List<StageController> stages = apps.get(findAppControllerIndex(stageController.getID().getAppID())).getStages();
			// Remove and close
			stages.remove(findStageIndex(stages, stageController.getID().getStageID())).close();
			scenicView.removeStage(stageController);
			dumpStatus("stageRemovedStop", stageController.getID().getStageID());
		});
	}

	public void stageAdded(final StageController stageController) {
		Platform.runLater(() -> {
			dumpStatus("stageAddedStart", stageController.getID().getStageID());
			apps.get(findAppControllerIndex(stageController.getID().getAppID())).getStages().add(stageController);
			stageController.setEventDispatcher(scenicView.getStageModelListener());
			scenicView.configurationUpdated();
			dumpStatus("stageAddedStop", stageController.getID().getStageID());
		});
	}

	public void appRemoved(final AppController appController) {
		Platform.runLater(() -> {
			dumpStatus("appRemovedStart", appController.getID());
			// Remove and close
			apps.remove(findAppControllerIndex(appController.getID())).close();
			scenicView.removeApp(appController);
			dumpStatus("appRemovedStop", appController.getID());
		});
	}

	public void appAdded(final AppController appController) {
		Platform.runLater(() -> {
			dumpStatus("appAddedStart", appController.getID());
			if (!apps.contains(appController)) {
				if (apps.isEmpty() && !appController.getStages().isEmpty()) {
					scenicView.setActiveStage(appController.getStages().get(0));
				}
				apps.add(appController);
			}
			final List<StageController> stages = appController.getStages();
			for (StageController stage : stages) {
				stage.setEventDispatcher(scenicView.getStageModelListener());
			}
			scenicView.configurationUpdated();
			dumpStatus("appAddedStop", appController.getID());
		});
	}

	private void dumpStatus(final String operation, final int id) {
		Logger.print(operation + ":" + id);
		for (AppController app : apps) {
			Logger.print("App:" + app.getID());
			final List<StageController> scs = app.getStages();
			for (StageController sc : scs) {
				Logger.print("\tStage:" + sc.getID().getStageID());
			}
		}
	}
}
