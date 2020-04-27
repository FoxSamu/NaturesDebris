/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package net.redgalaxy.expression;

public class ExpressionSyntaxException extends Exception {
    private final String input;
    private final int start;
    private final int end;
    private final String problem;

    private final String message;

    public ExpressionSyntaxException( String input, int start, int end, String problem ) {
        this.input = input;
        this.start = start;
        this.end = end;
        this.problem = problem;

        StringBuilder builder = new StringBuilder( "\n" ).append( input ).append( '\n' );
        for( int i = 0; i < start; i++ ) builder.append( ' ' );
        builder.append( '^' );
        for( int i = start + 1; i < end; i++ ) {
            builder.append( '~' );
        }
        builder.append( '\n' );
        builder.append( problem );
        message = builder.toString();
    }

    public ExpressionSyntaxException( String input, int start, int end, String problem, Throwable cause ) {
        super( cause );
        this.input = input;
        this.start = start;
        this.end = end;
        this.problem = problem;

        StringBuilder builder = new StringBuilder( "\n" ).append( input ).append( '\n' );
        for( int i = 0; i < start; i++ ) builder.append( ' ' );
        builder.append( '^' );
        for( int i = start + 1; i < end; i++ ) {
            builder.append( '~' );
        }
        builder.append( '\n' );
        builder.append( problem );
        message = builder.toString();
    }

    public String getInput() {
        return input;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
