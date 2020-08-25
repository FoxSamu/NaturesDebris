/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modul.modules;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import modul.ModulLogger;
import modul.core.MListFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class ModuleContext {
    private final String contextID;
    private final Set<ModuleInstance> modules = Sets.newHashSet();
    private final List<ModuleException> errors = Lists.newArrayList();
    private Supplier<MListFile.Context> contextSupplier = MListFile::context;

    private ModulLogger logger = ModulLogger.NONE;
    private boolean loaded;

    public ModuleContext(String contextID) {
        this.contextID = contextID;
    }

    public ModuleContext load() {
        if (loaded) return this;

        try {
            loadModules(loadFile());
        } catch (ModuleException exc) {
            errors.add(exc);
        }
        logLoadMsg();

        loaded = true;
        return this;
    }

    private Set<String> loadFile() throws ModuleException {
        Set<String> mlist = Sets.newHashSet();
        try {
            Enumeration<URL> resources = getClass().getClassLoader().getResources("META-INF/modul/" + contextID + ".mlist");

            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                InputStream stream = resource.openStream();

                try {
                    MListFile.load(contextSupplier.get(), stream).forEach(mlist::add);
                } catch (IOException exc) {
                    throw new ModuleException("Unable to load resource: " + resource.toExternalForm() + "\n - " + exc.getMessage());
                } catch (Exception exc) {
                    throw new ModuleException(String.format("Malformed MList format: %s (file: '%s')", exc.getMessage(), resource.toExternalForm()));
                }
            }
        } catch (IOException exception) {
            throw new ModuleException("Could not load context '" + contextID + "' because of IO problems\n - " + exception.getMessage());
        }
        return mlist;
    }

    private void loadModules(Set<String> mlist) {
        for (String entry : mlist) {
            try {
                logger.debug("Loading module in context '" + contextID + "': " + entry);
                loadModule(entry);
            } catch (ModuleException exc) {
                errors.add(exc);
            }
        }
    }

    private void loadModule(String className) throws ModuleException {
        try {
            Class<?> cls = Class.forName(className);

            Module module = cls.getAnnotation(Module.class);

            if (module == null) {
                throw new ModuleException("Not a module: '" + className + "'");
            }

            Object obj = cls.newInstance();

            ModuleInstance instance = new ModuleInstance(module, obj);
            modules.add(instance);
        } catch (ClassNotFoundException e) {
            throw new ModuleException("No such class: '" + className + "'");
        } catch (IllegalAccessException e) {
            throw new ModuleException("No-args constructor for class " + className + " does not exist or is not public");
        } catch (InstantiationException e) {
            // We do not handle this exception: this is a failure in construction and loading must stop
            throw new RuntimeException("Module " + className + " thew an exception during instantiation", e.getCause());
        }
    }

    private void logLoadMsg() {
        logger.error("Context '" + contextID + "':");

        for (ModuleException exc : errors) {
            logger.error(" - Error: " + exc.getMessage());
        }
        logger.info(" - " + modules.size() + " modules loaded successfully");
    }

    public ModuleContext logger(ModulLogger logger) {
        this.logger = logger;
        return this;
    }

    public ModuleContext contextFactory(Supplier<MListFile.Context> factory) {
        contextSupplier = factory;
        return this;
    }
}
