/*
 * Copyright 2014 - 2016 Michael Rapp
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package de.mrapp.android.preference.multithreading;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.View;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static de.mrapp.android.util.Condition.ensureNotNull;

/**
 * An abstract base class for all loaders, which allow to asynchronously load data and show it in a
 * view afterwards.
 *
 * @param <DataType>
 *         The type of the data, which should be loaded
 * @param <KeyType>
 *         The type of the keys, which allow to identify the data
 * @param <ViewType>
 *         The type of the views, which are able to show the data
 * @param <ParamType>
 *         The type of parameters, which can optionally be passed to the loader
 * @author Michael Rapp
 * @since 1.7.0
 */
public abstract class AbstractDataLoader<DataType, KeyType, ViewType extends View, ParamType> {

    /**
     * The context, which is used by the data loader.
     */
    private final Context context;

    /**
     * A map, which is used to cache already loaded data.
     */
    private final Map<KeyType, SoftReference<DataType>> cache;

    /**
     * A map, which is used to manage the views, which have already been used to show data.
     */
    private final Map<ViewType, KeyType> views;

    /**
     * The thread pool, which is used to manage the threads, which are used to asynchronously load
     * data.
     */
    private final ExecutorService threadPool;

    /**
     * Returns the data, which corresponds to a specific key, from the cache.
     *
     * @param key
     *         The key of the data, which should be retrieved, as an instance of the generic type
     *         KeyType. The key may not be null
     * @return The data, which corresponds to the given key, as an instance of the generic type
     * DataType, or null, if no data with the given key is contained by the cache
     */
    private DataType getCachedData(@NonNull final KeyType key) {
        SoftReference<DataType> reference = cache.get(key);

        if (reference != null) {
            return reference.get();
        }

        return null;
    }

    /**
     * Loads the data, which corresponds to a specific key, asynchronously and shows it afterwards.
     *
     * @param key
     *         The key of the data, which should be loaded, as an instance of the generic type
     *         KeyType. The key may not be null
     * @param view
     *         The view, which should be used to show the data after it has been loaded, as an
     *         instance of the generic type ViewType. The view may not be null
     * @param params
     *         An array, which may contain optional parameters, as an array of the generic type
     *         ParamType
     */
    @SuppressWarnings("unchecked")
    @SuppressLint("HandlerLeak")
    private void loadDataAsynchronously(@NonNull final KeyType key, @NonNull final ViewType view,
                                        final ParamType... params) {
        final Handler handler = new Handler() {

            @Override
            public void handleMessage(final Message msg) {
                KeyType tag = views.get(view);

                if (tag != null && tag.equals(key)) {
                    showData(view, (DataType) msg.obj);
                }
            }

        };

        threadPool.submit(new Runnable() {

            @Override
            public void run() {
                DataType data = loadData(key, params);
                Message message = Message.obtain();
                message.obj = data;
                handler.sendMessage(message);
            }

        });
    }

    /**
     * Returns the context, which is used by the data loader.
     *
     * @return The context, which is used by the data loader, as an instance of the class {@link
     * Context}
     */
    protected final Context getContext() {
        return context;
    }

    /**
     * The method, which is invoked on implementing subclasses, in order to load the data, which
     * corresponds to a specific key.
     *
     * @param key
     *         The key of the data, which should be loaded, as an instance of the generic type
     *         KeyType. The key may not be null
     * @param params
     *         An array, which may contain optional parameters, as an array of the generic type
     *         ParamType
     * @return The data, which has been loaded, as an instance of the generic type DataType
     */
    @SuppressWarnings("unchecked")
    protected abstract DataType loadData(@NonNull final KeyType key, final ParamType... params);

    /**
     * The method, which is invoked on implementing subclasses, in order to show data after it has
     * been loaded.
     *
     * @param view
     *         The view, which should be used to show the data after it has been loaded, as an
     *         instance of the generic type ViewType. The view may not be null
     * @param data
     *         The data, which should be shown, as an instance of the generic type DataType. The
     *         data may not be null
     */
    protected abstract void showData(@NonNull final ViewType view, @NonNull final DataType data);

    /**
     * Creates a new data loader, which allows to asynchronously load data and show it in a view
     * afterwards.
     *
     * @param context
     *         The context, which should be used by the data loader, as an instance of the class
     *         {@link Context}. The context may not be null
     */
    public AbstractDataLoader(@NonNull final Context context) {
        ensureNotNull(context, "The context may not be null");
        this.context = context;
        this.cache = new HashMap<>();
        this.views = Collections.synchronizedMap(new WeakHashMap<ViewType, KeyType>());
        this.threadPool = Executors.newCachedThreadPool();
    }

    /**
     * Loads the the data, which corresponds to a specific key, and shows it in a specific view
     * afterwards. If the data has already been loaded, it will be retrieved from the cache in order
     * to enhance the performance.
     *
     * @param key
     *         The key of the data, which should be loaded, as an instance of the generic type
     *         KeyType. The key may not be null
     * @param view
     *         The view, which should be used to show the data after it has been loaded, as an
     *         instance of the generic type ViewType. The view may not be null
     * @param params
     *         An array, which may contain optional parameters, as an array of the generic type
     *         ParamType
     */
    @SafeVarargs
    public final void load(@NonNull final KeyType key, @NonNull final ViewType view,
                           final ParamType... params) {
        ensureNotNull(key, "The key may not be null");
        ensureNotNull(view, "The view may not be null");

        views.put(view, key);
        DataType data = getCachedData(key);

        if (data != null) {
            showData(view, data);
        } else {
            loadDataAsynchronously(key, view, params);
        }
    }

}