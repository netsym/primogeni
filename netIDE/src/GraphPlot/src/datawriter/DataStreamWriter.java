package datawriter;

//import dynamicgraph.CreatePanels;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataStreamWriter {
	
	/**
	 * Writer for printing to the output stream.
	 */
	private BufferedWriter out = null;

	/**
	 * Whether new data series can still be added. 
	 */
	private boolean canAddDataSeries = true;

	/**
	 * Holds the data series labels.
	 */
	private ArrayList<String> dataSeriesLabels = null;

	/**
	 * Holds the series index cursor within the current dataset. 
	 */
	private int currentSeriesIndex = 0;


	/**
	 * Values of the current dataset.
	 */
	private Map<String, String> dataCache = null;


	/**
	 * Raised IOException (if any).
	 */
	private IOException ioException = null;
	
	
	DataStreamWriter(FileOutputStream os){
		this.out = new BufferedWriter(new OutputStreamWriter(os));
		this.canAddDataSeries = true;
		this.dataSeriesLabels = new ArrayList<String>();
		this.currentSeriesIndex = 0;	
		this.dataCache = new HashMap<String, String>();
		this.ioException = null;
	}
	
	
	/*
	 * Set up the names of the labels
	 * The labels will later be uses to add values
	 * */
	public void addDataSeries(String label) {
		if (null == label)
			throw new NullPointerException("Data series label may not be null");
		
		if (!canAddDataSeries)
			throw new IllegalStateException("Cannot add new data series at any more");
		
		label = label.trim();
		
		// enhance the string ie capitalize it 
		
		if(!dataSeriesLabels.contains(label))
			dataSeriesLabels.add(label);
	}
	
	public void setDataValue(int seriesIndex, double value) {
		if (0 > seriesIndex)
			throw new IllegalArgumentException("Series index may not be negative");
		if (dataSeriesLabels.size() <= seriesIndex)
			throw new IllegalArgumentException("Series index may not be >= number of data series (" + 
											   seriesIndex + " >= " + dataSeriesLabels.size()+")");
		dataCache.put(dataSeriesLabels.get(seriesIndex), Double.toString(value));
	}

	/**
	 * Assigns the specified value to the next data series in the current dataset.
	 * The "next"-pointer is reset each time a dataset is written to the stream.
	 *  
	 * @param value A value to include in the current dataset.
	 * @throws IllegalArgumentException If there are no more data series defined for this writer.
	 */
	public void setDataValue(double value) {
		setDataValue(currentSeriesIndex++, value);	
	}
	
	/**
	 * Assigns the specified value to the data series at the specified index in the
	 * current dataset.
	 * 
	 * @param seriesIndex Column index of the series to which {@code value} is to be assigned. 
	 * @param value A value to include in the current dataset ({@code null} will be
	 * converted to the empty string {@code ""}).
	 * @throws IllegalArgumentException If {@code seriesIndex < 0} or if
	 * {@code seriesIndex >= (number of data-series defined for this writer)}. 
	 */
	public void setDataValue(int seriesIndex, String value) {
		if (0 > seriesIndex)
			throw new IllegalArgumentException("Series index may not be negative");
		if (dataSeriesLabels.size() <= seriesIndex)
			throw new IllegalArgumentException("Series index may not be >= number of data series (" + 
											   seriesIndex + " >=" + dataSeriesLabels.size()+")");
		dataCache.put(dataSeriesLabels.get(seriesIndex), null == value ? "" : value);
	}

	/**
	 * Assigns the specified value to the next data series in the current dataset.
	 * The "next"-pointer is reset each time a dataset is written to the stream.
	 *  
	 * @param value A value to include in the current dataset ({@code null} will be
	 * converted to the empty string {@code ""}).
	 * @throws IllegalArgumentException If there are no more data series defined for this writer.
	 */
	public void setDataValue(String value) {
		setDataValue(currentSeriesIndex++, value);	
	}
	
	public void writeLabelSet() {
		try {
			for (int index = 1; index < dataSeriesLabels.size(); index++) {
				out.write(dataSeriesLabels.get(index));
				if(index < dataSeriesLabels.size())
					out.write(",");
			}
			out.newLine();
			out.flush();
		} catch (IOException e) {
			ioException = e;
		}
	}
	
	public void writeDataSet() {
		try {
			String val = null;
			for (int index = 0; index < dataSeriesLabels.size(); index++) {
				val = dataCache.get(dataSeriesLabels.get(index));
				if (null == val)
					val = "";
				out.write(val);
				if(index < dataSeriesLabels.size())
					out.write(",");
			}
			out.newLine();
			out.flush();
		} catch (IOException e) {
			ioException = e;
		}
		dataCache.clear();
		currentSeriesIndex = 0;
	}
	
	/**
	 * Closes the underlying output stream.
	 * If any of the data values which have previously been cached by any of the
	 * {@code setDataValue(...)}-methods are not written yet, they wre written to the stream before it is closed.
	 * Once this method was invoken, no more data can be written. 
	 */
	public void close() {
		if (!dataCache.isEmpty())
			writeDataSet();
		try {		
			out.close();
		} catch (IOException e) {
			ioException = e;
		}
	}
	
	/**
	 * Check whether a recent operation caused an {@code IOException}. 
	 * @return {@code true} if an {@code IOException} was encountered after this writer was created or after
	 * the last call to {@link #resetIOException()}, {@code false} otherwise.
	 */
	public boolean hadIOException() {
		return (null != ioException);
	}

	/**
	 * Gets the last {@code IOException} encountered by this writer.
	 * 
	 * @return If {@link #hadIOException()} returns {@code true} - the last {@code IOException} encountered
	 * by this writer, otherwise - {@code null}.
	 */
	public IOException getIOException() {
		return ioException;
	}

	/**
	 * Deletes any internal state concerned with previously encountered {@code IOException}s.
	 *
	 */
	public void resetIOException() {
		ioException = null;
	}
}