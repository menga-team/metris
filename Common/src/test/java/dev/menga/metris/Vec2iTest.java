package dev.menga.metris;

import dev.menga.metris.utils.Vec2i;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Vec2iTest {

    @Test
    void testConstructorAndGetters() {
        Vec2i v = new Vec2i(3, 4);
        assertEquals(3, v.getX());
        assertEquals(4, v.getY());
    }

    @Test
    void testCopyConstructor() {
        Vec2i v1 = new Vec2i(1, 2);
        Vec2i v2 = new Vec2i(v1);
        assertEquals(1, v2.getX());
        assertEquals(2, v2.getY());
        assertNotSame(v1, v2);
    }

    @Test
    void testOfFactory() {
        Vec2i v = Vec2i.of(5, 6);
        assertEquals(5, v.getX());
        assertEquals(6, v.getY());
    }

    @Test
    void testAddMutVec2i() {
        Vec2i v = new Vec2i(1, 2);
        v.addMut(new Vec2i(3, 4));
        assertEquals(4, v.getX());
        assertEquals(6, v.getY());
    }

    @Test
    void testAddMutInts() {
        Vec2i v = new Vec2i(1, 2);
        v.addMut(5, 7);
        assertEquals(6, v.getX());
        assertEquals(9, v.getY());
    }

    @Test
    void testAddVec2i() {
        Vec2i v1 = new Vec2i(2, 3);
        Vec2i v2 = new Vec2i(4, 5);
        Vec2i v3 = v1.add(v2);
        assertEquals(6, v3.getX());
        assertEquals(8, v3.getY());
        assertNotSame(v1, v3);
    }

    @Test
    void testAddInts() {
        Vec2i v = new Vec2i(2, 3);
        Vec2i result = v.add(7, 8);
        assertEquals(9, result.getX());
        assertEquals(11, result.getY());
    }

    @Test
    void testSubMutVec2i() {
        Vec2i v = new Vec2i(10, 5);
        v.subMut(new Vec2i(3, 2));
        assertEquals(7, v.getX());
        assertEquals(3, v.getY());
    }

    @Test
    void testSubVec2i() {
        Vec2i v1 = new Vec2i(8, 6);
        Vec2i v2 = new Vec2i(3, 2);
        Vec2i v3 = v1.sub(v2);
        assertEquals(5, v3.getX());
        assertEquals(4, v3.getY());
    }

    @Test
    void testSubInts() {
        Vec2i v = new Vec2i(7, 9);
        Vec2i result = v.sub(2, 4);
        assertEquals(5, result.getX());
        assertEquals(5, result.getY());
    }

    @Test
    void testScaleMut() {
        Vec2i v = new Vec2i(2, 3);
        v.scaleMut(4);
        assertEquals(8, v.getX());
        assertEquals(12, v.getY());
    }

    @Test
    void testScale() {
        Vec2i v = new Vec2i(3, 5);
        Vec2i result = v.scale(2);
        assertEquals(6, result.getX());
        assertEquals(10, result.getY());
    }

    @Test
    void testToString() {
        Vec2i v = new Vec2i(7, 8);
        assertEquals("X: 7 Y: 8", v.toString());
    }
}
