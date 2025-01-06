import desmoj.core.simulator.*;
import java.util.concurrent.TimeUnit;
import desmoj.core.dist.*;
import desmoj.core.statistic.*;
import java.util.ArrayList;
import java.util.List;

public class ClinicaVidaSilm extends Model {

        protected static int n_consultorios = 3;
        protected List<Consultorios> consultorios;
        protected Histogram atendimentoHistNormal;
        protected Histogram atendimentoHistGrave;
        protected Histogram chegadaHist;
        protected Histogram qtd_clientes;
        
        protected Count numeroDePacientes;
        protected Count numeroDePacientesNormal;
        protected Count numeroDePacientesGrave;
        private ContDistExponential chegadaPaciente;
        private ContDistNormal atendimentoPacienteNormal;
        private ContDistNormal atendimentoPacienteGrave;

        //definindo o construtor
        public ClinicaVidaSilm(Model owner, String modelName, boolean showInReport, boolean showInTrace) {
        super(owner, modelName, showInReport, showInTrace);

        consultorios = new ArrayList<>();
    }

        //descrição do modelo
        public String description() {
        return "Modelo da clinica vida saudavel responsavel pelo sistema de fila e atendimento de clientes. " +
                "A chegada de clientes e de forma exponencial com media de 15 minutos com cerca de 30% pacientes urgentes e 70% pacientes com condicoes menos graves" + 
                "O tempo de atendimento tem uma distribuicao normal de 20 minutos e desvio padrao de 5 minutos para pacientes em condicoes normais e 3 minutos para condições graves" +
                "A clinica funciona durante 10 horas do dia e o objetivo e nunca ter mais de 5 pessoas em fila.";
    }

    
    //iniciando setup de parâmetros necessários
    public void init() {
        chegadaHist = new Histogram(this, "tempo de chegada dos pacientes", 0, 50, 5, true, true);
        qtd_clientes = new Histogram(this, "Pacientes entrando durante a simulação", 0, 10, 10, true, true);
        atendimentoHistNormal = new Histogram(this, "tempo de atendimento dos pacientes normal", 10, 30, 5, true, false);
        atendimentoHistGrave = new Histogram(this, "tempo de atendimento dos pacientes grave", 5, 15, 5, true, false);
        

        chegadaPaciente = new ContDistExponential(this, "chegada de paciente", 15.0, true, true);
        chegadaPaciente.setSeed(System.currentTimeMillis());
        atendimentoPacienteNormal = new ContDistNormal(this, "atendimento de paciente normal", 20.0, 5.0, true, true);
        atendimentoPacienteGrave = new ContDistNormal(this, "atendimento de paciente grave", 10.0, 3.0, true, true);

        
        atendimentoPacienteNormal.setSeed(System.currentTimeMillis());
        numeroDePacientes = new Count(this, "qtd de pacientes total" , true, false);
        numeroDePacientesNormal= new Count(this, "qtd de pacientes estado normal" , true, false);
        numeroDePacientesGrave= new Count(this, "qtd de pacientes estado grave" , true, false);

        atendimentoPacienteGrave.setSeed(System.currentTimeMillis());

        

        for(int i = 0; i < n_consultorios; i++){
            Queue<Pacientes> pacientesFila = new Queue<>(this, "Fila de Pacientes do Consultório " + (i + 1), true, true);
            Consultorios consultorio = new Consultorios(this, "consultorio" + (i + 1), pacientesFila, true);
            consultorios.add(consultorio);
        }
    }

    public double getChegadaPaciente(){
        
       
        return chegadaPaciente.sample();
    }

    public double getAtendimentoPacienteNormal() {
        return atendimentoPacienteNormal.sample();
    }

    public double getAtendimentoPacienteGrave() {
        return atendimentoPacienteGrave.sample();
    }
    
    //Trigger para o ínicio do gerador de paciente
    public void doInitialSchedules(){
        GeradorPaciente gerandoPaciente = new GeradorPaciente(this, "evento de geracao de paciente", true);
        gerandoPaciente.schedule(new TimeSpan(getChegadaPaciente(),TimeUnit.MINUTES));
    }

    public static void main(String [] args){

        ClinicaVidaSilm modelo = new ClinicaVidaSilm(null, "modelo da clinica vida saudavel", true, true);
        Experiment exp = new Experiment("ClinicaSimultor");
        modelo.connectToExperiment(exp);

        exp.setShowProgressBar(true);
        exp.stop(new TimeInstant(600, TimeUnit.MINUTES));  
        exp.tracePeriod(new TimeInstant(0), new TimeInstant(60, TimeUnit.MINUTES));  // Período de rastreamento
        exp.debugPeriod(new TimeInstant(0), new TimeInstant(50, TimeUnit.MINUTES));   // Período de debug

        // Inicia o experimento
        exp.start();

        // Gera o relatório
        exp.report();

        // Finaliza o experimento
        exp.finish();
    }
}

