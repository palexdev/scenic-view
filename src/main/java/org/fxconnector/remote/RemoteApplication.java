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
package org.fxconnector.remote;

import org.fxconnector.Configuration;
import org.fxconnector.StageID;
import org.fxconnector.details.DetailPaneType;
import org.fxconnector.event.FXConnectorEventDispatcher;
import org.fxconnector.node.SVNode;

import java.rmi.Remote;
import java.rmi.RemoteException;

interface RemoteApplication extends Remote {

	void configurationUpdated(final StageID id, Configuration configuration) throws RemoteException;

	void update(final StageID id) throws RemoteException;

	void setEventDispatcher(final StageID id, FXConnectorEventDispatcher dispatcher) throws RemoteException;

	StageID[] getStageIDs() throws RemoteException;

	void close(final StageID id) throws RemoteException;

	void close() throws RemoteException;

	void setSelectedNode(final StageID id, SVNode value) throws RemoteException;

	void removeSelectedNode(final StageID id) throws RemoteException;

	void setDetail(StageID id, DetailPaneType detailType, int detailID, String value) throws RemoteException;

	void animationsEnabled(final StageID id, boolean enabled) throws RemoteException;

	void updateAnimations(final StageID id) throws RemoteException;

	void pauseAnimation(StageID id, int animationID) throws RemoteException;

}
