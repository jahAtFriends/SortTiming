import java.util.ArrayList;

/**
* Glorified stopwatch class for timing various algorithms and exporting
* the results. Each time record is regarded as a "trial" within which there
* may be multiple laps. For timing and comparing various algorithms, each
* trial represents one algorithm and each lap should represent that algorithm
* applied to a particular use case.
* <br/>
* <br/>
* When exporting, a {@code toString()} function is provided to reduce all
* time records to a uniform CSV format.
*/
public class TimeRecorder {
    private int currentTrialNumber = -1;
    private Long currentLapStart = -1L;
    private int longestTrial = -1;
    ArrayList<TimeRecord> records;
    TimeRecord currentTrial;
    
    private class TimeRecord {
        private String name;
        private ArrayList<Long> laps;
        
        TimeRecord(String name) {
            this.name = name;
            laps = new ArrayList<Long>();
        }
        
        int length() {
            return laps.size();
        }
        
        void addLap(Long time) {
            laps.add(time);
        }
        
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(this.name + ",");
            for (Long lp : laps) {
                sb.append(lp.toString() + ",");
            }
            
            return sb.toString();
        }
    }
    
    /**Initializes a new TimeRecorder */
    public TimeRecorder() {
        this.records = new ArrayList<TimeRecord>();
    }
    
    /** Starts a new trial with a given name */
    public void newTrial(String name) {
        if (currentTrial != null) {
            throw new IllegalStateException("Unconcluded trial in progress. Use concludeTrial() before calling newTrial()");
        }
        
        currentTrialNumber++;
        currentTrial = new TimeRecord(name);
    }
    
    /** Starts a new trial with no name (trial number will be used) */
    public void newTrial() {
        newTrial(++currentTrialNumber + "");
    }
    
    /** 
    * Concludes the current trial. Trials must be concluded to be recorded or
    * a new trial started.
    */
    public void concludeTrial() {
        records.add(currentTrial);
        currentTrial = null;
    }
    
    /** Starts timing a new lap in the current trial. */
    public void startLap() {
        if (currentTrial == null) {
            throw new IllegalStateException("No trial is currently active.");
        }
        
        currentLapStart = System.nanoTime();
    }
    
    /** Stops timing the current lap and logs the time */
    public void stopLap() {
        if (currentLapStart == -1L) {
            throw new IllegalStateException("No lap started.");
        }
        
        Long stop = System.nanoTime();
        Long lapLength = stop - currentLapStart;
        
        currentTrial.addLap(lapLength);
        if (currentTrial.length() > longestTrial) {
            longestTrial = currentTrial.length();
        }
        
        currentLapStart = -1L;
    }
    
    /** Converts all trials to CSV format */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name,");
        for (int i = 0; i < longestTrial; i++) {
            sb.append("Time " + i + ",");
        }
        sb.append("\n");
        
        for (TimeRecord tr : records) {
            sb.append(tr.toString());
        }
        
        return sb.toString();
    }
    
    public static void main(String[] args) {
        TimeRecorder tr = new TimeRecorder();
        
        tr.newTrial();
        for (int i = 0; i < 10; i++) {
            tr.startLap();
            for (int j = 0; j < 100 * i; j++) {
                ArrayList<Integer> x = new ArrayList<Integer>();
            }
            
            tr.stopLap();            
        }
        tr.concludeTrial();
        
        System.out.println(tr);

    }
}