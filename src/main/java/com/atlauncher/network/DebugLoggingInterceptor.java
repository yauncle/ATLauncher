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
package com.atlauncher.network;

import java.io.IOException;

import com.atlauncher.LogManager;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public final class DebugLoggingInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        LogManager.debug(String.format("Sending request %s", request.url()), 3);
        LogManager.debug(request.toString(), 5);

        long t1 = System.nanoTime();
        Response response = chain.proceed(request);
        long t2 = System.nanoTime();

        if (response.cacheResponse() != null && response.networkResponse() != null) {
            LogManager.debug(String.format("Received cached response for %s in %.1fms", response.request().url(),
                    (t2 - t1) / 1e6d), 3);
        } else {
            LogManager.debug(
                    String.format("Received response for %s in %.1fms", response.request().url(), (t2 - t1) / 1e6d), 3);
        }

        LogManager.debug(response.toString(), 5);

        return response;
    }
}
