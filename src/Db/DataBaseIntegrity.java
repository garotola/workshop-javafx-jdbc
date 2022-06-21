package Db;

public class DataBaseIntegrity extends RuntimeException{
    private static final long serialVersionUID = 1L;
    public DataBaseIntegrity(String msg) {
        super(msg);
    }
}
