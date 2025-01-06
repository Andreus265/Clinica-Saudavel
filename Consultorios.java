import desmoj.core.simulator.*;
import java.util.concurrent.TimeUnit;



import desmoj.core.dist.*;
import desmoj.core.statistic.*;
//import java.util.Queue;



public class Consultorios extends Entity {

    private Queue<Pacientes> pacientesFila;
    private boolean disponivel = true;
        //instanciando construtor
    public Consultorios(Model owner, String name, Queue<Pacientes> fila, Boolean showInTrace){
        super(owner, name, showInTrace); 
        this.pacientesFila = fila;
    }

    public boolean isDisponivel(){
        return disponivel;
    }

    public void ocupar(){
        disponivel = false;
    }

    public void liberar(){
        disponivel = true;
    }

    public Queue<Pacientes> getFila(){
        return pacientesFila;

    }
    public void dropPaciente(Pacientes paciente){
        this.pacientesFila.remove(paciente);

    }
    public void insertPaciente(Pacientes paciente){
        this.pacientesFila.insert(paciente);
    }
    
    public Pacientes getPaciente(){
        return this.pacientesFila.get(0);
    }
}