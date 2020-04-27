/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package net.redgalaxy.expression;

public interface Expression {
    double evaluate();
    double getVariable( int index );
    double getVariable( String name );
    void setVariable( int index, double value );
    void setVariable( String name, double value );
    int indexOfVariable( String name );
}
