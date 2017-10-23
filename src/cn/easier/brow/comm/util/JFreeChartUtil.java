package cn.easier.brow.comm.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.TextAnchor;

public class JFreeChartUtil {

	/**
	 * 
	 * @Description (创建三维柱状图)
	 * @param chartTitle     图表标题
	 * @param categoryAxisLabel    目录(X)轴的显示标签
	 * @param valueAxisLabel    数值(Y)轴的显示标签
	 * @param dataSet      数据集
	 * @param orientation  图表方向:PlotOrientation.VERTICAL水平、垂直 ; PlotOrientation.HORIZONTAL横向
	 * @param legend     是否显示图例(对于简单的柱状图必须是 false)
	 * @param tooltips   是否生成工具
	 * @param urls    是否生成 URL 链接
	 * @return JFreeChart
	 * @date 2016年1月28日下午12:01:45
	 * @author qiufh
	 */
	public static JFreeChart createBarChart3D(String chartTitle, String categoryAxisLabel, String valueAxisLabel,
			CategoryDataset dataSet, PlotOrientation orientation, boolean legend, boolean tooltips, boolean urls) {
		//创建主题样式 
		StandardChartTheme standardChartTheme = new StandardChartTheme("CN");//设置标题字体
		standardChartTheme.setExtraLargeFont(new Font("宋书", Font.BOLD, 20));//设置图例的字体
		standardChartTheme.setRegularFont(new Font("宋书", Font.PLAIN, 15));//设置轴向的字体
		standardChartTheme.setLargeFont(new Font("宋书", Font.PLAIN, 15));//应用主题样式
		ChartFactory.setChartTheme(standardChartTheme);
		JFreeChart chart = ChartFactory.createBarChart3D(//
				chartTitle, // 图表标题
				categoryAxisLabel, // 目录(X)轴的显示标签
				valueAxisLabel, // 数值(Y)轴的显示标签
				dataSet, // 数据集
				orientation, // 图表方向
				legend, // 是否显示图例(对于简单的柱状图必须是 false)
				tooltips, // 是否生成工具
				urls // 是否生成 URL 链接
		);
		//得到图表中的Plot对象
		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		//取得横轴
		CategoryAxis categoryAxis = plot.getDomainAxis();
		categoryAxis.setLabelFont(new Font("宋体", Font.BOLD, 20));//横轴显示标签的字体
		//categoryAxis.setLowerMargin(0.1);//设置距离图片左端距离此时为10%
		//categoryAxis.setUpperMargin(0.1);//设置距离图片右端距离此时为百分之10
		categoryAxis.setCategoryLabelPositionOffset(10);//图表横轴与标签的距离(10像素)
		categoryAxis.setCategoryMargin(0.3);//横轴标签之间的距离30%
		//categoryAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);//分类标签以45度倾斜
		//categoryAxis.setTickLabelFont(new Font("宋体", Font.BOLD, 20));
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();//取得纵轴（Y轴）
		rangeAxis.setLabelFont(new Font("宋体", Font.BOLD, 20));//设置纵轴（Y轴）显示标签的字体
		//设置最高的一个柱与图片顶端的距离
		rangeAxis.setUpperMargin(0.5);
		//设置最低的一个 Item 与图片底端的距离
		rangeAxis.setLowerMargin(0.15);
		// 数据轴数据标签的显示格式    
		rangeAxis.setNumberFormatOverride(new DecimalFormat("0%"));
		BarRenderer3D renderer = (BarRenderer3D) plot.getRenderer();
		renderer.setBaseOutlinePaint(Color.BLACK);
		renderer.setWallPaint(Color.gray);//设置 Wall 的颜色
		//设置网格背景颜色  
		plot.setBackgroundPaint(Color.white);
		//设置网格竖线颜色  
		plot.setDomainGridlinePaint(Color.pink);
		//设置网格横线颜色  
		plot.setRangeGridlinePaint(Color.pink);
		//设置每种水果代表的柱的颜色
		//renderer.setSeriesPaint(0, new Color(0, 0, 255));
		//renderer.setSeriesPaint(1, new Color(0, 100, 255));
		//renderer.setSeriesPaint(2, Color.GREEN);
		renderer.setItemMargin(0.3);//设置每个地区所包含的平行柱的之间距离
		//renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		renderer.setBaseItemLabelsVisible(true);//显示每个柱的数值
		//在柱上显示百分比
		java.text.NumberFormat nf = java.text.NumberFormat.getPercentInstance();
		nf.setMaximumFractionDigits(2);
		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}", nf));
		renderer.setBaseItemLabelFont(new java.awt.Font("黑体", Font.TRUETYPE_FONT, 20));
		//默认的数字显示在柱子中，通过如下两句可调整数字的显示  
		renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));
		renderer.setItemLabelAnchorOffset(10D);
		plot.setRenderer(renderer);//让上面对柱子的设置生效
		//设置地区、销量的显示位置  
		//将下方的“肉类”放到上方  
		//plot.setDomainAxisLocation(AxisLocation.TOP_OR_RIGHT);  
		//将默认放在左边的“销量”放到右方  
		//plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_RIGHT); 
		//plot.setForegroundAlpha(0.6f);//设置柱的透明度
		return chart;
	}

	/**
	 * 
	 * @Description (创建二维柱状图)
	 * @param chartTitle     图表标题
	 * @param categoryAxisLabel    目录(X)轴的显示标签
	 * @param valueAxisLabel    数值(Y)轴的显示标签
	 * @param dataSet      数据集
	 * @param orientation  图表方向:PlotOrientation.VERTICAL水平、垂直 ; PlotOrientation.HORIZONTAL横向
	 * @param legend     是否显示图例(对于简单的柱状图必须是 false)
	 * @param tooltips   是否生成工具
	 * @param urls    是否生成 URL 链接
	 * @return JFreeChart
	 * @date 2016年1月28日下午12:01:45
	 * @author qiufh
	 */
	public static JFreeChart createBarChart(String chartTitle, String categoryAxisLabel, String valueAxisLabel,
			CategoryDataset dataSet, PlotOrientation orientation, boolean legend, boolean tooltips, boolean urls) {
		//创建主题样式 
		StandardChartTheme standardChartTheme = new StandardChartTheme("CN");//设置标题字体
		standardChartTheme.setExtraLargeFont(new Font("宋书", Font.BOLD, 20));//设置图例的字体
		standardChartTheme.setRegularFont(new Font("宋书", Font.PLAIN, 15));//设置轴向的字体
		standardChartTheme.setLargeFont(new Font("宋书", Font.PLAIN, 15));//应用主题样式
		ChartFactory.setChartTheme(standardChartTheme);
		JFreeChart chart = ChartFactory.createBarChart(//
				chartTitle, // 图表标题
				categoryAxisLabel, // 目录(X)轴的显示标签
				valueAxisLabel, // 数值(Y)轴的显示标签
				dataSet, // 数据集
				orientation, // 图表方向
				legend, // 是否显示图例(对于简单的柱状图必须是 false)
				tooltips, // 是否生成工具
				urls // 是否生成 URL 链接
		);
		//得到图表中的Plot对象
		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		//取得横轴
		CategoryAxis categoryAxis = plot.getDomainAxis();
		categoryAxis.setLabelFont(new Font("宋体", Font.BOLD, 20));//横轴显示标签的字体
		//categoryAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);//分类标签以45度倾斜
		//categoryAxis.setTickLabelFont(new Font("宋体", Font.BOLD, 20));
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();//取得纵轴（Y轴）
		rangeAxis.setLabelFont(new Font("宋体", Font.BOLD, 20));//设置纵轴（Y轴）显示标签的字体
		//设置最高的一个柱与图片顶端的距离
		rangeAxis.setUpperMargin(0.5);
		//设置最低的一个 Item 与图片底端的距离
		rangeAxis.setLowerMargin(0.15);
		// 数据轴数据标签的显示格式    
		rangeAxis.setNumberFormatOverride(new DecimalFormat("0%"));
		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setBaseOutlinePaint(Color.BLACK);
		//设置每种水果代表的柱的颜色
		//renderer.setSeriesPaint(0, new Color(0, 0, 255));
		//renderer.setSeriesPaint(1, new Color(0, 100, 255));
		//renderer.setSeriesPaint(2, Color.GREEN);
		renderer.setItemMargin(0.1);//设置每个地区所包含的平行柱的之间距离
		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		renderer.setBaseItemLabelsVisible(true);//显示每个柱的数值
		//在柱上显示百分比
		java.text.NumberFormat nf = java.text.NumberFormat.getPercentInstance();
		nf.setMaximumFractionDigits(2);
		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}", nf));
		plot.setRenderer(renderer);//让上面对柱子的设置生效
		//设置网格背景颜色  
		plot.setBackgroundPaint(Color.white);
		//设置网格竖线颜色  
		plot.setDomainGridlinePaint(Color.white);
		//设置网格横线颜色  
		plot.setRangeGridlinePaint(Color.white);
		//plot.setForegroundAlpha(0.6f);//设置柱的透明度
		return chart;
	}

	/**
	 * 
	 * @Description (创建三维饼状图)
	 * @param chartTitle
	 * @param categoryAxisLabel  X轴标题
	 * @param valueAxisLabel  Y轴标题
	 * @param dataSet  数据集
	 * @param legend   是否显示图例
	 * @param tooltips 是否生成工具
	 * @param urls     是否生成url链接
	 * @return JFreeChart
	 * @date 2016年1月27日下午1:10:01
	 * @author qiufh
	 */
	public static JFreeChart createPieChart3D(String chartTitle, DefaultPieDataset dataSet, boolean legend, boolean tooltips,
			boolean urls) {
		JFreeChart chart = ChartFactory.createPieChart3D(//
				chartTitle, // 图表标题
				dataSet, //数据集
				legend, // 是否显示图例
				tooltips, //是否生成工具
				urls//是否生成url链接
		);
		// JFreeChart主要由三个部分构成：title(标题),legend(图释),plot(图表主体)。          
		//三个部分设置字体的方法分别如下:            
		TextTitle textTitle = chart.getTitle();
		textTitle.setFont(new Font("宋体", Font.BOLD, 20));
		LegendTitle leg = chart.getLegend();
		if (leg != null) {
			leg.setItemFont(new Font("宋体", Font.BOLD, 20));
		}
		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setLabelFont(new Font("宋体", Font.BOLD, 12));
		plot.setNoDataMessage("No data available");
		plot.setCircular(true);
		plot.setLabelGap(0.01D);// 间距
		//方块距离饼的距离 只要负值就能把数据放到饼里   
		//plot.setLabelLinkMargin(-0.8);
		//是否显示线 fasle就不显示了
		plot.setLabelLinksVisible(true);
		//{0}分类、{1}原值、{2}百分比
		//如果百分比要包括一位小数，则使用：
		plot.setLabelGenerator(
				new StandardPieSectionLabelGenerator("{0}:{2}", new DecimalFormat("0.00"), new DecimalFormat("0.00%")));
		//显示实际数值
		//plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}:{1}"));

		//标签与图形之间的距离
		plot.setLabelLinkMargin(-0.05);
		//设置连线的颜色
		//plot.setLabelLinkPaint(new Color(0, 180, 205));

		//为分区设置指定的颜色
		//plot.setSectionPaint("未用容量", new Color(125, 125, 125));
		//plot.setSectionPaint("已用容量", new Color(0, 180, 205));

		//设置绘图面板外边以及阴影的填充颜色
		plot.setOutlinePaint(Color.BLACK);
		plot.setShadowPaint(Color.WHITE);

		//设置整个面板的背景色
		plot.setBackgroundPaint(Color.WHITE);

		//设置标签背景颜色、边框颜色、阴影颜色和文字颜色
		plot.setLabelBackgroundPaint(null);
		plot.setLabelOutlinePaint(null);
		plot.setLabelShadowPaint(null);
		plot.setLabelPaint(new Color(120, 120, 120));

		return chart;
	}

	/**
	 * 
	 * @Description (创建二维饼状图)
	 * @param chartTitle
	 * @param categoryAxisLabel  X轴标题
	 * @param valueAxisLabel  Y轴标题
	 * @param dataSet  数据集
	 * @param legend   是否显示图例
	 * @param tooltips 是否生成工具
	 * @param urls     是否生成url链接
	 * @return JFreeChart
	 * @date 2016年1月27日下午1:10:01
	 * @author qiufh
	 */
	public static JFreeChart createPieChart(String chartTitle, DefaultPieDataset dataSet, boolean legend, boolean tooltips,
			boolean urls) {
		JFreeChart chart = ChartFactory.createPieChart(//
				chartTitle, // 图表标题
				dataSet, //数据集
				legend, // 是否显示图例
				tooltips, //是否生成工具
				urls//是否生成url链接
		);
		// JFreeChart主要由三个部分构成：title(标题),legend(图释),plot(图表主体)。          
		//三个部分设置字体的方法分别如下:            
		TextTitle textTitle = chart.getTitle();
		textTitle.setFont(new Font("宋体", Font.BOLD, 20));
		LegendTitle leg = chart.getLegend();
		if (leg != null) {
			leg.setItemFont(new Font("宋体", Font.BOLD, 20));
		}
		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setLabelFont(new Font("宋体", Font.BOLD, 12));
		plot.setNoDataMessage("No data available");
		plot.setCircular(true);
		plot.setLabelGap(0.01D);// 间距
		//方块距离饼的距离 只要负值就能把数据放到饼里   
		//plot.setLabelLinkMargin(-0.8);
		//是否显示线 fasle就不显示了
		plot.setLabelLinksVisible(true);
		//{0}分类、{1}原值、{2}百分比
		//如果百分比要包括一位小数，则使用：
		//plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}:{2}", new DecimalFormat("0.00"), new DecimalFormat("0.00%")));
		plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}"));
		//显示实际数值
		//plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}:{1}"));
		//标签与图形之间的距离
		plot.setLabelLinkMargin(-0.05);
		//设置连线的颜色
		//plot.setLabelLinkPaint(new Color(0, 180, 205));

		//为分区设置指定的颜色
		//plot.setSectionPaint("未用容量", new Color(125, 125, 125));
		//plot.setSectionPaint("已用容量", new Color(0, 180, 205));

		//设置绘图面板外边以及阴影的填充颜色
		plot.setOutlinePaint(Color.BLACK);
		plot.setShadowPaint(Color.WHITE);

		//设置整个面板的背景色
		plot.setBackgroundPaint(Color.WHITE);

		//设置标签背景颜色、边框颜色、阴影颜色和文字颜色
		plot.setLabelBackgroundPaint(null);
		plot.setLabelOutlinePaint(null);
		plot.setLabelShadowPaint(null);
		plot.setLabelPaint(new Color(120, 120, 120));
		return chart;
	}

	/**
	 * 
	 * @Description (创建三维折线图)
	 * @param chartTitle
	 * @param categoryAxisLabel  X轴标题
	 * @param valueAxisLabel  Y轴标题
	 * @param dataSet  数据集
	 * @param orientation   绘制方向 :PlotOrientation.VERTICAL（垂直，纵向）PlotOrientation.HORIZONTAL（横向）
	 * @param legend   显示图例  
	 * @param tooltips 采用标准生成器  
	 * @param urls     是否生成超链接  
	 * @return JFreeChart
	 * @date 2016年1月27日下午1:10:01
	 * @author qiufh
	 */
	public static JFreeChart createLineChart3D(String chartTitle, String categoryAxisLabel, String valueAxisLabel,
			CategoryDataset dataSet, PlotOrientation orientation, boolean legend, boolean tooltips, boolean urls) {
		JFreeChart chart = ChartFactory.createLineChart3D(//
				chartTitle, //图表标题
				categoryAxisLabel, //X轴标题
				valueAxisLabel, //Y轴标题
				dataSet, //数据集
				orientation, // 绘制方向  
				legend, // 显示图例  
				tooltips, // 采用标准生成器  
				urls // 是否生成超链接  
		);
		TextTitle textTitle = chart.getTitle();
		Font font = new Font("宋体", Font.BOLD, 20);
		Font titleFont = new Font("宋书", Font.PLAIN, 15);
		textTitle.setFont(font); // 设置标题字体  
		// 设置图例类别字体  
		LegendTitle leg = chart.getLegend();
		if (leg != null) {
			leg.setItemFont(font);
		}
		chart.setBackgroundPaint(Color.WHITE);// 设置背景色   
		//获取绘图区对象  
		CategoryPlot plot = chart.getCategoryPlot();
		plot.setBackgroundPaint(Color.WHITE); // 设置绘图区背景色  
		plot.setRangeGridlinePaint(Color.WHITE); // 设置水平方向背景线颜色  
		plot.setRangeGridlinesVisible(true);// 设置是否显示水平方向背景线,默认值为true  
		plot.setDomainGridlinePaint(Color.WHITE); // 设置垂直方向背景线颜色  
		plot.setDomainGridlinesVisible(true); // 设置是否显示垂直方向背景线,默认值为false  
		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setLabelFont(font); // 设置横轴字体  
		domainAxis.setTickLabelFont(font);// 设置坐标轴标尺值字体  
		domainAxis.setLowerMargin(0.01);// 左边距 边框距离  
		domainAxis.setUpperMargin(0.06);// 右边距 边框距离,防止最后边的一个数据靠近了坐标轴。  
		domainAxis.setMaximumCategoryLabelLines(2);
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setLabelFont(font);
		//rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());//Y轴显示整数  
		rangeAxis.setNumberFormatOverride(new DecimalFormat("0%"));//Y轴显示百分比
		rangeAxis.setAutoRangeMinimumSize(1); //最小跨度  
		rangeAxis.setUpperMargin(0.18);//上边距,防止最大的一个数据靠近了坐标轴。     
		rangeAxis.setLowerBound(0); //最小值显示0  
		rangeAxis.setAutoRange(false); //不自动分配Y轴数据  
		rangeAxis.setTickMarkStroke(new BasicStroke(1.6f)); // 设置坐标标记大小  
		rangeAxis.setTickMarkPaint(Color.BLACK); // 设置坐标标记颜色  
		// 获取折线对象  
		LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
		BasicStroke realLine = new BasicStroke(1.2f); // 设置实线  
		// 设置虚线  
		float dashes[] = { 5.0f };
		BasicStroke brokenLine = new BasicStroke(1.6f, // 线条粗细  
				BasicStroke.CAP_ROUND, // 端点风格  
				BasicStroke.JOIN_ROUND, // 折点风格  
				8f, dashes, 0.6f);
		for (int i = 0; i < dataSet.getRowCount(); i++) {
			if (i % 2 == 0) {
				renderer.setSeriesStroke(i, realLine); // 利用实线绘制  
			} else {
				renderer.setSeriesStroke(i, brokenLine); // 利用虚线绘制  
			}
		}
		//renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		//在折点显示百分比
		java.text.NumberFormat nf = java.text.NumberFormat.getPercentInstance();
		nf.setMaximumFractionDigits(2);
		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}", nf));
		renderer.setBaseItemLabelsVisible(true);//显示每个柱的数值
		//绘制折点样式
		renderer.setBaseShapesVisible(true);
		renderer.setDrawOutlines(true);
		renderer.setUseFillPaint(true);
		renderer.setBaseFillPaint(java.awt.Color.white);
		plot.setNoDataMessage("无对应的数据，请重新查询。");
		plot.setNoDataMessageFont(titleFont);//字体的大小  
		plot.setNoDataMessagePaint(Color.RED);//字体颜色 
		plot.setRenderer(renderer);
		return chart;
	}

	/**
	 * 
	 * @Description (创建二维折线图)
	 * @param chartTitle
	 * @param categoryAxisLabel  X轴标题
	 * @param valueAxisLabel  Y轴标题
	 * @param dataSet  数据集
	 * @param orientation   绘制方向 :PlotOrientation.VERTICAL（垂直，纵向）PlotOrientation.HORIZONTAL（横向）
	 * @param legend   显示图例  
	 * @param tooltips 采用标准生成器  
	 * @param urls     是否生成超链接  
	 * @return JFreeChart
	 * @date 2016年1月27日下午1:10:01
	 * @author qiufh
	 */
	public static JFreeChart createLineChart(String chartTitle, String categoryAxisLabel, String valueAxisLabel,
			CategoryDataset dataSet, PlotOrientation orientation, boolean legend, boolean tooltips, boolean urls) {
		JFreeChart chart = ChartFactory.createLineChart(//
				chartTitle, //图表标题
				categoryAxisLabel, //X轴标题
				valueAxisLabel, //Y轴标题
				dataSet, //数据集
				orientation, // 绘制方向  
				legend, // 显示图例  
				tooltips, // 采用标准生成器  
				urls // 是否生成超链接  
		);
		TextTitle textTitle = chart.getTitle();
		Font font = new Font("宋体", Font.BOLD, 20);
		Font titleFont = new Font("宋书", Font.PLAIN, 15);
		textTitle.setFont(font); // 设置标题字体  
		// 设置图例类别字体  
		LegendTitle leg = chart.getLegend();
		if (leg != null) {
			leg.setItemFont(font);
		}
		chart.setBackgroundPaint(Color.WHITE);// 设置背景色   
		//获取绘图区对象  
		CategoryPlot plot = chart.getCategoryPlot();
		plot.setBackgroundPaint(Color.WHITE); // 设置绘图区背景色  
		plot.setRangeGridlinePaint(Color.WHITE); // 设置水平方向背景线颜色  
		plot.setRangeGridlinesVisible(true);// 设置是否显示水平方向背景线,默认值为true  
		plot.setDomainGridlinePaint(Color.WHITE); // 设置垂直方向背景线颜色  
		plot.setDomainGridlinesVisible(true); // 设置是否显示垂直方向背景线,默认值为false  
		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setLabelFont(font); // 设置横轴字体  
		domainAxis.setTickLabelFont(font);// 设置坐标轴标尺值字体  
		domainAxis.setLowerMargin(0.01);// 左边距 边框距离  
		domainAxis.setUpperMargin(0.06);// 右边距 边框距离,防止最后边的一个数据靠近了坐标轴。  
		domainAxis.setMaximumCategoryLabelLines(2);
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setLabelFont(font);
		//rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());//Y轴显示整数  
		rangeAxis.setNumberFormatOverride(new DecimalFormat("0%"));//Y轴显示百分比
		rangeAxis.setAutoRangeMinimumSize(1); //最小跨度  
		rangeAxis.setUpperMargin(0.18);//上边距,防止最大的一个数据靠近了坐标轴。     
		rangeAxis.setLowerBound(0); //最小值显示0  
		rangeAxis.setAutoRange(false); //不自动分配Y轴数据  
		rangeAxis.setTickMarkStroke(new BasicStroke(1.6f)); // 设置坐标标记大小  
		rangeAxis.setTickMarkPaint(Color.BLACK); // 设置坐标标记颜色  
		// 获取折线对象  
		LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
		BasicStroke realLine = new BasicStroke(1.2f); // 设置实线  
		// 设置虚线  
		float dashes[] = { 5.0f };
		BasicStroke brokenLine = new BasicStroke(1.6f, // 线条粗细  
				BasicStroke.CAP_ROUND, // 端点风格  
				BasicStroke.JOIN_ROUND, // 折点风格  
				8f, dashes, 0.6f);
		for (int i = 0; i < dataSet.getRowCount(); i++) {
			if (i % 2 == 0) {
				renderer.setSeriesStroke(i, realLine); // 利用实线绘制  
			} else {
				renderer.setSeriesStroke(i, brokenLine); // 利用虚线绘制  
			}
		}
		//renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		//在折点显示百分比
		java.text.NumberFormat nf = java.text.NumberFormat.getPercentInstance();
		nf.setMaximumFractionDigits(2);
		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}", nf));
		renderer.setBaseItemLabelsVisible(true);//显示每个折点的数值
		//绘制折点样式
		renderer.setBaseShapesVisible(true);
		renderer.setDrawOutlines(true);
		renderer.setUseFillPaint(true);
		renderer.setBaseFillPaint(java.awt.Color.white);
		plot.setNoDataMessage("无对应的数据，请重新查询。");
		plot.setNoDataMessageFont(titleFont);//字体的大小  
		plot.setNoDataMessagePaint(Color.RED);//字体颜色 
		plot.setRenderer(renderer);
		return chart;
	}

	/**
	 * 
	 * @Description (将jfreechart图片输出到服务器磁盘,返回图片路径)
	 * @param chart
	 * @param name
	 * @return String  成功返回路径，失败则返回null
	 * @date 2016年1月27日上午9:17:53
	 * @author qiufh
	 */
	public static String saveChartAsPNG(JFreeChart chart, String uploaddir, String name) {
		FileOutputStream fos_jpg = null;
		try {
			fos_jpg = new FileOutputStream(uploaddir + name);
			ChartUtilities.writeChartAsJPEG(fos_jpg, 1, chart, 1020, 600, null);
			return uploaddir + name;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				fos_jpg.close();
			} catch (Exception e) {
			}
		}
	}
}
