import java.util.*;

public class IsOpenNowTest {
    static void assertFalse (boolean t)throws Exception{
	if(t) throw new Exception("");
    }

    static void assertTrue (boolean t)throws Exception{
	if(!t) throw new Exception();
    }

    public static void testIsOpenNow() {
	try{
	    IsOpenNow f = new IsOpenNow();
	    f.init();
	    assertFalse(f.isOpenNow("Huu",Calendar.MONDAY, 8.0));
	    System.out.println("1");
	    assertTrue(f.isOpenNow("Haruru",Calendar.MONDAY, 0.5));
	    System.out.println("2");
	    assertTrue(f.isOpenNow("Huu",Calendar.MONDAY, 23.5));
	    System.out.println("3");
	    assertFalse(f.isOpenNow("Matsukiyo",Calendar.SUNDAY, 23.5));
	    System.out.println("4");
	}catch(Exception e){
	    System.out.println("例外発生");
	}
    }
    static public void main(String[] args){
	testIsOpenNow();
    }
}
