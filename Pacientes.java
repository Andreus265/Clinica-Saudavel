import desmoj.core.simulator.*;
import java.util.concurrent.TimeUnit;
import desmoj.core.dist.*;
import desmoj.core.statistic.*;



public class Pacientes extends Entity{
    
    private String status;

    //instanciando construtor
    public Pacientes(Model owner, String name, String status, Boolean showInTrace){
        super(owner, name, showInTrace); 
        this.status = status;
    
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}