import org.apache.logging.log4j.LogManager;
	import org.apache.logging.log4j.Logger;

public class LoggingTest {

	

	
	    private static final Logger logger = LogManager.getLogger(LoggingTest.class);

	    public static void main(String[] args) {
	    	
	    	
	    	 logger.trace("Trace Message — Verbose details for debugging");
	    	logger.debug("Entering in method main");
	       
	        logger.debug("Debug Message — For developer analysis");
	        logger.info("Info Message — Normal flow information");
	        logger.warn("Warning Message — Something might be off");
	        logger.error("Error Message — Something failed");
	        logger.fatal("Fatal Message — Critical system failure");
	        
	        int i = 10/0;
	 
	        try {
	        String name = null;
	        
	       
	        
	           name.toLowerCase();
	        }catch(Exception ex) {
	        	logger.error("error in LoggingTest " + ex.getMessage());
	        }
	    	logger.debug("leaving from method main");
	    }
	
}