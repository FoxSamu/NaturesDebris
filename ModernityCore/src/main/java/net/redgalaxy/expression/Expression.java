/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 03 - 2020
 * Author: rgsw
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
