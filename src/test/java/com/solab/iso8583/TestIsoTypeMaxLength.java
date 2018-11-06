package com.solab.iso8583;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class TestIsoTypeMaxLength {

    @Parameterized.Parameters(name = "type={0}, expected={1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
            {IsoType.LLVAR, "LLVAR can only hold values up to 99 chars"},
            {IsoType.LLLVAR, "LLLVAR can only hold values up to 999 chars"},
            {IsoType.LLLLVAR, "LLLLVAR can only hold values up to 9999 chars"},
            {IsoType.LLBIN, "LLBIN can only hold values up to 99 chars"},
            {IsoType.LLLBIN, "LLLBIN can only hold values up to 999 chars"},
            {IsoType.LLLLBIN, "LLLLBIN can only hold values up to 9999 chars"},
            {IsoType.LLBCDBIN, "LLBCDBIN can only hold values up to 50 chars"},
            {IsoType.LLLBCDBIN, "LLLBCDBIN can only hold values up to 500 chars"},
            {IsoType.LLLLBCDBIN, "LLLLBCDBIN can only hold values up to 5000 chars"},
        });
    }

    private final IsoType type;
    private final String expected;

    public TestIsoTypeMaxLength(final IsoType type, final String expected) {
        this.type = type;
        this.expected = expected;
    }

    @Test
    public void shouldThrowIllegalArgumentException() {
        // Given
        final IsoValue dummyIsoValue = new IsoValue(IsoType.ALPHA, "value", 5);

        try {
            // When
            dummyIsoValue.validateTypeWithVariableLength(type, 1000000);
        } catch (IllegalArgumentException e) {
            // Then
            Assert.assertEquals(expected, e.getMessage());
        }
    }

}
