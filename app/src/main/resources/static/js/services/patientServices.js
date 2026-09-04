import { API_BASE_URL } from "../config/config.js";

const PATIENT_API = API_BASE_URL + "/patient";

export async function patientSignup(data) {
  try {
    // Send patient registration data
    const response = await fetch(`${PATIENT_API}/signup`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(data)
    });

    const result = await response.json();

    return {
      success: response.ok,
      message: result.message
    };
  } catch (error) {
    console.error("Error during patient signup:", error);

    return {
      success: false,
      message: "An error occurred during signup."
    };
  }
}

export async function patientLogin(data) {
  try {
    // Send patient login credentials
    const response = await fetch(`${PATIENT_API}/login`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(data)
    });

    return response;
  } catch (error) {
    console.error("Error during patient login:", error);
    return null;
  }
}

export async function getPatientData(token) {
  try {
    // Fetch logged-in patient information
    const response = await fetch(`${PATIENT_API}/${token}`);

    if (!response.ok) {
      console.error("Failed to fetch patient data:", response.status);
      return null;
    }

    const data = await response.json();

    return data.patient;
  } catch (error) {
    console.error("Error fetching patient data:", error);
    return null;
  }
}

export async function getPatientAppointments(id, token, user) {
  try {
    // Fetch appointments for patient or doctor
    const response = await fetch(
        `${PATIENT_API}/appointments/${id}/${token}/${user}`
    );

    if (!response.ok) {
      console.error(
          "Failed to fetch patient appointments:",
          response.status
      );
      return null;
    }

    const data = await response.json();

    return data.appointments;
  } catch (error) {
    console.error("Error fetching patient appointments:", error);
    return null;
  }
}

export async function filterAppointments(condition, name, token) {
  try {
    // Fetch filtered appointments
    const response = await fetch(
        `${PATIENT_API}/appointments/${condition}/${name}/${token}`
    );

    if (!response.ok) {
      console.error(
          "Failed to filter appointments:",
          response.status
      );
      return [];
    }

    const data = await response.json();

    return data.appointments || [];
  } catch (error) {
    console.error("Error filtering appointments:", error);
    alert("An error occurred while filtering appointments.");
    return [];
  }
}