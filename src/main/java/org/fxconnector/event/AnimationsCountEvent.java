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
package org.fxconnector.event;

import org.fxconnector.SVAnimation;
import org.fxconnector.StageID;

import java.util.List;


public class AnimationsCountEvent extends FXConnectorEvent {

	/**
	 *
	 */
	private static final long serialVersionUID = 1245351064842340102L;
	List<SVAnimation> animations;

	public AnimationsCountEvent(final StageID id, final List<SVAnimation> animations) {
		super(SVEventType.ANIMATIONS_UPDATED, id);
		this.animations = animations;
	}

	public List<SVAnimation> getAnimations() {
		return animations;
	}

}
