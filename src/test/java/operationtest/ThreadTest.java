package operationtest;

import org.junit.Test;

public class ThreadTest {
	
	private static QueryService service = new QueryService();
	
	@Test
	public void test() throws InterruptedException {
		
		CallOneThread t1 = new CallOneThread(service);
		CallTwoThread t2 = new CallTwoThread(service);
		CallThreeThread t3 = new CallThreeThread(service);
		CallFourThread t4 = new CallFourThread(service);
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		
		Thread.sleep(10000);
		
	}

}

class QueryService {
	private String value;
	public String getOne() throws InterruptedException {
		value = "get one";
		Thread.sleep(100);
		return value;
	}
	public String getTwo() throws InterruptedException {
		value = "get two";
		Thread.sleep(200);
		return value;
	}
	public String getThree() throws InterruptedException {
		value = "get three";
		Thread.sleep(300);
		return value;
	}
	public String getFour() throws InterruptedException {
		value = "get four";
		Thread.sleep(400);
		return value;
	}
}


class CallOneThread extends Thread {
	QueryService service;
//	String methodName;
//	public CallOneThread(QueryService service, String method) {
//		this.service = service;
//		this.methodName = method;
//	}
	
	public CallOneThread(QueryService service) {
		super();
		this.service = service;
	}
	
	public void run() {
		while(true) {
			try {
				System.out.println(Thread.currentThread().getName() + ":" + service.getOne());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
class CallTwoThread extends Thread {
	QueryService service;
//	String methodName;
//	public CallOneThread(QueryService service, String method) {
//		this.service = service;
//		this.methodName = method;
//	}
	
	public CallTwoThread(QueryService service) {
		super();
		this.service = service;
	}
	
	public void run() {
		while(true) {
			try {
				System.out.println(Thread.currentThread().getName() + ":" + service.getTwo());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
class CallThreeThread extends Thread {
	QueryService service;
//	String methodName;
//	public CallOneThread(QueryService service, String method) {
//		this.service = service;
//		this.methodName = method;
//	}
	
	public CallThreeThread(QueryService service) {
		super();
		this.service = service;
	}
	
	public void run() {
		while(true) {
			try {
				System.out.println(Thread.currentThread().getName() + ":" + service.getThree());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
class CallFourThread extends Thread {
	QueryService service;
//	String methodName;
//	public CallOneThread(QueryService service, String method) {
//		this.service = service;
//		this.methodName = method;
//	}
	
	public CallFourThread(QueryService service) {
		super();
		this.service = service;
	}
	
	public void run() {
		while(true) {
			try {
				System.out.println(Thread.currentThread().getName() + ":" + service.getFour());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}

