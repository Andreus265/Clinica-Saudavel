import desmoj.core.simulator.*;
import java.util.concurrent.TimeUnit;
import desmoj.core.dist.*;
import desmoj.core.statistic.*;

public class EventoDeAtendimento extends EventOf2Entities<Consultorios,Pacientes> {

    private ClinicaVidaSilm modelo;
    

    public EventoDeAtendimento(ClinicaVidaSilm owner, String name, boolean showInTrace){

        super(owner, name, showInTrace);
        modelo = (ClinicaVidaSilm)owner;
    }

    public void eventRoutine(Consultorios consultorio, Pacientes paciente){

        
        sendTraceNote(paciente + "saindo do consultorio");
       
        consultorio.liberar();


        if(!consultorio.getFila().isEmpty()){
            
            if(consultorio.isDisponivel()){
                Pacientes novo_paciente = consultorio.getPaciente();
                consultorio.dropPaciente(novo_paciente);
                consultorio.ocupar();

                EventoDeAtendimento final_atendimento = new EventoDeAtendimento(modelo, "evento de final de atendimento", true);
                if(novo_paciente.getStatus().equals("normal")){
                    double tempo = modelo.getAtendimentoPacienteNormal();
                    final_atendimento.schedule(consultorio, novo_paciente, new TimeSpan(tempo, TimeUnit.MINUTES));
                    modelo.atendimentoHistNormal.update(tempo);
                    
                    
            
                }
                else{
                    double tempo = modelo.getAtendimentoPacienteGrave();
                    final_atendimento.schedule(consultorio, novo_paciente, new TimeSpan(tempo, TimeUnit.MINUTES));
                    modelo.atendimentoHistGrave.update(tempo);
                    
                    
                }
            }
        }
        
        
        

    }
}