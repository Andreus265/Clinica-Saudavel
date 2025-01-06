import desmoj.core.simulator.*;
import java.util.concurrent.TimeUnit;
import desmoj.core.dist.*;
import desmoj.core.statistic.*;

public class EventoDeChegada extends Event<Pacientes>{

    private ClinicaVidaSilm modelo;
    // instânciando construtor
    public EventoDeChegada(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);

        modelo = (ClinicaVidaSilm)owner; // instânciando modelo de simulação
     }

     public void eventRoutine(Pacientes paciente){
    
     modelo.qtd_clientes.update(modelo.presentTime().getTimeAsDouble(TimeUnit.HOURS));   
     Consultorios consultorio = modelo.consultorios.get(0);

     for(Consultorios consultorio1 : modelo.consultorios){
        
        if(consultorio.getFila().size() > consultorio1.getFila().size()){
            consultorio = consultorio1; 
        }

        
     }

     consultorio.insertPaciente(paciente);


     /*for(Consultorios consultorio : modelo.consultorios){

        if(consultorio.isDisponivel()){
            
            consultorio.ocupar();
            Pacientes pacienteA = consultorio.pacientesFila.first();

            consultorio.pacientesFila.remove(pacienteA);


            
        }
     }*/

     
     if(!consultorio.getFila().isEmpty()){
        if(consultorio.isDisponivel()){

            consultorio.dropPaciente(paciente);
            consultorio.ocupar();

            EventoDeAtendimento final_atendimento = new EventoDeAtendimento(modelo, "evento de final de atendimento", true);
            if(paciente.getStatus().equals("normal")){
                Double tempo = modelo.getAtendimentoPacienteNormal();
                final_atendimento.schedule(consultorio, paciente, new TimeSpan(tempo, TimeUnit.MINUTES));
                modelo.atendimentoHistNormal.update(tempo);
                
                
                }
            else{
                Double tempo = modelo.getAtendimentoPacienteGrave();
                final_atendimento.schedule(consultorio, paciente, new TimeSpan(modelo.getAtendimentoPacienteGrave(), TimeUnit.MINUTES));
                modelo.atendimentoHistGrave.update(tempo);
                
                }

            }

        }
        

    }
    
}