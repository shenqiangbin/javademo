package MyStr;

import org.junit.Assert;
import org.junit.Test;


public class TestDemo {

    @Test
    public void test1() {
        String txt = "题：①当𝑛≤𝑚时，先手";
        String s = StringHasHighChar.convertSurrogatePairsToBMP(txt);
        System.out.println(s);
        Assert.assertEquals("题：①当ฆ≤ฆ时，先手", s);
        Assert.assertEquals(11, s.length());
    }

    @Test
    public void test_sub() {
        String txt = "题：①当𝑛≤𝑚时，先手";
        int startIndex = 0;
        int endIndex = 2;
        String sub = txt.substring(startIndex, endIndex);
        String s = StringHasHighChar.subStringForHasHighChar(txt, startIndex, endIndex);

        Assert.assertEquals(sub, s);
    }

    @Test
    public void test_sub_2() {
        String txt = "题：①当𝑛≤𝑚时，先手";
        int startIndex = 0;
        int endIndex = 6;
        String sub = txt.substring(startIndex, endIndex);
        String s = StringHasHighChar.subStringForHasHighChar(txt, startIndex, endIndex);

        Assert.assertEquals("题：①当𝑛≤", s);
    }

    @Test
    public void test_sub_3() {
        String txt = "题：①当𝑛≤𝑚时，先手";
        int startIndex = 5;
        int endIndex = 8;
        String s = StringHasHighChar.subStringForHasHighChar(txt, startIndex, endIndex);

        Assert.assertEquals("≤𝑚时", s);
    }

    @Test
    public void test_sub_4() {
        String txt = "题：①当𝑛≤𝑚时，先手";
        int startIndex = 9;
        int endIndex = 11;
        String s = StringHasHighChar.subStringForHasHighChar(txt, startIndex, endIndex);

        Assert.assertEquals("先手", s);
    }
}
