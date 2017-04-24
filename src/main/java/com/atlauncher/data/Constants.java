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
package com.atlauncher.data;

public class Constants {
    public static final LauncherVersion VERSION = new LauncherVersion(3, 2, 3, 7);
    public static final String LAUNCHER_NAME = "HCLauncher";
    public static final String API_BASE_URL = "https://api.yauncle.net/v1/launcher/";
    public static final String PASTE_CHECK_URL = "http://paste.pellens.one";
    public static final String PASTE_API_URL = "http://paste.pellens.one/api/create";
    public static final Server[] SERVERS = new Server[]{
            new Server("Auto", "files.minecraft.yauncle.net/hoshicraft/atl", true, false)
    };
}