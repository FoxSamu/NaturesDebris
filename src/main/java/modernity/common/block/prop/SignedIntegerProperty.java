/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.common.block.prop;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.minecraft.state.Property;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

/**
 * A state property that allows signed integers (negative and positive values)
 */
public class SignedIntegerProperty extends Property<Integer> {
    private final ImmutableSet<Integer> allowedValues;

    protected SignedIntegerProperty( String name, int min, int max ) {
        super( name, Integer.class );
        if( max <= min ) {
            throw new IllegalArgumentException( "Max value of " + name + " must be greater than min (" + min + ")" );
        } else {
            Set<Integer> allowed = Sets.newHashSet();

            for( int i = min; i <= max; ++ i ) {
                allowed.add( i );
            }

            allowedValues = ImmutableSet.copyOf( allowed );
        }
    }

    @Override
    public Collection<Integer> getAllowedValues() {
        return this.allowedValues;
    }

    @Override
    public boolean equals( Object o ) {
        if( this == o ) {
            return true;
        } else if( o instanceof SignedIntegerProperty && super.equals( o ) ) {
            SignedIntegerProperty prop = (SignedIntegerProperty) o;
            return allowedValues.equals( prop.allowedValues );
        } else {
            return false;
        }
    }

    @Override
    public int computeHashCode() {
        return 31 * super.computeHashCode() + allowedValues.hashCode();
    }

    /**
     * Creates a signed integer property
     * @param name The name
     * @param min  The minimum value
     * @param max  The maximum value
     *
     * @throws IllegalArgumentException When the minimum value is more than the maximum value
     */
    public static SignedIntegerProperty create( String name, int min, int max ) {
        return new SignedIntegerProperty( name, min, max );
    }

    @Override
    public Optional<Integer> parseValue( String value ) {
        try {
            int neg = value.charAt( 0 ) == 'n' ? - 1 : 1;
            int off = value.charAt( 0 ) == 'p' || neg < 0 ? 1 : 0;
            Integer val = Integer.valueOf( value.substring( off ) ) * neg;
            return allowedValues.contains( val ) ? Optional.of( val ) : Optional.empty();
        } catch( Exception exc ) {
            return Optional.empty();
        }
    }

    @Override
    public String getName( Integer value ) {
        return value < 0 ? "n" + - value : "p" + value;
    }
}