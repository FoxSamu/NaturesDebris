/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.command.node;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.RedirectModifier;
import com.mojang.brigadier.SingleRedirectModifier;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.util.ResourceLocation;

import java.util.function.Predicate;

public class ResourceLiteralArgumentBuilder<S> extends LiteralArgumentBuilder<S> {
    private final ResourceLocation literal;

    public ResourceLiteralArgumentBuilder(ResourceLocation literal) {
        super(literal.toString());
        this.literal = literal;
    }

    public static <S> ResourceLiteralArgumentBuilder<S> literal(ResourceLocation literal) {
        return new ResourceLiteralArgumentBuilder<>(literal);
    }

    public static <S> ResourceLiteralArgumentBuilder<S> literal(String res) {
        return new ResourceLiteralArgumentBuilder<>(new ResourceLocation(res));
    }

    @Override
    protected ResourceLiteralArgumentBuilder<S> getThis() {
        return this;
    }

    public ResourceLocation getResourceLiteral() {
        return literal;
    }

    @Override
    public ResourceLiteralCommandNode<S> build() {
        ResourceLiteralCommandNode<S> result = new ResourceLiteralCommandNode<>(getResourceLiteral(), getCommand(), getRequirement(), getRedirect(), getRedirectModifier(), isFork());

        for(CommandNode<S> argument : getArguments()) {
            result.addChild(argument);
        }

        return result;
    }

    @Override
    public ResourceLiteralArgumentBuilder<S> then(CommandNode<S> argument) {
        return (ResourceLiteralArgumentBuilder<S>) super.then(argument);
    }

    @Override
    public ResourceLiteralArgumentBuilder<S> then(ArgumentBuilder<S, ?> argument) {
        return (ResourceLiteralArgumentBuilder<S>) super.then(argument);
    }

    @Override
    public ResourceLiteralArgumentBuilder<S> executes(Command<S> command) {
        return (ResourceLiteralArgumentBuilder<S>) super.executes(command);
    }

    @Override
    public ResourceLiteralArgumentBuilder<S> requires(Predicate<S> requirement) {
        return (ResourceLiteralArgumentBuilder<S>) super.requires(requirement);
    }

    @Override
    public ResourceLiteralArgumentBuilder<S> redirect(CommandNode<S> target) {
        return (ResourceLiteralArgumentBuilder<S>) super.redirect(target);
    }

    @Override
    public ResourceLiteralArgumentBuilder<S> redirect(CommandNode<S> target, SingleRedirectModifier<S> modifier) {
        return (ResourceLiteralArgumentBuilder<S>) super.redirect(target, modifier);
    }

    @Override
    public ResourceLiteralArgumentBuilder<S> fork(CommandNode<S> target, RedirectModifier<S> modifier) {
        return (ResourceLiteralArgumentBuilder<S>) super.fork(target, modifier);
    }

    @Override
    public ResourceLiteralArgumentBuilder<S> forward(CommandNode<S> target, RedirectModifier<S> modifier, boolean fork) {
        return (ResourceLiteralArgumentBuilder<S>) super.forward(target, modifier, fork);
    }
}
