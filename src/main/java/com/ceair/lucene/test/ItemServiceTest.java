package com.ceair.lucene.test;



import org.junit.Test;

import com.ceair.lucene.pojo.Item;
import com.ceair.lucene.service.ItemService;

public class ItemServiceTest {
	
	private ItemService itemService = new ItemService();
	
	@Test
	public void addTest1() throws Exception {
//		for (int i = 1; i < 1000; i++ ) {
			Item item = new Item();
			item.setName("串件");
			item.setDescription("是指从停场飞机、装机或备用的发动机/APU拆下附件串到需要完成生产任务的飞机、发动机/APU上以满足航材供应需要，而串下的部件不再安装在飞机、发动机或APU上");
			
			itemService.addItem(item);
//		}
	}
	
	@Test
	public void addTest2() throws Exception {
		Item item = new Item();
		item.setName("飞机维修缺陷");
		item.setDescription("指在航线维护和定检维修工作中产生的故障缺陷");
		
		long start = System.currentTimeMillis();
		itemService.addItem(item);
		long end = System.currentTimeMillis();
		System.out.println("花费：" + (end - start) + "ms");
	}
	
	@Test
	public void addTest3() throws Exception {
		Item item = new Item();
		item.setName("非例行维修项目(NRC)");
		item.setDescription("指在航线维护和定检维修工作中因缺少航材、工装设备或时间不足等原因产生的不影响飞机适航和飞行安全，且无需按规定办理保留故障的故障缺陷项目");
		
		itemService.addItem(item);
	}

}
