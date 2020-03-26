/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 14 - 2020
 * Author: rgsw
 */

package modernity.common.block.prop;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.state.Property;

public class IntEnumProperty extends Property<Integer> {
    private final Map<Integer, String> valueToName;
    private final Map<String, Integer> nameToValue;
    private final List<Integer> values;

    protected IntEnumProperty( String name, Map<Integer, String> valueToName, Map<String, Integer> nameToValue, List<Integer> values ) {
        super( name, Integer.class );
        this.valueToName = valueToName;
        this.nameToValue = nameToValue;
        this.values = values;
    }

    @Override
    public Collection<Integer> getAllowedValues() {
        return values;
    }

    @Override
    public Optional<Integer> parseValue( String s ) {
        if( nameToValue.containsKey( s ) ) {
            return Optional.of( nameToValue.get( s ) );
        }
        return Optional.empty();
    }

    @Override
    public String getName( Integer integer ) {
        return valueToName.get( integer );
    }

    public static Builder builder( String name ) {
        return new Builder( name );
    }

    public static class Builder {
        private final String name;
        private final HashMap<Integer, String> valueToName = new HashMap<>();
        private final HashMap<String, Integer> nameToValue = new HashMap<>();
        private final ArrayList<Integer> values = new ArrayList<>();

        private Builder( String name ) {
            this.name = name;
        }

        public Builder with( int value, String name ) {
            if( name == null ) throw new NullPointerException();
            valueToName.put( value, name );
            nameToValue.put( name, value );
            if( ! values.contains( value ) ) {
                values.add( value );
            }
            return this;
        }

        public IntEnumProperty create() {
            return new IntEnumProperty(
                name,
                ImmutableMap.copyOf( valueToName ),
                ImmutableMap.copyOf( nameToValue ),
                ImmutableList.copyOf( values )
            );
        }
    }
}
