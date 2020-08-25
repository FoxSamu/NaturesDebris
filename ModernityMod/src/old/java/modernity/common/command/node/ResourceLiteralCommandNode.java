/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.command.node;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.RedirectModifier;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.util.ResourceLocation;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public class ResourceLiteralCommandNode<S> extends LiteralCommandNode<S> {
    private final ResourceLocation literal;

    protected ResourceLiteralCommandNode(ResourceLocation literal, Command<S> command, Predicate<S> requirement, CommandNode<S> redirect, RedirectModifier<S> modifier, boolean forks) {
        super(literal.toString(), command, requirement, redirect, modifier, forks);
        this.literal = literal;
    }

    public ResourceLocation getResLiteral() {
        return literal;
    }

    @Override
    public boolean isValidInput(String input) {
        return parse(new StringReader(input)) > -1;
    }

    @Override
    public String getName() {
        return literal.toString();
    }

    @Override
    public String getUsageText() {
        return literal.toString();
    }

    @Override
    public void parse(StringReader reader, CommandContextBuilder<S> contextBuilder) throws CommandSyntaxException {
        int start = reader.getCursor();
        int end = parse(reader);
        if(end > -1) {
            contextBuilder.withNode(this, StringRange.between(start, end));
            return;
        }

        throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.literalIncorrect().createWithContext(reader, literal);
    }

    private int parse(StringReader reader) {
        int end = tryFind(reader, literal.toString());
        if(end == -1 && literal.getNamespace().equals("minecraft")) {
            end = tryFind(reader, literal.getPath());
        }
        return end;
    }

    private int tryFind(StringReader reader, String string) {
        int start = reader.getCursor();
        if(reader.canRead(string.length())) {
            int end = start + string.length();
            if(reader.getString().substring(start, end).equals(string)) {
                reader.setCursor(end);
                if(!reader.canRead() || reader.peek() == ' ') {
                    return end;
                } else {
                    reader.setCursor(start);
                }
            }
        }
        return -1;
    }

    @Override
    public CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if(literal.toString().toLowerCase().startsWith(builder.getRemaining().toLowerCase())) {
            return builder.suggest(literal.toString()).buildFuture();
        } else if(literal.getPath().toLowerCase().startsWith(builder.getRemaining().toLowerCase())) {
            return builder.suggest(literal.toString()).buildFuture();
        } else {
            return Suggestions.empty();
        }
    }

    @Override
    public ResourceLiteralArgumentBuilder<S> createBuilder() {
        ResourceLiteralArgumentBuilder<S> builder = ResourceLiteralArgumentBuilder.literal(literal);
        builder.requires(getRequirement());
        builder.forward(getRedirect(), getRedirectModifier(), isFork());
        if(getCommand() != null) {
            builder.executes(getCommand());
        }
        return builder;
    }

    @Override
    protected String getSortedKey() {
        return literal.toString();
    }

    @Override
    public Collection<String> getExamples() {
        return literal.getNamespace().equals("minecraft")
               ? ImmutableList.of(literal.toString(), literal.getPath())
               : Collections.singleton(literal.toString());
    }

    @Override
    public String toString() {
        return "<res_literal " + literal + ">";
    }

    @Override
    public int hashCode() {
        int result = literal.hashCode();
        result = 31 * result + super.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof ResourceLiteralCommandNode)) return false;

        ResourceLiteralCommandNode that = (ResourceLiteralCommandNode) o;

        if(!literal.equals(that.literal)) return false;
        return super.equals(o);
    }
}
