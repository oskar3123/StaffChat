package me.oskar3123.staffchat.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class StringUtilsTest
{

    @Test
    public void sanitize()
    {
        assertEquals("message", "hejsan \\$ \\\\ xd", StringUtils.sanitize("hejsan $ \\ xd"));
    }

}