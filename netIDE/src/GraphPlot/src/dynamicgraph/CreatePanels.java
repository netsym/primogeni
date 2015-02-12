package dynamicgraph;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.OverlayLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingConstants;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.Range;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


/*
 * Note: Structure of the Panels are
 * Index 0: Drop down interface menu
 * Index 1: JPanel
 * 			Index 0: JLabel
 * 			Index 1: RadioButton
 * Index 2: Overlay
 * 			Index 0: Glass-pane
 * 			Index 1: Chart 
 */

public class CreatePanels extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// The time series data
	private static XYSeries series[];
	private static ChartPanel chartPanel[];
	private static JPanel pane[];
	private static JMenuBar interfaceMenu[];
	private static JLabel interfaceLabel[];
	
	private static boolean pause = false;
	private static String[] nameDescription;
	private static BufferedReader fileData;
	
	CreatePanels(String csv_file) throws NoDataFileDefined{
		if(csv_file == null)
			throw new NoDataFileDefined();
		
		String arrline[], submenu_names[];
		String line;
		
		submenu_names = init_label(csv_file.trim());
		
		series = new XYSeries[nameDescription.length];
		chartPanel = new ChartPanel[nameDescription.length];
		pane = new JPanel[4];
		interfaceMenu = new JMenuBar[4];
		interfaceLabel = new JLabel[4];
		
		setJMenuBar(init_menubar());
		
		getContentPane().setLayout(new GridLayout(2,2));
		
		for(int index = 0; index < series.length; index++)
			init_series(index);
		
		for(int index = 0; index < pane.length; index++){
			init_pane(index, submenu_names);
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
						arrline = line.split(",");
						for(int index = 0; index < series.length; index++){
							if(index + 1 < arrline.length)
								series[index].add(Double.parseDouble(arrline[0])/1000.0, Double.parseDouble(arrline[index+1]));
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
		String arrline[], arrtemp[];
		try{
			fileData = new BufferedReader(new FileReader(path));
			line = fileData.readLine();
			arrline = line.split(",");
			arrtemp = line.split(",");
			setTitle(Character.toUpperCase(arrline[0].charAt(0)) + arrline[0].replace('_', ' ').substring(1));
			for(int i = 1; i < arrline.length; i++){
				tempStr = arrline[i].replace('_', ' ');
				tempInt = tempStr.indexOf(' ');
				arrline[i] = Character.toUpperCase(tempStr.charAt(tempInt+1)) + tempStr.substring(tempInt + 2);
			}
			nameDescription = Arrays.copyOfRange(arrline, 1, arrline.length);;
			return Arrays.copyOfRange(arrtemp, 1, arrtemp.length);
		}catch(IOException e){ e.printStackTrace(); }
		
		return null;
	}
	
	
	private void init_interfaceDropMenu(final int index, String arr[]){
		int underscore_index, start = 0;
		String current_if, prev_if = "";
		
			
			LinkedList<JMenu> parent_submenu = new LinkedList<JMenu>();
			//JList parent_submenus = new JList();
			JMenu main_menu = new JMenu("Interfaces");
			
			// group up the namedescription into appropriate interface names
			for(int i = 0; i < arr.length; i++){
				
				
				underscore_index = arr[i].indexOf("_");
				current_if = arr[i].substring(2, underscore_index);
				
				// start submenu
				if(!current_if.equals(prev_if) && i != 0){
					final String tempInterfaceName = "Interface " + prev_if;
					JMenu submenu = new JMenu("Interface " + prev_if);
					JMenuItem item;
					
					for(int temp = start; temp < i; temp++){
						final int tempSelected = temp;
						item = new JMenuItem(nameDescription[temp]);
						
						item.addActionListener(new ActionListener(){
							final int paneIndex = index;
							final int selected = tempSelected;
							final String interfaceName = tempInterfaceName;
							
							public void actionPerformed(ActionEvent event) {
								//System.out.println(index + " " + selected + " " + namedescription[selected]);
								
								JLabel setNewLabel = (JLabel)((JPanel)pane[paneIndex].getComponent(1)).getComponent(0);
								JLabel getLabel = setNewLabel;
								setNewLabel.setText(interfaceName + " - " + nameDescription[selected]);
								
								// The current chart needs to be switched
								// Only if there's a chart already in quadrant 
								ChartPanel switchCharts = (ChartPanel)((JPanel)pane[paneIndex].getComponent(2)).getComponent(1);
								
								// Remove the current chart
								((JPanel)pane[paneIndex].getComponent(2)).remove(1);
								
								// problem: why can't I add this reference and have other panels with the same reference
								((JPanel)pane[paneIndex].getComponent(2)).add(chartPanel[selected], 1);
								
								/* Quick fix */
								// We need to search for a chart that is missing from the quadrant
								// Meaning it needs to be switched with the current
								for(JPanel panel : pane)
									try{ 
										((JPanel)panel.getComponent(2)).getComponent(1);
									}catch(Exception e){
										setNewLabel = (JLabel)((JPanel)pane[paneIndex].getComponent(1)).getComponent(0);
										setNewLabel.setText(getLabel.getText());
										((JPanel)panel.getComponent(2)).add(switchCharts, 1);
									}
								/* Quick fix */
								
								pane[paneIndex].revalidate();
								pane[paneIndex].repaint();
							}
						});
						submenu.add(item);
					}
					parent_submenu.add(submenu);
					start = i;
				}// end submenu
				prev_if = current_if;
			}
			
			interfaceMenu[index] = new JMenuBar();
			//parent_submenus.setVisibleRowCount(5);
			//JScrollPane scroll = new JScrollPane(parent_submenus);
			//scroll.setWheelScrollingEnabled(true);
			//scroll.getVerticalScrollBar().setUnitIncrement(16);
			for(JMenu sub: parent_submenu)
				main_menu.add(sub);
			interfaceMenu[index].add(main_menu);
	}
	
	
	private JMenuBar init_menubar(){
		JMenuBar menubar = new JMenuBar();
		JMenu file = new JMenu("File");
		
		JMenuItem item1 = new JMenuItem("Pause");
		JMenuItem setting = new JMenuItem("Settings");
		
		item1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				if(pause)
					pause = false;
				else 
					pause = true;
			}
		});
		
		setting.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				JFrame temp = new JFrame();
				temp.getContentPane().setLayout(new BorderLayout());
				
				JPanel p = new JPanel();
				final JLabel label = new JLabel("New x-axis size", SwingConstants.CENTER);
				final JTextField input = new JTextField(3);
				JButton button = new JButton("Enter");
				
				label.setPreferredSize(new Dimension(200, 50));
				p.setPreferredSize(new Dimension(200, 120));
				
				button.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent event) {
						int domain = 0;
						boolean domainProb = false, domainTypeProb = false;
						try{
							domain = Integer.parseInt(input.getText());
							if(domain > 401 || domain < 9)
								domainProb = true;
						}catch(NumberFormatException e){ domainTypeProb = true; }
						
						if(!domainProb && !domainTypeProb){
							label.setText("New x-axis size");
							for(JPanel panel: pane){
								XYPlot plot = ((ChartPanel)((JPanel)panel.getComponent(2))
										.getComponent(1)).getChart().getXYPlot();
								NumberAxis xAxis = (NumberAxis)plot.getDomainAxis();
								xAxis.setRange(new Range(0, domain));
								//xAxis.setTickUnit(new NumberTickUnit(5));
							}
						}
						else if(domainProb)
							label.setText("<html>New x-axis size<br><font color='red'>Domain size Error</font></html>");
						else
							label.setText("<html>New x-axis size<br><font color='red'>Input type Error</font></html>");
						
						domainProb = domainTypeProb = false;
					}
					
				});
				
				p.add(label, BorderLayout.NORTH);
				p.add(input, BorderLayout.CENTER);
				p.add(button, BorderLayout.SOUTH);
				temp.add(p);
				temp.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				temp.pack();
				temp.setVisible(true);
			}
		});
		
		file.add(item1);
		file.add(setting);
		menubar.add(file);
		
		return menubar;
	}
	
	
	private void init_series(final int index){
		series[index] = new XYSeries(nameDescription[index]);
		final XYSeriesCollection dataset = new XYSeriesCollection(series[index]);
		final JFreeChart chart = createChart(dataset, index);
		chartPanel[index] = new ChartPanel(chart);
		chartPanel[index].setPreferredSize(new java.awt.Dimension(500, 270));
	}
	
	
	private void init_pane_interface(final int index, final JRadioButton pan, JRadioButton zoom){
		final JPanel container = (JPanel)pane[index].getComponent(2);
		ButtonGroup group = new ButtonGroup();
		
		pan.setText("Pan");
		pan.setSelected(true);
		zoom.setText("Zoom");
		
		group.add(pan);
		group.add(zoom);
		
		// Functionality of panning the chart left or right
		((JPanel)container.getComponent(0)).addMouseMotionListener(new MouseMotionAdapter(){
			private final int paneIndex = index;
			private int lastValue = 0;
			private JRadioButton radiobut = ((JRadioButton)((JPanel)pane[paneIndex].getComponent(1)).getComponent(1));
			
			public void mouseDragged(MouseEvent event) {
				if( radiobut.isSelected() ){
					int value = event.getX();
					XYPlot plot = ((ChartPanel)((JPanel)pane[paneIndex].getComponent(2))
							.getComponent(1)).getChart().getXYPlot();
					
					ValueAxis axis = plot.getDomainAxis();
					
					if(value > lastValue)
						axis.pan(0.04);
					else
						axis.pan(-0.04);
					lastValue = value;
				}
			}
		});
		
		// Enable the glasspane (allows panning)
		pan.addActionListener(new ActionListener(){
			private final int paneIndex = index;
			JPanel container = (JPanel)pane[paneIndex].getComponent(2);
			
			public void actionPerformed(ActionEvent e){
				if (((JRadioButton)e.getSource()).isSelected())
					container.getComponent(0).setVisible(true);
			}
		});
		
		// Disable the glasspane (allows zooming)
		zoom.addActionListener(new ActionListener(){
			private final int paneIndex = index;
			JPanel container = (JPanel)pane[paneIndex].getComponent(2);
			
			public void actionPerformed(ActionEvent e){
				if (((JRadioButton)e.getSource()).isSelected())
					container.getComponent(0).setVisible(false);
			}
		});
	}
	
	
	private void init_pane(final int index, String arrtemp[]){
		
		JPanel overlayContainer = new JPanel();
		JPanel glasspane = new JPanel();
		
		JPanel label_button = new JPanel(new BorderLayout());
		label_button.setSize(new Dimension(100, 20));
		
		pane[index] = new JPanel(new BorderLayout());
		
		JRadioButton pan = new JRadioButton();
		JRadioButton zoom = new JRadioButton();
		
		interfaceLabel[index] = new JLabel("Interface 1 - " + nameDescription[index], SwingConstants.CENTER);
		interfaceLabel[index].setPreferredSize(new Dimension(400, 20));
		
		overlayContainer.setPreferredSize(new java.awt.Dimension(500, 270));
		glasspane.setPreferredSize(new java.awt.Dimension(500, 270));
		//setGlassPane(glasspane);
		glasspane.setOpaque(false);
		
		OverlayLayout overlay = new OverlayLayout(overlayContainer);
		overlayContainer.setLayout(overlay);
				
		init_interfaceDropMenu(index, arrtemp);
		
		pane[index].add(interfaceMenu[index], BorderLayout.NORTH);
		label_button.add(interfaceLabel[index], BorderLayout.WEST);
		label_button.add(pan, BorderLayout.CENTER);
		label_button.add(zoom, BorderLayout.EAST);
		pane[index].add(label_button, BorderLayout.CENTER);
		overlayContainer.add(glasspane);
		overlayContainer.add(chartPanel[index]);
		pane[index].add(overlayContainer, BorderLayout.SOUTH);
		
		init_pane_interface(index, pan, zoom);
	}
	
	
	
	private JFreeChart createChart(final XYDataset dataset, int index){
		final JFreeChart chart = ChartFactory.createXYLineChart(
				"", 
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
