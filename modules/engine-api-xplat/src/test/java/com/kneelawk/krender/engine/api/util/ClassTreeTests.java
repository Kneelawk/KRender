package com.kneelawk.krender.engine.api.util;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClassTreeTests {
    @Test
    void testSimple() {
        ClassTree<String> tree = new ClassTree<>();

        tree.add(CharSequence.class, "hello");

        assertEquals(Set.of("hello"), tree.get(String.class));
    }

    @Test
    void testTwoClasses() {
        ClassTree<String> tree = new ClassTree<>();

        tree.add(Object.class, "foo");
        tree.add(CharSequence.class, "bar");

        assertEquals(Set.of("foo", "bar"), tree.get(String.class));
    }

    @Test
    void testSelfClass() {
        ClassTree<String> tree = new ClassTree<>();

        tree.add(Object.class, "foo");
        tree.add(CharSequence.class, "bar");
        tree.add(String.class, "baz");

        assertEquals(Set.of("foo", "bar", "baz"), tree.get(String.class));
    }

    @Test
    void testOtherClass() {
        ClassTree<String> tree = new ClassTree<>();

        tree.add(Object.class, "foo");
        tree.add(CharSequence.class, "bar");
        tree.add(String.class, "baz");

        assertEquals(Set.of("foo", "bar", "baz"), tree.get(String.class));
        assertEquals(Set.of("bar"), tree.get(CharSequence.class));
    }

    @Test
    void testAddAfter() {
        ClassTree<String> tree = new ClassTree<>();

        tree.add(CharSequence.class, "foo");

        assertEquals(Set.of("foo"), tree.get(String.class));

        tree.add(Object.class, "bar");

        assertEquals(Set.of("foo", "bar"), tree.get(String.class));
    }

    @Test
    void testDiamond() {
        ClassTree<String> tree = new ClassTree<>();

        tree.add(SortedSet.class, "foo");
        tree.add(CharSequence.class, "bar");

        assertEquals(Set.of("foo"), tree.get(TreeSet.class));
    }
}
