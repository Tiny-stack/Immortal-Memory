
package  org.main.backend.records;

// import com.google.errorprone.annotations.ForOverride;

public class  Ressponse {
    public String message;
    public boolean status;
    public Ressponse(String message,boolean status)
    {
        this.message = message;
        this.status = status;
    }
    @Override
    public String toString()
    {
        return "{'message': "+this.message+",'status'"+this.status+"}";
    }
}