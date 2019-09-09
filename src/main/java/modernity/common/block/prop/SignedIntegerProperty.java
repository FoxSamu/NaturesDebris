/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 11 - 2019
 */

package modernity.common.block.prop;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.minecraft.state.AbstractProperty;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public class SignedIntegerProperty extends AbstractProperty<Integer> {
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

    public Collection<Integer> getAllowedValues() {
        return this.allowedValues;
    }

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

    public int computeHashCode() {
        return 31 * super.computeHashCode() + allowedValues.hashCode();
    }

    public static SignedIntegerProperty create( String name, int min, int max ) {
        return new SignedIntegerProperty( name, min, max );
    }

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

    /**
     * Get the name for the given value.
     */
    public String getName( Integer value ) {
        return value < 0 ? "n" + - value : "p" + value;
    }
}