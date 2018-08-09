package Tool;

import java.io.PrintStream;

public class Logger {
    private PrintStream writer;
    private Logger(){
        writer = System.out;
    }
    private void println(String message){
        writer.println(message);
    }
    private static Logger instance;

    public static void Log(String message){
        instance.println(message);
    }
    public static void Log(String format, Object... args){
        Log(String.format(format, args));
    }
    static {
        instance = new Logger();
    }
}
