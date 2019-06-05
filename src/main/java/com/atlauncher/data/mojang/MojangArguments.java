/*
 * ATLauncher - https://github.com/ATLauncher/ATLauncher
 * Copyright (C) 2013 ATLauncher
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.atlauncher.data.mojang;

import java.util.List;
import com.atlauncher.annot.Json;

@Json
public class MojangArguments {
    private List<ArgumentRule> game;
    private List<ArgumentRule> jvm;

    MojangArguments(List<ArgumentRule> game, List<ArgumentRule> jvm) {
        this.game = game;
        this.jvm = jvm;
    }

    public List<ArgumentRule> getGame() {
        return this.game;
    }

    public List<ArgumentRule> getJVM() {
        return this.jvm;
    }

    public String asString() {
        String arguments = "";

        for (ArgumentRule rule : this.jvm) {
            if (rule.applies()) {
                arguments += " " + rule.getValue();
            }
        }

        for (ArgumentRule rule : this.game) {
            if (rule.applies()) {
                arguments += " " + rule.getValue();
            }
        }

        return arguments;
    }
}
