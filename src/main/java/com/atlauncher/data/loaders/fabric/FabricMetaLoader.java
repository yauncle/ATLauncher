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
package com.atlauncher.data.loaders.fabric;

public class FabricMetaLoader {
    private String seperator;
    private int build;
    private String maven;
    private String version;
    private boolean stable;

    public String getSeperator() {
        return this.seperator;
    }

    public int getBuild() {
        return this.build;
    }

    public String getMaven() {
        return this.maven;
    }

    public String getVersion() {
        return this.version;
    }

    public boolean isStable() {
        return this.stable;
    }
}
