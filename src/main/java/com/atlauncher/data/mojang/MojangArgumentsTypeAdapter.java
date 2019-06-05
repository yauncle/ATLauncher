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

import com.atlauncher.Gsons;
import com.atlauncher.data.mojang.ArgumentRule;
import com.atlauncher.data.mojang.MojangArguments;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MojangArgumentsTypeAdapter implements JsonDeserializer<MojangArguments> {
    @Override
    public MojangArguments deserialize(JsonElement json, Type type, JsonDeserializationContext context)
            throws JsonParseException {
        List<ArgumentRule> game = new ArrayList<ArgumentRule>();
        List<ArgumentRule> jvm = new ArrayList<ArgumentRule>();

        final JsonObject rootJsonObject = json.getAsJsonObject();
        final JsonArray gameArray = rootJsonObject.getAsJsonArray("game");
        final JsonArray jvmArray = rootJsonObject.getAsJsonArray("jvm");

        for (JsonElement gameArg : gameArray) {
            if (gameArg.isJsonObject()) {
                JsonObject argument = gameArg.getAsJsonObject();
                game.add(Gsons.DEFAULT_ALT.fromJson(argument, ArgumentRule.class));
            } else {
                String argument = gameArg.getAsString();
                game.add(new ArgumentRule(null, argument));
            }
        }

        for (JsonElement gameArg : jvmArray) {
            if (gameArg.isJsonObject()) {
                JsonObject argument = gameArg.getAsJsonObject();
                jvm.add(Gsons.DEFAULT_ALT.fromJson(argument, ArgumentRule.class));
            } else {
                String argument = gameArg.getAsString();
                jvm.add(new ArgumentRule(null, argument));
            }
        }

        return new MojangArguments(game, jvm);
    }
}
