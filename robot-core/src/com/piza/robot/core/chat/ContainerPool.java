package com.piza.robot.core.chat;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class ContainerPool {
	
	private static final ContainerPool pool=new ContainerPool();
	
	private final LocalThreadFactory threadFactory=new LocalThreadFactory();
	private ExecutorService exec;
	private Map<String,LocalThread> allDispatcher=new HashMap<String,LocalThread>();
	
	private ContainerPool(){
		
	}
	
	public static ContainerPool getPool(){
		return pool;
	}
	
	
	public void init(int threadNumbers){
		if(exec==null){
			exec=Executors.newFixedThreadPool(threadNumbers, threadFactory);
		}
	}
	
	public void prcessMessage(AbstractDispatcher dispatcher){
		this.exec.execute(dispatcher);
	}
	
	public AbstractDispatcher getDispatcher(){
		for(LocalThread thread:allDispatcher.values()){
			if(!thread.isAlive()){
				return thread.getDispatcher();
			}
		}
		return null;
	}
	
	
	class LocalThreadFactory implements ThreadFactory{
		@Override
		public Thread newThread(Runnable r) {
			if(allDispatcher.containsKey(String.valueOf(r.hashCode()))){
				return allDispatcher.get(String.valueOf(r.hashCode()));
			}else{
			LocalThread newThread=new LocalThread(r);
			allDispatcher.put(String.valueOf(r.hashCode()),newThread);
			return newThread;
			}
		}
		
	}
	
	class LocalThread extends Thread{
		private Runnable dispatcher;
		public LocalThread(Runnable dispatcher){
			super(dispatcher);
			this.dispatcher=dispatcher;
		}
		
		public AbstractDispatcher getDispatcher(){
			return (AbstractDispatcher)this.dispatcher;
		}
		
		
	}
}
