import java.util.*;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.StringTokenizer;
import java.io.IOException;

public class IsOpenNow {
    final static int N = 9; //data数
    final static int SIZE = 20; //1行あたりの最大トークン数
    static String[] miseName;
    static TimeData[] miseData;


    IsOpenNow(){
	miseName = new String[this.N];
	miseData = new TimeData[this.N];
    }

    public static String toString(int day){
	switch(day) {
	case Calendar.SUNDAY: return "SUNDAY";
	case Calendar.SATURDAY: return "SATURDAY";
	case Calendar.FRIDAY: return "FRIDAY";
	case Calendar.THURSDAY: return "THURSDAY";
	case Calendar.WEDNESDAY: return "WEDNESDAY";
	case Calendar.TUESDAY: return "TUESDAY";
	case Calendar.MONDAY: return "MONDAY";
	default: return " ";
	}
    }

    public static boolean isOpenNow(String mise, int dayOfWeek, double time) {
	int i;
	for(i=0;i<N && !(miseName[i].equals(mise));i++){ }
	if(i==N) return false; //お店がリストにないとき

	return miseData[i].isOpen(toString(dayOfWeek),time);
    }

    // Get the current time.  
    public static boolean isOpenNow(String mise) {
	Calendar calendar = Calendar.getInstance();
	int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
	double time = (double)calendar.get(Calendar.HOUR_OF_DAY);
	time += (double)calendar.get(Calendar.MINUTE)/60;
	return isOpenNow(mise,dayOfWeek, time);
    }


    public static class Time{
	String day = "";
	double begin,end;
	Time(double b, double e){
	    begin = b;
	    end = e;
	}
	Time(String d,double b, double e){
	    day = d;
	    begin = b;
	    end = e;
	}

    }
    public static class TimeData {
	List<Time> timeList;
	List<Time> expList;
	List<String> closeDay;
	
	TimeData(){
	    timeList = new ArrayList<Time>();
	    expList = new ArrayList<Time>(); 
	    closeDay = new ArrayList<String>();
	}

	void addTime(double begin,double end){
	    if(end < begin){
		timeList.add(new Time(begin,24.0));		
		timeList.add(new Time(0,end));
	    }
	    else{
		timeList.add(new Time(begin,end));
	    }
	}
	void addExp(String day, double begin, double end){
	    expList.add(new Time(day,begin,end));
	}
	void addclose(String day){
	    closeDay.add(day);
	}
	boolean isOpen2(Time t,double time){
	    return t.begin <= time && time <= t.end;
	}
	boolean isOpen(String d,double time){
	    //全休
	    int i;
	    for(i=0;i<closeDay.size() && !(d.equals(closeDay.get(i)));i++){}
	    if(i!=closeDay.size()) return false;
	
	    //例外
	    for(i=0;i<expList.size() && !(d.equals((expList.get(i)).day));i++){}
		    
	    if(i!=expList.size()){
		return isOpen2(expList.get(i),time);
	    }else{
		for(i=0;i<timeList.size();i++){
		    if(isOpen2(timeList.get(i),time)){
			return true;
		    }
		}
		return false;
	    }
	}
    }

    void init(){	
	try {
            FileReader fr = new FileReader("eigyou.csv");
            BufferedReader br = new BufferedReader(fr);
	    
	    String[] tokens = new String[this.SIZE];
	    double[] ret = new double[2];
	    
            String line,next1,next2;
            StringTokenizer token;
	    int j,k,a,b,i =0;
	    
            while ((line = br.readLine()) != null) {
		
                token = new StringTokenizer(line, ",");
		miseName[i] = token.nextToken(); //店の名前
		miseData[i] = new TimeData(); 
		
		for(j = 0; token.hasMoreTokens(); j++){
		    tokens[j] = token.nextToken();
		    System.out.println("j="+j+" "+tokens[j]);
		}
		k = j;  // kはトークン数

		//通常営業時間を読み込む。
		a = 0;
		try{
		    while(a < k){
			ret[0] = Double.parseDouble(tokens[a]);//begin
			ret[1] = Double.parseDouble(tokens[++a]);//end
			miseData[i].addTime(ret[0],ret[1]);
			System.out.println("ret[0]="+ret[0]+" "+"ret[1]="+ret[1]);
			++a;
		    }
		}catch (Exception e){
		    //例外が出るのはtokens[a]が数字でなくて、文字列のときなので、
		    //例外営業時間の読み込みに移行する。
		}
		

		//例外営業時間を読み込む。
		b = a + 1;
		try{
		    while(b < k){
			ret[0] = Double.parseDouble(tokens[b]);//begin
			ret[1] = Double.parseDouble(tokens[++b]);//end
			miseData[i].addExp(tokens[a],ret[0],ret[1]);
			System.out.println(tokens[a]+" -> ret[0]="+ret[0]+" "+"ret[1]="+ret[1]);
			a = b + 1;
			b = a + 1;;
		    }
		}catch (Exception e){
		    //例外が出るのはtokens[b]が数字でなくて、文字列のときなので、
		    miseData[i].addclose(tokens[a]);
		    System.out.println("EXCP! close on " + tokens[a]);
		    b ++;
		}
	  
		b --;
		//定休日を読み込む
		while(b < k){
		    miseData[i].addclose(tokens[b]);
		    System.out.println("close on " + tokens[b]);
		    b++;
		}

                System.out.println("**********");
		i ++;
            }
            br.close();
	    
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
