/*
 * ATLauncher - https://github.com/ATLauncher/ATLauncher
 * Copyright (C) 2013-2019 ATLauncher
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
package com.atlauncher;

import java.awt.Color;

import com.atlauncher.adapter.ColorTypeAdapter;
import com.atlauncher.data.PackVersion;
import com.atlauncher.data.PackVersionTypeAdapter;
import com.atlauncher.data.minecraft.Arguments;
import com.atlauncher.data.minecraft.ArgumentsTypeAdapter;
import com.atlauncher.data.minecraft.Library;
import com.atlauncher.data.minecraft.LibraryTypeAdapter;
import com.atlauncher.data.minecraft.MojangStatus;
import com.atlauncher.data.minecraft.MojangStatusTypeAdapter;
import com.atlauncher.data.minecraft.loaders.fabric.FabricMetaLauncherMeta;
import com.atlauncher.data.minecraft.loaders.fabric.FabricMetaLauncherMetaTypeAdapter;
import com.atlauncher.data.minecraft.loaders.forge.ForgeLibrary;
import com.atlauncher.data.minecraft.loaders.forge.ForgeLibraryTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class Gsons {
    public static final Gson DEFAULT = new GsonBuilder().setPrettyPrinting().create();

    public static final Gson THEMES = new GsonBuilder().setPrettyPrinting()
            .registerTypeAdapter(Color.class, new ColorTypeAdapter()).create();

    public static final Gson DEFAULT_ALT = new GsonBuilder()
            .registerTypeAdapter(PackVersion.class, new PackVersionTypeAdapter()).create();

    public static final Gson MINECRAFT = new GsonBuilder().disableHtmlEscaping()
            .registerTypeAdapter(Library.class, new LibraryTypeAdapter())
            .registerTypeAdapter(Arguments.class, new ArgumentsTypeAdapter())
            .registerTypeAdapter(FabricMetaLauncherMeta.class, new FabricMetaLauncherMetaTypeAdapter())
            .registerTypeAdapter(ForgeLibrary.class, new ForgeLibraryTypeAdapter())
            .registerTypeAdapter(MojangStatus.class, new MojangStatusTypeAdapter()).create();
}
