/*
 * Copyright 2012-2016 Credit Suisse
 * Copyright 2018 Werner Keil, Otavio Santana, Trivadis AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package javax.money.spi;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class implements the (default) {@link ServiceProvider} interface and hereby uses the JDK
 * {@link java.util.ServiceLoader} to load the services required.
 *
 * @author Anatole Tresch
 */
class DefaultServiceProvider implements ServiceProvider {
    /** List of services loaded, per class. */
    private final ConcurrentHashMap<Class, List<Object>> servicesLoaded = new ConcurrentHashMap<>();

    @Override
    public int getPriority() {
        return 0;
    }

    /**
     * Loads and registers services.
     *
     * @param serviceType
     *            The service type.
     * @param <T>
     *            the concrete type.
     * @return the items found, never {@code null}.
     */
    @Override
    public <T> List<T> getServices(final Class<T> serviceType) {
        @SuppressWarnings("unchecked")
        List<T> found = (List<T>) servicesLoaded.get(serviceType);
        if (found != null) {
            return found;
        }

        return loadServices(serviceType);
    }

    @Override
    public <T> T getService(Class<T> serviceType) {
        List<T> servicesFound = getServices(serviceType);
        if(servicesFound.isEmpty()){
            return null;
        }
        return servicesFound.get(0);
    }

    /**
     * Loads and registers services.
     *
     * @param   serviceType  The service type.
     * @param   <T>          the concrete type.
     *
     * @return  the items found, never {@code null}.
     */
    private <T> List<T> loadServices(final Class<T> serviceType) {
        List<T> services = new ArrayList<>();
        try {
            for (T t : ServiceLoader.load(serviceType)) {
                services.add(t);
            }
            Collections.sort(services, new Comparator<T>() {
                @Override
                public int compare(T o1, T o2) {
                    return o1.getClass().getSimpleName().compareTo(o2.getClass().getSimpleName());
                }
            });
            @SuppressWarnings("unchecked")
            final List<T> previousServices = (List<T>) servicesLoaded.putIfAbsent(serviceType, (List<Object>) services);
            return Collections.unmodifiableList(previousServices != null ? previousServices : services);
        } catch (Exception e) {
            Logger.getLogger(DefaultServiceProvider.class.getName()).log(Level.WARNING,
                                                                         "Error loading services of type " + serviceType, e);
            return services;
        }
    }

}