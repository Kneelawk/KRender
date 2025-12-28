package com.kneelawk.krender.engine.api.util.bits;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IntBitsTests {
    @Test
    void simpleReadWrite() {
        IntBits bits = IntBits.of(4);

        int a = 0;
        a = bits.setI(a, 12);

        assertEquals(12, bits.getI(a));
    }

    @Test
    void offsetReadWrite() {
        BufferBits buffer = BufferBits.ofFixedI(0, 7);
        IntBits bits = IntBits.ofI(buffer, 4);

        int a = 0;
        a = bits.setI(a, 13);

        assertEquals(13, bits.getI(a));
    }

    @Test
    void splitReadWrite() {
        BufferBits buffer = BufferBits.ofFixedI(0, 29);
        IntBits bits = IntBits.ofI(buffer, 5);

        int a = 0, b = 0;
        a = bits.setI(a, 23);
        b = bits.setIHigh(b, 23);

        assertEquals(23, bits.getI(a, b));
    }

    @Test
    void arrayReadWrite() {
        BufferBits buffer = BufferBits.ofFixedI(0, 61);
        IntBits bits = IntBits.ofI(buffer, 5);

        int[] a = new int[3];
        bits.setI(a, 0, 25);

        assertEquals(25, bits.getI(a, 0));
    }
}
