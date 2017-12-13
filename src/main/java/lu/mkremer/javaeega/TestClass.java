package lu.mkremer.javaeega;

import java.util.Calendar;

import javax.faces.bean.ManagedBean;

@ManagedBean(name = "test", eager = true)
public class TestClass {
	
	private String expand(int n) {
		if(n < 10) {
			return "0" + n;
		}else {
			return "" + n;
		}
	}

	public String getTimeStamp() {
		Calendar c = Calendar.getInstance();
		return expand(c.get(Calendar.DAY_OF_MONTH)) + "/" + expand(c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR) + " "
				+ expand(c.get(Calendar.HOUR_OF_DAY)) + ":" + expand(c.get(Calendar.MINUTE)) + ":" + expand(c.get(Calendar.SECOND));
	}
	
}
