package edu.rmit.cosc1295.carehome;

import java.io.Serializable;

public class Prescription implements Serializable {

    private final String doctorId; // Doctor's ID
    private final String medicine; // Name of the medicine
    private final String dose; // Specific amount of dose
    private final String time; // Specific time to take medicine

    /**
     * Construct them with id, medicine name, dose and time
     * @param doctorId // The ID of doctor
     * @param medicine // The name of medicine
     * @param dose // Amount of doses
     * @param time // What time to take med
     */

    public Prescription(String doctorId, String medicine, String dose, String time) {
        this.doctorId = doctorId;
        this.medicine = medicine;
        this.dose = dose;
        this.time = time;
    }


    /**
     * Get the ID of doctor
     * @return the ID of doctor
     */

    public String getDoctorId() {
        return this.doctorId;
    }


    /**
     * Get the name of medicine
     * @return the name of medicine
     */

    public String getMedicine() {
        return this.medicine;
    }


    /**
     * Get the amount of dose
     * @return the amount of dose
     */

    public String getDose() {
        return this.dose;
    }


    /**
     * Get the specific time to take medicine
     * @return specific time
     */

    public String getTime() {
        return this.time;
    }


    @Override
    public String toString() {
        return this.medicine + " (" + this.dose + ") at " + this.time
                + "\n Don't forget to take it!!!";
    }
}
