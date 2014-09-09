package monitor.commands;



public class PrimeUpdateVizExportRate extends AbstractCmd{
	public final long new_rate;
	public PrimeUpdateVizExportRate(long new_rate) {
		super(CommandType.PRIME_VIZ_EXPORT_RATE_UPDATE);
		this.new_rate=new_rate;
	}

	/* (non-Javadoc)
	 * @see monitor.commands.AbstractCmd#getBodyLength()
	 */
	@Override
	public int getBodyLength() {
		return Long.SIZE/8;
	}
	public String toString() {
		return "[PrimeUpdateVizExportRate new_rate="+new_rate+"]";
	}
}