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
import es.uvigo.esei.dagss.dominio.entidades.Medicamento;
import es.uvigo.esei.dagss.dominio.entidades.Medico;
import es.uvigo.esei.dagss.dominio.entidades.Prescripcion;
import es.uvigo.esei.dagss.dominio.entidades.TipoUsuario;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

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
    
    public void doAddPrescripcion() {
        if(prescripcionActual.getMedicamento()!=null) {
            if(prescripcionActual.getFechaFin() != null) {
                if(prescripcionActual.getDosis() != null){
                    Date fechaActual = new Date();
                    prescripcionActual.setFechaInicio(fechaActual);
                    prescripcionActual.setPaciente(citaActual.getPaciente());
                    prescripcionActual.setMedico(citaActual.getMedico());
                    prescripcionDAO.crear(prescripcionActual);
                    prescripciones = prescripcionDAO.buscarPorPaciente(citaActual.getPaciente().getId(),fechaActual);
                    prescripcionActual = new Prescripcion();
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
    
    public void seleccionarPrescripcion(Prescripcion prescripcion) {
        prescripcionSeleccionada = prescripcion;
        System.out.println(prescripcionSeleccionada.getMedicamento().getNombre());
    }
    
    public void doEliminarPrescripcion() {
        prescripcionDAO.eliminar(prescripcionSeleccionada);
        Date fechaActual = new Date();
        prescripciones = prescripcionDAO.buscarPorPaciente(citaActual.getPaciente().getId(),fechaActual);
        prescripcionSeleccionada = new Prescripcion();
    }
    
    public void doEditarPrescripcion() {
        if(prescripcionSeleccionada.getMedicamento()!=null) {
            if(prescripcionSeleccionada.getFechaFin() != null) {
                if(prescripcionSeleccionada.getDosis() != null){
                    Date fechaActual = new Date();
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

}
