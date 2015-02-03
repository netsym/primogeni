package dynamicgraph;


import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.OverlayLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class CreatePanels extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// The time series data
	private static XYSeries series[];
	private static ChartPanel chartPanel[];
	private static JPanel pane[];
	private static JComboBox comboBox[];
	
	private static boolean pause = false;
	private static String[] namedescription;
	private static BufferedReader fileData;
	
	CreatePanels(String csv_file) throws NoDataFileDefined{
		if(csv_file == null)
			throw new NoDataFileDefined();
		
		String[] lineArr;
		String line;
		
		namedescription = init_label(csv_file.trim());
		
		series = new XYSeries[namedescription.length];
		chartPanel = new ChartPanel[namedescription.length];
		pane = new JPanel[4];
		comboBox = new JComboBox[4];
		
		setJMenuBar(init_menubar());
		
		getContentPane().setLayout(new GridLayout(2,2));
		
		for(int index = 0; index < series.length; index++)
			init_series(index);
		
		for(int index = 0; index < pane.length; index++){
			init_pane(index);
			add(pane[index]);
		}
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
		
		while(true){
			try{
				if(!pause){
					line = fileData.readLine();
					if(line != null){					
						lineArr = line.split(",");
						for(int index = 0; index < series.length; index++){
							if(index + 1 < lineArr.length)
								series[index].add(Double.parseDouble(lineArr[0])/1000.0, Double.parseDouble(lineArr[index+1]));
						}
					}
					else{ Thread.sleep(500); }
				}
				else{ Thread.sleep(1000); }
			}catch (IOException io){ io.printStackTrace(); } 
			 catch (InterruptedException e) { e.printStackTrace(); }
		}
	}
	
	private String[] init_label(String path){
		int tempInt;
		String line = "", tempStr;
		String arrline[];
		try{
			fileData = new BufferedReader(new FileReader(path));
			line = fileData.readLine();
			arrline = line.split(",");
			setTitle(Character.toUpperCase(arrline[0].charAt(0)) + arrline[0].replace('_', ' ').substring(1));
			for(int i = 1; i < arrline.length; i++){
				tempStr = arrline[i].replace('_', ' ');
				tempInt = tempStr.indexOf(' ');
				arrline[i] = Character.toUpperCase(tempStr.charAt(tempInt+1)) + tempStr.substring(tempInt + 2);
			}
			return Arrays.copyOfRange(arrline, 1, arrline.length);
		}catch(IOException e){ e.printStackTrace(); }
		
		return null;
	}
	
	private JMenuBar init_menubar(){
		JMenuBar menubar = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenuItem item1 = new JMenuItem("export data N/A");
		JMenuItem item2 = new JMenuItem("graph data N/A");
		JMenuItem item3 = new JMenuItem("pause");
		
		item1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				System.out.println("export data");
			}
		});
		
		item2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				System.out.println("display data");
			}
		});
		
		item3.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				if(pause)
					pause = false;
				else 
					pause = true;
			}
		});
		
		file.add(item1);
		file.add(item2);
		file.add(item3);
		menubar.add(file);
		return menubar;
	}
	
	private void init_series(final int index){
		series[index] = new XYSeries(namedescription[index]);
		final XYSeriesCollection dataset = new XYSeriesCollection(series[index]);
		final JFreeChart chart = createChart(dataset, index);
		chartPanel[index] = new ChartPanel(chart);
		chartPanel[index].setPreferredSize(new java.awt.Dimension(500, 270));
	}
	
	private void init_pane(final int index){
		
		JPanel p1 = new JPanel();
		JPanel glasspane = new JPanel();
		
		p1.setPreferredSize(new java.awt.Dimension(500, 270));
		glasspane.setPreferredSize(new java.awt.Dimension(500, 270));
		//setGlassPane(glasspane);
		glasspane.setOpaque(false);
		
		OverlayLayout overlay = new OverlayLayout(p1);
		p1.setLayout(overlay);
		
		pane[index] = new JPanel(new BorderLayout());
		comboBox[index] = new JComboBox(namedescription);
		comboBox[index].setSelectedIndex(index);
		
		final JComboBox cbox = comboBox[index];
		
		// Combo Box Listener
		cbox.addActionListener(new ActionListener(){
			private final int paneIndex = index;
			
			public void actionPerformed(ActionEvent event){
				int comboSelected = cbox.getSelectedIndex();
				
				// The current chart needs to be switched
				// Only if there's a chart already in quadrant 
				ChartPanel switchCharts = ((ChartPanel)((JPanel)pane[paneIndex].getComponent(1)).getComponent(1));
				
				// Remove the current chart
				((JPanel)pane[paneIndex].getComponent(1)).remove(1);
				
				// problem: why can't I add this reference and have other panels with the same reference
				((JPanel)pane[paneIndex].getComponent(1)).add(chartPanel[comboSelected], 1);
				
				/* Quick fix */
				// We need to search for a chart that is missing from the quadrant
				// Meaning it needs to be switched with the current
				for(JPanel panel : pane)
					try{ 
						((JPanel)panel.getComponent(1)).getComponent(1);
					}catch(Exception e){ 
						((JPanel)panel.getComponent(1)).add(switchCharts, 1);
					}
				/* Quick fix */
				
				pane[paneIndex].revalidate();
				pane[paneIndex].repaint();
			}
		});
		
		pane[index].add(comboBox[index], BorderLayout.NORTH);
		p1.add(glasspane);
		p1.add(chartPanel[index]);
		pane[index].add(p1, BorderLayout.SOUTH);
		
		// Functionality of panning the chart left or right
		glasspane.addMouseMotionListener(new MouseMotionAdapter(){
			private final int paneIndex = index;
			private int lastValue = 0;
			@Override
			public void mouseDragged(MouseEvent event) {
				int value = event.getX();
				XYPlot plot = ((ChartPanel)((JPanel)pane[paneIndex].getComponent(1))
								 .getComponent(1)).getChart().getXYPlot();
				
				ValueAxis axis = plot.getDomainAxis();
				
				if(value > lastValue){
					axis.pan(0.02);
					lastValue = value;
				}
				else{
					axis.pan(-0.02);
					lastValue = value;
				}
				
			}
		});
		
		glasspane.addMouseListener(new MouseAdapter(){
			JPanel container = (JPanel)pane[index].getComponent(1);
			
			public void mouseClicked(MouseEvent e){
				if (e.getClickCount() == 2){
					System.out.println("d Double click");
					container.getComponent(0).setVisible(false);
				}
			}
		});
		
		JPanel container = (JPanel)pane[index].getComponent(1);
		container.getComponent(1).addMouseListener(new MouseAdapter(){
			JPanel container = (JPanel)pane[index].getComponent(1);
			
			public void mouseClicked(MouseEvent e){
				if (e.getClickCount() == 2){
					System.out.println("e Double click");
					container.getComponent(0).setVisible(true);
				}
			}
		});
	}
	
	private JFreeChart createChart(final XYDataset dataset, int index){
		final JFreeChart chart = ChartFactory.createXYLineChart(
				namedescription[index], 
				"", 
				"", 
				dataset,
				PlotOrientation.VERTICAL,
				false,
				false,
				false
		);
		
		final XYPlot plot = chart.getXYPlot();
		NumberAxis xAxis = (NumberAxis)plot.getDomainAxis();
		xAxis.setAutoRange(true);
		//xAxis.setLowerBound(0);
		xAxis.setFixedAutoRange(100.0);
		
		plot.setDomainAxis(xAxis);
		
		return chart;
	}

}

@SuppressWarnings("serial")
class NoDataFileDefined extends Exception{
	NoDataFileDefined(){
		super("Possible Command line argument missing. Data File path missing.");
	}
}