/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.server.util;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class BinaryUtils {
    public static String decode(ByteBuffer buf) {
        int start = buf.position(), index = buf.position();
        while(index + 1 < buf.limit() && buf.get() != 0) index++;

        byte[] slice = Arrays.copyOfRange(buf.array(), start, index);
        return new String(slice, StandardCharsets.UTF_8);
    }

    public static byte[] encode(String string) {
        return string.getBytes(StandardCharsets.UTF_8);
    }
}
