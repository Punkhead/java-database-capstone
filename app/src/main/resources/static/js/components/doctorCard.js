import { openBookingOverlay } from "../loggedPatient.js";
import { deleteDoctor } from "../services/doctorServices.js";
import { getPatientData } from "../services/patientServices.js";

export function createDoctorCard(doctor) {
    const card = document.createElement("div");
    card.classList.add("doctor-card");

    const role = localStorage.getItem("userRole");

    const infoDiv = document.createElement("div");
    infoDiv.classList.add("doctor-info");

    const name = document.createElement("h3");
    name.textContent = doctor.name;

    const specialization = document.createElement("p");
    specialization.textContent = `Specialty: ${doctor.specialty}`;

    const email = document.createElement("p");
    email.textContent = `Email: ${doctor.email}`;

    const availability = document.createElement("p");
    availability.textContent =
        `Available Times: ${
            Array.isArray(doctor.availableTimes)
                ? doctor.availableTimes.join(", ")
                : doctor.availableTimes || "Not specified"
        }`;

    infoDiv.appendChild(name);
    infoDiv.appendChild(specialization);
    infoDiv.appendChild(email);
    infoDiv.appendChild(availability);

    const actionsDiv = document.createElement("div");
    actionsDiv.classList.add("card-actions");

    // ADMIN
    if (role === "admin") {
        const removeBtn = document.createElement("button");
        removeBtn.textContent = "Delete";
        removeBtn.classList.add("adminBtn");

        removeBtn.addEventListener("click", async () => {
            const confirmed = confirm(
                `Are you sure you want to delete Dr. ${doctor.name}?`
            );

            if (!confirmed) {
                return;
            }

            const token = localStorage.getItem("token");

            if (!token) {
                alert("Authentication token not found.");
                return;
            }

            const result = await deleteDoctor(doctor.id, token);

            alert(result.message);

            if (result.success) {
                card.remove();
            }
        });

        actionsDiv.appendChild(removeBtn);
    }

    // PATIENT - NOT LOGGED IN
    else if (role === "patient") {
        const bookNow = document.createElement("button");
        bookNow.textContent = "Book Now";
        bookNow.classList.add("adminBtn");

        bookNow.addEventListener("click", () => {
            alert("Patient needs to login first.");
        });

        actionsDiv.appendChild(bookNow);
    }

    // LOGGED-IN PATIENT
    else if (role === "loggedPatient") {
        const bookNow = document.createElement("button");
        bookNow.textContent = "Book Now";
        bookNow.classList.add("adminBtn");

        bookNow.addEventListener("click", async (e) => {
            const token = localStorage.getItem("token");

            if (!token) {
                alert("Session expired. Please log in again.");
                window.location.href = "/";
                return;
            }

            try {
                const patientData = await getPatientData(token);

                openBookingOverlay(e, doctor, patientData);
            } catch (error) {
                console.error("Error fetching patient data:", error);
                alert("Unable to fetch patient information.");
            }
        });

        actionsDiv.appendChild(bookNow);
    }

    card.appendChild(infoDiv);
    card.appendChild(actionsDiv);

    return card;
}