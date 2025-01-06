import desmoj.core.simulator.*;
import java.util.concurrent.TimeUnit;
import desmoj.core.dist.*;
import desmoj.core.statistic.*;

import java.util.Random;  //L como fun

public class GeradorPaciente extends ExternalEvent {
     public static int n_paciente;
    // instânciando construtor
    public GeradorPaciente(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);

    }
    
    


    public void eventRoutine(){

        n_paciente++;
        Random random = new Random();
        int indice_aleatorio = random.nextInt(100);  //L como fun
        ClinicaVidaSilm modelo = (ClinicaVidaSilm)getModel();
        String status;
        modelo.numeroDePacientes.update();

        double time_chegada = modelo.getChegadaPaciente();
        GeradorPaciente novo_paciente = new GeradorPaciente(modelo, "evento geracao de paciente", true);
        novo_paciente.schedule(new TimeSpan(time_chegada, TimeUnit.MINUTES));

        if(indice_aleatorio <= 70){
            status = "normal";
            
            modelo.numeroDePacientesNormal.update();
        }else{
            status = "grave";
            
            modelo.numeroDePacientesGrave.update();
        }

        Pacientes paciente = new Pacientes(modelo, String.valueOf(n_paciente), status , true);

        EventoDeChegada chegadaPaciente = new EventoDeChegada(modelo, "evento de chegada", true);
        
        chegadaPaciente.schedule(paciente, new TimeSpan(0, TimeUnit.MINUTES));
 

        modelo.chegadaHist.update(time_chegada);
  
    }

}
