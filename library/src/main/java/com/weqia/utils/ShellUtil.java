package com.weqia.utils;

import java.io.IOException;

/**
 * 用于执行shell脚本的线程 <br>
 * Description: ShellThread.java Create on 2013-1-3 下午10:39:46
 * 
 * @author Bewin berwinzheng@gmail.com
 */

public class ShellUtil extends Thread {
	private boolean isReturn;
	private boolean isSuccess;
	private String cmd;

	public boolean isReturn() {
		return isReturn;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	/**
	 * @param cmd
	 *            shell命令内容
	 * @param isReturn
	 *            线程是否已经返回
	 * @param isSuccess
	 *            Process是否执行成功
	 */
	public ShellUtil(String cmd) {
		this.cmd = cmd;
	}

	@Override
	public void run() {
		try {
			Runtime runtime = Runtime.getRuntime();
			Process proc;
			try {
				proc = runtime.exec(cmd);
				isSuccess = (proc.waitFor() == 0);
			} catch (IOException e) {
			    e.printStackTrace();
			}
			isSuccess = true;
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
		isReturn = true;
	}
}