package test;


import com.google.j2cl.junit.apt.J2clTestInput;
import org.junit.Assert;
import org.junit.Test;

@J2clTestInput(JunitTest.class)
public class JunitTest {

    // TODO verify Class.cast is emulated
    @Test
    public void testAssertEquals() {
        Assert.assertEquals(
                1,
                1
        );
    }
}
