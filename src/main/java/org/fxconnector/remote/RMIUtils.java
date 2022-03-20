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

import org.scenicview.utils.Logger;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

class RMIUtils {

	private static final AtomicInteger rmiPort = new AtomicInteger(7557);
	private static final String REMOTE_CONNECTOR = "RemoteConnector";
	private static final String REMOTE_AGENT = "AgentServer";
	static Registry localRegistry;

	private RMIUtils() {
	}

	private static RemoteConnector findScenicView(final String serverAdress, final int serverPort) throws Exception {
		final Registry registry = LocateRegistry.getRegistry(serverAdress, serverPort);
		// look up the remote object
		return (RemoteConnector) (registry.lookup(REMOTE_CONNECTOR));
	}

	private static RemoteApplication findApplication(final String serverAdress, final int serverPort) throws Exception {
		final Registry registry = LocateRegistry.getRegistry(serverAdress, serverPort);
		// look up the remote object
		return (RemoteApplication) (registry.lookup(REMOTE_AGENT));
	}

	public static void bindScenicView(final RemoteConnector view, final int port) throws RemoteException {
		// create the registry and bind the name and object.
		final Registry registry = LocateRegistry.createRegistry(port);
		registry.rebind(REMOTE_CONNECTOR, view);
	}

	public static void bindApplication(final RemoteApplication application, final int port) throws RemoteException {
		// create the registry and bind the name and object.
		localRegistry = LocateRegistry.createRegistry(port);
		localRegistry.rebind(REMOTE_AGENT, application);
	}

	public static void unbindApplication(final int port) throws RemoteException {
		try {
			localRegistry.unbind(REMOTE_AGENT);
			UnicastRemoteObject.unexportObject(localRegistry, true);
		} catch (NotBoundException e) {
			// we don't care
		}
	}

	public static void unbindScenicView(final int port) throws RemoteException, NotBoundException {
		// create the registry and bind the name and object.
		final Registry registry = LocateRegistry.getRegistry(port);
		registry.unbind(REMOTE_CONNECTOR);
	}

	public static void findScenicView(final int port, final Consumer<RemoteConnector> consumer) {
		new Thread(REMOTE_CONNECTOR + ".Finder") {
			@Override
			public void run() {
				RemoteConnector scenicView = null;

				while (scenicView == null) {
					try {
						Logger.print("Finding " + REMOTE_CONNECTOR + " connection for agent...");
						scenicView = findScenicView("127.0.0.1", port);
						if (scenicView == null) {
							sleep(50);
						}
					} catch (final Exception ignored) {

					}
				}
				consumer.accept(scenicView);
			}
		}.start();
	}

	public static void findApplication(final int port, final Consumer<RemoteApplication> consumer) {
		final Thread remoteBrowserFinder = new Thread(REMOTE_AGENT + ".Finder") {
			@Override
			public void run() {
				RemoteApplication application = null;

				while (application == null) {
					try {
						application = findApplication("127.0.0.1", port);
						if (application != null) {
							sleep(50);
						}
					} catch (final Exception ignored) {

					}
				}
				consumer.accept(application);
			}
		};
		remoteBrowserFinder.start();
	}

	public static int getClientPort() {
		return rmiPort.incrementAndGet();
	}

}
