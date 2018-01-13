/*
 Proyecto Java EE, DAGSS-2013
 */
package es.uvigo.esei.dagss.controladores.medico;

import es.uvigo.esei.dagss.controladores.autenticacion.AutenticacionControlador;
import es.uvigo.esei.dagss.dominio.daos.CitaDAO;
import es.uvigo.esei.dagss.dominio.daos.MedicamentoDAO;
import es.uvigo.esei.dagss.dominio.daos.MedicoDAO;
import es.uvigo.esei.dagss.dominio.daos.PrescripcionDAO;
import es.uvigo.esei.dagss.dominio.entidades.Cita;
import es.uvigo.esei.dagss.dominio.entidades.EstadoCita;
import es.uvigo.esei.dagss.dominio.entidades.EstadoReceta;
import es.uvigo.esei.dagss.dominio.entidades.Medicamento;
import es.uvigo.esei.dagss.dominio.entidades.Medico;
import es.uvigo.esei.dagss.dominio.entidades.Prescripcion;
import es.uvigo.esei.dagss.dominio.entidades.Receta;
import es.uvigo.esei.dagss.dominio.entidades.TipoUsuario;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.joda.time.DateTime;
import org.joda.time.Days;

/**
 *
 * @author ribadas
 */

@Named(value = "medicoControlador")
@SessionScoped
public class MedicoControlador implements Serializable {

    private Medico medicoActual;
    private Cita citaActual;
    
    private String dni;
    private String numeroColegiado;
    private String password;
    
    private List<Cita> citas;
    private List<Prescripcion> prescripciones;
    private List<Medicamento> medicamentos;
    
    private Prescripcion prescripcionActual = new Prescripcion();
    private Prescripcion prescripcionSeleccionada = new Prescripcion();

    @Inject
    private AutenticacionControlador autenticacionControlador;
    

    @EJB
    private MedicoDAO medicoDAO;
    @EJB
    private CitaDAO citaDAO;
    @EJB
    private PrescripcionDAO prescripcionDAO;
    @EJB
    private MedicamentoDAO medicamentoDAO;

    /**
     * Creates a new instance of AdministradorControlador
     */
    public MedicoControlador() {
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNumeroColegiado() {
        return numeroColegiado;
    }

    public void setNumeroColegiado(String numeroColegiado) {
        this.numeroColegiado = numeroColegiado;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Medico getMedicoActual() {
        return medicoActual;
    }

    public void setMedicoActual(Medico medicoActual) {
        this.medicoActual = medicoActual;
    }

    public List<Cita> getCitas() {
        return citas;
    }

    public void setCitas(List<Cita> citas) {
        this.citas = citas;
    }
    
    public List<Prescripcion> getPrescripciones() {
        return prescripciones;
    }

    public void setPrescripciones(List<Prescripcion> prescripciones) {
        this.prescripciones = prescripciones;
    }
    
    public List<Medicamento> getMedicamentos() {
        return medicamentos;
    }

    public void setMedicamentos(List<Medicamento> medicamentos) {
        this.medicamentos = medicamentos;
    }
    
    public Prescripcion getPrescripcionActual() {
        return prescripcionActual;
    }

    public void setPrescripcionActual(Prescripcion prescripcionActual) {
        this.prescripcionActual = prescripcionActual;
    }
    
    public Prescripcion getPrescripcionSeleccionada() {
        return prescripcionSeleccionada;
    }

    public void setPrescripcionSeleccionada(Prescripcion prescripcionSeleccionada) {
        this.prescripcionSeleccionada = prescripcionSeleccionada;
    }

    private boolean parametrosAccesoInvalidos() {
        return (((dni == null) && (numeroColegiado == null)) || (password == null));
    }

    private Medico recuperarDatosMedico() {
        Medico medico = null;
        if (dni != null) {
            medico = medicoDAO.buscarPorDNI(dni);
        }
        if ((medico == null) && (numeroColegiado != null)) {
            medico = medicoDAO.buscarPorNumeroColegiado(numeroColegiado);
        }
        return medico;
    }

    public String doLogin() {
        String destino = null;
        if (parametrosAccesoInvalidos()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No se ha indicado un DNI ó un número de colegiado o una contraseña", ""));
        } else {
            Medico medico = recuperarDatosMedico();
            if (medico == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No existe ningún médico con los datos indicados", ""));
            } else {
                if (autenticacionControlador.autenticarUsuario(medico.getId(), password,
                        TipoUsuario.MEDICO.getEtiqueta().toLowerCase())) {
                    medicoActual = medico;
                    
                    recuperarCitasMedico();
                    destino = "privado/index";
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Credenciales de acceso incorrectas", ""));
                }
            }
        }
        return destino;
    }
    
    public void recuperarCitasMedico() {
        Date fechaActual = new Date();
        
        if (citaDAO.buscarPorMedico(medicoActual,fechaActual) != null) {
            citas = citaDAO.buscarPorMedico(medicoActual,fechaActual);
        }
    }

    //Acciones
    public String doShowAgenda() {
        return "/medico/privado/agenda";
    }
    
    public String doShowCita(Cita cita) {
        
        citaActual = cita;
        
        Date fechaActual = new Date();
        // recuperar prescripciones del paciente asociado a la cita
        List<Prescripcion> listaPrescripciones = prescripcionDAO.buscarPorPaciente(cita.getPaciente().getId(),fechaActual);
        List<Medicamento> listaMedicamentos = medicamentoDAO.buscarTodos();
        
        if (listaPrescripciones != null) {
            prescripciones = listaPrescripciones;
        }
        if (listaMedicamentos != null) {
            medicamentos = listaMedicamentos;
        }
        return "/medico/privado/atencion_paciente";
    }
    
    public void setMedicamentoActual(Medicamento medicamento) {
        prescripcionActual.setMedicamento(medicamento);
    }
    
    public void setMedicamentoEditar(Medicamento medicamento) {
        prescripcionSeleccionada.setMedicamento(medicamento);
    }
    
    // Crear una prescripcion
    public void doAddPrescripcion() {
        if(prescripcionActual.getMedicamento()!=null) {
            Date fechaActual = new Date();
            if( prescripcionActual.getFechaFin() != null &&
                    (prescripcionActual.getFechaFin().after(fechaActual)) ) {
                if(prescripcionActual.getDosis() != null){
                    prescripcionActual.setFechaInicio(fechaActual);
                    prescripcionActual.setPaciente(citaActual.getPaciente());
                    prescripcionActual.setMedico(citaActual.getMedico());
                    prescripcionActual.setRecetas(doPlanRecetas());
                    prescripcionDAO.crear(prescripcionActual);
                    prescripciones = prescripcionDAO.buscarPorPaciente(citaActual.getPaciente().getId(),fechaActual);
                    prescripcionActual = new Prescripcion();
                }
                else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Introduzca la dosis diaria", ""));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Introduzca una fecha de fin válida", ""));
            }
        }
        else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Introduzca el medicamento a prescribir", ""));
        }
    }
    
    // Crear plan de recetas de la prescripcion a crear
    public List<Receta> doPlanRecetas() {
        List<Receta> recetas = new ArrayList<Receta>();
        int diasRestantes = calcularDiasPrescripcion();
        int dosisDiarias = prescripcionActual.getDosis();
        int dosisCaja = prescripcionActual.getMedicamento().getNumeroDosis();
        int dosisTotales = dosisDiarias * diasRestantes;
        int numRecetas = diasRestantes/7 + ((diasRestantes%7 == 0)?0:1); // 1 receta por semana
        // Primera receta con fecha de inicio de validez = fecha de inicio de la prescripcion
        int dosisSobrantes = 0;
        int dosisReceta = dosisDiarias * ((diasRestantes<7)?diasRestantes:7) - dosisSobrantes;
        int numCajasReceta = dosisReceta/dosisCaja + ((dosisReceta%dosisCaja==0)?0:1);
        Date fechaInicioValidezReceta = prescripcionActual.getFechaInicio();
        recetas.add(new Receta(prescripcionActual, numCajasReceta, fechaInicioValidezReceta, sumarDias(fechaInicioValidezReceta, 7), EstadoReceta.GENERADA));
        dosisSobrantes = dosisReceta%dosisCaja;
        diasRestantes -= 7;
        // Resto de recetas
        for(int i=1; i<numRecetas; i++) {
            dosisReceta = dosisDiarias * ((diasRestantes<7)?diasRestantes:7) - dosisSobrantes;
            numCajasReceta = dosisReceta/dosisCaja + ((dosisReceta%dosisCaja==0)?0:1);
            recetas.add(new Receta(prescripcionActual, numCajasReceta, fechaInicioValidezReceta, sumarDias(fechaInicioValidezReceta, 14), EstadoReceta.GENERADA));
            dosisSobrantes = dosisReceta%dosisCaja;
            fechaInicioValidezReceta = sumarDias(fechaInicioValidezReceta, 7);
            diasRestantes -= 7;
        }
        return recetas;
    }
    
    public static Date sumarDias(Date fecha, int dias) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(fecha);
		cal.add(Calendar.DATE, dias);
				
		return cal.getTime();
    }
    
    private int calcularDiasPrescripcion() {
        DateTime fechaInicio = new DateTime(prescripcionActual.getFechaInicio());
        DateTime fechaFin = new DateTime(prescripcionActual.getFechaFin());
        return Days.daysBetween(fechaInicio, fechaFin).getDays();
    }
    
    public void seleccionarPrescripcion(Prescripcion prescripcion) {
        prescripcionSeleccionada = prescripcion;
    }
    
    // Eliminar una prescripcion
    public void doEliminarPrescripcion() {
        prescripcionDAO.eliminar(prescripcionSeleccionada);
        Date fechaActual = new Date();
        prescripciones = prescripcionDAO.buscarPorPaciente(citaActual.getPaciente().getId(),fechaActual);
        prescripcionSeleccionada = new Prescripcion();
    }
    
    // Editar una prescripcion
    public void doEditarPrescripcion() {
        if(prescripcionSeleccionada.getMedicamento()!=null) {
            Date fechaActual = new Date();
            if(prescripcionSeleccionada.getFechaFin() != null &&
                    (prescripcionSeleccionada.getFechaFin().after(fechaActual) ) ) {
                if(prescripcionSeleccionada.getDosis() != null){
                    prescripcionSeleccionada.setFechaInicio(fechaActual);
                    prescripcionSeleccionada.setPaciente(citaActual.getPaciente());
                    prescripcionSeleccionada.setMedico(citaActual.getMedico());
                    prescripcionDAO.actualizar(prescripcionSeleccionada);
                    prescripciones = prescripcionDAO.buscarPorPaciente(citaActual.getPaciente().getId(),fechaActual);
                    prescripcionSeleccionada = new Prescripcion();
                }
                else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Introduzca la dosis diaria", ""));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Introduzca la fecha de fin", ""));
            }
        }
        else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Introduzca el medicamento a prescribir", ""));
        }
    }
    
    // Cambiar el estado de una cita
    public void doCambiarEstadoCitaActual(String estado) {
        switch(estado) {
            case "COMPLETADA":
                citaActual.setEstado(EstadoCita.COMPLETADA);
                break;
            case "AUSENTE":
                citaActual.setEstado(EstadoCita.AUSENTE);
                break;
        }
    }
    
    public String doEditarCita() {
        citaDAO.actualizar(citaActual);
        citaActual=null;
        return doShowAgenda();
    }

}
