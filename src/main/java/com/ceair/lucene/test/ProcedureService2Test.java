package com.ceair.lucene.test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.lucene.queryparser.classic.ParseException;
import org.junit.Test;

import com.ceair.lucene.pojo.Item;
import com.ceair.lucene.pojo.Procedure;
import com.ceair.lucene.service.ProcedureService2;

public class ProcedureService2Test {
	
	private ProcedureService2 proService = new ProcedureService2();
	
	@Test
	public void addTest() throws SQLException, IOException, ParseException {
		Procedure procedure = new Procedure();
		procedure.setName("ETMOP04-008串件的管理.doc");
		procedure.setContent("当串件涉及停场飞机时，完成飞机串件工作后，工作者应在串件及被串件飞机的《技术记录本》上填写工作内容，"
				+ "说明串件原因，被串件机号、部件装机位置、上下件序号等。同时，应在被串件飞机的驾驶舱明显部位及拆件位置悬挂警告标牌、拔出断路器等相关措施。"
				+ "当串件涉及发动机/APU时，应妥善对发动机/APU串件分离面进行包扎、堵盖并保存分离面之间的零件；在拆卸的零部件处应挂《串件警告标识牌》。"
				+ "当串件涉及停场飞机时，完成飞机串件工作后，工作者应在串件及被串件飞机的《技术记录本》上填写工作内容，"
				+ "说明串件原因，被串件机号、部件装机位置、上下件序号等。同时，应在被串件飞机的驾驶舱明显部位及拆件位置悬挂警告标牌、拔出断路器等相关措施。"
				+ "当串件涉及发动机/APU时，应妥善对发动机/APU串件分离面进行包扎、堵盖并保存分离面之间的零件；在拆卸的零部件处应挂《串件警告标识牌》。"
				+ "当串件涉及停场飞机时，完成飞机串件工作后，工作者应在串件及被串件飞机的《技术记录本》上填写工作内容，"
				+ "说明串件原因，被串件机号、部件装机位置、上下件序号等。同时，应在被串件飞机的驾驶舱明显部位及拆件位置悬挂警告标牌、拔出断路器等相关措施。"
				+ "当串件涉及发动机/APU时，应妥善对发动机/APU串件分离面进行包扎、堵盖并保存分离面之间的零件；在拆卸的零部件处应挂《串件警告标识牌》。"
				+ "当串件涉及停场飞机时，完成飞机串件工作后，工作者应在串件及被串件飞机的《技术记录本》上填写工作内容，"
				+ "说明串件原因，被串件机号、部件装机位置、上下件序号等。同时，应在被串件飞机的驾驶舱明显部位及拆件位置悬挂警告标牌、拔出断路器等相关措施。"
				+ "当串件涉及发动机/APU时，应妥善对发动机/APU串件分离面进行包扎、堵盖并保存分离面之间的零件；在拆卸的零部件处应挂《串件警告标识牌》。");
		procedure.setPath("E:\\svn\\MUMOP\\docs\\1_阶段文档\\1.1_需求\\需求资料\\规章、手册文档\\东航技术145组织机构变更\\ETMOP\\04 飞机维修\\04-008串件的管理\\ETMOP04-008串件的管理.doc");
		Date now = new Date();
		procedure.setCreateTime(now);
		procedure.setUpdateTime(now);
		
		long start = System.currentTimeMillis();
		List<Item> items = proService.addProcedure(procedure);
		long end = System.currentTimeMillis();
		System.out.println("总花费：" + (end-start) + "ms");
		System.out.println("术语数：" + items.size());
		for (Item item : items) {
			System.out.println(ReflectionToStringBuilder.toString(item, ToStringStyle.JSON_STYLE));
		}
	}

}
