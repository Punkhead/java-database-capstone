import { getAllAppointments } from "./services/appointmentRecordService.js";
import { createPatientRow } from "./components/patientRows.js";

const patientTableBody = document.getElementById("patientTableBody");

let selectedDate = new Date().toISOString().split("T")[0];
const token = localStorage.getItem("token");
let patientName = null;


document.addEventListener("DOMContentLoaded", () => {

    const searchBar = document.getElementById("searchBar");
    const todayButton = document.getElementById("todayAppointmentsButton");
    const datePicker = document.getElementById("appointmentDate");

    if (datePicker) {
        datePicker.value = selectedDate;
    }

    if (searchBar) {
        searchBar.addEventListener("input", () => {
            patientName = searchBar.value.trim() || "null";
            loadAppointments();
        });
    }

    if (todayButton) {
        todayButton.addEventListener("click", () => {
            selectedDate = new Date().toISOString().split("T")[0];

            if (datePicker) {
                datePicker.value = selectedDate;
            }

            loadAppointments();
        });
    }

    if (datePicker) {
        datePicker.addEventListener("change", () => {
            selectedDate = datePicker.value;
            loadAppointments();
        });
    }

    if (typeof renderContent === "function") {
        renderContent();
    }

    loadAppointments();
});


async function loadAppointments() {
    if (!patientTableBody) {
        return;
    }

    patientTableBody.innerHTML = "";

    try {
        const appointments = await getAllAppointments(
            selectedDate,
            patientName,
            token
        );

        if (!appointments || appointments.length === 0) {
            const row = document.createElement("tr");

            const cell = document.createElement("td");
            cell.colSpan = 5;
            cell.textContent = "No Appointments found for today";
            cell.classList.add("noPatientRecord");

            row.appendChild(cell);
            patientTableBody.appendChild(row);

            return;
        }

        appointments.forEach(appointment => {
            const patient = appointment.patient;

            const row = createPatientRow(patient, appointment);

            if (row) {
                patientTableBody.appendChild(row);
            }
        });

    } catch (error) {
        console.error("Error loading appointments:", error);

        const row = document.createElement("tr");

        const cell = document.createElement("td");
        cell.colSpan = 5;
        cell.textContent = "Failed to load appointments.";
        cell.classList.add("noPatientRecord");

        row.appendChild(cell);
        patientTableBody.appendChild(row);
    }
}