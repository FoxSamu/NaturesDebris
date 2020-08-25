/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.generic.util;

import com.mojang.brigadier.Command;
import org.apache.logging.log4j.Logger;

import java.io.PrintStream;
import java.util.function.Consumer;

public final class Functions {
    private Functions() {
    }

    public static <T> Command<T> tryCatch(Command<T> tryCmd, Consumer<Throwable> catchConsumer) {
        return ctx -> {
            try {
                return tryCmd.run(ctx);
            } catch (Throwable t) {
                catchConsumer.accept(t);
                return 1;
            }
        };
    }

    public static <T> Command<T> tryOrLog(Command<T> tryCmd, Logger logger) {
        return tryCatch(tryCmd, e -> logger.error("Caught exception", e));
    }

    public static <T> Command<T> tryOrPrint(Command<T> tryCmd, PrintStream ps) {
        return tryCatch(tryCmd, e -> e.printStackTrace(ps));
    }
}
