package test;

import android.graphics.Bitmap;
import android.os.Bundle;

import org.junit.Before;
import org.junit.Test;
import org.junit.*;

import ch.deletescape.lawnchair.TestActivity;

import static org.junit.Assert.*;

public class TestActivityTest {
    private TestActivity test;
    @Before
    public void setUp() throws Exception {
        test = new TestActivity();
    }

    //@Test
    public void takePhoto() {
        Bundle bundle=new Bundle();
        test.takePhotoFile();
        test.set_wallpaper();
        assertEquals(true,test.isSetWallpaper);
    }

    @Test
    public void setBack() {
        assertEquals(false,test.setPhotoPath());
    }
}